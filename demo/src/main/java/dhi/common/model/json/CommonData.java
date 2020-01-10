package dhi.common.model.json;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Json model of common API.
 */
public class CommonData {

	@JsonProperty("plant_unit_status")
	private List<PlantInfo> plantUnitStatus;
		
	@JsonProperty("always_display")
	private List<FooterInfo> alwaysDisplay;
	
	@JsonProperty("current_time")
	private String currentTime;
	
	public List<PlantInfo> getPlantUnitStatus() {
		return plantUnitStatus;
	}

	public void setPlantUnitStatus(List<PlantInfo> plantUnitStatus) {
		this.plantUnitStatus = plantUnitStatus;
	}
	
	public List<FooterInfo> getAlwaysDisplay() {
		return alwaysDisplay;
	}

	public void setAlwaysDisplay(List<FooterInfo> alwaysDisplay) {
		this.alwaysDisplay = alwaysDisplay;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}	
}
