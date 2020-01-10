package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;


/**
 * abstract neural layer class. This is a base neural layer class, which
 * represents collection of neurons.
 * 
 * @author jeeun.moon
 * @since 2015.03.30 
 * @version 1.0
 */

public abstract class Layer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2325448945114134680L;

	/**
	 * Layer's inputs count.
	 */
	protected int inputsCount = 0;

	/**
	 * Layer's neurons count.
	 */
	protected int neuronsCount = 0;

	/**
	 * Layer's neurons.
	 */
	protected Neuron[] neurons;

	/**
	 * Layer's output vector. The calculation way of layer's output vector is
	 * determined by neurons, which comprise the layer. The property is not
	 * initialized(equals to null) until Compute method is called.
	 */
	protected double[] output;

	/**
	 * Initializes a new instance of the Layer class. Protected constructor,
	 * which initializes inputsCount, neuronsCount and neurons members.
	 * 
	 * @param neuronsCount
	 *            Layer's neurons count
	 * @param inputsCount
	 *            Layer's inputs count.
	 */
	protected Layer(int neuronsCount, int inputsCount) {
		this.inputsCount = Math.max(1, inputsCount);
		this.neuronsCount = Math.max(1, neuronsCount);
		// create collection of neurons
		neurons = new Neuron[this.neuronsCount];
	}
	
	protected Layer() {
		
	}

	public int getInputsCount() {
		return inputsCount;
	}

	public void setInputsCount(int inputsCount) {
		this.inputsCount = inputsCount;
	}
	
	public int getNeuronsCount() {
		return neuronsCount;
	}

	public void setNeuronsCount(int neuronsCount) {
		this.neuronsCount = neuronsCount;
	}	
	
	public Neuron[] getNeurons() {
		return neurons;
	}

	public void setNeurons(Neuron[] neurons) {
		this.neurons = neurons;
	}

	public double[] getOutput() {
		return output;
	}
	
	public void setOutput(double[] output) {
		this.output = output;
	}	

	/**
	 * Compute output vector of the layer. The actual layer's output vector is
	 * determined by neurons, which comprise the layer - consists of output
	 * values of layer's neurons. The output vector is also stored in Output
	 * property. The method may be called safely from multiple threads to
	 * compute layer's output value for the specified input values. However, the
	 * value of Output property in multi-threaded environment is not
	 * predictable, since it may hold layer's output computed from any of the
	 * caller threads. Multi-threaded access to the method is useful in those
	 * cases when it is required to improve performance by utilizing several
	 * threads and the computation is based on the immediate return value of the
	 * method, but not on layer's output property.
	 * 
	 * @param input
	 *            Input vector.
	 * @return Returns layer's output vector.
	 * @throws Exception
	 *             Wrong length of the input vector, which is not equal to the
	 *             Neuron.InputsCount expected value
	 */
	public double[] computeOutput(double[] input) throws Exception {
		// check for correct input vector
		if (input.length != inputsCount) {
			throw new Exception("Wrong length of the input vector.");
		}

		// local variable to avoid mutlithread conflicts
		double[] output = new double[neuronsCount];

		// compute each neuron
		for (int i = 0; i < neurons.length; i++)
			output[i] = neurons[i].computeOutput(input);

		// assign output property as well (works correctly for single threaded
		// usage)
		this.output = output;

		return output;
	}

	/**
	 * Randomize neurons of the layer. Randomizes layer's neurons by calling
	 * Neuron.randomizeWeights method of each neuron.
	 * 
	 * @see Algorithm.NN.Neurons.Neuron.randomizeWeights()
	 */
	public void randomizeWeights() {
		for (int i = 0; i < neurons.length; i++) {
			neurons[i].randomizeWeights();
		}
	}
}
