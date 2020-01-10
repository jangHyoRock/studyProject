package dhi.optimizer.algorithm.pso;

import java.util.List;

import dhi.optimizer.common.CommonConst;
import dhi.optimizer.enumeration.PsoOptimizationFunctionModeStatus;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.model.db.PSOConfigEntity;

public class PsoCalculationFunction {

	private List<PsoTag> outputTagList;
	
	// PSO configuration Info.
	private double profitWeight = 0.0;
	private double emissionWeight = 0.0;
	private double equipmentWeight = 0.0;
	
	private double rhSparyK = 200 / 16.5;
	private double o2AvgK = 135 / 0.5;
	private double coK = 10 / 10;

	private double noxK = 20 / 30.6;
	private double fgtK = 50 / 10;
	private double rhSparyDiffK = 10 / 5; // K_RHSpray-D. (K_RHSprayDiff)
	private double o2DiffK = 100;
	
	private double o2AvgBoundary = 2.8;
	private double o2AvgPenalyWeight = 100;
	
	private double o2MinBoundary = 2.5;
	private double o2MinPenalyWeight = 100; 
	
	private double loadPenaltyWeight = 5;
	private double loadSetPointPenaltyWeight = 5;
	
	private double noxBoundary = 200;
	private double noxPenaltyWeight = 1;
	
	private double stackCoBoundary = 100;
	private double stackCoPenaltyWeight  = 2;
	
	private double fgTempBoundary = 915;
	private double fgTempPenaltyWeight  = 100;
	
	// RH Spray Flow
	private int rhMDshSprayFlowLeftIndex;
	private int rhMDshSprayFlowRightIndex;	
	private int rhEDshSprayFlowLeftIndex;
	private int rhEDshSprayFlowRightIndex;
	private int rhSprayCumulativeFlowIndex;
		
	// Econ Out Flue Gas O2.
	private int[] econOutFlueGasO2RightIndexs;
	private int[] econOutFlueGasO2LeftIndexs;
			
	private int horizonGasLeftTempIndex; 
	private int horizonGasRightTempIndex;
	private int noxCapabilityIndex;
	private int coCapabilityIndex;
	
	private int econOutFlueGasO2Right1Index;
	private int econOutFlueGasO2Right2Index;
	private int econOutFlueGasO2Left1Index;
	private int econOutFlueGasO2Left2Index; 
	private int activePowerOfGeneratorIndex;	
	
	private double opOutputLoadMW;
	private double opInputLoadSetPointMW;
			
	public double getProfitWeight() {
		return profitWeight;
	}

	public double getEmissionWeight() {
		return emissionWeight;
	}

	public double getEquipmentWeight() {
		return equipmentWeight;
	}

	public double getRhSparyK() {
		return rhSparyK;
	}

	public double getO2AvgK() {
		return o2AvgK;
	}

	public double getCoK() {
		return coK;
	}

	public double getNoxK() {
		return noxK;
	}

	public double getFgtK() {
		return fgtK;
	}

	public double getRhSparyDiffK() {
		return rhSparyDiffK;
	}

	public double getO2DiffK() {
		return o2DiffK;
	}

	public double getO2AvgBoundary() {
		return o2AvgBoundary;
	}

	public double getO2AvgPenalyWeight() {
		return o2AvgPenalyWeight;
	}

	public double getO2MinBoundary() {
		return o2MinBoundary;
	}

	public double getO2MinPenalyWeight() {
		return o2MinPenalyWeight;
	}

	public double getLoadPenaltyWeight() {
		return loadPenaltyWeight;
	}

	public double getLoadSetPointPenaltyWeight() {
		return loadSetPointPenaltyWeight;
	}

	public double getNoxBoundary() {
		return noxBoundary;
	}

	public double getNoxPenaltyWeight() {
		return noxPenaltyWeight;
	}

	public double getStackCoBoundary() {
		return stackCoBoundary;
	}
	
	public double getStackCoPenaltyWeight() {
		return stackCoPenaltyWeight;
	}

	public double getFgTempBoundary() {
		return fgTempBoundary;
	}

	public double getFgTempPenaltyWeight() {
		return fgTempPenaltyWeight;
	}

	public double getOpOutputLoadMW() {
		return opOutputLoadMW;
	}
	
	public double getOpInputLoadSetPointMW() {
		return opInputLoadSetPointMW;
	}
	
	public PsoCalculationFunction(List<PsoTag> outputTagList, ControlEntity controlEntity, PSOConfigEntity psoConfigEntity, double opOutputLoadMW, double opInputLoadSetPointMW) {
		this.outputTagList = outputTagList;

		String weightValues = CommonConst.StringEmpty;
		PsoOptimizationFunctionModeStatus controlOptMode = Enum.valueOf(PsoOptimizationFunctionModeStatus.class, controlEntity.getOptMode());
		switch (controlOptMode) {
		case P:
			weightValues = psoConfigEntity.getProfitMaxWeight();
			break;
		case E:
			weightValues = psoConfigEntity.getEmissionMinWeight();
			break;
		case S:
			weightValues = psoConfigEntity.getEquipmentDuraWeight();
			break;
		}
		
		String[] arrayWeightValues = weightValues.split(":");
		this.profitWeight = Double.parseDouble(arrayWeightValues[0]);
		this.emissionWeight = Double.parseDouble(arrayWeightValues[1]);
		this.equipmentWeight = Double.parseDouble(arrayWeightValues[2]);
		
		this.rhSparyK = psoConfigEntity.getRhSprayK();
		this.o2AvgK = psoConfigEntity.getO2AvgK();
		this.coK = psoConfigEntity.getCoK();
		
		this.noxK = psoConfigEntity.getNoxK();
		this.fgtK = psoConfigEntity.getFgtK();
		this.rhSparyDiffK = psoConfigEntity.getRhSprayDiffK();
		this.o2DiffK = psoConfigEntity.getO2DiffK();
		
		this.o2AvgBoundary = psoConfigEntity.getO2AvgBoundary();
		this.o2AvgPenalyWeight = psoConfigEntity.getO2AvgPenaltyWeight();
		
		this.o2MinBoundary = psoConfigEntity.getO2AvgBoundary();
		this.o2MinPenalyWeight = psoConfigEntity.getO2AvgPenaltyWeight();
		
		this.loadPenaltyWeight = psoConfigEntity.getLoadPenaltyWeight();
		this.loadSetPointPenaltyWeight = psoConfigEntity.getLoadSetPointPenaltyWeight();
		
		this.noxBoundary = psoConfigEntity.getNoxBoundary();
		this.noxPenaltyWeight = psoConfigEntity.getNoxPenaltyWeight();
		
		this.stackCoBoundary = psoConfigEntity.getStackCoBoundary();
		this.stackCoPenaltyWeight = psoConfigEntity.getStackCoPenaltyWeight();
		
		this.fgTempBoundary = psoConfigEntity.getFgTempBoundary();
		this.fgTempPenaltyWeight = psoConfigEntity.getFgTempPenaltyWeight();
	
		this.econOutFlueGasO2RightIndexs = this.outputValuesIndex(new String[] {
				CommonConst.ECON_OUT_FLUE_GAS_O2_1_RIGHT, // 18
				CommonConst.ECON_OUT_FLUE_GAS_O2_2_RIGHT  // 19
		});
		
		this.econOutFlueGasO2LeftIndexs = this.outputValuesIndex(new String[] {
				CommonConst.ECON_OUT_FLUE_GAS_OXYGE_1_LEFT, // 27
				CommonConst.ECON_OUT_FLUE_GAS_OXYGE_2_LEFT  // 28
		});		
		
		this.rhMDshSprayFlowLeftIndex = this.outputValuesIndex(CommonConst.RH_MICROFLOW_DSH_SPRAY_FLOW_L); // 37
		this.rhMDshSprayFlowRightIndex = this.outputValuesIndex(CommonConst.RH_MICROFLOW_DSH_SPRAY_FLOW_R); // 39
		this.rhEDshSprayFlowLeftIndex = this.outputValuesIndex(CommonConst.RH_E_DSH_SPRAY_FLOW_L_3); // 31
		this.rhEDshSprayFlowRightIndex = this.outputValuesIndex(CommonConst.RH_E_DSH_SPRAY_FLOW_R_3); // 34
		this.rhSprayCumulativeFlowIndex = this.outputValuesIndex(CommonConst.RH_SPRAY_WTR_FLOW); // 49
		
		this.horizonGasLeftTempIndex = this.outputValuesIndex(CommonConst.HRZN_FG_temp_L); // 03
		this.horizonGasRightTempIndex = this.outputValuesIndex(CommonConst.HRZN_FG_temp_R); // 05
		this.noxCapabilityIndex = this.outputValuesIndex(CommonConst.NOx); // 15				
		this.coCapabilityIndex = this.outputValuesIndex(CommonConst.Stack_CO); // 16
		
		this.econOutFlueGasO2Right1Index = this.outputValuesIndex(CommonConst.ECON_OUT_FLUE_GAS_O2_1_RIGHT); // 18
		this.econOutFlueGasO2Right2Index = this.outputValuesIndex(CommonConst.ECON_OUT_FLUE_GAS_O2_2_RIGHT); // 19
		this.econOutFlueGasO2Left1Index = this.outputValuesIndex(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_1_LEFT); // 27
		this.econOutFlueGasO2Left2Index = this.outputValuesIndex(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_2_LEFT); // 28
		this.activePowerOfGeneratorIndex = this.outputValuesIndex(CommonConst.ACTIVE_POWER_OF_GENERATOR); // 50
		
		this.opOutputLoadMW = opOutputLoadMW;
		this.opInputLoadSetPointMW = opInputLoadSetPointMW;
	}
	
	private int outputValuesIndex(String tagId) {
		int[] indexs = this.outputValuesIndex(new String[] { tagId });
		return indexs.length > 0 ? indexs[0] : -1;
	}
	
	private int[] outputValuesIndex(String[] tagIdList) {
		int[] indexs = new int[tagIdList.length];

		for (int i = 0; i < tagIdList.length; i++) {
			String tagId = tagIdList[i];
			for (PsoTag outputTag : this.outputTagList) {
				if (outputTag.getId().equals(tagId)) {
					indexs[i] = outputTag.getIndex();
					break;
				}
			}
		}

		return indexs;
	}
	
	private double outputValuesSum(int[] indexs, double[] outputData) {
		double sum = 0.0;
		for (int index : indexs) {
			sum += outputData[index];
		}
		
		return sum;
	}
	
	private double outputValuesAvg(int[] indexs, double[] outputData) {
		double sum = this.outputValuesSum(indexs, outputData);		
		double avg = sum / indexs.length;
		
		return avg;
	}
	
	public double[] getCalcOutputKeyTagValue(double[] outputData) {
		
		double[] resultKeyTagValues = new double[8];
		
		double rhMDshSprayFlowLeftAvg = outputData[this.rhMDshSprayFlowLeftIndex];
		double rhEDshSprayFlowLeftAvg = outputData[this.rhEDshSprayFlowLeftIndex];
		double rhSprayFlowLeftSum = rhMDshSprayFlowLeftAvg + rhEDshSprayFlowLeftAvg;
		
		double rhMDshSprayFlowRightAvg = outputData[this.rhMDshSprayFlowRightIndex];
		double rhEDshSprayFlowRightAvg = outputData[this.rhEDshSprayFlowRightIndex];
		double rhSprayFlowRightSum = rhMDshSprayFlowRightAvg + rhEDshSprayFlowRightAvg;

		// EmissionValue.
		double noxCapability = outputData[this.noxCapabilityIndex];
		
		double o2LeftAvg = this.outputValuesAvg(econOutFlueGasO2LeftIndexs, outputData);
		double o2RightAvg = this.outputValuesAvg(econOutFlueGasO2RightIndexs, outputData);
		
		// EquipmentValue.
		double horizonGasLeftTemp = outputData[this.horizonGasLeftTempIndex];
		double horizonGasRightTemp = outputData[this.horizonGasRightTempIndex];

		// ProfitValue
		double coCapability = outputData[this.coCapabilityIndex];
		
		resultKeyTagValues[0] = o2LeftAvg;
		resultKeyTagValues[1] = o2RightAvg;
		resultKeyTagValues[2] = rhSprayFlowLeftSum;
		resultKeyTagValues[3] = rhSprayFlowRightSum;
		resultKeyTagValues[4] = horizonGasLeftTemp;
		resultKeyTagValues[5] = horizonGasRightTemp;
		resultKeyTagValues[6] = noxCapability;
		resultKeyTagValues[7] = coCapability;
		
		return resultKeyTagValues;		
	}
		
	public double calcCostFunction(double[] outputData) {
		
		// Zero plate tag check
		for (PsoTag outputTag : this.outputTagList) {
			if (outputTag.isZeroPlate()) {
				if (outputData[outputTag.getIndex()] < 0)
					outputData[outputTag.getIndex()] = 0;
			}
		}
		
		double calcValue = 0.0;
		
		double rhMDshSprayFlowLeftAvg = outputData[this.rhMDshSprayFlowLeftIndex];
		double rhMDshSprayFlowRightAvg = outputData[this.rhMDshSprayFlowRightIndex]; 
		double rhEDshSprayFlowLeftAvg = outputData[this.rhEDshSprayFlowLeftIndex];
		double rhEDshSprayFlowRightAvg = outputData[this.rhEDshSprayFlowRightIndex];
				
		double gasO2RightAvg = this.outputValuesAvg(econOutFlueGasO2RightIndexs, outputData);
		double gasO2LeftAvg = this.outputValuesAvg(econOutFlueGasO2LeftIndexs, outputData);		
				
		// Profit Value
		double rhSprayFlow = outputData[this.rhSprayCumulativeFlowIndex];
		double gasO2Avg = (gasO2RightAvg + gasO2LeftAvg) / 2;
		double coCapability = outputData[this.coCapabilityIndex];

		// EmissionValue.
		double noxCapability = outputData[this.noxCapabilityIndex];
			
		// EquipmentValue.
		double horizonGasLeftTemp = outputData[this.horizonGasLeftTempIndex];
		double horizonGasRightTemp = outputData[this.horizonGasRightTempIndex];
		
		double rhMDshSprayFlowDiffAbs = Math.abs(rhMDshSprayFlowLeftAvg - rhMDshSprayFlowRightAvg);
		double rhEDshSprayFlowDiffAbs = Math.abs(rhEDshSprayFlowLeftAvg - rhEDshSprayFlowRightAvg);
		double gasO2AvgDiffAbs = Math.abs(gasO2RightAvg - gasO2LeftAvg);
		
		// Apply formula.
		double profitValue = (this.rhSparyK * rhSprayFlow) + (this.o2AvgK * gasO2Avg) + (this.coK * coCapability);
		double emissionValue = this.noxK * noxCapability;
		double equipmentValue = this.fgtK * Math.abs(horizonGasLeftTemp - horizonGasRightTemp) + this.rhSparyDiffK * (rhMDshSprayFlowDiffAbs + rhEDshSprayFlowDiffAbs) + this.o2DiffK * gasO2AvgDiffAbs;
		calcValue = (this.profitWeight * profitValue) + (this.emissionWeight * emissionValue) + (this.equipmentWeight * equipmentValue);
		
		// O2 Boundary - Penalty
		double econOutFlueGasO2Right1 = outputData[this.econOutFlueGasO2Right1Index];
		double econOutFlueGasO2Right2 = outputData[this.econOutFlueGasO2Right2Index];
		double econOutFlueGasO2Left1 = outputData[this.econOutFlueGasO2Left1Index];
		double econOutFlueGasO2Left2 = outputData[this.econOutFlueGasO2Left2Index];
		
		// Eco O2 4개(=N_ATT{323,324,325,326})의 평균인 O2AVG가 최소치(2.8 %) 보다 내려가면 Ramp로 패널티
		double econOutFlueGasO2Avg = (econOutFlueGasO2Right1 + econOutFlueGasO2Right2 + econOutFlueGasO2Left1 + econOutFlueGasO2Left2) / 4;
		if(econOutFlueGasO2Avg < this.o2AvgBoundary)
			calcValue += (this.o2AvgBoundary - econOutFlueGasO2Avg) * this.o2AvgPenalyWeight;
		
		// Eco O2 4개(=N_ATT{323,324,325,326})중 최소값인 O2min가 최소치(2.5) 보다 내려가면 Ramp로 패널티
		double econOutFlueGasO2Min = econOutFlueGasO2Right1 < econOutFlueGasO2Right2 ? econOutFlueGasO2Right1 : econOutFlueGasO2Right2;
		econOutFlueGasO2Min = econOutFlueGasO2Min < econOutFlueGasO2Left1 ? econOutFlueGasO2Min : econOutFlueGasO2Left1;
		econOutFlueGasO2Min = econOutFlueGasO2Min < econOutFlueGasO2Left2 ? econOutFlueGasO2Min : econOutFlueGasO2Left2;
		if (econOutFlueGasO2Min < this.o2MinBoundary)
			calcValue += (this.o2MinBoundary - econOutFlueGasO2Min) * this.o2MinPenalyWeight;
		
		// Load Boundary - Penalty
		double activePowerOfGenerator = outputData[this.activePowerOfGeneratorIndex];

		// Load(MW)값이 Load OP Load 보다 내려가면 Ramp로 패널티
		if (activePowerOfGenerator < this.opOutputLoadMW ) {
			calcValue += (this.opOutputLoadMW - activePowerOfGenerator) * this.loadPenaltyWeight;
		}

		// Load(MW)(=N_ATT51)의 값이 Load setpoint(MW)(=N_ATT1122; input) 보다 내려가면 Ramp로 패널티
		if (activePowerOfGenerator < this.opInputLoadSetPointMW ) {
			calcValue += (this.opInputLoadSetPointMW - activePowerOfGenerator) * this.loadSetPointPenaltyWeight;
		}
		
		// Nox Boundary - Penalty;
		// NOx(mg)(=N_ATT241)의 값이 NOx 최소치보다 내려가면 Ramp로 패널티
		if (noxCapability < this.noxBoundary) {
			calcValue += (this.noxBoundary - noxCapability) * this.noxPenaltyWeight;
		}
		
		// Stack Co Boundary - Penalty;
		// Stack CO(mg)(=N_ATT261)의 값이 CO 최대치 보다 올라가면 Ramp로 패널티;
		double coCapabilityIndex = outputData[this.coCapabilityIndex];
		if (coCapabilityIndex > this.stackCoBoundary) {
			calcValue += (coCapabilityIndex - this.stackCoBoundary) * this.stackCoPenaltyWeight;
		}
		
		// FG Temp Boundary - Penalty;
		// FG Temp. 2개(=N_ATT{858,871})의 최대값인 FGTmax가 최대치을 넘어가면 Ramp로 패널티
		double fgTempCapability = horizonGasLeftTemp > horizonGasRightTemp ? horizonGasLeftTemp : horizonGasRightTemp;
		if (fgTempCapability > this.fgTempBoundary) {
			calcValue += (fgTempCapability - this.fgTempBoundary) * this.fgTempPenaltyWeight;
		}

		return calcValue;
	}
}
