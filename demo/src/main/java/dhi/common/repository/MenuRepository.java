package dhi.common.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.MenuEntity;
/*
 * JAP user repository.
 */
@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, String> {

	@Query(value = "SELECT B.* "
					+ "FROM TB_USER_MENU_INFO A INNER JOIN TB_MENU B "
					+ "ON A.SYS_ID = B.SYS_ID "
					+ "AND A.MENU_ID = B.MENU_ID "
					+ "AND A.SYS_ID =?1 "
					+ "AND A.ROLE_ID =?2 "
					+ "AND IFNULL(B.PARENT_MENU, '') = '' "
					+ "ORDER BY A.MENU_ORDER ASC", nativeQuery = true)
	List<MenuEntity> findBySysIdAndRoleIdJoinUserMenuInfoNativeQuery(String systemId, String roleId);	

	@Query(value = "SELECT B.* "
					+ "FROM TB_USER_MENU_INFO A INNER JOIN TB_MENU B "
					+ "ON A.SYS_ID = B.SYS_ID "
					+ "AND A.MENU_ID = B.MENU_ID "
					+ "AND A.SYS_ID =?1 "
					+ "AND A.ROLE_ID =?2 "
					+ "AND IFNULL(B.PARENT_MENU, '') != '' "
					+ "ORDER BY B.PARENT_MENU, A.MENU_ORDER ASC", nativeQuery = true)
	List<MenuEntity> findBySysIdAndParentMenuIsNotNullSubMenuInfoNativeQuery(String systemId, String roleId);
	
	@Query(value = "SELECT B.* "
			+ "FROM TB_USER_MENU_INFO A INNER JOIN TB_MENU B "
			+ "ON A.SYS_ID = B.SYS_ID "
			+ "AND A.MENU_ID = B.MENU_ID "
			+ "AND A.ROLE_ID =?1 "
			+ "AND IFNULL(B.PARENT_MENU, '') = '' "
			+ "ORDER BY B.PARENT_MENU, A.MENU_ORDER ASC", nativeQuery = true)
	List<MenuEntity> findByRoleIdAndParentMenuIsNullNativeQuery(String roleId);
	
	@Query(value = "SELECT B.* "
			+ "FROM TB_USER_MENU_INFO A INNER JOIN TB_MENU B "
			+ "ON A.SYS_ID = B.SYS_ID "
			+ "AND A.MENU_ID = B.MENU_ID "
			+ "AND A.ROLE_ID =?1 "
			+ "ORDER BY B.PARENT_MENU, A.MENU_ORDER ASC", nativeQuery = true)
	List<MenuEntity> findByRoleIdNativeQuery(String roleId);
	
	@Query(value = "select * "  
			+ "from TB_MENU "  
			+ "order by sys_id, parent_menu , menu_order", nativeQuery = true)
	List<MenuEntity> findAllMenuList();
	
	@Modifying
	@Query(value = "upsert TB_MENU values(?1, ?2, ?3, ?4, ?5, ?6) where sys_id = ?4 and menu_id = ?1", nativeQuery = true)
	void upsertMenuInfo(String menuId, String menuNm, String parentMenu, String sysId, String description, int menuOrder);
	
	@Modifying
	@Query(value = "insert into TB_MENU values(?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
	void insertMenuInfo(String menuId, String menuNm, String parentMenu, String sysId, String description, int menuOrder);
	
	@Modifying
	@Query(value = "delete from TB_MENU where menu_id = ?1 and sys_id = ?2", nativeQuery = true)
	void deleteMenuInfo(String menuId, String sysId);
	
	@Query(value = "select MAX(menu_order) "  
			+ "from TB_MENU "  
			+ "where sys_id = ?1", nativeQuery = true)
	int findMaxMenuOrderBySysId(String sysId);
	
	@Query(value = "select MAX(menu_order) "  
			+ "from TB_MENU "  
			+ "where sys_id = ?1 and parent_menu = ?2", nativeQuery = true)
	int findMaxMenuOrderBySysIdAndParentMenu(String sysId, String parentMenu);
}