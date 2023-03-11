package br.senac.backend.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.senac.backend.converter.NotificationConverter;
import br.senac.backend.handler.HandlerNotification;
import br.senac.backend.model.User;
import br.senac.backend.request.FavoritePetsRequest;
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
	private HandlerNotification handlerNotification;

	@Autowired
	private NotificationConverter notificationConverter;

	private Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/notification/list", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> list(@RequestHeader(value = "token") String token,
			@RequestBody FavoritePetsRequest favoritePetsRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = tokenService.getByToken(token).getUser();
			if (user != null) {
				List<NotificationResponse> notificationList = notificationConverter
						.notificationsResponseList(notificationService.getByUserGuid(user.getGuid()));
				if (!notificationList.isEmpty()) {
					handlerNotification.handleListMessages(responseAPI, 200, notificationList);
				} else {
					handlerNotification.handleListMessages(responseAPI, 200, new ArrayList<NotificationResponse>());
				}
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
}
