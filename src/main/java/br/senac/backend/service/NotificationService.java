package br.senac.backend.service;

import java.util.List;

import br.senac.backend.model.Notification;
import br.senac.backend.model.Pet;

public interface NotificationService {

	void makeNotification(String petGuid);

	void makeNotificationPreferences(String preferencesGuid, String email);

	void makeNotificationReservePet(Pet pet, long daysRemaining);

	void makeNotificationReserveExpired(Pet pet);
	
	void makeNotificationAdoptionPet(Pet pet, long daysRemaining);

	void makeNotificationAdoptionExpired(Pet pet);

	List<Notification> getByUserGuid(String userGuid);
	
	Notification getByGuid(String guid);
	
	Notification save(Notification notification);
	
	void deleteByUser(String userGuid);
}
