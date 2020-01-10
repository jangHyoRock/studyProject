package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NavigatorTubeWallMetalTemp {

	@JsonProperty("tube_wall_front")
	private NavigatorTubeWallItem  tubeWallFrontItem;
	
	@JsonProperty("tube_wall_rear")
	private NavigatorTubeWallItem  tubeWallRearItem;
	
	@JsonProperty("tube_wall_left")
	private NavigatorTubeWallItem  tubeWallLeftItem;
	
	@JsonProperty("tube_wall_right")
	private NavigatorTubeWallItem  tubeWallRightItem;
	
	@JsonProperty("max_metal_temp")
	private String maxMetalTemp;

	public NavigatorTubeWallItem getTubeWallFrontItem() {
		return tubeWallFrontItem;
	}

	public void setTubeWallFrontItem(NavigatorTubeWallItem tubeWallFrontItem) {
		this.tubeWallFrontItem = tubeWallFrontItem;
	}

	public NavigatorTubeWallItem getTubeWallRearItem() {
		return tubeWallRearItem;
	}

	public void setTubeWallRearItem(NavigatorTubeWallItem tubeWallRearItem) {
		this.tubeWallRearItem = tubeWallRearItem;
	}

	public NavigatorTubeWallItem getTubeWallLeftItem() {
		return tubeWallLeftItem;
	}

	public void setTubeWallLeftItem(NavigatorTubeWallItem tubeWallLeftItem) {
		this.tubeWallLeftItem = tubeWallLeftItem;
	}

	public NavigatorTubeWallItem getTubeWallRightItem() {
		return tubeWallRightItem;
	}

	public void setTubeWallRightItem(NavigatorTubeWallItem tubeWallRightItem) {
		this.tubeWallRightItem = tubeWallRightItem;
	}

	public String getMaxMetalTemp() {
		return maxMetalTemp;
	}

	public void setMaxMetalTemp(String maxMetalTemp) {
		this.maxMetalTemp = maxMetalTemp;
	}	
}
