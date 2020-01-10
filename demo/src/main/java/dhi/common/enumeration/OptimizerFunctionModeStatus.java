package dhi.common.enumeration;

public enum OptimizerFunctionModeStatus {

	FALSE("OFF"), TRUE("ON");
	
	private String value;
	private OptimizerFunctionModeStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
