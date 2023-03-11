package br.senac.backend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.senac.backend.service.NotificationService;

@Component
public class NotificationTask implements Runnable {

	@Autowired
	private NotificationService notificationService;

	private String petGuid;

	public String getPetGuid() {
		return petGuid;
	}

	public void setPetGuid(String petGuid) {
		this.petGuid = petGuid;
	}

	@Override
	public void run() {
		notificationService.makeNotification(this.getPetGuid());
	}
}