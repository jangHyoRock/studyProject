package dhi.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.UserRoleEntity;

/*
 * JAP user repository.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {
	
	@Modifying
    @Query(value = "Insert into TB_USER_MENU_INFO ( SYS_ID, ROLE_ID, MENU_ID, MENU_ORDER) " +
            " select sys_id, ?2, menu_id, menu_order from TB_MENU where sys_id = ?1 and menu_id = ?3 ", nativeQuery = true)
    int insertTbUserMenuInfo(String sys_id, String role_id, String menu_id);

    @Modifying
    @Query(value = " DELETE from TB_USER_MENU_INFO where ROLE_ID = ?1 ", nativeQuery = true)
    int deleteTbUserMenuInfoByRoleId(String roleId);
	
	List<UserRoleEntity> findByRoleIdNot(String roleId);
	
	UserRoleEntity findByRoleId(String roleId);
	
	List<UserRoleEntity> findByEnabled(boolean b);
}