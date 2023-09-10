package br.senac.backend.service;

import br.senac.backend.model.Pet;

public interface NotificationService {

	void makeNotification(String petGuid);

	void makeNotificationPreferences(String preferencesGuid, String email);

	void makeNotificationReservePet(Pet pet, long daysRemaining);

	void makeNotificationReserveExpired(Pet pet);
}
