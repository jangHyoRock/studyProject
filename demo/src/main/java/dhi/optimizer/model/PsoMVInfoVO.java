package dhi.optimizer.model;

import java.util.List;

import dhi.optimizer.algorithm.pso.PsoMV.MVType;

/**
 * PSO MV VO Model Class. <br>
 * : PSO MV 정보를 담는 VO Model Class.
 */
public class PsoMVInfoVO {

	private String psoMV;
	
	private MVType psoMVType;
	
	private double psoResultVal;

	private double inputBiasVal;
	
	private boolean hold;
	
	private List<Double> tagSetPointBiasValList;
	
	private List<Double> pureSetPointValList;
	
	private String outputBiasTagId;
	
	private double outputBiasVal;

	public String getPsoMV() {
		return psoMV;
	}

	public void setPsoMV(String psoMV) {
		this.psoMV = psoMV;
	}	

	public MVType getPsoMVType() {
		return psoMVType;
	}

	public void setPsoMVType(MVType psoMVType) {
		this.psoMVType = psoMVType;
	}

	public double getPsoResultVal() {
		return psoResultVal;
	}

	public void setPsoResultVal(double psoResultVal) {
		this.psoResultVal = psoResultVal;
	}

	public double getInputBiasVal() {
		return inputBiasVal;
	}

	public void setInputBiasVal(double inputBiasVal) {
		this.inputBiasVal = inputBiasVal;
	}

	public boolean isHold() {
		return hold;
	}

	public void setHold(boolean hold) {
		this.hold = hold;
	}

	public List<Double> getTagSetPointBiasValList() {
		return tagSetPointBiasValList;
	}

	public void setTagSetPointBiasValList(List<Double> tagSetPointBiasValList) {
		this.tagSetPointBiasValList = tagSetPointBiasValList;
	}
	
	public List<Double> getPureSetPointValList() {
		return pureSetPointValList;
	}

	public void setPureSetPointValList(List<Double> pureSetPointValList) {
		this.pureSetPointValList = pureSetPointValList;
	}

	public String getOutputBiasTagId() {
		return outputBiasTagId;
	}

	public void setOutputBiasTagId(String outputBiasTagId) {
		this.outputBiasTagId = outputBiasTagId;
	}

	public double getOutputBiasVal() {
		return outputBiasVal;
	}

	public void setOutputBiasVal(double outputBiasVal) {
		this.outputBiasVal = outputBiasVal;
	}
}
