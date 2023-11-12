package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Preferences;
import br.senac.backend.model.User;

@Repository
public interface PreferencesRepository extends PagingAndSortingRepository<Preferences, Long> {

	@Query("SELECT p FROM Preferences p WHERE p.user.guid = :userGuid")
	List<Preferences> getByUserGuid(@Param("userGuid") String userGuid);

	@Query("SELECT p FROM Preferences p WHERE p.guid = :guid")
	Preferences getByGuid(@Param("guid") String guid);

	@Query("SELECT u FROM User u WHERE u.email in (SELECT distinct p.user.email from Preferences p WHERE ((:size is null or p.size LIKE CONCAT('%',:size,'%')) "
			+ "or (:breed is null or p.breed LIKE CONCAT('%',:breed,'%')) "
			+ "or (:gender is null or p.gender LIKE CONCAT('%',:gender,'%'))) "
			+ "and (:typePet is null or p.typePet LIKE CONCAT('%',:typePet,'%')))")
	List<User> findPreferences(@Param("size") String size, @Param("breed") String breed,
			@Param("typePet") String typePet, @Param("gender") String gender);

	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Preferences u WHERE u.gender = :gender AND u.typePet = :typePet AND u.breed = :breed AND u.size = :size")
	Boolean isExists(@Param("gender") String gender, @Param("typePet") String typePet, @Param("breed") String breed,
			@Param("size") String size);

	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Preferences u WHERE u.gender = :gender AND u.typePet = :typePet AND u.size = :size")
	Boolean isExists(@Param("gender") String gender, @Param("typePet") String typePet, @Param("size") String size);

	@Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Preferences u WHERE (u.gender = :gender AND u.typePet = :typePet AND u.breed = :breed AND u.size = :size) and u.guid <> :guid")
	Boolean isExists(@Param("gender") String gender, @Param("typePet") String typePet, @Param("breed") String breed,
			@Param("guid") String guid, @Param("size") String size);
}
