package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NN_TRAIN_IN)
 */
@Entity
@Table(name="TB_NN_TRAIN_IN")
public class NNTrainInEntity {

	@Id
	@Column(name="TIMESTAMP")
	private Date timestamp;
	
	@Column(name="TAG_ID")
	private String tagId;
	
	@Column(name="TAG_VAL")
	private double tagVal;
	
	@Column(name="TAG_NM")
	private String tagNM;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public double getTagVal() {
		return tagVal;
	}

	public void setTagVal(double tagVal) {
		this.tagVal = tagVal;
	}

	public String getTagNM() {
		return tagNM;
	}

	public void setTagNM(String tagNM) {
		this.tagNM = tagNM;
	}
}
