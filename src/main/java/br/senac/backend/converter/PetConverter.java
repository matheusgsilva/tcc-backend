package br.senac.backend.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.senac.backend.model.Company;
import br.senac.backend.model.Pet;
import br.senac.backend.request.PetRequest;
import br.senac.backend.response.PetResponse;
import br.senac.backend.service.CompanyService;
import br.senac.backend.util.EACTIVE;

@Component
public class PetConverter {

	@Autowired
	private CompanyService companyService;

	public Pet petSave(PetRequest petRequest, String companyGuid) {

		try {
			Company company = companyService.getByGuid(companyGuid);
			if (company == null)
				return null;

			Pet pet = new Pet();
			pet.setAge(petRequest.getAge());
			pet.setBreed(petRequest.getBreed());
			pet.setDescription(petRequest.getDescription());
			pet.setGuid(UUID.randomUUID().toString());
			pet.setMedication(petRequest.getMedication());
			pet.setSize(petRequest.getSize());
			pet.setActive(EACTIVE.YES);
			pet.setPhoto1(petRequest.getPhoto1());
			pet.setPhoto2(petRequest.getPhoto2());
			pet.setPhoto3(petRequest.getPhoto3());
			pet.setPhoto4(petRequest.getPhoto4());
			pet.setCompany(company);
			pet.setVaccines(petRequest.getVaccines());
			pet.setTypePet(petRequest.getTypePet());
			pet.setGender(petRequest.getGender());
			return pet;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Pet petUpdate(PetRequest petRequest, Pet pet) {

		try {
			pet.setAge(petRequest.getAge());
			pet.setBreed(petRequest.getBreed());
			pet.setDescription(petRequest.getDescription());
			pet.setMedication(petRequest.getMedication());
			pet.setSize(petRequest.getSize());
			pet.setVaccines(petRequest.getVaccines());
			pet.setPhoto1(petRequest.getPhoto1());
			pet.setPhoto2(petRequest.getPhoto2());
			pet.setPhoto3(petRequest.getPhoto3());
			pet.setPhoto4(petRequest.getPhoto4());
			pet.setTypePet(petRequest.getTypePet());
			pet.setGender(petRequest.getGender());
			return pet;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public PetResponse petToResponse(Pet pet) {

		try {
			PetResponse petResponse = new PetResponse();
			petResponse.setAge(pet.getAge());
			petResponse.setBreed(pet.getBreed());
			petResponse.setDescription(pet.getDescription());
			petResponse.setGuid(pet.getGuid());
			petResponse.setMedication(pet.getMedication());
			petResponse.setSize(pet.getSize());
			if (pet.getCompany() != null) {
				petResponse.setCompany(pet.getCompany().getName());
				petResponse.setCompanyGuid(pet.getCompany().getGuid());
				petResponse.setCompanyPhone(pet.getCompany().getPhone());
			}
			petResponse.setVaccines(pet.getVaccines());
			petResponse.setPhoto1(pet.getPhoto1());
			petResponse.setPhoto2(pet.getPhoto2());
			petResponse.setPhoto3(pet.getPhoto3());
			petResponse.setPhoto4(pet.getPhoto4());
			petResponse.setTypePet(pet.getTypePet());
			petResponse.setGender(pet.getGender());
			return petResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<PetResponse> petsToResponseList(List<Pet> pets) {

		try {
			List<PetResponse> list = new ArrayList<PetResponse>();
			for (Pet pet : pets) {
				PetResponse petResponse = new PetResponse();
				petResponse.setAge(pet.getAge());
				petResponse.setBreed(pet.getBreed());
				petResponse.setDescription(pet.getDescription());
				petResponse.setGuid(pet.getGuid());
				petResponse.setMedication(pet.getMedication());
				petResponse.setSize(pet.getSize());
				if (pet.getCompany() != null) {
					petResponse.setCompany(pet.getCompany().getName());
					petResponse.setCompanyGuid(pet.getCompany().getGuid());
				}
				petResponse.setVaccines(pet.getVaccines());
				petResponse.setPhoto1(pet.getPhoto1());
				petResponse.setTypePet(pet.getTypePet());
				petResponse.setGender(pet.getGender());
				list.add(petResponse);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
