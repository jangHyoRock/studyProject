package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.
 */
@Entity
@Table(name="TB_COMPANY")
public class CompanyEntity {

	@Id
	@Column(name="COMPANY_ID")
	private String companyId;
	
	@Column(name="COMPANY_NM")
	private String companyNm;
	
	@Column(name="COMPANY_ORDER")
	private int companyOrder;
	
	@Column(name="DESCRIPTION")
	private String description;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyNm() {
		return companyNm;
	}

	public void setCompanyNm(String companyNm) {
		this.companyNm = companyNm;
	}

	public int getCompanyOrder() {
		return companyOrder;
	}

	public void setCompanyOrder(int companyOrder) {
		this.companyOrder = companyOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
