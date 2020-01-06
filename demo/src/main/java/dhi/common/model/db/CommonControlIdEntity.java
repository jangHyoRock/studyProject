package dhi.common.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_PLANT_UNIT)
 */
@SuppressWarnings("serial")
@Embeddable
public class CommonControlIdEntity implements Serializable {

	@Column(name="TIMESTAMP")
	private String timestamp;
	
	@Column(name="P_UNIT_ID")
	private String plantUnitId;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getPlantUnitId() {
		return plantUnitId;
	}

	public void setPlantUnitId(String plantUnitId) {
		this.plantUnitId = plantUnitId;
	}	
}
