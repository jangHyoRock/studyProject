package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigatorConfig {

	@JsonProperty("excess_air_ratio")
	private Double excessAirRatio;

	@JsonProperty("metal_temp_filter_rate")
	private Double metalTempFilterRate;
	
	@JsonProperty("metal_temp_allowable_vlaue")
	private Double metalTempAllowableVlaue;
	
	@JsonProperty("allowable_flue_gas_temp_dev")	
	private Double allowableFlueGasTempDev;
		
	@JsonProperty("allowable_spray_flow_dev")
	private Double allowableSprayFlowDev;
	
	public Double getExcessAirRatio() {
		return excessAirRatio;
	}

	public void setExcessAirRatio(Double excessAirRatio) {
		this.excessAirRatio = excessAirRatio;
	}	

	public Double getMetalTempFilterRate() {
		return metalTempFilterRate;
	}

	public void setMetalTempFilterRate(Double metalTempFilterRate) {
		this.metalTempFilterRate = metalTempFilterRate;
	}

	public Double getMetalTempAllowableVlaue() {
		return metalTempAllowableVlaue;
	}

	public void setMetalTempAllowableVlaue(Double metalTempAllowableVlaue) {
		this.metalTempAllowableVlaue = metalTempAllowableVlaue;
	}

	public Double getAllowableFlueGasTempDev() {
		return allowableFlueGasTempDev;
	}

	public void setAllowableFlueGasTempDev(Double allowableFlueGasTempDev) {
		this.allowableFlueGasTempDev = allowableFlueGasTempDev;
	}

	public Double getAllowableSprayFlowDev() {
		return allowableSprayFlowDev;
	}

	public void setAllowableSprayFlowDev(Double allowableSprayFlowDev) {
		this.allowableSprayFlowDev = allowableSprayFlowDev;
	}
}
