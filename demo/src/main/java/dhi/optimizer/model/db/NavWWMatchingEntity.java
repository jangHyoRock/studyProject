package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NAV_WW_MATCHING)
 */
@Entity
@Table(name="TB_NAV_WW_MATCHING")
public class NavWWMatchingEntity {

	@EmbeddedId
	private NavWWMatchingIdEntity navWWMatchingIdEntity;
	
	@Column(name="C_1")
	private String c1;
		
	@Column(name="C_2")
	private String c2;
	
	@Column(name="C_3")
	private String c3;
	
	@Column(name="C_4")
	private String c4;
	
	@Column(name="MATCHING_ORDER")
	private int matchingOrder;
	
	@Column(name="TIMESTAMP")
	private Date timestamp;
	
	public NavWWMatchingIdEntity getNavWWMatchingIdEntity() {
		return navWWMatchingIdEntity;
	}

	public void setNavWWMatchingIdEntity(NavWWMatchingIdEntity navWWMatchingIdEntity) {
		this.navWWMatchingIdEntity = navWWMatchingIdEntity;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public String getC3() {
		return c3;
	}

	public void setC3(String c3) {
		this.c3 = c3;
	}

	public String getC4() {
		return c4;
	}

	public void setC4(String c4) {
		this.c4 = c4;
	}

	public int getMatchingOrder() {
		return matchingOrder;
	}

	public void setMatchingOrder(int matchingOrder) {
		this.matchingOrder = matchingOrder;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
