package br.senac.backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.senac.backend.model.Pet;
import br.senac.backend.repository.PetRepository;

@Service
public class PetServiceBean implements PetService{

	@Autowired
	private PetRepository repository;

	@Transactional
	public Pet save(Pet pet) {
		return repository.save(pet);
	}
	
	public Pet getByGuid(String guid) {
		return repository.getByGuid(guid);
	}
	
	public List<Pet> getByCompanyGuid(String companyGuid) {
		return repository.getByCompanyGuid(companyGuid);
	}
	
	@Transactional
	public void delete(Pet pet) {
		pet.setCompany(null);
		pet = save(pet);
		repository.delete(pet);
	}
}
