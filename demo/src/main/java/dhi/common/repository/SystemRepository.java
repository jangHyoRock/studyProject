package dhi.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.SystemEntity;

/*
 * JAP user repository.
 */
@Repository
public interface SystemRepository extends JpaRepository<SystemEntity, String> {

	@Query(value = "SELECT * FROM TB_SYSTEM WHERE SYS_ID IN ( SELECT DISTINCT SYS_ID FROM TB_USER_MENU_INFO WHERE ROLE_ID =?1) ORDER BY SYS_ORDER", nativeQuery = true)
	List<SystemEntity> findBySysIdInUserMenuInfoByRoleIdNativeQuery(String roleId);
}