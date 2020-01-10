package dhi.optimizer.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * CoalProperty parts area json model for CombustionStatus class.
 */
public class CoalProperty {

	@JsonProperty("chemical_composition")
	private ArrayList<ItemStatus> chemicalCompositionList;
	
	@JsonProperty("coal_info")
	private CoalInfo coalInfo;

	public CoalInfo getCoalInfo() {
		return coalInfo;
	}

	public void setCoalInfo(CoalInfo coalInfo) {
		this.coalInfo = coalInfo;
	}

	public ArrayList<ItemStatus> getChemicalCompositionList() {
		return chemicalCompositionList;
	}

	public void setChemicalCompositionList(ArrayList<ItemStatus> chemicalCompositionList) {
		this.chemicalCompositionList = chemicalCompositionList;
	}
}
