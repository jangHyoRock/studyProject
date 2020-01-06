package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NAV_CFD)
 */
@Entity
@Table(name="TB_NAV_CFD")
public class NavCfdEntity {

	@EmbeddedId
	private NavCfdIdEntity navCfdIdEntity;
	
	@Column(name="BNR_P_1")
	private String bnrP1;
	
	@Column(name="BNR_P_2")
	private String bnrP2;
	
	@Column(name="BNR_P_3")
	private String bnrP3;
	
	@Column(name="OFA_P_1")
	private String ofaP1;
	
	@Column(name="OFA_P_2")
	private String ofaP2;
	
	@Column(name="OFA_P_3")
	private String ofaP3;

	public NavCfdIdEntity getNavCfdIdEntity() {
		return navCfdIdEntity;
	}

	public void setNavCfdIdEntity(NavCfdIdEntity navCfdIdEntity) {
		this.navCfdIdEntity = navCfdIdEntity;
	}

	public String getBnrP1() {
		return bnrP1;
	}

	public void setBnrP1(String bnrP1) {
		this.bnrP1 = bnrP1;
	}

	public String getBnrP2() {
		return bnrP2;
	}

	public void setBnrP2(String bnrP2) {
		this.bnrP2 = bnrP2;
	}

	public String getBnrP3() {
		return bnrP3;
	}

	public void setBnrP3(String bnrP3) {
		this.bnrP3 = bnrP3;
	}

	public String getOfaP1() {
		return ofaP1;
	}

	public void setOfaP1(String ofaP1) {
		this.ofaP1 = ofaP1;
	}

	public String getOfaP2() {
		return ofaP2;
	}

	public void setOfaP2(String ofaP2) {
		this.ofaP2 = ofaP2;
	}

	public String getOfaP3() {
		return ofaP3;
	}

	public void setOfaP3(String ofaP3) {
		this.ofaP3 = ofaP3;
	}	
}
