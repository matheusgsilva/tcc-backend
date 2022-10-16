package br.senac.backend.service;

import java.util.List;

import br.senac.backend.model.Rating;

public interface RatingService {

	Rating getByGuid(String guid);
	
	Rating getByUserAndCompany(String userGuid, String companyGuid);
	
	List<Rating> getByCompanyGuid(String companyGuid);
	
	Rating save(Rating rating);
	
	Boolean isExists(String userGuid, String companyGuid);
	
	Boolean isExists(String userGuid, String companyGuid, String guid);
}
