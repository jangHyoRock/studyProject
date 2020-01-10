package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;

import dhi.optimizer.algorithm.common.LearningDataSet;

/**
 * Back propagation learning algorithm The class implements back propagation
 * learning algorithm, which is widely used for training multi-layer neural
 * networks with continuous activation functions *
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public class BackPropagation implements ISupervisedLearning, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3485834260631619449L;

	/** 
	 * network to teach.
	 */
	private ActivationNetwork network;

	/**
	 * learning rate. Default value equals to 0.1. The value determines speed of
	 * learning.
	 */
	private double learningRate = 0.1;

	/**
	 * Momentum. The value determines the portion of previous weight's update to
	 * use on current iteration. Weight's update values are calculated on each
	 * iteration depending on neuron's error. The momentum specifies the amount
	 * of update to use from previous iteration and the amount of update to use
	 * from current iteration. If the value is equal to 0.1, for example, then
	 * 0.1 portion of previous update and 0.9 portion of current update are used
	 * to update weight's value. Default value equals to 0.0.
	 */
	private double momentum = 0.0;

	/**
	 * neuron's errors
	 */
	private double[][] neuronErrors = null;

	/**
	 * weight's updates
	 */
	private double[][][] weightsUpdates = null;

	/**
	 * threshold's updates
	 */
	private double[][] thresholdsUpdates = null;

	/**
	 * Sum of weight changes
	 */
	private double sumOfWeightChanges = 0.0;

	private LearningDataSet inputDataSet = null;
	
	private LearningDataSet targetDataSet = null;
	
	/**
	 * Initializes a new instance of the BackPropagation class.
	 * 
	 * @param network
	 *            Network to teach.
	 */
	public BackPropagation(ActivationNetwork network) {
		int layersCount = 0;
		this.network = network;

		layersCount = network.getLayers().length;

		// create error and deltas arrays
		neuronErrors = new double[layersCount][];
		weightsUpdates = new double[layersCount][][];
		thresholdsUpdates = new double[layersCount][];

		// initialize errors and deltas arrays for each layer
		for (int i = 0; i < layersCount; i++) {
			Layer layer = network.getLayers()[i];
			int neuronsCount = layer.getNeurons().length;

			neuronErrors[i] = new double[neuronsCount];
			weightsUpdates[i] = new double[neuronsCount][];
			thresholdsUpdates[i] = new double[neuronsCount];

			// for each neuron
			for (int j = 0; j < weightsUpdates[i].length; j++) {
				weightsUpdates[i][j] = new double[layer.getInputsCount()];
			}
		}
	}

	/**
	 * Get learning rate.
	 */
	@Override
	public double getLearningRate() {
		return learningRate;
	}

	/**
	 * Learning rate, [0, 1].
	 * 
	 * @param learning
	 *            rate value to set.
	 */
	@Override
	public void setLearningRate(double learningRate) {
		this.learningRate = Math.max(0.0, Math.min(1.0, learningRate));
	}

	/**
	 * get momentum.
	 */
	@Override
	public double getMomentum() {
		return momentum;
	}

	/**
	 * Momentum, [0, 1].
	 * 
	 * @param momentum
	 *            momentum value to set.
	 */
	@Override
	public void setMomentum(double momentum) {
		this.momentum = Math.max(0.0, Math.min(1.0, momentum));
	}

	/**
	 * Get sum of weight changes.
	 */
	@Override
	public double getSumOfWeightChanges() {
		return sumOfWeightChanges;
	}

	@Override
	public void setLearingDataSet(LearningDataSet input, LearningDataSet target) {
		inputDataSet = input;
		targetDataSet = target;
		network.setInputNormalizeInfo(inputDataSet.getNormalizeInfo().clone());
		network.setTargetNormalizeInfo(targetDataSet.getNormalizeInfo().clone());
	}	

	/**
	 * Runs learning iteration. Runs one learning iteration and updates neuron's
	 * weights.
	 * 
	 * @param input
	 *            Input vector.
	 * @param target
	 *            Desired output vector.
	 * @return Returns squared error(difference between current network's output
	 *         and desired output) divided by 2.
	 */
	@Override
	public double train(double[] input, double[] target) {
		// compute the network's output
		try {
			network.computeOutput(input);
		} catch (Exception ex) {
			System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			ex.printStackTrace();
		}

		// calculate network error
		double error = calculateError(target);

		// calculate weights updates
		calculateUpdates(input);

		// update the network
		updateNetwork();

		return error;
	}

	/**
	 * Runs learning epoch. The method runs one learning epoch, by calling train
	 * method for each vector provided in the input array.
	 * 
	 * @param input
	 *            Array of input vectors.
	 * @param target
	 *            Array of output vectors.
	 * @return Returns summary learning error for the epoch. See train method
	 *         for details about learning error calculation.
	 * @see train()
	 */
	@Override
	public double trainEpoch(double[][] input, double[][] target) {
		double error = 0.0;
		sumOfWeightChanges = 0.0;

		// run learning procedure for all samples
		for (int i = 0; i < input.length; i++) {
			error += train(input[i], target[i]);
		}

		// return summary error
		return error;
	}
	
	@Override
	public double trainEpoch() {
		double error = 0.0;

		error = trainEpoch(inputDataSet.getNormalizedDataItems(), targetDataSet.getNormalizedDataItems());

		return error;
	}
	

	/**
	 * Calculates error values for all neurons of the network.
	 * 
	 * @param target
	 *            Desired output vector.
	 * @return Returns summary squared error of the last layer divided by 2.
	 */
	private double calculateError(double[] target) {
		// current and the next layers
		Layer layer, layerNext;
		// current and the next errors arrays
		double[] errors, errorsNext;
		// error values
		double error = 0, e, sum;
		// neuron's output value
		double output;
		// layers count
		int layersCount = network.getLayers().length;
		int neuronsCount = 0;
		int nextLayerNeuronsCount = 0;

		// assume, that all neurons of the network have the same activation
		// function
		// IActivationFunction function = ( network.Layers[0].Neurons[0] as
		// ActivationNeuron ).ActivationFunction;

		// calculate error values for the last layer first
		layer = network.getLayers()[layersCount - 1];
		errors = neuronErrors[layersCount - 1];

		neuronsCount = layer.getNeurons().length;
		for (int i = 0; i < neuronsCount; i++) {
			output = layer.getNeurons()[i].getOutput();
			// error of the neuron
			e = target[i] - output;
			// error multiplied with activation function's derivative
			errors[i] = e
					* ((ActivationNeuron) (layer.getNeurons()[i]))
							.getActivationFunction().calDerivativeWithOutput(
									output);
			// square the error and sum it
			error += (e * e);
		}

		// calculate error values for other layers
		for (int j = layersCount - 2; j >= 0; j--) {
			layer = network.getLayers()[j];
			layerNext = network.getLayers()[j + 1];
			errors = neuronErrors[j];
			errorsNext = neuronErrors[j + 1];

			// for all neurons of the layer
			neuronsCount = layer.getNeurons().length;
			nextLayerNeuronsCount = layerNext.getNeurons().length;
			for (int i = 0; i < neuronsCount; i++) {
				sum = 0.0;
				// for all neurons of the next layer
				for (int k = 0; k < nextLayerNeuronsCount; k++) {
					sum += errorsNext[k]
							* layerNext.getNeurons()[k].getWeights()[i];
				}
				errors[i] = sum
						* ((ActivationNeuron) (layer.getNeurons()[i]))
								.getActivationFunction()
								.calDerivativeWithOutput(
										layer.getNeurons()[i].getOutput());
			}
		}

		// return squared error of the last layer divided by 2
		return error / 2.0;
	}

	/**
	 * Calculate weights updates.
	 * 
	 * @param input
	 *            Network's input vector.
	 */
	private void calculateUpdates(double[] input) {
		// current neuron
		//Neuron neuron;
		// current and previous layers
		Layer layer, layerPrev;
		// layer's weights updates
		double[][] layerWeightsUpdates;
		// layer's thresholds updates
		double[] layerThresholdUpdates;
		// layer's error
		double[] errors;
		// neuron's weights updates
		double[] neuronWeightUpdates;
		// error value
		// double error;

		int neuronsCount = 0;
		int layersCount = 0;

		// 1 - calculate updates for the first layer
		layer = network.getLayers()[0];
		errors = neuronErrors[0];
		layerWeightsUpdates = weightsUpdates[0];
		layerThresholdUpdates = thresholdsUpdates[0];

		// cache for frequently used values
		double cachedMomentum = learningRate * momentum;
		double cached1mMomentum = learningRate * (1.0 - momentum);
		double cachedError;

		// for each neuron of the layer
		neuronsCount = layer.getNeurons().length;
		for (int i = 0; i < neuronsCount; i++) {
			//neuron = layer.getNeurons()[i];
			cachedError = errors[i] * cached1mMomentum;
			neuronWeightUpdates = layerWeightsUpdates[i];

			// for each weight of the neuron
			for (int j = 0; j < neuronWeightUpdates.length; j++) {
				// calculate weight update
				neuronWeightUpdates[j] = cachedMomentum
						* neuronWeightUpdates[j] + cachedError * input[j];
			}

			// calculate threshold update
			layerThresholdUpdates[i] = cachedMomentum
					* layerThresholdUpdates[i] + cachedError;
		}

		// 2 - for all other layers
		layersCount = network.getLayers().length;
		for (int k = 1; k < layersCount; k++) {
			layerPrev = network.getLayers()[k - 1];
			layer = network.getLayers()[k];
			errors = neuronErrors[k];
			layerWeightsUpdates = weightsUpdates[k];
			layerThresholdUpdates = thresholdsUpdates[k];

			// for each neuron of the layer
			neuronsCount = layer.getNeurons().length;
			for (int i = 0; i < neuronsCount; i++) {
				//neuron = layer.getNeurons()[i];
				cachedError = errors[i] * cached1mMomentum;
				neuronWeightUpdates = layerWeightsUpdates[i];

				// for each synapse of the neuron
				for (int j = 0; j < neuronWeightUpdates.length; j++) {
					// calculate weight update
					neuronWeightUpdates[j] = cachedMomentum
							* neuronWeightUpdates[j] + cachedError
							* layerPrev.getNeurons()[j].getOutput();
				}

				// calculate threshold update
				layerThresholdUpdates[i] = cachedMomentum
						* layerThresholdUpdates[i] + cachedError;
			}
		}
	}

	/**
	 * Update network'sweights.
	 */
	private void updateNetwork() {
		// current neuron
		ActivationNeuron neuron;
		// current layer
		Layer layer;
		// layer's weights updates
		double[][] layerWeightsUpdates;
		// layer's thresholds updates
		double[] layerThresholdUpdates;
		// neuron's weights updates
		double[] neuronWeightUpdates;

		int layersCount = 0;
		int neuronsCount = 0;

		// for each layer of the network
		layersCount = network.getLayers().length;
		;
		for (int i = 0; i < layersCount; i++) {
			layer = network.getLayers()[i];
			layerWeightsUpdates = weightsUpdates[i];
			layerThresholdUpdates = thresholdsUpdates[i];

			// for each neuron of the layer
			neuronsCount = layer.getNeurons().length;
			for (int j = 0; j < neuronsCount; j++) {
				neuron = (ActivationNeuron) layer.getNeurons()[j];
				neuronWeightUpdates = layerWeightsUpdates[j];

				// for each weight of the neuron
				for (int k = 0; k < neuron.getWeights().length; k++) {
					// update weight
					neuron.getWeights()[k] += neuronWeightUpdates[k];
					sumOfWeightChanges += Math.abs(neuronWeightUpdates[k]);
				}
				// update threshold
				neuron.setThreshold(neuron.getThreshold()
						+ layerThresholdUpdates[j]);
			}
		}
	}
}
