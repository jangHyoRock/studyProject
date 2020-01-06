package dhi.optimizer.algorithm.simulation.pso;

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
		double swarmBestValue = psoSwarm.getSwarmBestValue();
		List<PsoMV> mvList = this.psoSwarm.getMvList();
		try {
			for (int i = 0; i < this.psoSwarm.getSwarmSize(); i++) {
				for (PsoMV mv : mvList) {
					double currValue = mv.getValues()[i];
					double newValue = currValue + mv.getVelocity()[i] / 1.3; // update mv position
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

				// fitness evaluation (you may replace this objective function with any function
				// having a global minima)
				double currentValue = this.calculationFunction.calcCostFunction(outputData);
				double bestValue = this.psoSwarm.getBestValue()[i];				
				if (currentValue < bestValue) {
					for (PsoMV mv : mvList) {
						mv.getBestMV()[i] = mv.getValues()[i]; // update best mv
					}

					this.psoSwarm.getBestValue()[i] = currentValue; // update best value;
					
					// Swarm 중에서 Best Value 가장 작은 값의 Output을 저장하기 위함.
					if (currentValue < swarmBestValue) {
						this.psoSwarm.setBestOutput(outputData);
						this.psoSwarm.setSwarmBestValue(currentValue);
						swarmBestValue = currentValue;
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}