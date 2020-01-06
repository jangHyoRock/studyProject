package dhi.common.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Config item model of setting API.
 */
public class ConfigItem {
		
	@JsonProperty("tag_id")
	private String tagId;
	
	private String item;
	
	private double current;
	
	private String unit;
	
	public ConfigItem() {}
	
	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public double getCurrent() {
		return current;
	}

	public void setCurrent(double current) {
		this.current = current;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	};
}
