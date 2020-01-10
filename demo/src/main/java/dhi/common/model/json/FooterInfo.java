package dhi.common.model.json;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Footer json model of common API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FooterInfo {

	@JsonProperty("plant")
	private String plant;
	
	@JsonProperty("data")
	private List<ItemStatus> dataList;
	
	public FooterInfo(String plant, List<ItemStatus> dataList)
	{
		this.plant = plant;
		this.dataList = dataList;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public List<ItemStatus> getDataList() {
		return dataList;
	}

	public void setDataList(List<ItemStatus> dataList) {
		this.dataList = dataList;
	}
}
