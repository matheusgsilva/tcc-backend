package br.senac.backend.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sun.istack.ByteArrayDataSource;

import br.senac.backend.model.Pet;
import br.senac.backend.model.Preferences;
import br.senac.backend.model.User;

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
				List<String> emails = preferencesService.findPreferences(pet.getSize(), pet.getBreed(),
						pet.getTypePet(), pet.getGender());
				for (String email : emails) {
					MimeMessage message = javaMailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true);
					helper.setTo(email);
					helper.setSubject("Alerta de Novos Pets - 4PET");

					ClassPathResource watermarkResource = new ClassPathResource("logo.png");

					BufferedImage watermarkImage = ImageIO.read(watermarkResource.getInputStream());
					int newWidth = 200;
					int newHeight = (int) ((double) watermarkImage.getHeight() / watermarkImage.getWidth() * newWidth);
					Image scaledWatermark = watermarkImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
					BufferedImage resizedWatermark = new BufferedImage(newWidth, newHeight,
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2d = resizedWatermark.createGraphics();
					g2d.drawImage(scaledWatermark, 0, 0, null);
					g2d.dispose();

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(resizedWatermark, "png", baos);
					byte[] watermarkBytes = baos.toByteArray();

					helper.setText(
							"<html><body><h1>Foi encontrado um Pet de acordo com suas preferências.</h1><p>Acesse já o aplicativo!</p><p>Atenciosamente,</p><p>Equipe 4PET.</p><br><img src='cid:watermark'></body></html>",
							true);
					helper.addInline("watermark", new ByteArrayDataSource(watermarkBytes, "image/png"));

					javaMailSender.send(message);
				}
			}
		} catch (IOException | MessagingException e) {
			e.printStackTrace();
		}
	}

	public void makeNotificationPreferences(String preferencesGuid, String email) {
		try {
			Preferences preferences = preferencesService.getByGuid(preferencesGuid);
			if (preferences != null) {
				if (petService.isExists(preferences.getSize(), preferences.getBreed(), preferences.getTypePet(),
						preferences.getGender())) {
					MimeMessage message = javaMailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message, true);
					helper.setTo(email);
					helper.setSubject("Alerta de Novos Pets - 4PET");

					ClassPathResource watermarkResource = new ClassPathResource("logo.png");

					BufferedImage watermarkImage = ImageIO.read(watermarkResource.getInputStream());
					int newWidth = 200;
					int newHeight = (int) ((double) watermarkImage.getHeight() / watermarkImage.getWidth() * newWidth);
					Image scaledWatermark = watermarkImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
					BufferedImage resizedWatermark = new BufferedImage(newWidth, newHeight,
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2d = resizedWatermark.createGraphics();
					g2d.drawImage(scaledWatermark, 0, 0, null);
					g2d.dispose();

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(resizedWatermark, "png", baos);
					byte[] watermarkBytes = baos.toByteArray();

					helper.setText(
							"<html><body><h1>Foi encontrado Pets de acordo com as preferências criadas.</h1><p>Acesse já o aplicativo!</p><p>Atenciosamente,</p><p>Equipe 4PET.</p><br><img src='cid:watermark'></body></html>",
							true);
					helper.addInline("watermark", new ByteArrayDataSource(watermarkBytes, "image/png"));

					javaMailSender.send(message);
				}
			}
		} catch (IOException | MessagingException e) {
			e.printStackTrace();
		}
	}

	public void makeNotificationReservePet(Pet pet, long daysRemaining) {
		try {
			if (pet != null) {
				User adopterUser = pet.getAdopterUser();
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setTo(adopterUser.getEmail());
				helper.setSubject("Alerta de Reserva de Pet - 4PET");

				ClassPathResource watermarkResource = new ClassPathResource("logo.png");

				BufferedImage watermarkImage = ImageIO.read(watermarkResource.getInputStream());
				int newWidth = 200;
				int newHeight = (int) ((double) watermarkImage.getHeight() / watermarkImage.getWidth() * newWidth);
				Image scaledWatermark = watermarkImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
				BufferedImage resizedWatermark = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = resizedWatermark.createGraphics();
				g2d.drawImage(scaledWatermark, 0, 0, null);
				g2d.dispose();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(resizedWatermark, "png", baos);
				byte[] watermarkBytes = baos.toByteArray();

				String messageContent = String.format(
						"<html><body><h1>Você tem %d dias restantes na sua reserva do pet de identificação: "
								+ pet.getIdentification()
								+ ".</h1><p>Acesse o aplicativo para mais detalhes!</p><p>Atenciosamente,</p><p>Equipe 4PET.</p><br><img src='cid:watermark'></body></html>",
						daysRemaining);

				helper.setText(messageContent, true);
				helper.addInline("watermark", new ByteArrayDataSource(watermarkBytes, "image/png"));

				javaMailSender.send(message);
			}
		} catch (IOException | MessagingException e) {
			e.printStackTrace();
		}
	}

	public void makeNotificationReserveExpired(Pet pet) {
		try {
			if (pet != null) {
				User adopterUser = pet.getAdopterUser();
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setTo(adopterUser.getEmail());
				helper.setSubject("Alerta de Expiração de Reserva de Pet - 4PET");

				ClassPathResource watermarkResource = new ClassPathResource("logo.png");

				BufferedImage watermarkImage = ImageIO.read(watermarkResource.getInputStream());
				int newWidth = 200;
				int newHeight = (int) ((double) watermarkImage.getHeight() / watermarkImage.getWidth() * newWidth);
				Image scaledWatermark = watermarkImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
				BufferedImage resizedWatermark = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = resizedWatermark.createGraphics();
				g2d.drawImage(scaledWatermark, 0, 0, null);
				g2d.dispose();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(resizedWatermark, "png", baos);
				byte[] watermarkBytes = baos.toByteArray();

				helper.setText("<html><body><h2>Cancelamento de Reserva</h2>"
	                    + "<p>Olá, " + adopterUser.getName() + "!</p>"
	                    + "<p>Lamentamos informar que a reserva do pet de identificação <strong>" + pet.getIdentification() + "</strong> foi cancelada.</p>"
	                    + "<p>Se tiver dúvidas ou precisar de mais informações, entre em contato conosco.</p>"
	                    + "<p>Atenciosamente,</p>"
	                    + "<p>Sistema 4PET.</p>"
	                    + "<br><img src='cid:banner'></body></html>", true);
				helper.addInline("watermark", new ByteArrayDataSource(watermarkBytes, "image/png"));

				javaMailSender.send(message);
			}
		} catch (IOException | MessagingException e) {
			e.printStackTrace();
		}
	}
}
