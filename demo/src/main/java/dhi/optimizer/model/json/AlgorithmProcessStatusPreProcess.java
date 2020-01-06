package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmProcessStatusPreProcess {

	@JsonProperty("last_processing_time")
	private String lastProcessingTime;

	@JsonProperty("last_saving_training_time")
	private String lastSavingTrainingTime;

	@JsonProperty("any_by_pass_valve_open_status")
	private String anyByPassValveOpenStatus;

	@JsonProperty("any_oil_bnr_firing_on_status")
	private String anyOilBnrFiringOnStatus;

	@JsonProperty("runback_active_on_status")
	private String runbackActiveOnStatus;

	@JsonProperty("unit_load_target_low_status")
	private String unitLoadTargetLowStatus;

	@JsonProperty("load_change_detected_status")
	private String loadChangeDetectedStatus;

	@JsonProperty("freq_correction_on_status")
	private String freqCorrectionOnStatus;

	public String getLastProcessingTime() {
		return lastProcessingTime;
	}

	public void setLastProcessingTime(String lastProcessingTime) {
		this.lastProcessingTime = lastProcessingTime;
	}

	public String getLastSavingTrainingTime() {
		return lastSavingTrainingTime;
	}

	public void setLastSavingTrainingTime(String lastSavingTrainingTime) {
		this.lastSavingTrainingTime = lastSavingTrainingTime;
	}

	public String getAnyByPassValveOpenStatus() {
		return anyByPassValveOpenStatus;
	}

	public void setAnyByPassValveOpenStatus(String anyByPassValveOpenStatus) {
		this.anyByPassValveOpenStatus = anyByPassValveOpenStatus;
	}

	public String getAnyOilBnrFiringOnStatus() {
		return anyOilBnrFiringOnStatus;
	}

	public void setAnyOilBnrFiringOnStatus(String anyOilBnrFiringOnStatus) {
		this.anyOilBnrFiringOnStatus = anyOilBnrFiringOnStatus;
	}

	public String getRunbackActiveOnStatus() {
		return runbackActiveOnStatus;
	}

	public void setRunbackActiveOnStatus(String runbackActiveOnStatus) {
		this.runbackActiveOnStatus = runbackActiveOnStatus;
	}

	public String getUnitLoadTargetLowStatus() {
		return unitLoadTargetLowStatus;
	}

	public void setUnitLoadTargetLowStatus(String unitLoadTargetLowStatus) {
		this.unitLoadTargetLowStatus = unitLoadTargetLowStatus;
	}

	public String getLoadChangeDetectedStatus() {
		return loadChangeDetectedStatus;
	}

	public void setLoadChangeDetectedStatus(String loadChangeDetectedStatus) {
		this.loadChangeDetectedStatus = loadChangeDetectedStatus;
	}

	public String getFreqCorrectionOnStatus() {
		return freqCorrectionOnStatus;
	}

	public void setFreqCorrectionOnStatus(String freqCorrectionOnStatus) {
		this.freqCorrectionOnStatus = freqCorrectionOnStatus;
	}
}
