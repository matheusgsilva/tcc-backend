package br.senac.backend.service;

import br.senac.backend.model.Company;

public interface CompanyService {

	Company getByGuid(String guid);

	Company save(Company company);

	Company getByLoginPassword(String email, String password);

	Boolean isExists(String nome, String email, String documento);
	
	Boolean isExists(String nome, String email, String documento, String guid);
}
