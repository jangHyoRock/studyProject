package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NAV_SETTING)
 */
@Entity
@Table(name="TB_NAV_SETTING")
public class NavSettingEntity {

	public static final int defaultID = 1;

	@Id
	@Column(name="SETTING_ID")
	private int settingId = defaultID;
		
	@Column(name="EXCESS_AIR_RATIO")
	private double excessAirRatio;
	
	@Column(name="METAL_TEMP_DEV_RATE")
	private double metalTempDevRate;
	
	@Column(name="ALLOWABLE_METAL_TEMP")
	private double allowableMetalTemp;
	
	@Column(name="ALLOWABLE_FLUE_GAS_TEMP_DEV")
	private double allowableFlueGasTempDev;
	
	@Column(name="ALLOWABLE_SPRAY_FLOW_DEV")
	private double allowableSprayFlowDev;

	public double getExcessAirRatio() {
		return excessAirRatio;
	}

	public void setExcessAirRatio(double excessAirRatio) {
		this.excessAirRatio = excessAirRatio;
	}

	public double getMetalTempDevRate() {
		return metalTempDevRate;
	}

	public void setMetalTempDevRate(double metalTempDevRate) {
		this.metalTempDevRate = metalTempDevRate;
	}

	public double getAllowableMetalTemp() {
		return allowableMetalTemp;
	}

	public void setAllowableMetalTemp(double allowableMetalTemp) {
		this.allowableMetalTemp = allowableMetalTemp;
	}

	public double getAllowableFlueGasTempDev() {
		return allowableFlueGasTempDev;
	}

	public void setAllowableFlueGasTempDev(double allowableFlueGasTempDev) {
		this.allowableFlueGasTempDev = allowableFlueGasTempDev;
	}

	public double getAllowableSprayFlowDev() {
		return allowableSprayFlowDev;
	}

	public void setAllowableSprayFlowDev(double allowableSprayFlowDev) {
		this.allowableSprayFlowDev = allowableSprayFlowDev;
	}	
}
