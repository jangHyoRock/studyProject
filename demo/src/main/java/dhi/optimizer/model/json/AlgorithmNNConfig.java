package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmNNConfig {

	@JsonProperty("learning_rate")
	private double learningRate;
		
	private double momentum;
	
	@JsonProperty("sigmoid_alpha_value")
	private double sigmoidAlphaValue;
	
	@JsonProperty("neruons_1st_layer")
	private int neruons1stLayer;
	
	@JsonProperty("neruons_2nd_layer")
	private int neruons2ndLayer;
	
	@JsonProperty("training_algorithm")
	private String trainingAlgorithm;
		
	private int iterations;
	
	@JsonProperty("training_time")
	private String trainTime;
	
	private double mu;
	
	@JsonProperty("validation_check")
	private int validationCheck;
	
	@JsonProperty("training_mse")
	private double trainingMSE;
	
	@JsonProperty("validation_mse")
	private double validationMSE;
	
	@JsonProperty("validation_data_last_hours")
	private int validationDataLastHours;
	
	@JsonProperty("validation_data_min_count")
	private int validationDataMinCount;
	
	@JsonProperty("training_data_last_days")
	private int trainingDataLastDays;
	
	@JsonProperty("training_data_min_count")
	private int trainingDataMinCount;
	
	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getMomentum() {
		return momentum;
	}

	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

	public double getSigmoidAlphaValue() {
		return sigmoidAlphaValue;
	}

	public void setSigmoidAlphaValue(double sigmoidAlphaValue) {
		this.sigmoidAlphaValue = sigmoidAlphaValue;
	}

	public int getNeruons1stLayer() {
		return neruons1stLayer;
	}

	public void setNeruons1stLayer(int neruons1stLayer) {
		this.neruons1stLayer = neruons1stLayer;
	}	

	public int getNeruons2ndLayer() {
		return neruons2ndLayer;
	}

	public void setNeruons2ndLayer(int neruons2ndLayer) {
		this.neruons2ndLayer = neruons2ndLayer;
	}

	public String getTrainingAlgorithm() {
		return trainingAlgorithm;
	}

	public void setTrainingAlgorithm(String trainingAlgorithm) {
		this.trainingAlgorithm = trainingAlgorithm;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public String getTrainTime() {
		return trainTime;
	}

	public void setTrainTime(String trainTime) {
		this.trainTime = trainTime;
	}

	public double getMu() {
		return mu;
	}

	public void setMu(double mu) {
		this.mu = mu;
	}

	public int getValidationCheck() {
		return validationCheck;
	}

	public void setValidationCheck(int validationCheck) {
		this.validationCheck = validationCheck;
	}

	public double getTrainingMSE() {
		return trainingMSE;
	}

	public void setTrainingMSE(double trainingMSE) {
		this.trainingMSE = trainingMSE;
	}

	public double getValidationMSE() {
		return validationMSE;
	}

	public void setValidationMSE(double validationMSE) {
		this.validationMSE = validationMSE;
	}
	
	public int getValidationDataLastHours() {
		return validationDataLastHours;
	}

	public void setValidationDataLastHours(int validationDataLastHours) {
		this.validationDataLastHours = validationDataLastHours;
	}

	public int getValidationDataMinCount() {
		return validationDataMinCount;
	}

	public void setValidationDataMinCount(int validationDataMinCount) {
		this.validationDataMinCount = validationDataMinCount;
	}

	public int getTrainingDataLastDays() {
		return trainingDataLastDays;
	}

	public void setTrainingDataLastDays(int trainingDataLastDays) {
		this.trainingDataLastDays = trainingDataLastDays;
	}

	public int getTrainingDataMinCount() {
		return trainingDataMinCount;
	}

	public void setTrainingDataMinCount(int trainingDataMinCount) {
		this.trainingDataMinCount = trainingDataMinCount;
	}
}
