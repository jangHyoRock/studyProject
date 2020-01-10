package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CombustionStatusT {

	@JsonProperty("combustion_status")
	private CombustionStatusT combustionStatus;
	
	@JsonProperty("coal_property")
	private CoalProperty coalProperty;
	
	@JsonProperty("position")
	private CombustionStatusPositionT combustionStatusPosition;
		
	@JsonProperty("rh_spray_supply")
	private List<RHSpraySupplyStatus> rhSpraySupplyList;

	public CombustionStatusT() {}
	
	public CombustionStatusT(CombustionStatusT combustionStatus) {
		this.combustionStatus = combustionStatus;
	}

	public CombustionStatusT getCombustionStatus() {
		return combustionStatus;
	}

	public void setCombustionStatus(CombustionStatusT combustionStatus) {
		this.combustionStatus = combustionStatus;
	}

	public CoalProperty getCoalProperty() {
		return coalProperty;
	}

	public void setCoalProperty(CoalProperty coalProperty) {
		this.coalProperty = coalProperty;
	}	

	public CombustionStatusPositionT getCombustionStatusPosition() {
		return combustionStatusPosition;
	}

	public void setCombustionStatusPosition(CombustionStatusPositionT combustionStatusPosition) {
		this.combustionStatusPosition = combustionStatusPosition;
	}

	public List<RHSpraySupplyStatus> getRhSpraySupplyList() {
		return rhSpraySupplyList;
	}

	public void setRhSpraySupplyList(List<RHSpraySupplyStatus> rhSpraySupplyList) {
		this.rhSpraySupplyList = rhSpraySupplyList;
	}
}
