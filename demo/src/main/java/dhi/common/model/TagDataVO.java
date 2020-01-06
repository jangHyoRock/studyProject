package dhi.common.model;

import java.util.Date;

import dhi.common.util.Utilities;

public class TagDataVO {

	private String tagId;
	
	private String tagNm;
	
	private String unit;
	
	private String plantUnitId;
	
	private Date timeStamp;
	
	private Double tagVal;

	public TagDataVO () {};
	public TagDataVO (String plantUnitId, String tagId)
	{
		this.plantUnitId = plantUnitId;
		this.tagId = tagId;
	}
	
	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagNm() {
		return tagNm;
	}

	public void setTagNm(String tagNm) {
		this.tagNm = tagNm;
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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Double getTagVal() {
		return tagVal;
	}

	public double getTagVal(int roundDigit) {
		return Utilities.round(this.tagVal, roundDigit);
	}
	
	public double getTagValFirstRound() {
		return Utilities.round(this.tagVal, 1);
	}
	
	public double getTagValSecondRound() {
		return Utilities.round(this.tagVal, 2);
	}

	public int getTagValToInt() {
		return this.tagVal.intValue();
	}

	public double getTagValueRange(double min, double max) {
		return Utilities.valueOfDoubleRange(this.tagVal, min, max);
	}

	public double getTagValDoubleOrIntToNumber(double val, int compareDigit, int roundDigit) {
		double num = Math.pow(10d, compareDigit);
		if (val >= num)
			return (int) val;
		else
			return Utilities.round(val, roundDigit);
	}

	public void setTagVal(Double tagVal) {
		this.tagVal = tagVal;
	}
	
	@Override
    public int hashCode(){
        String s = this.plantUnitId + this.tagId; 
        return s.hashCode();
    }
	
    @Override
    public boolean equals(Object object) {    	
    	TagDataVO tagDataVO = (TagDataVO)object;
		if (this.plantUnitId.equals(tagDataVO.getPlantUnitId()) && this.tagId.equals(tagDataVO.getTagId()))
			return true;
		
		return false;
    }
}