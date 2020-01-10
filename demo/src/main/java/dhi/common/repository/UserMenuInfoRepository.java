package dhi.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.UserMenuInfoEntity;

/*
 * JAP user repository.
 */
@Repository
public interface UserMenuInfoRepository extends JpaRepository<UserMenuInfoEntity, String> {
	
}