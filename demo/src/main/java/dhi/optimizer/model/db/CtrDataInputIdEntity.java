package dhi.optimizer.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_CTR_DATA_INPUT)
 */
@SuppressWarnings("serial")
@Embeddable
public class CtrDataInputIdEntity implements Serializable {

	@Column(name="TIMESTAMP")
	private Date timestamp;	
	
	@Column(name="TAG_NM")
	private String tagNm;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTagNm() {
		return tagNm;
	}

	public void setTagNm(String tagNm) {
		this.tagNm = tagNm;
	}
}