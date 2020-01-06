package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmProcessStatusPSO {

	@JsonProperty("last_pso_running_time")
	private String lastPSORunningTime;
	
	@JsonProperty("model_test_error_sum")
	private String modelTestErrorSum;	
	
	@JsonProperty("model_test_error_limit")
	private String modelTestErrorLimit;
	
	@JsonProperty("object_function_f_val")
	private String objectFuntionFVal;
	
	@JsonProperty("object_function_optimal_f_val")
	private String objectFuntionOptimalFVal;
	
	@JsonProperty("optimization_performance_prediction")	
	private List<AlgorithmItemStatus> optimizationPerformancePredictionList;
	
	@JsonProperty("optimal_bias")
	private String[] arrayOptimalBias;

	public String getLastPSORunningTime() {
		return lastPSORunningTime;
	}

	public void setLastPSORunningTime(String lastPSORunningTime) {
		this.lastPSORunningTime = lastPSORunningTime;
	}
	
	public String getModelTestErrorSum() {
		return modelTestErrorSum;
	}

	public void setModelTestErrorSum(String modelTestErrorSum) {
		this.modelTestErrorSum = modelTestErrorSum;
	}

	public String getModelTestErrorLimit() {
		return modelTestErrorLimit;
	}

	public void setModelTestErrorLimit(String modelTestErrorLimit) {
		this.modelTestErrorLimit = modelTestErrorLimit;
	}

	public String getObjectFuntionFVal() {
		return objectFuntionFVal;
	}

	public void setObjectFuntionFVal(String objectFuntionFVal) {
		this.objectFuntionFVal = objectFuntionFVal;
	}

	public String getObjectFuntionOptimalFVal() {
		return objectFuntionOptimalFVal;
	}

	public void setObjectFuntionOptimalFVal(String objectFuntionOptimalFVal) {
		this.objectFuntionOptimalFVal = objectFuntionOptimalFVal;
	}

	public List<AlgorithmItemStatus> getOptimizationPerformancePredictionList() {
		return optimizationPerformancePredictionList;
	}

	public void setOptimizationPerformancePredictionList(List<AlgorithmItemStatus> optimizationPerformancePredictionList) {
		this.optimizationPerformancePredictionList = optimizationPerformancePredictionList;
	}

	public String[] getArrayOptimalBias() {
		return arrayOptimalBias;
	}

	public void setArrayOptimalBias(String[] arrayOptimalBias) {
		this.arrayOptimalBias = arrayOptimalBias;
	}
}
