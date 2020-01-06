package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CombustionStatusPositionCoal {

	private String location;
	
	@JsonProperty("supply")
	private List<CombustionStatusPositionCoalSupply> coalSupplyList;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<CombustionStatusPositionCoalSupply> getCoalSupplyList() {
		return coalSupplyList;
	}

	public void setCoalSupplyList(List<CombustionStatusPositionCoalSupply> coalSupplyList) {
		this.coalSupplyList = coalSupplyList;
	}
}
