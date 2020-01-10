package dhi.optimizer.algorithm.common;

import java.io.Serializable;

/**
 * Represents an integer range with minimum and maximum values. The class
 * represents an integer range with inclusive limits both minimum and maximum
 * values of the range are included into it. Mathematical notation of such range
 * is [min, max]
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public class IntRange implements Serializable { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6159355132548364697L;

	/** Minimum value of the range. */
	private int min = 0;

	/** Maximum value of the range. */
	private int max = 0;

	/**
	 * Initializes a new instance of the class
	 * 
	 * @param min
	 *            Minimum value of the range.
	 * @param max
	 *            Maximum value of the range.
	 */
	public IntRange(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Get length of the range.
	 * 
	 * @return difference between maximum and minimum values.
	 */
	public int getLength() {
		int length = 0;
		length = max - min;

		return length;
	}

	/**
	 * Check if the specified value is inside of the range.
	 * 
	 * @param x
	 *            Value to check.
	 * @return if the specified range is inside of the range then return true or
	 *         otherwise return false
	 */
	public boolean isInside(int x) {
		boolean isInside = false;
		isInside = (x >= min) && (x <= max);

		return isInside;
	}

	/**
	 * Check if the specified range overlaps with the range.
	 * 
	 * @param range
	 *            Range to check.
	 * @return if the specified range is inside of the range then return true or
	 *         otherwise return false
	 */
	public boolean isInside(IntRange range) {
		boolean isInside = false;
		isInside = (isInside(range.min)) && (isInside(range.max));

		return isInside;
	}

	/**
	 * Check if the specified range overlaps with the range.
	 * 
	 * @param range
	 *            Range to check for overlapping.
	 * @return if the specified range overlaps with the range then return true
	 *         or otherwise return false
	 */
	public boolean isOverlapping(IntRange range) {
		boolean isOverapped = false;
		isOverapped = (isInside(range.min)) || (isInside(range.max))
				|| (range.isInside(min)) || (range.isInside(max));

		return isOverapped;
	}

	/**
	 * Convert the single precision range to integer range.
	 * 
	 * @param provideInnerRange
	 *            Specifies if inner integer range must be returned or outer
	 *            range.
	 * @return Returns integer version of the range.
	 */
	public IntRange toIntRange(boolean provideInnerRange) {
		IntRange intRange = null;
		intRange = new IntRange(min, max);

		return intRange;
	}

	/**
	 * Get string representation of the class.
	 * 
	 * @return Returns string, which contains min/max values of the range in
	 *         readable form.
	 */
	public String toString() {
		String tmp = String.format("%d, %d", min, max);
		return tmp;
	}
}
