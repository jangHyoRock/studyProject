package dhi.common.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table Model Class using JPA.
 */
@SuppressWarnings("serial")
@Embeddable
public class MenuEntityId implements Serializable {

	@Column(name="MENU_ID")
	private String menuId;
	
	@Column(name="SYS_ID")
	private String systemId;
	
	public MenuEntityId () {};
	
	public MenuEntityId (String menuId, String systemId)
	{
		this.menuId = menuId;
		this.systemId = systemId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}	
}
