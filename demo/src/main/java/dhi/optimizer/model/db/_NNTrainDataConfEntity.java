package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(DD_TB_NN_TRAIN_DATA_CONF)
 */
@Entity
@Table(name = "DD_TB_NN_TRAIN_DATA_CONF")
public class _NNTrainDataConfEntity {
	
	@EmbeddedId
	private _NNTrainDataConfIdEntity id;
	
	@Column(name="TAG_NM")
	private String tagNm;
	
	@Column(name="PSO_MV")
	private String psoMV;
	
	@Column(name="ZERO_PLATE")
	private Boolean zeroPlate;
	
	@Column(name="IO_TYPE")
	private String ioType;
	
	@Column(name="TAG_INDEX")
	private int tagIndex;

	public _NNTrainDataConfIdEntity getId() {
		return id;
	}

	public void setId(_NNTrainDataConfIdEntity id) {
		this.id = id;
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

	public int getTagIndex() {
		return tagIndex;
	}

	public void setTagIndex(int tagIndex) {
		this.tagIndex = tagIndex;
	}

	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}	
}
