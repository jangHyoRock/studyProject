package dhi.optimizer.model.json;

/*
 * Boiler parts area json model for combustionDynamics class.
 */
public class BoilerPart {
	
	private String part;

	private ContourData data;

	public BoilerPart(String part, ContourData data) {
		this.part = part;
		this.data = data;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public ContourData getData() {
		return data;
	}

	public void setData(ContourData data) {
		this.data = data;
	}
}
