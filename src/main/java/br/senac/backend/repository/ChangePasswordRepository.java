package br.senac.backend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.senac.backend.model.ChangePassword;
import br.senac.backend.util.EACTIVE;

@Repository
public interface ChangePasswordRepository extends PagingAndSortingRepository<ChangePassword, String> {

	@Query("SELECT c FROM ChangePassword c WHERE c.guid = :guid AND c.active = :active")
	ChangePassword findByGuid(String guid, EACTIVE active);

	@Query("SELECT c FROM ChangePassword c WHERE c.userGuid = :userGuid AND c.active = :active")
	ChangePassword findByUserGuid(String userGuid, EACTIVE active);
}