package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * RHSpraySupplyStatus parts area json model for CombustionStatus class.
 */
public class RHSpraySupplyStatus {
	
	private String item;
	
	@JsonProperty("tag_id")
	private String tagId;
	
	private String current;
	
	private String unit;

	public RHSpraySupplyStatus() {};
	
	public RHSpraySupplyStatus(String item, String tagId, String current, String unit)
	{
		this.item = item;
		this.tagId = tagId;
		this.current = current;
		this.unit = unit;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
