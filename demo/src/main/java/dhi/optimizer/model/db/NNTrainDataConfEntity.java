package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NN_TRAIN_DATA_CONF)
 */
@Entity
@Table(name="TB_NN_TRAIN_DATA_CONF")
public class NNTrainDataConfEntity {

	@Id
	@Column(name="TAG_ID")
	private String tagId;
	
	@Column(name="TAG_NM")
	private String tagNm;
	
	@Column(name="PSO_MV")
	private String psoMV;
	
	@Column(name="ZERO_PLATE")
	private Boolean zeroPlate;
	
	@Column(name="IO_TYPE")
	private String ioType;
	
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
