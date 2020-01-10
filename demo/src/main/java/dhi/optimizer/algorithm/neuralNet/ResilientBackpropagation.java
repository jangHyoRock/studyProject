package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;
import java.util.Arrays;

import dhi.optimizer.algorithm.common.LearningDataSet;

/**
 * Resilient Backpropagation learning algorithm. This class implements the
 * resilient backpropagation (RProp) learning algorithm. The RProp learning
 * algorithm is one of the fastest learning algorithms for feed-forward learning
 * networks which use only first-order information.
 * 
 * @author jeeun.moon 
 * @since 2015.03.30
 * @version 1.0
 */

public class ResilientBackpropagation implements ISupervisedLearning, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4604323384817089466L;

	/**
	 * network to teach.
	 */
	private ActivationNetwork network;

	/**
	 * learning rate. Default value equals to 0.1. The value determines speed of
	 * learning. Default value equals to 0.0125
	 */
	private double learningRate = 0.0125;

	/**
	 * maximum boundary for adaptation of the weight update values. Default
	 * value equals to 50.0.
	 */
	private double deltaMax = 50.0;

	/**
	 * minimum boundary for adaptation of the weight update values. Default
	 * value equals to 1e-6.
	 */
	private double deltaMin = 1e-6;

	/**
	 * decrease factor: adaptation of the weight update values. Default value
	 * equals to 0.5.
	 */
	private final double etaMinus = 0.5;

	/**
	 * increase factor: adaptation of the weight update values. Default value
	 * equals to 1.2.
	 */
	private double etaPlus = 1.2;

	/**
	 * neuron's errors
	 */
	private double[][] neuronErrors = null;

	/**
	 * weight's updates: update values, also known as deltas
	 */
	private double[][][] weightsUpdates = null;

	/**
	 * threshold's updates: update values, also known as deltas
	 */
	private double[][] thresholdsUpdates = null;

	/**
	 * weights Derivatives: current gradient values
	 */
	private double[][][] weightsDerivatives = null;

	/**
	 * threshold Derivatives: current gradient values
	 */
	private double[][] thresholdsDerivatives = null;

	/**
	 * weights Derivatives: previous gradient values
	 */
	private double[][][] weightsPreviousDerivatives = null;

	/**
	 * threshold Derivatives: current gradient values
	 */
	private double[][] thresholdsPreviousDerivatives = null;

	/**
	 * Sum of weight changes
	 */
	private double sumOfWeightChanges = 0.0;

	/**
	 * momentum
	 */
	private double momentum = 0.0;

	private LearningDataSet inputDataSet = null;
	
	private LearningDataSet targetDataSet = null;	
	
	/**
	 * Initializes a new instance of the ResilientBackpropagation class.
	 * 
	 * @param network
	 *            Network to teach.
	 */
	public ResilientBackpropagation(ActivationNetwork network) {
		this.network = network;

		int layersCount = network.getLayers().length;

		neuronErrors = new double[layersCount][];

		weightsDerivatives = new double[layersCount][][];
		thresholdsDerivatives = new double[layersCount][];

		weightsPreviousDerivatives = new double[layersCount][][];
		thresholdsPreviousDerivatives = new double[layersCount][];

		weightsUpdates = new double[layersCount][][];
		thresholdsUpdates = new double[layersCount][];

		// initialize errors, derivatives and steps
		for (int i = 0; i < layersCount; i++) {
			Layer layer = network.getLayers()[i];
			int neuronsCount = layer.getNeurons().length;
			int inputsCount = layer.getInputsCount();

			neuronErrors[i] = new double[neuronsCount];

			weightsDerivatives[i] = new double[neuronsCount][];
			weightsPreviousDerivatives[i] = new double[neuronsCount][];
			weightsUpdates[i] = new double[neuronsCount][];

			thresholdsDerivatives[i] = new double[neuronsCount];
			thresholdsPreviousDerivatives[i] = new double[neuronsCount];
			thresholdsUpdates[i] = new double[neuronsCount];

			// for each neuron
			for (int j = 0; j < neuronsCount; j++) {
				weightsDerivatives[i][j] = new double[inputsCount];
				weightsPreviousDerivatives[i][j] = new double[inputsCount];
				weightsUpdates[i][j] = new double[inputsCount];
			}
		}

		// initialize steps
		resetWeightUpdates(learningRate);
	}

	/**
	 * getter for learningRate.
	 */
	@Override
	public double getLearningRate() {
		return learningRate;
	}

	/**
	 * Setter for learningRate.
	 */
	@Override
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
		resetWeightUpdates(learningRate);
	}

	/**
	 * getter for sumOfWeightChanges.
	 */
	@Override
	public double getSumOfWeightChanges() {
		return sumOfWeightChanges;
	}

	/**
	 * getter for momentum.
	 */
	@Override
	public double getMomentum() {
		return momentum;
	}

	/**
	 * setter for momentum;
	 */
	@Override
	public void setMomentum(double momentum) {
		this.momentum = Math.max(0.0, Math.min(1.0, momentum));
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
		// zero gradient
		resetGradient();

		// compute the network's output
		try {
			network.computeOutput(input);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// calculate network error
		double error = calculateError(target);

		// calculate weights updates
		calculateGradient(input);

		// update the network
		updateNetwork();

		// return summary error
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
		// zero gradient
		resetGradient();

		double error = 0.0;
		sumOfWeightChanges = 0.0;

		// run learning procedure for all samples
		for (int i = 0; i < input.length; i++) {
			// compute the network's output
			try {
				network.computeOutput(input[i]);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// calculate network error
			error += calculateError(target[i]);

			// calculate weights updates
			calculateGradient(input[i]);
		}

		// update the network
		updateNetwork();

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
	 * Resets current weight and threshold derivatives.
	 */
	private void resetGradient() {
		for (int i = 0; i < weightsDerivatives.length; i++) {
			for (int j = 0; j < weightsDerivatives[i].length; j++) {
				// Arrays.fill(weightsDerivatives[i][j], 0,
				// weightsDerivatives[i][j].length, 0);
				Arrays.fill(weightsDerivatives[i][j], 0.0);
			}
		}

		for (int i = 0; i < thresholdsDerivatives.length; i++) {
			// Arrays.fill(thresholdsDerivatives[i], 0,
			// thresholdsDerivatives[i].length, 0);
			Arrays.fill(thresholdsDerivatives[i], 0.0);
		}
	}

	/**
	 * Resets the current update steps using the given learning rate.
	 * 
	 * @param rate
	 *            rate to set.
	 */
	private void resetWeightUpdates(double rate) {
		for (int i = 0; i < weightsUpdates.length; i++) {
			for (int j = 0; j < weightsUpdates[i].length; j++) {
				for (int k = 0; k < weightsUpdates[i][j].length; k++) {
					weightsUpdates[i][j][k] = rate;
				}
			}
		}

		for (int i = 0; i < thresholdsUpdates.length; i++) {
			for (int j = 0; j < thresholdsUpdates[i].length; j++) {
				thresholdsUpdates[i][j] = rate;
			}
		}
	}

	/**
	 * Update network's weights.
	 */
	private void updateNetwork() {
		double[][] layerWeightsUpdates;
		double[] layerThresholdUpdates;
		double[] neuronWeightUpdates;

		double[][] layerWeightsDerivatives;
		double[] layerThresholdDerivatives;
		double[] neuronWeightDerivatives;

		double[][] layerPreviousWeightsDerivatives;
		double[] layerPreviousThresholdDerivatives;
		double[] neuronPreviousWeightDerivatives;

		int layersCount = 0;
		int neuronsCount = 0;
		int inputsCount = 0;

		// for each layer of the network
		layersCount = network.getLayers().length;
		for (int i = 0; i < layersCount; i++) {
			ActivationLayer layer = (ActivationLayer) network.getLayers()[i];

			layerWeightsUpdates = weightsUpdates[i];
			layerThresholdUpdates = thresholdsUpdates[i];

			layerWeightsDerivatives = weightsDerivatives[i];
			layerThresholdDerivatives = thresholdsDerivatives[i];

			layerPreviousWeightsDerivatives = weightsPreviousDerivatives[i];
			layerPreviousThresholdDerivatives = thresholdsPreviousDerivatives[i];

			// for each neuron of the layer
			neuronsCount = layer.getNeurons().length;
			for (int j = 0; j < neuronsCount; j++) {
				ActivationNeuron neuron = (ActivationNeuron) layer.getNeurons()[j];

				neuronWeightUpdates = layerWeightsUpdates[j];
				neuronWeightDerivatives = layerWeightsDerivatives[j];
				neuronPreviousWeightDerivatives = layerPreviousWeightsDerivatives[j];

				double S = 0.0;

				// for each weight of the neuron
				inputsCount = neuron.getInputsCount();
				for (int k = 0; k < inputsCount; k++) {
					S = neuronPreviousWeightDerivatives[k]
							* neuronWeightDerivatives[k];

					if (S > 0.0) {
						neuronWeightUpdates[k] = Math.min(
								neuronWeightUpdates[k] * etaPlus, deltaMax);
						neuronWeightUpdates[k] = Math
								.signum(neuronWeightDerivatives[k])
								* neuronWeightUpdates[k];
						neuron.getWeights()[k] -= neuronWeightUpdates[k];
						sumOfWeightChanges += Math.abs(neuronWeightUpdates[k]);
						neuronPreviousWeightDerivatives[k] = neuronWeightDerivatives[k];
					} else if (S < 0.0) {
						neuronWeightUpdates[k] = Math.max(
								neuronWeightUpdates[k] * etaMinus, deltaMin);
						neuronPreviousWeightDerivatives[k] = 0.0;
					} else {
						neuronWeightUpdates[k] = Math
								.signum(neuronWeightDerivatives[k])
								* neuronWeightUpdates[k];
						neuron.getWeights()[k] -= neuronWeightUpdates[k];
						sumOfWeightChanges += Math.abs(neuronWeightUpdates[k]);
						neuronPreviousWeightDerivatives[k] = neuronWeightDerivatives[k];
					}
				}

				// update threshold
				S = layerPreviousThresholdDerivatives[j]
						* layerThresholdDerivatives[j];

				if (S > 0.0) {
					layerThresholdUpdates[j] = Math.min(
							layerThresholdUpdates[j] * etaPlus, deltaMax);
					neuron.setThreshold(neuron.getThreshold()
							- Math.signum(layerThresholdDerivatives[j])
							* layerThresholdUpdates[j]);
					layerPreviousThresholdDerivatives[j] = layerThresholdDerivatives[j];
				} else if (S < 0.0) {
					layerThresholdUpdates[j] = Math.max(
							layerThresholdUpdates[j] * etaMinus, deltaMin);
					layerThresholdDerivatives[j] = 0;
				} else {
					neuron.setThreshold(neuron.getThreshold()
							- Math.signum(layerThresholdDerivatives[j])
							* layerThresholdUpdates[j]);
					layerPreviousThresholdDerivatives[j] = layerThresholdDerivatives[j];
				}
			}
		}
	}

	/**
	 * Calculates error values for all neurons of the network.
	 * 
	 * @param target
	 *            Desired output vector.
	 * @return Returns summary squared error of the last layer divided by 2.
	 */
	private double calculateError(double[] target) {
		double error = 0;
		int layersCount = 0;
		int neuronsCount = 0;
		int nextNeuronsCount = 0;

		// calculate error values for the last layer first
		layersCount = network.getLayers().length;
		ActivationLayer layer = (ActivationLayer) (network.getLayers()[layersCount - 1]);
		double[] layerDerivatives = neuronErrors[layersCount - 1];

		neuronsCount = layer.getNeurons().length;
		for (int i = 0; i < neuronsCount; i++) {
			double output = layer.getNeurons()[i].getOutput();

			double e = output - target[i];
			layerDerivatives[i] = e
					* ((ActivationNeuron) (layer.getNeurons()[i]))
							.getActivationFunction().calDerivativeWithOutput(
									output);
			error += (e * e);
		}

		// calculate error values for other layers
		for (int j = layersCount - 2; j >= 0; j--) {
			layer = (ActivationLayer) network.getLayers()[j];
			layerDerivatives = neuronErrors[j];

			ActivationLayer layerNext = (ActivationLayer) network.getLayers()[j + 1];
			double[] nextDerivatives = neuronErrors[j + 1];

			// for all neurons of the layer
			neuronsCount = layer.getNeurons().length;
			for (int i = 0; i < neuronsCount; i++) {
				double sum = 0.0;
				nextNeuronsCount = layerNext.getNeurons().length;
				for (int k = 0; k < nextNeuronsCount; k++) {
					sum += nextDerivatives[k]
							* layerNext.getNeurons()[k].getWeights()[i];
				}

				layerDerivatives[i] = sum
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
	 * Calculate weights updates
	 * 
	 * @param input
	 *            Network's input vector.
	 */
	private void calculateGradient(double[] input) {
		// 1. calculate updates for the first layer
		ActivationLayer layer = (ActivationLayer) network.getLayers()[0];
		double[] weightErrors = neuronErrors[0];
		double[][] layerWeightsDerivatives = weightsDerivatives[0];
		double[] layerThresholdDerivatives = thresholdsDerivatives[0];
		int layersCount = 0;
		int neuronsCount = 0;
		int inputsCount = 0;
		int prevNeuronsCount = 0;

		// So, for each neuron of the first layer:
		neuronsCount = layer.getNeurons().length;
		for (int i = 0; i < neuronsCount; i++) {
			ActivationNeuron neuron = (ActivationNeuron) layer.getNeurons()[i];
			double[] neuronWeightDerivatives = layerWeightsDerivatives[i];

			// for each weight of the neuron:
			inputsCount = neuron.getInputsCount();
			for (int j = 0; j < inputsCount; j++) {
				neuronWeightDerivatives[j] += weightErrors[i] * input[j];
			}
			layerThresholdDerivatives[i] += weightErrors[i];
		}

		// 2. for all other layers
		layersCount = network.getLayers().length;
		for (int k = 1; k < layersCount; k++) {
			layer = (ActivationLayer) network.getLayers()[k];
			weightErrors = neuronErrors[k];
			layerWeightsDerivatives = weightsDerivatives[k];
			layerThresholdDerivatives = thresholdsDerivatives[k];

			ActivationLayer layerPrev = (ActivationLayer) network.getLayers()[k - 1];

			// for each neuron of the layer
			neuronsCount = layer.getNeurons().length;
			for (int i = 0; i < neuronsCount; i++) {
				double[] neuronWeightDerivatives = layerWeightsDerivatives[i];

				// for each weight of the neuron
				prevNeuronsCount = layerPrev.getNeurons().length;
				for (int j = 0; j < prevNeuronsCount; j++) {
					neuronWeightDerivatives[j] += weightErrors[i]
							* layerPrev.getNeurons()[j].getOutput();
				}
				layerThresholdDerivatives[i] += weightErrors[i];
			}
		}
	}
}
