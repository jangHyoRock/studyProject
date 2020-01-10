package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;

/*
 * Item status json model dependent on the overview screen.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyValue {
	
	private String key;

	private String value;
	
	private double doubleValue;

	public KeyValue() {
	};

	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public KeyValue(String key, double doubleValue) {
		this.key = key;
		this.doubleValue = doubleValue;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}
}
