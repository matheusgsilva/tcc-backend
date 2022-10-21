package br.senac.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senac.backend.model.Rating;
import br.senac.backend.repository.RatingRepository;

@Service
public class RatingServiceBean implements RatingService {

	@Autowired
	private RatingRepository repository;

	public Rating getByGuid(String guid) {
		return repository.getByGuid(guid);
	}
	
	public Rating getByUserAndCompany(String userGuid, String companyGuid) {
		return repository.getByUserAndCompany(userGuid, companyGuid);
	}
	
	public List<Rating> getByCompanyGuid(String companyGuid) {
		return repository.getByCompanyGuid(companyGuid);
	}

	@Transactional
	public Rating save(Rating rating) {
		return repository.save(rating);
	}
}
