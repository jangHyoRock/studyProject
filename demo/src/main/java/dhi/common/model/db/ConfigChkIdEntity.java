package dhi.common.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_CONFIG_CHK)
 */
@SuppressWarnings("serial")
@Embeddable
public class ConfigChkIdEntity implements Serializable {

	@Column(name="P_UNIT_ID")
	private String plantUnitId;
	
	@Column(name="CHK_ID")
	private String chkId;
	
	public ConfigChkIdEntity () {};
	
	public ConfigChkIdEntity (String plantUnitId, String chkId)
	{
		this.plantUnitId = plantUnitId;
		this.chkId = chkId;
	}

	public String getPlantUnitId() {
		return plantUnitId;
	}

	public void setPlantUnitId(String plantUnitId) {
		this.plantUnitId = plantUnitId;
	}

	public String getChkId() {
		return chkId;
	}

	public void setChkId(String chkId) {
		this.chkId = chkId;
	}
}
