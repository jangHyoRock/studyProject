package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigatorLimitsAndFireBallCenter {

	@JsonProperty("limit")
	private List<NavigatorLimit> limitList;
	
	@JsonProperty("fire_ball_center")
	private List<NavigatorFireBallCenter> fireBallCenterList;

	public List<NavigatorLimit> getLimitList() {
		return limitList;
	}

	public void setLimitList(List<NavigatorLimit> limitList) {
		this.limitList = limitList;
	}

	public List<NavigatorFireBallCenter> getFireBallCenterList() {
		return fireBallCenterList;
	}

	public void setFireBallCenterList(List<NavigatorFireBallCenter> fireBallCenterList) {
		this.fireBallCenterList = fireBallCenterList;
	}
}
