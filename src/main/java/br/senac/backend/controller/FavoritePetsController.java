package br.senac.backend.controller;

import java.util.ArrayList;
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

import br.senac.backend.converter.PetConverter;
import br.senac.backend.handler.HandlerPet;
import br.senac.backend.handler.HandlerUser;
import br.senac.backend.model.Pet;
import br.senac.backend.model.User;
import br.senac.backend.request.FavoritePetsRequest;
import br.senac.backend.response.PetResponse;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.PetService;
import br.senac.backend.service.UserService;

@Controller
public class FavoritePetsController {

	@Autowired
	private UserService userService;

	@Autowired
	private PetService petService;

	@Autowired
	private HandlerPet handlerPet;

	@Autowired
	private HandlerUser handlerUser;

	@Autowired
	private PetConverter petConverter;

	private Logger LOGGER = LoggerFactory.getLogger(FavoritePetsController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/favorite/add/{userGuid}", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> add(@RequestHeader(value = "token") String token, @PathVariable String userGuid,
			@RequestBody FavoritePetsRequest favoritePetsRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = userService.getByGuid(userGuid);
			if (user != null) {
				Pet pet = petService.getByGuid(favoritePetsRequest.getPetGuid());
				if (pet != null) {
					if (!user.getFavoritePets().contains(pet)) {
						user.getFavoritePets().add(pet);
						userService.save(user);
					}
					handlerPet.handleAddMessages(responseAPI, 200, null);
				} else
					handlerPet.handleDetailMessages(responseAPI, 404, null);
			} else
				handlerUser.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/favorite/add - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/favorite/add - 400 - BAD REQUEST :: ");
			handlerPet.handleDetailMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/favorite/remove/{userGuid}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseAPI> remove(@RequestHeader(value = "token") String token,
			@PathVariable String userGuid, @RequestBody FavoritePetsRequest favoritePetsRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = userService.getByGuid(userGuid);
			if (user != null) {
				Pet pet = petService.getByGuid(favoritePetsRequest.getPetGuid());
				if (pet != null) {
					if (user.getFavoritePets().contains(pet)) {
						user.getFavoritePets().remove(pet);
						userService.save(user);
					}
					handlerPet.handleDeleteMessages(responseAPI, 200);
				} else
					handlerPet.handleDetailMessages(responseAPI, 404, null);
			} else
				handlerUser.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/favorite/remove - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/favorite/remove - 400 - BAD REQUEST :: ");
			handlerPet.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/favorite/list/{userGuid}", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> list(@RequestHeader(value = "token") String token, @PathVariable String userGuid,
			@RequestBody FavoritePetsRequest favoritePetsRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			User user = userService.getByGuid(userGuid);
			if (user != null) {
				List<Pet> pets = user.getFavoritePets();
				if (!pets.isEmpty()) {
					List<PetResponse> listResponse = petConverter.petsToResponseList(pets);
					if (!listResponse.isEmpty())
						handlerPet.handleDetailMessages(responseAPI, 200, listResponse);
					else
						handlerPet.handleDetailMessages(responseAPI, 200, new ArrayList<Pet>());
				} else
					handlerPet.handleDetailMessages(responseAPI, 200, new ArrayList<Pet>());
			} else
				handlerUser.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/favorite/list - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/favorite/list - 400 - BAD REQUEST :: ");
			handlerPet.handleListMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}
