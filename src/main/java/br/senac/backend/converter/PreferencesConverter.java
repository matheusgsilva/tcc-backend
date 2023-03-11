package br.senac.backend.converter;

import java.util.UUID;

import org.springframework.stereotype.Component;

import br.senac.backend.model.Preferences;
import br.senac.backend.model.User;
import br.senac.backend.request.PreferencesRequest;
import br.senac.backend.response.PreferencesResponse;

@Component
public class PreferencesConverter {

	public Preferences preferencesSave(PreferencesRequest preferencesRequest, User user) {

		try {
			Preferences preferences = new Preferences();
			preferences.setAge(preferencesRequest.getAge());
			preferences.setBreed(preferencesRequest.getBreed());
			preferences.setGuid(UUID.randomUUID().toString());
			preferences.setSize(preferencesRequest.getSize());
			preferences.setTypePet(preferencesRequest.getTypePet());
			preferences.setGender(preferencesRequest.getGender());
			preferences.setUser(user);
			return preferences;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Preferences preferencesUpdate(PreferencesRequest preferencesRequest, Preferences preferences) {

		try {
			preferences.setAge(preferencesRequest.getAge());
			preferences.setBreed(preferencesRequest.getBreed());
			preferences.setSize(preferencesRequest.getSize());
			preferences.setTypePet(preferencesRequest.getTypePet());
			preferences.setGender(preferencesRequest.getGender());
			return preferences;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public PreferencesResponse preferencesToResponse(Preferences preferences) {

		try {
			PreferencesResponse preferencesResponse = new PreferencesResponse();
			preferencesResponse.setAge(preferences.getAge());
			preferencesResponse.setBreed(preferences.getBreed());
			preferencesResponse.setGuid(preferences.getGuid());
			preferencesResponse.setSize(preferences.getSize());
			preferencesResponse.setTypePet(preferences.getTypePet());
			preferencesResponse.setGender(preferences.getGender());
			return preferencesResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
