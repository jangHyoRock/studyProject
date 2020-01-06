package dhi.optimizer.model;

import dhi.common.util.Utilities;

/**
 * Algorithm Status VO Class <br>
 * : Algorithm Status 화면에서 사용하는  Model VO.
 */
public class AlgorithmStatusInfoVO {

	private String trainProcessStartTime;
	
	private String lastNNTrainTime;
	
	private String anyByPassValueOpen;
	
	private String anyOilBurnerFiringOn;
	
	private String runbackActionOn;
	
	private String unitLoadTargetLow;
	
	private String loadChangeDetected;
	
	private String freqCorrectionOn;
	
	private String prevNNModelTime;
	
	private String lastNNModelTime;
	
	private String seedNNModel1Time;
	
	private String seedNNModel2Time;
		
	private double nnModelValidErrorRate;
	
	private String fruitNNModel1Time;
	
	private String fruitNNModel2Time;
	
	private String fruitNNModel3Time;
	
	private String lastPsoRunningTime;
	
	private double lastPsoErrorSum;
	
	private double psoConfAllowValue;
	
	private double lastPsoOptimalFValue;
	
	private double lastPsoOpDataFValue;
	
	private String lastPsoOptimalPositon;
	
	private String lastOutputControllerTime;
	
	private String outputControllerInitialize;
	
	private String outputControllerFullHold;
	
	public AlgorithmStatusInfoVO() {}
	
	public AlgorithmStatusInfoVO(Object[] object) {
		
		this.trainProcessStartTime = Utilities.ObjectToTimeString(object[0], "yyyy/MM/dd HH:mm:ss");
		this.lastNNTrainTime = Utilities.ObjectToTimeString(object[1], "yyyy/MM/dd HH:mm:ss");
		this.anyByPassValueOpen = Utilities.ObjectToIntString(object[2]);
		this.anyOilBurnerFiringOn = Utilities.ObjectToIntString(object[3]);
		this.runbackActionOn = Utilities.ObjectToIntString(object[4]);
		this.unitLoadTargetLow = Utilities.ObjectToIntString(object[5]);
		this.loadChangeDetected = Utilities.ObjectToIntString(object[6]);
		this.freqCorrectionOn = Utilities.ObjectToIntString(object[7]);
		this.prevNNModelTime = Utilities.ObjectToTimeString(object[8], "yyyy/MM/dd HH:mm:ss");
		this.lastNNModelTime = Utilities.ObjectToTimeString(object[9], "yyyy/MM/dd HH:mm:ss");
		this.seedNNModel1Time = Utilities.ObjectToTimeString(object[10], "yyyy/MM/dd HH:mm:ss");
		this.seedNNModel2Time = Utilities.ObjectToTimeString(object[11], "yyyy/MM/dd HH:mm:ss");
		
		this.nnModelValidErrorRate = Utilities.ObjectToDouble(object[12]);
		
		this.fruitNNModel1Time = Utilities.ObjectToTimeString(object[13], "yyyy/MM/dd HH:mm:ss");
		this.fruitNNModel2Time = Utilities.ObjectToTimeString(object[14], "yyyy/MM/dd HH:mm:ss");
		this.fruitNNModel3Time = Utilities.ObjectToTimeString(object[15], "yyyy/MM/dd HH:mm:ss");
		this.lastPsoRunningTime = Utilities.ObjectToTimeString(object[16], "yyyy/MM/dd HH:mm:ss");
		
		this.lastPsoErrorSum = Utilities.ObjectToDouble(object[17]);
		this.psoConfAllowValue = Utilities.ObjectToDouble(object[18]);
		
		this.lastPsoOptimalFValue = Utilities.ObjectToDouble(object[19]);
		this.lastPsoOpDataFValue = Utilities.ObjectToDouble(object[20]);
		this.lastPsoOptimalPositon = Utilities.ObjectToString(object[21]);
		
		this.lastOutputControllerTime = Utilities.ObjectToTimeString(object[22], "yyyy/MM/dd HH:mm:ss");
		this.outputControllerFullHold = Utilities.ObjectToIntString(object[23]);
		this.outputControllerInitialize = Utilities.ObjectToIntString(object[24]);
	}	
	
	public String getTrainProcessStartTime() {
		return trainProcessStartTime;
	}
	
	public String getLastNNTrainTime() {
		return lastNNTrainTime;
	}

	public String getAnyByPassValueOpen() {
		return anyByPassValueOpen;
	}

	public String getAnyOilBurnerFiringOn() {
		return anyOilBurnerFiringOn;
	}

	public String getRunbackActionOn() {
		return runbackActionOn;
	}

	public String getUnitLoadTargetLow() {
		return unitLoadTargetLow;
	}

	public String getLoadChangeDetected() {
		return loadChangeDetected;
	}
	
	public String getFreqCorrectionOn() {
		return freqCorrectionOn;
	}
	
	public String getPrevNNModelTime() {
		return prevNNModelTime;
	}

	public String getLastNNModelTime() {
		return lastNNModelTime;
	}
	
	public String getSeedNNModel1Time() {
		return seedNNModel1Time;
	}
	
	public String getSeedNNModel2Time() {
		return seedNNModel2Time;
	}
	
	public double getNnModelValidErrorRate() {
		return nnModelValidErrorRate;
	}
	
	public String getFruitNNModel1Time() {
		return fruitNNModel1Time;
	}
	
	public String getFruitNNModel2Time() {
		return fruitNNModel2Time;
	}
	
	public String getFruitNNModel3Time() {
		return fruitNNModel3Time;
	}

	public String getLastPsoRunningTime() {
		return lastPsoRunningTime;
	}
	
	public double getLastPsoErrorSum() {
		return lastPsoErrorSum;
	}

	public double getPsoConfAllowValue() {
		return psoConfAllowValue;
	}

	public double getLastPsoOptimalFValue() {
		return lastPsoOptimalFValue;
	}
	
	public double getLastPsoOpDataFValue() {
		return lastPsoOpDataFValue;
	}
	
	public String getLastPsoOptimalPositon() {
		return lastPsoOptimalPositon;
	}
	
	public String getLastOutputControllerTime() {
		return lastOutputControllerTime;
	}	
	
	public String getOutputControllerInitialize() {
		return outputControllerInitialize;
	}

	public String getOutputControllerFullHold() {
		return outputControllerFullHold;
	}
}
