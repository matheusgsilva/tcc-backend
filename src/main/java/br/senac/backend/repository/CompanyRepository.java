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
	
	@Query(value = "SELECT *, sum(r.classification) as sumClassifications "
			+ "FROM company as c "
			+ "inner join rating as r on r.company = c.id "
			+ "group by c.name "
			+ "order by sumClassifications desc", nativeQuery = true)
	List<Company> getAllByClassification();
	
	@Query("SELECT c FROM Company c")
	List<Company> getAll();
	
	@Query("SELECT c.name FROM Company c")
	List<String> getNames();
	
	@Query("SELECT distinct c.city FROM Company c")
	List<String> getCities();
	
	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Company u WHERE u.email = :email")
	Boolean isExists( @Param("email") String email);

	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Company u WHERE (u.name = :name or u.document = :document or u.email = :email)")
	Boolean isExists(@Param("name") String name, @Param("document") String document, @Param("email") String email);
	
	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Company u WHERE (u.name = :name or u.document = :document or u.email = :email) and u.guid <> :guid")
	Boolean isExists(@Param("name") String name, @Param("document") String document, @Param("email") String email, @Param("guid") String guid);
}
