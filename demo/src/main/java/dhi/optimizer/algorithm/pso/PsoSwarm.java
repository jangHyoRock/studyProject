package dhi.optimizer.algorithm.pso;

import java.util.List;

public class PsoSwarm {

	private int particleSize;
	private int swarmSize;

	private List<PsoMV> mvList;
	private double[] particleBestValue;
	private double bestValue;
	private int bestIndex;	
	private boolean isAddZeroToLastIndex;

	public PsoSwarm(int particleSize, int swarmSize, List<PsoMV> mvList, boolean isAddZeroToLastIndex) {
		this.particleSize = particleSize;
		this.swarmSize = swarmSize;
		this.mvList = mvList;
		this.isAddZeroToLastIndex = isAddZeroToLastIndex;
		this.particleBestValue = new double[swarmSize];
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
	
	public double[] getParticleBestValue() {
		return particleBestValue;
	}
	
	public void setParticleBestValue(double[] particleBestValue) {
		this.particleBestValue = particleBestValue;
	}

	public double getBestValue() {
		return bestValue;
	}

	public void setBestValue(double bestValue) {
		this.bestValue = bestValue;
	}

	public int getBestIndex() {
		return bestIndex;
	}

	public void setBestIndex(int bestIndex) {
		this.bestIndex = bestIndex;
	}

	public boolean isAddZeroToLastIndex() {
		return isAddZeroToLastIndex;
	}

	public void setAddZeroToLastIndex(boolean isAddZeroToLastIndex) {
		this.isAddZeroToLastIndex = isAddZeroToLastIndex;
	}	
}
