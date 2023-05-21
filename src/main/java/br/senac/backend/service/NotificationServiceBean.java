package br.senac.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.senac.backend.model.Pet;

@Service
public class NotificationServiceBean implements NotificationService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PetService petService;

	@Autowired
	private PreferencesService preferencesService;

	public void makeNotification(String petGuid) {
		try {
			Pet pet = petService.getByGuid(petGuid);
			if (pet != null) {
				List<String> emails = preferencesService.findPreferences(pet.getAge(), pet.getSize(), pet.getBreed(),
						pet.getTypePet(), pet.getGender());
				for (String email : emails) {
					SimpleMailMessage msg = new SimpleMailMessage();
					msg.setTo(email);
					msg.setSubject("Alerta de Novos Pets - 4PET");
					msg.setText(
							"Foi encontrado um Pet de acordo com suas preferências.\nAcesse já o aplicativo!\nAtenciosamente,\nEquipe 4PET.");
					javaMailSender.send(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
