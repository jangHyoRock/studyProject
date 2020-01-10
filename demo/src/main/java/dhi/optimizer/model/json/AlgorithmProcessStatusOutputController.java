package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmProcessStatusOutputController {

	@JsonProperty("last_calculation_time")
	private String lastCalculationTime;

	@JsonProperty("mv_status")
	private AlgorithmOptimizerMVStatus algorithmOptimizerMVStatus;

	public String getLastCalculationTime() {
		return lastCalculationTime;
	}

	public void setLastCalculationTime(String lastCalculationTime) {
		this.lastCalculationTime = lastCalculationTime;
	}

	public AlgorithmOptimizerMVStatus getAlgorithmOptimizerMVStatus() {
		return algorithmOptimizerMVStatus;
	}

	public void setAlgorithmOptimizerMVStatus(AlgorithmOptimizerMVStatus algorithmOptimizerMVStatus) {
		this.algorithmOptimizerMVStatus = algorithmOptimizerMVStatus;
	}
}
