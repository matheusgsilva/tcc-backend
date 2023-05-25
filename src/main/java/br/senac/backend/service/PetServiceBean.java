package br.senac.backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.senac.backend.model.Pet;
import br.senac.backend.repository.PetRepository;

@Service
public class PetServiceBean implements PetService {

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
	
	public Boolean isExists(String size, String breed, String typePet, String gender) {
		return repository.isExists(size, breed, typePet, gender);
	}

	public List<Pet> getAllFilteredCompany(String description, String size, String breed, String typePet, String city,
			String district, String companyGuid, String gender) {
		if ((description == null || description.equals("")) && (size == null || size.equals(""))
				&& (breed == null || breed.equals("")) && (typePet == null || typePet.equals(""))
				&& (city == null || city.equals("")) && (district == null || district.equals(""))
				&& (gender == null || gender.equals("")))
			return repository.getAllUnfilteredCompany(companyGuid);

		return repository.getAllFilteredCompany(description, size, breed, typePet, city, district, companyGuid, gender);
	}

	public List<Pet> getAllFiltered(String description, String size, String breed, String typePet, String city,
			String district, String companyName, String gender) {
		if ((description == null || description.equals("")) && (size == null || size.equals(""))
				&& (breed == null || breed.equals("")) && (typePet == null || typePet.equals(""))
				&& (city == null || city.equals("")) && (district == null || district.equals(""))
				&& (companyName == null || companyName.equals("")) && (gender == null || gender.equals("")))
			return repository.getAllUnfiltered();

		return repository.getAllFiltered(description, size, breed, typePet, city, district, gender, companyName);
	}

	@Transactional
	public void delete(Pet pet) {
		pet.setCompany(null);
		pet = save(pet);
		repository.delete(pet);
	}
}
