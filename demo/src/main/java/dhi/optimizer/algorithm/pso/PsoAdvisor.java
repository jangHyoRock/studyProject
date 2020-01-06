package dhi.optimizer.algorithm.pso;

import java.util.ArrayList;
import java.util.List;

import dhi.optimizer.algorithm.common.XThreadPool;
import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;

public class PsoAdvisor implements IPsoAdvisor {
	
	private int[] globalBestIndexs = new int[] {0, 0};
	
	// PSO configuration Info.
	protected int particleSize = 0;
	protected double inertia = 0.3;
	protected double correctionFactorCognitive = 1.2;
	protected double correctionFactorSocial = 1.2;

	// PSO NNTrain Info.
	protected List<PsoMV> mvList;
	protected double[] inputData = null;
	protected ActivationNetwork networkModel = null;	
	protected PsoCalculationFunction calculationFunction;
		
	protected int globalBestIndex = -1;	
	protected double globalBestValue = 0.0;
	protected double[] globalBestMV = null;
	
	private int threadCount = (int)(Runtime.getRuntime().availableProcessors() * 0.9);
	private List<PsoSwarm> psoSwarmList;
	
	public PsoAdvisor(List<PsoMV> mvList, double[] inputData, ActivationNetwork networkModel, PsoCalculationFunction calculationFunction, int particleSize, double inertia, double correctionFactorCognitive, double correctionFactorSocial) {
		this.mvList = mvList;
		this.inputData = inputData;
		this.networkModel = networkModel;
		this.calculationFunction = calculationFunction;
		this.particleSize = particleSize;
		this.inertia = inertia;
		this.correctionFactorCognitive = correctionFactorCognitive;
		this.correctionFactorSocial = correctionFactorSocial;
		this.globalBestMV = new double[mvList.size()];
	}
	
	public PsoAdvisor(List<PsoMV> mvList, double[] inputData, ActivationNetwork networkModel, PsoCalculationFunction calculationFunction, int particleSize, double inertia, double correctionFactorCognitive, double correctionFactorSocial, int threadCount) {
		this(mvList, inputData, networkModel, calculationFunction, particleSize, inertia, correctionFactorCognitive, correctionFactorSocial);		
		this.threadCount = threadCount;
	}
	
	public int getGlobalBestIndex() {		
		return globalBestIndex;
	}
	
	public double getGlobalBestValue() {
		return globalBestValue;
	}
	
	public double[] getGlobalBestOutput() {
		double[] inputDataNext = new double[this.inputData.length];
		System.arraycopy(this.inputData, 0, inputDataNext, 0, this.inputData.length);		
		int i = 0;
		for (PsoMV mv : this.mvList) {
			PsoTag[] inputTagList = mv.getInputTagList();
			for (PsoTag inputTag : inputTagList) {

				double inputValue = this.inputData[inputTag.getIndex()];
				switch (mv.getMvType()) {
				case Burner:
				case OFA:
					inputValue += this.globalBestMV[i];
					break;
				case Air:
					inputValue += (inputValue * (this.globalBestMV[i] / 100));
					break;
				}
				
				inputValue = inputTag.valueOfRange(inputValue);
				inputDataNext[inputTag.getIndex()] = inputValue;
			}
			
			i++;
		}
		
		return this.networkModel.calcOutput(inputDataNext);
	}

	public double[] getGlobalBestMV() {
		return globalBestMV;
	}

	public void initialize() {
		
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
			this.psoSwarmList.add(psoSwarm);

			PsoInitalize runnable = new PsoInitalize(psoSwarm, (int)startIdx);
			xThreadPool.submit(runnable);
		}

		boolean isComplete = xThreadPool.waitForComplete();
		if(!isComplete)
			throw new RuntimeException(xThreadPool.getErrorMessage());
	}
	
	public void iteration() {
		updatePosition();
		updateGlobalBest();
		updateVelocityVector();
	}
	
	private void updatePosition() {
		XThreadPool xThreadPool = new XThreadPool(this.psoSwarmList.size());
		for (PsoSwarm psoSwarm : this.psoSwarmList) {			
			Runnable runnable = new PsoUpdatePosition(psoSwarm, this.networkModel, this.inputData, this.calculationFunction);
			xThreadPool.submit(runnable);
		}
		
		boolean isComplete = xThreadPool.waitForComplete();
		if(!isComplete)
			throw new RuntimeException(xThreadPool.getErrorMessage());
	}
	
	private void updateGlobalBest() {
		this.globalBestValue = Double.MAX_VALUE;
		this.globalBestIndexs[0] = -1;
		this.globalBestIndexs[1] = -1;
		int swarmIndex = 0;
		for (PsoSwarm psoSwarm : this.psoSwarmList) {
			if (psoSwarm.getBestValue() < this.globalBestValue) {
				this.globalBestValue = psoSwarm.getBestValue();
				this.globalBestIndexs[0] = swarmIndex;
				this.globalBestIndexs[1] = psoSwarm.getBestIndex();
			}

			swarmIndex++;
		}

		this.globalBestIndex = (this.globalBestIndexs[0] * this.psoSwarmList.get(0).getSwarmSize())	+ (this.globalBestIndexs[1]);
		PsoSwarm globalBestPsoSwarm = this.psoSwarmList.get(this.globalBestIndexs[0]);
		for (int i = 0; i < globalBestPsoSwarm.getMvList().size(); i++) {
			PsoMV psoMV = globalBestPsoSwarm.getMvList().get(i);
			this.globalBestMV[i] = psoMV.getValues()[this.globalBestIndexs[1]];
		}
	}

	private void updateVelocityVector() {
		XThreadPool xThreadPool = new XThreadPool(this.psoSwarmList.size());
		
		for (PsoSwarm psoSwarm : this.psoSwarmList) {
			Runnable runnable = new PsoUpdateVelocityVector(psoSwarm, this.inertia, this.correctionFactorCognitive, this.correctionFactorSocial, this.globalBestMV);
			xThreadPool.submit(runnable);
		}
		
		boolean isComplete = xThreadPool.waitForComplete();
		if(!isComplete)
			throw new RuntimeException(xThreadPool.getErrorMessage());
	}
}
