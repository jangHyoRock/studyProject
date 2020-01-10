package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmSolutionStatus {
	
	@JsonProperty("last_model_generation_time")
	private String lastModelGenerationTime;
	
	@JsonProperty("last_pso_running_time")
	private String lastPSORunningTime;
	
	@JsonProperty("boiler_process_danger_status")
	private String boilerProcessDangerStatus;
	
	@JsonProperty("boiler_process_danger_change_time")
	private String boilerProcessDangerLastChangeTime;	
	
	@JsonProperty("efficiency_status")
	private List<ItemStatus> efficiencyStatusList;
	
	@JsonProperty("solution_mode_control")
	private List<AlgorithmItemStatus> solutionModeControlList;
	
	@JsonProperty("solution_diagnostic")
	private List<AlgorithmItemStatus> solutionDiagnosticList;
	
	@JsonProperty("optimizer_mv_status")
	private AlgorithmOptimizerMVStatus algorithmOptimizerMVStatus;
		
	@JsonProperty("optimization_performance_overview")
	private List<AlgorithmItemStatus> optimizationPerformanceOverviewList;

	public String getLastModelGenerationTime() {
		return lastModelGenerationTime;
	}

	public void setLastModelGenerationTime(String lastModelGenerationTime) {
		this.lastModelGenerationTime = lastModelGenerationTime;
	}

	public String getLastPSORunningTime() {
		return lastPSORunningTime;
	}

	public void setLastPSORunningTime(String lastPSORunningTime) {
		this.lastPSORunningTime = lastPSORunningTime;
	}
	
	public String getBoilerProcessDangerStatus() {
		return boilerProcessDangerStatus;
	}

	public void setBoilerProcessDangerStatus(String boilerProcessDangerStatus) {
		this.boilerProcessDangerStatus = boilerProcessDangerStatus;
	}

	public String getBoilerProcessDangerLastChangeTime() {
		return boilerProcessDangerLastChangeTime;
	}

	public void setBoilerProcessDangerLastChangeTime(String boilerProcessDangerLastChangeTime) {
		this.boilerProcessDangerLastChangeTime = boilerProcessDangerLastChangeTime;
	}

	public List<ItemStatus> getEfficiencyStatusList() {
		return efficiencyStatusList;
	}

	public void setEfficiencyStatusList(List<ItemStatus> efficiencyStatusList) {
		this.efficiencyStatusList = efficiencyStatusList;
	}

	public List<AlgorithmItemStatus> getSolutionModeControlList() {
		return solutionModeControlList;
	}

	public void setSolutionModeControlList(List<AlgorithmItemStatus> solutionModeControlList) {
		this.solutionModeControlList = solutionModeControlList;
	}
	
	public List<AlgorithmItemStatus> getSolutionDiagnosticList() {
		return solutionDiagnosticList;
	}

	public void setSolutionDiagnosticList(List<AlgorithmItemStatus> solutionDiagnosticList) {
		this.solutionDiagnosticList = solutionDiagnosticList;
	}

	public AlgorithmOptimizerMVStatus getAlgorithmOptimizerMVStatus() {
		return algorithmOptimizerMVStatus;
	}

	public void setAlgorithmOptimizerMVStatus(AlgorithmOptimizerMVStatus algorithmOptimizerMVStatus) {
		this.algorithmOptimizerMVStatus = algorithmOptimizerMVStatus;
	}

	public List<AlgorithmItemStatus> getOptimizationPerformanceOverviewList() {
		return optimizationPerformanceOverviewList;
	}

	public void setOptimizationPerformanceOverviewList(List<AlgorithmItemStatus> optimizationPerformanceOverviewList) {
		this.optimizationPerformanceOverviewList = optimizationPerformanceOverviewList;
	}	
}
