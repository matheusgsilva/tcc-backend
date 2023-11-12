package br.senac.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.senac.backend.converter.NotificationConverter;
import br.senac.backend.handler.HandlerNotification;
import br.senac.backend.handler.HandlerUser;
import br.senac.backend.model.Notification;
import br.senac.backend.model.User;
import br.senac.backend.response.NotificationResponse;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.NotificationService;
import br.senac.backend.service.TokenService;

@Controller
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private NotificationConverter notificationConverter;

	@Autowired
	private HandlerNotification handlerNotification;

	@Autowired
	private HandlerUser handlerUser;

	private Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/notification/list", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> list(@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = tokenService.getByToken(token).getUser();
			if (user == null) {
				handlerUser.handleDetailMessages(responseAPI, 404, null);
				return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
			}

			List<Notification> notifications = notificationService.getByUserGuid(user.getGuid());
			if (!notifications.isEmpty()) {
				List<NotificationResponse> responseList = notificationConverter
						.notificationToResponseList(notifications);
				if (!responseList.isEmpty())
					handlerNotification.handleListMessages(responseAPI, 200, responseList);
				else
					handlerNotification.handleListMessages(responseAPI, 404, null);
			} else
				handlerNotification.handleListMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/notification/list - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/notification/list - 400 - BAD REQUEST :: ");
			handlerNotification.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/notification/read/guid/{guid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> read(@RequestHeader(value = "token") String token, @PathVariable String guid) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Notification notification = notificationService.getByGuid(guid);
			if (notification != null) {
				notification.setIsRead(true);
				notificationService.save(notification);
				handlerNotification.handleUpdateMessages(responseAPI, 200, null);
			} else {
				handlerNotification.handleUpdateMessages(responseAPI, 404, null);
			}

			LOGGER.info(" :: Encerrando o método /api/notification/read/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/notification/read/guid - 400 - BAD REQUEST :: ");
			handlerNotification.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}
