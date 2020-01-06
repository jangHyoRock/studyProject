package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SASupplyStatus {

	@JsonProperty("sa_number")
	private String saNumber;

	@JsonProperty("damper_open_current")
	private String damperOpenCurrent;
	
	@JsonProperty("damper_unit")
	private String damperUnit;

	public SASupplyStatus() {}

	public SASupplyStatus(String saNumber, String damperOpenCurrent, String damperUnit) {
		this.saNumber = saNumber;
		this.damperOpenCurrent = damperOpenCurrent;
		this.damperUnit = damperUnit;
	}

	public String getSaNumber() {
		return saNumber;
	}

	public void setSaNumber(String saNumber) {
		this.saNumber = saNumber;
	}

	public String getDamperOpenCurrent() {
		return damperOpenCurrent;
	}

	public void setDamperOpenCurrent(String damperOpenCurrent) {
		this.damperOpenCurrent = damperOpenCurrent;
	}

	public String getDamperUnit() {
		return damperUnit;
	}

	public void setDamperUnit(String damperUnit) {
		this.damperUnit = damperUnit;
	}
}
