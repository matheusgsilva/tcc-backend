package br.senac.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
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
import br.senac.backend.handler.HandlerPet;
import br.senac.backend.handler.HandlerRating;
import br.senac.backend.handler.HandlerUser;
import br.senac.backend.model.Company;
import br.senac.backend.model.Pet;
import br.senac.backend.model.Rating;
import br.senac.backend.model.User;
import br.senac.backend.request.RatingEmailRequest;
import br.senac.backend.request.RatingRequest;
import br.senac.backend.response.AverageRatingResponse;
import br.senac.backend.response.RatingResponse;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.CompanyService;
import br.senac.backend.service.PetService;
import br.senac.backend.service.RatingService;
import br.senac.backend.service.UserService;
import br.senac.backend.task.EmailRatingTask;

@Controller
public class RatingController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private UserService userService;

	@Autowired
	private PetService petService;

	@Autowired
	private RatingService ratingService;

	@Autowired
	private HandlerUser handlerUser;

	@Autowired
	private HandlerPet handlerPet;

	@Autowired
	private HandlerCompany handlerCompany;

	@Autowired
	private HandlerRating handlerRating;

	@Autowired
	private RatingConverter ratingConverter;

	@Value("${url.rating}")
	private String url;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TaskExecutor taskExecutor;

	private Logger LOGGER = LoggerFactory.getLogger(RatingController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/rating/save/companyguid/{companyguid}", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> save(@PathVariable String companyguid,
			@RequestBody RatingRequest ratingRequest) {

		ResponseAPI responseAPI = new ResponseAPI();
		try {

			User user = userService.getByLoginPassword(ratingRequest.getEmail(), ratingRequest.getPassword());
			if (user == null) {
				handlerUser.handleDetailMessages(responseAPI, 404, null);
				return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
			}
			Company company = companyService.getByGuid(companyguid);
			if (company == null) {
				handlerCompany.handleDetailMessages(responseAPI, 404, null);
				return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
			}

			RatingResponse ratingResponse = new RatingResponse();
			Rating rating = ratingService.getByUserAndCompany(user.getGuid(), companyguid);
			if (rating != null) {
				ratingResponse = ratingConverter
						.ratingToResponse(ratingService.save(ratingConverter.ratingUpdate(ratingRequest, rating)));
			} else {
				ratingResponse = ratingConverter
						.ratingToResponse(ratingService.save(ratingConverter.ratingSave(ratingRequest, user, company)));
			}
			if (ratingResponse != null)
				handlerRating.handleAddMessages(responseAPI, 200, ratingResponse);
			else
				handlerRating.handleAddMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/rating/save/companyguid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/rating/save/companyguid - 400 - BAD REQUEST :: ");
			handlerRating.handleAddMessages(responseAPI, 400, null);
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
				ratingService.delete(rating);
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

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/rating/average/companyguid/{companyguid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> average(@RequestHeader(value = "token") String token,
			@PathVariable String companyguid) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			AverageRatingResponse averageRatingResponse = ratingConverter
					.ratingAverageToResponse(ratingService.getByCompanyGuid(companyguid));
			if (averageRatingResponse != null)
				handlerRating.handleDetailMessages(responseAPI, 200, averageRatingResponse);
			else
				handlerRating.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/rating/average/companyguid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/rating/average/companyguid - 400 - BAD REQUEST :: ");
			handlerRating.handleDetailMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/rating/send/email", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> sendEmail(@RequestHeader(value = "token") String token,
			@RequestBody RatingEmailRequest ratingEmailRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Pet pet = petService.getByGuid(ratingEmailRequest.getPetGuid());
			if (pet != null) {
				User user = pet.getAdopterUser();
				if (user != null) {
					EmailRatingTask emailTask = applicationContext.getBean(EmailRatingTask.class);
					emailTask.setCompanyGuid(ratingEmailRequest.getCompanyGuid());
					emailTask.setEmail(user.getEmail());
					emailTask.setUrl(url);
					taskExecutor.execute(emailTask);
					handlerRating.handleSendEmailMessages(responseAPI, 200, null);
				} else
					handlerUser.handleDetailMessages(responseAPI, 404, null);
			} else
				handlerPet.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/rating/send/email - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/rating/send/email - 400 - BAD REQUEST :: ");
			handlerRating.handleSendEmailMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}