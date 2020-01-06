package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * CoalSupplyStatus parts area json model for CombustionStatus class.
 */
public class CoalSupplyStatus {
	
	@JsonProperty("mill_number")
	private String millNumber;
	
	private String status;
	
	@JsonProperty("coal_current")
	private String coalCurrent;
	
	@JsonProperty("coal_unit")
	private String coalUnit;
	
	@JsonProperty("pa_current")
	private String paCurrent;
	
	@JsonProperty("pa_unit")
	private String paUnit;
	
	@JsonProperty("sad_current")
	private String sadCurrent;
	
	@JsonProperty("sad_unit")
	private String sadUnit;

	public String getMillNumber() {
		return millNumber;
	}

	public void setMillNumber(String millNumber) {
		this.millNumber = millNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCoalCurrent() {
		return coalCurrent;
	}

	public void setCoalCurrent(String coalCurrent) {
		this.coalCurrent = coalCurrent;
	}

	public String getCoalUnit() {
		return coalUnit;
	}

	public void setCoalUnit(String coalUnit) {
		this.coalUnit = coalUnit;
	}
	
	public String getPaCurrent() {
		return paCurrent;
	}

	public void setPaCurrent(String paCurrent) {
		this.paCurrent = paCurrent;
	}

	public String getPaUnit() {
		return paUnit;
	}

	public void setPaUnit(String paUnit) {
		this.paUnit = paUnit;
	}

	public String getSadCurrent() {
		return sadCurrent;
	}

	public void setSadCurrent(String sadCurrent) {
		this.sadCurrent = sadCurrent;
	}

	public String getSadUnit() {
		return sadUnit;
	}

	public void setSadUnit(String sadUnit) {
		this.sadUnit = sadUnit;
	}
}
