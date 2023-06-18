package br.senac.backend.converter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.senac.backend.model.Company;
import br.senac.backend.model.Pet;
import br.senac.backend.model.User;
import br.senac.backend.request.PetRequest;
import br.senac.backend.response.PetResponse;
import br.senac.backend.service.CompanyService;

@Component
public class PetConverter {

	@Autowired
	private CompanyService companyService;

	public Pet petSave(PetRequest petRequest, String companyGuid) {

		try {
			Company company = companyService.getByGuid(companyGuid);
			if (company == null)
				return null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Pet pet = new Pet();
			pet.setBirthDate(dateFormat.parse(petRequest.getBirthDate()));
			pet.setBreed(petRequest.getBreed());
			pet.setDescription(petRequest.getDescription());
			pet.setGuid(UUID.randomUUID().toString());
			pet.setColor(petRequest.getColor());
			pet.setSize(petRequest.getSize());
			pet.setPhoto1(petRequest.getPhoto1());
			pet.setPhoto2(petRequest.getPhoto2());
			pet.setPhoto3(petRequest.getPhoto3());
			pet.setPhoto4(petRequest.getPhoto4());
			pet.setCompany(company);
			pet.setVaccines(petRequest.getVaccines());
			pet.setTypePet(petRequest.getTypePet());
			pet.setGender(petRequest.getGender());
			pet.setIdentification(petRequest.getTypePet().equals("Cachorro") ? "CAO" + generateRandomNumber()
					: petRequest.getTypePet().equals("Gato") ? "GAT" + generateRandomNumber()
							: petRequest.getTypePet().equals("Passarinho") ? "PAS" + generateRandomNumber()
									: "COE" + generateRandomNumber());
			return pet;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Pet petUpdate(PetRequest petRequest, Pet pet) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			pet.setBirthDate(dateFormat.parse(petRequest.getBirthDate()));
			pet.setBreed(petRequest.getBreed());
			pet.setDescription(petRequest.getDescription());
			pet.setColor(petRequest.getColor());
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
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			PetResponse petResponse = new PetResponse();
			petResponse.setBirthDate(dateFormat.format(pet.getBirthDate()));
			petResponse.setBreed(pet.getBreed());
			petResponse.setDescription(pet.getDescription());
			petResponse.setGuid(pet.getGuid());
			petResponse.setColor(pet.getColor());
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
			petResponse.setIdentification(pet.getIdentification());
			return petResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public PetResponse petToResponse(Pet pet, User user) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			PetResponse petResponse = new PetResponse();
			petResponse.setBirthDate(dateFormat.format(pet.getBirthDate()));
			petResponse.setBreed(pet.getBreed());
			petResponse.setDescription(pet.getDescription());
			petResponse.setGuid(pet.getGuid());
			petResponse.setColor(pet.getColor());
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
			petResponse.setIdentification(pet.getIdentification());
			if (user != null)
				petResponse.setIsFavorite(user.getFavoritePets().contains(pet));
			return petResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<PetResponse> petsToResponseList(List<Pet> pets) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			List<PetResponse> list = new ArrayList<PetResponse>();
			for (Pet pet : pets) {
				PetResponse petResponse = new PetResponse();
				LocalDate birth = pet.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate now = LocalDate.now();
				Period periodo = Period.between(birth, now);
				int years = periodo.getYears();
				int months = periodo.getMonths();
				if (years != 0 && months != 0) {
					petResponse.setAge((years > 1 ? years + " anos" : "1 ano")
							+ (months > 1 ? " e " + months + " meses" : " e 1 mês"));
				} else if (years != 0) {
					petResponse.setAge(years > 1 ? years + " anos" : "1 ano");
				} else {
					petResponse.setAge(months > 1 ? months + " meses" : "1 mês");
				}
				petResponse.setBirthDate(dateFormat.format(pet.getBirthDate()));
				petResponse.setBreed(pet.getBreed());
				petResponse.setDescription(pet.getDescription());
				petResponse.setGuid(pet.getGuid());
				petResponse.setColor(pet.getColor());
				petResponse.setSize(pet.getSize());
				if (pet.getCompany() != null) {
					petResponse.setCompany(pet.getCompany().getName());
					petResponse.setCompanyGuid(pet.getCompany().getGuid());
				}
				petResponse.setVaccines(pet.getVaccines());
				petResponse.setPhoto1(pet.getPhoto1());
				petResponse.setTypePet(pet.getTypePet());
				petResponse.setGender(pet.getGender());
				petResponse.setIdentification(pet.getIdentification());
				list.add(petResponse);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(9999999 - 1000000 + 1) + 1000000;
	}
}
