package br.senac.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.senac.backend.converter.PreferencesConverter;
import br.senac.backend.handler.HandlerPreferences;
import br.senac.backend.handler.HandlerUser;
import br.senac.backend.model.Preferences;
import br.senac.backend.model.User;
import br.senac.backend.request.PreferencesRequest;
import br.senac.backend.response.PreferencesResponse;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.PreferencesService;
import br.senac.backend.service.TokenService;
import br.senac.backend.task.NotificationPreferencesTask;

@Controller
public class PreferencesController {

	@Autowired
	private PreferencesService preferencesService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private HandlerPreferences handlerPreferences;

	@Autowired
	private HandlerUser handlerUser;

	@Autowired
	private PreferencesConverter preferencesConverter;
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TaskExecutor taskExecutor;

	private Logger LOGGER = LoggerFactory.getLogger(PreferencesController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/preferences/add", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> add(@RequestHeader(value = "token") String token,
			@RequestBody PreferencesRequest preferencesRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = tokenService.getByToken(token).getUser();
			if (user != null) {
				if (!preferencesService.isExists(preferencesRequest.getGender(), preferencesRequest.getTypePet(),
						preferencesRequest.getBreed())) {
					PreferencesResponse preferencesResponse = preferencesConverter.preferencesToResponse(
							preferencesService.save(preferencesConverter.preferencesSave(preferencesRequest, user)));
					if (preferencesResponse != null) {
						NotificationPreferencesTask notificationPreferencesTask = applicationContext.getBean(NotificationPreferencesTask.class);
						notificationPreferencesTask.setPreferencesGuid(preferencesResponse.getGuid());
						notificationPreferencesTask.setEmail(user.getEmail());
						taskExecutor.execute(notificationPreferencesTask);
						handlerPreferences.handleAddMessages(responseAPI, 200, preferencesResponse);
					}else
						handlerPreferences.handleAddMessages(responseAPI, 404, null);
				} else
					handlerPreferences.handleAddMessages(responseAPI, 304, null);
			} else
				handlerUser.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/preferences/add - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/preferences/add - 400 - BAD REQUEST :: ");
			handlerPreferences.handleAddMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/preferences/update/guid/{guid}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseAPI> update(@RequestHeader(value = "token") String token, @PathVariable String guid,
			@RequestBody PreferencesRequest preferencesRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = tokenService.getByToken(token).getUser();
			if (user != null) {
				Preferences preferences = preferencesService.getByGuid(guid);
				if (preferences != null) {
					if (!preferencesService.isExists(preferencesRequest.getGender(), preferencesRequest.getTypePet(),
							preferencesRequest.getBreed(), guid)) {
						PreferencesResponse preferencesResponse = preferencesConverter
								.preferencesToResponse(preferencesService
										.save(preferencesConverter.preferencesUpdate(preferencesRequest, preferences)));
						if (preferencesResponse != null)
							handlerPreferences.handleUpdateMessages(responseAPI, 200, preferencesResponse);
						else
							handlerPreferences.handleUpdateMessages(responseAPI, 404, null);
					} else
						handlerPreferences.handleUpdateMessages(responseAPI, 304, null);
				} else
					handlerPreferences.handleUpdateMessages(responseAPI, 404, null);
			} else
				handlerUser.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/preferences/update/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/preferences/update/guid - 400 - BAD REQUEST :: ");
			handlerPreferences.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/preferences/detail/guid/{guid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> detail(@RequestHeader(value = "token") String token, @PathVariable String guid) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = tokenService.getByToken(token).getUser();
			if (user != null) {
				PreferencesResponse preferencesResponse = preferencesConverter
						.preferencesToResponse(preferencesService.getByGuid(guid));
				if (preferencesResponse != null) {
					handlerPreferences.handleDetailMessages(responseAPI, 200, preferencesResponse);
				} else
					handlerPreferences.handleDetailMessages(responseAPI, 404, null);
			} else
				handlerUser.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/preferences/detail/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/preferences/detail/guid - 400 - BAD REQUEST :: ");
			handlerPreferences.handleDetailMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/preferences/list", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> list(@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = tokenService.getByToken(token).getUser();
			if (user != null) {
				List<PreferencesResponse> preferencesResponse = preferencesConverter
						.preferencesToResponseList(preferencesService.getByUserGuid(user.getGuid()));
				if (preferencesResponse != null) {
					handlerPreferences.handleDetailMessages(responseAPI, 200, preferencesResponse);
				} else
					handlerPreferences.handleDetailMessages(responseAPI, 404, null);
			} else
				handlerUser.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/preferences/list - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/preferences/list - 400 - BAD REQUEST :: ");
			handlerPreferences.handleDetailMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/preferences/delete/guid/{guid}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseAPI> delete(@RequestHeader(value = "token") String token, @PathVariable String guid) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = tokenService.getByToken(token).getUser();
			if (user != null) {
				Preferences preferences = preferencesService.getByGuid(guid);
				if (preferences != null) {
					preferencesService.delete(preferences);
					handlerPreferences.handleDeleteMessages(responseAPI, 200);
				} else
					handlerPreferences.handleDeleteMessages(responseAPI, 404);
			} else
				handlerUser.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/preferences/delete/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/preferences/delete/guid - 400 - BAD REQUEST :: ");
			handlerPreferences.handleDeleteMessages(responseAPI, 400);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}
