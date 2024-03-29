package br.senac.backend.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import br.senac.backend.model.User;
import br.senac.backend.request.UserRequest;
import br.senac.backend.response.UserResponse;
import br.senac.backend.util.ETYPE_USER;

@Component
public class UserConverter {

	public User userSave(UserRequest userRequest) {

		try {
			User user = new User();
			user.setDocument(userRequest.getDocument());
			user.setEmail(userRequest.getEmail());
			user.setGuid(UUID.randomUUID().toString());
			user.setName(userRequest.getName());
			user.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
			user.setPhone(userRequest.getPhone());
			if (userRequest.getIsAdmin() == null || !userRequest.getIsAdmin())
				user.setType(ETYPE_USER.NORMAL);
			else
				user.setType(ETYPE_USER.BACKOFFICE);
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
			if (userRequest.getIsAdmin() == null || !userRequest.getIsAdmin())
				user.setType(ETYPE_USER.NORMAL);
			else
				user.setType(ETYPE_USER.BACKOFFICE);
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
			userResponse.setIsAdmin(user.getType().equals(ETYPE_USER.NORMAL) ? false : true);
			return userResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<UserResponse> userToResponseList(List<User> users) {

		try {

			List<UserResponse> list = new ArrayList<UserResponse>();
			for (User user : users) {
				UserResponse userResponse = new UserResponse();
				userResponse.setDocument(user.getDocument());
				userResponse.setEmail(user.getEmail());
				userResponse.setName(user.getName());
				userResponse.setPhone(user.getPhone());
				userResponse.setGuid(user.getGuid());
				userResponse.setIsAdmin(user.getType().equals(ETYPE_USER.NORMAL) ? false : true);
				list.add(userResponse);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}