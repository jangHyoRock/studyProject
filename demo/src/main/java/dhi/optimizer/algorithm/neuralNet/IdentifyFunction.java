package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;

/**
 * Identify activation function. The class represents identify
 * activation function with the next expression:
 * f(x) = x
 * f'(x) = 1
 * 
 * @author jeeun.moon 
 * @since 2015.03.30
 * @version 1.0
 */

public class IdentifyFunction implements IActivationFunction, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 762758760884163524L;

	/**
	 * Initializes a new instance of the IdentifyFunction class.
	 */
	public IdentifyFunction() {
	}

	/**
	 * Calculates function value. The method calculates function value at point
	 * 'x'.
	 * 
	 * @param x
	 *            Function input value.
	 * @return Function output value, f(x).
	 */
	@Override
	public double calcFunction(double x) {
		double tmp = x;
		return tmp;
	}

	/**
	 * Calculates function derivative. The method calculates function derivative
	 * at point 'x'.
	 * 
	 * @param x
	 *            Function input value.
	 * @return Function derivative, f'(x).
	 */
	@Override
	public double calDerivative(double x) {
		double tmp = 1.0;
		return tmp;
	}

	/**
	 * Calculates function derivative. The method calculates the same derivative
	 * value as the Derivative method, but it takes not the input 'x' value
	 * itself, but the function value, which was calculated previously with the
	 * help of Function method. Some applications require as function value, as
	 * derivative value, so they can save the amount of calculations using this
	 * method to calculate derivative.
	 * 
	 * @param y
	 *            Function output value - the value, which was obtained with the
	 *            help of Function method.
	 * @return Function derivative, f'(x).
	 */
	@Override
	public double calDerivativeWithOutput(double y) {
		double tmp = 1.0;
		return tmp;
	}
}
