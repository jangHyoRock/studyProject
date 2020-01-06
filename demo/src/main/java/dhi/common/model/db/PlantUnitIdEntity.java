package dhi.common.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_PLANT_UNIT)
 */
@SuppressWarnings("serial")
@Embeddable
public class PlantUnitIdEntity implements Serializable {

	@Column(name="P_UNIT_ID")
	private String plantUnitId;	
	
	@Column(name="SYS_ID")
	private String sysId;

	public String getPlantUnitId() {
		return plantUnitId;
	}

	public void setPlantUnitId(String plantUnitId) {
		this.plantUnitId = plantUnitId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
}
