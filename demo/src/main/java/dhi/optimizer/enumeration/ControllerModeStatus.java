package dhi.optimizer.enumeration;

/**
 * Control Mode Enumeration. <br>
 * : OL(STOP) => Open Loop, CL(RUN) => Close Loop.
 */
public enum ControllerModeStatus {

	OL("STOP"), CL("RUN");
	
	private String value;
	private ControllerModeStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
