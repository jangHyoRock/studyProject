package dhi.optimizer.algorithm.pso;

import java.util.List;
import dhi.optimizer.algorithm.common.XFunc;

public class PsoUpdateVelocityVector implements Runnable {

	private PsoSwarm psoSwarm;
	private double[] globalBestMV;
	
	private double inertia = 0.3;
	private double correctionFactorCognitive = 1.2;
	private double correctionFactorSocial = 1.2;	

	private static final double RandRangeMin = 0.0;
	private static final double RandRangeMax = 1.0;
	
	public PsoUpdateVelocityVector(PsoSwarm psoSwarm, double inertia, double correctionFactorCognitive, double correctionFactorSocial, double[] globalBestMV)
	{
		this.psoSwarm = psoSwarm;
		this.inertia = inertia;
		this.correctionFactorCognitive = correctionFactorCognitive;
		this.correctionFactorSocial = correctionFactorSocial;		
		this.globalBestMV = globalBestMV;
	}
	
	@Override
	public void run() {
		
		List<PsoMV> mvList = this.psoSwarm.getMvList();
		try {
			for (int i = 0; i < this.psoSwarm.getSwarmSize(); i++) {				
				double rand1 = XFunc.randomRange(RandRangeMin, RandRangeMax);
				double rand2 = XFunc.randomRange(RandRangeMin, RandRangeMax);
				int mvIndex = 0;
				for (PsoMV mv : mvList) {
					double newVelocity = 0.0;
					double currVelocity = mv.getVelocity()[i];
					double currMV = mv.getValues()[i];
					double pBestMV = mv.getBestMV()[i];
					double gBestMV = this.globalBestMV[mvIndex];

					newVelocity = this.inertia * currVelocity + this.correctionFactorCognitive * rand1 * (pBestMV - currMV) + this.correctionFactorSocial * rand2 * (gBestMV - currMV);
					mv.getVelocity()[i] = newVelocity;

					mvIndex++;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
