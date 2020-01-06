package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_OPT_CODE_INF)
 */
@Entity
@Table(name="TB_OPT_CODE_INF")
public class OptCodeInfEntity {

	@EmbeddedId
	private OptCodeInfIdEntity optCodeInfIdEntity;
	
	@Column(name="CODE_NM")
	private String codeNm;
	
	@Column(name="TAG_ID")
	private String tagId;

	public OptCodeInfIdEntity getOptCodeInfIdEntity() {
		return optCodeInfIdEntity;
	}

	public void setOptCodeInfIdEntity(OptCodeInfIdEntity optCodeInfIdEntity) {
		this.optCodeInfIdEntity = optCodeInfIdEntity;
	}

	public String getCodeNm() {
		return codeNm;
	}

	public void setCodeNm(String codeNm) {
		this.codeNm = codeNm;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
}
