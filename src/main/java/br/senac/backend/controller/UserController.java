package br.senac.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.senac.backend.converter.UserConverter;
import br.senac.backend.handler.HandlerUser;
import br.senac.backend.model.User;
import br.senac.backend.request.UserRequest;
import br.senac.backend.request.UpdatePassRequest;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.response.UserResponse;
import br.senac.backend.service.UserService;
import br.senac.backend.util.EACTIVE;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private HandlerUser handlerUser;

	@Autowired
	private UserConverter userConverter;

	private Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/user/add", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> add(@RequestBody UserRequest userRequest) {

		ResponseAPI responseAPI = new ResponseAPI();
		try {
			if (!userService.isExists(userRequest.getEmail())) {
				UserResponse userResponse = userConverter
						.userToResponse(userService.save(userConverter.userSave(userRequest)));
				if (userResponse != null)
					handlerUser.handleAddMessages(responseAPI, 200, userResponse);
				else
					handlerUser.handleAddMessages(responseAPI, 404, null);

			} else
				handlerUser.handleAddMessages(responseAPI, 304, null);

			LOGGER.info(" :: Encerrando o método /api/user/add - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/user/add - 400 - BAD REQUEST :: ");
			handlerUser.handleAddMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/user/update/guid/{guid}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseAPI> update(@RequestHeader(value = "token") String token, @PathVariable String guid,
			@RequestBody UserRequest userRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = userService.getByGuid(guid);
			if (user != null) {
				if (!userService.isExists(userRequest.getEmail(), guid)) {
					UserResponse userResponse = userConverter
							.userToResponse(userService.save(userConverter.userUpdate(userRequest, user)));
					if (userResponse != null)
						handlerUser.handleUpdateMessages(responseAPI, 200, userResponse);
					else
						handlerUser.handleUpdateMessages(responseAPI, 404, null);
				} else
					handlerUser.handleUpdateMessages(responseAPI, 304, null);
			} else
				handlerUser.handleUpdateMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/user/update/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/user/update/guid - 400 - BAD REQUEST :: ");
			handlerUser.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/user/updatepass", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> updatePass(@RequestHeader(value = "token") String token,
			@RequestBody UpdatePassRequest userUpdatePassRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = userService.getByGuid(userUpdatePassRequest.getGuid());
			if (user != null) {
				user.setPassword(BCrypt.hashpw(userUpdatePassRequest.getPassword(), BCrypt.gensalt()));
				UserResponse userResponse = userConverter.userToResponse(userService.save(user));
				if (userResponse != null)
					handlerUser.handleUpdateMessages(responseAPI, 200, userResponse);
				else
					handlerUser.handleUpdateMessages(responseAPI, 404, null);
			} else
				handlerUser.handleUpdateMessages(responseAPI, 200, null);

			LOGGER.info(" :: Encerrando o método /api/user/updatepass - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/user/updatepass - 400 - BAD REQUEST :: ");
			handlerUser.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/user/detail/guid/{guid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> getByGuid(@PathVariable String guid,
			@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			UserResponse userResponse = userConverter.userToResponse(userService.getByGuid(guid));
			if (userResponse != null)
				handlerUser.handleDetailMessages(responseAPI, 200, userResponse);
			else
				handlerUser.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/user/detail/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/user/detail/guid - 400 - BAD REQUEST :: ");
			handlerUser.handleDetailMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/user/delete/guid/{guid}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseAPI> delete(@PathVariable String guid, @RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = userService.getByGuid(guid);
			if (user != null) {
				user.setActive(EACTIVE.NO);
				user = userService.save(user);
				handlerUser.handleDeleteMessages(responseAPI, 200);
			} else
				handlerUser.handleDeleteMessages(responseAPI, 404);

			LOGGER.info(" :: Encerrando o método /api/user/delete/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/user/delete/guid - 400 - BAD REQUEST :: ");
			handlerUser.handleDeleteMessages(responseAPI, 400);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/user/list", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> list(@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			List<UserResponse> list = userConverter.userToResponseList(userService.getAll());
			if (!list.isEmpty())
				handlerUser.handleListMessages(responseAPI, 200, list);
			else
				handlerUser.handleListMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/user/list - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/user/list - 400 - BAD REQUEST :: ");
			handlerUser.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}