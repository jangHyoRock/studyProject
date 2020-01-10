package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_PSO_CONF)
 */
@Entity
@Table(name = "TB_PSO_CONF")
public class PSOConfigEntity {

	public static final int defaultID = 1;

	@Id
	@Column(name = "CONF_ID")
	private int confId = defaultID;

	@Column(name = "CORRECTION_FACTOR")
	private double correctionFactor;

	@Column(name = "INERTIA")
	private double inertia;

	@Column(name = "PARTICLE_CNT_PER_MV")
	private int particleCntPerMv;

	@Column(name = "ITERATION")
	private int iteration;

	@Column(name = "PROFIT_MAX_WEIGHT")
	private String profitMaxWeight;

	@Column(name = "EMISSION_MIN_WEIGHT")
	private String emissionMinWeight;

	@Column(name = "EQUIPMENT_DURA_WEIGHT")
	private String equipmentDuraWeight;

	@Column(name = "NNMODEL_ALLOW_VALUE")
	private double nnModelAllowValue;

	@Column(name = "RH_SPRAY_K")
	private double rhSprayK;

	@Column(name = "O2_AVG_K")
	private double o2AvgK;

	@Column(name = "CO_K")
	private double coK;

	@Column(name = "NOX_K")
	private double noxK;

	@Column(name = "FGT_K")
	private double fgtK;

	@Column(name = "RH_SPRAY_D_K")
	private double rhSprayDiffK;

	@Column(name = "O2_D_K")
	private double o2DiffK;

	@Column(name = "O2_AVG_BOUNDARY")
	private double o2AvgBoundary;

	@Column(name = "O2_AVG_PENALTY_WEIGHT")
	private double o2AvgPenaltyWeight;

	@Column(name = "O2_MIN_BOUNDARY")
	private double o2MinBoundary;

	@Column(name = "O2_MIN_PENALTY_WEIGHT")
	private double o2MinPenaltyWeight;

	@Column(name = "LOAD_PENALTY_WEIGHT")
	private double loadPenaltyWeight;

	@Column(name = "LOAD_SETPOINT_PENALTY_WEIGHT")
	private double loadSetPointPenaltyWeight;

	@Column(name = "NOX_BOUNDARY")
	private double noxBoundary;

	@Column(name = "NOX_PENALTY_WEIGHT")
	private double noxPenaltyWeight;

	@Column(name = "STACK_CO_BOUNDARY")
	private double stackCoBoundary;

	@Column(name = "STACK_CO_PENALTY_WEIGHT")
	private double stackCoPenaltyWeight;

	@Column(name = "FG_TEMP_BOUNDARY")
	private double fgTempBoundary;

	@Column(name = "FG_TEMP_PENALTY_WEIGHT")
	private double fgTempPenaltyWeight;

	@Column(name = "CPU_USAGE_LIMIT_VAL")
	private int cpuUsageLimitValue;

	@Column(name = "PSO_CL_MODE")
	private String psoCLMode;

	@Column(name = "TIMESTAMP")
	private Date timestamp;

	public int getConfId() {
		return confId;
	}

	public void setConfId(int confId) {
		this.confId = confId;
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

	public int getParticleCntPerMv() {
		return particleCntPerMv;
	}

	public void setParticleCntPerMv(int particleCntPerMv) {
		this.particleCntPerMv = particleCntPerMv;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	public String getProfitMaxWeight() {
		return profitMaxWeight;
	}

	public void setProfitMaxWeight(String profitMaxWeight) {
		this.profitMaxWeight = profitMaxWeight;
	}

	public String getEmissionMinWeight() {
		return emissionMinWeight;
	}

	public void setEmissionMinWeight(String emissionMinWeight) {
		this.emissionMinWeight = emissionMinWeight;
	}

	public String getEquipmentDuraWeight() {
		return equipmentDuraWeight;
	}

	public void setEquipmentDuraWeight(String equipmentDuraWeight) {
		this.equipmentDuraWeight = equipmentDuraWeight;
	}

	public double getNnModelAllowValue() {
		return nnModelAllowValue;
	}

	public void setNnModelAllowValue(double nnModelAllowValue) {
		this.nnModelAllowValue = nnModelAllowValue;
	}

	public double getRhSprayK() {
		return rhSprayK;
	}

	public void setRhSprayK(double rhSprayK) {
		this.rhSprayK = rhSprayK;
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

	public double getRhSprayDiffK() {
		return rhSprayDiffK;
	}

	public void setRhSprayDiffK(double rhSprayDiffK) {
		this.rhSprayDiffK = rhSprayDiffK;
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

	public double getO2AvgPenaltyWeight() {
		return o2AvgPenaltyWeight;
	}

	public void setO2AvgPenaltyWeight(double o2AvgPenaltyWeight) {
		this.o2AvgPenaltyWeight = o2AvgPenaltyWeight;
	}

	public double getO2MinBoundary() {
		return o2MinBoundary;
	}

	public void setO2MinBoundary(double o2MinBoundary) {
		this.o2MinBoundary = o2MinBoundary;
	}

	public double getO2MinPenaltyWeight() {
		return o2MinPenaltyWeight;
	}

	public void setO2MinPenaltyWeight(double o2MinPenaltyWeight) {
		this.o2MinPenaltyWeight = o2MinPenaltyWeight;
	}

	public double getLoadPenaltyWeight() {
		return loadPenaltyWeight;
	}

	public void setLoadPenaltyWeight(double loadPenaltyWeight) {
		this.loadPenaltyWeight = loadPenaltyWeight;
	}

	public double getLoadSetPointPenaltyWeight() {
		return loadSetPointPenaltyWeight;
	}

	public void setLoadSetPointPenaltyWeight(double loadSetPointPenaltyWeight) {
		this.loadSetPointPenaltyWeight = loadSetPointPenaltyWeight;
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

	public String getPsoCLMode() {
		return psoCLMode;
	}

	public void setPsoCLMode(String psoCLMode) {
		this.psoCLMode = psoCLMode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
