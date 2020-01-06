package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;
import java.util.List;


/**
 * Activation network. Activation network is a base for multi-layer neural
 * network with activation functions. It consists of activation layers
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public class ActivationNetwork extends Network implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2992234235837899465L;

	/** 
	 * Initializes a new instance of the ActivationNetwork class The new network
	 * will be randomized(ActivationNeuron.randomizeWeights method) after it is
	 * created.
	 * 
	 * @param inputsCount
	 *            Network's inputs count
	 * @param neuronsInLayers
	 *            ArrayList, which specifies the amount of neurons in each layer
	 *            of the neural network
	 * @param actvationFunctions
	 *            ArrayList, Activation function of neurons of the network
	 */
	public ActivationNetwork(int inputsCount, List<Integer> neuronsInLayers,
			List<IActivationFunction> actvationFunctions) {
		super(inputsCount, neuronsInLayers.size());

		// create each layer
		for (int i = 0; i < neuronsInLayers.size(); i++) {
			int inputs = (i == 0) ? inputsCount : neuronsInLayers.get(i - 1);
			layers[i] = new ActivationLayer(neuronsInLayers.get(i), inputs,
					actvationFunctions.get(i));
		}
	}

	public ActivationNetwork() {
		
	}
	
	/**
	 * Set new activation function for all neurons of the network. The method
	 * sets new activation function for all neurons by calling
	 * ActivationLayer.SetActivationFunction method for each layer of the
	 * network.
	 * 
	 * @param function
	 *            Activation function to set.
	 */
	public void setActivationFunction(IActivationFunction function) {
		for (int i = 0; i < layers.length; i++) {
			((ActivationLayer) layers[i]).setActivationFunction(function);
		}
	}
}
