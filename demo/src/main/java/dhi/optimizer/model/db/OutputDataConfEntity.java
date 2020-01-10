package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_OUTPUT_DATA_CONF)
 */
@Entity
@Table(name="TB_OUTPUT_DATA_CONF")
public class OutputDataConfEntity {

	public static final int defaultID = 1;

	@Id
	@Column(name="CONF_ID")
	private int confId = defaultID;
		
	@Column(name="DV_MIN_CHG_VAL")
	private double dvMinChgVal;
	
	@Column(name="DV_TAG_CHG_RATE")
	private double dvTagChgRate;
	
	@Column(name="MV_DAMPER_CHG_RATE")
	private double mvDamperChgRate;
	
	@Column(name="MV_AIR_CHG_RATE")
	private double mvAirChgRate;
	
	@Column(name="MV_STAR_DAMPER_CHG_RATE")
	private double mvStarDamperChgRate;
	
	@Column(name="MV_STAR_AIR_CHG_RATE")
	private double mvStarAirChgRate;

	public int getConfId() {
		return confId;
	}

	public void setConfId(int confId) {
		this.confId = confId;
	}

	public double getDvMinChgVal() {
		return dvMinChgVal;
	}

	public void setDvMinChgVal(double dvMinChgVal) {
		this.dvMinChgVal = dvMinChgVal;
	}

	public double getDvTagChgRate() {
		return dvTagChgRate;
	}

	public void setDvTagChgRate(double dvTagChgRate) {
		this.dvTagChgRate = dvTagChgRate;
	}

	public double getMvDamperChgRate() {
		return mvDamperChgRate;
	}

	public void setMvDamperChgRate(double mvDamperChgRate) {
		this.mvDamperChgRate = mvDamperChgRate;
	}

	public double getMvAirChgRate() {
		return mvAirChgRate;
	}

	public void setMvAirChgRate(double mvAirChgRate) {
		this.mvAirChgRate = mvAirChgRate;
	}

	public double getMvStarDamperChgRate() {
		return mvStarDamperChgRate;
	}

	public void setMvStarDamperChgRate(double mvStarDamperChgRate) {
		this.mvStarDamperChgRate = mvStarDamperChgRate;
	}

	public double getMvStarAirChgRate() {
		return mvStarAirChgRate;
	}

	public void setMvStarAirChgRate(double mvStarAirChgRate) {
		this.mvStarAirChgRate = mvStarAirChgRate;
	}

	public static int getDefaultid() {
		return defaultID;
	}
}
