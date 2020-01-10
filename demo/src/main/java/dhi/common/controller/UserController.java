package dhi.common.controller;

import java.net.URL;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dhi.common.httpEntity.RestBaseResponse;
import dhi.common.httpEntity.RestResponse;
import dhi.common.httpEntity.RestResponseEntity;
import dhi.common.httpEntity.RestResponseEntityList;
import dhi.common.model.json.MenuInfo;
import dhi.common.model.json.RoleInfo;
import dhi.common.model.json.UserCommonInfo;
import dhi.common.model.json.UserInfo;
import dhi.common.model.json.UserMenu;
import dhi.common.service.UserService;
import dhi.common.util.MessageService;

/*
 * Controller that manages user information.
 */
@RestController
@RequestMapping("/user")
public class UserController {

	static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	static final String IdCheckDuplicate = "Unavailable";
	static final String IdCheckNoDuplicate = "Available";

	@Autowired
	private Environment environment;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@PostMapping("/login")
	public RestResponseEntity<UserInfo> login(@RequestBody UserInfo userInputParam, HttpSession httpSession) {

		RestResponseEntity<UserInfo> result = null;
		try {
			UserDetails userDetails = userService.loadUserByUsername(userInputParam.getUserId());

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,
					userInputParam.getUserPassword(), userDetails.getAuthorities());

			// Spring security set
			Authentication authentication = authenticationManager.authenticate(token);

			// 인증 받은 객체를 context를 받아와서 set
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// 세션 속성 값 설정
			httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());

			UserInfo userInfo = userService.getUserInfoAfterLogin(userInputParam.getUserId());
			userInfo.setAuthToken(httpSession.getId());
			result = new RestResponseEntity<UserInfo>(userInfo);

		} catch (Exception e) {
			result = new RestResponseEntity<UserInfo>(RestBaseResponse.FailLoginErrorCode, messageService.getMessage("login.failure.message"));
		}

		return result;
	}

	@DeleteMapping("/logout")
	public RestResponse logout(HttpServletRequest request, HttpServletResponse response) {
		RestResponse result;
		try {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}

			result = new RestResponse();
		} catch (Exception e) {

			System.out.println(e.toString());
			result = new RestResponse(e);
		}

		return result;
	}

	@GetMapping("/system/info/{role_id}/{user_id}")
	public RestResponseEntityList<UserCommonInfo> getUserSystemInfo(@PathVariable(value = "role_id") String roleId, @PathVariable(value = "user_id") String userId, ServletRequest servletRequest) {
		RestResponseEntityList<UserCommonInfo> result;
		try {
			
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;			
			URL urlInfo = new URL(httpServletRequest.getRequestURL().toString());
			
			result = new RestResponseEntityList<UserCommonInfo>(userService.getUserSystemInfoList(roleId, userId, urlInfo.getHost()));
		} catch (Exception e) {
			result = new RestResponseEntityList<UserCommonInfo>(e);
		}

		return result;
	}

	@GetMapping("/menu/info")
	public RestResponseEntityList<UserMenu> getUserMenuInfo(@RequestParam(value = "system_id") String systemId,
			@RequestParam(value = "role_id") String roleId) {
		RestResponseEntityList<UserMenu> result;
		try {
			result = new RestResponseEntityList<UserMenu>(userService.getUserMenuList(systemId, roleId));
		} catch (Exception e) {
			result = new RestResponseEntityList<UserMenu>(e);
		}

		return result;
	}

	@GetMapping("/list/{role_id}")
	public RestResponseEntityList<UserInfo> getUserList(@PathVariable(value = "role_id") String roleId) {
		RestResponseEntityList<UserInfo> result;
		try {
			result = new RestResponseEntityList<UserInfo>(userService.getUserList(roleId));
		} catch (Exception e) {
			result = new RestResponseEntityList<UserInfo>(e);
		}

		return result;
	}
	
	@GetMapping("/id/check/{user_id}")
	public RestResponse userIdCheck(@PathVariable(value = "user_id") String userId)
	{		
		RestResponse result;
		try {
			
			boolean isDuplicate = userService.isUserIdDuplicate(userId);
			result = new RestResponse(isDuplicate ? IdCheckDuplicate : IdCheckNoDuplicate);

		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}	
	

	@PostMapping("/create")
	public RestResponse createUser(@RequestBody UserInfo userInfo) {

		RestResponse result;
		try {
			
			userInfo.setUserPassword(environment.getProperty("user.default.password"));
			userService.createUser(userInfo);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	@PostMapping("/update")
	public RestResponse UpdateUsers(@RequestBody List<UserInfo> userInfoList) {

		RestResponse result;
		try {
			userService.updateUsers(userInfoList);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	@PutMapping("/password/reset/{user_id}")
	public RestResponse resetPassword(@PathVariable(value = "user_id") String userId) {
		RestResponse result;
		try {
			userService.saveUserPassword(userId, environment.getProperty("user.default.password"), "Y");
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	@PostMapping("/password/update")
	public RestResponse updatePassword(@RequestBody UserInfo userInfo) {
		RestResponse result;
		try {
			userService.saveUserPassword(userInfo.getUserId(), userInfo.getUserPassword(), "N");
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	@GetMapping("/role/list/{role_id}")
	public RestResponseEntityList<UserCommonInfo> getRoleList(@PathVariable(value = "role_id") String roleId) {
		RestResponseEntityList<UserCommonInfo> result;
		try {
			result = new RestResponseEntityList<UserCommonInfo>(userService.getRoleList(roleId));
		} catch (Exception e) {
			result = new RestResponseEntityList<UserCommonInfo>(e);
		}

		return result;
	}
	
	@GetMapping("/menu/list")
	public RestResponseEntityList<MenuInfo> getMenuList() {
		try {
			return new RestResponseEntityList<>(userService.getMenuAll());
		} catch (Exception e) {
			return new RestResponseEntityList<>(e);
		}
	}
	
	@PostMapping("/menu/insert")
	public RestResponse insertMenu(@RequestBody MenuInfo menuInfo) {
		try {
			userService.insertMenu(menuInfo);
		} catch (Exception e) {
			return new RestResponse(e);
		}
		return new RestResponse();
	}
	
	@PostMapping("/menu/update")
	public RestResponse updateMenu(@RequestBody List<MenuInfo> menuInfoList) {
		try {
			userService.upsertMenu(menuInfoList);
		} catch (Exception e) {
			return new RestResponse(e);
		}
		return new RestResponse();
	}
	
	@DeleteMapping("/menu/delete")
	public RestResponse deleteMenu(@RequestBody MenuInfo menuInfo) {
		try {
			userService.deleteMenu(menuInfo);
		} catch (Exception e) {
			return new RestResponse(e);
		}
		return new RestResponse();
	}
	
	

	@GetMapping("/role/list/")
	public RestResponseEntityList<RoleInfo> getRoleList() {
		try {
			return new RestResponseEntityList<>(userService.getRoleAll());
		} catch (Exception e) {
			return new RestResponseEntityList<>(e);
		}
	}

	@GetMapping("/role/list/enabled/")
	public RestResponseEntityList<RoleInfo> getRoleAllList() {
		try {
			return new RestResponseEntityList<>(userService.getRoleEnabledAll());
		} catch (Exception e) {
			return new RestResponseEntityList<>(e);
		}
	}


	@PostMapping("/role/create")
	public RestResponse createRole(@RequestBody RoleInfo roleInfo) {
		try {
			userService.createRole(roleInfo);
		} catch (Exception e) {
			return new RestResponse(e);
		}
		return new RestResponse();
	}

	@PostMapping("/role/update")
	public RestResponse updateRoles(@RequestBody List<RoleInfo> roleInfoList) {
		try {
			userService.updateRoles(roleInfoList);
		} catch (Exception e) {
			return new RestResponse(e);
		}
		return new RestResponse();
	}


	@DeleteMapping("/role/delete/{role_id}")
	public RestResponse deleteRole(@PathVariable(value = "role_id") String role_id) {
		try {
			userService.deleteRole(role_id);
		} catch (Exception e) {
			return new RestResponse(e);
		}
		return new RestResponse();
	}
}