package dhi.optimizer.algorithm.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * ObjectiveFunction
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public class ObjectiveFunction { 

	static double minimumZero = 1.0 / 100000.0;
	static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss",
			Locale.KOREA);

	static public double[] sumError(double[][] target, double[][] output) {
		int rows = target.length;
		int cols = target[0].length;
		double[] error = new double[cols + 1];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (i == 0)
					error[j] = 0.0;
				error[j] += Math.abs(target[i][j] - output[i][j]);
			}
		}

		error[cols] = 0.0;
		for (int j = 0; j < cols; j++) {
			error[cols] += error[j];
		}

		return error;
	}

	static public double[] sumSquaredError(double[][] target, double[][] output) {
		int rows = target.length;
		int cols = target[0].length;
		double[] error = new double[cols + 1];

		error[cols] = 0.0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (i == 0)
					error[j] = 0.0;
				error[j] += Math.pow(target[i][j] - output[i][j], 2);
			}
		}

		error[cols] = 0.0;
		for (int j = 0; j < cols; j++) {
			error[cols] += error[j];
		}

		return error;
	}

	static public double[] meanSquaredError(double[][] target, double[][] output) {
		int rows = target.length;
		int cols = target[0].length;
		double[] error = sumSquaredError(target, output);

		if (rows > 0) {
			for (int j = 0; j < cols; j++) {
				error[j] /= rows;
			}
			error[cols] /= (rows * cols);
		}

		return error;
	}

	static public double[] rootMeanSquaredError(double[] mse) {
		int cols = mse.length;
		double[] error = new double[cols];

		for (int j = 0; j < cols; j++) {
			error[j] = Math.sqrt(mse[j]);
		}

		return error;
	}

	static public double[] rootMeanSquaredError(double[][] target,
			double[][] output) {
		double[] mse = meanSquaredError(target, output);
		double[] error = rootMeanSquaredError(mse);

		return error;
	}

	static public double[] sumSquaredData(double[][] data) {
		int rows = data.length;
		int cols = data[0].length;
		double[] sd = new double[cols + 1];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (i == 0)
					sd[j] = 0.0;
				sd[j] += Math.pow(data[i][j], 2);
			}
		}

		sd[cols] = 0.0;
		for (int j = 0; j < cols; j++) {
			sd[cols] += sd[j];
		}

		return sd;
	}

	static public double[] meanSquaredData(double[][] data) {
		int rows = data.length;
		int cols = data[0].length;
		double[] msd = sumSquaredData(data);

		if (rows > 0) {
			for (int j = 0; j < cols; j++) {
				msd[j] /= rows;
			}
			msd[cols] /= (rows * cols);
		}

		return msd;
	}

	static public double[] rootMeanSquaredData(double[] msd) {
		int cols = msd.length;
		double[] rmsd = new double[cols];

		for (int j = 0; j < cols; j++) {
			rmsd[j] = Math.sqrt(msd[j]);
		}

		return rmsd;
	}

	static public double[] rootMeanSquaredData(double[][] data) {
		double[] msd = meanSquaredData(data);
		double[] rmsd = rootMeanSquaredData(msd);

		return rmsd;
	}

	static public double[] errorRate(double[] rmse, double[] rmsd) {
		int cols = rmse.length;
		double[] errorRate = new double[cols];

		for (int j = 0; j < cols; j++) {
			double data = rmsd[j] != 0 ? rmsd[j]
					: ObjectiveFunction.minimumZero;
			errorRate[j] = rmse[j] / data * 100.0;
		}

		return errorRate;
	}

	static public double[][] getErrors(double[][] target, double[][] output) {
		int rows = target.length;
		int cols = target[0].length;
		double[][] errors = new double[rows][cols + 1];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				errors[i][j] = target[i][j] - output[i][j];
				errors[i][cols] += errors[i][j];
			}
		}

		return errors;
	}

	static public double[][] getAbsErrors(double[][] target, double[][] output) {
		int rows = target.length;
		int cols = target[0].length;
		double[][] errors = new double[rows][cols + 1];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				errors[i][j] = Math.abs(target[i][j] - output[i][j]);
				errors[i][cols] += errors[i][j];
			}
		}

		return errors;
	}

	static public LearningInfo getErrorInfo(double[][] target, double[][] output) {
		LearningInfo errorInfo = new LearningInfo();

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
					errorIn1Percent[j] = in1PercentCount[j] / (double) rows
							* 100.0;
					errorIn2Percent[j] = in2PercentCount[j] / (double) rows
							* 100.0;
					errorIn5Percent[j] = in5PercentCount[j] / (double) rows
							* 100.0;
					errorIn10Percent[j] = in10PercentCount[j] / (double) rows
							* 100.0;

					dataMins[cols] = (dataMins[cols] < dataMins[j]) ? dataMins[cols]
							: dataMins[j];
					dataMaxs[cols] = (dataMaxs[cols] > dataMaxs[j]) ? dataMaxs[cols]
							: dataMaxs[j];
					dataRanges[cols] = dataMaxs[cols] - dataMins[cols];
					errorMins[cols] = (errorMins[cols] < errorMins[j]) ? errorMins[cols]
							: errorMins[j];
					errorMaxs[cols] = (errorMaxs[cols] > errorMaxs[j]) ? errorMaxs[cols]
							: errorMaxs[j];

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

				errorIn1Percent[cols] = in1PercentCount[cols]
						/ (double) (rows * cols) * 100.0;
				errorIn2Percent[cols] = in2PercentCount[cols]
						/ (double) (rows * cols) * 100.0;
				errorIn5Percent[cols] = in5PercentCount[cols]
						/ (double) (rows * cols) * 100.0;
				errorIn10Percent[cols] = in10PercentCount[cols]
						/ (double) (rows * cols) * 100.0;

				mses[cols] = squaredErrors[cols] / (double) (rows * cols);
				rmses[cols] = Math.sqrt(mses[cols]);

				msds[cols] = squaredDatas[cols] / (double) (rows * cols);
				rmsds[cols] = Math.sqrt(msds[cols]);

				double data2 = rmsds[cols] != 0 ? rmsds[cols] : minimumZero;
				errorRates[cols] = rmses[cols] / data2 * 100.0;
			}

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
		return errorInfo;
	}

	static public boolean isStopCondition(LearningInfo current, LearningInfo target) {
		boolean stop = false;
		boolean isIterationExpired = false;
		boolean isTimeExpired = false;
		boolean isValidationFailed = false;
		boolean isMseOk = false;
		boolean isRmseOk = false;
		boolean isMuOk = false;
		int curIterations = current.getIterations();
		int targetIterations = target.getIterations();
		Calendar curTrainTime = current.getElapsedTrainTime();
		Calendar trainTime = target.getElapsedTrainTime();

		curTrainTime.set(Calendar.ERA, 0);
		curTrainTime.set(Calendar.WEEK_OF_YEAR, 0);
		curTrainTime.set(Calendar.WEEK_OF_MONTH, 0);
		curTrainTime.set(Calendar.DAY_OF_MONTH, 0);
		curTrainTime.set(Calendar.DAY_OF_YEAR, 0);
		curTrainTime.set(Calendar.DAY_OF_WEEK, 0);
		curTrainTime.set(Calendar.DAY_OF_WEEK_IN_MONTH, 0);
		curTrainTime.set(Calendar.AM_PM, 0);
		curTrainTime.set(Calendar.AM_PM, 0);
		curTrainTime.set(Calendar.YEAR, 0);
		curTrainTime.set(Calendar.MONTH, 0);
		curTrainTime.set(Calendar.DATE, 0);
		// curTrainTime.set(Calendar.MILLISECOND, 0);

		trainTime.set(Calendar.ERA, 0);
		trainTime.set(Calendar.WEEK_OF_YEAR, 0);
		trainTime.set(Calendar.WEEK_OF_MONTH, 0);
		trainTime.set(Calendar.DAY_OF_MONTH, 0);
		trainTime.set(Calendar.DAY_OF_YEAR, 0);
		trainTime.set(Calendar.DAY_OF_WEEK, 0);
		trainTime.set(Calendar.DAY_OF_WEEK_IN_MONTH, 0);
		trainTime.set(Calendar.AM_PM, 0);
		trainTime.set(Calendar.AM_PM, 0);
		trainTime.set(Calendar.YEAR, 0);
		trainTime.set(Calendar.MONTH, 0);
		trainTime.set(Calendar.DATE, 0);
		// trainTime.set(Calendar.MILLISECOND, 0);

		int curValidationFailedCount = current.getValidationFailedCount();
		int targetValidationFailedCount = target.getValidationFailedCount();
		// long curTicks = current.getElapsedTrainTime().getTime();
		// long targetTicks = target.getElapsedTrainTime().getTime();
		double curMse = current.getMse();
		double targetMse = target.getMse();
		double curRmse = current.getRmse();
		double targetRmse = target.getRmse();		
		double curMu = current.getMu();
		double targetMu = target.getMu();

		// stop = ((targetIterations > 0) && (curIterations >=
		// targetIterations))
		// || ((targetTicks > 0) && (curTicks >= targetTicks))
		// || ((targetValidationFailedCount > 0) && (curValidationFailedCount >=
		// targetValidationFailedCount))
		// || ((targetMse > 0) && (curMse <= targetMse))
		// || ((targetMu > 0) && (curMu <= targetMu));

		// System.out.println(curTrainTime.get(12) + " " + curTrainTime.get(13)
		// + " " + curTrainTime.get(14) + " " + curTrainTime.get(15) + " "
		// + curTrainTime.get(16) );

		if ((trainTime.get(11) == 0) && (trainTime.get(12) == 0)
				&& (trainTime.get(13) == 0) && (trainTime.get(14) == 0)) {
			isTimeExpired = false;
		} else {
			isTimeExpired = curTrainTime.after(trainTime);
		}

		isIterationExpired = (targetIterations > 0)
				&& (curIterations >= targetIterations);
		isMseOk = (targetMse > 0) && (curMse <= targetMse);
		isRmseOk = (targetRmse > 0) && (curRmse <= targetRmse);		
		isMuOk = (targetMu > 0) && (curMu <= targetMu);
		isValidationFailed = (targetValidationFailedCount > 0)
				&& (curValidationFailedCount >= targetValidationFailedCount);

		stop = isIterationExpired || isTimeExpired || isMseOk || isRmseOk || isMuOk	|| isValidationFailed;

		// System.out.println("@@@@@@@@@@@@@@@@@" + curTrainTime
		// + "###################" + trainTime);

		return stop;
	}
}
