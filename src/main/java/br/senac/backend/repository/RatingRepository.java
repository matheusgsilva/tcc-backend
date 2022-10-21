package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Rating;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {

	@Query("SELECT r FROM Rating r WHERE r.company.guid = :companyGuid and r.active = 0 order by r.date desc")
	List<Rating> getByCompanyGuid(@Param("companyGuid") String companyGuid);
	
	@Query("SELECT r FROM Rating r WHERE r.user.guid = :userGuid and r.company.guid = :companyGuid and r.active = 0")
	Rating getByUserAndCompany(@Param("userGuid") String userGuid, @Param("companyGuid") String companyGuid);
	
	@Query("SELECT r FROM Rating r WHERE r.guid = :guid")
	Rating getByGuid(@Param("guid") String guid);
}
