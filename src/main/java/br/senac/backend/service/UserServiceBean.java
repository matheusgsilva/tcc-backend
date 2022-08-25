package br.senac.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senac.backend.model.User;
import br.senac.backend.repository.UserRepository;
import br.senac.backend.util.EACTIVE;

@Service
public class UserServiceBean implements UserService {

	@Autowired
	private UserRepository repository;

	public User getByGuid(String guid) {
		if (guid == null)
			return null;
		return repository.getByGuid(guid);
	}

	@Transactional
	public User save(User user) {
		return repository.save(user);
	}

	public User getByLoginPassword(String email, String password) {
		if ((!email.equals("")) && (!password.equals(""))) {
			User u = repository.getByEmail(email);
			if (u != null)
				if (u.getActive().equals(EACTIVE.YES))
					if (BCrypt.checkpw(password, u.getPassword()))
						return u;
		}
		return null;
	}

	public Boolean isExists(String email) {
		return repository.isExists(email);
	}
	
	public Boolean isExists(String email, String guid) {
		return repository.isExists(email, guid);
	}

	public User locateByEmail(String email) {
		return repository.getByEmail(email);
	}

	@Transactional
	public void delete(User user) {
		repository.delete(user);
	}
}