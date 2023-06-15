package br.senac.backend.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.senac.backend.model.Company;
import br.senac.backend.model.Rating;
import br.senac.backend.model.User;
import br.senac.backend.request.RatingRequest;
import br.senac.backend.response.AverageRatingResponse;
import br.senac.backend.response.RatingResponse;
import br.senac.backend.util.EACTIVE;

@Component
public class RatingConverter {

	public Rating ratingSave(RatingRequest ratingRequest, User user, Company company) {

		try {
			Rating rating = new Rating();
			rating.setActive(EACTIVE.YES);
			rating.setGuid(UUID.randomUUID().toString());
			rating.setClassification(ratingRequest.getClassification());
			rating.setDate(new Date());
			rating.setUser(user);
			rating.setCompany(company);
			return rating;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Rating ratingUpdate(RatingRequest ratingRequest, Rating rating) {

		try {
			rating.setClassification(ratingRequest.getClassification());
			rating.setDate(new Date());
			return rating;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public RatingResponse ratingToResponse(Rating rating) {

		try {
			RatingResponse ratingResponse = new RatingResponse();
			ratingResponse.setClassification(rating.getClassification());
			ratingResponse.setDate(rating.getDate());
			ratingResponse.setGuid(rating.getGuid());
			if (rating.getUser() != null) {
				ratingResponse.setUser(rating.getUser().getName());
				ratingResponse.setUserGuid(rating.getUser().getGuid());
			}
			if (rating.getCompany() != null) {
				ratingResponse.setCompany(rating.getCompany().getName());
				ratingResponse.setCompanyGuid(rating.getCompany().getGuid());
			}
			return ratingResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<RatingResponse> ratingToResponseList(List<Rating> ratings) {

		try {
			List<RatingResponse> list = new ArrayList<RatingResponse>();
			for (Rating rating : ratings) {
				RatingResponse ratingResponse = new RatingResponse();
				ratingResponse.setClassification(rating.getClassification());
				ratingResponse.setDate(rating.getDate());
				ratingResponse.setGuid(rating.getGuid());
				if (rating.getUser() != null) {
					ratingResponse.setUser(rating.getUser().getName());
					ratingResponse.setUserGuid(rating.getUser().getGuid());
				}
				if (rating.getCompany() != null) {
					ratingResponse.setCompany(rating.getCompany().getName());
					ratingResponse.setCompanyGuid(rating.getCompany().getGuid());
				}
				list.add(ratingResponse);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public AverageRatingResponse ratingAverageToResponse(List<Rating> ratings) {

		try {
			AverageRatingResponse averageRatingResponse = new AverageRatingResponse();
			if (ratings != null && !ratings.isEmpty()) {
				averageRatingResponse.setAverage("Esta Instituição não foi avaliada ainda.");
			} else {
				double sum = 0;
				for (Rating rating : ratings) {
					sum += rating.getClassification();
				}
				double average = sum / ratings.size();
				double averageRounded = Math.round(average * 10.0) / 10.0;
				averageRatingResponse.setAverage(String.format("%.1f", averageRounded));
			}
			return averageRatingResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
