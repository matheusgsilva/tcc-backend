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

import br.senac.backend.converter.NotificationConverter;
import br.senac.backend.model.Company;
import br.senac.backend.model.User;
import br.senac.backend.service.CompanyService;
import br.senac.backend.service.UserService;

@Component
public class EmailRatingTask implements Runnable {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private NotificationConverter notificationConverter;

	private String companyGuid;
	private String email;
	private String url;

	public String getCompanyGuid() {
		return companyGuid;
	}

	public void setCompanyGuid(String companyGuid) {
		this.companyGuid = companyGuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		User user = userService.locateByEmail(this.getEmail());
		Company company = companyService.getByGuid(this.getCompanyGuid());

		if (user != null && company != null) {
			try {

				notificationConverter.saveNotification("Avaliação de Serviço - 4PET",
						"O que você achou do processo de adoção? Avalie já os serviços prestados pela Organização - "
								+ company.getName()
								+ ". O link para avaliação foi enviado por e-mail. Atenciosamente, Equipe 4PET.",
						user.getGuid(), company);

				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setTo(user.getEmail());
				helper.setSubject("Avaliação de Serviço - 4PET");

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

				helper.setText(
						"<html><body><h1>O que você achou do processo de adoção?</h1><p>Avalie já os serviços prestados pela Organização - "
								+ company.getName() + "</p><p>Segue o link para avaliação: " + this.getUrl()
								+ company.getGuid()
								+ "</p><p>Atenciosamente,</p><p>Equipe 4PET.</p><br><img src='cid:watermark'></body></html>",
						true);
				helper.addInline("watermark", new ByteArrayDataSource(watermarkBytes, "image/png"));

				javaMailSender.send(message);
			} catch (MessagingException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}