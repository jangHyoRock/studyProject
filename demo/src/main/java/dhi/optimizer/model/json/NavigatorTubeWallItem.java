package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class NavigatorTubeWallItem {
	
	@JsonProperty("temp_avg_value")
	private String tempAvg;
	
	@JsonProperty("temp_item")
	private List<NavigatorTubeWallTempItem> navigatorTubeWallTempItemList;

	public NavigatorTubeWallItem(String tempAvg, List<NavigatorTubeWallTempItem> navigatorTubeWallTempItemList) {
		this.tempAvg = tempAvg;
		this.navigatorTubeWallTempItemList = navigatorTubeWallTempItemList;
	}
	
	public String getTempAvg() {
		return tempAvg;
	}

	public void setTempAvg(String tempAvg) {
		this.tempAvg = tempAvg;
	}
	
	public List<NavigatorTubeWallTempItem> getNavigatorTubeWallTempItemList() {
		return navigatorTubeWallTempItemList;
	}

	public void setNavigatorTubeWallTempItemList(List<NavigatorTubeWallTempItem> navigatorTubeWallTempItemList) {
		this.navigatorTubeWallTempItemList = navigatorTubeWallTempItemList;
	}	
}

