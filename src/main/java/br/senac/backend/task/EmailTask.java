package br.senac.backend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import br.senac.backend.model.Company;

@Component
public class EmailTask implements Runnable {

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
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(company.getEmail());
		msg.setSubject("Alerta de Acesso - 4PET");
		msg.setText(
				"Seu acesso foi liberado no aplicativo.\nFaça login e insira já os pets para a adoção!\nAtenciosamente,\nEquipe 4PET.");
		javaMailSender.send(msg);
	}
}