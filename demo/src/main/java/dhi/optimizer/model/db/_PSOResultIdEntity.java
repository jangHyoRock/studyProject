package dhi.optimizer.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(DD_TB_PSO_RESULT)
 */
@SuppressWarnings("serial")
@Embeddable
public class _PSOResultIdEntity implements Serializable {

	@Column(name = "TIMESTAMP")
	private Date timestamp;

	@Column(name = "PSO_ID")
	private String psoId;

	public _PSOResultIdEntity() {
	};

	public _PSOResultIdEntity(Date timestamp, String psoId) {
		this.timestamp = timestamp;
		this.psoId = psoId;
	};

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getPsoId() {
		return psoId;
	}

	public void setPsoId(String psoId) {
		this.psoId = psoId;
	}
}
