package dhi.optimizer.algorithm.pso;

import java.util.List;

public class PsoMV {

	public enum MVType {
		Burner, OFA, Air
	};

	private String name;

	private MVType mvType;

	private double min;

	private double max;

	private PsoTag[] inputTagList;

	private double[] values;

	private double[] bestMV;

	private double[] velocity;

	private double[] spreadData;
	
	private List<PsoMVDistribution> psoMVDistributionList;
	
	public PsoMV() {}

	public PsoMV(String name, MVType mvType, double min, double max) {
		this.name = name;
		this.mvType = mvType;
		this.min = min;
		this.max = max;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MVType getMvType() {
		return mvType;
	}

	public void setMvType(MVType mvType) {
		this.mvType = mvType;
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

	public PsoTag[] getInputTagList() {
		return inputTagList;
	}

	public void setInputTagList(PsoTag[] inputTagList) {
		this.inputTagList = inputTagList;
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public double[] getVelocity() {
		return velocity;
	}

	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	public double[] getBestMV() {
		return bestMV;
	}

	public void setBestMV(double[] bestMV) {
		this.bestMV = bestMV;
	}

	public double[] getSpreadData() {
		return spreadData;
	}

	public void setSpreadData(double[] spreadData) {
		this.spreadData = spreadData;
	}

	public void makeSpreadDatas(int size) {
		this.spreadData = new double[size];
		double spaceSize = (this.max - this.min) / (double) (size - 1);

		for (int i = 0; i < size; i++) {
			spreadData[i] = this.min + (spaceSize * i);
		}
	}

	public void setValuesToSpreadData(int valueIndex, int spreadIndex) {
		this.values[valueIndex] = this.spreadData[spreadIndex];
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

	public List<PsoMVDistribution> getPsoMVDistributionList() {
		return psoMVDistributionList;
	}

	public void setPsoMVDistributionList(List<PsoMVDistribution> psoMVDistributionList) {
		this.psoMVDistributionList = psoMVDistributionList;
	}
}
