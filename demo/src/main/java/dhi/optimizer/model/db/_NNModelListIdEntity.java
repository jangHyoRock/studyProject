package dhi.optimizer.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(DD_TB_NN_MODEL_LIST)
 */
@SuppressWarnings("serial")
@Embeddable
public class _NNModelListIdEntity implements Serializable {

	@Column(name = "TIMESTAMP")
	private Date timestamp;

	@Column(name = "MODEL_ID")
	private String modelId;

	public _NNModelListIdEntity() {
	}

	public _NNModelListIdEntity(Date timestamp, String modelId) {
		this.timestamp = timestamp;
		this.modelId = modelId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
}