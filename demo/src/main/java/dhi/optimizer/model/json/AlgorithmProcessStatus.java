package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmProcessStatus {
	
	@JsonProperty("data_pre_process")
	private AlgorithmProcessStatusPreProcess algorithmprocessStatusPreProcess;
	
	@JsonProperty("nn_model_generator")
	private AlgorithmProcessStatusNNModel algorithmprocessStatusNNModel;
	
	@JsonProperty("static_optimizer")
	private AlgorithmProcessStatusPSO algorithmprocessStatusPSO;
	
	@JsonProperty("output_controller")
	private AlgorithmProcessStatusOutputController algorithmProcessStatusOutputController;

	public AlgorithmProcessStatusPreProcess getAlgorithmprocessStatusPreProcess() {
		return algorithmprocessStatusPreProcess;
	}

	public void setAlgorithmprocessStatusPreProcess(AlgorithmProcessStatusPreProcess algorithmprocessStatusPreProcess) {
		this.algorithmprocessStatusPreProcess = algorithmprocessStatusPreProcess;
	}

	public AlgorithmProcessStatusNNModel getAlgorithmprocessStatusNNModel() {
		return algorithmprocessStatusNNModel;
	}

	public void setAlgorithmprocessStatusNNModel(AlgorithmProcessStatusNNModel algorithmprocessStatusNNModel) {
		this.algorithmprocessStatusNNModel = algorithmprocessStatusNNModel;
	}

	public AlgorithmProcessStatusPSO getAlgorithmprocessStatusPSO() {
		return algorithmprocessStatusPSO;
	}

	public void setAlgorithmprocessStatusPSO(AlgorithmProcessStatusPSO algorithmprocessStatusPSO) {
		this.algorithmprocessStatusPSO = algorithmprocessStatusPSO;
	}

	public AlgorithmProcessStatusOutputController getAlgorithmProcessStatusOutputController() {
		return algorithmProcessStatusOutputController;
	}

	public void setAlgorithmProcessStatusOutputController(
			AlgorithmProcessStatusOutputController algorithmProcessStatusOutputController) {
		this.algorithmProcessStatusOutputController = algorithmProcessStatusOutputController;
	}	
}
