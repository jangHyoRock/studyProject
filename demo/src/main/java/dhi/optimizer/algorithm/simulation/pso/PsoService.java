package dhi.optimizer.algorithm.simulation.pso;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dhi.common.util.Utilities;

public class PsoService {

	private static final Logger logger = LoggerFactory.getLogger(PsoService.class);
	
	private PsoAdvisor pso;
	private int iterations = 0;
	
	private double[] zeroBestInputNext;
	private double[] zeroBestOutput;
	private double zeroBestValue = 0;
	
	public PsoService(PsoAdvisor pso, int iterations) {
		this.pso = pso;
		this.iterations = iterations;
	}

	public PsoAdvisor getPso() {
		return pso;
	}

	public void setPso(PsoAdvisor pso) {
		this.pso = pso;
	}	

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public void initalize() {
		this.pso.initalize();
	}
	
	public void initalizeRandom() {
		this.pso.initalizeRandom();
	}

	public int excute() {
		
		for (int i = 0; i < this.iterations; i++) {

			this.pso.iteration(i, this.iterations);
			
			// TODO : 분석용 소스
			// psoStatistics(i);

			int[] gBestIndex = this.pso.getGlobalBestIndex();
			double gBestVal = this.pso.getGlobalBestValue();
			
			int globalBestPsoSwarmIndex = gBestIndex[0];
			int globalBestPsoMVIndex = gBestIndex[1];
			int gBestId = (globalBestPsoSwarmIndex * this.pso.psoSwarmList.get(0).getSwarmSize()) + (globalBestPsoMVIndex + 1);
			
			/*if (i == 0) {
				
				int finalIndex = this.pso.psoSwarmList.size() - 1;
				PsoSwarm finalPsoSwarm = this.pso.psoSwarmList.get(finalIndex);
				
				int finalIndex2 = finalPsoSwarm.getSwarmSize() - 1;
				this.zeroBestValue = finalPsoSwarm.getBestValue()[finalIndex2];
				
				int dataSize = finalPsoSwarm.getBestInputNext().length;
				this.zeroBestInputNext =  new double[dataSize];
				System.arraycopy(finalPsoSwarm.getBestInputNext(), 0, this.zeroBestInputNext, 0, dataSize);
				
				dataSize = finalPsoSwarm.getBestOutput().length;
				this.zeroBestOutput =  new double[dataSize];
				System.arraycopy(finalPsoSwarm.getBestOutput(), 0, this.zeroBestOutput, 0, dataSize);
			}*/
			
			String msg = "Iteration = " + i + "\t" + "(best)optimal val = (" + (gBestId) + ") " + gBestVal + "\t";
			
			PsoSwarm globalBestPsoSwarm = this.pso.psoSwarmList.get(globalBestPsoSwarmIndex);
			for (int j = 0; j < globalBestPsoSwarm.getMvList().size(); j++) {
			
				PsoMV psoMV = globalBestPsoSwarm.getMvList().get(j);
				msg += psoMV.getName() + " = " + psoMV.getBestMV()[globalBestPsoMVIndex] + "\t";
			}
			
			logger.info(msg);
		}
		
		// TODO : 분석용 소스
		// checkVaild();
		
		return 0;
	}
	
	private void checkVaild()
	{
		int[] gBestIndex = this.pso.getGlobalBestIndex();
		double gBestVal = this.pso.getGlobalBestValue();
		
		int globalBestPsoSwarmIndex = gBestIndex[0];
		int globalBestPsoMVIndex = gBestIndex[1];
		
		PsoSwarm globalBestPsoSwarm = this.pso.psoSwarmList.get(globalBestPsoSwarmIndex);
		
		String inputZeroMsg= "";
		for(double input:this.zeroBestInputNext)
		{
			inputZeroMsg += input + ",";
		}		
		
		String inputMsg = "";
		for(double input:globalBestPsoSwarm.getBestInputNext())
		{
			inputMsg += input + ",";
		}
		
		String outputZeroMsg = "";
		for(double output:this.zeroBestOutput)
		{
			outputZeroMsg += output + ",";
		}
		
		String outputMsg = "";
		for(double output:globalBestPsoSwarm.getBestOutput())
		{
			outputMsg += output + ",";
		}
		
		logger.info("## Zero gBest : " + this.zeroBestValue);
		logger.info("## Pso gBest : " +  gBestVal);
		
		logger.info("## Zero input : " + inputZeroMsg);
		logger.info("## Pso input : " + inputMsg);
		
		logger.info("## Zero output : " + outputZeroMsg);
		logger.info("## Pso output : " + outputMsg);
	}
	
	private void psoStatistics(int i)
	{
		List<PsoMV> psoMvStatisticsList = this.pso.getPsoMvStatisticsList();

		// TODO : 로그 분석 용 소스 
		//if(i == 0 || i == 1 || i == this.iterations - 1)
		if(i >= 0)
		{
			StringBuilder sb = new StringBuilder();
			for(PsoMV psoMvStatistics:psoMvStatisticsList)
			{	
				sb.append(" "+"\t" + psoMvStatistics.getName() + "\t");
			}
			sb.append("\r\n");
			for(int j=0;j<10;j++)
			{
				for(PsoMV psoMvStatistics:psoMvStatisticsList)
				{	
					if(psoMvStatistics.getPsoMVDistributionList().size() > j)
					{
						List<PsoMVDistribution> psoMVDistributionList = psoMvStatistics.getPsoMVDistributionList();
						PsoMVDistribution psoMVDistribution = psoMVDistributionList.get(j);
						sb.append(Utilities.round(psoMVDistribution.getRangeMin(), 1)+ "~" + Utilities.round(psoMVDistribution.getRangeMax(), 1) + "\t" + psoMVDistribution.getCount()+"\t");
					}
				}
				
				sb.append("\r\n");
			}
			
			logger.info(sb.toString());
		}
	}
}