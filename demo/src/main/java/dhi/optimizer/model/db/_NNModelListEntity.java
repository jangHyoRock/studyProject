package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(DD_TB_NN_MODEL_LIST)
 */
@Entity
@Table(name = "DD_TB_NN_MODEL_LIST")
public class _NNModelListEntity {

	@EmbeddedId
	private _NNModelListIdEntity id;

	@Column(name = "ITERATION")
	private int iteration;

	@Column(name = "TRAIN_ERROR_RATE")
	private double trainErrorRate;

	@Column(name = "VALID_ERROR_RATE")
	private double validErrorRate;

	@Column(name = "TRAIN_STATUS")
	private Boolean trainStatus;

	@Column(name = "MODEL")
	private String model;

	public _NNModelListIdEntity getId() {
		return id;
	}

	public void setId(_NNModelListIdEntity id) {
		this.id = id;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public double getTrainErrorRate() {
		return trainErrorRate;
	}

	public void setTrainErrorRate(double trainErrorRate) {
		this.trainErrorRate = trainErrorRate;
	}

	public double getValidErrorRate() {
		return validErrorRate;
	}

	public void setValidErrorRate(double validErrorRate) {
		this.validErrorRate = validErrorRate;
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