package br.senac.backend.service;

import java.util.List;

import br.senac.backend.model.Preferences;
import br.senac.backend.model.User;

public interface PreferencesService {

	Preferences save(Preferences preferences);
	
	Preferences getByGuid(String guid);

	List<Preferences> getByUserGuid(String userGuid);

	List<User> findPreferences(String size, String breed, String typePet, String gender);
	
	Boolean isExists(String gender, String typePet, String breed, String size);
	
	Boolean isExists(String gender, String typePet, String breed, String guid, String size);
	
	void delete(Preferences preferences);
}
