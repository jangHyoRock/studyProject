package dhi.common.schedule;

/*
 * Scheduler status information enumeration.
 */
public enum ScheduleStatus {

	START(1), END(0), ERROR(999);
	
	private ScheduleStatus(int value) {
		this.value = value;
	}
	
	private final int value;
	
	public int getValue() {
		return value;
	}
}
