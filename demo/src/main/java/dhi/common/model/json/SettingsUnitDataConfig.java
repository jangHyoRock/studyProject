package dhi.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsUnitDataConfig {

	@JsonProperty("p_unit_id")
	private String pUnitId;
	
	@JsonProperty("p_unit_nm")
	private String pUnitNm;
	
	@JsonProperty("p_unit_order")
	private String pUnitOrder;
		
	@JsonProperty("description")
	private String description;

	public String getpUnitId() {
		return pUnitId;
	}

	public void setpUnitId(String pUnitId) {
		this.pUnitId = pUnitId;
	}

	public String getpUnitNm() {
		return pUnitNm;
	}

	public void setpUnitNm(String pUnitNm) {
		this.pUnitNm = pUnitNm;
	}

	public String getpUnitOrder() {
		return pUnitOrder;
	}

	public void setpUnitOrder(String pUnitOrder) {
		this.pUnitOrder = pUnitOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}