package dhi.common.enumeration;

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
