package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NavigatorCombustionStatus {
	
	@JsonProperty("combustion_sa")
	private List<NavigatorCombustionItem> combustionSAList;
	
	@JsonProperty("combustion_uofa")
	private List<NavigatorCombustionItem> combustionUOFAList;
	
	@JsonProperty("combustion_flow")
	private List<NavigatorCombustionItem> combustionFlowList;
	
	@JsonProperty("combustion_coal_status")
	private List<NavigatorCombustionCoalSupplyStatus> coalSupplyStatusList;
		
	@JsonProperty("spiral_water_wall")
	private NavigatorCombustionSpiralWaterWall navigatorCombustionSpiralWaterWall;
		
	public List<NavigatorCombustionItem> getCombustionSAList() {
		return combustionSAList;
	}

	public void setCombustionSAList(List<NavigatorCombustionItem> combustionSAList) {
		this.combustionSAList = combustionSAList;
	}

	public List<NavigatorCombustionItem> getCombustionUOFAList() {
		return combustionUOFAList;
	}

	public void setCombustionUOFAList(List<NavigatorCombustionItem> combustionUOFAList) {
		this.combustionUOFAList = combustionUOFAList;
	}

	public List<NavigatorCombustionItem> getCombustionFlowList() {
		return combustionFlowList;
	}

	public void setCombustionFlowList(List<NavigatorCombustionItem> combustionFlowList) {
		this.combustionFlowList = combustionFlowList;
	}	

	public NavigatorCombustionSpiralWaterWall getNavigatorCombustionSpiralWaterWall() {
		return navigatorCombustionSpiralWaterWall;
	}

	public void setNavigatorCombustionSpiralWaterWall(
			NavigatorCombustionSpiralWaterWall navigatorCombustionSpiralWaterWall) {
		this.navigatorCombustionSpiralWaterWall = navigatorCombustionSpiralWaterWall;
	}

	public List<NavigatorCombustionCoalSupplyStatus> getCoalSupplyStatusList() {
		return coalSupplyStatusList;
	}

	public void setCoalSupplyStatusList(List<NavigatorCombustionCoalSupplyStatus> coalSupplyStatusList) {
		this.coalSupplyStatusList = coalSupplyStatusList;
	}

}
