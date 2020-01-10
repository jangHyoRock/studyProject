package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithOutputControllerConfig {

	@JsonProperty("dv_tag_change_value")
	private double dvTagChangValue;

	@JsonProperty("dv_tag_change_rate")
	private double dvTagChangRate;

	@JsonProperty("mv_damper_change_rate")
	private double mvDamperChangeRate;

	@JsonProperty("mv_damper_star_change_rate")
	private double mvDamperStarChangeRate;

	@JsonProperty("mv_total_air_change_rate")
	private double mvTotalAirChangeRate;

	@JsonProperty("mv_total_air_star_change_rate")
	private double mvTotalAirStarChangeRate;

	@JsonProperty("burner_permit_value_per_minutes")
	private double burnerPermitValuePerMinutes;

	@JsonProperty("burner_bias_min_value")
	private double burnerBiasMinValue;

	@JsonProperty("burner_bias_max_value")
	private double burnerBiasMaxValue;

	@JsonProperty("ofa_permit_value_per_minutes")
	private double ofaPermitValuePerMinutes;

	@JsonProperty("ofa_bias_min_value")
	private double ofaBiasMinValue;

	@JsonProperty("ofa_bias_max_value")
	private double ofaBiasMaxValue;

	@JsonProperty("total_air_permit_value_per_minutes")
	private double totalAirPermitValuePerMinutes;

	@JsonProperty("total_air_bias_min_value")
	private double totalAirBiasMinValue;

	@JsonProperty("total_air_bias_max_value")
	private double totalAirBiasMaxValue;

	@JsonProperty("total_air_cer1")
	private double totalAirCER1;

	@JsonProperty("total_air_cer2")
	private double totalAirCER2;

	@JsonProperty("total_air_cer3")
	private double totalAirCER3;

	@JsonProperty("total_air_cer4")
	private double totalAirCER4;

	@JsonProperty("total_air_nfo1")
	private double totalAirNFO1;

	@JsonProperty("total_air_nfo2")
	private double totalAirNFO2;

	@JsonProperty("total_air_nfo3")
	private double totalAirNFO3;

	@JsonProperty("total_air_nfo4")
	private double totalAirNFO4;

	public double getDvTagChangValue() {
		return dvTagChangValue;
	}

	public void setDvTagChangValue(double dvTagChangValue) {
		this.dvTagChangValue = dvTagChangValue;
	}

	public double getDvTagChangRate() {
		return dvTagChangRate;
	}

	public void setDvTagChangRate(double dvTagChangRate) {
		this.dvTagChangRate = dvTagChangRate;
	}

	public double getMvDamperChangeRate() {
		return mvDamperChangeRate;
	}

	public void setMvDamperChangeRate(double mvDamperChangeRate) {
		this.mvDamperChangeRate = mvDamperChangeRate;
	}

	public double getMvDamperStarChangeRate() {
		return mvDamperStarChangeRate;
	}

	public void setMvDamperStarChangeRate(double mvDamperStarChangeRate) {
		this.mvDamperStarChangeRate = mvDamperStarChangeRate;
	}

	public double getMvTotalAirChangeRate() {
		return mvTotalAirChangeRate;
	}

	public void setMvTotalAirChangeRate(double mvTotalAirChangeRate) {
		this.mvTotalAirChangeRate = mvTotalAirChangeRate;
	}

	public double getMvTotalAirStarChangeRate() {
		return mvTotalAirStarChangeRate;
	}

	public void setMvTotalAirStarChangeRate(double mvTotalAirStarChangeRate) {
		this.mvTotalAirStarChangeRate = mvTotalAirStarChangeRate;
	}

	public double getBurnerPermitValuePerMinutes() {
		return burnerPermitValuePerMinutes;
	}

	public void setBurnerPermitValuePerMinutes(double burnerPermitValuePerMinutes) {
		this.burnerPermitValuePerMinutes = burnerPermitValuePerMinutes;
	}

	public double getBurnerBiasMinValue() {
		return burnerBiasMinValue;
	}

	public void setBurnerBiasMinValue(double burnerBiasMinValue) {
		this.burnerBiasMinValue = burnerBiasMinValue;
	}

	public double getBurnerBiasMaxValue() {
		return burnerBiasMaxValue;
	}

	public void setBurnerBiasMaxValue(double burnerBiasMaxValue) {
		this.burnerBiasMaxValue = burnerBiasMaxValue;
	}

	public double getOfaPermitValuePerMinutes() {
		return ofaPermitValuePerMinutes;
	}

	public void setOfaPermitValuePerMinutes(double ofaPermitValuePerMinutes) {
		this.ofaPermitValuePerMinutes = ofaPermitValuePerMinutes;
	}

	public double getOfaBiasMinValue() {
		return ofaBiasMinValue;
	}

	public void setOfaBiasMinValue(double ofaBiasMinValue) {
		this.ofaBiasMinValue = ofaBiasMinValue;
	}

	public double getOfaBiasMaxValue() {
		return ofaBiasMaxValue;
	}

	public void setOfaBiasMaxValue(double ofaBiasMaxValue) {
		this.ofaBiasMaxValue = ofaBiasMaxValue;
	}

	public double getTotalAirPermitValuePerMinutes() {
		return totalAirPermitValuePerMinutes;
	}

	public void setTotalAirPermitValuePerMinutes(double totalAirPermitValuePerMinutes) {
		this.totalAirPermitValuePerMinutes = totalAirPermitValuePerMinutes;
	}

	public double getTotalAirBiasMinValue() {
		return totalAirBiasMinValue;
	}

	public void setTotalAirBiasMinValue(double totalAirBiasMinValue) {
		this.totalAirBiasMinValue = totalAirBiasMinValue;
	}

	public double getTotalAirBiasMaxValue() {
		return totalAirBiasMaxValue;
	}

	public void setTotalAirBiasMaxValue(double totalAirBiasMaxValue) {
		this.totalAirBiasMaxValue = totalAirBiasMaxValue;
	}

	public double getTotalAirCER1() {
		return totalAirCER1;
	}

	public void setTotalAirCER1(double totalAirCER1) {
		this.totalAirCER1 = totalAirCER1;
	}

	public double getTotalAirCER2() {
		return totalAirCER2;
	}

	public void setTotalAirCER2(double totalAirCER2) {
		this.totalAirCER2 = totalAirCER2;
	}

	public double getTotalAirCER3() {
		return totalAirCER3;
	}

	public void setTotalAirCER3(double totalAirCER3) {
		this.totalAirCER3 = totalAirCER3;
	}

	public double getTotalAirCER4() {
		return totalAirCER4;
	}

	public void setTotalAirCER4(double totalAirCER4) {
		this.totalAirCER4 = totalAirCER4;
	}

	public double getTotalAirNFO1() {
		return totalAirNFO1;
	}

	public void setTotalAirNFO1(double totalAirNFO1) {
		this.totalAirNFO1 = totalAirNFO1;
	}

	public double getTotalAirNFO2() {
		return totalAirNFO2;
	}

	public void setTotalAirNFO2(double totalAirNFO2) {
		this.totalAirNFO2 = totalAirNFO2;
	}

	public double getTotalAirNFO3() {
		return totalAirNFO3;
	}

	public void setTotalAirNFO3(double totalAirNFO3) {
		this.totalAirNFO3 = totalAirNFO3;
	}

	public double getTotalAirNFO4() {
		return totalAirNFO4;
	}

	public void setTotalAirNFO4(double totalAirNFO4) {
		this.totalAirNFO4 = totalAirNFO4;
	}
}
