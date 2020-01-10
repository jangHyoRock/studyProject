package dhi.optimizer.algorithm.neuralNet;

import dhi.optimizer.algorithm.common.LearningDataSet;

/**
 * Supervised learning interface.
 * 
 * The interface describes methods, which should be implemented by all
 * supervised learning algorithms. Supervised learning is such type of learning
 * algorithms, where system's desired output is known on the learning stage. So,
 * given sample input values and desired outputs, system should adopt its
 * internals to produce correct (or close to correct) * result after the
 * learning step is complete.
 * 
 * @author jeeun.moon 
 * @since 2015.03.30
 * @version 1.0
 */

public interface ISupervisedLearning {

	/**
	 * Get sum of weight changes.
	 * 
	 * @return Returns sum of weigh changes of network.
	 */
	double getSumOfWeightChanges();

	/**
	 * Get learning rate.
	 * 
	 * @return Returns learning rate.
	 */
	double getLearningRate();

	/**
	 * Set learning rate for training.
	 * 
	 * @param learningRate
	 *            learning rate to set.
	 */
	void setLearningRate(double learningRate);

	/**
	 * Get momentum.
	 * 
	 * @return Returns momentum.
	 */
	double getMomentum();

	/**
	 * Set momentum.
	 * 
	 * @param momentum
	 *            momentum value to set.
	 */
	void setMomentum(double momentum);

	void setLearingDataSet(LearningDataSet input, LearningDataSet target);
	
	/**
	 * Runs learning iteration.
	 * 
	 * @param input
	 *            Input vector.
	 * @param target
	 *            Desired output vector.
	 * @return Returns learning error.
	 */
	double train(double[] input, double[] target);

	/**
	 * Runs learning epoch.
	 * 
	 * @param input
	 *            Array of input vectors.
	 * @param target
	 *            Array of output vectors.
	 * @return Returns sum of learning errors
	 */
	double trainEpoch(double[][] input, double[][] target);
	
	/**
	 * Runs learning epoch with LearningDataSets.
	 * 
	 * @return Returns sum of learning errors
	 */
	double trainEpoch();
}
