package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_OUTPUT_DATA_HIST)
 */
@Entity
@Table(name="TB_OUTPUT_DATA_HIST")
public class OutputDataHistEntity {
	
	@Column(name="TIMESTAMP")
	private Date timestamp;
	
	@Column(name="CONTROL_VALUE")
	private String controlValue;
	
	@Id
	@Column(name="CONTROL_MV")
	private String controlMV;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getControlValue() {
		return controlValue;
	}

	public void setControlValue(String controlValue) {
		this.controlValue = controlValue;
	}

	public String getControlMV() {
		return controlMV;
	}

	public void setControlMV(String controlMV) {
		this.controlMV = controlMV;
	}
}
