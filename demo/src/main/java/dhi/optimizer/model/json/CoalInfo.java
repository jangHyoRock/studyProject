package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * CoalInfo parts area json model for CoalProperty class.
 */
public class CoalInfo {

	private String moisture;
	
	@JsonProperty("moisture_unit")
	private String moistureUnit;
	
	private String gcv;
	
	@JsonProperty("gcv_unit")
	private String gcvUnit;
	
	private String ash;
	
	@JsonProperty("ash_unit")
	private String ashUnit;
		
	public String getMoisture() {
		return moisture;
	}

	public void setMoisture(String moisture) {
		this.moisture = moisture;
	}

	public String getMoistureUnit() {
		return moistureUnit;
	}

	public void setMoistureUnit(String moistureUnit) {
		this.moistureUnit = moistureUnit;
	}

	public String getGcv() {
		return gcv;
	}

	public void setGcv(String gcv) {
		this.gcv = gcv;
	}

	public String getGcvUnit() {
		return gcvUnit;
	}

	public void setGcvUnit(String gcvUnit) {
		this.gcvUnit = gcvUnit;
	}

	public String getAsh() {
		return ash;
	}

	public void setAsh(String ash) {
		this.ash = ash;
	}

	public String getAshUnit() {
		return ashUnit;
	}

	public void setAshUnit(String ashUnit) {
		this.ashUnit = ashUnit;
	}
}
