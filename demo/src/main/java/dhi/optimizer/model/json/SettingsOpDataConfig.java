package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsOpDataConfig {

	@JsonProperty("tag_id")
	private String tagId;
	
	@JsonProperty("tag_nm")
	private String tagNm;
	
	private String description;
	
	@JsonProperty("min_raw")
	private Double minRaw;
	
	@JsonProperty("max_raw")
	private Double maxRaw;
	
	private String unit;
	
	@JsonProperty("min_eu")
	private Double minEu;
	
	@JsonProperty("max_eu")
	private Double maxEu;
	
	@JsonProperty("plant_unit_id")
	private String plantUnitId;
	
	@JsonProperty("tag_no")
	private int tagNo;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getMinRaw() {
		return minRaw;
	}

	public void setMinRaw(Double minRaw) {
		this.minRaw = minRaw;
	}

	public Double getMaxRaw() {
		return maxRaw;
	}

	public void setMaxRaw(Double maxRaw) {
		this.maxRaw = maxRaw;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getMinEu() {
		return minEu;
	}

	public void setMinEu(Double minEu) {
		this.minEu = minEu;
	}

	public Double getMaxEu() {
		return maxEu;
	}

	public void setMaxEu(Double maxEu) {
		this.maxEu = maxEu;
	}

	public String getPlantUnitId() {
		return plantUnitId;
	}

	public void setPlantUnitId(String plantUnitId) {
		this.plantUnitId = plantUnitId;
	}

	public int getTagNo() {
		return tagNo;
	}

	public void setTagNo(int tagNo) {
		this.tagNo = tagNo;
	}
}