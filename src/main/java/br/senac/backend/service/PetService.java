package br.senac.backend.service;

import java.util.List;

import br.senac.backend.model.Pet;

public interface PetService {

	Pet save(Pet pet);

	Pet getByGuid(String guid);

	List<Pet> getByCompanyGuid(String companyGuid);

	void delete(Pet pet);
}
