package br.senac.backend.service;

import br.senac.backend.model.Company;

public interface CompanyService {

	Company getByGuid(String guid);

	Company save(Company company);

	Company getByLoginPassword(String email, String password);

	Boolean isExists(String name, String email, String document);
	
	Boolean isExists(String name, String email, String document, String guid);
}
