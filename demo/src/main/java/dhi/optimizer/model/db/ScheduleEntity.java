package dhi.optimizer.model.db;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_SCHEDULE)
 */
@Entity
@Table(name="TB_SCHEDULE")
public class ScheduleEntity {	
	
	@Id
	@Column(name = "SCHEDULE_ID")
	private int scheduleId;

	@Column(name = "SCHEDULE_NM")
	private String scheduleName;

	@Column(name = "MODULE_CLASS")
	private String moduleClass;

	@Column(name = "USE_YN")
	private String useYN;

	@Column(name = "SYSTEM_READY_CHECK")
	private boolean systemReadyCheck;

	@Column(name = "START_DT")
	private Date startDate;

	@Column(name = "END_DT")
	private Date endDate;

	@Column(name = "STATUS")
	private int status;

	@Column(name = "INTERVAL")
	private int interval;

	@Column(name = "ALWAYS_RUN")
	private boolean alWaysRun;
	
	public int getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getScheduleName() {
		return this.scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getModuleClass() {
		return this.moduleClass;
	}

	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}

	public String getUseYN() {
		return this.useYN;
	}

	public void setUseYN(String useYN) {
		this.useYN = useYN;
	}

	public boolean isSystemReadyCheck() {
		return systemReadyCheck;
	}

	public void setSystemReadyCheck(boolean systemReadyCheck) {
		this.systemReadyCheck = systemReadyCheck;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
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

	public int getInterval() {
		return this.interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public boolean isAlWaysRun() {
		return alWaysRun;
	}

	public void setAlWaysRun(boolean alWaysRun) {
		this.alWaysRun = alWaysRun;
	}
}