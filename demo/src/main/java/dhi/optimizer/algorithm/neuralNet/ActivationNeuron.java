package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;


/**
 * Activation neuron. Activation neuron computes weighted sum of its inputs,
 * adds threshold value and then applies activation function. The neuron is
 * usually used in multi-layer neural networks.
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public class ActivationNeuron extends Neuron implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3322336237786799093L;

	/** 
	 * Threshold value. The value is added to inputs weighted sum before it is
	 * passed to activation function.
	 */
	protected double threshold = 0.0;

	/**
	 * Activation function. The function is applied to inputs weighted sum plus
	 * threshold value.
	 */
	protected IActivationFunction activationFunction = null;

	/**
	 * Initializes a new instance of the class.
	 * 
	 * @param inputs
	 *            Neuron's inputs count.
	 * @param activationFunction
	 *            Neuron's activation function.
	 */
	public ActivationNeuron(int inputs, IActivationFunction activationFunction) {
		super(inputs);
		this.activationFunction = activationFunction;
	}
	
	public ActivationNeuron() {
		
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public IActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(IActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	/**
	 * Randomize weight of neuron. Calls base class Neuron.randomizeWeights
	 * method to randomize neuron's weights and then randomizes threshold's
	 * value.
	 */
	@Override
	public void randomizeWeights() {
		// randomize weights
		super.randomizeWeights();
		// randomize threshold
		this.threshold = randGenerator.nextDouble() * (randRange.getLength())
				+ randRange.getMin();
	}

	/**
	 * Computes output value of neuron. The output value of activation neuron is
	 * equal to value of nueron's activation function, which parameter is
	 * weighted sum of its inputs plus threshold value. The output value is also
	 * stored in Neuron.Output property. The method may be called safely from
	 * multiple threads to compute neuron's output value for the specified input
	 * values. However, the value of Neuron.Output property in multi-threaded
	 * environment is not predictable, since it may hold neuron's output
	 * computed from any of the caller threads. Multi-threaded access to the
	 * method is useful in those cases when it is required to improve
	 * performance by utilizing several threads and the computation is based on
	 * the immediate return value of the method, but not on neuron's output
	 * property.
	 * 
	 * @param input
	 *            Input vector.
	 * @return Returns neuron's output value.
	 */
	@Override
	public double computeOutput(double[] input) throws Exception {
		// initial sum value
		double sum = 0.0;
		double outputTemp = 0.0;

		// check for correct input vector
		if (input.length != inputsCount) {
			throw new Exception("Wrong length of the input vector.");
		}

		// compute weighted sum of inputs
		for (int i = 0; i < weights.length; i++) {
			sum += weights[i] * input[i];
		}
		sum += threshold;

		// local variable to avoid multi threaded conflicts
		outputTemp = activationFunction.calcFunction(sum);

		// assign output property as well (works correctly for single threaded
		// usage)
		this.output = outputTemp;

		return this.output;
	}
}
