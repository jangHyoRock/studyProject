package dhi.optimizer.model.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(DD_TB_NN_TRAIN_DATA_CONF)
 */
@SuppressWarnings("serial")
@Embeddable
public class _NNTrainDataConfIdEntity implements Serializable {

	@Column(name = "MODEL_ID")
	private String modelId;

	@Column(name = "TAG_ID")
	private String tagId;

	public _NNTrainDataConfIdEntity() {
	}

	public _NNTrainDataConfIdEntity(String modelId, String tagId) {
		this.modelId = modelId;
		this.tagId = tagId;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
}
