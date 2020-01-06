package dhi.optimizer.algorithm.simulation.pso;

import java.util.List;

public class PsoInitalizeRandom implements Runnable {

	private PsoSwarm psoSwarm;
	private long startIndex;

	public PsoInitalizeRandom(PsoSwarm psoSwarm, long startIndex) {
		this.psoSwarm = psoSwarm;
		this.startIndex = startIndex;
	}
	
	@Override
	public void run() {
		int index = 0;
		int swarmSize = this.psoSwarm.getSwarmSize();
		boolean isZeroAdd = this.psoSwarm.isAddZeroToLastIndex();

		try {
			List<PsoMV> psoMVList = this.psoSwarm.getMvList();
			int loopSize = swarmSize;
			if (isZeroAdd) {
				loopSize = swarmSize - 1;
			}
			
			while (loopSize > index) {

				// Set the best value to the max value.
				this.psoSwarm.getBestValue()[index] = Double.MAX_VALUE;

				// Initialized to 0.
				for (PsoMV mv : psoMVList) {
					mv.setValuesToRandomData(index);
					mv.getBestMV()[index] = 0.0;
					mv.getVelocity()[index] = 0.0;
				}

				index++;
			}

			// Set the last value to zero.
			if (isZeroAdd) {
				for (PsoMV mv : psoMVList) {
					mv.getValues()[index] = 0.0;
					mv.getBestMV()[index] = 0.0;
					mv.getVelocity()[index] = 0.0;
				}

				this.psoSwarm.getBestValue()[index] = Double.MAX_VALUE;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}