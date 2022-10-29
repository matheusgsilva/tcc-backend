package br.senac.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.senac.backend.model.Company;
import br.senac.backend.repository.CompanyRepository;
import br.senac.backend.util.EACTIVE;

@Service
public class CompanyServiceBean implements CompanyService {

	@Autowired
	private CompanyRepository repository;

	public Company getByGuid(String guid) {
		return repository.getByGuid(guid);
	}
	
	public List<Company> getAll() {
		List<Company> listByClassinfications = repository.getAllByClassification();
		List<Company> list = repository.getAll();
		list.removeAll(listByClassinfications);
		List<Company> listReturn = new ArrayList<Company>();
		listReturn.addAll(listByClassinfications);
		listReturn.addAll(list);
		return listReturn;
	}

	@Transactional
	public Company save(Company company) {
		return repository.save(company);
	}

	public Company getByLoginPassword(String email, String password) {
		if ((!email.equals("")) && (!password.equals(""))) {
			Company u = repository.getByEmail(email);
			if (u != null)
				if (u.getActive().equals(EACTIVE.YES))
					if (BCrypt.checkpw(password, u.getPassword()))
						return u;
		}
		return null;
	}

	public Boolean isExists(String name, String email, String document) {
		return repository.isExists(name, document, email);
	}

	public Boolean isExists(String name, String email, String document, String guid) {
		return repository.isExists(name, document, email, guid);
	}
}
