package br.senac.backend.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.senac.backend.model.Notification;
import br.senac.backend.response.NotificationResponse;

@Component
public class NotificationConverter {

	public List<NotificationResponse> notificationsResponseList(List<Notification> notifications) {
		try {
			List<NotificationResponse> list = new ArrayList<NotificationResponse>();
			for (Notification notification : notifications) {
				NotificationResponse notificationResponse = new NotificationResponse();
				notificationResponse.setCompanyGuid(notification.getPet().getCompany().getGuid());
				notificationResponse.setCompanyName(notification.getPet().getCompany().getName());
				notificationResponse.setDate(notification.getDate());
				notificationResponse.setPetGuid(notification.getPet().getGuid());
				list.add(notificationResponse);
			}
			return list;
		} catch (Exception ex) {
			return new ArrayList<NotificationResponse>();
		}
	}
}
