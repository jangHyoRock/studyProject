package dhi.optimizer.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_COMMON_DATA_CONF)
 */
@SuppressWarnings("serial")
@Embeddable
public class CommonDataConfIdEntity implements Serializable {

	@Column(name="TAG_NM")
	private String tagNm;
	
	@Column(name="P_UNIT_ID")
	private String plantUnitId;
	
	public CommonDataConfIdEntity() {};

	public CommonDataConfIdEntity(String tagNm, String plantUnitId) {
		this.tagNm = tagNm;
		this.plantUnitId = plantUnitId;
	};
	
	public String getTagNm() {
		return tagNm;
	}

	public void setTagNm(String tagNm) {
		this.tagNm = tagNm;
	}

	public String getPlantUnitId() {
		return plantUnitId;
	}

	public void setPlantUnitId(String plantUnitId) {
		this.plantUnitId = plantUnitId;
	}
}
