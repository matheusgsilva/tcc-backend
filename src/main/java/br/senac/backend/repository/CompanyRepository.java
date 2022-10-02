package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Company;

@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long>{
	
	@Query("SELECT c FROM Company c WHERE c.guid = :guid")
	Company getByGuid(@Param("guid") String guid);
	
	@Query("SELECT c FROM Company c WHERE c.email = :email")
	Company getByEmail(@Param("email") String email);
	
	@Query("SELECT c FROM Company c WHERE c.active = 0")
	List<Company> getAll();

	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Company u WHERE (u.name = :name or u.document = :document or u.email = :email) and u.active = 0")
	Boolean isExists(@Param("name") String name, @Param("document") String document, @Param("email") String email);
	
	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Company u WHERE (u.name = :name or u.document = :document or u.email = :email) and u.guid <> :guid and u.active = 0")
	Boolean isExists(@Param("name") String name, @Param("document") String document, @Param("email") String email, @Param("guid") String guid);
}
