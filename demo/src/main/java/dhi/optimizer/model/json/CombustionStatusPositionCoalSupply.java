package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CombustionStatusPositionCoalSupply {

	private String item;
	
	@JsonProperty("status")	
	private List<ItemStatus> coalPaSaSupplyStatusList;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public List<ItemStatus> getCoalPaSaSupplyStatusList() {
		return coalPaSaSupplyStatusList;
	}

	public void setCoalPaSaSupplyStatusList(List<ItemStatus> coalPaSaSupplyStatusList) {
		this.coalPaSaSupplyStatusList = coalPaSaSupplyStatusList;
	}
	
}
