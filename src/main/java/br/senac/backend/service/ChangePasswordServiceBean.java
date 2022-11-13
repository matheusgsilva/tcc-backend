package br.senac.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.senac.backend.model.ChangePassword;
import br.senac.backend.repository.ChangePasswordRepository;
import br.senac.backend.util.EACTIVE;

@Service
public class ChangePasswordServiceBean implements ChangePasswordService {

	@Autowired
	private ChangePasswordRepository changePasswordRepository;

	@Override
	public ChangePassword save(ChangePassword changePassword) {
		return changePasswordRepository.save(changePassword);
	}

	@Override
	public ChangePassword findByGuid(String guid) {
		return changePasswordRepository.findByGuid(guid, EACTIVE.YES);
	}

	@Override
	public ChangePassword findByUserGuid(String userGuid) {
		return changePasswordRepository.findByUserGuid(userGuid, EACTIVE.YES);
	}
}