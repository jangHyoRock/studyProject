package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_PSO_CONF)
 */
@Entity
@Table(name="TB_PSO_MV_INFO")
public class PSOMVInfoEntity {
	
	@Id
	@Column(name="PSO_MV")
	private String psoMV;
		
	@Column(name="PSO_MV_TYPE")
	private String psoMVType;
	
	@Column(name="PSO_MV_MAX")
	private Double psoMVMax;
	
	@Column(name="PSO_MV_MIN")
	private Double psoMVMin;
	
	@Column(name="PSO_MV_ORDER")
	private int psoMVOrder;
	
	@Column(name="AUTO_MODE_TAG_ID")
	private String autoModeTagId;
	
	@Column(name="HOLD_TAG_ID")
	private String holdTagId;
	
	@Column(name="INPUT_BIAS_TAG_ID")
	private String inputBiasTagId;
	
	@Column(name="OUTPUT_BIAS_TAG_ID")
	private String outputBiasTagId;
	
	public PSOMVInfoEntity() {}
	public PSOMVInfoEntity(String psoMV, String psoMVType)
	{
		this.psoMV = psoMV;
		this.psoMVType = psoMVType;
	}
	
	public String getPsoMV() {
		return psoMV;
	}

	public void setPsoMV(String psoMV) {
		this.psoMV = psoMV;
	}

	public String getPsoMVType() {
		return psoMVType;
	}

	public void setPsoMVType(String psoMVType) {
		this.psoMVType = psoMVType;
	}

	public Double getPsoMVMax() {
		return psoMVMax;
	}

	public void setPsoMVMax(Double psoMVMax) {
		this.psoMVMax = psoMVMax;
	}

	public Double getPsoMVMin() {
		return psoMVMin;
	}

	public void setPsoMVMin(Double psoMVMin) {
		this.psoMVMin = psoMVMin;
	}

	public int getPsoMVOrder() {
		return psoMVOrder;
	}

	public void setPsoMVOrder(int psoMVOrder) {
		this.psoMVOrder = psoMVOrder;
	}

	public String getAutoModeTagId() {
		return autoModeTagId;
	}

	public void setAutoModeTagId(String autoModeTagId) {
		this.autoModeTagId = autoModeTagId;
	}

	public String getHoldTagId() {
		return holdTagId;
	}

	public void setHoldTagId(String holdTagId) {
		this.holdTagId = holdTagId;
	}

	public String getInputBiasTagId() {
		return inputBiasTagId;
	}

	public void setInputBiasTagId(String inputBiasTagId) {
		this.inputBiasTagId = inputBiasTagId;
	}

	public String getOutputBiasTagId() {
		return outputBiasTagId;
	}

	public void setOutputBiasTagId(String outputBiasTagId) {
		this.outputBiasTagId = outputBiasTagId;
	}
}
