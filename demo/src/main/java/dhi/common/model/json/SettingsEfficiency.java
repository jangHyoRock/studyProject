package dhi.common.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsEfficiency {

	@JsonProperty("category_nm")
	private String categoryNm;
	
	@JsonProperty("data")
	private List<ConfigItem> dataList;

	public String getCategoryNm() {
		return categoryNm;
	}

	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}

	public List<ConfigItem> getDataList() {
		return dataList;
	}

	public void setDataList(List<ConfigItem> dataList) {
		this.dataList = dataList;
	}
}
