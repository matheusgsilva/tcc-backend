package br.senac.backend.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senac.backend.model.Notification;
import br.senac.backend.model.Pet;
import br.senac.backend.model.Preferences;
import br.senac.backend.repository.NotificationRepository;

@Service
public class NotificationServiceBean implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private PetService petService;

	@Autowired
	private PreferencesService preferencesService;

	@Transactional
	public Notification save(Notification notification) {
		return notificationRepository.save(notification);
	}

	public List<Notification> getByUserGuid(String userGuid) {
		return notificationRepository.getByUserGuid(userGuid, PageRequest.of(0, 60));
	}

	public void makeNotification(String petGuid) {
		Pet pet = petService.getByGuid(petGuid);
		if (pet != null) {
			List<Preferences> listOfPreferences = preferencesService.findPreferences(pet.getAge(), pet.getSize(),
					pet.getBreed(), pet.getTypePet(), pet.getGender());
			for (Preferences preferences : listOfPreferences) {
				Notification notification = new Notification();
				notification.setDate(new Date());
				notification.setGuid(UUID.randomUUID().toString());
				notification.setPet(pet);
				notification.setUser(preferences.getUser());
				save(notification);
			}
		}
	}
}
