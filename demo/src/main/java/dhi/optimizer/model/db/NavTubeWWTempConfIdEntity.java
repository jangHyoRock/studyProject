package dhi.optimizer.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_NAV_TUBE_WW_TEMP_CONF)
 */
@SuppressWarnings("serial")
@Embeddable
public class NavTubeWWTempConfIdEntity implements Serializable {

	@Column(name="TUBE_TYPE")
	private Date tubeType;
	
	@Column(name="DIRECTION")
	private Date direction;	
	
	@Column(name="TUBE_NO")
	private String tubeNo;
	
	public Date getTubeType() {
		return tubeType;
	}

	public void setTubeType(Date tubeType) {
		this.tubeType = tubeType;
	}

	public Date getDirection() {
		return direction;
	}

	public void setDirection(Date direction) {
		this.direction = direction;
	}

	public String getTubeNo() {
		return tubeNo;
	}

	public void setTubeNo(String tubeNo) {
		this.tubeNo = tubeNo;
	}
}