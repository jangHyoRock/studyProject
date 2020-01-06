package dhi.optimizer.algorithm.pso;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;
import dhi.optimizer.model.db.PSOConfigEntity;

public class PsoService {
	
	private enum ProcessingUnit {
		CPU, GPU
	}
	
	private static final Logger logger = LoggerFactory.getLogger(PsoService.class);	
	
	private ProcessingUnit processingUnitMode;
	private IPsoAdvisor pso;
	
	private int iteration = 0;
	private List<PsoMV> mvList;
	
	public PsoService(List<PsoMV> mvList, double[] inputData, ActivationNetwork networkModel, PsoCalculationFunction calculationFunction, PSOConfigEntity psoConfigEntity, String processingUnit, int gpuUseSwarmsize) {
		this.mvList = mvList;
		this.iteration = psoConfigEntity.getIteration();		

		if (ProcessingUnit.CPU.name().equals(processingUnit.toUpperCase())) {
			this.processingUnitMode = ProcessingUnit.CPU;
		} else if (ProcessingUnit.GPU.name().equals(processingUnit.toUpperCase())) {
			this.processingUnitMode = ProcessingUnit.GPU;
		} else {
			long swarmSize = (long)Math.pow(psoConfigEntity.getParticleCntPerMv(), this.mvList.size());
			if (gpuUseSwarmsize > swarmSize) {
				this.processingUnitMode = ProcessingUnit.GPU;
			} else {
				this.processingUnitMode = ProcessingUnit.CPU;
			}
		}
		
		switch (this.processingUnitMode) {
		case CPU:
			this.pso = new PsoAdvisor(mvList, inputData, networkModel, calculationFunction, psoConfigEntity.getParticleCntPerMv(), psoConfigEntity.getInertia(), psoConfigEntity.getCorrectionFactor(), psoConfigEntity.getCorrectionFactor(), psoConfigEntity.getCpuUsageLimitValue());
			break;
			
		case GPU:
			this.pso = new PsoAdvisorGPU(mvList, inputData, networkModel, calculationFunction, psoConfigEntity.getParticleCntPerMv(), psoConfigEntity.getInertia(), psoConfigEntity.getCorrectionFactor(), psoConfigEntity.getCorrectionFactor(), psoConfigEntity.getIteration());
			break;
		}
	}	
	
	public int getGlobalBestIndex() {
		return this.pso.getGlobalBestIndex();
	}

	public double getGlobalBestValue() {
		return this.pso.getGlobalBestValue();
	}

	public double[] getGlobalBestOutput() {
		return this.pso.getGlobalBestOutput();
	}	

	public double[] getGlobalBestMV() {
		return this.pso.getGlobalBestMV();
	}

	public int execute() {

		int result = 0;
		try {
			
			// PSO Initialize.
			this.pso.initialize();

			for (int i = 0; i < this.iteration; i++) {
				
				// PSO Process Iteration.
				this.pso.iteration();
				
				String msg = "[" + this.processingUnitMode.name() + "] Iteration = " + i + "\t" + "(best)optimal val = (" + (this.pso.getGlobalBestIndex()) + ") " + this.pso.getGlobalBestValue() + "\t";
				for (int j = 0; j < this.mvList.size(); j++) {
					PsoMV psoMV = this.mvList.get(j);
					msg += psoMV.getName() + " = " + this.pso.getGlobalBestMV()[j] + "\t";
				}
				logger.info(msg);
			}			
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			result = -1;
		}
		
		return result;
	}
}