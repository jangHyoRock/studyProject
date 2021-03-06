package dhi.common.enumeration;

public enum LearningModeStatus {

	OFF("OFF"), ON("ON"), SO("Self ON"), SAO("Self Auto ON");
	
	private String value;
	private LearningModeStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
