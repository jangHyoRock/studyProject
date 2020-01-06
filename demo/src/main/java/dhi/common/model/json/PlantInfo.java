package dhi.common.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Plant information json model in bottom of common API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlantInfo {

	@JsonProperty("plant")
	private List<PlantInfo> plantInfoList;
	
	@JsonProperty("company_name")
	private String companyName;
	
	@JsonProperty("plant_unit_id")
	private String plantUnitId;
	
	@JsonProperty("plant_unit_name")
	private String plantUnitName;
	
	@JsonProperty("plant_unit_ip")
	private String plantUnitIp;	
	
	@JsonProperty("plant_unit_port")
	private String plantUnitPort;	
	
	@JsonProperty("status")
	private String status;

	public PlantInfo() {};
	
	public PlantInfo(String plantUnitId, String status)
	{
		this.plantUnitId = plantUnitId;
		this.status = status;	
	}
	
	public PlantInfo(String plantUnitId, String plantUnitName, String plantUnitIp, String plantUnitPort)
	{
		this.plantUnitId = plantUnitId;
		this.plantUnitName = plantUnitName;
		this.plantUnitIp = plantUnitIp;
		this.plantUnitPort = plantUnitPort;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public List<PlantInfo> getPlantInfoList() {
		return plantInfoList;
	}

	public void setPlantInfoList(List<PlantInfo> plantInfoList) {
		this.plantInfoList = plantInfoList;
	}

	public String getPlantUnitId() {
		return plantUnitId;
	}

	public void setPlantUnitId(String plantUnitId) {
		this.plantUnitId = plantUnitId;
	}	

	public String getPlantUnitName() {
		return plantUnitName;
	}

	public void setPlantUnitName(String plantUnitName) {
		this.plantUnitName = plantUnitName;
	}

	public String getPlantUnitIp() {
		return plantUnitIp;
	}

	public void setPlantUnitIp(String plantUnitIp) {
		this.plantUnitIp = plantUnitIp;
	}

	public String getPlantUnitPort() {
		return plantUnitPort;
	}

	public void setPlantUnitPort(String plantUnitPort) {
		this.plantUnitPort = plantUnitPort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}	
}