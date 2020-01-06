package dhi.optimizer.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PerformanceAndEfficiency {

	@JsonProperty("efficiency_kpi")
	private ArrayList<PerformanceItemStatus> efficiencyKPI;
	
	@JsonProperty("fuel_information")
	private ArrayList<PerformanceItemStatus> fuelInformation;
	
	@JsonProperty("combustion_condition")
	private ArrayList<PerformanceItemStatus> combustionCondition;
	
	@JsonProperty("process_status")
	private PerformanceProcessStatus processStatus;
	
	@JsonProperty("emission_status")
	private PerformanceEmissionStatus emissionStatus;
	
	@JsonProperty("cost_saving_information_version_sasan")
	private ArrayList<PerformanceConstSaving> costSavingInformationVersionSasan;

	public ArrayList<PerformanceItemStatus> getEfficiencyKPI() {
		return efficiencyKPI;
	}

	public void setEfficiencyKPI(ArrayList<PerformanceItemStatus> efficiencyKPI) {
		this.efficiencyKPI = efficiencyKPI;
	}

	public ArrayList<PerformanceItemStatus> getFuelInformation() {
		return fuelInformation;
	}

	public void setFuelInformation(ArrayList<PerformanceItemStatus> fuelInformation) {
		this.fuelInformation = fuelInformation;
	}

	public ArrayList<PerformanceItemStatus> getCombustionCondition() {
		return combustionCondition;
	}

	public void setCombustionCondition(ArrayList<PerformanceItemStatus> combustionCondition) {
		this.combustionCondition = combustionCondition;
	}

	public PerformanceProcessStatus getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(PerformanceProcessStatus processStatus) {
		this.processStatus = processStatus;
	}

	public PerformanceEmissionStatus getEmissionStatus() {
		return emissionStatus;
	}

	public void setEmissionStatus(PerformanceEmissionStatus emissionStatus) {
		this.emissionStatus = emissionStatus;
	}

	public ArrayList<PerformanceConstSaving> getCostSavingInformationVersionSasan() {
		return costSavingInformationVersionSasan;
	}

	public void setCostSavingInformationVersionSasan(ArrayList<PerformanceConstSaving> costSavingInformationVersionSasan) {
		this.costSavingInformationVersionSasan = costSavingInformationVersionSasan;
	}
}
