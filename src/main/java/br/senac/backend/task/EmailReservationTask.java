package br.senac.backend.task;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.sun.istack.ByteArrayDataSource;

import br.senac.backend.model.Pet;
import br.senac.backend.model.User;

@Component
public class EmailReservationTask implements Runnable {

	@Autowired
	private JavaMailSender javaMailSender;

	private Pet pet;
	private User user;

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void run() {
		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(user.getEmail());
			helper.setSubject("Confirmação de Reserva - 4PET");

			ClassPathResource bannerResource = new ClassPathResource("logo.png");

			BufferedImage bannerImage = ImageIO.read(bannerResource.getInputStream());
			int newWidth = 200;
			int newHeight = (int) ((double) bannerImage.getHeight() / bannerImage.getWidth() * newWidth);
			Image scaledBanner = bannerImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			BufferedImage resizedBanner = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = resizedBanner.createGraphics();
			g2d.drawImage(scaledBanner, 0, 0, null);
			g2d.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resizedBanner, "png", baos);
			byte[] bannerBytes = baos.toByteArray();

			helper.setText("<html><body><h2>Reserva Confirmada!</h2>" + "<p>Olá, " + user.getName() + "!</p>"
					+ "<p>Estamos felizes em informar que a reserva do pet de identificação <strong>" + pet.getIdentification()
					+ "</strong> foi realizada com sucesso.</p>" + "<p>Atenciosamente,</p>" + "<p>Sistema 4PET.</p>"
					+ "<br><img src='cid:watermark'></body></html>", true);
			helper.addInline("banner", new ByteArrayDataSource(bannerBytes, "image/png"));

			javaMailSender.send(message);
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}
}