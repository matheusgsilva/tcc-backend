package br.senac.backend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.Notification;

@Repository
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {

	@Query("SELECT n FROM Notification n where n.user.guid = :userGuid order by n.date desc")
	List<Notification> getByUserGuid(@Param("userGuid") String userGuid, Pageable pageable);
}
