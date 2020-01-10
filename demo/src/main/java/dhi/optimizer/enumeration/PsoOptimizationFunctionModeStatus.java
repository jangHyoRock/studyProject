package dhi.optimizer.enumeration;

/**
 * PSO Optimization Function Mode Enumeration.
 */
public enum PsoOptimizationFunctionModeStatus {

	P("Profit"), E("Emission"), S("Equipment");
	
	private String value;
	private PsoOptimizationFunctionModeStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
