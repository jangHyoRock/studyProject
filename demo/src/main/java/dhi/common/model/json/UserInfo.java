package dhi.common.model.json;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Json model with user information.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("user_name")
	private String userName;

	@JsonProperty("user_password")
	private String userPassword;

	@JsonProperty("role_id")
	private String roleId;

	@JsonProperty("role_name")
	private String roleName;
	
	@JsonProperty("user_enabled")
	private Boolean userEnabled;
	
	@JsonProperty("default_plant_unit_id")
	private String defaultPlantId;
	
	@JsonProperty("reg_date")	
	private Date regDate;

	@JsonProperty("password_update_yn")
	private String passwordUpdateYn;
	
	@JsonProperty("auth_token")
	private String authToken;	
	
	public UserInfo() {
	};

	public UserInfo(String userId, String userName, String roleId, Boolean userEnabled, String defaultPlantId, Date regDate) {
		this.userId = userId;
		this.userName = userName;
		this.roleId = roleId;
		this.userEnabled = userEnabled;
		this.defaultPlantId = defaultPlantId;
		this.regDate = regDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}	

	public Boolean getUserEnabled() {
		return userEnabled;
	}

	public void setUserEnabled(Boolean userEnabled) {
		this.userEnabled = userEnabled;
	}

	public String getDefaultPlantId() {
		return defaultPlantId;
	}

	public void setDefaultPlantId(String defaultPlantId) {
		this.defaultPlantId = defaultPlantId;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getPasswordUpdateYn() {
		return passwordUpdateYn;
	}

	public void setPasswordUpdateYn(String passwordUpdateYn) {
		this.passwordUpdateYn = passwordUpdateYn;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}	
}
