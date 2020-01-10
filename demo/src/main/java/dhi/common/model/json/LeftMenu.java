package dhi.common.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * Json model of left menu API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeftMenu {

	@JsonProperty("company")
	private List<PlantInfo> companyList;

	public List<PlantInfo> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<PlantInfo> companyList) {
		this.companyList = companyList;
	}
}
