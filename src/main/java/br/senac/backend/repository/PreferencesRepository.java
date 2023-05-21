package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Preferences;

@Repository
public interface PreferencesRepository extends PagingAndSortingRepository<Preferences, Long> {

	@Query("SELECT p FROM Preferences p WHERE p.user.guid = :userGuid")
	List<Preferences> getByUserGuid(@Param("userGuid") String userGuid);
	
	@Query("SELECT p FROM Preferences p WHERE p.guid = :guid")
	Preferences getByGuid(@Param("guid") String guid);

	@Query("SELECT distinct p.user.email from Preferences p WHERE ((:age is null or p.age LIKE CONCAT('%',:age,'%')) "
			+ "or (:size is null or p.size LIKE CONCAT('%',:size,'%')) "
			+ "or (:breed is null or p.breed LIKE CONCAT('%',:breed,'%')) "
			+ "or (:gender is null or p.gender LIKE CONCAT('%',:gender,'%'))) "
			+ "and (:typePet is null or p.typePet LIKE CONCAT('%',:typePet,'%'))")
	List<String> findPreferences(@Param("age") String age, @Param("size") String size,
			@Param("breed") String breed, @Param("typePet") String typePet, @Param("gender") String gender);
}
