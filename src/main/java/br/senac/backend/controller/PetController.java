package br.senac.backend.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import br.senac.backend.converter.PetConverter;
import br.senac.backend.handler.HandlerPet;
import br.senac.backend.model.Pet;
import br.senac.backend.model.User;
import br.senac.backend.request.ListPetRequest;
import br.senac.backend.request.PetRequest;
import br.senac.backend.response.PetResponse;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.PetService;
import br.senac.backend.service.TokenService;
import br.senac.backend.task.NotificationTask;
import br.senac.backend.util.ESTATUS_PET;

@Controller
public class PetController {

	@Autowired
	private PetService petService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private HandlerPet handlerPet;

	@Autowired
	private PetConverter petConverter;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TaskExecutor taskExecutor;

	private Logger LOGGER = LoggerFactory.getLogger(PetController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/add/companyguid/{companyGuid}", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> add(@RequestHeader(value = "token") String token,
			@PathVariable String companyGuid, @RequestBody PetRequest petRequest) {

		ResponseAPI responseAPI = new ResponseAPI();
		try {

			Pet pet = petConverter.petSave(petRequest, companyGuid);
			if (pet != null) {
				pet = petService.save(pet);
				NotificationTask notificationTask = applicationContext.getBean(NotificationTask.class);
				notificationTask.setPetGuid(pet.getGuid());
				taskExecutor.execute(notificationTask);
				PetResponse petResponse = petConverter.petToResponse(pet);
				if (petResponse != null)
					handlerPet.handleAddMessages(responseAPI, 200, petResponse);
				else
					handlerPet.handleAddMessages(responseAPI, 404, null);
			} else
				handlerPet.handleAddMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/add - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/add - 400 - BAD REQUEST :: ");
			handlerPet.handleAddMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/update/guid/{guid}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseAPI> update(@RequestHeader(value = "token") String token, @PathVariable String guid,
			@RequestBody PetRequest petRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Pet pet = petService.getByGuid(guid);
			if (pet != null) {
				pet = petConverter.petUpdate(petRequest, pet);
				if (pet != null) {
					pet = petService.save(pet);
					PetResponse petResponse = petConverter.petToResponse(pet);
					if (petResponse != null)
						handlerPet.handleUpdateMessages(responseAPI, 200, petResponse);
					else
						handlerPet.handleUpdateMessages(responseAPI, 404, null);
				} else
					handlerPet.handleUpdateMessages(responseAPI, 404, null);
			} else
				handlerPet.handleUpdateMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/update - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/update - 400 - BAD REQUEST :: ");
			handlerPet.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/detail/guid/{guid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> getByGuid(@PathVariable String guid,
			@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Pet pet = petService.getByGuid(guid);
			if (pet != null) {
				PetResponse petResponse = petConverter.petToResponse(pet, tokenService.getByToken(token).getUser());
				if (petResponse != null)
					handlerPet.handleDetailMessages(responseAPI, 200, petResponse);
				else
					handlerPet.handleDetailMessages(responseAPI, 404, null);
			} else
				handlerPet.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/detail/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/detail/guid - 400 - BAD REQUEST :: ");
			handlerPet.handleDetailMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/reserve/guid/{guid}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseAPI> reservePet(@PathVariable String guid,
			@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Pet pet = petService.getByGuid(guid);
			if (pet != null) {
				if (pet.getStatus().equals(ESTATUS_PET.AVAILABLE)) {
					User user = tokenService.getByToken(token).getUser();
					pet.setStatus(ESTATUS_PET.RESERVED);
					pet.setAdopterUser(user);
					pet.setReservationDate(new Date());
					pet = petService.save(pet);
					PetResponse petResponse = petConverter.petToResponse(pet, user);
					if (petResponse != null)
						handlerPet.handleReserveMessages(responseAPI, 200, petResponse);
					else
						handlerPet.handleReserveMessages(responseAPI, 404, null);
				} else
					handlerPet.handleReserveMessages(responseAPI, 304, null);
			} else
				handlerPet.handleReserveMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/reserve/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/reserve/guid - 400 - BAD REQUEST :: ");
			handlerPet.handleReserveMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/cancel/reserve/guid/{guid}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseAPI> cancelReservePet(@PathVariable String guid,
			@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Pet pet = petService.getByGuid(guid);
			if (pet != null) {
				pet.setStatus(ESTATUS_PET.AVAILABLE);
				pet.setAdopterUser(null);
				pet.setReservationDate(null);
				pet = petService.save(pet);
				PetResponse petResponse = petConverter.petToResponse(pet, null);
				if (petResponse != null)
					handlerPet.handleReserveMessages(responseAPI, 200, petResponse);
				else
					handlerPet.handleReserveMessages(responseAPI, 404, null);
			} else
				handlerPet.handleReserveMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/reserve/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/reserve/guid - 400 - BAD REQUEST :: ");
			handlerPet.handleReserveMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/adopt/guid/{guid}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseAPI> adoptPet(@PathVariable String guid,
			@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Pet pet = petService.getByGuid(guid);
			if (pet != null) {
				if (pet.getStatus().equals(ESTATUS_PET.RESERVED)) {
					pet.setStatus(ESTATUS_PET.ADOPTED);
					pet.setReservationDate(null);
					pet = petService.save(pet);
					PetResponse petResponse = petConverter.petToResponse(pet, null);
					if (petResponse != null)
						handlerPet.handleAdoptionMessages(responseAPI, 200, petResponse);
					else
						handlerPet.handleAdoptionMessages(responseAPI, 404, null);
				} else
					handlerPet.handleAdoptionMessages(responseAPI, 304, null);
			} else
				handlerPet.handleAdoptionMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/adopt/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/adopt/guid - 400 - BAD REQUEST :: ");
			handlerPet.handleAdoptionMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/delete/guid/{guid}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseAPI> delete(@PathVariable String guid, @RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Pet pet = petService.getByGuid(guid);
			if (pet != null) {
				petService.delete(pet);
				handlerPet.handleDeleteMessages(responseAPI, 200);
			} else
				handlerPet.handleDeleteMessages(responseAPI, 404);

			LOGGER.info(" :: Encerrando o método /api/pet/delete/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/delete/guid - 400 - BAD REQUEST :: ");
			handlerPet.handleDeleteMessages(responseAPI, 400);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/list/companyguid/{guid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> listByCompany(@PathVariable String guid,
			@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			List<Pet> pets = petService.getByCompanyGuid(guid);
			if (!pets.isEmpty()) {
				List<PetResponse> responseList = petConverter.petsToResponseList(pets);
				if (!responseList.isEmpty())
					handlerPet.handleListMessages(responseAPI, 200, responseList);
				else
					handlerPet.handleListMessages(responseAPI, 404, null);
			} else
				handlerPet.handleListMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/list/companyguid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/list/companyguid - 400 - BAD REQUEST :: ");
			handlerPet.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/list", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> list(@RequestHeader(value = "token") String token,
			@RequestBody ListPetRequest listPetRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			petService.updateStatusPets();
			List<Pet> pets = petService.getAllFiltered(listPetRequest.getDescription(), listPetRequest.getSize(),
					listPetRequest.getBreed(), listPetRequest.getTypePet(), listPetRequest.getCity(),
					listPetRequest.getDistrict(), listPetRequest.getCompanyName(), listPetRequest.getGender());
			if (!pets.isEmpty()) {
				List<PetResponse> responseList = petConverter.petsToResponseList(pets);
				if (!responseList.isEmpty())
					handlerPet.handleListMessages(responseAPI, 200, responseList);
				else
					handlerPet.handleListMessages(responseAPI, 404, null);
			} else
				handlerPet.handleListMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/list - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/list - 400 - BAD REQUEST :: ");
			handlerPet.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/list/companyguid/{guid}", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> listByCompany(@RequestHeader(value = "token") String token,
			@PathVariable String guid, @RequestBody ListPetRequest listPetRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			petService.updateStatusPets();
			List<Pet> pets = petService.getAllFilteredCompany(listPetRequest.getDescription(), listPetRequest.getSize(),
					listPetRequest.getBreed(), listPetRequest.getTypePet(), listPetRequest.getCity(),
					listPetRequest.getDistrict(), guid, listPetRequest.getGender());
			if (!pets.isEmpty()) {
				List<PetResponse> responseList = petConverter.petsToResponseList(pets);
				if (!responseList.isEmpty())
					handlerPet.handleListMessages(responseAPI, 200, responseList);
				else
					handlerPet.handleListMessages(responseAPI, 404, null);
			} else
				handlerPet.handleListMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/list/companyguid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/list/companyguid - 400 - BAD REQUEST :: ");
			handlerPet.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}