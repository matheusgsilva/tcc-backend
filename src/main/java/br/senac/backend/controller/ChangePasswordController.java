package br.senac.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.senac.backend.converter.ChangePasswordConverter;
import br.senac.backend.handler.HandlerChangePassword;
import br.senac.backend.handler.HandlerUser;
import br.senac.backend.model.ChangePassword;
import br.senac.backend.model.Company;
import br.senac.backend.model.User;
import br.senac.backend.repository.UpdatePasswordRequest;
import br.senac.backend.request.ChangePasswordRequest;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.ChangePasswordService;
import br.senac.backend.service.CompanyService;
import br.senac.backend.service.UserService;
import br.senac.backend.task.EmailPasswordTask;
import br.senac.backend.util.EACTIVE;

@Controller
public class ChangePasswordController {

	@Autowired
	private ChangePasswordService changePasswordService;

	@Autowired
	private ChangePasswordConverter changePasswordConverter;

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private HandlerUser handlerUser;

	@Autowired
	private HandlerChangePassword handlerChangePassword;

	@Autowired
	private TaskExecutor taskExecutorEmail;

	@Autowired
	private ApplicationContext applicationContext;

	@Value("${url.password}")
	private String url;

	private Logger LOGGER = LoggerFactory.getLogger(ChangePasswordController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/change/password", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> add(@RequestBody ChangePasswordRequest changePasswordRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = userService.locateByEmail(changePasswordRequest.getEmail());
			Company company = companyService.getByEmail(changePasswordRequest.getEmail());
			if (user != null || company != null) {
				ChangePassword changePassword = changePasswordService
						.save(changePasswordConverter.passwordToSave(changePasswordRequest, user, company));
				if (changePassword.getEmail().equals(changePasswordRequest.getEmail())) {
					EmailPasswordTask emailTask = applicationContext.getBean(EmailPasswordTask.class);
					emailTask.setReceiver(changePasswordRequest.getEmail());
					emailTask.setUrl(url + changePassword.getGuid());
					taskExecutorEmail.execute(emailTask);
					handlerUser.handleEmailMessages(responseAPI, 200, null);
				} else {
					handlerChangePassword.handleUpdateMessages(responseAPI, 404, null);
				}

			} else {
				handlerUser.handleEmailMessages(responseAPI, 404, null);
			}

			LOGGER.info(" :: Encerrando o método /change/password - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /change/password - 400 - BAD REQUEST :: ");
			handlerUser.handleEmailMessages(responseAPI, 400, null);

			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/update/password", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> update(@RequestBody UpdatePasswordRequest updatePasswordRequest)
			throws IOException {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = userService.locateByEmail(updatePasswordRequest.getEmail());
			Company company = companyService.getByEmail(updatePasswordRequest.getEmail());
			if (user != null || company != null) {
				ChangePassword changePassword = changePasswordService.findByGuid(updatePasswordRequest.getGuid());
				if (changePassword != null) {
					if (changePassword.getActive().equals(EACTIVE.YES)) {
						if (changePassword.getEmail().equals(updatePasswordRequest.getEmail())) {
							if (user != null) {
								user.setPassword(BCrypt.hashpw(updatePasswordRequest.getPassword(), BCrypt.gensalt()));
								user = userService.save(user);
							}
							if (company != null) {
								company.setPassword(
										BCrypt.hashpw(updatePasswordRequest.getPassword(), BCrypt.gensalt()));
								company = companyService.save(company);
							}
							changePassword.setActive(EACTIVE.NO);
							changePasswordService.save(changePassword);
							handlerChangePassword.handleUpdateMessages(responseAPI, 200, null);
						} else {
							handlerChangePassword.handleUpdateMessages(responseAPI, 404, null);
						}
					} else {
						handlerChangePassword.handleUpdateMessages(responseAPI, 404, null);
					}
				} else {
					handlerChangePassword.handleUpdateMessages(responseAPI, 404, null);
				}
			} else {
				handlerUser.handleDetailMessages(responseAPI, 404, null);
			}

			LOGGER.info(" :: Encerrando o método /update/password - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /update/password - 400 - BAD REQUEST :: ");
			handlerUser.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}