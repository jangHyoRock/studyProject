package dhi.optimizer.algorithm.common;

/**
 * LearningDataSet
 * 
 * @author jeeun.moon
 * @since 2015.03.30
 */

public class LearningDataSet {

	public static final double defaultNormalizeScaleX = 2.0f;
	public static final double defaultNormalizeScaleY = 1.7f; 
	private double[][] dataItems = null;
	private double[][] normalizedDataItems = null;
	private int cols = 0;
	private int rows = 0;
	private NormalizeInfo normalizeInfo = null;

	/**
	 * Initializes a new instance of the class
	 * 
	 * @param normalizeScale
	 *            Normalize scale range
	 */
	public LearningDataSet(double normalizeScale) {
//		this.normalizeScale = normalizeScale;
//		this.normalizeScaleHalf = normalizeScale / 2.0f;
		
		normalizeInfo = new NormalizeInfo(normalizeScale);
	}

	public double[][] getDataItems() {
		return dataItems;
	}

	public void setDataItems(double[][] items) {
		this.dataItems = items;
	}

	public double getDataItem(int row, int col) {
		double temp = dataItems[row][col];
		return temp;
	}

	public void setDataItem(int row, int col, float val) {
		this.dataItems[row][col] = val;
	}

	public double[][] getNormalizedDataItems() {
		return normalizedDataItems;
	}

	public void setNormalizedDataItems(double[][] normalizedData) {
		this.normalizedDataItems = normalizedData;
	}

	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}

	public void initDataSet(double[][] dataItems) {

		if (dataItems != null) {
			if (dataItems.length != 0) {
				rows = dataItems.length;
				cols = dataItems[0].length;

				this.dataItems = new double[rows][cols];

				for (int i = 0; i < rows; i++) {
					System.arraycopy(dataItems[i], 0, this.dataItems[i], 0,
							cols);
				}

				initValueRange();

				normalize();
			}
		}
	}

	public void initValueRange() {
		double value = 0.0f;
		double minValue = 0.0f;
		double maxValue = 0.0f;
		DoubleRange[] ranges = new DoubleRange[cols];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (i == 0) {
					ranges[j] = new DoubleRange(Double.MAX_VALUE,
							-Double.MAX_VALUE);
				}

				// search for minimum value
				value = dataItems[i][j];
				minValue = ranges[j].getMin();
				maxValue = ranges[j].getMax();
				if (value < minValue) {
					ranges[j].setMin(value);
				}

				// search for max value
				if (value > maxValue) {
					ranges[j].setMax(value);
				}
			}
		}

		normalizeInfo.setRanges(ranges);
	}

	public void normalize() {
		normalizedDataItems = new double[rows][];	
		for (int i = 0; i < rows; i++) {
			normalizedDataItems[i] = normalizeInfo.normalizeValues(dataItems[i]);
		}		
	}
	
	public void deNormalize() {
		for (int i = 0; i < rows; i++) {
			dataItems[i] = normalizeInfo.deNormalizeValues(normalizedDataItems[i]);
		}		
	}
	
	public void copyNormalizedDataItems(int rowIndex, double[] normalizedData) {
		System.arraycopy(
				normalizedData,
				0,
				this.normalizedDataItems[rowIndex],
				0, cols);		
	}	
	
	public NormalizeInfo getNormalizeInfo() {
		return normalizeInfo;
	}

	public void setNormalizeInfo(NormalizeInfo normalizeInfo) {
		this.normalizeInfo = normalizeInfo;
	}	
}