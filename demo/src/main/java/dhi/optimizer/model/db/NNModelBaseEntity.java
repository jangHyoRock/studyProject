package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * TODO : 다단 적용. 삭제예정
 * DB Table Model Class using JPA.(TB_NN_MODEL_BASE)
 */
@Entity
@Table(name="TB_NN_MODEL_BASE")
public class NNModelBaseEntity {

	@Id
	@Column(name="TIMESTAMP")
	private Date timestamp; 
	
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
