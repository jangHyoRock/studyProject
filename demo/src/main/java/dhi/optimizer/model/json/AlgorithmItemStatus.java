package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlgorithmItemStatus {

	private String group;
	
	private String item;
	
	private String status;
	
	private String unit;
	
	private String value;
	
	private String timestamp;
	
	private String baseline;
	
	private String current;
	
	private String before;
	
	@JsonProperty("after_nn")
	private String afterNN;
	
	private String after;
	
	@JsonProperty("before_rate")
	private String beforeRate;
	
	@JsonProperty("after_nn_rate")
	private String afterNNRate;

	@JsonProperty("after_rate")
	private String afterRate;
	
	@JsonProperty("before_left")
	private String beforeLeft;
	
	@JsonProperty("before_right")
	private String beforeRight;
	
	@JsonProperty("after_nn_left")
	private String afterNNLeft;
	
	@JsonProperty("after_nn_right")
	private String afterNNRight;
	
	@JsonProperty("after_left")
	private String afterLeft;
	
	@JsonProperty("after_right")
	private String afterRight;

	private String impact;
	
	private String min;
	
	private String max;
	
	private String target;
	
	private String bias;

	public AlgorithmItemStatus() {
	};

	public AlgorithmItemStatus(String item, String current) {
		this.item = item;
		this.current = current;
	}
	
	public AlgorithmItemStatus(String item, String value, String timestamp) {
		this.item = item;
		this.value = value;
		this.timestamp = timestamp;
	}
		
	public AlgorithmItemStatus(String group, String item, String value, String status) {
		this.group = group;
		this.item = item;
		this.value = value;
		this.status = status;
	};
	
	public AlgorithmItemStatus(String item, String status, String min, String bias, String target, String max) {		
		this.item = item;
		this.status = status;
		this.min = min;
		this.bias = bias;
		this.target = target;
		this.max = max;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getBaseline() {
		return baseline;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getAfterNN() {
		return afterNN;
	}

	public void setAfterNN(String afterNN) {
		this.afterNN = afterNN;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	public String getBeforeRate() {
		return beforeRate;
	}

	public void setBeforeRate(String beforeRate) {
		this.beforeRate = beforeRate;
	}

	public String getAfterNNRate() {
		return afterNNRate;
	}

	public void setAfterNNRate(String afterNNRate) {
		this.afterNNRate = afterNNRate;
	}

	public String getAfterRate() {
		return afterRate;
	}

	public void setAfterRate(String afterRate) {
		this.afterRate = afterRate;
	}

	public String getBeforeLeft() {
		return beforeLeft;
	}

	public void setBeforeLeft(String beforeLeft) {
		this.beforeLeft = beforeLeft;
	}

	public String getBeforeRight() {
		return beforeRight;
	}

	public void setBeforeRight(String beforeRight) {
		this.beforeRight = beforeRight;
	}

	public String getAfterNNLeft() {
		return afterNNLeft;
	}

	public void setAfterNNLeft(String afterNNLeft) {
		this.afterNNLeft = afterNNLeft;
	}

	public String getAfterNNRight() {
		return afterNNRight;
	}

	public void setAfterNNRight(String afterNNRight) {
		this.afterNNRight = afterNNRight;
	}

	public String getAfterLeft() {
		return afterLeft;
	}

	public void setAfterLeft(String afterLeft) {
		this.afterLeft = afterLeft;
	}

	public String getAfterRight() {
		return afterRight;
	}

	public void setAfterRight(String afterRight) {
		this.afterRight = afterRight;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getBias() {
		return bias;
	}

	public void setBias(String bias) {
		this.bias = bias;
	};
}
