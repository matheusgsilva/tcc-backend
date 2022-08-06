package br.senac.backend.repository;

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

	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Company u WHERE (u.nome = :nome or u.documento = :documento or u.email = :email) and u.ativo = 0")
	Boolean isExists(@Param("nome") String name, @Param("documento") String documento, @Param("email") String email);
	
	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Company u WHERE (u.nome = :nome or u.documento = :documento or u.email = :email) and u.guid <> :guid and u.ativo = 0")
	Boolean isExists(@Param("nome") String name, @Param("documento") String documento, @Param("email") String email, @Param("guid") String guid);
}
