package dhi.optimizer.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NavigatorWallMatchingTable {

	@JsonProperty("deviation")
	private List<NavigatorWallMatchingTable> wallMatchingTableDeviationList;
	
	@JsonProperty("highLow")
	private List<NavigatorWallMatchingTable> wallMatchingTableHighLowList;
	
	private String p1;
	private String p2;
	private String p3;
	private String p4;
	
	private String aa1;
	private String aa2;
	private String aa3;
	private String aa4;
	
	private String abfg1;
	private String abfg2;
	private String abfg3;
	private String abfg4;
	
	private String gg1;
	private String gg2;
	private String gg3;
	private String gg4;	

	public List<NavigatorWallMatchingTable> getWallMatchingTableDeviationList() {
		return wallMatchingTableDeviationList;
	}

	public void setWallMatchingTableDeviationList(List<NavigatorWallMatchingTable> wallMatchingTableDeviationList) {
		this.wallMatchingTableDeviationList = wallMatchingTableDeviationList;
	}

	public List<NavigatorWallMatchingTable> getWallMatchingTableHighLowList() {
		return wallMatchingTableHighLowList;
	}

	public void setWallMatchingTableHighLowList(List<NavigatorWallMatchingTable> wallMatchingTableHighLowList) {
		this.wallMatchingTableHighLowList = wallMatchingTableHighLowList;
	}

	public String getP1() {
		return p1;
	}
	
	public void setP1(String p1) {
		this.p1 = p1;
	}
	
	public String getP2() {
		return p2;
	}
	
	public void setP2(String p2) {
		this.p2 = p2;
	}
	
	public String getP3() {
		return p3;
	}
	
	public void setP3(String p3) {
		this.p3 = p3;
	}
	
	public String getP4() {
		return p4;
	}
	
	public void setP4(String p4) {
		this.p4 = p4;
	}
	
	public String getAa1() {
		return aa1;
	}
	
	public void setAa1(String aa1) {
		this.aa1 = aa1;
	}
	
	public String getAa2() {
		return aa2;
	}
	
	public void setAa2(String aa2) {
		this.aa2 = aa2;
	}
	
	public String getAa3() {
		return aa3;
	}
	
	public void setAa3(String aa3) {
		this.aa3 = aa3;
	}
	
	public String getAa4() {
		return aa4;
	}
	
	public void setAa4(String aa4) {
		this.aa4 = aa4;
	}
	
	public String getAbfg1() {
		return abfg1;
	}

	public void setAbfg1(String abfg1) {
		this.abfg1 = abfg1;
	}

	public String getAbfg2() {
		return abfg2;
	}

	public void setAbfg2(String abfg2) {
		this.abfg2 = abfg2;
	}

	public String getAbfg3() {
		return abfg3;
	}

	public void setAbfg3(String abfg3) {
		this.abfg3 = abfg3;
	}

	public String getAbfg4() {
		return abfg4;
	}

	public void setAbfg4(String abfg4) {
		this.abfg4 = abfg4;
	}

	public String getGg1() {
		return gg1;
	}
	
	public void setGg1(String gg1) {
		this.gg1 = gg1;
	}
	
	public String getGg2() {
		return gg2;
	}
	
	public void setGg2(String gg2) {
		this.gg2 = gg2;
	}
	
	public String getGg3() {
		return gg3;
	}
	
	public void setGg3(String gg3) {
		this.gg3 = gg3;
	}
	
	public String getGg4() {
		return gg4;
	}
	
	public void setGg4(String gg4) {
		this.gg4 = gg4;
	}
}
