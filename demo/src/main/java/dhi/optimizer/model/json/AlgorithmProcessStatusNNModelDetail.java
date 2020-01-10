package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmProcessStatusNNModelDetail {

	@JsonProperty("time")
	private String time;

	@JsonProperty("used")
	private Boolean used;
	
	public AlgorithmProcessStatusNNModelDetail() {};
	
	public AlgorithmProcessStatusNNModelDetail(String time, Boolean used)
	{
		this.time = time;
		this.used = used;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}
}
