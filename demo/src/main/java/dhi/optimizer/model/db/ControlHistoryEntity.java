package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_CONTROL)
 */
@Entity
@Table(name="TB_CONTROL")
public class ControlHistoryEntity {

	@Id
	@Column(name="TIMESTAMP")
	private Date timestamp;

	@Column(name="SYSTEM_START")
	private Boolean systemStart;

	@Column(name="CONTROL_MODE")
	private String controlMode;

	@Column(name="OPT_MODE")
	private String optMode;
	
	@Column(name="LEARNING_MODE")
	private String learningMode;
	
	@Column(name="USER_ID")
	private String userId;
	
	public ControlHistoryEntity() {}
	
	public ControlHistoryEntity(Boolean systemStart, String controlMode, String optMode, String learningMode)
	{
		this.systemStart = systemStart;
		this.controlMode = controlMode;
		this.optMode = optMode;
		this.learningMode = learningMode;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getSystemStart() {
		return systemStart;
	}

	public void setSystemStart(Boolean systemStart) {
		this.systemStart = systemStart;
	}

	public String getControlMode() {
		return controlMode;
	}

	public void setControlMode(String controlMode) {
		this.controlMode = controlMode;
	}

	public String getOptMode() {
		return optMode;
	}

	public void setOptMode(String optMode) {
		this.optMode = optMode;
	}

	public String getLearningMode() {
		return learningMode;
	}

	public void setLearningMode(String learningMode) {
		this.learningMode = learningMode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
