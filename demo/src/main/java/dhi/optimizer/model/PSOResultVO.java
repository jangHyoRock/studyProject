package dhi.optimizer.model;

import dhi.optimizer.algorithm.pso.PsoCalculationFunction;
import dhi.optimizer.model.db.PSOResultEntity;
import dhi.optimizer.model.db._NNTrainDataConfEntity;

public class PSOResultVO {
	
	private String psoID;
	
	private double globalBestValue;
	
	private double[] globalBestMV;
	
	private double[] globalBestOutput;
	
	private double[] outputDataValues;
	
	private double[] outputModelResult;
	
	private String mvValues;
	
	private PsoCalculationFunction psoCalculationFunction;
	
	private _NNTrainDataConfEntity outputTagInfoEntity;

	public String getPsoID() {
		return psoID;
	}

	public void setPsoID(String psoID) {
		this.psoID = psoID;
	}

	public double getGlobalBestValue() {
		return globalBestValue;
	}

	public void setGlobalBestValue(double globalBestValue) {
		this.globalBestValue = globalBestValue;
	}

	public double[] getGlobalBestMV() {
		return globalBestMV;
	}

	public void setGlobalBestMV(double[] globalBestMV) {
		this.globalBestMV = globalBestMV;
	}

	public double[] getGlobalBestOutput() {
		return globalBestOutput;
	}

	public void setGlobalBestOutput(double[] globalBestOutput) {
		this.globalBestOutput = globalBestOutput;
	}

	public double[] getOutputDataValues() {
		return outputDataValues;
	}

	public void setOutputDataValues(double[] outputDataValues) {
		this.outputDataValues = outputDataValues;
	}

	public double[] getOutputModelResult() {
		return outputModelResult;
	}

	public void setOutputModelResult(double[] outputModelResult) {
		this.outputModelResult = outputModelResult;
	}

	public String getMvValues() {
		return mvValues;
	}

	public void setMvValues(String mvValues) {
		this.mvValues = mvValues;
	}

	public PsoCalculationFunction getPsoCalculationFunction() {
		return psoCalculationFunction;
	}

	public void setPsoCalculationFunction(PsoCalculationFunction psoCalculationFunction) {
		this.psoCalculationFunction = psoCalculationFunction;
	}

	public _NNTrainDataConfEntity getOutputTagInfoEntity() {
		return outputTagInfoEntity;
	}

	public void setOutputTagInfoEntity(_NNTrainDataConfEntity outputTagInfoEntity) {
		this.outputTagInfoEntity = outputTagInfoEntity;
	}
}

