package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmProcessStatusNNModel {

	@JsonProperty("pre_model_generation_time")
	private String preModelGenerationTime;

	@JsonProperty("last_model_generation_time")
	private String lastModelGenerationTime;

	@JsonProperty("seed_model_1_save_time")
	private AlgorithmProcessStatusNNModelDetail seedModel1SaveTime;

	@JsonProperty("seed_model_2_save_time")
	private AlgorithmProcessStatusNNModelDetail seedModel2SaveTime;

	@JsonProperty("last_model_valid_error")
	private String lastModelValidError;

	@JsonProperty("fruit_model_1_save_time")
	private AlgorithmProcessStatusNNModelDetail fruitModel1SaveTime;

	@JsonProperty("fruit_model_2_save_time")
	private AlgorithmProcessStatusNNModelDetail fruitModel2SaveTime;

	@JsonProperty("fruit_model_3_save_time")
	private AlgorithmProcessStatusNNModelDetail fruitModel3SaveTime;

	public String getPreModelGenerationTime() {
		return preModelGenerationTime;
	}

	public void setPreModelGenerationTime(String preModelGenerationTime) {
		this.preModelGenerationTime = preModelGenerationTime;
	}

	public String getLastModelGenerationTime() {
		return lastModelGenerationTime;
	}

	public void setLastModelGenerationTime(String lastModelGenerationTime) {
		this.lastModelGenerationTime = lastModelGenerationTime;
	}

	public AlgorithmProcessStatusNNModelDetail getSeedModel1SaveTime() {
		return seedModel1SaveTime;
	}

	public void setSeedModel1SaveTime(AlgorithmProcessStatusNNModelDetail seedModel1SaveTime) {
		this.seedModel1SaveTime = seedModel1SaveTime;
	}

	public AlgorithmProcessStatusNNModelDetail getSeedModel2SaveTime() {
		return seedModel2SaveTime;
	}

	public void setSeedModel2SaveTime(AlgorithmProcessStatusNNModelDetail seedModel2SaveTime) {
		this.seedModel2SaveTime = seedModel2SaveTime;
	}

	public String getLastModelValidError() {
		return lastModelValidError;
	}

	public void setLastModelValidError(String lastModelValidError) {
		this.lastModelValidError = lastModelValidError;
	}

	public AlgorithmProcessStatusNNModelDetail getFruitModel1SaveTime() {
		return fruitModel1SaveTime;
	}

	public void setFruitModel1SaveTime(AlgorithmProcessStatusNNModelDetail fruitModel1SaveTime) {
		this.fruitModel1SaveTime = fruitModel1SaveTime;
	}

	public AlgorithmProcessStatusNNModelDetail getFruitModel2SaveTime() {
		return fruitModel2SaveTime;
	}

	public void setFruitModel2SaveTime(AlgorithmProcessStatusNNModelDetail fruitModel2SaveTime) {
		this.fruitModel2SaveTime = fruitModel2SaveTime;
	}

	public AlgorithmProcessStatusNNModelDetail getFruitModel3SaveTime() {
		return fruitModel3SaveTime;
	}

	public void setFruitModel3SaveTime(AlgorithmProcessStatusNNModelDetail fruitModel3SaveTime) {
		this.fruitModel3SaveTime = fruitModel3SaveTime;
	}
}
