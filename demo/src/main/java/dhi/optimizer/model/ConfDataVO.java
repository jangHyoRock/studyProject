package dhi.optimizer.model;

/**
 * Optimizer Common ConfigData VO Class.
 */
public class ConfDataVO {

private String confId;
	
	private String unit;
	
	private String plantUnitId;
	
	private Double confVal;
	
	private String confNm;
	
	public ConfDataVO () {};
	public ConfDataVO (String plantUnitId, String tagId)
	{
		this.plantUnitId = plantUnitId;
		this.confId = tagId;
	}
	
	public String getConfId() {
		return confId;
	}
	
	public void setConfId(String confId) {
		this.confId = confId;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getPlantUnitId() {
		return plantUnitId;
	}
	
	public void setPlantUnitId(String plantUnitId) {
		this.plantUnitId = plantUnitId;
	}
	
	public Double getConfVal() {
		return confVal;
	}
	
	public void setConfVal(Double confVal) {
		this.confVal = confVal;
	}
	
	public String getConfNm() {
		return confNm;
	}
	
	public void setConfNm(String confNm) {
		this.confNm = confNm;
	}
}
