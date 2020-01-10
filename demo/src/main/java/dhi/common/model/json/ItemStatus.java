package dhi.common.model.json;
import com.fasterxml.jackson.annotation.JsonInclude;

/*
 * ItemStatus json model of common API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemStatus {

	private String item;
	
	private String current;
	
	private String unit;

	public ItemStatus(String item, String unit, String current)
	{
		this.item = item;		
		this.unit = unit;
		this.current = current;
	}
	
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
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
