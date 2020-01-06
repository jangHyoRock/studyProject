package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigatorTubeWallMetalTempAll {

	@JsonProperty("spiral_tube_wall_metal_temp")
	private NavigatorTubeWallMetalTemp spiralTubeWallMetalTemp;
	
	@JsonProperty("vertical_tube_wall_metal_temp")
	private NavigatorTubeWallMetalTemp verticalTubeWallMetalTemp;

	public NavigatorTubeWallMetalTemp getSpiralTubeWallMetalTemp() {
		return spiralTubeWallMetalTemp;
	}

	public void setSpiralTubeWallMetalTemp(NavigatorTubeWallMetalTemp spiralTubeWallMetalTemp) {
		this.spiralTubeWallMetalTemp = spiralTubeWallMetalTemp;
	}

	public NavigatorTubeWallMetalTemp getVerticalTubeWallMetalTemp() {
		return verticalTubeWallMetalTemp;
	}

	public void setVerticalTubeWallMetalTemp(NavigatorTubeWallMetalTemp verticalTubeWallMetalTemp) {
		this.verticalTubeWallMetalTemp = verticalTubeWallMetalTemp;
	}
}
