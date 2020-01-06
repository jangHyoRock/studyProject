package dhi.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsPlantDataConfig {

	@JsonProperty("plant_id")
	private String plantId;
	
	@JsonProperty("plant_nm")
	private String plantNm;
	
	@JsonProperty("plant_order")
	private String plantOrder;
		
	@JsonProperty("description")
	private String description;

	public String getPlantId() {
		return plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public String getPlantNm() {
		return plantNm;
	}

	public void setPlantNm(String plantNm) {
		this.plantNm = plantNm;
	}

	public String getPlantOrder() {
		return plantOrder;
	}

	public void setPlantOrder(String plantOrder) {
		this.plantOrder = plantOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}