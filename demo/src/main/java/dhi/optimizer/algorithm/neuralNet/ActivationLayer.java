package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;


/**
 * Activation layer. Activation layer is a layer of activation neurons. The
 * layer is usually used in multi-layer neural networks.
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public class ActivationLayer extends Layer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1158523726859098659L;

	/** 
	 * Initializes a new instance of the ActivationLayer class. The new layer is
	 * randomized(ActivationNeuron.randomizeWeight method) after it is created
	 * 
	 * @param neuronsCount
	 *            Layer's neurons count.
	 * @param inputsCount
	 *            Layer's inputs count.
	 * @param function
	 *            Activation function of neurons of the layer.
	 */
	public ActivationLayer(int neuronsCount, int inputsCount,
			IActivationFunction function) {
		super(neuronsCount, inputsCount);

		// create each neuron
		for (int i = 0; i < neurons.length; i++)
			neurons[i] = new ActivationNeuron(inputsCount, function);
	}

	public ActivationLayer() {
		
	}
	
	/**
	 * Set new activation function for all neurons of the layer. The methods
	 * sets new activation function for each neuron by setting their
	 * ActivationNeuron.ActivationFunction property.
	 * 
	 * @param function
	 *            Activation function to set.
	 */
	public void setActivationFunction(IActivationFunction function) {
		for (int i = 0; i < neurons.length; i++) {
			((ActivationNeuron) neurons[i]).setActivationFunction(function);
		}
	}
}
