package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Pet;

@Repository
public interface PetRepository extends PagingAndSortingRepository<Pet, Long> {

	@Query("SELECT c FROM Pet c WHERE c.company.guid = :companyGuid")
	List<Pet> getByCompanyGuid(@Param("companyGuid") String companyGuid);

	@Query("SELECT c from Pet c WHERE ((:description is null or c.description LIKE CONCAT('%',:description,'%')) "
			+ "and (:size is null or c.size LIKE CONCAT('%',:size,'%')) "
			+ "and (:breed is null or c.breed LIKE CONCAT('%',:breed,'%'))"
			+ "and (:typePet is null or c.typePet LIKE CONCAT('%',:typePet,'%')) "
			+ "and (:gender is null or c.gender LIKE CONCAT('%',:gender,'%')) "
			+ "and (:city is null or c.company.city LIKE CONCAT('%',:city,'%')) "
			+ "and (:companyName is null or c.company.name LIKE CONCAT('%',:companyName,'%')) "
			+ "and (:district is null or c.company.district LIKE CONCAT('%',:district,'%')))")
	List<Pet> getAllFiltered(@Param("description") String description, @Param("size") String size,
			@Param("breed") String breed, @Param("typePet") String typePet, @Param("city") String city,
			@Param("district") String district, @Param("gender") String gender,
			@Param("companyName") String companyName);

	@Query("SELECT c from Pet c WHERE ((:description is null or c.description LIKE CONCAT('%',:description,'%')) "
			+ "and (:size is null or c.size LIKE CONCAT('%',:size,'%')) "
			+ "and (:typePet is null or c.typePet LIKE CONCAT('%',:typePet,'%')) "
			+ "and (:gender is null or c.gender LIKE CONCAT('%',:gender,'%')) "
			+ "and (:city is null or c.company.city LIKE CONCAT('%',:city,'%')) "
			+ "and (:companyName is null or c.company.name LIKE CONCAT('%',:companyName,'%')) "
			+ "and (:district is null or c.company.district LIKE CONCAT('%',:district,'%')))")
	List<Pet> getAllFilteredWithOutBreed(@Param("description") String description, @Param("size") String size,
			@Param("typePet") String typePet, @Param("city") String city, @Param("district") String district,
			@Param("gender") String gender, @Param("companyName") String companyName);

	@Query("SELECT c from Pet c WHERE ((:description is null or c.description LIKE CONCAT('%',:description,'%')) "
			+ "and (:size is null or c.size LIKE CONCAT('%',:size,'%')) "
			+ "and (:breed is null or c.breed LIKE CONCAT('%',:breed,'%'))"
			+ "and (:typePet is null or c.typePet LIKE CONCAT('%',:typePet,'%')) "
			+ "and (:gender is null or c.gender LIKE CONCAT('%',:gender,'%')) "
			+ "and (:city is null or c.company.city LIKE CONCAT('%',:city,'%')) "
			+ "and (:district is null or c.company.district LIKE CONCAT('%',:district,'%'))) "
			+ "and c.company.guid = :companyGuid")
	List<Pet> getAllFilteredCompany(@Param("description") String description, @Param("size") String size,
			@Param("breed") String breed, @Param("typePet") String typePet, @Param("city") String city,
			@Param("district") String district, @Param("companyGuid") String companyGuid,
			@Param("gender") String gender);

	@Query("SELECT CASE WHEN (COUNT(p) > 0) THEN true ELSE false END FROM Pet p WHERE "
			+ "((:size is null or p.size LIKE CONCAT('%',:size,'%')) "
			+ "and (:breed is null or p.breed LIKE CONCAT('%',:breed,'%')) "
			+ "and (:gender is null or p.gender LIKE CONCAT('%',:gender,'%'))) "
			+ "and (:typePet is null or p.typePet LIKE CONCAT('%',:typePet,'%'))")
	Boolean isExists(@Param("size") String size, @Param("breed") String breed, @Param("typePet") String typePet,
			@Param("gender") String gender);

	@Query("SELECT c from Pet c")
	List<Pet> getAllUnfiltered();

	@Query("SELECT c from Pet c WHERE c.company.guid = :companyGuid")
	List<Pet> getAllUnfilteredCompany(@Param("companyGuid") String companyGuid);

	@Query("SELECT c FROM Pet c WHERE c.guid = :guid")
	Pet getByGuid(@Param("guid") String guid);
}
