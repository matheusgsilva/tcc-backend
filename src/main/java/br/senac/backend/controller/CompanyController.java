package br.senac.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import br.senac.backend.converter.CompanyConverter;
import br.senac.backend.handler.HandlerCompany;
import br.senac.backend.model.Company;
import br.senac.backend.request.CompanyRequest;
import br.senac.backend.request.UpdatePassRequest;
import br.senac.backend.response.CepResponse;
import br.senac.backend.response.CompanyResponse;
import br.senac.backend.response.ResponseAPI;
import br.senac.backend.service.CompanyService;
import br.senac.backend.util.EACTIVE;
import br.senac.backend.util.ECOMPANY_PERMISSION;
import br.senac.backend.util.RestUrl;

@Controller
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private HandlerCompany handlerCompany;

	@Autowired
	private CompanyConverter companyConverter;

	@Autowired
	private RestUrl restUrl;

	private Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/company/add", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> add(@RequestBody CompanyRequest companyRequest) {

		ResponseAPI responseAPI = new ResponseAPI();
		try {
			if (!companyService.isExists(companyRequest.getName(), companyRequest.getEmail(),
					companyRequest.getDocument())) {
				Company company = companyConverter.companySave(companyRequest);
				if (company != null) {
					company = companyService.save(company);
					CompanyResponse companyResponse = companyConverter.companyToResponse(company);
					if (companyResponse != null)
						handlerCompany.handleAddMessages(responseAPI, 200, companyResponse);
					else
						handlerCompany.handleAddMessages(responseAPI, 404, null);
				} else
					handlerCompany.handleAddMessages(responseAPI, 404, null);

			} else
				handlerCompany.handleAddMessages(responseAPI, 304, null);

			LOGGER.info(" :: Encerrando o método /company/add - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /company/add - 400 - BAD REQUEST :: ");
			handlerCompany.handleAddMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/company/update", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> update(@RequestHeader(value = "token") String token,
			@RequestBody CompanyRequest companyRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Company company = companyService.getByGuid(companyRequest.getGuid());
			if (company != null) {
				if (!companyService.isExists(companyRequest.getName(), companyRequest.getEmail(),
						companyRequest.getDocument(), companyRequest.getGuid())) {
					company = companyConverter.companyUpdate(companyRequest, company);
					if (company != null) {
						company = companyService.save(company);
						CompanyResponse companyResponse = companyConverter.companyToResponse(company);
						if (companyResponse != null)
							handlerCompany.handleUpdateMessages(responseAPI, 200, companyResponse);
						else
							handlerCompany.handleUpdateMessages(responseAPI, 404, null);
					} else
						handlerCompany.handleUpdateMessages(responseAPI, 404, null);

				} else
					handlerCompany.handleUpdateMessages(responseAPI, 304, null);
			} else
				handlerCompany.handleUpdateMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/company/update - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/company/update - 400 - BAD REQUEST :: ");
			handlerCompany.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/company/query/cep/{cep}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> queryCep(@RequestHeader(value = "token") String token,
			@PathVariable String cep) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			String data = restUrl.getData("http://viacep.com.br/ws/" + cep + "/json/", "");
			if (data.contains("erro")) {
				handlerCompany.handleCepMessages(responseAPI, 404, null);
			} else {
				CepResponse cepResponse = new Gson().fromJson(data, CepResponse.class);
				if (cepResponse != null)
					handlerCompany.handleCepMessages(responseAPI, 200, cepResponse);
				else
					handlerCompany.handleCepMessages(responseAPI, 404, null);
			}

			LOGGER.info(" :: Encerrando o método /api/company/query/cep - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/company/query/cep - 400 - BAD REQUEST :: ");
			handlerCompany.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/company/authorize", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> autorizar(@RequestHeader(value = "token") String token,
			@RequestBody CompanyRequest companyRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Company company = companyService.getByGuid(companyRequest.getGuid());
			if (company != null) {
				company.setPermission(ECOMPANY_PERMISSION.AUTHORIZED);
				company = companyService.save(company);
				CompanyResponse companyResponse = companyConverter.companyToResponse(company);
				if (companyResponse != null)
					handlerCompany.handleUpdateMessages(responseAPI, 200, companyResponse);
				else
					handlerCompany.handleUpdateMessages(responseAPI, 404, null);
			} else
				handlerCompany.handleUpdateMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/company/authorize - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/company/authorize - 400 - BAD REQUEST :: ");
			handlerCompany.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/company/updatepass", method = RequestMethod.POST)
	public ResponseEntity<ResponseAPI> updatePass(@RequestHeader(value = "token") String token,
			@RequestBody UpdatePassRequest companyUpdatePassRequest) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Company company = companyService.getByGuid(companyUpdatePassRequest.getGuid());
			if (company != null) {
				company.setPassword(BCrypt.hashpw(companyUpdatePassRequest.getPassword(), BCrypt.gensalt()));
				CompanyResponse companyResponse = companyConverter.companyToResponse(companyService.save(company));
				if (companyResponse != null)
					handlerCompany.handleUpdateMessages(responseAPI, 200, companyResponse);
				else
					handlerCompany.handleUpdateMessages(responseAPI, 404, null);
			} else
				handlerCompany.handleUpdateMessages(responseAPI, 200, null);

			LOGGER.info(" :: Encerrando o método /api/company/updatepass - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/comapny/updatepass - 400 - BAD REQUEST :: ");
			handlerCompany.handleUpdateMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/company/detail/guid/{guid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> getByGuid(@PathVariable String guid,
			@RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Company company = companyService.getByGuid(guid);
			if (company != null) {
				CompanyResponse companyResponse = companyConverter.companyToResponse(company);
				if (companyResponse != null)
					handlerCompany.handleDetailMessages(responseAPI, 200, companyResponse);
				else
					handlerCompany.handleDetailMessages(responseAPI, 404, null);
			} else
				handlerCompany.handleDetailMessages(responseAPI, 404, null);

			LOGGER.info(" :: Encerrando o método /api/company/detail/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/company/detail/guid - 400 - BAD REQUEST :: ");
			handlerCompany.handleDetailMessages(responseAPI, 400, null);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/company/delete/guid/{guid}", method = RequestMethod.GET)
	public ResponseEntity<ResponseAPI> delete(@PathVariable String guid, @RequestHeader(value = "token") String token) {

		ResponseAPI responseAPI = new ResponseAPI();

		try {
			Company company = companyService.getByGuid(guid);
			if (company != null) {
				company.setActive(EACTIVE.NO);
				company = companyService.save(company);
				handlerCompany.handleDeleteMessages(responseAPI, 200);
			} else
				handlerCompany.handleDeleteMessages(responseAPI, 404);

			LOGGER.info(" :: Encerrando o método /api/company/delete/guid - 200 - OK :: ");
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error(" :: Encerrando o método /api/company/delete/guid - 400 - BAD REQUEST :: ");
			handlerCompany.handleDeleteMessages(responseAPI, 400);
			return new ResponseEntity<ResponseAPI>(responseAPI, HttpStatus.BAD_REQUEST);
		}
	}
}
