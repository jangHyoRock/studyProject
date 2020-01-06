package dhi.optimizer.model.json;

/*
 * Optimizer json model dependent on trend chart.
 */
public class Optimizer {

	private String end_date;

	private int status;

	private String start_date;

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	@Override
	public String toString() {
		return "ClassPojo [end_date = " + end_date + ", status = " + status + ", start_date = " + start_date + "]";
	}
}
