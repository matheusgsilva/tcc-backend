package br.senac.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.senac.backend.handler.HandlerLogin;
import br.senac.backend.model.Company;
import br.senac.backend.model.Token;
import br.senac.backend.model.User;
import br.senac.backend.request.LoginRequest;
import br.senac.backend.response.LoginResponse;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.CompanyService;
import br.senac.backend.service.TokenService;
import br.senac.backend.service.UserService;
import br.senac.backend.util.ELOGIN_TYPE;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyService companyService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private HandlerLogin handlerLogin;

	private Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/logon", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> login(@RequestBody LoginRequest loginRequest) {

		LoginResponse login = new LoginResponse();
		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = userService.getByLoginPassword(loginRequest.getEmail(), loginRequest.getPassword());
			if (user != null) {
				Token t = tokenService.getNewTokenPersisted(user);
				login.setEmail(user.getEmail());
				login.setToken(t.getToken());
				login.setGuid(user.getGuid());
				login.setType(ELOGIN_TYPE.USER);
				handlerLogin.handleLoginMessages(responseAPI, 200, login);
				LOGGER.info(" :: Encerrando o método logon - 200 - OK :: ");
				return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
			}
			
			Company company = companyService.getByLoginPassword(loginRequest.getEmail(), loginRequest.getPassword());
			if(company != null) {
				Token t = tokenService.getNewTokenPersisted(company);
				login.setEmail(company.getEmail());
				login.setToken(t.getToken());
				login.setGuid(company.getGuid());
				login.setType(ELOGIN_TYPE.COMPANY);
				handlerLogin.handleLoginMessages(responseAPI, 200, login);
				LOGGER.info(" :: Encerrando o método logon - 200 - OK :: ");
				return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
			}

			LOGGER.info(" :: Encerrando o método logon - 401 - UNAUTHORIZED :: ");
			handlerLogin.handleLoginMessages(responseAPI, 401, login);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.UNAUTHORIZED);

		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.info(" :: Encerrando o método logon - 400 - BAD REQUEST :: ");
			handlerLogin.handleLoginMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(HttpStatus.UNAUTHORIZED);
		}
	}
}