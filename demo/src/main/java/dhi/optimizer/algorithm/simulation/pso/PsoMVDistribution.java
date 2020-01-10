package dhi.optimizer.algorithm.simulation.pso;

public class PsoMVDistribution {

	private double rangeMin;

	private double rangeMax;

	private long count;

	public double getRangeMin() {
		return rangeMin;
	}

	public void setRangeMin(double rangeMin) {
		this.rangeMin = rangeMin;
	}

	public double getRangeMax() {
		return rangeMax;
	}

	public void setRangeMax(double rangeMax) {
		this.rangeMax = rangeMax;
	}

	public long getCount() {
		return this.count;
	}

	public void increase() {
		this.count++;
	}
}
