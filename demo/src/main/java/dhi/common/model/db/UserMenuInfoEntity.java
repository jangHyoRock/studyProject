package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.
 */
@Entity
@Table(name="TB_USER_MENU_INFO")
public class UserMenuInfoEntity {

	@Id
	@Column(name="SYS_ID")
	private String systemId;
	
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Column(name="MENU_ID")
	private String menuId;
	
	@Column(name="MENU_ORDER")
	private int menuOrder;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}
}
