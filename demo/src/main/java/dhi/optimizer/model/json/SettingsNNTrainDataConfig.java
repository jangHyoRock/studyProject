package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsNNTrainDataConfig {

	@JsonProperty("tag_id")
	private String tagId;
	
	@JsonProperty("tag_nm")
	private String tagNm;
	
	@JsonProperty("pso_mv")
	private String psoMV;
	
	@JsonProperty("zero_plate")
	private Boolean zeroPlate;
	
	@JsonProperty("io_type")
	private String ioType;
	
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

	public String getPsoMV() {
		return psoMV;
	}

	public void setPsoMV(String psoMV) {
		this.psoMV = psoMV;
	}

	public Boolean getZeroPlate() {
		return zeroPlate;
	}

	public void setZeroPlate(Boolean zeroPlate) {
		this.zeroPlate = zeroPlate;
	}

	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}

	public int getTagNo() {
		return tagNo;
	}

	public void setTagNo(int tagNo) {
		this.tagNo = tagNo;
	}
}
