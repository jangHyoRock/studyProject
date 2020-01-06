package dhi.optimizer.enumeration;

/**
 * Combustion Optimizer System Function Mode Enumeration.
 */
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
