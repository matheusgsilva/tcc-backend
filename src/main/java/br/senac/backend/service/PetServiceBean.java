package br.senac.backend.service;

import java.util.List;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import br.senac.backend.model.Company;
import br.senac.backend.model.Pet;
import br.senac.backend.model.User;
import br.senac.backend.repository.PetRepository;
import br.senac.backend.task.CompanyEmailTask;
import br.senac.backend.task.SupportEmailTask;
import br.senac.backend.task.TypePetEmailTask;
import br.senac.backend.util.ECOMPANY_PERMISSION;
import br.senac.backend.util.ESTATUS_PET;

@Service
public class PetServiceBean implements PetService {

	@Autowired
	private PetRepository repository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private ImageVerificationServiceBean imageVerificationServiceBean;

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
	public void updateStatusPets(String guid) {
		repository.updateStatusPets(guid, ESTATUS_PET.AVAILABLE);
	}

	public Integer getDaysSinceReservationByGuid(String guid) {
		return repository.getDaysSinceReservationByGuid(guid);
	}

	public List<Pet> getByStatusAndUser(String userGuid, ESTATUS_PET status) {
		return repository.getByStatusAndUser(userGuid, status);
	}

	public List<Pet> getByStatusAndCompany(String companyGuid, ESTATUS_PET status) {
		return repository.getByStatusAndCompany(companyGuid, status);
	}

	public List<Pet> getByStatus(ESTATUS_PET status) {
		return repository.getByStatus(ESTATUS_PET.RESERVED);
	}

	public void verifyPetImage(Pet pet) {
	    if (pet == null) {
	        return;
	    }
	    String base64Image = pet.getPhoto1();
	    if (base64Image == null || base64Image.equals("")) {
	        return;
	    }
	    String response = imageVerificationServiceBean.getImageVerification(base64Image);
	    
	    JSONObject jsonResponse = new JSONObject(response);
	    String result = jsonResponse.getString("result");
	    
	    if (!result.contains("Detectado")) {
	    	Company company = pet.getCompany();
	        company.setPermission(ECOMPANY_PERMISSION.UNAUTHORIZED);
	        companyService.save(company);
	        CompanyEmailTask accountTask = applicationContext.getBean(CompanyEmailTask.class);
			accountTask.setCompany(company);
			taskExecutor.execute(accountTask);
			SupportEmailTask supportEmailTask = applicationContext.getBean(SupportEmailTask.class);
			supportEmailTask.setCompany(company);
			supportEmailTask.setPet(pet);
			taskExecutor.execute(supportEmailTask);
	    }
	    
	    String detectedAnimal = result.split(": ")[1].trim();
	    
	    if (!pet.getTypePet().equalsIgnoreCase(detectedAnimal)) {
	    	TypePetEmailTask typePetEmailTask = applicationContext.getBean(TypePetEmailTask.class);
	    	Company company = pet.getCompany();
	    	typePetEmailTask.setCompany(company);
	    	typePetEmailTask.setPet(pet);
			taskExecutor.execute(typePetEmailTask);
			repository.getByCompanyGuid(company.getGuid()).forEach(p -> {
				p.setStatus(ESTATUS_PET.AVAILABLE);
				save(p);
			});
	    }
	}
}
