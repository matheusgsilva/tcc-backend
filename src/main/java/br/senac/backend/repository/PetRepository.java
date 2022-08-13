package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Pet;

@Repository
public interface PetRepository extends PagingAndSortingRepository<Pet, Long> {

	@Query("SELECT c FROM Pet c WHERE c.company.guid = :companyGuid and c.active = 0")
	List<Pet> getByCompanyGuid(@Param("companyGuid") String userGuid);
	
	@Query("SELECT c FROM Pet c WHERE c.guid = :guid and c.active = 0")
	Pet getByGuid(@Param("guid") String guid);
}
