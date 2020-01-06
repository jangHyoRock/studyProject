package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigatorCombustionSpiralWaterWall {
	
	@JsonProperty("sprial_water_wall_temp")
	private List<ItemStatus> spiralWaterWallTempList;
		
	@JsonProperty("fire_ball_postion")
	private Position fireBallPostion;

	public List<ItemStatus> getSpiralWaterWallTempList() {
		return spiralWaterWallTempList;
	}

	public void setSpiralWaterWallTempList(List<ItemStatus> spiralWaterWallTempList) {
		this.spiralWaterWallTempList = spiralWaterWallTempList;
	}

	public Position getFireBallPostion() {
		return fireBallPostion;
	}

	public void setFireBallPostion(Position fireBallPostion) {
		this.fireBallPostion = fireBallPostion;
	}
}
