package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_COMMON_DATA_INPUT)
 */
@Entity
@Table(name="TB_COMMON_DATA_INPUT")
public class CommonDataInputEntity {

	@EmbeddedId
	private CommonDataInputIdEntity commonDataInputIdEntity;
	
	@Column(name="TAG_VAL")
	private double tagVal;
	
	public CommonDataInputIdEntity getCommonDataInputIdEntity() {
		return commonDataInputIdEntity;
	}

	public void setCommonDataInputIdEntity(CommonDataInputIdEntity commonDataInputIdEntity) {
		this.commonDataInputIdEntity = commonDataInputIdEntity;
	}

	public double getTagVal() {
		return tagVal;
	}

	public void setTagVal(double tagVal) {
		this.tagVal = tagVal;
	}
}
