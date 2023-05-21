package br.senac.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.senac.backend.model.Pet;
import br.senac.backend.model.Preferences;

@Service
public class NotificationServiceBean implements NotificationService {
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PetService petService;

	@Autowired
	private PreferencesService preferencesService;

	public void makeNotification(String petGuid) {
		Pet pet = petService.getByGuid(petGuid);
		if (pet != null) {
			List<Preferences> listOfPreferences = preferencesService.findPreferences(pet.getAge(), pet.getSize(),
					pet.getBreed(), pet.getTypePet(), pet.getGender());
			for (Preferences preferences : listOfPreferences) {
				SimpleMailMessage msg = new SimpleMailMessage();
				msg.setTo(preferences.getUser().getEmail());
				msg.setSubject("Alerta de Novos Pets - 4PET");
				msg.setText(
						"Foi encontrado um Pet de acordo com suas preferências.\nAcesse já o aplicativo!\nAtenciosamente,\nEquipe 4PET.");
				javaMailSender.send(msg);
			}
		}
	}
}
