package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(DD_TB_NN_MODEL_EXEC_STATUS)
 */
@Entity
@Table(name = "DD_TB_NN_MODEL_EXEC_STATUS")
public class _NNModelExecStatusEntity {

	@Id
	@Column(name = "MODEL_ID")
	private String modelId;

	@Column(name = "MODEL_TYPE")
	private String modelType;

	@Column(name = "MODEL_STATUS")
	private Integer modelStatus;

	@Column(name = "EXEC_PRIORITY")
	private Integer execPriority;

	@Column(name = "START_DT")
	private Date startDt;

	@Column(name = "END_DT")
	private Date endDt;

	@Column(name = "END_YN")
	private Boolean endYn;

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
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

	public Integer getExecPriority() {
		return execPriority;
	}

	public void setExecPriority(Integer execPriority) {
		this.execPriority = execPriority;
	}

	public Date getStartDt() {
		return startDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public Date getEndDt() {
		return endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public Boolean getEndYn() {
		return endYn;
	}

	public void setEndYn(Boolean endYn) {
		this.endYn = endYn;
	}
}