package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmOptimizerMVStatus {

	@JsonProperty("mv_status")
	private List<AlgorithmItemStatus> optimizerMVStatusList;
		
	@JsonProperty("all_hold_status")
	private String allHoldStatus;
	
	@JsonProperty("initialize_status")
	private String initializeStatus;

	public List<AlgorithmItemStatus> getOptimizerMVStatusList() {
		return optimizerMVStatusList;
	}

	public void setOptimizerMVStatusList(List<AlgorithmItemStatus> optimizerMVStatusList) {
		this.optimizerMVStatusList = optimizerMVStatusList;
	}
	public String getAllHoldStatus() {
		return allHoldStatus;
	}

	public void setAllHoldStatus(String allHoldStatus) {
		this.allHoldStatus = allHoldStatus;
	}

	public String getInitializeStatus() {
		return initializeStatus;
	}

	public void setInitializeStatus(String initializeStatus) {
		this.initializeStatus = initializeStatus;
	}
	
}
