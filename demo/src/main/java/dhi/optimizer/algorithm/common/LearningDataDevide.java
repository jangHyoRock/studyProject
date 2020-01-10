package dhi.optimizer.algorithm.common;

import java.io.Serializable;

/**
 * LearningDataDevide
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public class LearningDataDevide implements Serializable { 
	private static final long serialVersionUID = -4861978429390652821L;
	private double[][] trainInput = null;
	private double[][] trainTarget = null;
	private double[][] validInput = null;
	private double[][] validTarget = null;
		
	public double[][] getTrainInput() {
		return trainInput;
	}
	public void setTrainInput(double[][] trainInput) {
		this.trainInput = trainInput.clone();
	}
	public double[][] getTrainTarget() {
		return trainTarget;
	}
	public void setTrainTarget(double[][] trainTarget) {
		this.trainTarget = trainTarget.clone();
	}
	public double[][] getValidInput() {
		return validInput;
	}
	public void setValidInput(double[][] validInput) {
		this.validInput = validInput.clone();
	}
	public double[][] getValidTarget() {
		return validTarget;
	}
	public void setValidTarget(double[][] validTarget) {
		this.validTarget = validTarget.clone();
	}		
	
	public LearningDataDevide dataDevide(double[][] input, double[][] target,
			double trainDataRatio) {

		int dataCount = input.length;
		int validationDataCount = (int) (dataCount * (1.0 - (trainDataRatio / 100.0)));
		int dataRows = input.length;
		int inputCols = input[0].length;
		int targetCols = target[0].length;
		int trainRowId = 0;
		int validRowId = 0;
		boolean[] validationSelected = new boolean[dataCount];
		double[][] trainInput = null;
		double[][] trainTarget = null;
		double[][] validInput = null;
		double[][] validTarget = null;
		LearningDataDevide devidedData = new LearningDataDevide();

		ThreadSafeRandom rand = new ThreadSafeRandom();

		for (int i = 0; i < validationDataCount; i++) {
			int findId = 0;
			boolean findNew = false;
			while (!findNew) {
				findId = rand.nextInt(0, (dataCount - 1));
				findNew = !validationSelected[findId];
			}

			validationSelected[findId] = true;
		}

		trainInput = new double[dataRows - validationDataCount][inputCols];
		trainTarget = new double[dataRows - validationDataCount][targetCols];
		validInput = new double[validationDataCount][inputCols];
		validTarget = new double[validationDataCount][targetCols];

		for (int i = 0; i < dataRows; i++) {
			if (!validationSelected[i]) {
				for (int j = 0; j < inputCols; j++) {
					trainInput[trainRowId][j] = input[i][j];
				}
				for (int j = 0; j < targetCols; j++) {
					trainTarget[trainRowId][j] = target[i][j];
				}
				trainRowId++;
			} else {
				for (int j = 0; j < inputCols; j++) {
					validInput[validRowId][j] = input[i][j];
				}
				for (int j = 0; j < targetCols; j++) {
					validTarget[validRowId][j] = target[i][j];
				}
				validRowId++;
			}
		}

		devidedData.setTrainInput(trainInput);
		devidedData.setTrainTarget(trainTarget);
		devidedData.setValidInput(validInput);
		devidedData.setValidTarget(validTarget);

		return devidedData;
	}
}
