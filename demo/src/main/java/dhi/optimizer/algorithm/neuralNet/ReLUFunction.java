package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;

public class ReLUFunction implements IActivationFunction, Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes a new instance of the ReLUFunction class.
	 */
	public ReLUFunction() {

	}

	/**
	 * Calculates function value. The method calculates function value at point
	 * 'x'.
	 * 
	 * @param x
	 *            Function input value.
	 * @return Function output value, f(x).
	 */
	//@Override
	public double calcFunction(double x) {		 
		return x < 0 ? 0 : x;
	}

	@Override
	public double calDerivative(double x) {
		// return x < 0 ? 0 : x;
		return x < 0 ? 0 : 1;
	}

	@Override
	public double calDerivativeWithOutput(double y) {
		// return y < 0 ? 0 : y;
		return y < 0 ? 0 : 1;
	}
}
