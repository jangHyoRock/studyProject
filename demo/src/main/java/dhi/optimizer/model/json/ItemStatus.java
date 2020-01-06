package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;

/*
 * Item status json model dependent on the overview screen.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemStatus {

	private String item;
	
	private String status;
	
	private String unit;
	
	private String current;
	
	private String target;
	
	private Number before;
	
	private Number after;
	
	private String impact;
	
	private String baseline;
					
	public ItemStatus() {}
	
	public ItemStatus(String item, String status) {
		this.item = item;
		this.status = status;
	}

	public ItemStatus(String item, String unit, String current) {
		this.item = item;
		this.unit = unit;
		this.current = current;
	}
	
	public String getItem() {
		return this.item;
	}
	
	public void setItem(String item ) {
		this.item = item;
	}

	public String getStatus() {
		return this.status;
	}
	
	public void setStatus(String status ) {
		this.status = status;
	}	
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getCurrent() {
		return this.current;
	}
	
	public void setCurrent(String current ) {
		this.current = current;
	}
	
	public String getTarget() {
		return this.target;
	}
	
	public void setTarget(String target ) {
		this.target = target;
	}

	public Number getBefore() {
		return before;
	}

	public void setBefore(Number before) {
		this.before = before;
	}

	public Number getAfter() {
		return after;
	}

	public void setAfter(Number after) {
		this.after = after;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getBaseline() {
		return baseline;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}
}
