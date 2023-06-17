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

@Component
public class EmailPasswordTask implements Runnable {

	@Autowired
	private JavaMailSender javaMailSender;

	private String receiver;
	private String url;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void run() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setText("Segue o link de Recuperação de Senha");
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(this.getReceiver());
            helper.setSubject("Segue o link de Recuperação de Senha");

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

            helper.setText("<html><body><h1>Segue o link de Recuperação de Senha</h1><p>" + this.getUrl() + "</p><br><img src='cid:watermark'></body></html>", true);
            helper.addInline("watermark", new ByteArrayDataSource(watermarkBytes, "image/png"));

            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}