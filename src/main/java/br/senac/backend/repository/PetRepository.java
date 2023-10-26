package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Pet;
import br.senac.backend.util.ESTATUS_PET;

@Repository
public interface PetRepository extends PagingAndSortingRepository<Pet, Long> {

	@Query("SELECT c FROM Pet c WHERE c.company.guid = :companyGuid and c.status < 3")
	List<Pet> getByCompanyGuid(@Param("companyGuid") String companyGuid);

	@Query("SELECT c from Pet c WHERE ((:description is null or c.description LIKE CONCAT('%',:description,'%')) "
			+ "and (:size is null or c.size LIKE CONCAT('%',:size,'%')) "
			+ "and (:breed is null or c.breed LIKE CONCAT('%',:breed,'%'))"
			+ "and (:typePet is null or c.typePet LIKE CONCAT('%',:typePet,'%')) "
			+ "and (:gender is null or c.gender LIKE CONCAT('%',:gender,'%')) "
			+ "and (:city is null or c.company.city LIKE CONCAT('%',:city,'%')) "
			+ "and (:companyName is null or c.company.name LIKE CONCAT('%',:companyName,'%')) "
			+ "and (:district is null or c.company.district LIKE CONCAT('%',:district,'%'))) and c.status < 3")
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
			+ "and (:district is null or c.company.district LIKE CONCAT('%',:district,'%'))) and c.status < 3")
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
			+ "and c.company.guid = :companyGuid and c.status < 3")
	List<Pet> getAllFilteredCompany(@Param("description") String description, @Param("size") String size,
			@Param("breed") String breed, @Param("typePet") String typePet, @Param("city") String city,
			@Param("district") String district, @Param("companyGuid") String companyGuid,
			@Param("gender") String gender);

	@Query("SELECT CASE WHEN (COUNT(p) > 0) THEN true ELSE false END FROM Pet p WHERE "
			+ "((:size is null or p.size LIKE CONCAT('%',:size,'%')) "
			+ "and (:breed is null or p.breed LIKE CONCAT('%',:breed,'%')) "
			+ "and (:gender is null or p.gender LIKE CONCAT('%',:gender,'%'))) "
			+ "and (:typePet is null or p.typePet LIKE CONCAT('%',:typePet,'%')) and p.status < 3")
	Boolean isExists(@Param("size") String size, @Param("breed") String breed, @Param("typePet") String typePet,
			@Param("gender") String gender);

	@Query("SELECT c from Pet c WHERE c.status < 3")
	List<Pet> getAllUnfiltered();

	@Query("SELECT c from Pet c WHERE c.company.guid = :companyGuid and c.status < 3")
	List<Pet> getAllUnfilteredCompany(@Param("companyGuid") String companyGuid);

	@Query("SELECT c FROM Pet c WHERE c.guid = :guid and c.status < 3")
	Pet getByGuid(@Param("guid") String guid);

	@Modifying
	@Query(value = "UPDATE pets "
			+ "JOIN company ON pets.company = company.id "
			+ "SET pets.status = :status, pets.reservation_date = NULL, pets.user = NULL "
			+ "WHERE pets.guid = :guid and pets.status < 3 AND company.days_pet_reservation IS NOT NULL "
			+ "AND pets.reservation_date IS NOT NULL "
			+ "AND (DATEDIFF(CURRENT_DATE, pets.reservation_date) - company.days_pet_reservation) >= 0", nativeQuery = true)
	void updateStatusPets(@Param("guid") String guid, @Param("status") ESTATUS_PET status);

	@Query("SELECT DATEDIFF(CURRENT_DATE, p.reservationDate) FROM Pet p WHERE p.guid = :guid and p.status < 3")
	Integer getDaysSinceReservationByGuid(@Param("guid") String guid);
	
	@Query("SELECT p FROM Pet p WHERE p.adopterUser.guid = :userGuid AND p.status = :status")
	List<Pet> getByStatusAndUser(@Param("userGuid") String userGuid, @Param("status") ESTATUS_PET status);
	
	@Query("SELECT p FROM Pet p WHERE p.company.guid = :companyGuid AND p.status = :status")
	List<Pet> getByStatusAndCompany(@Param("companyGuid") String companyGuid, @Param("status") ESTATUS_PET status);
	
	@Query("SELECT p FROM Pet p WHERE p.status = :status")
	List<Pet> getByStatus(@Param("status") ESTATUS_PET status);

}
