package br.senac.backend.converter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.senac.backend.model.Notification;
import br.senac.backend.response.NotificationResponse;
import br.senac.backend.service.NotificationService;
import br.senac.backend.service.UserService;

@Component
public class NotificationConverter {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserService userService;

	public void saveNotification(String title, String content, String userGuid) {
		Notification notification = new Notification();
		notification.setTitle(title);
		notification.setContent(content);
		notification.setDate(new Date());
		notification.setGuid(UUID.randomUUID().toString());
		notification.setIsRead(false);
		notification.setUser(userService.getByGuid(userGuid));
		notificationService.save(notification);
	}

	public List<NotificationResponse> notificationToResponseList(List<Notification> notifications) {
		try {
			List<NotificationResponse> list = new ArrayList<NotificationResponse>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			for (Notification notification : notifications) {
				NotificationResponse notificationResponse = new NotificationResponse();
				notificationResponse.setTitle(notification.getTitle());
				notificationResponse.setContent(notification.getContent());
				notificationResponse.setDate(dateFormat.format(notification.getDate()));
				notificationResponse.setGuid(notification.getGuid());
				notificationResponse.setIsRead(notification.getIsRead());
				notificationResponse.setUserGuid(notification.getUser().getGuid());
				list.add(notificationResponse);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
