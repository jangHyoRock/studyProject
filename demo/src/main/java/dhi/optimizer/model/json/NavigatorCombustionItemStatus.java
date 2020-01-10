package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NavigatorCombustionItemStatus {

	@JsonProperty("item_id")
	private String itemId;
	
	@JsonProperty("item_nm")
	private String itemName;
	
	private String yaw;
	
	private String pos;
	
	private String fde;
	
	private String flow;
	
	@JsonProperty("flow_unit")
	private String flowUnit;
		
	public NavigatorCombustionItemStatus() {};
	
	public NavigatorCombustionItemStatus(String itemId, String itemName, String flowUnit, String flow) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.flowUnit = flowUnit;
		this.flow = flow;
	};

	public NavigatorCombustionItemStatus(String itemId, String itemName, String yaw, String pos, String fde) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.yaw = yaw;
		this.pos = pos;
		this.fde = fde;
	}	

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getYaw() {
		return yaw;
	}

	public void setYaw(String yaw) {
		this.yaw = yaw;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getFde() {
		return fde;
	}

	public void setFde(String fde) {
		this.fde = fde;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getFlowUnit() {
		return flowUnit;
	}

	public void setFlowUnit(String flowUnit) {
		this.flowUnit = flowUnit;
	}
}
