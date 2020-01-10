package com.project.common.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MenuEntityId implements Serializable {
	@Column(name="MENU_ID")
	private String menuId;
	
	@Column(name="SYS_ID")
	private String systemId;

}
