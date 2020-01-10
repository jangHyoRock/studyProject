package dhi.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/*
 * Json model with user information.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleInfo {

	@JsonProperty("role_id")
	private String roleId;

	@JsonProperty("role_name")
	private String roleName;

	@JsonProperty("menu_list")
	private List<UserMenu> menu_list;

	@JsonProperty("description")
	private String description;

	@JsonProperty("role_enabled")
	private Boolean roleEnabled;

	public RoleInfo() {
	}

	public RoleInfo(String roleId, String roleName, List<UserMenu> menu_list, String description, Boolean roleEnabled) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.menu_list = menu_list;
		this.description = description;
		this.roleEnabled = roleEnabled;
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

	public List<UserMenu> getMenu_list() {
		return menu_list;
	}

	public void setMenu_list(List<UserMenu> menu_list) {
		this.menu_list = menu_list;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getRoleEnabled() {
		return roleEnabled;
	}

	public void setRoleEnabled(Boolean roleEnabled) {
		this.roleEnabled = roleEnabled;
	}
}