package br.senac.backend.service;

public interface NotificationService {

	void makeNotification(String petGuid);
	
	void makeNotificationPreferences(String preferencesGuid, String email);
}
