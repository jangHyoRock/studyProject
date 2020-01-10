package dhi.common.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;

/*
 * Item status json model dependent on the overview screen.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeyValue {
	
	private String key;

	private String value;

	public KeyValue() {	};

	public KeyValue(String key, String value) {
		this.key = key;
		this.value = value;
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
}
