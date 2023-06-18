package br.senac.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senac.backend.model.User;
import br.senac.backend.repository.UserRepository;

@Service
public class UserServiceBean implements UserService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PreferencesService preferencesService;

	@Autowired
	private RatingService ratingService;
	
	@Autowired
	private TokenService tokenService;

	@Autowired
	private ChangePasswordService changePasswordService;

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

	public List<User> getAll() {
		return repository.getAll();
	}

	@Transactional
	public void delete(User user) {
		preferencesService.getByUserGuid(user.getGuid()).forEach(item -> {
			preferencesService.delete(item);
		});
		ratingService.delete(user);
		changePasswordService.findItemsByUserGuid(user.getGuid()).forEach(item -> {
			changePasswordService.delete(item);
		});
		tokenService.delete(user);
		user.setFavoritePets(null);
		user = save(user);
		repository.delete(user);
	}
}