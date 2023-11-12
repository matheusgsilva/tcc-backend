package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Notification;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {

	@Query("SELECT n FROM Notification n WHERE n.user.guid = :userGuid ORDER BY date desc")
	List<Notification> getByUserGuid(@Param("userGuid") String userGuid);
	
	@Query("SELECT n FROM Notification n WHERE n.guid = :guid")
	Notification getByGuid(@Param("guid") String guid);
	
	@Modifying
	@Query("DELETE FROM Notification n WHERE n.user.guid = :userGuid")
	void deleteByUser(@Param("userGuid") String userGuid);

}
