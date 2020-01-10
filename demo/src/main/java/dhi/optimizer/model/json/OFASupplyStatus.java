package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OFASupplyStatus {

	private String item;
	
	@JsonProperty("damper_open_current")
	private String damperOpenCurrnet;
	
	@JsonProperty("damper_unit")
	private String damperUnit;
	
	@JsonProperty("yaw_current")
	private String yawCurrent;
	
	@JsonProperty("yaw_unit")
	private String yawUnit;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getDamperOpenCurrnet() {
		return damperOpenCurrnet;
	}

	public void setDamperOpenCurrnet(String damperOpenCurrnet) {
		this.damperOpenCurrnet = damperOpenCurrnet;
	}

	public String getDamperUnit() {
		return damperUnit;
	}

	public void setDamperUnit(String damperUnit) {
		this.damperUnit = damperUnit;
	}	

	public String getYawCurrent() {
		return yawCurrent;
	}

	public void setYawCurrent(String yawCurrent) {
		this.yawCurrent = yawCurrent;
	}

	public String getYawUnit() {
		return yawUnit;
	}

	public void setYawUnit(String yawUnit) {
		this.yawUnit = yawUnit;
	}
}
