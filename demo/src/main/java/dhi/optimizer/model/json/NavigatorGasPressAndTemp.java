package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigatorGasPressAndTemp {

	@JsonProperty("data")
	private List<ItemStatus> dataList;
	
	@JsonProperty("gas_analyzer")
	private List<NavigatorGasAnalyzerItem> gasAnalyzerItemList;

	public List<ItemStatus> getDataList() {
		return dataList;
	}

	public void setDataList(List<ItemStatus> dataList) {
		this.dataList = dataList;
	}

	public List<NavigatorGasAnalyzerItem> getGasAnalyzerItemList() {
		return gasAnalyzerItemList;
	}

	public void setGasAnalyzerItemList(List<NavigatorGasAnalyzerItem> gasAnalyzerItemList) {
		this.gasAnalyzerItemList = gasAnalyzerItemList;
	}
}
