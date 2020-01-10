package dhi.optimizer.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * CombustionDynamics main json model of screen.
 */
public class CombustionDynamics {
		
	@JsonProperty("boiler_part")
	private ArrayList<BoilerPart> boilerPartList;

	public ArrayList<BoilerPart> getBoilerPartList() {
		return boilerPartList;
	}

	public void setBoilerPartList(ArrayList<BoilerPart> boilerPartList) {
		this.boilerPartList = boilerPartList;
	}
}
