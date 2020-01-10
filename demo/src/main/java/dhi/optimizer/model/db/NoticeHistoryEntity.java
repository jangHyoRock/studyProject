package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_OPDATA)
 */
@Entity
@Table(name="TB_NOTICE_HIST")
public class NoticeHistoryEntity {

	@Id
	@Column(name="TIMESTAMP")
	private Date timestamp;
	
	@Column(name="TYPE")
	private String type;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="DESCRIPTION")
	private String description;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
