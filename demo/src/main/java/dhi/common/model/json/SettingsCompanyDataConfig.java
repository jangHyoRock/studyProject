package dhi.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsCompanyDataConfig {

	@JsonProperty("company_id")
	private String companyId;
	
	@JsonProperty("company_nm")
	private String companyNm;
	
	@JsonProperty("company_order")
	private String companyOrder;
		
	@JsonProperty("description")
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

	public String getCompanyOrder() {
		return companyOrder;
	}

	public void setCompanyOrder(String companyOrder) {
		this.companyOrder = companyOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}