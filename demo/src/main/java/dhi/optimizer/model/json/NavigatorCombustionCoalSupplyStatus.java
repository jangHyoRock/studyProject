package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigatorCombustionCoalSupplyStatus {

	@JsonProperty("item_id")
	private String itemId;
	
	@JsonProperty("item_nm")
	private String itemName;
	
	private String status;
	
	private String flow;
	
	@JsonProperty("flow_unit")
	private String flowUnit;
	
	private String yaw1;
	
	private String dir1;
	
	private String yaw2;
	
	private String dir2;
	
	private String yaw3;
	
	private String dir3;
	
	private String yaw4;
	
	private String dir4;
	
	public NavigatorCombustionCoalSupplyStatus() {};

	public NavigatorCombustionCoalSupplyStatus(String itemId, String itemName, String status, String flow, String flowUnit, String yaw1, String dir1,
			String yaw2, String dir2, String yaw3, String dir3, String yaw4, String dir4) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.status = status;
		this.flow = flow;
		this.flowUnit = flowUnit;
		this.yaw1 = yaw1;
		this.dir1 = dir1;
		this.yaw2 = yaw2;
		this.dir2 = dir2;
		this.yaw3 = yaw3;
		this.dir3 = dir3;
		this.yaw4 = yaw4;
		this.dir4 = dir4;
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

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFlowUnit() {
		return flowUnit;
	}

	public void setFlowUnit(String flowUnit) {
		this.flowUnit = flowUnit;
	}

	public String getYaw1() {
		return yaw1;
	}

	public void setYaw1(String yaw1) {
		this.yaw1 = yaw1;
	}

	public String getDir1() {
		return dir1;
	}

	public void setDir1(String dir1) {
		this.dir1 = dir1;
	}

	public String getYaw2() {
		return yaw2;
	}

	public void setYaw2(String yaw2) {
		this.yaw2 = yaw2;
	}

	public String getDir2() {
		return dir2;
	}

	public void setDir2(String dir2) {
		this.dir2 = dir2;
	}

	public String getYaw3() {
		return yaw3;
	}

	public void setYaw3(String yaw3) {
		this.yaw3 = yaw3;
	}

	public String getDir3() {
		return dir3;
	}

	public void setDir3(String dir3) {
		this.dir3 = dir3;
	}

	public String getYaw4() {
		return yaw4;
	}

	public void setYaw4(String yaw4) {
		this.yaw4 = yaw4;
	}

	public String getDir4() {
		return dir4;
	}

	public void setDir4(String dir4) {
		this.dir4 = dir4;
	}
}
