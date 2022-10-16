package br.senac.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.senac.backend.converter.RatingConverter;
import br.senac.backend.handler.HandlerCompany;
import br.senac.backend.handler.HandlerRating;
import br.senac.backend.handler.HandlerUser;
import br.senac.backend.model.Company;
import br.senac.backend.model.Rating;
import br.senac.backend.model.User;
import br.senac.backend.request.RatingRequest;
import br.senac.backend.response.RatingResponse;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.CompanyService;
import br.senac.backend.service.RatingService;
import br.senac.backend.service.TokenService;
import br.senac.backend.util.EACTIVE;

@Controller
public class RatingController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private RatingService ratingService;

	@Autowired
	private HandlerUser handlerUser;

	@Autowired
	private HandlerCompany handlerCompany;

	@Autowired
	private HandlerRating handlerRating;

	@Autowired
	private RatingConverter ratingConverter;

	private Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/rating/add/companyguid/{companyguid}", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> add(@RequestHeader(value = "token") String token,
			@PathVariable String companyguid, @RequestBody RatingRequest ratingRequest) {

		ResponseAPI responseAPI = new ResponseAPI();
		try {

			User user = tokenService.getByToken(token).getUser();
			if (user == null) {
				handlerUser.handleDetailMessages(responseAPI, 404, null);
				return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
			}
			Company company = companyService.getByGuid(companyguid);
			if (company == null) {
				handlerCompany.handleDetailMessages(responseAPI, 404, null);
				return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
			}

			if (!ratingService.isExists(user.getGuid(), company.getGuid())) {
				RatingResponse ratingResponse = ratingConverter
						.ratingToResponse(ratingService.save(ratingConverter.ratingSave(ratingRequest, user, company)));
				if (ratingResponse != null)
					handlerRating.handleAddMessages(responseAPI, 200, ratingResponse);
				else
					handlerRating.handleAddMessages(responseAPI, 404, null);
			} else
				handlerRating.handleAddMessages(responseAPI, 304, null);

			LOGGER.info(" :: Encerrando o método /api/rating/add/companyguid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/rating/add/companyguid - 400 - BAD REQUEST :: ");
			handlerRating.handleAddMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/rating/update/guid/{guid}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseAPI> update(@RequestHeader(value = "token") String token, @PathVariable String guid,
			@RequestBody RatingRequest ratingRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Rating rating = ratingService.getByGuid(guid);
			if (rating != null) {
				RatingResponse ratingResponse = ratingConverter
						.ratingToResponse(ratingService.save(ratingConverter.ratingUpdate(ratingRequest, rating)));
				if (ratingResponse != null)
					handlerRating.handleUpdateMessages(responseAPI, 200, ratingResponse);
				else
					handlerRating.handleUpdateMessages(responseAPI, 404, null);
			} else
				handlerRating.handleUpdateMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/rating/update/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/rating/update/guid - 400 - BAD REQUEST :: ");
			handlerRating.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/rating/detail/guid/{guid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> getByGuid(@PathVariable String guid,
			@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			RatingResponse ratingResponse = ratingConverter.ratingToResponse(ratingService.getByGuid(guid));
			if (ratingResponse != null)
				handlerRating.handleDetailMessages(responseAPI, 200, ratingResponse);
			else
				handlerRating.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/rating/detail/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/rating/detail/guid - 400 - BAD REQUEST :: ");
			handlerRating.handleDetailMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/rating/delete/guid/{guid}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseAPI> delete(@PathVariable String guid, @RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Rating rating = ratingService.getByGuid(guid);
			if (rating != null) {
				rating.setActive(EACTIVE.NO);
				rating = ratingService.save(rating);
				handlerRating.handleDeleteMessages(responseAPI, 200);
			} else
				handlerRating.handleDeleteMessages(responseAPI, 404);

			LOGGER.info(" :: Encerrando o método /api/rating/delete/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/rating/delete/guid - 400 - BAD REQUEST :: ");
			handlerRating.handleDeleteMessages(responseAPI, 400);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/rating/list/companyguid/{companyguid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> list(@RequestHeader(value = "token") String token,
			@PathVariable String companyguid) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			List<RatingResponse> list = ratingConverter
					.ratingToResponseList(ratingService.getByCompanyGuid(companyguid));
			if (!list.isEmpty())
				handlerRating.handleListMessages(responseAPI, 200, list);
			else
				handlerRating.handleListMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/rating/list/companyguid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/rating/list/companyguid - 400 - BAD REQUEST :: ");
			handlerRating.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}