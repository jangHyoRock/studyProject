package dhi.common.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Json model with user menu.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMenu {

	@JsonProperty("menu_id")
	private String menuId;
	
	@JsonProperty("menu_name")
	private String menuName;
	
	@JsonProperty("sys_id")
	private String sysId;
	
	@JsonProperty("sub_menu")
	private List<UserMenu> subMenuList;

	public UserMenu() {}
	
	public UserMenu(String menuId, String menuName)	
	{
		this.menuId = menuId;
		this.menuName = menuName;		
	}
	
	public UserMenu(String menuId, String menuName, String sysId)	{
		this.menuId = menuId;
		this.menuName = menuName;
		this.sysId = sysId;
	}
	
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public List<UserMenu> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<UserMenu> subMenuList) {
		this.subMenuList = subMenuList;
	}	
}
