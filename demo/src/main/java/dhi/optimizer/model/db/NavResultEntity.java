package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NAV_RESULT)
 */
@Entity
@Table(name="TB_NAV_RESULT")
public class NavResultEntity {

	@EmbeddedId
	private NavResultIdEntity navResultIdEntity;
	
	@Column(name="YAW")
	private String yaw;
		
	@Column(name="F")
	private String f;
	
	@Column(name="D")
	private String d;
	
	@Column(name="E")
	private String e;
	
	@Column(name="TIMESTAMP")
	private Date timestamp;

	public NavResultIdEntity getNavResultIdEntity() {
		return navResultIdEntity;
	}

	public void setNavResultIdEntity(NavResultIdEntity navResultIdEntity) {
		this.navResultIdEntity = navResultIdEntity;
	}

	public String getYaw() {
		return yaw;
	}

	public void setYaw(String yaw) {
		this.yaw = yaw;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}