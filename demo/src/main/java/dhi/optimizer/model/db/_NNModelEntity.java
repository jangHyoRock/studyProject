package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NN_MODEL)
 */
@Entity
@Table(name = "DD_TB_NN_MODEL")
public class _NNModelEntity {

	@Id
	@Column(name = "MODEL_ID")
	private String modelId;

	@Column(name = "MODEL_NM")
	private String modelNm;

	@Column(name = "MODEL_TYPE")
	private String modelType;

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelNm() {
		return modelNm;
	}

	public void setModelNm(String modelNm) {
		this.modelNm = modelNm;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
}
