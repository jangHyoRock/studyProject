package dhi.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.UserEntity;

/*
 * JAP user repository.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

	List<UserEntity> findByEnabledTrue();
	
	UserEntity findByUserId(String userId);
	
	List<UserEntity> findByRoleIdNot(String roleId);
}