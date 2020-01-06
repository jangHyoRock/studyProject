package dhi.optimizer.algorithm.neuralNet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dhi.common.util.Utilities;
import dhi.optimizer.algorithm.common.LearningDataSet;
import dhi.optimizer.algorithm.common.LearningInfo;
import dhi.optimizer.algorithm.common.ObjectiveFunction;

public class SupervisedLearningProcessor implements SupervisedLearningService {

	private static final Logger logger = LoggerFactory.getLogger(SupervisedLearningProcessor.class);

	private ActivationNetwork network = null;

	private ISupervisedLearning teacher = null;
	private List<Integer> neuronsInLayer;

	private LearningInfo currentTrainInfo = new LearningInfo();
	private LearningInfo targetTrainInfo = new LearningInfo();

	private LearningDataSet trainInputDataSet = new LearningDataSet(LearningDataSet.defaultNormalizeScaleX);
	private LearningDataSet trainTargetDataSet = new LearningDataSet(LearningDataSet.defaultNormalizeScaleY);
	private LearningDataSet trainOutputDataSet = new LearningDataSet(LearningDataSet.defaultNormalizeScaleY);
	private LearningDataSet validInputDataSet = new LearningDataSet(LearningDataSet.defaultNormalizeScaleX);
	private LearningDataSet validTargetDataSet = new LearningDataSet(LearningDataSet.defaultNormalizeScaleY);
	private LearningDataSet validOutputDataSet = new LearningDataSet(LearningDataSet.defaultNormalizeScaleY);
	private LearningDataSet testInputDataSet = new LearningDataSet(LearningDataSet.defaultNormalizeScaleX);
	private LearningDataSet testTargetDataSet = new LearningDataSet(LearningDataSet.defaultNormalizeScaleY);
	private LearningDataSet testOutputDataSet = new LearningDataSet(LearningDataSet.defaultNormalizeScaleY);

	private SupervisedLearnings selectedlearningAlgorithm = SupervisedLearnings.BackPropagation;

	private boolean needToStop = false;
	private double minTrainMseAvg = 0.0f;
	private double minValidMseAvg = 0.0f;
	private String minNNModel;
	private int minIteration = 0;

	private int trainIterationCount;
	private double trainingErrorPercent;
	private double validErrorPercent;	
	private boolean isStopCondition = false;
	
	private String testFileId = "";
		
	public String getTestFileId() {
		return testFileId;
	}

	public void setTestFileId(String testFileId) {
		this.testFileId = testFileId;
	}

	public LearningInfo getTargetTrainInfo() {
		return targetTrainInfo;
	}

	public void setTargetTrainInfo(LearningInfo targetTrainInfo) {
		this.targetTrainInfo = targetTrainInfo;
	}

	public SupervisedLearnings getSelectedlearningAlgorithm() {
		return selectedlearningAlgorithm;
	}

	public void setSelectedlearningAlgorithm(SupervisedLearnings selectedlearningAlgorithm) {
		this.selectedlearningAlgorithm = selectedlearningAlgorithm;
	}

	public int getTrainIterationCount() {
		return trainIterationCount;
	}

	public void setTrainIterationCount(int trainIterationCount) {
		this.trainIterationCount = trainIterationCount;
	}

	public double getTrainingErrorPercent() {
		return trainingErrorPercent;
	}

	public void setTrainingErrorPercent(double trainingErrorPercent) {
		this.trainingErrorPercent = trainingErrorPercent;
	}

	public double getValidErrorPercent() {
		return validErrorPercent;
	}

	public void setValidErrorPercent(double validErrorPercent) {
		this.validErrorPercent = validErrorPercent;
	}	

	public ActivationNetwork getAvailableNetwork() {
		return  (ActivationNetwork)Utilities.deSerialize(this.minNNModel);
	}	

	public double getMinTrainMseAvg() {
		return minTrainMseAvg;
	}

	public void setMinTrainMseAvg(double minTrainMseAvg) {
		this.minTrainMseAvg = minTrainMseAvg;
	}

	public double getMinValidMseAvg() {
		return minValidMseAvg;
	}

	public void setMinValidMseAvg(double minValidMseAvg) {
		this.minValidMseAvg = minValidMseAvg;
	}

	public void intialize(double[][] trainInputData, double[][] trainTargetData, double[][] validInputData, double[][] validTargetData) {
		this.trainInputDataSet.initDataSet(trainInputData);
		this.trainTargetDataSet.initDataSet(trainTargetData);
		this.trainOutputDataSet.initDataSet(this.trainTargetDataSet.getDataItems());

		this.validInputDataSet.initDataSet(validInputData);
		this.validTargetDataSet.initDataSet(validTargetData);
		this.validOutputDataSet.initDataSet(this.validTargetDataSet.getDataItems());

		this.currentTrainInfo.initInfo(trainInputDataSet.getCols(), trainTargetDataSet.getCols());
		this.targetTrainInfo.initInfo(trainInputDataSet.getCols(), trainTargetDataSet.getCols());
		
		this.createNetwork();
	}

	public void execute() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(0, 0, 0, 0, 0, 0);
		
		currentTrainInfo.setElapsedTrainTime(calendar);
		currentTrainInfo.setIterations(0);
		currentTrainInfo.setValidationFailedCount(0);

		// Train loop
		while (!this.needToStop) {

			// run epoch of learning procedure
			teacher.trainEpoch();

			// validation check
			for (int i = 0; i < this.validInputDataSet.getRows(); i++) {
				double[] output = network.calcOutput(this.validInputDataSet.getDataItems()[i]);
				this.validOutputDataSet.getDataItems()[i] = output;
			}

			// calculate error.
			for (int i = 0; i < this.trainInputDataSet.getRows(); i++) {
				double[] output = network.calcOutput(this.trainInputDataSet.getDataItems()[i]);
				this.trainOutputDataSet.getDataItems()[i] = output;
			}

			this.updateCurrentTrainInfo();

			// check if we need to stop
			isStopCondition = ObjectiveFunction.isStopCondition(currentTrainInfo, targetTrainInfo);
			if (isStopCondition || needToStop) {
				break;
			}
		}
	}

	/*
	 * Compare the current NNModel with the previous History NNModel Top 3,
	 * Base NNModel and adopt it as an available NNModel if the 'MSE' is smaller.
	 */
	public boolean verification(double[][] testInputData, double[][] testTargetData, List<ActivationNetwork> networkList) {
		
		// If there is no NNModel to be verified, it is judged to be the first model and used.
		if(networkList.size() <= 0)
			return true;

		this.testInputDataSet.initDataSet(testInputData);
		this.testTargetDataSet.initDataSet(testTargetData);
		this.testOutputDataSet.initDataSet(testTargetDataSet.getDataItems());

		boolean isFirstNNModel = true;
		double newNNModelValidMseAvg = 0.0f;
		boolean isUseNetwork = true;
		for (ActivationNetwork network : networkList) {
			for (int i = 0; i < this.testInputDataSet.getRows(); i++) {
				double[] output = network.calcOutput(this.testInputDataSet.getDataItems()[i]);
				this.testOutputDataSet.getDataItems()[i] = output;
			}

			LearningInfo testErrorInfo = ObjectiveFunction.getErrorInfo(this.testTargetDataSet.getDataItems(), this.testOutputDataSet.getDataItems());
			double validMseAvg = testErrorInfo.getMseItems()[testErrorInfo.getMseItems().length - 1];

			if (isFirstNNModel) {
				newNNModelValidMseAvg = validMseAvg;
				isFirstNNModel = false;
			}

			if (newNNModelValidMseAvg > validMseAvg) {
				isUseNetwork = false;
				break;
			}
		}

		return isUseNetwork;
	}

	/*
	 * Compare the current NNModel with the previous History NNModel Top 3,
	 * The model with the lowest value of 'MSE' is selected from the model list.
	 */
	public Object[] selectAfterVerification(double[][] testInputData, double[][] testTargetData, List<Object[]> networkInfoList) {

		if (networkInfoList.size() <= 0)
			return null;

		this.testInputDataSet.initDataSet(testInputData);
		this.testTargetDataSet.initDataSet(testTargetData);
		this.testOutputDataSet.initDataSet(testTargetDataSet.getDataItems());
		
		double minValidMseAvg = Double.MAX_VALUE;
		Object[] minNetwork = null;
		for (Object[] networkInfo : networkInfoList) {
			ActivationNetwork network = (ActivationNetwork)networkInfo[2];
			for (int i = 0; i < this.testInputDataSet.getRows(); i++) {
				double[] output = network.calcOutput(this.testInputDataSet.getDataItems()[i]);
				this.testOutputDataSet.getDataItems()[i] = output;
			}

			LearningInfo testErrorInfo = ObjectiveFunction.getErrorInfo(this.testTargetDataSet.getDataItems(), this.testOutputDataSet.getDataItems());
			double validMseAvg = testErrorInfo.getMseItems()[testErrorInfo.getMseItems().length - 1];

			if (minValidMseAvg > validMseAvg) {
				minNetwork = networkInfo;
				minValidMseAvg = validMseAvg;
			}
		}

		return minNetwork;
	}
	
	private void createNetwork() {
		
		this.neuronsInLayer = this.targetTrainInfo.getNeuronsInLayer();
		
		// for output layer
		int neuronsCount = trainTargetDataSet.getCols();
		this.neuronsInLayer.add(neuronsCount);
				
		List<IActivationFunction> activationFunctions = new ArrayList<IActivationFunction>();
		int layersCount = this.neuronsInLayer.size();
		double sigmoidAlpha = this.targetTrainInfo.getSigmoidAlphaValue();
		
		// activation function for hidden layer1
		activationFunctions.add(new BipolarSigmoidFunction(sigmoidAlpha));
		// activation function for hidden layer2
		if (layersCount >= 3) {
			activationFunctions.add(new BipolarSigmoidFunction(sigmoidAlpha));
		}
		// activation function output layer
		activationFunctions.add(new BipolarSigmoidFunction(sigmoidAlpha));

		// create multi-layer neural network
		int intputsCount = this.trainInputDataSet.getCols();
		this.network = new ActivationNetwork(intputsCount, this.neuronsInLayer, activationFunctions);

		// create teacher
		this.teacher = createTeacher(this.selectedlearningAlgorithm, this.network);

		// set learning rate and momentum
		this.teacher.setLearningRate(this.targetTrainInfo.getLearningRate());
		this.teacher.setMomentum(this.targetTrainInfo.getMomentum());
		this.teacher.setLearingDataSet(this.trainInputDataSet, this.trainTargetDataSet);
	}
	
	private ISupervisedLearning createTeacher(SupervisedLearnings learningAlogrithm, ActivationNetwork network) {
		ISupervisedLearning trainAlgorithm = null;
		switch (learningAlogrithm) {
		case BackPropagation:
			trainAlgorithm = new BackPropagation(network);
			break;
		case ResilientBackpropagation:
			trainAlgorithm = new ResilientBackpropagation(network);
			break;
		}

		return trainAlgorithm;
	}
	
	private void updateCurrentTrainInfo() {
		
		// Make Training Info
		this.currentTrainInfo.increaseIterations(1);
		this.currentTrainInfo.checkValidation();
		
		LearningInfo learningErrorInfo = ObjectiveFunction.getErrorInfo(this.trainTargetDataSet.getDataItems(), this.trainOutputDataSet.getDataItems());
		LearningInfo validErrorInfo = ObjectiveFunction.getErrorInfo(this.validTargetDataSet.getDataItems(), this.validOutputDataSet.getDataItems());

		double trainingError = learningErrorInfo.getErrorRateItems()[learningErrorInfo.getErrorRateItems().length - 1];
		double validError = validErrorInfo.getErrorRateItems()[validErrorInfo.getMseItems().length - 1];
		double validMseAvg = validErrorInfo.getMseItems()[validErrorInfo.getMseItems().length - 1];
		double trainMseAvg = learningErrorInfo.getMseItems()[learningErrorInfo.getMseItems().length - 1];
		
		if(this.minValidMseAvg == 0.0f) {
			this.minIteration = this.currentTrainInfo.getIterations();
			this.minTrainMseAvg = trainMseAvg;
			this.minValidMseAvg = validMseAvg;
			this.minNNModel = Utilities.serialize(this.network);
		} else {
			if(this.minValidMseAvg >= validMseAvg) {
				this.minIteration = this.currentTrainInfo.getIterations();
				this.minTrainMseAvg = trainMseAvg;
				this.minValidMseAvg = validMseAvg;
				this.minNNModel = Utilities.serialize(this.network);
				
				// testPrintMin(this.currentTrainInfo.getIterations(), trainTargetDataSet.getDataItems(), this.trainOutputDataSet.getDataItems(), "train");
				// testPrintMin(this.currentTrainInfo.getIterations(), this.validTargetDataSet.getDataItems(), this.validOutputDataSet.getDataItems(), "valid");
			}
		}
		
		// testPrintAll(this.currentTrainInfo.getIterations(), trainTargetDataSet.getDataItems(), this.trainOutputDataSet.getDataItems(), "train");
		// testPrintAll(this.currentTrainInfo.getIterations(), this.validTargetDataSet.getDataItems(), this.validOutputDataSet.getDataItems(), "valid");
		
		logger.info("# "+ this.currentTrainInfo.getIterations() + " trainingErrorPercentage = " + trainingError + ", validErrorPercentage = " + validError + ", minValidMseAvg = " + this.minValidMseAvg);
		
		this.currentTrainInfo.setMseItems(learningErrorInfo.getMseItems());
		this.currentTrainInfo.setRmseItems(learningErrorInfo.getRmseItems());
		this.currentTrainInfo.setRmsd(learningErrorInfo.getRmsd());
		this.currentTrainInfo.setErrorRateItems(learningErrorInfo.getErrorRateItems());
		this.currentTrainInfo.setDataMins(learningErrorInfo.getDataMins());
		this.currentTrainInfo.setDataMaxs(learningErrorInfo.getDataMaxs());
		this.currentTrainInfo.setDataRanges(learningErrorInfo.getDataRanges());
		this.currentTrainInfo.setErrorMins(learningErrorInfo.getErrorMins());
		this.currentTrainInfo.setErrorMaxs(learningErrorInfo.getErrorMaxs());
		this.currentTrainInfo.setErrorIn1Percent(learningErrorInfo.getErrorIn1Percent());
		this.currentTrainInfo.setErrorIn2Percent(learningErrorInfo.getErrorIn2Percent());
		this.currentTrainInfo.setErrorIn5Percent(learningErrorInfo.getErrorIn5Percent());
		this.currentTrainInfo.setErrorIn10Percent(learningErrorInfo.getErrorIn10Percent());
		this.currentTrainInfo.setErrors(learningErrorInfo.getErrors());
		
		this.trainingErrorPercent = trainingError;
		this.validErrorPercent = validError;
		
		this.currentTrainInfo.setMu(teacher.getSumOfWeightChanges());
		this.currentTrainInfo.setValidMseItems(validErrorInfo.getMseItems());
		this.trainIterationCount++;
	}
	
	private void testPrintAll(int interation, double[][] target, double[][] output, String type) {
		double minimumZero = 1.0 / 100000.0;
		
		LearningInfo errorInfo = new LearningInfo();
		StringBuilder sb = new StringBuilder();

		if (target == null || output == null) {

		} else {
			int rows = target.length;
			int cols = target[0].length;

			int[] in1PercentCount = new int[cols + 1];
			int[] in2PercentCount = new int[cols + 1];
			int[] in5PercentCount = new int[cols + 1];
			int[] in10PercentCount = new int[cols + 1];

			double[] dataMins = new double[cols + 1];
			double[] dataMaxs = new double[cols + 1];
			double[] dataRanges = new double[cols + 1];
			double[] errorMins = new double[cols + 1];
			double[] errorMaxs = new double[cols + 1];
			double[] errorIn1Percent = new double[cols + 1];
			double[] errorIn2Percent = new double[cols + 1];
			double[] errorIn5Percent = new double[cols + 1];
			double[] errorIn10Percent = new double[cols + 1];

			double[] squaredErrors = new double[cols + 1];
			double[] mses = new double[cols + 1];
			double[] rmses = new double[cols + 1];
			double[] squaredDatas = new double[cols + 1];
			double[] msds = new double[cols + 1];
			double[] rmsds = new double[cols + 1];
			double[] errorRates = new double[cols + 1];
			double[][] errors = new double[rows][cols];

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if (i == 0) {
						in1PercentCount[j] = 0;
						in2PercentCount[j] = 0;
						in5PercentCount[j] = 0;
						in10PercentCount[j] = 0;
						dataMins[j] = Double.MAX_VALUE;
						dataMaxs[j] = -Double.MAX_VALUE;
						errorMins[j] = Double.MAX_VALUE;
						errorMaxs[j] = -Double.MAX_VALUE;
						squaredErrors[j] = 0.0;
						mses[j] = 0.0;
						rmses[j] = 0.0;
						squaredDatas[j] = 0.0;
						msds[j] = 0.0;
						rmsds[j] = 0.0;
						errorRates[j] = 0.0;
					}

					double targetVal = target[i][j];
					double outputVal = output[i][j];
					double absTargetVal = Math.abs(targetVal);
					double error = targetVal - outputVal;
					double absError = Math.abs(error);
					double errorRate = absError
							/ ((absTargetVal != 0.0) ? absTargetVal
									: minimumZero);
					errors[i][j] = error;
					dataMins[j] = (dataMins[j] < targetVal) ? dataMins[j]
							: targetVal;
					dataMaxs[j] = (dataMaxs[j] > targetVal) ? dataMaxs[j]
							: targetVal;
					dataRanges[j] = dataMaxs[j] - dataMins[j];
					errorMins[j] = (errorMins[j] < error) ? errorMins[j]
							: error;
					errorMaxs[j] = (errorMaxs[j] > error) ? errorMaxs[j]
							: error;

					if (errorRate <= 0.01) {
						in1PercentCount[j]++;
						in2PercentCount[j]++;
						in5PercentCount[j]++;
						in10PercentCount[j]++;
					} else if (errorRate <= 0.02) {
						in2PercentCount[j]++;
						in5PercentCount[j]++;
						in10PercentCount[j]++;
					} else if (errorRate <= 0.05) {
						in5PercentCount[j]++;
						in10PercentCount[j]++;
					} else if (errorRate <= 0.10) {
						in10PercentCount[j]++;
					}

					squaredErrors[j] += (error * error);
					squaredDatas[j] += (targetVal * targetVal);
				}
			}

			dataMins[cols] = Double.MAX_VALUE;
			dataMaxs[cols] = -Double.MAX_VALUE;
			dataRanges[cols] = Double.MAX_VALUE;
			errorMins[cols] = Double.MAX_VALUE;
			errorMaxs[cols] = -Double.MAX_VALUE;
			in1PercentCount[cols] = 0;
			in2PercentCount[cols] = 0;
			in5PercentCount[cols] = 0;
			in10PercentCount[cols] = 0;

			squaredErrors[cols] = 0.0;
			mses[cols] = 0.0;
			rmses[cols] = 0.0;
			squaredDatas[cols] = 0.0;
			msds[cols] = 0.0;
			rmsds[cols] = 0.0;
			errorRates[cols] = 0.0;

			if (rows > 0) {
				for (int j = 0; j < cols; j++) {
					errorIn1Percent[j] = in1PercentCount[j] / (double) rows * 100.0;
					errorIn2Percent[j] = in2PercentCount[j] / (double) rows * 100.0;
					errorIn5Percent[j] = in5PercentCount[j] / (double) rows * 100.0;
					errorIn10Percent[j] = in10PercentCount[j] / (double) rows * 100.0;

					dataMins[cols] = (dataMins[cols] < dataMins[j]) ? dataMins[cols] : dataMins[j];
					dataMaxs[cols] = (dataMaxs[cols] > dataMaxs[j]) ? dataMaxs[cols] : dataMaxs[j];
					dataRanges[cols] = dataMaxs[cols] - dataMins[cols];
					errorMins[cols] = (errorMins[cols] < errorMins[j]) ? errorMins[cols] : errorMins[j];
					errorMaxs[cols] = (errorMaxs[cols] > errorMaxs[j]) ? errorMaxs[cols] : errorMaxs[j];

					in1PercentCount[cols] += in1PercentCount[j];
					in2PercentCount[cols] += in2PercentCount[j];
					in5PercentCount[cols] += in5PercentCount[j];
					in10PercentCount[cols] += in10PercentCount[j];

					squaredErrors[cols] += squaredErrors[j];
					mses[j] = squaredErrors[j] / (double) rows;
					rmses[j] = Math.sqrt(mses[j]);

					squaredDatas[cols] += squaredDatas[j];
					msds[j] = squaredDatas[j] / (double) rows;
					rmsds[j] = Math.sqrt(msds[j]);

					double data1 = rmsds[j] != 0 ? rmsds[j] : minimumZero;
					errorRates[j] = rmses[j] / data1 * 100.0;
				}

				errorIn1Percent[cols] = in1PercentCount[cols] / (double) (rows * cols) * 100.0;
				errorIn2Percent[cols] = in2PercentCount[cols] / (double) (rows * cols) * 100.0;
				errorIn5Percent[cols] = in5PercentCount[cols] / (double) (rows * cols) * 100.0;
				errorIn10Percent[cols] = in10PercentCount[cols] / (double) (rows * cols) * 100.0;

				mses[cols] = squaredErrors[cols] / (double) (rows * cols);
				rmses[cols] = Math.sqrt(mses[cols]);

				msds[cols] = squaredDatas[cols] / (double) (rows * cols);
				rmsds[cols] = Math.sqrt(msds[cols]);

				double data2 = rmsds[cols] != 0 ? rmsds[cols] : minimumZero;
				errorRates[cols] = rmses[cols] / data2 * 100.0;
			}
			
			sb.append(interation + "\t");
			for (int j = 0; j < cols; j++) {
				sb.append(mses[j]+ "\t");
			}
			
			sb.append(mses[cols] + "\t");
			sb.append(errorRates[cols] + "\t");
			sb.append(this.minIteration + "\t");
			//sb.append(this.minTrainMseAvg + "\t");
			sb.append(mses[cols] + "\t");
			sb.append(this.minValidMseAvg + "\t");

			sb.append(System.getProperty("line.separator"));
			
			//Utilities.saveFile("d:\\nnmodel_test\\", "nnmodel_"+type+"_All_"+this.getTestFileId()+".txt", sb.toString(), true);
			Utilities.saveFile("logs", "nnmodel_"+type+"_All_"+this.getTestFileId()+".txt", sb.toString(), true);

			errorInfo.setMseItems(mses);
			errorInfo.setRmseItems(rmses);
			errorInfo.setRmsd(rmsds);
			errorInfo.setErrorRateItems(errorRates);
			errorInfo.setDataMins(dataMins);
			errorInfo.setDataMaxs(dataMaxs);
			errorInfo.setDataRanges(dataRanges);
			errorInfo.setErrorMins(errorMins);
			errorInfo.setErrorMaxs(errorMaxs);
			errorInfo.setErrorIn1Percent(errorIn1Percent);
			errorInfo.setErrorIn2Percent(errorIn2Percent);
			errorInfo.setErrorIn5Percent(errorIn5Percent);
			errorInfo.setErrorIn10Percent(errorIn10Percent);
			errorInfo.setErrors(errors);
		}
	}
	
	private void testPrintMin(int interation, double[][] target, double[][] output, String type)
	{
		double minimumZero = 1.0 / 100000.0;
		
		StringBuilder sb = new StringBuilder();
		sb.append("interation : " + interation);
		sb.append(System.getProperty("line.separator"));

		if (target == null || output == null) {

		} else {
			int rows = target.length;
			int cols = target[0].length;

			int[] in1PercentCount = new int[cols + 1];
			int[] in2PercentCount = new int[cols + 1];
			int[] in5PercentCount = new int[cols + 1];
			int[] in10PercentCount = new int[cols + 1];

			double[] dataMins = new double[cols + 1];
			double[] dataMaxs = new double[cols + 1];
			double[] dataRanges = new double[cols + 1];
			double[] errorMins = new double[cols + 1];
			double[] errorMaxs = new double[cols + 1];
			double[] errorIn1Percent = new double[cols + 1];
			double[] errorIn2Percent = new double[cols + 1];
			double[] errorIn5Percent = new double[cols + 1];
			double[] errorIn10Percent = new double[cols + 1];

			double[] squaredErrors = new double[cols + 1];
			double[] mses = new double[cols + 1];
			double[] rmses = new double[cols + 1];
			double[] squaredDatas = new double[cols + 1];
			double[] msds = new double[cols + 1];
			double[] rmsds = new double[cols + 1];
			double[] errorRates = new double[cols + 1];
			double[][] errors = new double[rows][cols];

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if (i == 0) {
						in1PercentCount[j] = 0;
						in2PercentCount[j] = 0;
						in5PercentCount[j] = 0;
						in10PercentCount[j] = 0;
						dataMins[j] = Double.MAX_VALUE;
						dataMaxs[j] = -Double.MAX_VALUE;
						errorMins[j] = Double.MAX_VALUE;
						errorMaxs[j] = -Double.MAX_VALUE;
						squaredErrors[j] = 0.0;
						mses[j] = 0.0;
						rmses[j] = 0.0;
						squaredDatas[j] = 0.0;
						msds[j] = 0.0;
						rmsds[j] = 0.0;
						errorRates[j] = 0.0;
					}

					double targetVal = target[i][j];
					double outputVal = output[i][j];
					double absTargetVal = Math.abs(targetVal);
					double error = targetVal - outputVal;
					double absError = Math.abs(error);
					double errorRate = absError / ((absTargetVal != 0.0) ? absTargetVal : minimumZero);
					errors[i][j] = error;
					dataMins[j] = (dataMins[j] < targetVal) ? dataMins[j] : targetVal;
					dataMaxs[j] = (dataMaxs[j] > targetVal) ? dataMaxs[j] : targetVal;
					dataRanges[j] = dataMaxs[j] - dataMins[j];
					errorMins[j] = (errorMins[j] < error) ? errorMins[j] : error;
					errorMaxs[j] = (errorMaxs[j] > error) ? errorMaxs[j] : error;

					if (errorRate <= 0.01) {
						in1PercentCount[j]++;
						in2PercentCount[j]++;
						in5PercentCount[j]++;
						in10PercentCount[j]++;
					} else if (errorRate <= 0.02) {
						in2PercentCount[j]++;
						in5PercentCount[j]++;
						in10PercentCount[j]++;
					} else if (errorRate <= 0.05) {
						in5PercentCount[j]++;
						in10PercentCount[j]++;
					} else if (errorRate <= 0.10) {
						in10PercentCount[j]++;
					}

					squaredErrors[j] += (error * error);
					squaredDatas[j] += (targetVal * targetVal);
					
					sb.append(String.valueOf(error) + "\t");
				}
				
				sb.append(System.getProperty("line.separator"));
			}

			dataMins[cols] = Double.MAX_VALUE;
			dataMaxs[cols] = -Double.MAX_VALUE;
			dataRanges[cols] = Double.MAX_VALUE;
			errorMins[cols] = Double.MAX_VALUE;
			errorMaxs[cols] = -Double.MAX_VALUE;
			in1PercentCount[cols] = 0;
			in2PercentCount[cols] = 0;
			in5PercentCount[cols] = 0;
			in10PercentCount[cols] = 0;

			squaredErrors[cols] = 0.0;
			mses[cols] = 0.0;
			rmses[cols] = 0.0;
			squaredDatas[cols] = 0.0;
			msds[cols] = 0.0;
			rmsds[cols] = 0.0;
			errorRates[cols] = 0.0;

			if (rows > 0) {
				for (int j = 0; j < cols; j++) {
					errorIn1Percent[j] = in1PercentCount[j] / (double) rows * 100.0;
					errorIn2Percent[j] = in2PercentCount[j] / (double) rows * 100.0;
					errorIn5Percent[j] = in5PercentCount[j] / (double) rows * 100.0;
					errorIn10Percent[j] = in10PercentCount[j] / (double) rows * 100.0;

					dataMins[cols] = (dataMins[cols] < dataMins[j]) ? dataMins[cols] : dataMins[j];
					dataMaxs[cols] = (dataMaxs[cols] > dataMaxs[j]) ? dataMaxs[cols] : dataMaxs[j];
					dataRanges[cols] = dataMaxs[cols] - dataMins[cols];
					errorMins[cols] = (errorMins[cols] < errorMins[j]) ? errorMins[cols] : errorMins[j];
					errorMaxs[cols] = (errorMaxs[cols] > errorMaxs[j]) ? errorMaxs[cols] : errorMaxs[j];

					in1PercentCount[cols] += in1PercentCount[j];
					in2PercentCount[cols] += in2PercentCount[j];
					in5PercentCount[cols] += in5PercentCount[j];
					in10PercentCount[cols] += in10PercentCount[j];

					squaredErrors[cols] += squaredErrors[j];
					mses[j] = squaredErrors[j] / (double) rows;
					rmses[j] = Math.sqrt(mses[j]);

					squaredDatas[cols] += squaredDatas[j];
					msds[j] = squaredDatas[j] / (double) rows;
					rmsds[j] = Math.sqrt(msds[j]);

					double data1 = rmsds[j] != 0 ? rmsds[j] : minimumZero;
					errorRates[j] = rmses[j] / data1 * 100.0;
				}

				errorIn1Percent[cols] = in1PercentCount[cols] / (double) (rows * cols) * 100.0;
				errorIn2Percent[cols] = in2PercentCount[cols] / (double) (rows * cols) * 100.0;
				errorIn5Percent[cols] = in5PercentCount[cols] / (double) (rows * cols) * 100.0;
				errorIn10Percent[cols] = in10PercentCount[cols] / (double) (rows * cols) * 100.0;

				mses[cols] = squaredErrors[cols] / (double) (rows * cols);
				rmses[cols] = Math.sqrt(mses[cols]);

				msds[cols] = squaredDatas[cols] / (double) (rows * cols);
				rmsds[cols] = Math.sqrt(msds[cols]);

				double data2 = rmsds[cols] != 0 ? rmsds[cols] : minimumZero;
				errorRates[cols] = rmses[cols] / data2 * 100.0;
			}

			for (int j = 0; j < cols; j++) {
				sb.append(mses[j]+ "\t");
			}
			
			sb.append(mses[cols]);
			
			// Utilities.saveFile("d:\\nnmodel_test\\", "nnmodel_"+type+"_min_"+this.getTestFileId()+".txt", sb.toString(), false);
			Utilities.saveFile("logs", "nnmodel_"+type+"_min_"+this.getTestFileId()+".txt", sb.toString(), false);

		}
	}
}
