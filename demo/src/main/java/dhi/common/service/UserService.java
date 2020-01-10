package dhi.common.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import dhi.common.exception.InvalidParameterException;
import dhi.common.model.json.MenuInfo;
import dhi.common.model.json.RoleInfo;
import dhi.common.model.json.UserCommonInfo;
import dhi.common.model.json.UserInfo;
import dhi.common.model.json.UserMenu;

/*
 * Login and user management service interface.
 */
public interface UserService extends UserDetailsService {
	
	public UserInfo getUserInfoAfterLogin(String userId);
	
	public List<UserCommonInfo> getUserSystemInfoList(String roleId, String userId, String requestHost);
	
	public List<UserMenu> getUserMenuList(String systemId, String roleId);
	
	public boolean isUserIdDuplicate(String userId);
	
	public void createUser(UserInfo userInfo);
	
	public void updateUsers(List<UserInfo> userInfoList);
	
	public void saveUserPassword(String userId, String userPassword, String passwordUpdateYn);
	
	public List<UserInfo> getUserList(String roleId) throws InvalidParameterException;
	
	public List<UserCommonInfo> getRoleList(String roldId) throws InvalidParameterException ;

	List<MenuInfo> getMenuAll();
	
	List<RoleInfo> getRoleAll();

	List<RoleInfo> getRoleEnabledAll();
	
	void insertMenu(MenuInfo menuInfo);
	
	void upsertMenu(List<MenuInfo> menuInfo);
	
	void deleteMenu(MenuInfo menuInfo);

	void createRole(RoleInfo roleInfo);

	void updateRoles(List<RoleInfo> roleInfoList);

	void deleteRole(String roleId);
}
