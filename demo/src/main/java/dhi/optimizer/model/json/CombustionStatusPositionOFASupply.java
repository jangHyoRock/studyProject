package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CombustionStatusPositionOFASupply {

	private String item;
	
	@JsonProperty("status")
	private List<OFASupplyStatus> ofaSupplyStatusList;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public List<OFASupplyStatus> getOfaSupplyStatusList() {
		return ofaSupplyStatusList;
	}

	public void setOfaSupplyStatusList(List<OFASupplyStatus> ofaSupplyStatusList) {
		this.ofaSupplyStatusList = ofaSupplyStatusList;
	}	
}