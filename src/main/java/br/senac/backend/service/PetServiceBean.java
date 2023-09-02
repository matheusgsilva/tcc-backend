package br.senac.backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import br.senac.backend.model.Pet;
import br.senac.backend.model.User;
import br.senac.backend.repository.PetRepository;
import br.senac.backend.util.ESTATUS_PET;

@Service
public class PetServiceBean implements PetService {

	@Autowired
	private PetRepository repository;

	@Autowired
	private UserService userService;

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

		return repository.getAllFilteredCompany(description, size, typePet.equals("Cachorro") ? breed : "", typePet,
				city, district, companyGuid, gender);
	}

	public List<Pet> getAllFiltered(String description, String size, String breed, String typePet, String city,
			String district, String companyName, String gender) {
		if ((description == null || description.equals("")) && (size == null || size.equals(""))
				&& (breed == null || breed.equals("")) && (typePet == null || typePet.equals(""))
				&& (city == null || city.equals("")) && (district == null || district.equals(""))
				&& (companyName == null || companyName.equals("")) && (gender == null || gender.equals("")))
			return repository.getAllUnfiltered();

		if (typePet.equals("Cachorro"))
			return repository.getAllFiltered(description, size, breed, typePet, city, district, gender, companyName);
		else
			return repository.getAllFilteredWithOutBreed(description, size, typePet, city, district, gender,
					companyName);
	}

	@Transactional
	public void delete(Pet pet) {
		for (User user : userService.getAll()) {
			if (user.getFavoritePets().contains(pet)) {
				user.getFavoritePets().remove(pet);
				userService.save(user);
			}
		}
		pet.setCompany(null);
		pet = save(pet);
		repository.delete(pet);
	}
	
	@Transactional
	@Modifying
	public void updateStatusPets() {
		repository.updateStatusPets(ESTATUS_PET.AVAILABLE);
	}
}
