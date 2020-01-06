package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;

/**
 * Rectifier Linear activation function. The class represents Rectifier Linear
 * activation function with the next expression:
 * f(x) = max(0, x)
 * f'(x) = if x < 0 then 0, else if x > 0 then 1
 * 
 * @author jeeun.moon
 * @since 2015.03.30 
 * @version 1.0
 */

public class RectifierLinearFunction implements IActivationFunction, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8127631409704818252L;

	/**
	 * Initializes a new instance of the RectifierLinearFunction class.
	 */
	public RectifierLinearFunction() {
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
		double tmp = Math.max(0.0, x);
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
		double tmp = (x >= 0.0) ? 1.0 : 0.0;
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
		double tmp = calDerivative(y);
		return tmp;
	}
}
