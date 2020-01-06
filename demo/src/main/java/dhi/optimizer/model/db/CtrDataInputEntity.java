package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_CTR_DATA_INPUT)
 */
@Entity
@Table(name="TB_CTR_DATA_INPUT")
public class CtrDataInputEntity {

	@EmbeddedId
	private CtrDataInputIdEntity CtrDataInputIdEntity;
	
	@Column(name="TAG_VAL")
	private double tagVal;

	public CtrDataInputIdEntity getCtrDataInputIdEntity() {
		return CtrDataInputIdEntity;
	}

	public void setCtrDataInputIdEntity(CtrDataInputIdEntity ctrDataInputIdEntity) {
		CtrDataInputIdEntity = ctrDataInputIdEntity;
	}

	public double getTagVal() {
		return tagVal;
	}

	public void setTagVal(double tagVal) {
		this.tagVal = tagVal;
	}
}
