package br.senac.backend.service;

import br.senac.backend.model.Company;
import br.senac.backend.model.Token;
import br.senac.backend.model.User;

public interface TokenService {

	Token getByToken(String token);

	boolean validToken(String token);

	void delete(Long id);

	Token save(Token obj);

	Token getNewTokenPersisted(User user);
	
	Token getNewTokenPersisted(Company company);

	Token getLastTokenForToday(Token tbToken);
	
	void delete(User user);
	
	void delete(Company company);
}
