package dhi.optimizer.algorithm.simulation.pso;

public class PsoTag {

	private String id;
	
	private double min;

	private double max;

	private int index;

	private boolean isZeroPlate;

	public PsoTag(String id, int index, boolean isZeroPlate) {
		this.id = id;
		this.index = index;
		this.isZeroPlate = isZeroPlate;
	}

	public PsoTag(String id, double min, double max, int index) {
		this.id = id;
		this.min = min;
		this.max = max;
		this.index = index;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isZeroPlate() {
		return isZeroPlate;
	}

	public void setZeroPlate(boolean isZeroPlate) {
		this.isZeroPlate = isZeroPlate;
	}

	public double valueOfRange(double targetValue) {
		double value = targetValue;
		if (targetValue > this.max) {
			value = this.max;
		} else if (targetValue < this.min) {
			value = this.min;
		}

		return value;
	}
}
