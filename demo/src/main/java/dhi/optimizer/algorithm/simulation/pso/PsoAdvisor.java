package dhi.optimizer.algorithm.simulation.pso;

import java.util.ArrayList;
import java.util.List;

import dhi.common.util.Utilities;
import dhi.optimizer.algorithm.common.XThreadPool;
import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;

public class PsoAdvisor {
	
	private int threadCount = (int)(Runtime.getRuntime().availableProcessors() * 0.9);

	// PSO configuration Info.
	private int particleSize = 0;
	private double inertia = 0.3;
	private double correctionFactor = 1.2;

	// PSO NNTrain Info.
	private double[] inputData = null;
	private String model = null;

	List<PsoSwarm> psoSwarmList;
	private List<PsoMV> mvList;
	
	private double globalBestValue = 0.0;
	private double[] globalBestOutput = null;
	
	private int[] globalBestIndex = new int[] {0, 0};

	private List<PsoMV> psoMvStatisticsList;
	private PsoCalculationFunction calculationFunction;

	public PsoAdvisor(int particleSize, double inertia, double correctionFactor, PsoCalculationFunction calculationFunction) {
		this.particleSize = particleSize;
		this.inertia = inertia;
		this.correctionFactor = correctionFactor;
		this.calculationFunction = calculationFunction;
	}
	
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	
	public void setInertia(double inertia) {
		this.inertia = inertia;
	}
	
	public void setCorrectionFactor(double correctionFactor) {
		this.correctionFactor = correctionFactor;
	}
	
	public double[] getInputData() {
		return inputData;
	}

	public void setInputData(double[] inputData) {
		this.inputData = inputData;
	}	

	public void setModel(String model) {
		this.model = model;
	}
	
	public List<PsoSwarm> getPsoSwarmList() {
		return psoSwarmList;
	}

	public List<PsoMV> getMvList() {
		return this.mvList;
	}
	
	public void setMvList(List<PsoMV> mvList) {
		this.mvList = mvList;
	}

	public double getGlobalBestValue() {
		return globalBestValue;
	}

	public void setGlobalBestValue(double globalBestValue) {
		this.globalBestValue = globalBestValue;
	}
	
	public double[] getGlobalBestOutput() {
		return globalBestOutput;
	}
	
	public int[] getGlobalBestIndex() {
		return this.globalBestIndex;
	}	

	public List<PsoMV> getPsoMvStatisticsList() {
		return psoMvStatisticsList;
	}

	public void initalize() {
		long totalSwarmSize = (long)Math.pow((double)this.particleSize, (double)this.mvList.size());
		totalSwarmSize++; // Increment to add '0' to the last line.
		
		int swarmSize  = (int)(totalSwarmSize / this.threadCount);
		XThreadPool xThreadPool = new XThreadPool(this.threadCount);
		
		this.psoSwarmList = new ArrayList<PsoSwarm>();
		boolean isAddZero = false;
		for (int i = 0; i < threadCount; i++) {

			long startIdx = i * swarmSize;
			List<PsoMV> newPsoMVList = new ArrayList<PsoMV>();
			if (threadCount - 1 == i) {
				swarmSize = (int)(totalSwarmSize- startIdx);
				isAddZero = true;
			}

			for (PsoMV mv : this.mvList) {
				PsoMV newPsoMV = new PsoMV(mv.getName(), mv.getMvType(), mv.getMin(), mv.getMax());
				newPsoMV.setInputTagList(mv.getInputTagList());
				newPsoMV.setValues(new double[swarmSize]);
				newPsoMV.setVelocity(new double[swarmSize]);
				newPsoMV.setBestMV(new double[swarmSize]);
				newPsoMV.makeSpreadDatas(this.particleSize);
				newPsoMVList.add(newPsoMV);
			}

			PsoSwarm psoSwarm = new PsoSwarm(this.particleSize, swarmSize, newPsoMVList, isAddZero);
			psoSwarm.setSwarmBestValue(Double.MAX_VALUE);
			this.psoSwarmList.add(psoSwarm);

			PsoInitalize runnable = new PsoInitalize(psoSwarm, (int)startIdx);
			xThreadPool.submit(runnable);
		}

		boolean isComplete = xThreadPool.waitForComplete();
		if(!isComplete)
			throw new RuntimeException(xThreadPool.getErrorMessage());

		this.globalBestValue = 0.0;
		this.globalBestIndex = new int[] { 0, 0 };
	}
	
	public void initalizeRandom() {
		long totalSwarmSize = (long)this.particleSize;
		totalSwarmSize++; // Increment to add '0' to the last line.
		
		int swarmSize  = (int)(totalSwarmSize / this.threadCount);
		XThreadPool xThreadPool = new XThreadPool(this.threadCount);
		
		this.psoSwarmList = new ArrayList<PsoSwarm>();
		boolean isAddZero = false;
		for (int i = 0; i < threadCount; i++) {

			long startIdx = i * swarmSize;
			List<PsoMV> newPsoMVList = new ArrayList<PsoMV>();
			if (threadCount - 1 == i) {
				swarmSize = (int)(totalSwarmSize- startIdx);
				isAddZero = true;
			}

			for (PsoMV mv : this.mvList) {
				PsoMV newPsoMV = new PsoMV(mv.getName(), mv.getMvType(), mv.getMin(), mv.getMax());
				newPsoMV.setInputTagList(mv.getInputTagList());
				newPsoMV.setValues(new double[swarmSize]);
				newPsoMV.setVelocity(new double[swarmSize]);
				newPsoMV.setBestMV(new double[swarmSize]);
				newPsoMV.makeSpreadDatas(this.particleSize);
				newPsoMVList.add(newPsoMV);
			}

			PsoSwarm psoSwarm = new PsoSwarm(this.particleSize, swarmSize, newPsoMVList, isAddZero);
			psoSwarm.setSwarmBestValue(Double.MAX_VALUE);
			this.psoSwarmList.add(psoSwarm);

			PsoInitalizeRandom runnable = new PsoInitalizeRandom(psoSwarm, (int)startIdx);
			xThreadPool.submit(runnable);
		}

		boolean isComplete = xThreadPool.waitForComplete();
		if(!isComplete)
			throw new RuntimeException(xThreadPool.getErrorMessage());

		this.globalBestValue = 0.0;
		this.globalBestIndex = new int[] { 0, 0 };
	}
	
	public void iteration(int i, int iterations) {
		updatePosition(i);
		
		// TODO : 로그 분석 용 소스 
		//if(i == 0 || i == 1 || i == iterations - 1)
		//if(i >= 0)
		//	psoDistribution();
		
		updateGlobalBest();
		updateVelocityVector();
	}

	private void updatePosition(int iterations) {
		XThreadPool xThreadPool = new XThreadPool(this.psoSwarmList.size());
		for (PsoSwarm psoSwarm : this.psoSwarmList) {
			ActivationNetwork model = (ActivationNetwork)Utilities.deSerialize(this.model);
			Runnable runnable = new PsoUpdatePosition(psoSwarm, model, this.inputData, this.calculationFunction);
			xThreadPool.submit(runnable);
		}
		
		boolean isComplete = xThreadPool.waitForComplete();
		if(!isComplete)
			throw new RuntimeException(xThreadPool.getErrorMessage());
	}
	
	private void psoDistribution() {
		this.psoMvStatisticsList = new ArrayList<PsoMV>();

		for (PsoMV mv : this.mvList) {
			PsoMV psoMvStatistics = new PsoMV();
			psoMvStatistics.setName(mv.getName());

			List<PsoMVDistribution> psoMVDistributionList = new ArrayList<PsoMVDistribution>();
			int intervel = 1;
			double stepValue = mv.getMin();
			do {
				PsoMVDistribution psoMVDistribution = new PsoMVDistribution();
				psoMVDistribution.setRangeMin(stepValue);
				stepValue += intervel;
				if (mv.getMax() <= stepValue)
					psoMVDistribution.setRangeMax(mv.getMax() + 0.1);
				else
					psoMVDistribution.setRangeMax(stepValue);
				
				psoMVDistributionList.add(psoMVDistribution);

			} while (mv.getMax() > stepValue);

			for (PsoSwarm psoSwarm : this.psoSwarmList) {
				for (PsoMV swarmMV : psoSwarm.getMvList()) {

					if (psoMvStatistics.getName().equals(swarmMV.getName())) {
						for (double value : swarmMV.getValues()) {
							for (PsoMVDistribution psoMVDistribution : psoMVDistributionList) {
								if (psoMVDistribution.getRangeMin() <= value
										&& psoMVDistribution.getRangeMax() > value) {
									psoMVDistribution.increase();
									break;
								}
							}
						}

						break;
					}
				}
			}

			psoMvStatistics.setPsoMVDistributionList(psoMVDistributionList);
			psoMvStatisticsList.add(psoMvStatistics);
		}
	}
	
	private void updateGlobalBest() {
		
		this.globalBestValue = Double.MAX_VALUE;
		this.globalBestIndex[0] = -1;
		this.globalBestIndex[1] = -1;
		int swarmIndex = 0;
		
		for (PsoSwarm psoSwarm : this.psoSwarmList) {
			double[] bestValues = psoSwarm.getBestValue();
			for (int i = 0; i < bestValues.length; i++) {
				if (this.globalBestValue > bestValues[i]) {
					this.globalBestValue = bestValues[i];
					this.globalBestIndex[0] = swarmIndex;
					this.globalBestIndex[1] = i;
					this.globalBestOutput = psoSwarm.getBestOutput();
				}
			}

			swarmIndex++;
		}
	}

	private void updateVelocityVector() {
		XThreadPool xThreadPool = new XThreadPool(this.psoSwarmList.size());
		int globalBestPsoSwarmIndex = this.globalBestIndex[0];
		int globalBestPsoMVIndex = this.globalBestIndex[1];

		PsoSwarm globalBestPsoSwarm = this.psoSwarmList.get(globalBestPsoSwarmIndex);
		double[] globalBestPsoMV = new double[globalBestPsoSwarm.getMvList().size()];
		for (int i = 0; i < globalBestPsoMV.length; i++) {
			PsoMV psoMV = globalBestPsoSwarm.getMvList().get(i);
			globalBestPsoMV[i] = psoMV.getBestMV()[globalBestPsoMVIndex];
		}
		
		for (PsoSwarm psoSwarm : this.psoSwarmList) {
			Runnable runnable = new PsoUpdateVelocityVector(psoSwarm, this.inertia, this.correctionFactor, globalBestPsoMV);
			xThreadPool.submit(runnable);
		}
		
		boolean isComplete = xThreadPool.waitForComplete();
		if(!isComplete)
			throw new RuntimeException(xThreadPool.getErrorMessage());
	}
}
