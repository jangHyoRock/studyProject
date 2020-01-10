package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgorithmPSOConfig {

	@JsonProperty("pso_control_mode")
	private String psoControlMode;

	@JsonProperty("correction_factor")
	private double correctionFactor;

	private double inertia;

	@JsonProperty("mv_count")
	private int mvCount;

	@JsonProperty("particles_count")
	private int particlesCount;

	private int iterations;

	@JsonProperty("opt_mode")
	private String optMode;

	@JsonProperty("opt_profit_max_profit_weight")
	private double optProfitMaxProfitWeight;

	@JsonProperty("opt_profit_max_emission_weight")
	private double optProfitMaxEmissionWeight;

	@JsonProperty("opt_profit_max_equipment_weight")
	private double optProfitMaxEquipmentWeight;

	@JsonProperty("opt_emission_min_profit_weight")
	private double optEmissionMinProfitWeight;

	@JsonProperty("opt_emission_min_emission_weight")
	private double optEmissionMinEmissionWeight;

	@JsonProperty("opt_emission_min_equipment_weight")
	private double optEmissionMinEquipmentWeight;

	@JsonProperty("opt_equipment_dura_profit_weight")
	private double optEquipmentDuraProfitWeight;

	@JsonProperty("opt_equipment_dura_emission_weight")
	private double optEquipmentDuraEmissionWeight;

	@JsonProperty("opt_equipment_dura_equipment_weight")
	private double optEquipmentDuraEquipmentWeight;

	@JsonProperty("nn_model_allow_value")
	private double nnModelAllowValue;

	@JsonProperty("function_rh_spary_k")
	private double rhSparyK;

	@JsonProperty("function_o2_avg_k")
	private double o2AvgK;

	@JsonProperty("function_co_k")
	private double coK;

	@JsonProperty("function_nox_k")
	private double noxK;

	@JsonProperty("function_fgt_k")
	private double fgtK;

	@JsonProperty("function_rh_spary_d_k")
	private double rhSparyDiffK;

	@JsonProperty("function_o2_d_k")
	private double o2DiffK;

	@JsonProperty("function_o2_avg_boundary")
	private double o2AvgBoundary;

	@JsonProperty("function_o2_avg_penaly_weight")
	private double o2AvgPenalyWeight;

	@JsonProperty("function_o2_min_boundary")
	private double o2MinBoundary;

	@JsonProperty("function_o2_min_penaly_weight")
	private double o2MinPenalyWeight;

	@JsonProperty("function_load_penaly_weight")
	private double loadPenalyWeight;

	@JsonProperty("function_load_setpont_penaly_weight")
	private double loadSetPontPenalyWeight;

	@JsonProperty("function_nox_boundary")
	private double noxBoundary;

	@JsonProperty("function_nox_penalty_weight")
	private double noxPenaltyWeight;

	@JsonProperty("function_stack_co_boundary")
	private double stackCoBoundary;

	@JsonProperty("function_stack_co_penalty_weight")
	private double stackCoPenaltyWeight;

	@JsonProperty("function_fg_temp_boundary")
	private double fgTempBoundary;

	@JsonProperty("function_fg_temp_penalty_weight")
	private double fgTempPenaltyWeight;

	@JsonProperty("cpu_usage_limit_value")
	private int cpuUsageLimitValue;

	@JsonProperty("cpu_usage_limit_max_value")
	private int cpuUsageLimitMaxValue;

	public String getPsoControlMode() {
		return psoControlMode;
	}

	public void setPsoControlMode(String psoControlMode) {
		this.psoControlMode = psoControlMode;
	}

	public double getCorrectionFactor() {
		return correctionFactor;
	}

	public void setCorrectionFactor(double correctionFactor) {
		this.correctionFactor = correctionFactor;
	}

	public double getInertia() {
		return inertia;
	}

	public void setInertia(double inertia) {
		this.inertia = inertia;
	}

	public int getMvCount() {
		return mvCount;
	}

	public void setMvCount(int mvCount) {
		this.mvCount = mvCount;
	}

	public int getParticlesCount() {
		return particlesCount;
	}

	public void setParticlesCount(int particlesCount) {
		this.particlesCount = particlesCount;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public String getOptMode() {
		return optMode;
	}

	public void setOptMode(String optMode) {
		this.optMode = optMode;
	}

	public double getOptProfitMaxProfitWeight() {
		return optProfitMaxProfitWeight;
	}

	public void setOptProfitMaxProfitWeight(double optProfitMaxProfitWeight) {
		this.optProfitMaxProfitWeight = optProfitMaxProfitWeight;
	}

	public double getOptProfitMaxEmissionWeight() {
		return optProfitMaxEmissionWeight;
	}

	public void setOptProfitMaxEmissionWeight(double optProfitMaxEmissionWeight) {
		this.optProfitMaxEmissionWeight = optProfitMaxEmissionWeight;
	}

	public double getOptProfitMaxEquipmentWeight() {
		return optProfitMaxEquipmentWeight;
	}

	public void setOptProfitMaxEquipmentWeight(double optProfitMaxEquipmentWeight) {
		this.optProfitMaxEquipmentWeight = optProfitMaxEquipmentWeight;
	}

	public double getOptEmissionMinProfitWeight() {
		return optEmissionMinProfitWeight;
	}

	public void setOptEmissionMinProfitWeight(double optEmissionMinProfitWeight) {
		this.optEmissionMinProfitWeight = optEmissionMinProfitWeight;
	}

	public double getOptEmissionMinEmissionWeight() {
		return optEmissionMinEmissionWeight;
	}

	public void setOptEmissionMinEmissionWeight(double optEmissionMinEmissionWeight) {
		this.optEmissionMinEmissionWeight = optEmissionMinEmissionWeight;
	}

	public double getOptEmissionMinEquipmentWeight() {
		return optEmissionMinEquipmentWeight;
	}

	public void setOptEmissionMinEquipmentWeight(double optEmissionMinEquipmentWeight) {
		this.optEmissionMinEquipmentWeight = optEmissionMinEquipmentWeight;
	}

	public double getOptEquipmentDuraProfitWeight() {
		return optEquipmentDuraProfitWeight;
	}

	public void setOptEquipmentDuraProfitWeight(double optEquipmentDuraProfitWeight) {
		this.optEquipmentDuraProfitWeight = optEquipmentDuraProfitWeight;
	}

	public double getOptEquipmentDuraEmissionWeight() {
		return optEquipmentDuraEmissionWeight;
	}

	public void setOptEquipmentDuraEmissionWeight(double optEquipmentDuraEmissionWeight) {
		this.optEquipmentDuraEmissionWeight = optEquipmentDuraEmissionWeight;
	}

	public double getOptEquipmentDuraEquipmentWeight() {
		return optEquipmentDuraEquipmentWeight;
	}

	public void setOptEquipmentDuraEquipmentWeight(double optEquipmentDuraEquipmentWeight) {
		this.optEquipmentDuraEquipmentWeight = optEquipmentDuraEquipmentWeight;
	}

	public double getNnModelAllowValue() {
		return nnModelAllowValue;
	}

	public void setNnModelAllowValue(double nnModelAllowValue) {
		this.nnModelAllowValue = nnModelAllowValue;
	}

	public double getRhSparyK() {
		return rhSparyK;
	}

	public void setRhSparyK(double rhSparyK) {
		this.rhSparyK = rhSparyK;
	}

	public double getO2AvgK() {
		return o2AvgK;
	}

	public void setO2AvgK(double o2AvgK) {
		this.o2AvgK = o2AvgK;
	}

	public double getCoK() {
		return coK;
	}

	public void setCoK(double coK) {
		this.coK = coK;
	}

	public double getNoxK() {
		return noxK;
	}

	public void setNoxK(double noxK) {
		this.noxK = noxK;
	}

	public double getFgtK() {
		return fgtK;
	}

	public void setFgtK(double fgtK) {
		this.fgtK = fgtK;
	}

	public double getRhSparyDiffK() {
		return rhSparyDiffK;
	}

	public void setRhSparyDiffK(double rhSparyDiffK) {
		this.rhSparyDiffK = rhSparyDiffK;
	}

	public double getO2DiffK() {
		return o2DiffK;
	}

	public void setO2DiffK(double o2DiffK) {
		this.o2DiffK = o2DiffK;
	}

	public double getO2AvgBoundary() {
		return o2AvgBoundary;
	}

	public void setO2AvgBoundary(double o2AvgBoundary) {
		this.o2AvgBoundary = o2AvgBoundary;
	}

	public double getO2AvgPenalyWeight() {
		return o2AvgPenalyWeight;
	}

	public void setO2AvgPenalyWeight(double o2AvgPenalyWeight) {
		this.o2AvgPenalyWeight = o2AvgPenalyWeight;
	}

	public double getO2MinBoundary() {
		return o2MinBoundary;
	}

	public void setO2MinBoundary(double o2MinBoundary) {
		this.o2MinBoundary = o2MinBoundary;
	}

	public double getO2MinPenalyWeight() {
		return o2MinPenalyWeight;
	}

	public void setO2MinPenalyWeight(double o2MinPenalyWeight) {
		this.o2MinPenalyWeight = o2MinPenalyWeight;
	}

	public double getLoadPenalyWeight() {
		return loadPenalyWeight;
	}

	public void setLoadPenalyWeight(double loadPenalyWeight) {
		this.loadPenalyWeight = loadPenalyWeight;
	}

	public double getLoadSetPontPenalyWeight() {
		return loadSetPontPenalyWeight;
	}

	public void setLoadSetPontPenalyWeight(double loadSetPontPenalyWeight) {
		this.loadSetPontPenalyWeight = loadSetPontPenalyWeight;
	}

	public double getNoxBoundary() {
		return noxBoundary;
	}

	public void setNoxBoundary(double noxBoundary) {
		this.noxBoundary = noxBoundary;
	}

	public double getNoxPenaltyWeight() {
		return noxPenaltyWeight;
	}

	public void setNoxPenaltyWeight(double noxPenaltyWeight) {
		this.noxPenaltyWeight = noxPenaltyWeight;
	}

	public double getStackCoBoundary() {
		return stackCoBoundary;
	}

	public void setStackCoBoundary(double stackCoBoundary) {
		this.stackCoBoundary = stackCoBoundary;
	}

	public double getStackCoPenaltyWeight() {
		return stackCoPenaltyWeight;
	}

	public void setStackCoPenaltyWeight(double stackCoPenaltyWeight) {
		this.stackCoPenaltyWeight = stackCoPenaltyWeight;
	}

	public double getFgTempBoundary() {
		return fgTempBoundary;
	}

	public void setFgTempBoundary(double fgTempBoundary) {
		this.fgTempBoundary = fgTempBoundary;
	}

	public double getFgTempPenaltyWeight() {
		return fgTempPenaltyWeight;
	}

	public void setFgTempPenaltyWeight(double fgTempPenaltyWeight) {
		this.fgTempPenaltyWeight = fgTempPenaltyWeight;
	}

	public int getCpuUsageLimitValue() {
		return cpuUsageLimitValue;
	}

	public void setCpuUsageLimitValue(int cpuUsageLimitValue) {
		this.cpuUsageLimitValue = cpuUsageLimitValue;
	}

	public int getCpuUsageLimitMaxValue() {
		return cpuUsageLimitMaxValue;
	}

	public void setCpuUsageLimitMaxValue(int cpuUsageLimitMaxValue) {
		this.cpuUsageLimitMaxValue = cpuUsageLimitMaxValue;
	}
}
