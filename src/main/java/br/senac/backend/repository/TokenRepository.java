package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Company;
import br.senac.backend.model.Token;
import br.senac.backend.model.User;

@Repository
public interface TokenRepository extends PagingAndSortingRepository<Token, Long> {

	@Query("SELECT c FROM Token c WHERE c.token = :token")
	Token getByToken(@Param("token") String token);

	@Query("SELECT c FROM Token c WHERE c.id = (SELECT MAX(c.id) FROM Token c WHERE c.user = :user)")
	Token getLastUserTokenForToday(@Param("user") User user);
	
	@Query("SELECT c FROM Token c WHERE c.user = :user")
	List<Token> getAllByUser(@Param("user") User user);
	
	@Query("SELECT c FROM Token c WHERE c.company = :company")
	List<Token> getAllByCompany(@Param("company") Company company);
	
	@Query("SELECT c FROM Token c WHERE c.id = (SELECT MAX(c.id) FROM Token c WHERE c.company = :company)")
	Token getLastCompanyTokenForToday(@Param("company") Company company);

	@Query("SELECT c FROM Token c WHERE c.user = :user")
	List<Token> getAccessByUser(@Param("user") User user);
	
	@Query("Delete FROM Token c WHERE c.company.guid = :guid")
	void delete(@Param("guid") String guid);
}