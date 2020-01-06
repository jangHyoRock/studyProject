package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CombustionStatusPosition {

	private String item;
	
	@JsonProperty("ofa_supply")
	private List<OFASupplyStatus> ofaSupplyStatusList;
	
	@JsonProperty("coal_supply")
	private List<CoalSupplyStatus> coalSupplyStatusList;
	
	@JsonProperty("pa_sa_supply")
	private List<SASupplyStatus> saSupplyStatusList;

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

	public List<CoalSupplyStatus> getCoalSupplyStatusList() {
		return coalSupplyStatusList;
	}

	public void setCoalSupplyStatusList(List<CoalSupplyStatus> coalSupplyStatusList) {
		this.coalSupplyStatusList = coalSupplyStatusList;
	}

	public List<SASupplyStatus> getSaSupplyStatusList() {
		return saSupplyStatusList;
	}

	public void setSaSupplyStatusList(List<SASupplyStatus> saSupplyStatusList) {
		this.saSupplyStatusList = saSupplyStatusList;
	}
}
