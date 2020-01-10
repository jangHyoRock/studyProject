package dhi.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dhi.common.enumeration.SystemRole;
import dhi.common.exception.InvalidParameterException;
import dhi.common.model.db.MenuEntity;
import dhi.common.model.db.MenuEntityId;
import dhi.common.model.db.SystemEntity;
import dhi.common.model.db.UserEntity;
import dhi.common.model.db.UserRoleEntity;
import dhi.common.model.json.MenuInfo;
import dhi.common.model.json.RoleInfo;
import dhi.common.model.json.UserCommonInfo;
import dhi.common.model.json.UserInfo;
import dhi.common.model.json.UserMenu;
import dhi.common.repository.MenuRepository;
import dhi.common.repository.PlantUnitRepository;
import dhi.common.repository.SystemRepository;
import dhi.common.repository.UserRepository;
import dhi.common.repository.UserRoleRepository;

/*
 * Login and user management service.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	SystemRepository systemRepository;

	@Autowired
	MenuRepository menuRepository;
	
	@Autowired
	UserRoleRepository roleRepository;
	
	@Autowired
	PlantUnitRepository plantUnitRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.getOne(userId);
		if (userEntity == null) {
			throw new UsernameNotFoundException("Not Exist User");
		}

		return new org.springframework.security.core.userdetails.User(userEntity.getUserId(), 
				userEntity.getUserPassword(),
				userEntity.getAuthorities());
	}

	public UserInfo getUserInfoAfterLogin(String userId) {
		UserEntity userEntity = userRepository.getOne(userId);
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(userEntity.getUserName());
		userInfo.setPasswordUpdateYn(userEntity.getPasswordUpdateYn());

		UserRoleEntity userRoleEntity = roleRepository.getOne(userEntity.getRoleId());
		userInfo.setRoleId(userRoleEntity.getRoleId());
		userInfo.setRoleName(userRoleEntity.getRoleName());
		
		return userInfo;
	}
	
	public List<UserCommonInfo> getUserSystemInfoList(String roleId, String userId, String requestHost) {
		List<SystemEntity> systemEntityList = systemRepository.findBySysIdInUserMenuInfoByRoleIdNativeQuery(roleId);
		List<UserCommonInfo> userSystemInfoList = new ArrayList<UserCommonInfo>();
		
		UserEntity userEntity = userRepository.getOne(userId);
		List<Object[]> plantInfoList = plantUnitRepository.findByAllPlantIdWithIpWithSysIdNativeQuery(requestHost);
		
		for (SystemEntity systemEntity : systemEntityList) {
			UserCommonInfo userSystemInfo = new UserCommonInfo();
			userSystemInfo.setSystemId(systemEntity.getSystemId());
			userSystemInfo.setSystemName(systemEntity.getSystemName());

			boolean isSamePlantUnitId = false;
			Object[] firstPlantUnitObject = null;
			Object[] defaultPlantUnitObject = null;
			for (Object[] plantInfo : plantInfoList) {
				String objSysmtemId = String.valueOf(plantInfo[3]);
				String objUnitId = String.valueOf(plantInfo[0]);
				if (objSysmtemId.equals(systemEntity.getSystemId())) {
					
					if(firstPlantUnitObject == null)
						firstPlantUnitObject = plantInfo;
					
					if (objUnitId.equals(userEntity.getDefaultPlantUnitId())) {
						defaultPlantUnitObject = plantInfo;
						isSamePlantUnitId = true;
						break;
					}
				}
			}

			if (!isSamePlantUnitId) {
				defaultPlantUnitObject = firstPlantUnitObject;
			}

			if (defaultPlantUnitObject != null) {
				userSystemInfo.setDefaultPlantId(String.valueOf(defaultPlantUnitObject[0]));
				userSystemInfo.setDefaultPlantIp(String.valueOf(defaultPlantUnitObject[1]));
				userSystemInfo.setDefaultPlantPort(String.valueOf(defaultPlantUnitObject[2]));
			}
			
			userSystemInfoList.add(userSystemInfo);
		}
		
		return userSystemInfoList;
	}
	
	public List<UserMenu> getUserMenuList(String systemId, String roleId) {

		List<MenuEntity> menuEntityList = menuRepository.findBySysIdAndRoleIdJoinUserMenuInfoNativeQuery(systemId, roleId);
		List<MenuEntity> subMenuEntityList = menuRepository.findBySysIdAndParentMenuIsNotNullSubMenuInfoNativeQuery(systemId, roleId);
		
		List<UserMenu> userMenuList = new ArrayList<UserMenu>();
		for (MenuEntity menuEntity : menuEntityList) {
			UserMenu userMenu = new UserMenu();

			String menuId = menuEntity.getMenuEntityId().getMenuId();
			userMenu.setMenuId(menuId);
			userMenu.setMenuName(menuEntity.getMenuName());

			// Set Sub Menu
			List<UserMenu> subMenuList = new ArrayList<UserMenu>();
			for (MenuEntity subMenuEntity : subMenuEntityList) {
				if (menuId.equals(subMenuEntity.getParentMenu())) {
					subMenuList.add(new UserMenu(subMenuEntity.getMenuEntityId().getMenuId(), subMenuEntity.getMenuName()));
				}
			}
			
			if(subMenuList.size() > 0)
				userMenu.setSubMenuList(subMenuList);

			userMenuList.add(userMenu);
		}

		return userMenuList;
	}
	
	public boolean isUserIdDuplicate(String userId)
	{
		boolean isDuplicate = false;
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity != null) {
			isDuplicate = true;
		}
		
		return isDuplicate;
	}
	
	public void createUser(UserInfo userInfo) {
		UserEntity userEntity = userRepository.findByUserId(userInfo.getUserId());
		if (userEntity == null)
			userEntity = new UserEntity(userInfo.getUserId(), userInfo.getUserName(), userInfo.getRoleId(), true, userInfo.getDefaultPlantId(), new Date());

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		userEntity.setUserPassword(passwordEncoder.encode(userInfo.getUserPassword()));
		userEntity.setPasswordUpdateYn("Y");

		entityManager.persist(userEntity);
		entityManager.flush();
		entityManager.close();
	}
	
	public void updateUsers(List<UserInfo> userInfoList) {
		
		for (UserInfo userInfo : userInfoList) {
			UserEntity userEntity = userRepository.getOne(userInfo.getUserId());
			userEntity.setUserName(userInfo.getUserName());
			userEntity.setRoleId(userInfo.getRoleId());
			userEntity.setDefaultPlantUnitId(userInfo.getDefaultPlantId());
			userEntity.setEnabled(userInfo.getUserEnabled());
			userEntity.setRegDate(new Date());

			entityManager.persist(userEntity);
			entityManager.flush();
			entityManager.close();
		}
	}
	
	public void saveUserPassword(String userId, String userPassword, String passwordUpdateYn) {
		UserEntity userEntity = userRepository.findByUserId(userId);

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		userEntity.setUserPassword(passwordEncoder.encode(userPassword));
		userEntity.setPasswordUpdateYn(passwordUpdateYn);

		entityManager.persist(userEntity);
		entityManager.flush();
		entityManager.close();
	}	

	public List<UserInfo> getUserList(String roleId) throws InvalidParameterException {
		SystemRole systemRole = null;
		try {
			systemRole = (SystemRole) SystemRole.valueOf(roleId);
		} catch (Exception e) {
			throw new InvalidParameterException();
		}
		
		List<UserEntity> userEntityList = null;
		if (SystemRole.AD.equals(systemRole)) {
			userEntityList = userRepository.findAll();
		} else {
			userEntityList = userRepository.findByRoleIdNot(SystemRole.AD.name());
		}
		
		List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		for (UserEntity userEntity : userEntityList) {
			userInfoList.add(new UserInfo(userEntity.getUserId(), userEntity.getUserName(), userEntity.getRoleId(), userEntity.getEnabled(), userEntity.getDefaultPlantUnitId(), userEntity.getRegDate()));
		}
		
		return userInfoList;
	}
	
	public List<UserCommonInfo> getRoleList(String roldId) throws InvalidParameterException {
		List<UserCommonInfo> roleList = new ArrayList<UserCommonInfo>();
		SystemRole systemRole = null;
		
		try {
			systemRole = (SystemRole) SystemRole.valueOf(roldId);
		} catch (Exception e) {
			throw new InvalidParameterException();
		}

		List<UserRoleEntity> userRoleEntityList = null;
		if (SystemRole.AD.equals(systemRole)) {
			userRoleEntityList = roleRepository.findAll();
		} else {
			userRoleEntityList = roleRepository.findByRoleIdNot(SystemRole.AD.name());
		}
		
		for (UserRoleEntity roleEntity : userRoleEntityList) {
			UserCommonInfo roleInfo = new UserCommonInfo();
			roleInfo.setRoleId(roleEntity.getRoleId());
			roleInfo.setRoleName(roleEntity.getRoleName());
			roleList.add(roleInfo);
		}
		
		return roleList;
	}
	
	public List<RoleInfo> getRoleAll() {
		List<UserRoleEntity> roleEntityList = roleRepository.findAll();
		List<RoleInfo> roleInfoList = new ArrayList<>();
 
		roleInfoList = roleEntityList.stream().map(roleEntity -> new RoleInfo(
				roleEntity.getRoleId(),
				roleEntity.getRoleName(),
				menuRepository.findByRoleIdNativeQuery(roleEntity.getRoleId()).stream().map(menuEntity ->
						new UserMenu(menuEntity.getMenuEntityId().getMenuId(),
								menuEntity.getMenuName(),
								menuEntity.getMenuEntityId().getSystemId())).collect(Collectors.toList()),
				roleEntity.getDescription(),
				roleEntity.getEnabled()
		)).collect(Collectors.toList());

		return roleInfoList;
	}
	
	@Override
	public List<MenuInfo> getMenuAll() {
		List<MenuEntity> menuEntityList = menuRepository.findAllMenuList();
		List<MenuInfo> menuList = menuEntityList.stream().map(menuEntity -> new MenuInfo(menuEntity.getMenuEntityId().getMenuId(), menuEntity.getMenuEntityId().getSystemId(), menuEntity.getMenuName(), menuEntity.getParentMenu(), menuEntity.getMenuOrder())).collect(Collectors.toList());
		return menuList;
	}
	
	@Override
	public void insertMenu(MenuInfo menuInfo) {
		
		int menuOrder = 0;
		if(menuInfo.getParentMenu() == null || menuInfo.getParentMenu().equals("")) {
			menuOrder = menuRepository.findMaxMenuOrderBySysId(menuInfo.getSysId());
		} else {
			menuOrder = menuRepository.findMaxMenuOrderBySysIdAndParentMenu(menuInfo.getSysId(), menuInfo.getParentMenu());
		}
		menuRepository.insertMenuInfo(menuInfo.getMenuId(), menuInfo.getMenuName(), menuInfo.getParentMenu(), menuInfo.getSysId(), menuInfo.getDescription(), menuOrder + 1);
	}
	
	
	@Override
	public void upsertMenu (List<MenuInfo> menuList) {
		for (MenuInfo menuInfo : menuList) {
			menuRepository.upsertMenuInfo(menuInfo.getMenuId(), menuInfo.getMenuName(), menuInfo.getParentMenu(), menuInfo.getSysId(), menuInfo.getDescription(), menuInfo.getMenuOrder());
		}
	}
	
	@Override
	public void deleteMenu(MenuInfo menuInfo) {
		
		menuRepository.deleteMenuInfo(menuInfo.getMenuId(), menuInfo.getSysId());
	}
	
	
	public void createRole(RoleInfo roleInfo) {
		UserRoleEntity roleEntity = roleRepository.findByRoleId(roleInfo.getRoleId());
		if (roleEntity == null){
			roleEntity = new UserRoleEntity();
			roleEntity.setRoleId(roleInfo.getRoleId());
			roleEntity.setRoleName(roleInfo.getRoleName());
			roleEntity.setDescription(roleInfo.getDescription());
		}

		entityManager.persist(roleEntity);

		for(UserMenu userMenu:roleInfo.getMenu_list()){
			roleRepository.insertTbUserMenuInfo(userMenu.getSysId(), roleEntity.getRoleId(), userMenu.getMenuId());
		}
		entityManager.flush();
		entityManager.close();
	}

	public void updateRoles(List<RoleInfo> roleInfoList) {
		for (RoleInfo roleInfo : roleInfoList) {
			UserRoleEntity roleEntity = roleRepository.getOne(roleInfo.getRoleId());
			roleEntity.setRoleId(roleInfo.getRoleId());
			roleEntity.setRoleName(roleInfo.getRoleName());
			roleEntity.setDescription(roleInfo.getDescription());
			roleEntity.setEnabled(roleInfo.getRoleEnabled());

			entityManager.persist(roleEntity);

			roleRepository.deleteTbUserMenuInfoByRoleId(roleInfo.getRoleId());

			for(UserMenu userMenu:roleInfo.getMenu_list()){
				roleRepository.insertTbUserMenuInfo(userMenu.getSysId(), roleInfo.getRoleId(), userMenu.getMenuId());

			}
		}
	}

	@Override
	public void deleteRole(String roleId) {
		UserRoleEntity roleEntity = roleRepository.getOne(roleId);

		entityManager.remove(roleEntity);

		roleRepository.deleteTbUserMenuInfoByRoleId(roleId);
	}


	@Override
	public List<RoleInfo> getRoleEnabledAll() {
		List<UserRoleEntity> roleEntityList = roleRepository.findByEnabled(true);
        List<RoleInfo> roleInfoList = new ArrayList<>();

        roleInfoList = roleEntityList.stream().map(roleEntity -> new RoleInfo(
                roleEntity.getRoleId(),
				roleEntity.getRoleName(),
				menuRepository.findByRoleIdAndParentMenuIsNullNativeQuery(roleEntity.getRoleId()).stream().map(menuEntity ->
                		new UserMenu(menuEntity.getMenuEntityId().getMenuId(),
						menuEntity.getMenuName(),
						menuEntity.getMenuEntityId().getSystemId())).collect(Collectors.toList()),
				roleEntity.getDescription(),
				roleEntity.getEnabled()
				)).collect(Collectors.toList());

        return roleInfoList;
	}
}
