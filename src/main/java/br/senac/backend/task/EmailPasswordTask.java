package br.senac.backend.task;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

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
			helper.setText(this.getUrl(), true);
			javaMailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}