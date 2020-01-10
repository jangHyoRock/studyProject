package dhi.optimizer.algorithm.simulation.pso;

import java.util.List;

public class PsoSwarm {

	private int particleSize;
	private int swarmSize;

	private List<PsoMV> mvList;
	private double[] bestValue;
	private double[] bestInputNext;
	private double[] bestOutput;
	private double swarmBestValue;			

	private boolean isAddZeroToLastIndex;

	public PsoSwarm(int particleSize, int swarmSize, List<PsoMV> mvList, boolean isAddZeroToLastIndex) {
		this.particleSize = particleSize;
		this.swarmSize = swarmSize;
		this.mvList = mvList;
		this.isAddZeroToLastIndex = isAddZeroToLastIndex;
		this.bestValue = new double[swarmSize];
	}

	public int getParticleSize() {
		return particleSize;
	}

	public void setParticleSize(int particleSize) {
		this.particleSize = particleSize;
	}

	public int getSwarmSize() {
		return swarmSize;
	}

	public void setSwarmSize(int swarmSize) {
		this.swarmSize = swarmSize;
	}

	public List<PsoMV> getMvList() {
		return mvList;
	}

	public void setMvList(List<PsoMV> mvList) {
		this.mvList = mvList;
	}

	public double[] getBestValue() {
		return bestValue;
	}

	public void setBestValue(double[] bestValue) {
		this.bestValue = bestValue;
	}
	
	public double[] getBestInputNext() {
		return bestInputNext;
	}

	public void setBestInputNext(double[] bestInputNext) {
		this.bestInputNext = bestInputNext;
	}

	public double[] getBestOutput() {
		return bestOutput;
	}

	public void setBestOutput(double[] bestOutput) {
		this.bestOutput = bestOutput;
	}

	public boolean isAddZeroToLastIndex() {
		return isAddZeroToLastIndex;
	}

	public void setAddZeroToLastIndex(boolean isAddZeroToLastIndex) {
		this.isAddZeroToLastIndex = isAddZeroToLastIndex;
	}

	public double getSwarmBestValue() {
		return swarmBestValue;
	}

	public void setSwarmBestValue(double swarmBestValue) {
		this.swarmBestValue = swarmBestValue;
	}
	
}
