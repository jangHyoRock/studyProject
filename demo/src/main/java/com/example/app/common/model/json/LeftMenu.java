package com.example.app.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeftMenu {

	private String pUnitId;
	private String pUnitNm;
	private String pUnitOrder;
	private String description;
	private String tubineType;
	
}
