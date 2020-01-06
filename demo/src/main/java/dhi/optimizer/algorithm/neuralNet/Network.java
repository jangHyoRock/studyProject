package dhi.optimizer.algorithm.neuralNet;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import dhi.optimizer.algorithm.common.NormalizeInfo;



/**
 * abstract neural network class. This is a base neural network class, which
 * represents collection of neurons's layers.
 * 
 * @author jeeun.moon
 * @since 2015.03.30 
 * @version 1.0
 */

public abstract class Network implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -491331529714936939L;

	/**
	 * Network's inputs count.
	 */
	protected int inputsCount;

	/**
	 * Network's layers count.
	 */
	protected int layersCount;

	/**
	 * Network's layers.
	 */
	protected Layer[] layers;

	/**
	 * Network's output vector. The calculation way of network's output vector
	 * is determined by layers, which comprise the network The property is not
	 * initialized(equals to null) until computeOutput method is called.
	 */
	protected double[] output;
		
	protected NormalizeInfo inputNormalizeInfo = null;

	protected NormalizeInfo targetNormalizeInfo = null;

	/**
	 * Initializes a new instance of the Network class. Protected constructor,
	 * which initializes inputsCount, layersCount and layers members.
	 * 
	 * @param inputsCount
	 *            Network's inputs count.
	 * @param layersCount
	 *            Network's layers count.
	 */
	protected Network(int inputsCount, int layersCount) {
		this.inputsCount = Math.max(1, inputsCount);
		this.layersCount = Math.max(1, layersCount);
		// create collection of layers
		this.layers = new Layer[this.layersCount];
	}
	
	protected Network() {
		
	}

	public int getInputsCount() {
		return inputsCount;
	}

	public void setInputsCount(int inputsCount) {
		this.inputsCount = inputsCount;
	}

	public int getLayersCount() {
		return layersCount;
	}

	public void setLayersCount(int layersCount) {
		this.layersCount = layersCount;
	}	
	
	public Layer[] getLayers() {
		return layers;
	}

	public void setLayers(Layer[] layers) {
		this.layers = layers;
	}	
	
	public double[] getOutput() {
		return output;
	}
	
	public void setOutput(double[] output) {
		this.output = output;
	}
	
	public NormalizeInfo getInputNormalizeInfo() {
		return inputNormalizeInfo;
	}

	public void setInputNormalizeInfo(NormalizeInfo inputNormalizeInfo) {
		this.inputNormalizeInfo = inputNormalizeInfo;
	}

	public NormalizeInfo getTargetNormalizeInfo() {
		return targetNormalizeInfo;
	}

	public void setTargetNormalizeInfo(NormalizeInfo targetNormalizeInfo) {
		this.targetNormalizeInfo = targetNormalizeInfo;
	}	
	
	/**
	 * Compute output vector of the network. The actual network's output vector
	 * is determined by layers, which comprise the layer - represents an output
	 * vector of the last layer of the network. The output vector is also stored
	 * in Output property. The method may be called safely from multiple threads
	 * to compute network's output value for the specified input values.
	 * However, the value of Output property in multi-threaded environment is
	 * not predictable, since it may hold network's output computed from any of
	 * the caller threads. Multi-threaded access to the method is useful in
	 * those cases when it is required to improve performance by utilizing
	 * several threads and the computation is based on the immediate return
	 * value of the method, but not on network's output property.
	 * 
	 * @param input
	 *            Normalized Input vector.
	 * @return Returns network's Normalized output vector.
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
		double[] output = input;

		// compute each layer
		for (int i = 0; i < layers.length; i++) {
			output = layers[i].computeOutput(output);
		}

		// assign output property as well (works correctly for single threaded
		// usage)
		this.output = output;

		return output;
	}

	public double[] calcOutput(double[] input) {
		double[] normalizedInput = null;
		double[] normalizedOutput = null;
		double[] deNormalizedOutput = null;		
		normalizedInput = inputNormalizeInfo.normalizeValues(input);
		
		try {
			normalizedOutput = computeOutput(normalizedInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		deNormalizedOutput = targetNormalizeInfo.deNormalizeValues(normalizedOutput);
		
		return deNormalizedOutput;		
	}

	/**
	 * Randomize layers of the network. Randomizes network's layers by calling
	 * Layer.randomizeWeights method of each layer.
	 */
	public void randomizeWeights() {
		for (int i = 0; i < layers.length; i++) {
			layers[i].randomizeWeights();
		}
	}
	
	public void saveToXml(String fileName) {
        XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
            encoder.writeObject(this);
            encoder.close();		
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Network LoadFromXml(String fileName) {
        XMLDecoder decoder = null;
        Network obj = null;
        try {
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(fileName)));
			obj = (Network)decoder.readObject();
            decoder.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return obj;
	}
}
