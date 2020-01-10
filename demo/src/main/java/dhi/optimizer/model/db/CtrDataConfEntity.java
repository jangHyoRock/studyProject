package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_CTR_DATA_CONF)
 */
@Entity
@Table(name="TB_CTR_DATA_CONF")
public class CtrDataConfEntity {
	
	@Column(name="TAG_ID")
	private String tagId;
	
	@Id
	@Column(name="TAG_NM")
	private String tagNm;
	
	private String description;
	
	@Column(name="MIN_RAW")
	private Double minRaw;
	
	@Column(name="MAX_RAW")
	private Double maxRaw;
	
	@Column(name="UNIT")
	private String Unit;
	
	@Column(name="MIN_EU")
	private Double minEu;
	
	@Column(name="MAX_EU")
	private Double maxEu;
	
	@Column(name="P_UNIT_ID")
	private String plantUnitId;
	
	@Column(name="TAG_NO")
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
		return Unit;
	}

	public void setUnit(String unit) {
		Unit = unit;
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
