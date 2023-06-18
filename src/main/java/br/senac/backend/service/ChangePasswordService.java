package br.senac.backend.service;

import java.util.List;

import br.senac.backend.model.ChangePassword;

public interface ChangePasswordService {

	ChangePassword save(ChangePassword changePassword);
	
	ChangePassword findByGuid(String guid);
	
	ChangePassword findByUserGuid(String userGuid);
	
	List<ChangePassword> findItemsByUserGuid(String userGuid);
	
	void delete(ChangePassword changePassword);
}
