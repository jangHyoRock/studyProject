package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_OP_DATA_INPUT)
 */
@Entity
@Table(name="TB_OP_DATA_INPUT")
public class OPDataInputEntity {

	@EmbeddedId
	private OPDataInputIdEntity opDataInputIdEntity;
	
	@Column(name="TAG_VAL")
	private double tagVal;
	
	public OPDataInputIdEntity getOpDataInputIdEntity() {
		return opDataInputIdEntity;
	}

	public void setOpDataInputIdEntity(OPDataInputIdEntity opDataInputIdEntity) {
		this.opDataInputIdEntity = opDataInputIdEntity;
	}

	public double getTagVal() {
		return tagVal;
	}

	public void setTagVal(double tagVal) {
		this.tagVal = tagVal;
	}
}
