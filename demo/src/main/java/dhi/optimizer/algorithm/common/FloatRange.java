package dhi.optimizer.algorithm.common;

/**
 * Represents a range with minimum and maximum values, which are single
 * precision numbers (floats). The class represents a single precision range
 * with inclusive limits - both minimum and maximum values of the range are
 * included into it. Mathematical notation of such range is [min, max]
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 * @version 1.0
 */

public class FloatRange { 

	/** Minimum value of the range. */
	private float min = 0.0f;

	/** Maximum value of the range. */
	private float max = 0.0f;

	/**
	 * Initializes a new instance of the class
	 * 
	 * @param min
	 *            Minimum value of the range.
	 * @param max
	 *            Maximum value of the range.
	 */
	public FloatRange(float min, float max) {
		this.min = min;
		this.max = max;
	}

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	/**
	 * Get length of the range.
	 * 
	 * @return difference between maximum and minimum values.
	 */
	public float getLength() {
		float length = 0.0f;
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
	public boolean isInside(float x) {
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
	public boolean isInside(FloatRange range) {
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
	public boolean isOverlapping(FloatRange range) {
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
		int min = 0;
		int max = 0;
		IntRange intRange = null;

		if (provideInnerRange) {
			min = (int) Math.ceil(min);
			max = (int) Math.floor(max);
		} else {
			min = (int) Math.floor(min);
			max = (int) Math.ceil(max);
		}

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
		String tmp = String.format("%f, %f", min, max);
		return tmp;
	}
}
