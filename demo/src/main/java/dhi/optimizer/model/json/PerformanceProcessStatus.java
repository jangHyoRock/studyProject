package dhi.optimizer.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerformanceProcessStatus {

	@JsonProperty("furnace_temperature")
	private ArrayList<PerformanceItemStatus> furnaceTemperatureList;
	
	@JsonProperty("rh_spray_flow")
	private ArrayList<PerformanceItemStatus> rhSprayFlow;

	public ArrayList<PerformanceItemStatus> getFurnaceTemperatureList() {
		return furnaceTemperatureList;
	}

	public void setFurnaceTemperatureList(ArrayList<PerformanceItemStatus> furnaceTemperatureList) {
		this.furnaceTemperatureList = furnaceTemperatureList;
	}

	public ArrayList<PerformanceItemStatus> getRhSprayFlow() {
		return rhSprayFlow;
	}

	public void setRhSprayFlow(ArrayList<PerformanceItemStatus> rhSprayFlow) {
		this.rhSprayFlow = rhSprayFlow;
	}
}
