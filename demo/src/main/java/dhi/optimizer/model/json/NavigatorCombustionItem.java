package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NavigatorCombustionItem {

	private String type;
	
	@JsonProperty("sa")
	private List<NavigatorCombustionItemStatus> saItemStatusList;
	
	@JsonProperty("uofa")
	private List<NavigatorCombustionItemStatus> ofaItemStatusList;
	
	@JsonProperty("flow")
	private List<NavigatorCombustionItemStatus> flowItemStatusList;
		
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<NavigatorCombustionItemStatus> getSaItemStatusList() {
		return saItemStatusList;
	}

	public void setSaItemStatusList(List<NavigatorCombustionItemStatus> saItemStatusList) {
		this.saItemStatusList = saItemStatusList;
	}

	public List<NavigatorCombustionItemStatus> getOfaItemStatusList() {
		return ofaItemStatusList;
	}

	public void setOfaItemStatusList(List<NavigatorCombustionItemStatus> ofaItemStatusList) {
		this.ofaItemStatusList = ofaItemStatusList;
	}

	public List<NavigatorCombustionItemStatus> getFlowItemStatusList() {
		return flowItemStatusList;
	}

	public void setFlowItemStatusList(List<NavigatorCombustionItemStatus> flowItemStatusList) {
		this.flowItemStatusList = flowItemStatusList;
	}
}
