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
			user.setDocumento(userRequest.getDocumento());
			user.setEmail(userRequest.getEmail());
			user.setGuid(UUID.randomUUID().toString());
			user.setNome(userRequest.getNome());
			user.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
			user.setTelefone(userRequest.getTelefone());
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public User userUpdate(UserRequest userRequest, User user) {

		try {
			user.setDocumento(userRequest.getDocumento());
			user.setEmail(userRequest.getEmail());
			user.setNome(userRequest.getNome());
			user.setTelefone(userRequest.getTelefone());
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public UserResponse userToResponse(User user) {

		try {
			UserResponse userResponse = new UserResponse();
			userResponse.setDocumento(user.getDocumento());
			userResponse.setEmail(user.getEmail());
			userResponse.setNome(user.getNome());
			userResponse.setTelefone(user.getTelefone());
			userResponse.setGuid(user.getGuid());
			return userResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}