package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TB_COMMON_DATA_CONF")
public class CommonDataConfEntity {

	@EmbeddedId
	private CommonDataConfIdEntity commonDataConfIdEntity;

	@Column(name="TAG_ID")
	private String tagId;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="MIN_RAW")
	private Double minRaw;
	
	@Column(name="MAX_RAW")
	private Double maxRaw;
	
	@Column(name="UNIT")
	private String unit;
	
	@Column(name="MIN_EU")
	private Double minEu;
	
	@Column(name="MAX_EU")
	private Double maxEu;
	
	@Column(name="TAG_NO")
	private int tagNo;
		
	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public CommonDataConfIdEntity getCommonDataConfIdEntity() {
		return commonDataConfIdEntity;
	}

	public void setCommonDataConfIdEntity(CommonDataConfIdEntity commonDataConfIdEntity) {
		this.commonDataConfIdEntity = commonDataConfIdEntity;
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

	public int getTagNo() {
		return tagNo;
	}

	public void setTagNo(int tagNo) {
		this.tagNo = tagNo;
	}	
}
