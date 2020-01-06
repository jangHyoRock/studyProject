package dhi.optimizer.algorithm.common;

import java.io.Serializable;

/**
 * LearningDataSet
 * 
 * @author jeeun.moon
 * @since 2015.06.04
 */

public class NormalizeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7961892878053992836L;

	private double normalizeScale = 2.0f; // Normalize Scale
	private double normalizeScaleHalf = 1.0f; // Normalize Scale 50%
	private double[] scaleFactors = null;
	private DoubleRange[] ranges = null;

	// private double[] datas = null;
	// private double[] normalizedDatas = null;

	/**
	 * Initializes a new instance of the class
	 * 
	 * @param normalizeScale
	 *            Normalize scale range
	 */
	public NormalizeInfo(double normalizeScale) {
		this.normalizeScale = normalizeScale;
		this.normalizeScaleHalf = normalizeScale / 2.0f;
	}

	public NormalizeInfo() {

	}

	public double getNormalizeScale() {
		return normalizeScale;
	}

	public void setNormalizeScale(double normalizeScale) {
		this.normalizeScale = normalizeScale;
	}

	public double getNormalizeScaleHalf() {
		return normalizeScaleHalf;
	}

	public void setNormalizeScaleHalf(double normalizeScaleHalf) {
		this.normalizeScaleHalf = normalizeScaleHalf;
	}

	public double[] getScaleFactors() {
		return scaleFactors;
	}

	public void setScaleFactors(double[] scaleFactors) {
		this.scaleFactors = scaleFactors;
	}

	public DoubleRange[] getRanges() {
		return ranges;
	}

	public void setRanges(DoubleRange[] ranges) {
		this.ranges = ranges;

		int cols = ranges.length;
		scaleFactors = new double[cols];
		for (int j = 0; j < cols; j++) {
			// data transformation factor
			double length = ranges[j].getLength() != 0 ? ranges[j].getLength()
					: ObjectiveFunction.minimumZero;
			scaleFactors[j] = normalizeScale / length;
		}		
	}

	public double[] normalizeValues(double[] vals) {
		int cols = vals.length;
		double[] normalizedVals = new double[cols];
		for (int j = 0; j < cols; j++) {
			// data transformation factor
			double length = ranges[j].getLength() != 0 ? ranges[j].getLength()
					: ObjectiveFunction.minimumZero;
			scaleFactors[j] = normalizeScale / length;
			normalizedVals[j] = (vals[j] - ranges[j].getMin())
					* scaleFactors[j] - normalizeScaleHalf;
		}

		return normalizedVals;
	}

	public double[] deNormalizeValues(double[] normalizedVals) {
		int cols = normalizedVals.length;
		double[] vals = new double[cols];
		for (int j = 0; j < cols; j++) {
			vals[j] = (normalizedVals[j] + normalizeScaleHalf)
					/ scaleFactors[j] + ranges[j].getMin();
		}

		return vals;
	}
	
	public NormalizeInfo clone() {		
		NormalizeInfo obj = new NormalizeInfo();
	
		obj.setNormalizeScale(normalizeScale);
		obj.setNormalizeScaleHalf(normalizeScaleHalf);
		obj.setRanges(ranges.clone());
		
		return obj;
	}
}