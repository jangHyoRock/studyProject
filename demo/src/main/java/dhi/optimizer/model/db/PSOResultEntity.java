package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_PSO_RESULT)
 */
@Entity
@Table(name="TB_PSO_RESULT")
public class PSOResultEntity {

	@Id
	@Column(name="TIMESTAMP")
	private Date timestamp;
	
	@Column(name="ITERATION")
	private int iteration;
	
	@Column(name="OPTIMAL_POSITION")
	private String optimalPosition;
	
	@Column(name="OPTIMAL_F_VAL")
	private double optimalFVal;
	
	@Column(name="PSO_SYSTEM_START")
	private Boolean psoSystemStart;
	
	@Column(name="PSO_CONTROL_MODE")
	private String psoControlMode;
	
	@Column(name="PSO_OPT_MODE")
	private String psoOptMode;
	
	@Column(name="PSO_USED_MODEL_TS")
	private Date psoUsedModelTs;
	
	@Column(name="PSO_USED_MODEL_TYPE")
	private String psoUsedModelType;
	
	@Column(name="NEW_MV_TGT_YN")
	private Boolean newMVTGTYN;
	
	@Column(name="ERROR_SUM")
	private double errorSum;
	
	@Column(name="OP_DATA_F_VAL")
	private double opDataFValue;
	
	@Column(name="PSO_INPUT_DATA")
	private String psoInputData;
	
	@Column(name="PSO_OUTPUT_DATA")
	private String psoOutputData;
	
	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getIteration() {
		return this.iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public String getOptimalPosition() {
		return this.optimalPosition;
	}

	public void setOptimalPosition(String optimalPosition) {
		this.optimalPosition = optimalPosition;
	}

	public double getOptimalFVal() {
		return this.optimalFVal;
	}

	public void setOptimalFVal(double optimalFVal) {
		this.optimalFVal = optimalFVal;
	}

	public Boolean getPsoSystemStart() {
		return psoSystemStart;
	}

	public void setPsoSystemStart(Boolean psoSystemStart) {
		this.psoSystemStart = psoSystemStart;
	}

	public String getPsoControlMode() {
		return psoControlMode;
	}

	public void setPsoControlMode(String psoControlMode) {
		this.psoControlMode = psoControlMode;
	}

	public String getPsoOptMode() {
		return psoOptMode;
	}

	public void setPsoOptMode(String psoOptMode) {
		this.psoOptMode = psoOptMode;
	}

	public Date getPsoUsedModelTs() {
		return psoUsedModelTs;
	}

	public void setPsoUsedModelTs(Date psoUsedModelTs) {
		this.psoUsedModelTs = psoUsedModelTs;
	}

	public String getPsoUsedModelType() {
		return psoUsedModelType;
	}

	public void setPsoUsedModelType(String psoUsedModelType) {
		this.psoUsedModelType = psoUsedModelType;
	}

	public Boolean getNewMVTGTYN() {
		return newMVTGTYN;
	}

	public void setNewMVTGTYN(Boolean newMVTGTYN) {
		this.newMVTGTYN = newMVTGTYN;
	}

	public double getErrorSum() {
		return errorSum;
	}

	public void setErrorSum(double errorSum) {
		this.errorSum = errorSum;
	}

	public double getOpDataFValue() {
		return opDataFValue;
	}

	public void setOpDataFValue(double opDataFValue) {
		this.opDataFValue = opDataFValue;
	}

	public String getPsoInputData() {
		return psoInputData;
	}

	public void setPsoInputData(String psoInputData) {
		this.psoInputData = psoInputData;
	}

	public String getPsoOutputData() {
		return psoOutputData;
	}

	public void setPsoOutputData(String psoOutputData) {
		this.psoOutputData = psoOutputData;
	}
}
