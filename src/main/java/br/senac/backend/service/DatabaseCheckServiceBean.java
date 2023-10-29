package br.senac.backend.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.senac.backend.model.Company;
import br.senac.backend.model.Pet;
import br.senac.backend.util.ESTATUS_PET;

@Service
public class DatabaseCheckServiceBean {

	@Autowired
	private PetService petService;

	@Autowired
	private NotificationService notificationService;

	@Async
//	@Scheduled(cron = "0 0 8 * * ?") // Executa todos os dias as 8 horas da manhã
	@Scheduled(fixedRate = 60000) // Executa a cada 60 segundos
	public void notifyUserForPetReservation() {
		System.out.println("Verificando reservas de pet...");
		List<Pet> reservedPets = petService.getByStatus(ESTATUS_PET.RESERVED);

		for (Pet pet : reservedPets) {
			Company company = pet.getCompany();
			Date reservationDate = pet.getReservationDate();
			long daysSinceReservation = Duration.between(reservationDate.toInstant(), Instant.now()).toDays();
			long daysRemaining = company.getDaysPetReservation() - daysSinceReservation;

			if (daysRemaining <= 0) {
				notificationService.makeNotificationReserveExpired(pet);
				petService.updateStatusPets(pet.getGuid());
			} else if (daysRemaining > 0) {
				notificationService.makeNotificationReservePet(pet, daysRemaining);
			}
		}
		System.out.println("Verificação de reservas de pet realizadas.");
	}

}