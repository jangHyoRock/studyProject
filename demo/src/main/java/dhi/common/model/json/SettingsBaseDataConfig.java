package dhi.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsBaseDataConfig {

	@JsonProperty("company_id")
	private String companyId;
	
	@JsonProperty("p_unit_id")
	private String pUnitId;
	
	@JsonProperty("plant_id")
	private String plantId;
	
	@JsonProperty("sys_id")
	private String sysId;
	
	@JsonProperty("p_unit_ip")
	private String pUnitIp;
	
	@JsonProperty("p_unit_port")
	private String pUnitPort;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("p_unit_no")
	private String pUnitNo;
	

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getpUnitId() {
		return pUnitId;
	}

	public void setpUnitId(String pUnitId) {
		this.pUnitId = pUnitId;
	}

	public String getPlantId() {
		return plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getpUnitIp() {
		return pUnitIp;
	}

	public void setpUnitIp(String pUnitIp) {
		this.pUnitIp = pUnitIp;
	}

	public String getpUnitPort() {
		return pUnitPort;
	}

	public void setpUnitPort(String pUnitPort) {
		this.pUnitPort = pUnitPort;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getpUnitNo() {
		return pUnitNo;
	}

	public void setpUnitNo(String pUnitNo) {
		this.pUnitNo = pUnitNo;
	}	
}