package dhi.optimizer.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_SCHEDULE_HIST)
 */
@SuppressWarnings("serial")
@Embeddable
public class ScheduleHistoryIdEntity implements Serializable {
	
	@Column(name="SCHEDULE_ID")
	private int scheduleId;	
	
	@Column(name="START_DT")
	private Date startDate;
	
	public ScheduleHistoryIdEntity() {
	}

	public ScheduleHistoryIdEntity(int scheduleId, Date startDate) {
		this.scheduleId = scheduleId;
		this.startDate = startDate;
	}

	public int getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
