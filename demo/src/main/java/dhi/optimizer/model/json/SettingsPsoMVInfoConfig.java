package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsPsoMVInfoConfig {

	@JsonProperty("pso_mv")
	private String psoMV;
	
	@JsonProperty("pso_mv_type")
	private String psoMVType;
	
	@JsonProperty("pso_mv_max")
	private Double psoMVMax;
	
	@JsonProperty("pso_mv_min")
	private Double psoMVMin;
	
	@JsonProperty("pso_mv_order")
	private int psoMVOrder;
	
	@JsonProperty("auto_mode_tag_id")
	private String autoModeTagId;
	
	@JsonProperty("hold_tag_id")
	private String holdTagId;
	
	@JsonProperty("input_bias_tag_id")
	private String inputBiasTagId;
	
	@JsonProperty("output_bias_tag_id")
	private String outputBiasTagId;

	public String getPsoMV() {
		return psoMV;
	}

	public void setPsoMV(String psoMV) {
		this.psoMV = psoMV;
	}

	public String getPsoMVType() {
		return psoMVType;
	}

	public void setPsoMVType(String psoMVType) {
		this.psoMVType = psoMVType;
	}

	public Double getPsoMVMax() {
		return psoMVMax;
	}

	public void setPsoMVMax(Double psoMVMax) {
		this.psoMVMax = psoMVMax;
	}

	public Double getPsoMVMin() {
		return psoMVMin;
	}

	public void setPsoMVMin(Double psoMVMin) {
		this.psoMVMin = psoMVMin;
	}

	public int getPsoMVOrder() {
		return psoMVOrder;
	}

	public void setPsoMVOrder(int psoMVOrder) {
		this.psoMVOrder = psoMVOrder;
	}

	public String getAutoModeTagId() {
		return autoModeTagId;
	}

	public void setAutoModeTagId(String autoModeTagId) {
		this.autoModeTagId = autoModeTagId;
	}

	public String getHoldTagId() {
		return holdTagId;
	}

	public void setHoldTagId(String holdTagId) {
		this.holdTagId = holdTagId;
	}

	public String getInputBiasTagId() {
		return inputBiasTagId;
	}

	public void setInputBiasTagId(String inputBiasTagId) {
		this.inputBiasTagId = inputBiasTagId;
	}

	public String getOutputBiasTagId() {
		return outputBiasTagId;
	}

	public void setOutputBiasTagId(String outputBiasTagId) {
		this.outputBiasTagId = outputBiasTagId;
	}
}
