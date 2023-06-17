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

import br.senac.backend.model.Company;

@Component
public class EmailAccessTask implements Runnable {

	@Autowired
	private JavaMailSender javaMailSender;

	private Company company;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	 public void run() {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(company.getEmail());
            helper.setSubject("Alerta de Acesso - 4PET");

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

            helper.setText("<html><body><h1>Seu acesso foi liberado no aplicativo.</h1><p>Faça login e insira já os pets para a adoção!</p><p>Atenciosamente,</p><p>Equipe 4PET.</p><br><img src='cid:watermark'></body></html>", true);
            helper.addInline("watermark", new ByteArrayDataSource(watermarkBytes, "image/png"));

            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}