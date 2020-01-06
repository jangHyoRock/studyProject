package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(DD_TB_NN_MODEL_BASE_LIST)
 */
@Entity
@Table(name = "DD_TB_NN_MODEL_BASE_LIST")
public class _NNModelBaseListEntity {

	@EmbeddedId
	private _NNModelBaseListIdEntity id;

	@Column(name = "TRAIN_STATUS")
	private Boolean trainStatus;

	@Column(name = "MODEL")
	private String model;

	public _NNModelBaseListIdEntity getId() {
		return id;
	}

	public void setId(_NNModelBaseListIdEntity id) {
		this.id = id;
	}

	public Boolean getTrainStatus() {
		return trainStatus;
	}

	public void setTrainStatus(Boolean trainStatus) {
		this.trainStatus = trainStatus;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
}