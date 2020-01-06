package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(DD_TB_NN_MODEL_EXEC_STATUS_HIST)
 */
@Entity
@Table(name = "DD_TB_NN_MODEL_EXEC_STATUS_HIST")
public class _NNModelExecStatusHistEntity {

	@EmbeddedId
	private _NNModelExecStatusHistIdEntity id;

	@Column(name = "MODEL_TYPE")
	private String modelType;

	@Column(name = "MODEL_STATUS")
	private Integer modelStatus;

	@Column(name = "DESCRIPTION")
	private String description;

	public _NNModelExecStatusHistIdEntity getId() {
		return id;
	}

	public void setId(_NNModelExecStatusHistIdEntity id) {
		this.id = id;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public Integer getModelStatus() {
		return modelStatus;
	}

	public void setModelStatus(Integer modelStatus) {
		this.modelStatus = modelStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}