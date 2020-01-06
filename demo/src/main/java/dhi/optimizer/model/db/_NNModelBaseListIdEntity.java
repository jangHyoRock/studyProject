package dhi.optimizer.model.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 * DB Table PK Model Class using JPA.(DD_TB_NN_MODEL_BASE_LIST)
 */
@SuppressWarnings("serial")
@Embeddable
public class _NNModelBaseListIdEntity implements Serializable {

	@Column(name = "TIMESTAMP")
	private Date timestamp;

	@Column(name = "MODEL_ID")
	private String modelId;

	public _NNModelBaseListIdEntity() {
	}

	public _NNModelBaseListIdEntity(Date timestamp, String modelId) {
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
