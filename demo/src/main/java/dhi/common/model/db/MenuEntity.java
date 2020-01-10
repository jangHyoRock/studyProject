package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
/*
 * DB Table Model Class using JPA.
 */
@Entity
@Table(name="TB_MENU")
public class MenuEntity {

	@EmbeddedId
	private MenuEntityId menuEntityId;
	
	@Column(name="MENU_NM")
	private String menuName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="PARENT_MENU")
	private String parentMenu;
	
	@Column(name="MENU_ORDER")
	private int menuOrder;
		
	public MenuEntityId getMenuEntityId() {
		return menuEntityId;
	}

	public void setMenuEntityId(MenuEntityId menuEntityId) {
		this.menuEntityId = menuEntityId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(String parentMenu) {
		this.parentMenu = parentMenu;
	}

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}
	
	
}
