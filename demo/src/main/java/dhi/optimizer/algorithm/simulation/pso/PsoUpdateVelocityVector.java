package dhi.optimizer.algorithm.simulation.pso;

import java.util.List;
import dhi.optimizer.algorithm.common.XFunc;

public class PsoUpdateVelocityVector implements Runnable {

	private PsoSwarm psoSwarm;
	private double[] globalBestPsoMV;
	
	private double inertia = 0.3;
	private double correctionFactor = 1.2;

	private static final double RandRangeMin = 0.0;
	private static final double RandRangeMax = 1.0;
	
	public PsoUpdateVelocityVector(PsoSwarm psoSwarm, double inertia, double correctionFactor, double[] globalBestPsoMV)
	{
		this.psoSwarm = psoSwarm;
		this.inertia = inertia;
		this.correctionFactor = correctionFactor;
		this.globalBestPsoMV = globalBestPsoMV;
	}
	
	@Override
	public void run() {
		
		List<PsoMV> mvList = this.psoSwarm.getMvList();
		try {
			for (int i = 0; i < this.psoSwarm.getSwarmSize(); i++) {
				int mvIndex = 0;
				for (PsoMV mv : mvList) {
					double newVelocity = 0.0;
					double currVelocity = mv.getVelocity()[i];
					double currMV = mv.getValues()[i];
					double bestMV = mv.getBestMV()[i];
					double gBestMV = this.globalBestPsoMV[mvIndex];

					double rand1 = XFunc.randomRange(RandRangeMin, RandRangeMax);
					double rand2 = XFunc.randomRange(RandRangeMin, RandRangeMax);

					newVelocity = this.inertia * currVelocity + this.correctionFactor * rand1 * (bestMV - currMV) + this.correctionFactor * rand2 * (gBestMV - currMV);
					mv.getVelocity()[i] = newVelocity;

					mvIndex++;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
