package br.senac.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senac.backend.model.Preferences;
import br.senac.backend.repository.PreferencesRepository;

@Service
public class PreferencesServiceBean implements PreferencesService {

	@Autowired
	private PreferencesRepository preferencesRepository;
	
	@Transactional
	public Preferences save(Preferences preferences) {
		return preferencesRepository.save(preferences);
	}
	
	public List<Preferences> getByUserGuid(String userGuid) {
		return preferencesRepository.getByUserGuid(userGuid);
	}
	
	public Preferences getByGuid(String userGuid) {
		return preferencesRepository.getByGuid(userGuid);
	}
	
	public List<Preferences> findPreferences(String age, String size, String breed, String typePet, String gender) {
		return preferencesRepository.findPreferences(age, size, breed, typePet, gender);
	}
	
	@Transactional
	public void delete(Preferences preferences) {
		preferences.setUser(null);
		preferences = save(preferences);
		preferencesRepository.delete(preferences);
	}
}
