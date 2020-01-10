package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_OUTPUT_DATA)
 */
@Entity
@Table(name="TB_OUTPUT_HOLD")
public class OutputHoldEntity {

	@Id
	@Column(name="HOLD_ID")
	private String holdId;
	
	@Column(name="HOLD_START_TIME")
	private Date holdStartTime;
	
	@Column(name="HOLD_END_TIME")
	private Date holdEndTime;
	
	@Column(name="HOLD_YN")
	private boolean holdYn;
	
	@Column(name="PROGRESS_START_TIME")
	private Date progressStartTime;

	public String getHoldId() {
		return holdId;
	}

	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}

	public Date getHoldStartTime() {
		return holdStartTime;
	}

	public void setHoldStartTime(Date holdStartTime) {
		this.holdStartTime = holdStartTime;
	}

	public Date getHoldEndTime() {
		return holdEndTime;
	}

	public void setHoldEndTime(Date holdEndTime) {
		this.holdEndTime = holdEndTime;
	}

	public boolean getHoldYn() {
		return holdYn;
	}

	public void setHoldYn(boolean holdYn) {
		this.holdYn = holdYn;
	}

	public Date getProgressStartTime() {
		return progressStartTime;
	}

	public void setProgressStartTime(Date progressStartTime) {
		this.progressStartTime = progressStartTime;
	}
}
