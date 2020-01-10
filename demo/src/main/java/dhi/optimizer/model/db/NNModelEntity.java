package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * TODO : 다단 적용. 삭제예정
 * DB Table Model Class using JPA.(TB_NN_MODEL)
 */
@Entity
@Table(name="TB_NN_MODEL")
public class NNModelEntity {
	
	@Id
	@Column(name="TIMESTAMP")
	private Date timestamp; 
	
	@Column(name="ITERATION")
	private int iteration;
	
	@Column(name="TRAIN_ERROR_RATE")
	private double trainErrorRate;
	
	@Column(name="VALID_ERROR_RATE")
	private double validErrorRate;
	
	@Column(name="TRAIN_STATUS")
	private Boolean trainStatus;
	
	@Column(name="MODEL")
	private String model;

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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
