package dhi.common.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_CONFIG_CHK)
 */
@SuppressWarnings("serial")
@Embeddable
public class ConfigInfIdEntity implements Serializable {

	@Column(name="P_UNIT_ID")
	private String plantUnitId;
	
	@Column(name="CONF_ID")
	private String confId;
	
	@Column(name="CONF_TYPE")
	private String confType;
	
	public String getPlantUnitId() {
		return plantUnitId;
	}

	public void setPlantUnitId(String plantUnitId) {
		this.plantUnitId = plantUnitId;
	}

	public String getConfId() {
		return confId;
	}

	public void setConfId(String confId) {
		this.confId = confId;
	}

	public String getConfType() {
		return confType;
	}

	public void setConfType(String confType) {
		this.confType = confType;
	}	
}
