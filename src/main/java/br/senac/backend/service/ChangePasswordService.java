package br.senac.backend.service;

import br.senac.backend.model.ChangePassword;

public interface ChangePasswordService {

	ChangePassword save(ChangePassword changePassword);
	
	ChangePassword findByGuid(String guid);
	
	ChangePassword findByUserGuid(String userGuid);
}
