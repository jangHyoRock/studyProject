package dhi.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Json model with common user information in user management.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCommonInfo {

	@JsonProperty("system_id")
	private String systemId;
	
	@JsonProperty("system_name")
	private String systemName;
	
	@JsonProperty("role_id")
	private String roleId;
	
	@JsonProperty("role_name")
	private String roleName;
	
	@JsonProperty("default_plant_unit_id")
	private String defaultPlantId;
	
	@JsonProperty("default_plant_unit_ip")
	private String defaultPlantIp;
	
	@JsonProperty("default_plant_unit_port")
	private String defaultPlantPort;
	
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
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

	public String getDefaultPlantId() {
		return defaultPlantId;
	}

	public void setDefaultPlantId(String defaultPlantId) {
		this.defaultPlantId = defaultPlantId;
	}

	public String getDefaultPlantIp() {
		return defaultPlantIp;
	}

	public void setDefaultPlantIp(String defaultPlantIp) {
		this.defaultPlantIp = defaultPlantIp;
	}

	public String getDefaultPlantPort() {
		return defaultPlantPort;
	}

	public void setDefaultPlantPort(String defaultPlantPort) {
		this.defaultPlantPort = defaultPlantPort;
	}	
}
