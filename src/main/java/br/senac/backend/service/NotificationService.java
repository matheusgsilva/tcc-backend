package br.senac.backend.service;

import java.util.List;

import br.senac.backend.model.Notification;

public interface NotificationService {

	void makeNotification(String petGuid);
	
	List<Notification> getByUserGuid(String userGuid);
}
