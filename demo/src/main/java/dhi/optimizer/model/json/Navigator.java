package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Navigator {

	@JsonProperty("combustion_status")
	private NavigatorCombustionStatus combustionStatus;
	
	@JsonProperty("gas_press_temp")
	private NavigatorGasPressAndTemp gasPressAndTemp;

	public NavigatorCombustionStatus getCombustionStatus() {
		return combustionStatus;
	}

	public void setCombustionStatus(NavigatorCombustionStatus combustionStatus) {
		this.combustionStatus = combustionStatus;
	}

	public NavigatorGasPressAndTemp getGasPressAndTemp() {
		return gasPressAndTemp;
	}

	public void setGasPressAndTemp(NavigatorGasPressAndTemp gasPressAndTemp) {
		this.gasPressAndTemp = gasPressAndTemp;
	}
}
