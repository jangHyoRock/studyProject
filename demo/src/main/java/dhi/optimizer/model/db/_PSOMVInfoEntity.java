package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DD_TB_PSO_MV_INFO")
public class _PSOMVInfoEntity {

	@Id
	@Column(name = "PSO_MV")
	private String psoMV;

	@Column(name = "PSO_MV_TYPE")
	private String psoMVType;

	@Column(name = "PSO_MV_MAX")
	private Double psoMVMax;

	@Column(name = "PSO_MV_MIN")
	private Double psoMVMin;

	@Column(name = "PSO_MV_ORDER")
	private int psoMVOrder;

	@Column(name = "AUTO_MODE_TAG_ID")
	private String autoModeTagId;

	@Column(name = "HOLD_TAG_ID")
	private String holdTagId;

	@Column(name = "INPUT_BIAS_TAG_ID")
	private String inputBiasTagId;

	@Column(name = "OUTPUT_BIAS_TAG_ID")
	private String outputBiasTagId;

	@Column(name = "DCS_USE_YN")
	private String dcsUseYn;
	
	@Column(name = "PSO_ID")
	private String psoId;

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

	public String getDcsUseYn() {
		return dcsUseYn;
	}

	public void setDcsUseYn(String dcsUseYn) {
		this.dcsUseYn = dcsUseYn;
	}

	public String getPsoId() {
		return psoId;
	}

	public void setPsoId(String psoId) {
		this.psoId = psoId;
	}
}
