package dhi.common.model;

public class ConfDataVO {

	private String confId;
	
	private String unit;
	
	private String plantUnitId;
	
	private Double confVal;
	
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
	
	@Override
    public int hashCode(){
        String s = this.plantUnitId + this.confId; 
        return s.hashCode();
    }
	
    @Override
    public boolean equals(Object object) {    	
		ConfDataVO confDataVO = (ConfDataVO)object;
		if (this.plantUnitId.equals(confDataVO.getPlantUnitId()) && this.confId.equals(confDataVO.getConfId()))
			return true;
		
		return false;
    }
}
