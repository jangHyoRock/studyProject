package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CombustionStatusPositionT {

	@JsonProperty("ofa_info")
	private List<CombustionStatusPositionOFA> combustionStatusPositionOFAList;
	
	@JsonProperty("coal_info")
	private List<CombustionStatusPositionCoal> combustionStatusPositionCoalList;

	public List<CombustionStatusPositionOFA> getCombustionStatusPositionOFAList() {
		return combustionStatusPositionOFAList;
	}

	public void setCombustionStatusPositionOFAList(List<CombustionStatusPositionOFA> combustionStatusPositionOFAList) {
		this.combustionStatusPositionOFAList = combustionStatusPositionOFAList;
	}
	
	public List<CombustionStatusPositionCoal> getCombustionStatusPositionCoalList() {
		return combustionStatusPositionCoalList;
	}

	public void setCombustionStatusPositionCoalList(List<CombustionStatusPositionCoal> combustionStatusPositionCoalList) {
		this.combustionStatusPositionCoalList = combustionStatusPositionCoalList;
	}
}
