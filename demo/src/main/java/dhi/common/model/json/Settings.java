package dhi.common.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Settings {

	@JsonProperty("plant_unit_id")
	private String plantUnitId;
	
	@JsonProperty("check")
	private Boolean check;
		
	@JsonProperty("data")
	private List<ConfigItem> dataList;

	public String getPlantUnitId() {
		return plantUnitId;
	}

	public void setPlantUnitId(String plantUnitId) {
		this.plantUnitId = plantUnitId;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public List<ConfigItem> getDataList() {
		return dataList;
	}

	public void setDataList(List<ConfigItem> dataList) {
		this.dataList = dataList;
	}	
}
