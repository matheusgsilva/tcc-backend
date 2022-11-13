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

	public List<Pet> getAllFilteredCompany(String description, String size, String breed, String typePet, String city,
			String district, String companyGuid, String gender) {
		if ((description.equals("") || description == null) && (size.equals("") || size == null)
				&& (breed.equals("") || breed == null) && (typePet.equals("") || typePet == null)
				&& (city.equals("") || city == null) && (district.equals("") || district == null)
				&& (gender.equals("") || gender == null))
			return repository.getAllUnfilteredCompany(companyGuid);

		return repository.getAllFilteredCompany(description, size, breed, typePet, city, district, companyGuid, gender);
	}

	public List<Pet> getAllFiltered(String description, String size, String breed, String typePet, String city,
			String district, String companyName, String gender) {
		if ((description.equals("") || description == null) && (size.equals("") || size == null)
				&& (breed.equals("") || breed == null) && (typePet.equals("") || typePet == null)
				&& (city.equals("") || city == null) && (district.equals("") || district == null)
				&& (companyName.equals("") || companyName == null) && (gender.equals("") || gender == null))
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
