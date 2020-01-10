package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_OUTPUT_BIAS_CONF)
 */
@Entity
@Table(name="TB_OUTPUT_BIAS_CONF")
public class OutputBiasConfEntity {

	public static final int defaultID = 1;

	@Id
	@Column(name="CONF_ID")
	private int confId = defaultID;
	
	@Column(name="BNR_DAMPER_BIAS_CHG_PERMIT_VAL")
	private Double bnrDamperBiasChgPermitVal;
	
	@Column(name="BNR_DAMPER_BIAS_MIN")
	private Double bnrDamperBiasMin;
	
	@Column(name="BNR_DAMPER_BIAS_MAX")
	private Double bnrDamperBiasMax;
	
	@Column(name="OFA_DAMPER_BIAS_CHG_PERMIT_VAL")
	private Double ofaDamperBiasChgPermitVal;
	
	@Column(name="OFA_DAMPER_BIAS_MIN")
	private Double ofaDamperBiasMin;
	
	@Column(name="OFA_DAMPER_BIAS_MAX")
	private Double ofaDamperBiasMax;
	
	@Column(name="AIR_BIAS_CER3")
	private Double airBiasCer3;
	
	@Column(name="AIR_BIAS_CER2")
	private Double airBiasCer2;
	
	@Column(name="AIR_BIAS_CER1")
	private Double airBiasCer1;
	
	@Column(name="AIR_BIAS_CER0")
	private Double airBiasCer0;
	
	@Column(name="AIR_BIAS_NFO3")
	private Double airBiasNfo3;
	
	@Column(name="AIR_BIAS_NFO2")
	private Double airBiasNfo2;
	
	@Column(name="AIR_BIAS_NFO1")
	private Double airBiasNfo1;
	
	@Column(name="AIR_BIAS_NFO0")
	private Double airBiasNfo0;
	
	@Column(name="AIR_BIAS_CHG_PERMIT_VAL")
	private Double airBiasChgPermitVal;
	
	@Column(name="AIR_BIAS_MIN")
	private Double airBiasMin;
	
	@Column(name="AIR_BIAS_MAX")
	private Double airBiasMax;

	public int getConfId() {
		return confId;
	}

	public void setConfId(int confId) {
		this.confId = confId;
	}

	public Double getBnrDamperBiasChgPermitVal() {
		return bnrDamperBiasChgPermitVal;
	}

	public void setBnrDamperBiasChgPermitVal(Double bnrDamperBiasChgPermitVal) {
		this.bnrDamperBiasChgPermitVal = bnrDamperBiasChgPermitVal;
	}

	public Double getBnrDamperBiasMin() {
		return bnrDamperBiasMin;
	}

	public void setBnrDamperBiasMin(Double bnrDamperBiasMin) {
		this.bnrDamperBiasMin = bnrDamperBiasMin;
	}

	public Double getBnrDamperBiasMax() {
		return bnrDamperBiasMax;
	}

	public void setBnrDamperBiasMax(Double bnrDamperBiasMax) {
		this.bnrDamperBiasMax = bnrDamperBiasMax;
	}

	public Double getOfaDamperBiasChgPermitVal() {
		return ofaDamperBiasChgPermitVal;
	}

	public void setOfaDamperBiasChgPermitVal(Double ofaDamperBiasChgPermitVal) {
		this.ofaDamperBiasChgPermitVal = ofaDamperBiasChgPermitVal;
	}
	
	public Double getOfaDamperBiasMin() {
		return ofaDamperBiasMin;
	}

	public void setOfaDamperBiasMin(Double ofaDamperBiasMin) {
		this.ofaDamperBiasMin = ofaDamperBiasMin;
	}

	public Double getOfaDamperBiasMax() {
		return ofaDamperBiasMax;
	}

	public void setOfaDamperBiasMax(Double ofaDamperBiasMax) {
		this.ofaDamperBiasMax = ofaDamperBiasMax;
	}

	public Double getAirBiasCer3() {
		return airBiasCer3;
	}

	public void setAirBiasCer3(Double airBiasCer3) {
		this.airBiasCer3 = airBiasCer3;
	}

	public Double getAirBiasCer2() {
		return airBiasCer2;
	}

	public void setAirBiasCer2(Double airBiasCer2) {
		this.airBiasCer2 = airBiasCer2;
	}

	public Double getAirBiasCer1() {
		return airBiasCer1;
	}

	public void setAirBiasCer1(Double airBiasCer1) {
		this.airBiasCer1 = airBiasCer1;
	}

	public Double getAirBiasCer0() {
		return airBiasCer0;
	}

	public void setAirBiasCer0(Double airBiasCer0) {
		this.airBiasCer0 = airBiasCer0;
	}

	public Double getAirBiasNfo3() {
		return airBiasNfo3;
	}

	public void setAirBiasNfo3(Double airBiasNfo3) {
		this.airBiasNfo3 = airBiasNfo3;
	}

	public Double getAirBiasNfo2() {
		return airBiasNfo2;
	}

	public void setAirBiasNfo2(Double airBiasNfo2) {
		this.airBiasNfo2 = airBiasNfo2;
	}

	public Double getAirBiasNfo1() {
		return airBiasNfo1;
	}

	public void setAirBiasNfo1(Double airBiasNfo1) {
		this.airBiasNfo1 = airBiasNfo1;
	}

	public Double getAirBiasNfo0() {
		return airBiasNfo0;
	}

	public void setAirBiasNfo0(Double airBiasNfo0) {
		this.airBiasNfo0 = airBiasNfo0;
	}

	public Double getAirBiasChgPermitVal() {
		return airBiasChgPermitVal;
	}

	public void setAirBiasChgPermitVal(Double airBiasChgPermitVal) {
		this.airBiasChgPermitVal = airBiasChgPermitVal;
	}
	
	public Double getAirBiasMin() {
		return airBiasMin;
	}

	public void setAirBiasMin(Double airBiasMin) {
		this.airBiasMin = airBiasMin;
	}

	public Double getAirBiasMax() {
		return airBiasMax;
	}

	public void setAirBiasMax(Double airBiasMax) {
		this.airBiasMax = airBiasMax;
	}
}
