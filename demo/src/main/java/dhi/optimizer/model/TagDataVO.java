package dhi.optimizer.model;

import java.util.Date;

/**
 * Tag Data Model Class. <br>
 * : Tag 정보를 담는 Model VO Class.
 */
public class TagDataVO {

	private String tagId;
	
	private String tagNm;
	
	private String unit;
	
	private String plantUnitId;
	
	private Date timeStamp;
	
	private Double tagVal;

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
	
	public void setTagVal(Double tagVal) {
		this.tagVal = tagVal;
	}
}