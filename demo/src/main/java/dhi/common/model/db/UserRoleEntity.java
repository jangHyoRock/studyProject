package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_USER_ROLE")
public class UserRoleEntity {

	@Id
	@Column(name="ROLE_ID")
	private String roleId;	

	@Column(name="ROLE_NM")
	private String roleName;
	
	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="ENABLED")
	private Boolean enabled;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
