package dhi.optimizer.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Overview main json model of screen.
 */
public class Overview {

	@JsonProperty("efficiency_status")
	private ArrayList<ItemStatus> efficiencyStatus;
	
	@JsonProperty("emission_status")
	private ArrayList<ItemStatus> emissionStatus;
	
	@JsonProperty("slagging_fouling_status")
	private ArrayList<ItemStatus> slaggingFoulingStatus;
	
	@JsonProperty("optimizer_mode")
	private ArrayList<ItemStatus> optimizerMode;
	
	@JsonProperty("optimum_zone")
	private ArrayList<ItemStatus> optimumZone;

	public ArrayList<ItemStatus> getEfficiencyStatus() {
		return this.efficiencyStatus;
	}

	public void setEfficiencyStatus(ArrayList<ItemStatus> efficiencyStatus) {
		this.efficiencyStatus = efficiencyStatus;
	}

	public ArrayList<ItemStatus> getEmissionStatus() {
		return this.emissionStatus;
	}

	public void setEmissionStatus(ArrayList<ItemStatus> emissionStatus) {
		this.emissionStatus = emissionStatus;
	}
	
	public ArrayList<ItemStatus> getSlaggingFoulingStatus() {
		return slaggingFoulingStatus;
	}

	public void setSlaggingFoulingStatus(ArrayList<ItemStatus> slaggingFoulingStatus) {
		this.slaggingFoulingStatus = slaggingFoulingStatus;
	}

	public ArrayList<ItemStatus> getOptimizerMode() {
		return this.optimizerMode;
	}

	public void setOptimizerMode(ArrayList<ItemStatus> optimizerMode) {
		this.optimizerMode = optimizerMode;
	}

	public ArrayList<ItemStatus> getOptimumZone() {
		return optimumZone;
	}

	public void setOptimumZone(ArrayList<ItemStatus> optimumZone) {
		this.optimumZone = optimumZone;
	}
}
