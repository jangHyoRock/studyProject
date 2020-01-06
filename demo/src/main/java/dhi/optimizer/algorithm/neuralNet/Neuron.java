package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;

import dhi.optimizer.algorithm.common.FloatRange;
import dhi.optimizer.algorithm.common.ThreadSafeRandom;

/**
 * abstract neuron class. This is a base neuron class, which encapsulates such
 * common properties, like neuron's input, output and weights.
 *  
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public abstract class Neuron implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5908451801424443324L;

	/**
	 * Neuron's inputs count.
	 */
	protected int inputsCount = 0;

	/**
	 * Nouron's weights.
	 */
	protected double[] weights = null;

	/**
	 * Neuron's output value.
	 */
	protected double output = 0;


	/**
	 * Random number generator. The generator is used for neuron's weights
	 * randomization.
	 */
	protected static ThreadSafeRandom randGenerator = new ThreadSafeRandom();

	/**
	 * Random generator range. Sets the range of random generator. Affects
	 * initial values of neuron's weight. Default value is [0, 1].
	 */
	protected static FloatRange randRange = new FloatRange(0.0f, 1.0f);

	/**
	 * Initializes a new instance of the class The new neuron will be randomized
	 * Randomize method after it is created.
	 * 
	 * @param inputs
	 *            Neuron's inputs count.
	 * @see dhi.optimizer.algorithm.common.ThreadSafeRandom
	 */
	protected Neuron(int inputs) {
		// allocate weights
		inputsCount = Math.max(1, inputs);
		weights = new double[inputsCount];
		// randomize the neuron
		randomizeWeights();
	}
	
	protected Neuron() {
		
	}

	public int getInputsCount() {
		return inputsCount;
	}
	
	public void setInputsCount(int inputsCount) {
		this.inputsCount = inputsCount;
	}	

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}	
	
	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public static ThreadSafeRandom getRandGenerator() {
		return randGenerator;
	}

	public static void setRandGenerator(ThreadSafeRandom randGenerator) {
		Neuron.randGenerator = randGenerator;
	}

	public static FloatRange getRandRange() {
		return randRange;
	}

	public static void setRandRange(FloatRange randRange) {
		Neuron.randRange = randRange;
	}

	/**
	 * Randomize neuron. Initialize neuron's weights with random values within
	 * the range specified
	 */
	public void randomizeWeights() {
		double f = randRange.getMax();

		// randomize weights
		for (int i = 0; i < inputsCount; i++)
			weights[i] = randGenerator.nextDouble() * f + randRange.getMin();
	}

	/**
	 * Computes output value of neuron. The actual neuron's output value is
	 * determined by inherited class. The output value is also stored in output
	 * property
	 * 
	 * @param input
	 *            Input vector.
	 * @return Returns neuron's output value.
	 * @throws Exception
	 *             Wrong length of the input vector, which is not equal to the
	 *             Neuron.InputsCount expected value
	 */
	public abstract double computeOutput(double[] input) throws Exception;
}
