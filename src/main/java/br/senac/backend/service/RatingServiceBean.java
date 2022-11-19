package br.senac.backend.service;

import java.util.List;
import java.util.stream.Collectors;

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

	public Double getByAverageRatingCompanyGuid(String companyGuid) {
		List<Rating> ratings = repository.getByCompanyGuid(companyGuid);
		return Math.round((ratings.size() == 0 ? Double.parseDouble("0")
				: ratings.stream().collect(Collectors.summingInt(r -> r.getClassification())) / ratings.size()) * 100.0)
				/ 100.0;
	}

	@Transactional
	public Rating save(Rating rating) {
		return repository.save(rating);
	}
}
