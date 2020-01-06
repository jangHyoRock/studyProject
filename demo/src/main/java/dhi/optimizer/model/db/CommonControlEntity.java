package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_COMMON_CONTROL)
 */
@Entity
@Table(name="TB_COMMON_CONTROL")
public class CommonControlEntity {
	
	@Id
	@Column(name="TIMESTAMP")
	private Date timestamp;	

	@Column(name="SYSTEM_START")
	private Boolean systemStart;

	@Column(name="CONTROL_MODE")
	private String controlMode;

	@Column(name="OPT_MODE")
	private String optMode;
	
	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getSystemStart() {
		return this.systemStart;
	}

	public void setSystemStart(Boolean systemStart) {
		this.systemStart = systemStart;
	}

	public String getControlMode() {
		return this.controlMode;
	}

	public void setControlMode(String controlMode) {
		this.controlMode = controlMode;
	}

	public String getOptMode() {
		return this.optMode;
	}

	public void setOptMode(String optMode) {
		this.optMode = optMode;
	}
}