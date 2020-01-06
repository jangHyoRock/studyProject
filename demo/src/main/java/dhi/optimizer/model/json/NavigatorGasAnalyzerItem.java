package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigatorGasAnalyzerItem {

	private String item;
	
	private String[] front1;
	private String[] front2;
	private String[] rear1;
	private String[] rear2;
	
	@JsonProperty("front_min")
	private String frontMin;
	
	@JsonProperty("front_max")
	private String frontMax;
	
	@JsonProperty("front_avg")
	private String frontAvg;
	
	@JsonProperty("rear_min")
	private String rearMin;
	
	@JsonProperty("rear_max")
	private String rearMax;
	
	@JsonProperty("rear_avg")
	private String rearAvg;
		
	@JsonProperty("all_min")
	private String allMin;
	
	@JsonProperty("all_max")
	private String allMax;
	
	@JsonProperty("all_avg")
	private String allAvg;
	
	public NavigatorGasAnalyzerItem() {
	};

	public NavigatorGasAnalyzerItem(String item) {

		this.item = item;
		this.front1 = new String[] { "-", "-", "-", "-" };
		this.front2 = new String[] { "-", "-", "-", "-" };
		this.rear1 = new String[] { "-", "-", "-", "-" };
		this.rear2 = new String[] { "-", "-", "-", "-" };
		this.frontMin = "-";
		this.frontMax = "-";		
		this.frontAvg = "-";
		this.rearMin = "-";
		this.rearMax = "-";
		this.rearAvg = "-";
		this.allMin = "-";
		this.allMax = "-";
		this.allAvg = "-";
	};

	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String[] getFront1() {
		return front1;
	}
	public void setFront1(String[] front1) {
		this.front1 = front1;
	}
	public String[] getFront2() {
		return front2;
	}
	public void setFront2(String[] front2) {
		this.front2 = front2;
	}
	public String[] getRear1() {
		return rear1;
	}
	public void setRear1(String[] rear1) {
		this.rear1 = rear1;
	}
	public String[] getRear2() {
		return rear2;
	}
	public void setRear2(String[] rear2) {
		this.rear2 = rear2;
	}
	public String getFrontMin() {
		return frontMin;
	}
	public void setFrontMin(String frontMin) {
		this.frontMin = frontMin;
	}
	public String getFrontMax() {
		return frontMax;
	}
	public void setFrontMax(String frontMax) {
		this.frontMax = frontMax;
	}
	public String getFrontAvg() {
		return frontAvg;
	}
	public void setFrontAvg(String frontAvg) {
		this.frontAvg = frontAvg;
	}
	public String getRearMin() {
		return rearMin;
	}
	public void setRearMin(String rearMin) {
		this.rearMin = rearMin;
	}
	public String getRearMax() {
		return rearMax;
	}
	public void setRearMax(String rearMax) {
		this.rearMax = rearMax;
	}
	public String getRearAvg() {
		return rearAvg;
	}
	public void setRearAvg(String rearAvg) {
		this.rearAvg = rearAvg;
	}
	public String getAllMin() {
		return allMin;
	}
	public void setAllMin(String allMin) {
		this.allMin = allMin;
	}
	public String getAllMax() {
		return allMax;
	}
	public void setAllMax(String allMax) {
		this.allMax = allMax;
	}
	public String getAllAvg() {
		return allAvg;
	}
	public void setAllAvg(String allAvg) {
		this.allAvg = allAvg;
	}
}
