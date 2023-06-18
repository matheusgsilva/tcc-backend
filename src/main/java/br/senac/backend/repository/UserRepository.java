package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	@Query("SELECT c FROM User c WHERE c.email = :email and c.password = :password")
	User getByLoginPassword(@Param("email") String email, @Param("password") String password);
	
	@Query("SELECT c FROM User c WHERE c.email = :email")
	User getByEmail(@Param("email") String email);

	@Query("SELECT c FROM User c WHERE c.guid = :guid")
	User getByGuid(@Param("guid") String guid);
	
	@Query("SELECT c FROM User c")
	List<User> getAll();

	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM User u WHERE u.email = :email")
	Boolean isExists(@Param("email") String email);
	
	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM User u WHERE u.email = :email and u.guid <> :guid")
	Boolean isExists(@Param("email") String email, @Param("guid") String guid);
}