package br.senac.backend.service;

import java.util.List;

import br.senac.backend.model.Pet;

public interface PetService {

	Pet save(Pet pet);

	Pet getByGuid(String guid);

	List<Pet> getByCompanyGuid(String companyGuid);
	
	List<Pet> getAllFiltered(String description, String size, String breed, String typePet, String city, String district, String companyName, String gender);
	
	List<Pet> getAllFilteredCompany(String description, String size, String breed, String typePet, String city,
			String district, String companyGuid, String gender);

	void delete(Pet pet);
}
