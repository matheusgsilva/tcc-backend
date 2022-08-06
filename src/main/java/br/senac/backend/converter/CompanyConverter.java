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
			company.setAtivo(EACTIVE.YES);
			company.setBairro(companyRequest.getBairro());
			company.setCep(companyRequest.getCep());
			company.setCidade(companyRequest.getCidade());
			company.setDescricao(companyRequest.getDescricao());
			company.setDocumento(companyRequest.getDocumento());
			company.setEmail(companyRequest.getEmail());
			company.setGuid(UUID.randomUUID().toString());
			company.setNome(companyRequest.getNome());
			company.setNumero(companyRequest.getNumero());
			company.setPassword(BCrypt.hashpw(companyRequest.getPassword(), BCrypt.gensalt()));
			company.setPermissao(ECOMPANY_PERMISSION.AUTHORIZED);
			company.setRua(companyRequest.getRua());
			company.setTelefone(companyRequest.getTelefone());
			company.setUf(companyRequest.getUf());
			return company;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Company companyUpdate(CompanyRequest companyRequest, Company company) {

		try {
			company.setBairro(companyRequest.getBairro());
			company.setCep(companyRequest.getCep());
			company.setCidade(companyRequest.getCidade());
			company.setDescricao(companyRequest.getDescricao());
			company.setDocumento(companyRequest.getDocumento());
			company.setEmail(companyRequest.getEmail());
			company.setNome(companyRequest.getNome());
			company.setNumero(companyRequest.getNumero());
			company.setPassword(BCrypt.hashpw(companyRequest.getPassword(), BCrypt.gensalt()));
			company.setRua(companyRequest.getRua());
			company.setTelefone(companyRequest.getTelefone());
			company.setUf(companyRequest.getUf());
			return company;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public CompanyResponse companyToResponse(Company company) {

		try {
			CompanyResponse companyResponse = new CompanyResponse();
			companyResponse.setBairro(company.getBairro());
			companyResponse.setCep(company.getCep());
			companyResponse.setCidade(company.getCidade());
			companyResponse.setDescricao(company.getDescricao());
			companyResponse.setDocumento(company.getDocumento());
			companyResponse.setEmail(company.getEmail());
			companyResponse.setGuid(company.getGuid());
			companyResponse.setNome(company.getNome());
			companyResponse.setNumero(company.getNumero());
			companyResponse.setRua(company.getRua());
			companyResponse.setTelefone(company.getTelefone());
			companyResponse.setUf(company.getUf());
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
				companyResponse.setBairro(company.getBairro());
				companyResponse.setCep(company.getCep());
				companyResponse.setCidade(company.getCidade());
				companyResponse.setDescricao(company.getDescricao());
				companyResponse.setDocumento(company.getDocumento());
				companyResponse.setEmail(company.getEmail());
				companyResponse.setGuid(company.getGuid());
				companyResponse.setNome(company.getNome());
				companyResponse.setNumero(company.getNumero());
				companyResponse.setRua(company.getRua());
				companyResponse.setTelefone(company.getTelefone());
				companyResponse.setUf(company.getUf());
				list.add(companyResponse);
			});
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
