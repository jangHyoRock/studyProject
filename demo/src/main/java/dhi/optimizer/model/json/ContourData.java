package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Contour parts area json model for combustionDynamics class.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContourData {
	
	private Number[] x;
	
	private Number[] y;
	
	private Number[][] z;
	
	private String message;
	
	@JsonProperty("fot_rate")
	private String fotRate;
	
	@JsonProperty("mean_value")
	private String meanValue;
	
	@JsonProperty("max_value")
	private String maxValue;
	
	@JsonProperty("min_value")
	private String minValue;
		
	@JsonProperty("left_mean")
	private String leftMean;
	
	@JsonProperty("right_mean")
	private String rightMean;
	
	@JsonProperty("standard_deviation")
	private String standardDeviation;
	
	public ContourData (Number[] x, Number[] y, Number[][] z) {		
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Number[] getX() {
		return x;
	}

	public void setX(Number[] x) {
		this.x = x;
	}

	public Number[] getY() {
		return y;
	}

	public void setY(Number[] y) {
		this.y = y;
	}

	public Number[][] getZ() {
		return z;
	}

	public void setZ(Number[][] z) {
		this.z = z;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFotRate() {
		return fotRate;
	}

	public void setFotRate(String fotRate) {
		this.fotRate = fotRate;
	}

	public String getMeanValue() {
		return meanValue;
	}

	public void setMeanValue(String meanValue) {
		this.meanValue = meanValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getLeftMean() {
		return leftMean;
	}

	public void setLeftMean(String leftMean) {
		this.leftMean = leftMean;
	}

	public String getRightMean() {
		return rightMean;
	}

	public void setRightMean(String rightMean) {
		this.rightMean = rightMean;
	}

	public String getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(String standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
}
