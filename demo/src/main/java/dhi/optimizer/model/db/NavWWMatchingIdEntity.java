package dhi.optimizer.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(TB_NAV_WW_MATCHING)
 */
@SuppressWarnings("serial")
@Embeddable
public class NavWWMatchingIdEntity implements Serializable {

	@Column(name="P_1")
	private String p1;

	@Column(name="P_2")
	private String p2;
	
	@Column(name="P_3")
	private String p3;
	
	@Column(name="P_4")
	private String p4;
	
	@Column(name="TYPE")
	private String type;

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		this.p4 = p4;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
