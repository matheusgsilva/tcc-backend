package br.senac.backend.converter;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.senac.backend.model.ChangePassword;
import br.senac.backend.model.Company;
import br.senac.backend.model.User;
import br.senac.backend.request.ChangePasswordRequest;
import br.senac.backend.service.ChangePasswordService;
import br.senac.backend.util.EACTIVE;

@Component
public class ChangePasswordConverter {

	@Autowired
	private ChangePasswordService changePasswordService;

	public ChangePassword passwordToSave(ChangePasswordRequest changePasswordRequest, User user, Company company) {

		try {
			ChangePassword changePassword = changePasswordService.findByUserGuid(user != null ? user.getGuid() : company.getGuid());
			if (changePassword != null) {
				changePassword.setActive(EACTIVE.NO);
				changePassword = changePasswordService.save(changePassword);
			}
			changePassword = new ChangePassword();
			changePassword.setActive(EACTIVE.YES);
			changePassword.setEmail(changePasswordRequest.getEmail());
			changePassword.setGuid(UUID.randomUUID().toString());
			changePassword.setUserGuid(user != null ? user.getGuid() : company.getGuid());
			return changePassword;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}