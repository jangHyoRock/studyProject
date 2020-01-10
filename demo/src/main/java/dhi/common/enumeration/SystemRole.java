package dhi.common.enumeration;

public enum SystemRole {

	AD("Admin"), CE("Operation Manager"), OP("Operator"), ME("Maintenance Engineer"), EF("Efficiency Engineer");
	
	private String value;
	private SystemRole(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}	
}
