package br.senac.backend.service;

import java.util.List;

import br.senac.backend.model.Company;
import br.senac.backend.model.Rating;
import br.senac.backend.model.User;

public interface RatingService {

	Rating getByGuid(String guid);
	
	Rating getByUserAndCompany(String userGuid, String companyGuid);
	
	List<Rating> getByCompanyGuid(String companyGuid);
	
	Double getByAverageRatingCompanyGuid(String companyGuid);
	
	Rating save(Rating rating);
	
	void delete(User user);
	
	void delete(Rating rating);
	
	void delete(Company company);
}
