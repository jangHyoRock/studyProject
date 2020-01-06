package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_CTR_DATA_OUTPUT)
 */
@Entity
@Table(name="TB_CTR_DATA_OUTPUT")
public class CtrDataOutputEntity {

	@EmbeddedId
	private CtrDataOutputIdEntity ctrDataOutputIdEntity;
	
	@Column(name="TAG_VAL")
	private double tagVal;
	
	public CtrDataOutputIdEntity getCtrDataOutputIdEntity() {
		return ctrDataOutputIdEntity;
	}

	public void setCtrDataOutputIdEntity(CtrDataOutputIdEntity ctrDataOutputIdEntity) {
		this.ctrDataOutputIdEntity = ctrDataOutputIdEntity;
	}

	public double getTagVal() {
		return tagVal;
	}

	public void setTagVal(double tagVal) {
		this.tagVal = tagVal;
	}
}
