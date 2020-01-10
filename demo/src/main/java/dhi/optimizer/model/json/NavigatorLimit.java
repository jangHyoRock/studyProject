package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NavigatorLimit {

	private boolean use;
	
	private String item;
	
	private String title;
	
	private Number high;
	
	private Number margin;
	
	private String unit;
	
	private String type;

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Number getHigh() {
		return high;
	}

	public void setHigh(Number high) {
		this.high = high;
	}

	public Number getMargin() {
		return margin;
	}

	public void setMargin(Number margin) {
		this.margin = margin;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
