package dhi.optimizer.algorithm.neuralNet;

import java.io.Serializable;

/**
 * Bipolar sigmoid activation function. The class represents bipolar sigmoid
 * activation function with the next expression:
 *                2
 * f(x) = ------------------ - 1
 *        1 + exp(-alpha * x)
 *
 *           2 * alpha * exp(-alpha * x )
 * f'(x) = -------------------------------- = alpha * (1 - f(x)^2) / 2
 *           (1 + exp(-alpha * x))^2
 *           
 * Output range of the function: [-1, 1]
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */ 

public class BipolarSigmoidFunction implements IActivationFunction, Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1794650394407894444L;
	
	/**
	 * sigmoid's alpha value The value determines steepness of the function.
	 * Increasing value of this property changes sigmoid to look more like a
	 * threshold function. Decreasing value of this property makes sigmoid to be
	 * very smooth (slowly growing from its minimum value to its maximum value).
	 * Default value is set to 1.</b>
	 */
	private double alpha = 1.0;

	/**
	 * Initializes a new instance of the BipolarSigmoidFunction class.
	 */
	public BipolarSigmoidFunction() {

	}

	/**
	 * Initializes a new instance of the BipolarSigmoidFunction class.
	 * 
	 * @param alpha
	 *            Sigmoid's alpha value.
	 */
	public BipolarSigmoidFunction(double alpha) {
		this.alpha = alpha;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
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
		double tmp;
		tmp = (2.0 / (1.0 + Math.exp(-alpha * x))) - 1.0;
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
		double y = calcFunction(x);
		double tmp = alpha * (1.0 - y * y) / 2.0;

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
		double tmp = alpha * (1.0 - y * y) / 2.0;
		return tmp;
	}
}
