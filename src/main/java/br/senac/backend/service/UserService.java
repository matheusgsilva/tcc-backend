package br.senac.backend.service;

import br.senac.backend.model.User;

public interface UserService {

	User getByGuid(String guid);
	
	User save(User user);
	
	User getByLoginPassword(String username, String password);
	
	Boolean isExists(String email);
	
	Boolean isExists(String email, String guid);
	
	User locateByEmail(String email);
	
	void delete(User user);
}
