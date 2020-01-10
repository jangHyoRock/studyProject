package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DD_TB_PSO")
public class _PSOEntity {

	@Id
	@Column(name="PSO_ID")
	private String psoId;

	@Column(name="PSO_NM")
	private String psoNM;

	@Column(name="PSO_TYPE")
	private String psoType;

	@Column(name="DESCRIPTION")
	private String description;

	@Column(name="MODEL_ID")
	private String modelId;

	@Column(name="REG_DT")
	private Date regDt;

	public String getPsoId() {
		return psoId;
	}

	public void setPsoId(String psoId) {
		this.psoId = psoId;
	}

	public String getPsoNM() {
		return psoNM;
	}

	public void setPsoNM(String psoNM) {
		this.psoNM = psoNM;
	}

	public String getPsoType() {
		return psoType;
	}

	public void setPsoType(String psoType) {
		this.psoType = psoType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Date getRegDt() {
		return regDt;
	}

	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
}
