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

import br.senac.backend.converter.CompanyConverter;
import br.senac.backend.converter.PetConverter;
import br.senac.backend.handler.HandlerCompany;
import br.senac.backend.handler.HandlerPet;
import br.senac.backend.handler.HandlerUser;
import br.senac.backend.model.Company;
import br.senac.backend.model.Pet;
import br.senac.backend.model.User;
import br.senac.backend.request.PetRequest;
import br.senac.backend.response.CompanyResponse;
import br.senac.backend.response.PetResponse;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.PetService;
import br.senac.backend.service.TokenService;
import br.senac.backend.util.EACTIVE;

@Controller
public class PetController {

	@Autowired
	private PetService petService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CompanyConverter companyConverter;

	@Autowired
	private HandlerPet handlerPet;

	@Autowired
	private HandlerUser handlerUser;

	@Autowired
	private HandlerCompany handlerCompany;

	@Autowired
	private PetConverter petConverter;

	private Logger LOGGER = LoggerFactory.getLogger(PetController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/add", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> add(@RequestHeader(value = "token") String token,
			@RequestBody PetRequest petRequest) {

		ResponseAPI responseAPI = new ResponseAPI();
		try {

			Pet pet = petConverter.petSave(petRequest);
			if (pet != null) {
				pet = petService.save(pet);
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
	@RequestMapping(value = "/api/pet/update", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> update(@RequestHeader(value = "token") String token,
			@RequestBody PetRequest petRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Pet pet = petService.getByGuid(petRequest.getGuid());
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
				PetResponse petResponse = petConverter.petToResponse(pet);
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
	@RequestMapping(value = "/api/pet/confirm/contact/guid/{guid}", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> contact(@RequestHeader(value = "token") String token,
			@PathVariable String guid) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {

			User user = tokenService.getByToken(token).getUser();
			if (user == null) {
				handlerUser.handleDetailMessages(responseAPI, 404, null);
			}

			Pet pet = petService.getByGuid(guid);
			if (pet == null) {
				handlerPet.handleDetailMessages(responseAPI, 404, null);
			}

			Company company = pet.getCompany();
			if (company != null) {
				if (company.getActive().equals(EACTIVE.YES)) {
					CompanyResponse companyResponse = companyConverter.companyToResponseContact(company);
					if (companyResponse != null)
						handlerPet.handleContactMessages(responseAPI, 200, companyResponse);
					else
						handlerCompany.handleDetailMessages(responseAPI, 404, null);
				} else
					handlerCompany.handleDetailMessages(responseAPI, 404, null);
			} else
				handlerCompany.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/pet/confirm/contact/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/confirm/contact/guid - 400 - BAD REQUEST :: ");
			handlerPet.handleContactMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/pet/delete/guid/{guid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> delete(@PathVariable String guid, @RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Pet pet = petService.getByGuid(guid);
			if (pet != null) {
				pet.setActive(EACTIVE.NO);
				petService.save(pet);
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
	public ResponseEntity<ResponseAPI> listByUser(@PathVariable String guid,
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

			LOGGER.info(" :: Encerrando o método /api/pet/list/userguid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/pet/list/userguid - 400 - BAD REQUEST :: ");
			handlerPet.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}