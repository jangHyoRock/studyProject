package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CombustionStatusPositionOFA {
	
	private String location;
	
	@JsonProperty("supply")
	private List<CombustionStatusPositionOFASupply> ofaSupplyList;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<CombustionStatusPositionOFASupply> getOfaSupplyList() {
		return ofaSupplyList;
	}

	public void setOfaSupplyList(List<CombustionStatusPositionOFASupply> ofaSupplyList) {
		this.ofaSupplyList = ofaSupplyList;
	}
	
}
