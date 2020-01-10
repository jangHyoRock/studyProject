package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_SYSTEM_CHECK)
 */
@Entity
@Table(name="TB_SYSTEM_CHECK")
public class SystemCheckEntity {

	@Id
	@Column(name="TIMESTAMP")
	private Date timestamp;

	@Column(name="CPU_USAGE")
	private String cpuUsage;
	
	@Column(name="MEMORY_USAGE")
	private String memoryUsage;
	
	@Column(name="DISK_USAGE")
	private String diskUsage;
	
	@Column(name="UNIT")
	private String unit;
	
	@Column(name="REMAINED_DISK_AMOUNT")
	private String remainedDiskAmount;
	
	@Column(name="RDA_UNIT")
	private String radUnit;
	
	@Column(name="NW_HEALTH")
	private Boolean nwHealth;
	
	@Column(name="PROCESS_ALIVE")
	private Boolean processAlive;
	
	@Column(name="SYSTEM_READY")
	private Boolean systemReady;
	
	@Column(name="SYSTEM_READY_TS")
	private Date systemReadyTs;

	@Column(name="CONTROL_READY")
	private Boolean controlReady;
	
	@Column(name="CONTROL_READY_TS")
	private Date controlReadyTs;
	
	@Column(name="CONTROL_READY_TM")
	private Boolean controlReadyTm;
	
	@Column(name="CONTROL_READY_AL")
	private Boolean controlReadyAl;
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public String getMemoryUsage() {
		return memoryUsage;
	}

	public void setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	public String getDiskUsage() {
		return diskUsage;
	}

	public void setDiskUsage(String diskUsage) {
		this.diskUsage = diskUsage;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRemainedDiskAmount() {
		return remainedDiskAmount;
	}

	public void setRemainedDiskAmount(String remainedDiskAmount) {
		this.remainedDiskAmount = remainedDiskAmount;
	}

	public String getRadUnit() {
		return radUnit;
	}

	public void setRadUnit(String radUnit) {
		this.radUnit = radUnit;
	}

	public Boolean getNwHealth() {
		return nwHealth;
	}

	public void setNwHealth(Boolean nwHealth) {
		this.nwHealth = nwHealth;
	}

	public Boolean getProcessAlive() {
		return processAlive;
	}

	public void setProcessAlive(Boolean processAlive) {
		this.processAlive = processAlive;
	}

	public Boolean getSystemReady() {
		return systemReady;
	}

	public void setSystemReady(Boolean systemReady) {
		this.systemReady = systemReady;
	}

	public Date getSystemReadyTs() {
		return systemReadyTs;
	}

	public void setSystemReadyTs(Date systemReadyTs) {
		this.systemReadyTs = systemReadyTs;
	}

	public Boolean getControlReady() {
		return controlReady;
	}

	public void setControlReady(Boolean controlReady) {
		this.controlReady = controlReady;
	}

	public Date getControlReadyTs() {
		return controlReadyTs;
	}

	public void setControlReadyTs(Date controlReadyTs) {
		this.controlReadyTs = controlReadyTs;
	}

	public Boolean getControlReadyTm() {
		return controlReadyTm;
	}

	public void setControlReadyTm(Boolean controlReadyTm) {
		this.controlReadyTm = controlReadyTm;
	}

	public Boolean getControlReadyAl() {
		return controlReadyAl;
	}

	public void setControlReadyAl(Boolean controlReadyAl) {
		this.controlReadyAl = controlReadyAl;
	}
}
