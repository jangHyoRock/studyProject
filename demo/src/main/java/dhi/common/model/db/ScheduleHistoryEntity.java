package dhi.common.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_SCHEDULE_HIST)
 */
@Entity
@Table(name="TB_SCHEDULE_HIST")
public class ScheduleHistoryEntity {

	@EmbeddedId
	private ScheduleHistoryIdEntity scheduleHistoryIdEntity;
	
	@Column(name="END_DT")
	private Date endDate;
	
	@Column(name="STATUS")
	private int status;	
	
	@Column(name="ERROR")
	private String error;
	
	public ScheduleHistoryEntity() {
	}

	public ScheduleHistoryEntity(ScheduleEntity schedule) {
		this.scheduleHistoryIdEntity = new ScheduleHistoryIdEntity(schedule.getScheduleId(), schedule.getStartDate());
		this.status = schedule.getStatus();
	}

	public void setScheduleToScheduleHistroy(ScheduleEntity scheduleEntity) {
		this.setEndDate(scheduleEntity.getEndDate());
		this.setStatus(scheduleEntity.getStatus());
	}

	public ScheduleHistoryIdEntity getScheduleHistoryIdEntity() {
		return this.scheduleHistoryIdEntity;
	}

	public void setScheduleHistoryIdEntity(ScheduleHistoryIdEntity scheduleHistoryIdEntity) {
		this.scheduleHistoryIdEntity = scheduleHistoryIdEntity;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return this.error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
