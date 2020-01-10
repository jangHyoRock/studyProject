package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * CombustionStatus main json model of screen.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CombustionStatus {

	@JsonProperty("combustion_status")
	private CombustionStatus combustionStatus;
	
	@JsonProperty("coal_property")
	private CoalProperty coalProperty;
	
	@JsonProperty("position")
	private List<CombustionStatusPosition> positionList;
	
	@JsonProperty("fireball_position")
	private List<Position> fireballPosition;
	
	@JsonProperty("rh_spray_supply")
	private List<RHSpraySupplyStatus> rhSpraySupplyList;

	public CombustionStatus() {}
	
	public CombustionStatus(CombustionStatus combustionStatus) {
		this.combustionStatus = combustionStatus;
	}

	public CombustionStatus getCombustionStatus() {
		return combustionStatus;
	}

	public void setCombustionStatus(CombustionStatus combustionStatus) {
		this.combustionStatus = combustionStatus;
	}

	public CoalProperty getCoalProperty() {
		return coalProperty;
	}

	public void setCoalProperty(CoalProperty coalProperty) {
		this.coalProperty = coalProperty;
	}

	public List<CombustionStatusPosition> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<CombustionStatusPosition> positionList) {
		this.positionList = positionList;
	}

	public List<Position> getFireballPosition() {
		return fireballPosition;
	}

	public void setFireballPosition(List<Position> fireballPosition) {
		this.fireballPosition = fireballPosition;
	}

	public List<RHSpraySupplyStatus> getRhSpraySupplyList() {
		return rhSpraySupplyList;
	}

	public void setRhSpraySupplyList(List<RHSpraySupplyStatus> rhSpraySupplyList) {
		this.rhSpraySupplyList = rhSpraySupplyList;
	}
}
