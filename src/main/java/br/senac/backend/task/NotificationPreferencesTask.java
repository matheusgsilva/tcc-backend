package br.senac.backend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.senac.backend.service.NotificationService;

@Component
public class NotificationPreferencesTask implements Runnable {

	@Autowired
	private NotificationService notificationService;

	private String preferencesGuid;
	private String email;

	public String getPreferencesGuid() {
		return preferencesGuid;
	}

	public void setPreferencesGuid(String preferencesGuid) {
		this.preferencesGuid = preferencesGuid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void run() {
		notificationService.makeNotificationPreferences(this.getPreferencesGuid(), this.getEmail());
	}
}