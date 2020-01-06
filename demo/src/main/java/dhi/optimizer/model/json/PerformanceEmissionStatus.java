package dhi.optimizer.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PerformanceEmissionStatus {

	@JsonProperty("eco_outlet_flue_gas")
	private ArrayList<PerformanceItemStatus> ecoOutletFlueGasList;
	
	@JsonProperty("stack_flue_gas")
	private ArrayList<PerformanceItemStatus> stackFlueGasList;

	public ArrayList<PerformanceItemStatus> getEcoOutletFlueGasList() {
		return ecoOutletFlueGasList;
	}

	public void setEcoOutletFlueGasList(ArrayList<PerformanceItemStatus> ecoOutletFlueGasList) {
		this.ecoOutletFlueGasList = ecoOutletFlueGasList;
	}

	public ArrayList<PerformanceItemStatus> getStackFlueGasList() {
		return stackFlueGasList;
	}

	public void setStackFlueGasList(ArrayList<PerformanceItemStatus> stackFlueGasList) {
		this.stackFlueGasList = stackFlueGasList;
	}
}
