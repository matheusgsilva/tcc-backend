package br.senac.backend.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import br.senac.backend.model.Company;
import br.senac.backend.request.CompanyRequest;
import br.senac.backend.response.CompanyResponse;
import br.senac.backend.util.EACTIVE;
import br.senac.backend.util.ECOMPANY_PERMISSION;

@Component
public class CompanyConverter {

	public Company companySave(CompanyRequest companyRequest) {

		try {
			Company company = new Company();
			company.setActive(EACTIVE.YES);
			company.setDistrict(companyRequest.getDistrict());
			company.setCep(companyRequest.getCep());
			company.setCity(companyRequest.getCity());
			company.setDescription(companyRequest.getDescription());
			company.setDocument(companyRequest.getDocument());
			company.setEmailAccess(companyRequest.getEmailAccess());
			company.setEmailContact(companyRequest.getEmailContact());
			company.setGuid(UUID.randomUUID().toString());
			company.setName(companyRequest.getName());
			company.setNumberAddress(companyRequest.getNumberAddress());
			company.setPassword(BCrypt.hashpw(companyRequest.getPassword(), BCrypt.gensalt()));
			company.setPermission(ECOMPANY_PERMISSION.AUTHORIZED);
			company.setStreet(companyRequest.getStreet());
			company.setPhone(companyRequest.getPhone());
			company.setUf(companyRequest.getUf());
			company.setCountry(companyRequest.getCountry());
			return company;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Company companyUpdate(CompanyRequest companyRequest, Company company) {

		try {
			company.setDistrict(companyRequest.getDistrict());
			company.setCep(companyRequest.getCep());
			company.setCity(companyRequest.getCity());
			company.setDescription(companyRequest.getDescription());
			company.setDocument(companyRequest.getDocument());
			company.setEmailAccess(companyRequest.getEmailAccess());
			company.setEmailContact(companyRequest.getEmailContact());
			company.setName(companyRequest.getName());
			company.setNumberAddress(companyRequest.getNumberAddress());
			company.setStreet(companyRequest.getStreet());
			company.setPhone(companyRequest.getPhone());
			company.setUf(companyRequest.getUf());
			company.setCountry(companyRequest.getCountry());
			return company;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public CompanyResponse companyToResponse(Company company) {

		try {
			CompanyResponse companyResponse = new CompanyResponse();
			companyResponse.setDistrict(company.getDistrict());
			companyResponse.setCep(company.getCep());
			companyResponse.setCity(company.getCity());
			companyResponse.setDescription(company.getDescription());
			companyResponse.setDocument(company.getDocument());
			companyResponse.setEmailAccess(company.getEmailAccess());
			companyResponse.setEmailContact(company.getEmailContact());
			companyResponse.setName(company.getName());
			companyResponse.setNumberAddress(company.getNumberAddress());
			companyResponse.setStreet(company.getStreet());
			companyResponse.setPhone(company.getPhone());
			companyResponse.setUf(company.getUf());
			companyResponse.setCountry(company.getCountry());
			companyResponse.setGuid(company.getGuid());
			return companyResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public CompanyResponse companyToResponseContact(Company company) {

		try {
			CompanyResponse companyResponse = new CompanyResponse();
			companyResponse.setEmailContact(company.getEmailContact());
			companyResponse.setPhone(company.getPhone());
			return companyResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<CompanyResponse> companyToResponseList(List<Company> companies) {

		try {
			List<CompanyResponse> list = new ArrayList<CompanyResponse>();
			companies.forEach(company -> {
				CompanyResponse companyResponse = new CompanyResponse();
				companyResponse.setDistrict(company.getDistrict());
				companyResponse.setCep(company.getCep());
				companyResponse.setCity(company.getCity());
				companyResponse.setDescription(company.getDescription());
				companyResponse.setDocument(company.getDocument());
				companyResponse.setEmailAccess(company.getEmailAccess());
				companyResponse.setEmailContact(company.getEmailContact());
				companyResponse.setName(company.getName());
				companyResponse.setNumberAddress(company.getNumberAddress());
				companyResponse.setStreet(company.getStreet());
				companyResponse.setPhone(company.getPhone());
				companyResponse.setUf(company.getUf());
				companyResponse.setCountry(company.getCountry());
				companyResponse.setGuid(company.getGuid());
				list.add(companyResponse);
			});
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
