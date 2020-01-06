package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NavigatorTubeWallTempItem {

	@JsonProperty("tube_no")
	private Number tubeNo;

	@JsonProperty("temp_value")
	private String tempValue;

	@JsonProperty("percentage")
	private String percentage;
	
	public NavigatorTubeWallTempItem(Number tubeNo, String tempValue, String percentage) {
		this.tubeNo = tubeNo;
		this.tempValue = tempValue;
		this.percentage = percentage;
	}

	public Number getTubeNo() {
		return tubeNo;
	}

	public void setTubeNo(Number tubeNo) {
		this.tubeNo = tubeNo;
	}

	public String getTempValue() {
		return tempValue;
	}

	public void setTempValue(String tempValue) {
		this.tempValue = tempValue;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
}
