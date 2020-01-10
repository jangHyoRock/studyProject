package com.project.common.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Data
@Table(name="TB_TAG_LIST")
@NoArgsConstructor
@AllArgsConstructor
public class OverviewEntity {

	@Id
	@Column(name="TAG_ID")
	@JoinColumn(name="TAG_ID")
	private String tagId;
	
	@Column(name="P_UNIT_ID")
	@JoinColumn(name="P_UNIT_ID")
	private String pUnitId;
	
	@Column(name="TIMESTAMP")
	@JoinColumn(name="TIMESTAMP")
	private String timeStamp;
	
	
//	public OverviewEntity(String tagId, String pUnitId, String timeStamp) {
//		this.pUnitId = pUnitId;
//		this.tagId = tagId;
//		this.timeStamp = timeStamp;
//		
//	}
}
