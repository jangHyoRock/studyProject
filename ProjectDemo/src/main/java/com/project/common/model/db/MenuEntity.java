package com.project.common.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntity {
	@EmbeddedId
	private MenuEntityId menuEntityId;
	
	@Column(name="MENU_NM")
	private String menuName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="PARENT_MENU")
	private String parentMenu;
}
