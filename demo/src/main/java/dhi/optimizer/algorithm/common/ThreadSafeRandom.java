package dhi.optimizer.algorithm.common;

import java.util.Random;

/**
 * Thread safe version of the java.util.Random class The class inherits the
 * java.util.Random and overrides its random numbers generation methods
 * providing thread safety by guarding call to the base class with a lock. See
 * documentation to java.util.Random for additional information about the base
 * class.
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 * @see java.util.Random
 */
 
public class ThreadSafeRandom extends Random {
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes a new instance of the class
	 */
	public ThreadSafeRandom() {
		super();
	}

	/**
	 * Initializes a new instance of the class
	 */
	public ThreadSafeRandom(int seed) {
		super(seed);
	}

	/**
	 * Returns a nonnegative random number.
	 * 
	 * @return Returns a 32-bit signed integer greater than or equal to zero and
	 *         less than Integer.MAX_VALUE
	 * @see java.util.Random.nextInt()
	 * @see Integer.MAX_VALUE
	 */
	@Override
	public int nextInt() {
		synchronized (this) {
			int value = 0;
			value = super.nextInt();
			return value;
		}
	}

	/**
	 * Returns a nonnegative random number less than the specified maximum.
	 * 
	 * @param maxValue
	 *            The exclusive upper bound of the random number to be
	 *            generated. must be greater than or equal to zero.
	 * @return Returns a 32-bit signed integer greater than or equal to zero and
	 *         less than maxValue that is, the range of return values ordinarily
	 *         includes zero but not maxValue.
	 * @see java.util.Random.nextInt(int)
	 */
	@Override
	public int nextInt(int maxValue) {
		synchronized (this) {
			int value = 0;
			value = super.nextInt(maxValue);
			return value;
		}
	}

	public int nextInt(int minValue, int maxValue) {
		synchronized (this) {
			int value = 0;
			value = super.nextInt(maxValue - minValue) + minValue;
			return value;
		}
	}

	/**
	 * Fills the elements of a specified array of bytes with random numbers.
	 * 
	 * @param buffer
	 *            An array of bytes to contain random numbers.
	 * @see java.util.Random.nextBytes(byte[])
	 */
	@Override
	public void nextBytes(byte[] buffer) {
		synchronized (this) {
			super.nextBytes(buffer);
		}
	}

	/**
	 * Returns a random number between 0.0 and 1.0.
	 * 
	 * @return Returns a double-precision floating point number greater than or
	 *         equal to 0.0, and less than 1.0.
	 * @see java.util.Random.nextDouble()
	 */
	@Override
	public double nextDouble() {
		synchronized (this) {
			double value = 0;
			value = super.nextDouble();
			return value;
		}
	}
}
