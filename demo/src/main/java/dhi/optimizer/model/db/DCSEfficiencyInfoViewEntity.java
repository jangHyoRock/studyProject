package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_LAST_OP_DATA_INPUT")
public class DCSEfficiencyInfoViewEntity {
	@Id
	@Column(name="TAG_ID")
	private String tagId;	

	@Column(name="TAG_NM")
	private String tagNm;
	
	@Column(name="TAG_VAL")
	private double tagVal;

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagNm() {
		return tagNm;
	}

	public void setTagNm(String tagNm) {
		this.tagNm = tagNm;
	}

	public double getTagVal() {
		return tagVal;
	}

	public void setTagVal(double tagVal) {
		this.tagVal = tagVal;
	}
}
