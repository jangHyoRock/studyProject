package dhi.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Json model with user menu.
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class MenuInfo {

	@JsonProperty("menu_id")
	private String menuId;
	
	@JsonProperty("menu_name")
	private String menuName;
	
	@JsonProperty("sys_id")
	private String sysId;
	
	@JsonProperty("parent_menu")
	private String parentMenu;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("menu_order")
	private int menuOrder;

	public MenuInfo() {}
	
	public MenuInfo(String menuId, String sysId, String menuName, String parentMenu, int menuOrder)	
	{
		this.menuId = menuId;
		this.sysId = sysId;	
		this.menuName = menuName;
		this.parentMenu = parentMenu;
		this.menuOrder = menuOrder;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}
	
}
