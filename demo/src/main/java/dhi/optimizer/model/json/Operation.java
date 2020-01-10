package dhi.optimizer.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Operation main json model of screen.
 */
public class Operation {

	@JsonProperty("optimize_mode")
	private ArrayList<ItemStatus> optimizeModeList;

	public ArrayList<ItemStatus> getOptimizeModeList() {
		return optimizeModeList;
	}

	public void setOptimizeModeList(ArrayList<ItemStatus> optimizeModeList) {
		this.optimizeModeList = optimizeModeList;
	}
}
