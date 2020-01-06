package dhi.optimizer.algorithm.pso;

import java.util.List;

public class PsoInitalize implements Runnable {

	private PsoSwarm psoSwarm;
	private long startIndex;

	public PsoInitalize(PsoSwarm psoSwarm, long startIndex) {
		this.psoSwarm = psoSwarm;
		this.startIndex = startIndex;
	}
	
	@Override
	public void run() {
		int index = 0;
		int swarmSize = this.psoSwarm.getSwarmSize();
		int particleSize = this.psoSwarm.getParticleSize();
		boolean isZeroAdd = this.psoSwarm.isAddZeroToLastIndex();

		try {
			List<PsoMV> psoMVList = this.psoSwarm.getMvList();
			int mvSize = psoMVList.size();
			int loopSize = swarmSize;
			if (isZeroAdd) {
				loopSize = swarmSize - 1;
			}
			
			while (loopSize > index) {

				// Set the best value to the max value.
				this.psoSwarm.getParticleBestValue()[index] = Double.MAX_VALUE;

				// Initialized to 0.
				for (PsoMV mv : psoMVList) {
					mv.setValuesToSpreadData(index, 0);
					mv.getBestMV()[index] = 0.0;
					mv.getVelocity()[index] = 0.0;
				}

				// Each MV is filled with a value from the last MV (dimension) as a concept of
				// dimension.
				long lastRemainder = this.startIndex + index;
				int row = mvSize - 1;
				while (true) {
					int quotient = (int) (lastRemainder / particleSize);
					psoMVList.get(row).setValuesToSpreadData(index, (int) (lastRemainder % particleSize));
					if (quotient >= particleSize) {
						lastRemainder = quotient;
						row--;
					} else {
						psoMVList.get(--row).setValuesToSpreadData(index, quotient);
						break;
					}
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
				
				this.psoSwarm.getParticleBestValue()[index] = Double.MAX_VALUE;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}