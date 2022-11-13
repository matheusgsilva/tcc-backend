package br.senac.backend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

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
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(user.getEmail());
			msg.setSubject("Avaliação de Serviço - 4PET");
			msg.setText("O que você achou do processo de adoção?\nAvalie já os serviços prestados pela Organização - "
					+ company.getName() + "\n Segue o link para avaliação: " + this.getUrl() + company.getGuid()
					+ "\nAtenciosamente,\nEquipe 4PET.");
			javaMailSender.send(msg);
		}
	}
}