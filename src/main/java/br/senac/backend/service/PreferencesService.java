package br.senac.backend.service;

import java.util.List;

import br.senac.backend.model.Preferences;

public interface PreferencesService {

	Preferences save(Preferences preferences);
	
	Preferences getByGuid(String guid);

	List<Preferences> getByUserGuid(String userGuid);

	List<String> findPreferences(String age, String size, String breed, String typePet, String gender);
	
	Boolean isExists(String gender, String typePet, String breed);
	
	Boolean isExists(String gender, String typePet, String breed, String guid);
	
	void delete(Preferences preferences);
}
