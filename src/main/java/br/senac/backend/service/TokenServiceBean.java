package br.senac.backend.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.senac.backend.model.Company;
import br.senac.backend.model.Token;
import br.senac.backend.model.User;
import br.senac.backend.repository.TokenRepository;

@Service
public class TokenServiceBean implements TokenService {

	@Autowired
	private TokenRepository repository;

	public Token getByToken(String token) {
		return repository.getByToken(token);
	}

	public boolean validToken(String token) {

		if (token == null || token.trim().isEmpty())
			return false;

		Token tbToken = this.getByToken(token);
		if (tbToken == null || tbToken.getCreatedDate() == null || tbToken.getId() == null)
			return false;

		Token tokenDay = this.getLastTokenForToday(tbToken);
		if (tokenDay != null) {
			if (!tbToken.getToken().equals(tokenDay.getToken())) {
				return false;
			}
		}

		SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		if (!fmt.format(tbToken.getCreatedDate()).equals(fmt.format(new Date())))
			return false;

		return true;
	}

	@Transactional
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Transactional
	public Token save(Token obj) {
		return repository.save(obj);
	}

	public Token getLastTokenForToday(Token tbToken) {

		if (tbToken.getUser() != null) {
			Token token = repository.getLastUserTokenForToday(tbToken.getUser());
			if (token != null)
				return token;
		}

		if (tbToken.getCompany() != null) {
			Token token = repository.getLastCompanyTokenForToday(tbToken.getCompany());
			if (token != null)
				return token;
		}

		return null;
	}

	public Token getLastCompanyTokenForToday(Company company) {
		return repository.getLastCompanyTokenForToday(company);
	}

	@Transactional
	public Token getNewTokenPersisted(User user) {
		Token token = new Token();
		token.setCreatedDate(new Date());
		token.setUser(user);
		token.setToken(getNewToken());
		token = save(token);
		return token;
	}

	public String getNewToken() {
		return UUID.randomUUID().toString();
	}
}