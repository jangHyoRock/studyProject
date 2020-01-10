package dhi.optimizer.algorithm.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * LearningInfo
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public class LearningInfo implements Serializable {
	private static final long serialVersionUID = -7045107856585545061L;
	private int inputCount;
	private int targetCount;
	private int validationFailedCount;
	private int iterations;
	private double learningRate;
	private double momentum; 
	private double sigmoidAlphaValue;
	private double mu;
	private double mse;
	private double rmse;
	private double validMse;
	private double validRmse;
	private double oldValidMse;
	private double trainDataRatio;
	private double[] rmsd;
	private double[] mseItems;
	private double[] validMseItems;
	private double[] validRmseItems;
	private double[] rmseItems;
	private double[] errorRateItems;
	private double[] dataMins;
	private double[] dataMaxs;
	private double[] dataRanges;
	private double[] errorMins;
	private double[] errorMaxs;
	private double[] errorIn1Percent;
	private double[] errorIn2Percent;
	private double[] errorIn5Percent;
	private double[] errorIn10Percent;
	private double[][] errors;
	private List<Integer> neuronsInLayer = new ArrayList<Integer>();
	private List<double[]> mseItemsOfIterations = new ArrayList<double[]>();
	private List<double[]> rmseItemsOfIterations = new ArrayList<double[]>();
	private List<double[]> validMseItemsOfIterations = new ArrayList<double[]>();	
	private List<double[]> validRmseItemsOfIterations = new ArrayList<double[]>();	
	private Calendar elapsedTrainTime;

	/**
	 * Initializes a new instance of the class
	 * 
	 * @param isTarget
	 *            instance for target Information
	 */
	public LearningInfo() {
		learningRate = 0.01;
		momentum = 0.0;
		sigmoidAlphaValue = 0.5;
		trainDataRatio = 100;
		mseItems = null;
		rmseItems = null;
		rmsd = null;
		errorRateItems = null;
		mse = 0.0;
		validMse = Float.MAX_VALUE;
		oldValidMse = Float.MAX_VALUE;
		rmse = 0.0;
		mu = 0.0;
		neuronsInLayer.add(12); // 1st Hidden Layer
		neuronsInLayer.add(12); // 2nd Hidden Layer
		iterations = 0;
		validationFailedCount = 0;
		elapsedTrainTime = Calendar.getInstance ( );
		elapsedTrainTime.clear();
		elapsedTrainTime.set(0, 0, 0, 0, 0, 0);	
	}

	public int getInputCount() {
		return inputCount;
	}

	public void setInputCount(int inputCount) {
		this.inputCount = inputCount;
	}

	public int getTargetCount() {
		return targetCount;
	}

	public void setTargetCount(int targetCount) {
		this.targetCount = targetCount;
	}

	public int getValidationFailedCount() {
		return validationFailedCount;
	}

	public void setValidationFailedCount(int validationFailedCount) {
		this.validationFailedCount = validationFailedCount;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

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

	public double getMu() {
		return mu;
	}

	public void setMu(double mu) {
		this.mu = mu;
	}

	public double getMse() {
		return mse;
	}

	public void setMse(double mse) {
		this.mse = mse;
	}

	public double getRmse() {
		return rmse;
	}

	public void setRmse(double rmse) {
		this.rmse = rmse;
	}

	public double getValidMse() {
		return validMse;
	}

	public void setValidMse(double validMse) {
		this.validMse = validMse;
	}

	public double getTrainDataRatio() {
		return trainDataRatio;
	}

	public void setTrainDataRatio(double trainDataRatio) {
		this.trainDataRatio = trainDataRatio;
	}

	public double[] getRmsd() {
		return rmsd;
	}

	public void setRmsd(double[] rmsd) {
		this.rmsd = rmsd;
	}

	public double[] getMseItems() {
		return mseItems;
	}

	public void setMseItems(double[] mseItems) {
		this.mseItems = mseItems;
		this.mse = mseItems[targetCount];
	}

	public double[] getValidMseItems() {
		return validMseItems;
	}

	public void setValidMseItems(double[] validMseItems) {
		this.validMseItems = validMseItems;
		this.validMse = validMseItems[targetCount];
	}

	public double getValidationDataRatio() {
		double tmp = 100 - trainDataRatio;
		return tmp;
	}

	public double[] getRmseItems() {
		return rmseItems;
	}

	public void setRmseItems(double[] rmseItems) {
		this.rmseItems = rmseItems;
		this.rmse = rmseItems[targetCount];
	}

	public double[] getErrorRateItems() {
		return errorRateItems;
	}

	public void setErrorRateItems(double[] errorRateItems) {
		this.errorRateItems = errorRateItems;
	}

	public List<Integer> getNeuronsInLayer() {
		return neuronsInLayer;
	}

	public void setNeuronsInLayer(List<Integer> neuronsInLayer) {
		this.neuronsInLayer = neuronsInLayer;
	}

	public Calendar getElapsedTrainTime() {
		return elapsedTrainTime;
	}

	public void setElapsedTrainTime(Calendar calendar) {
		this.elapsedTrainTime = calendar;
	}

	public double getOldValidMse() {
		return oldValidMse;
	}

	public void setOldValidMse(double oldValidMse) {
		this.oldValidMse = oldValidMse;
	}

	public void initInfo(int inputs, int outputs) {
		inputCount = inputs;
		targetCount = outputs;
		mseItems = new double[targetCount];
		rmseItems = new double[targetCount];
		errorRateItems = new double[targetCount];
		rmsd = new double[targetCount];
		mseItemsOfIterations.clear();
		rmseItemsOfIterations.clear();
		validMseItemsOfIterations.clear();
		validRmseItemsOfIterations.clear();		
	}

	public void checkValidation() {
		if (validMse > oldValidMse) {
			validationFailedCount++;
		} else {
			validationFailedCount = 0;
		}

		oldValidMse = validMse;
	}

	public void increaseIterations(int iterations) {
		this.iterations += iterations;
	}
	
	public double getValidRmse() {
		return validRmse;
	}

	public void setValidRmse(double validRmse) {
		this.validRmse = validRmse;
	}	

	public double[] getValidRmseItems() {
		return validRmseItems;
	}

	public void setValidRmseItems(double[] validRmseItems) {
		this.validRmseItems = validRmseItems;
		validRmse = validRmseItems[targetCount];
	}	
	
	
	public List<double[]> getMseItemsOfIterations() {
		return mseItemsOfIterations;
	}

	public void setMseItemsOfIterations(List<double[]> mseItemsOfIterations) {
		this.mseItemsOfIterations = mseItemsOfIterations;
	}

	public List<double[]> getRmseItemsOfIterations() {
		return rmseItemsOfIterations;
	}

	public void setRmseItemsOfIterations(List<double[]> rmseItemsOfIterations) {
		this.rmseItemsOfIterations = rmseItemsOfIterations;
	}

	public List<double[]> getValidMseItemsOfIterations() {
		return validMseItemsOfIterations;
	}

	public void setValidMseItemsOfIterations(
			List<double[]> validMseItemsOfIterations) {
		this.validMseItemsOfIterations = validMseItemsOfIterations;
	}
	public List<double[]> getValidRmseItemsOfIterations() {
		return validRmseItemsOfIterations;
	}

	public void setValidRmseItemsOfIterations(
			List<double[]> validRmseItemsOfIterations) {
		this.validRmseItemsOfIterations = validRmseItemsOfIterations;
	}	
	
	public double[] getDataMins() {
		return dataMins;
	}

	public void setDataMins(double[] dataMins) {
		this.dataMins = dataMins.clone();
	}

	public double[] getDataMaxs() {
		return dataMaxs;
	}

	public void setDataMaxs(double[] dataMaxs) {
		this.dataMaxs = dataMaxs.clone();
	}
	
	public double[] getDataRanges() {
		return dataRanges;
	}

	public void setDataRanges(double[] dataRanges) {
		this.dataRanges = dataRanges.clone();
	}

	public double[] getErrorMins() {
		return errorMins;
	}

	public void setErrorMins(double[] errorMins) {
		this.errorMins = errorMins.clone();
	}

	public double[] getErrorMaxs() {
		return errorMaxs;
	}

	public void setErrorMaxs(double[] errorMaxs) {
		this.errorMaxs = errorMaxs.clone();
	}

	public double[] getErrorIn1Percent() {
		return errorIn1Percent;
	}

	public void setErrorIn1Percent(double[] errorIn1Percent) {
		this.errorIn1Percent = errorIn1Percent.clone();
	}

	public double[] getErrorIn2Percent() {
		return errorIn2Percent;
	}

	public void setErrorIn2Percent(double[] errorIn2Percent) {
		this.errorIn2Percent = errorIn2Percent.clone();
	}

	public double[] getErrorIn5Percent() {
		return errorIn5Percent;
	}

	public void setErrorIn5Percent(double[] errorIn5Percent) {
		this.errorIn5Percent = errorIn5Percent.clone();
	}

	public double[] getErrorIn10Percent() {
		return errorIn10Percent;
	}

	public void setErrorIn10Percent(double[] errorIn10Percent) {
		this.errorIn10Percent = errorIn10Percent.clone();
	}	
	
	public double[][] getErrors() {
		return errors;
	}

	public void setErrors(double[][] errors) {
		this.errors = errors;
	}
}
