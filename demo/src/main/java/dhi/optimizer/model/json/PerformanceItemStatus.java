package dhi.optimizer.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerformanceItemStatus {

	private String item;
		
	private String unit;
	
	private String baseline;
	
	private String current;
	
	@JsonProperty("additional_information")
	private ArrayList<PerformanceItemStatus> additionalInformationList;
	
	@JsonProperty("total_mill")
	private String[] totalMills;
	
	@JsonProperty("operation_mill")
	private String[] operationMills;

	public PerformanceItemStatus() {}
	
	public PerformanceItemStatus(String item, String unit, String current) {
		
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getBaseline() {
		return baseline;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public ArrayList<PerformanceItemStatus> getAdditionalInformationList() {
		return additionalInformationList;
	}

	public void setAdditionalInformationList(ArrayList<PerformanceItemStatus> additionalInformationList) {
		this.additionalInformationList = additionalInformationList;
	}

	public String[] getTotalMills() {
		return totalMills;
	}

	public void setTotalMills(String[] totalMills) {
		this.totalMills = totalMills;
	}

	public String[] getOperationMills() {
		return operationMills;
	}

	public void setOperationMills(String[] operationMills) {
		this.operationMills = operationMills;
	}
}
