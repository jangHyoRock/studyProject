package dhi.optimizer.algorithm.neuralNet;

import java.rmi.Remote;
import java.util.List;

import dhi.optimizer.algorithm.common.LearningInfo;

public interface SupervisedLearningService extends Remote {
	
	public void setTargetTrainInfo(LearningInfo targetTrainInfo);
	
	public void setSelectedlearningAlgorithm(SupervisedLearnings selectedlearningAlgorithm);
		
	public int getTrainIterationCount();
	
	public double getMinTrainMseAvg();
	
	public double getMinValidMseAvg();
	
	public ActivationNetwork getAvailableNetwork();
	
	public void intialize(double[][] trainInputData, double[][] trainTargetData, double[][] validInputData, double[][] validTargetData);
	
	public void execute();
	
	public boolean verification(double[][] testInputData, double[][] testTargetData, List<ActivationNetwork> networkList);

	public void setTestFileId(String string);
}
