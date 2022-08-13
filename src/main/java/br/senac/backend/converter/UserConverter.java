package br.senac.backend.converter;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import br.senac.backend.model.User;
import br.senac.backend.request.UserRequest;
import br.senac.backend.response.UserResponse;
import br.senac.backend.util.EACTIVE;

@Component
public class UserConverter {

	public User userSave(UserRequest userRequest) {

		try {
			User user = new User();
			user.setActive(EACTIVE.YES);
			user.setDocument(userRequest.getDocument());
			user.setEmail(userRequest.getEmail());
			user.setGuid(UUID.randomUUID().toString());
			user.setName(userRequest.getName());
			user.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
			user.setPhone(userRequest.getPhone());
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public User userUpdate(UserRequest userRequest, User user) {

		try {
			user.setDocument(userRequest.getDocument());
			user.setEmail(userRequest.getEmail());
			user.setName(userRequest.getName());
			user.setPhone(userRequest.getPhone());
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public UserResponse userToResponse(User user) {

		try {
			UserResponse userResponse = new UserResponse();
			userResponse.setDocument(user.getDocument());
			userResponse.setEmail(user.getEmail());
			userResponse.setName(user.getName());
			userResponse.setPhone(user.getPhone());
			userResponse.setGuid(user.getGuid());
			return userResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}