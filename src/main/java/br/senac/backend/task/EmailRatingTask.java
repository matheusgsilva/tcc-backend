package br.senac.backend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import br.senac.backend.model.Company;
import br.senac.backend.model.User;

@Component
public class EmailRatingTask implements Runnable {

	@Autowired
	private JavaMailSender javaMailSender;

	private Company company;
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public void run() {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(user.getEmail());
		msg.setSubject("Avaliação de Serviço - 4PET");
		msg.setText(
				"O que você achou do processo de adoção?\nAvalie já os serviços prestados pela Organização - " + company.getName() + ""
						+ "\n Segue o link para avaliação: " + "" +"\nAtenciosamente,\nEquipe 4PET.");
		javaMailSender.send(msg);
	}
}