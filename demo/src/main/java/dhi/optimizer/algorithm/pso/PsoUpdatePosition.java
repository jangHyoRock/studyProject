package dhi.optimizer.algorithm.pso;

import java.util.List;

import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;

public class PsoUpdatePosition implements Runnable {
	private PsoSwarm psoSwarm;
		
	private double[] inputData = null;
	private double[] inputDataNext = null;
	
	private PsoCalculationFunction calculationFunction;	
	private ActivationNetwork model = null;
			 
	public PsoUpdatePosition(PsoSwarm psoSwarm, ActivationNetwork model, double[] inputData, PsoCalculationFunction calculationFunction) {
		this.psoSwarm = psoSwarm;
		this.model = model;
		
		this.inputData = inputData;		
		int inputDataSize = this.inputData.length;
		this.inputDataNext = new double[inputDataSize];
		System.arraycopy(this.inputData, 0, this.inputDataNext, 0, inputDataSize);
		this.calculationFunction = calculationFunction;	
	}

	@Override
	public void run() {
		List<PsoMV> mvList = this.psoSwarm.getMvList();
		try {
			
			double swarmBestValue = Double.MAX_VALUE;
			for (int i = 0; i < this.psoSwarm.getSwarmSize(); i++) {
				for (PsoMV mv : mvList) {
					double currValue = mv.getValues()[i];
					double newValue = currValue + mv.getVelocity()[i];// / 1.3; // update mv position
					newValue = mv.valueOfRange(newValue);
					mv.getValues()[i] = newValue;

					PsoTag[] inputTagList = mv.getInputTagList();
					for (PsoTag inputTag : inputTagList) {

						double inputValue = this.inputData[inputTag.getIndex()];
						switch (mv.getMvType()) {
						case Burner:
						case OFA:
							inputValue += newValue;
							break;
						case Air:
							inputValue += (inputValue * (newValue / 100));
							break;
						}

						inputValue = inputTag.valueOfRange(inputValue);
						this.inputDataNext[inputTag.getIndex()] = inputValue;
					}
				}
				
				////////////////////////////////////////////////////////////////////////
				// Compute Particle Fitness
				////////////////////////////////////////////////////////////////////////
				double[] outputData = this.model.calcOutput(this.inputDataNext);		
				double currentValue = this.calculationFunction.calcCostFunction(outputData);
				double pBestValue = this.psoSwarm.getParticleBestValue()[i];

				// Swarm Best Value. (Global Best 찾기 위함)
				// Swarm Best는 현재 Iteration 에서 가장 작은 값을 찾는다.
				if (currentValue < swarmBestValue) {
					swarmBestValue = currentValue;
					this.psoSwarm.setBestValue(currentValue);
					this.psoSwarm.setBestIndex(i);
				}
				
				// Particle Best Value.
				// Particle Best는 역대 Iteration 에서 가장 작은 값을 찾는다. (Best MV를 찾기 위함)
				if (currentValue < pBestValue) {
					for (PsoMV mv : mvList) {
						mv.getBestMV()[i] = mv.getValues()[i]; // update best mv
					}

					this.psoSwarm.getParticleBestValue()[i] = currentValue;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}