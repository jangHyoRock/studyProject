package dhi.optimizer.algorithm.neuralNet;

/**
 * Activation function interface. All activation functions, which are supposed
 * to be used with neurons, which calculate their output as a function of
 * weighted sum of their inputs, should implement this interfaces.
 * 
 * @author jeeun.moon 
 * @since 2015.03.30
 * @version 1.0
 */

public interface IActivationFunction {
	/**
	 * Calculates function value. The method calculates function value at point
	 * 'x'.
	 * 
	 * @param x
	 *            Function input value.
	 * @return Function output value, f(x).
	 */
	double calcFunction(double x);

	/**
	 * Calculates function derivative. The method calculates function derivative
	 * at point 'x'.
	 * 
	 * @param x
	 *            Function input value.
	 * @return Function derivative, f'(x).
	 */
	double calDerivative(double x);

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
	double calDerivativeWithOutput(double y);
}
