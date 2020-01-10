package dhi.optimizer.algorithm.common;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.optimizer.common.CommonConst;
import dhi.optimizer.enumeration.RHSprayLossType;
import dhi.optimizer.model.BoilerEfficiencyFactor;
import dhi.optimizer.model.ConfDataVO;
import dhi.optimizer.model.PlantEfficiencyFactor;
import dhi.optimizer.model.TagDataVO;
import dhi.optimizer.service.CommonDataService;

@Service
public class PerformanceCalculation {
		
	private static final double UNIT_TRANSFORM = 0.980665;
	private static final double ENTALPHY_TEMP_ADD_VAL = 273.15;
	private static final double ENTALPHY_PRESSURE_ADD_VAL = 1.013;
	private static final double HEAT_FLUX_THRU_FURNACE_HOPPER_OPENING = 31.5;
	private static final double MG_NM3_2_PPM = 1.25;
	private static final double PPM_2_VOLUME_PERCENT = 10000;

	@Autowired
	CommonDataService cds;
	
	/*private double dryGasLoss;
	private double otherLoss;
	private double pipingLoss = 14.0;*/
	
	private EnthalpyCalculation ec;
	private PlantEfficiencyFactor pef;
	private BoilerEfficiencyCalculation bec;
	private BoilerEfficiencyFactor bef;
	
	public PerformanceCalculation() {
		this.ec = new EnthalpyCalculation();
		this.pef = new PlantEfficiencyFactor();
		this.bef = new BoilerEfficiencyFactor();
	}
	
	public void init(HashMap<String, TagDataVO> opDataMap, HashMap<String, HashMap<String, ConfDataVO>> refConfTypesMap) {
		
		
		// 1) Boiler Efficiency Factor Setting
		//HashMap<String, Double> opDataMap = cds.getOpDataMap();
		
	   	double fuelConsumptionRate = 0;
	   	
	   	if(opDataMap.get(CommonConst.COAL_FEEDER_A_FEEDRATE) != null) {
	   		fuelConsumptionRate += opDataMap.get(CommonConst.COAL_FEEDER_A_FEEDRATE).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.COAL_FEEDER_B_FEEDRATE) != null) {
	   		fuelConsumptionRate += opDataMap.get(CommonConst.COAL_FEEDER_B_FEEDRATE).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.COAL_FEEDER_C_FEEDRATE) != null) {
	   		fuelConsumptionRate += opDataMap.get(CommonConst.COAL_FEEDER_C_FEEDRATE).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.COAL_FEEDER_D_FEEDRATE) != null) {
	   		fuelConsumptionRate += opDataMap.get(CommonConst.COAL_FEEDER_D_FEEDRATE).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.COAL_FEEDER_E_FEEDRATE) != null) {
	   		fuelConsumptionRate += opDataMap.get(CommonConst.COAL_FEEDER_E_FEEDRATE).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.COAL_FEEDER_F_FEEDRATE) != null) {
	   		fuelConsumptionRate += opDataMap.get(CommonConst.COAL_FEEDER_F_FEEDRATE).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.COAL_FEEDER_G_FEEDRATE) != null) {
	   		fuelConsumptionRate += opDataMap.get(CommonConst.COAL_FEEDER_G_FEEDRATE).getTagVal();
	   	}
		
		//bef.setFuelConsumptionRate(opDataMap.get(CommonConst.TOTAL_COAL_FLOW) * 1000);
	   	bef.setFuelConsumptionRate(fuelConsumptionRate * 1000);
		
	   	
	   	double fanInletAir = 0;
		int fanInletAirCount = 0;
		
		if(opDataMap.get(CommonConst.FD_A_INLET_AIR_TEMP) != null) {
			fanInletAir += opDataMap.get(CommonConst.FD_A_INLET_AIR_TEMP).getTagVal();
			fanInletAirCount++;
		}
		
		if(opDataMap.get(CommonConst.FD_B_INLET_AIR_TEMP) != null) {
			fanInletAir += opDataMap.get(CommonConst.FD_B_INLET_AIR_TEMP).getTagVal();
			fanInletAirCount++;
		}
		
		if(opDataMap.get(CommonConst.PA_FAN_A_INLET_AIR_TEMP) != null) {
			fanInletAir += opDataMap.get(CommonConst.PA_FAN_A_INLET_AIR_TEMP).getTagVal();
			fanInletAirCount++;
		}
		
		if(opDataMap.get(CommonConst.PA_FAN_A_INLET_AIR_TEMP) != null) {
			fanInletAir += opDataMap.get(CommonConst.PA_FAN_A_INLET_AIR_TEMP).getTagVal();
			fanInletAirCount++;
		}
		
		if(fanInletAirCount == 0) {
			bef.setFanInletAir(fanInletAir);
		} else {
			bef.setFanInletAir(fanInletAir / fanInletAirCount);
		}
		
		
		double SATSum = 0;
		int SATCount = 0;
		
		if(opDataMap.get(CommonConst.AIR_PREHEATER_A_INLET_SA_TEMP1) != null) {
			SATSum += opDataMap.get(CommonConst.AIR_PREHEATER_A_INLET_SA_TEMP1).getTagVal();
			SATCount++;
		}
		
		if(opDataMap.get(CommonConst.AIR_PREHEATER_A_INLET_SA_TEMP2) != null) {
			SATSum += opDataMap.get(CommonConst.AIR_PREHEATER_A_INLET_SA_TEMP2).getTagVal();
			SATCount++;
		}
		
		if(opDataMap.get(CommonConst.AIR_PREHEATER_B_INLET_SA_TEMP1) != null) {
			SATSum += opDataMap.get(CommonConst.AIR_PREHEATER_B_INLET_SA_TEMP1).getTagVal();
			SATCount++;
		}
		
		if(opDataMap.get(CommonConst.AIR_PREHEATER_B_INLET_SA_TEMP2) != null) {
			SATSum += opDataMap.get(CommonConst.AIR_PREHEATER_B_INLET_SA_TEMP2).getTagVal();
			SATCount++;
		}
		
		double SAT = SATCount == 0 ? SATSum : (SATSum / SATCount);
		
		
		double PATSum = 0;
		double PATCount = 0;
		
		if(opDataMap.get(CommonConst.AIR_PREHEATER_A_INLET_PA_TEMP1) != null) {
			PATSum += opDataMap.get(CommonConst.AIR_PREHEATER_A_INLET_PA_TEMP1).getTagVal();
			PATCount++;
		}
		
		if(opDataMap.get(CommonConst.AIR_PREHEATER_A_INLET_PA_TEMP2) != null) {
			PATSum += opDataMap.get(CommonConst.AIR_PREHEATER_A_INLET_PA_TEMP2).getTagVal();
			PATCount++;
		}
		
		if(opDataMap.get(CommonConst.AIR_PREHEATER_B_INLET_PA_TEMP1) != null) {
			PATSum += opDataMap.get(CommonConst.AIR_PREHEATER_B_INLET_PA_TEMP1).getTagVal();
			PATCount++;
		}
		
		if(opDataMap.get(CommonConst.AIR_PREHEATER_B_INLET_PA_TEMP2) != null) {
			PATSum += opDataMap.get(CommonConst.AIR_PREHEATER_B_INLET_PA_TEMP2).getTagVal();
			PATCount++;
		}
		
		
		double PAT = PATCount == 0 ? PATSum : (PATSum / PATCount);
		
		
		double TAF = 0;
		if(opDataMap.get(CommonConst.TOTAL_AIR_FLOW) != null) {
			TAF = opDataMap.get(CommonConst.TOTAL_AIR_FLOW).getTagVal();
		}
		
		double PAF = 0;
		
		if(opDataMap.get(CommonConst.MILL_A_INL_PRI_AIR_FLOW) != null) {
			PAF += opDataMap.get(CommonConst.MILL_A_INL_PRI_AIR_FLOW).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.MILL_B_INL_PRI_AIR_FLOW) != null) {
	   		PAF += opDataMap.get(CommonConst.MILL_B_INL_PRI_AIR_FLOW).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.MILL_C_INL_PRI_AIR_FLOW) != null) {
	   		PAF += opDataMap.get(CommonConst.MILL_C_INL_PRI_AIR_FLOW).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.MILL_D_INL_PRI_AIR_FLOW) != null) {
	   		PAF += opDataMap.get(CommonConst.MILL_D_INL_PRI_AIR_FLOW).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.MILL_E_INL_PRI_AIR_FLOW) != null) {
	   		PAF += opDataMap.get(CommonConst.MILL_E_INL_PRI_AIR_FLOW).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.MILL_F_INL_PRI_AIR_FLOW) != null) {
	   		PAF += opDataMap.get(CommonConst.MILL_F_INL_PRI_AIR_FLOW).getTagVal();
	   	}
	   	if(opDataMap.get(CommonConst.MILL_G_INL_PRI_AIR_FLOW) != null) {
	   		PAF += opDataMap.get(CommonConst.MILL_G_INL_PRI_AIR_FLOW).getTagVal();
	   	}
		
		if(TAF == 0) {
			bef.setAHInletAir(SAT * (TAF - PAF) + PAT * PAF);
		} else {
			bef.setAHInletAir(SAT * (TAF - PAF) / TAF + PAT * PAF / TAF);
		}
		
		
		double o2AtEcoOutlet = 0;
		int o2AtEcoOutletCount = 0;

		if(opDataMap.get(CommonConst.ECON_OUT_FLUE_GAS_O2_1_RIGHT) != null) {
			o2AtEcoOutlet += opDataMap.get(CommonConst.ECON_OUT_FLUE_GAS_O2_1_RIGHT).getTagVal();
			o2AtEcoOutletCount++;
	   	}
		if(opDataMap.get(CommonConst.ECON_OUT_FLUE_GAS_O2_2_RIGHT) != null) {
			o2AtEcoOutlet += opDataMap.get(CommonConst.ECON_OUT_FLUE_GAS_O2_2_RIGHT).getTagVal();
			o2AtEcoOutletCount++;
	   	}
		if(opDataMap.get(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_1_LEFT) != null) {
			o2AtEcoOutlet += opDataMap.get(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_1_LEFT).getTagVal();
			o2AtEcoOutletCount++;
	   	}
		if(opDataMap.get(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_2_LEFT) != null) {
			o2AtEcoOutlet += opDataMap.get(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_2_LEFT).getTagVal();
			o2AtEcoOutletCount++;
	   	}
		
		if(o2AtEcoOutletCount == 0) {
			bef.setO2AtEconomizerOutlet(o2AtEcoOutlet);
		} else {
			bef.setO2AtEconomizerOutlet(o2AtEcoOutlet / o2AtEcoOutletCount);
		}
		

		double o2AtAHOutlet = 0;
		int o2AtAHOutletCount = 0;
		if(opDataMap.get(CommonConst.APH_A_OUTLET_FLUE_GAS_OXYGEN) != null) {
			o2AtAHOutlet += opDataMap.get(CommonConst.APH_A_OUTLET_FLUE_GAS_OXYGEN).getTagVal();
			o2AtAHOutletCount++;
	   	}
		if(opDataMap.get(CommonConst.APH_B_OUTLET_FLUE_GAS_OXYGEN) != null) {
			o2AtAHOutlet += opDataMap.get(CommonConst.APH_B_OUTLET_FLUE_GAS_OXYGEN).getTagVal();
			o2AtAHOutletCount++;
	   	}
		if(opDataMap.get(CommonConst.Stack_O2) != null) {
			o2AtAHOutlet += opDataMap.get(CommonConst.Stack_O2).getTagVal();
			o2AtAHOutletCount++;
	   	}
		
		if(o2AtAHOutletCount == 0) {
			bef.setO2AtAHOutlet(o2AtAHOutlet);
		} else {
			bef.setO2AtAHOutlet(o2AtAHOutlet / o2AtAHOutletCount);
		}
		
		double coAtAHOutlet = 0;
		double coAtAHOutletSum = 0;
		int coAtAHOutletCount = 0;
		if(opDataMap.get(CommonConst.ECO_OUT_FLUE_GAS_CO_L) != null) {
			coAtAHOutletSum += opDataMap.get(CommonConst.ECO_OUT_FLUE_GAS_CO_L).getTagVal();
			coAtAHOutletCount ++;
		}
		if(opDataMap.get(CommonConst.ECO_OUT_FLUE_GAS_CO_R) != null) {
			coAtAHOutletSum += opDataMap.get(CommonConst.ECO_OUT_FLUE_GAS_CO_R).getTagVal();
			coAtAHOutletCount ++;
		}
		
		if(coAtAHOutletCount == 0) {
			coAtAHOutlet = coAtAHOutletSum;
		} else {
			coAtAHOutlet = coAtAHOutletSum / coAtAHOutletCount;
		}
		
		bef.setCOAtAHOutlet((coAtAHOutlet / MG_NM3_2_PPM) / PPM_2_VOLUME_PERCENT);
		
		
		
		double ecoOutlet = 0;
		int ecoOutletCount = 0;
		if(opDataMap.get(CommonConst.APH_A_INLET_FLUE_GAS_TEMP_1) != null) {
			ecoOutlet += opDataMap.get(CommonConst.APH_A_INLET_FLUE_GAS_TEMP_1).getTagVal();
			ecoOutletCount ++;
		}
		if(opDataMap.get(CommonConst.APH_A_INLET_FLUE_GAS_TEMP_2) != null) {
			ecoOutlet += opDataMap.get(CommonConst.APH_A_INLET_FLUE_GAS_TEMP_2).getTagVal();
			ecoOutletCount ++;
		}
		if(opDataMap.get(CommonConst.APH_B_INLET_FLUE_GAS_TEMP_1) != null) {
			ecoOutlet += opDataMap.get(CommonConst.APH_B_INLET_FLUE_GAS_TEMP_1).getTagVal();
			ecoOutletCount ++;
		}
		if(opDataMap.get(CommonConst.APH_B_INLET_FLUE_GAS_TEMP_2) != null) {
			ecoOutlet += opDataMap.get(CommonConst.APH_B_INLET_FLUE_GAS_TEMP_2).getTagVal();
			ecoOutletCount ++;
		}
		
		
		if(ecoOutletCount == 0) {
			bef.setEconomizerOutlet(ecoOutlet);
		} else {
			bef.setEconomizerOutlet(ecoOutlet / ecoOutletCount);
		}
		
		
		
		double ahOutletGas = 0;
		int ahOutletGasCount = 0;
		if(opDataMap.get(CommonConst.APH_A_OUTLET_FLUE_GAS_TEMP_1) != null) {
			ahOutletGas += opDataMap.get(CommonConst.APH_A_OUTLET_FLUE_GAS_TEMP_1).getTagVal();
			ahOutletGasCount ++;
		}
		if(opDataMap.get(CommonConst.APH_A_OUTLET_FLUE_GAS_TEMP_2) != null) {
			ahOutletGas += opDataMap.get(CommonConst.APH_A_OUTLET_FLUE_GAS_TEMP_2).getTagVal();
			ahOutletGasCount ++;
		}
		if(opDataMap.get(CommonConst.APH_B_OUTLET_FLUE_GAS_TEMP_1) != null) {
			ahOutletGas += opDataMap.get(CommonConst.APH_B_OUTLET_FLUE_GAS_TEMP_1).getTagVal();
			ahOutletGasCount ++;
		}
		if(opDataMap.get(CommonConst.APH_B_OUTLET_FLUE_GAS_TEMP_2) != null) {
			ahOutletGas += opDataMap.get(CommonConst.APH_B_OUTLET_FLUE_GAS_TEMP_2).getTagVal();
			ahOutletGasCount ++;
		}
		
		if(ahOutletGasCount == 0) {
			bef.setAHOutletGas(ahOutletGas);
		} else {
			bef.setAHOutletGas(ahOutletGas / ahOutletGasCount);
		}
		
		
		// 2) Coal Setting
		HashMap<String, ConfDataVO> coalInfoMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_COAL);
		
		bef.setMoisture(coalInfoMap.get(CommonConst.TOTAL_MOISTURE) != null ? coalInfoMap.get(CommonConst.TOTAL_MOISTURE).getConfVal() : 0); // Total Moisture
		bef.setAshAsFireBasis(coalInfoMap.get(CommonConst.ASH_CONTENTS) != null ? coalInfoMap.get(CommonConst.ASH_CONTENTS).getConfVal() : 0); // Ash Contents
		bef.setAsh(bef.getAshAsFireBasis());
		bef.setVolatileMatterAsFireBasis(coalInfoMap.get(CommonConst.VOLATILE_MATTER) != null ? coalInfoMap.get(CommonConst.VOLATILE_MATTER).getConfVal() : 0); // Volatile Matter
		bef.setFixedCarbonAsFireBasis(coalInfoMap.get(CommonConst.FIXED_CARBON) != null ? coalInfoMap.get(CommonConst.FIXED_CARBON).getConfVal() : 0); // Fixed Carbone
		bef.setHigherHeatingValue(coalInfoMap.get(CommonConst.GROSS_CALORIFIC_VALUE) != null ? coalInfoMap.get(CommonConst.GROSS_CALORIFIC_VALUE).getConfVal() * 4.1861 : 0); // HHVF = Gross Calorific Value * 4.1861
		bef.setNitrogen(coalInfoMap.get(CommonConst.N) != null ? coalInfoMap.get(CommonConst.N).getConfVal() : 0); // N
		bef.setCarbon(coalInfoMap.get(CommonConst.C) != null ? coalInfoMap.get(CommonConst.C).getConfVal(): 0); // C
		bef.setHydrogen(coalInfoMap.get(CommonConst.H) != null ? coalInfoMap.get(CommonConst.H).getConfVal() : 0); // H
		bef.setSulfur(coalInfoMap.get(CommonConst.S) != null ? coalInfoMap.get(CommonConst.S).getConfVal() : 0); // S
		bef.setOxygen(coalInfoMap.get(CommonConst.O) != null ? coalInfoMap.get(CommonConst.O).getConfVal() : 0); // O
		

		// 3) Input Parameter Setting		
		HashMap<String, ConfDataVO> efficiencyInfoMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_EFFICIENCY);
		
		bef.setBottomAshTotalResidue(efficiencyInfoMap.get(CommonConst.TOTAL_RESIDUE_DISTRIBUTION_RATE_BOTTOM_ASH) != null ? efficiencyInfoMap.get(CommonConst.TOTAL_RESIDUE_DISTRIBUTION_RATE_BOTTOM_ASH).getConfVal() : 0);
		bef.setEconomizerHopperAshTotalResidue(efficiencyInfoMap.get(CommonConst.TOTAL_RESIDUE_DISTRIBUTION_RATE_ECONOMIZER_HOPPER_ASH) != null ? efficiencyInfoMap.get(CommonConst.TOTAL_RESIDUE_DISTRIBUTION_RATE_ECONOMIZER_HOPPER_ASH).getConfVal() : 0);
		bef.setBottomAshUBCResidue(efficiencyInfoMap.get(CommonConst.UNBURNED_CARBON_IN_RESIDUE_BOTTOM_ASH) != null ? efficiencyInfoMap.get(CommonConst.UNBURNED_CARBON_IN_RESIDUE_BOTTOM_ASH).getConfVal() : 0);
		bef.setEconimizerHopperAshUBCResidue(efficiencyInfoMap.get(CommonConst.UNBURNED_CARBON_IN_RESIDUE_ECONOMIZER_HOPPER_ASH) != null ? efficiencyInfoMap.get(CommonConst.UNBURNED_CARBON_IN_RESIDUE_ECONOMIZER_HOPPER_ASH).getConfVal() : 0);
		bef.setFlyAshUBCResidue(efficiencyInfoMap.get(CommonConst.FLY_ASH) != null ? efficiencyInfoMap.get(CommonConst.FLY_ASH).getConfVal() : 0);
		bef.setRelativeHumidity(efficiencyInfoMap.get(CommonConst.RELATIVE_HUMIDITY) != null ? efficiencyInfoMap.get(CommonConst.RELATIVE_HUMIDITY).getConfVal() : 0);
		bef.setAtmosphericPressure(efficiencyInfoMap.get(CommonConst.ATMOSPHERIC_PRESSURE) != null ? efficiencyInfoMap.get(CommonConst.ATMOSPHERIC_PRESSURE).getConfVal() : 0);
		
		double wetAshPitLoss = HEAT_FLUX_THRU_FURNACE_HOPPER_OPENING * (efficiencyInfoMap.get(CommonConst.PROJECTED_AREA_OF_HOPPER_OPENING) != null ? efficiencyInfoMap.get(CommonConst.PROJECTED_AREA_OF_HOPPER_OPENING).getConfVal() : 0) /(bef.getHigherHeatingValue() * bef.getFuelConsumptionRate() / 3600)*100; 
		bef.setWetAshPitLoss(wetAshPitLoss);
		
		//bef.setWetAshPitLoss(efficiencyInfoMap.get(CommonConst.WET_ASH_PIT_LOSS).getTagVal());
		bef.setProjectedAreaOfHopperOpening(efficiencyInfoMap.get(CommonConst.PROJECTED_AREA_OF_HOPPER_OPENING) != null ? efficiencyInfoMap.get(CommonConst.PROJECTED_AREA_OF_HOPPER_OPENING).getConfVal() : 0);
		bef.setSurfaceRadiationAndConvection(efficiencyInfoMap.get(CommonConst.SURFACE_RADIATION_CONVECTION) != null ? efficiencyInfoMap.get(CommonConst.SURFACE_RADIATION_CONVECTION).getConfVal() : 0);
		bef.setUnmeasuredLosses(efficiencyInfoMap.get(CommonConst.UNMEASURED_LOSSES) != null ? efficiencyInfoMap.get(CommonConst.UNMEASURED_LOSSES).getConfVal() : 0);
		bef.setAuxiliaryDrivesPowerConsumption(efficiencyInfoMap.get(CommonConst.AUXILIARY_DRIVES_POWER_CONSUMPTION) != null ? efficiencyInfoMap.get(CommonConst.AUXILIARY_DRIVES_POWER_CONSUMPTION).getConfVal() : 0);
		bef.setOverallDriveEfficiency(efficiencyInfoMap.get(CommonConst.OVERALL_DRIVE_EFFICIENCY) != null ? efficiencyInfoMap.get(CommonConst.OVERALL_DRIVE_EFFICIENCY).getConfVal() : 0);
		
		this.bec = new BoilerEfficiencyCalculation(bef);
		
		double mainSteamTemp = 0;
		int mainSteamTempCount = 0;
		if(opDataMap.get(CommonConst.SH_FINISH_OL_TEMP_L) != null) {
			mainSteamTemp += opDataMap.get(CommonConst.SH_FINISH_OL_TEMP_L).getTagVal();
			mainSteamTempCount ++;
		}
		if(opDataMap.get(CommonConst.SH_FINISH_OL_TEMP_R) != null) {
			mainSteamTemp += opDataMap.get(CommonConst.SH_FINISH_OL_TEMP_R).getTagVal();
			mainSteamTempCount ++;
		}
		
		if(mainSteamTempCount == 0) {
			pef.setMainSteamTemp(mainSteamTemp);
		} else {
			pef.setMainSteamTemp(mainSteamTemp / mainSteamTempCount);
		}
		
		// pef.setMainSteamTemp(opDataMap.get(CommonConst.MAIN_STEAM_TEMP) != null ? opDataMap.get(CommonConst.MAIN_STEAM_TEMP).getTagVal() : 0);
		pef.setMainSteamPressure((opDataMap.get(CommonConst.MAIN_STEAM_PRESS_1) != null ? opDataMap.get(CommonConst.MAIN_STEAM_PRESS_1).getTagVal() : 0) * UNIT_TRANSFORM); // Unit Transformation
		
		double feedwaterTemp = 0;
		int feedwaterTempCount = 0;
		if(opDataMap.get(CommonConst.FEED_WATER_TEMP_1) != null) {
			feedwaterTemp += opDataMap.get(CommonConst.FEED_WATER_TEMP_1).getTagVal();
			feedwaterTempCount ++;
		} 
		if(opDataMap.get(CommonConst.FEED_WATER_TEMP_2) != null) {
			feedwaterTemp += opDataMap.get(CommonConst.FEED_WATER_TEMP_2).getTagVal();
			feedwaterTempCount ++;
		}
		
		if(feedwaterTempCount == 0) {
			pef.setFwTemp(feedwaterTemp);
		} else {
			pef.setFwTemp(feedwaterTemp / feedwaterTempCount);
		}
		
		double feedwaterPress = 0;
		int feedwaterPressCount = 0;
		if(opDataMap.get(CommonConst.MAIN_FEEDWATER_PRESS) != null) {
			feedwaterPress += opDataMap.get(CommonConst.MAIN_FEEDWATER_PRESS).getTagVal();
			feedwaterPressCount ++;
		} 
		if(opDataMap.get(CommonConst.BOILER_END_FEEDWATER_PRESS) != null) {
			feedwaterPress += opDataMap.get(CommonConst.BOILER_END_FEEDWATER_PRESS).getTagVal();
			feedwaterPressCount ++;
		}
		
		if(feedwaterPressCount == 0) {
			pef.setFwPressure(feedwaterPress);
		} else {
			pef.setFwPressure(feedwaterPress / feedwaterPressCount * UNIT_TRANSFORM);
		}
		
		pef.setMainSteamFlow(opDataMap.get(CommonConst.TOTAL_FEEDWATER_FLOW) != null ? opDataMap.get(CommonConst.TOTAL_FEEDWATER_FLOW).getTagVal() : 0);
		
		double hotRHSteamTemp = 0;
		int hotRHSteamTempCount = 0;
		
		if(opDataMap.get(CommonConst.RH_FN_OL_STM_TEMP_LEFT) != null) {
			hotRHSteamTemp += opDataMap.get(CommonConst.RH_FN_OL_STM_TEMP_LEFT).getTagVal();
			hotRHSteamTempCount ++;
		} 
		if(opDataMap.get(CommonConst.RH_FN_OL_STM_TEMP_RIGHT) != null) {
			hotRHSteamTemp += opDataMap.get(CommonConst.RH_FN_OL_STM_TEMP_RIGHT).getTagVal();
			hotRHSteamTempCount ++;
		}		
		
		/*if(opDataMap.get(CommonConst.RH_FINISH_OL_STM_TEMP_LEFT_1) != null) {
			hotRHSteamTemp += opDataMap.get(CommonConst.RH_FINISH_OL_STM_TEMP_LEFT_1).getTagVal();
			hotRHSteamTempCount ++;
		} 
		if(opDataMap.get(CommonConst.RH_FINISH_OL_STM_TEMP_LEFT_2) != null) {
			hotRHSteamTemp += opDataMap.get(CommonConst.RH_FINISH_OL_STM_TEMP_LEFT_2).getTagVal();
			hotRHSteamTempCount ++;
		}
		if(opDataMap.get(CommonConst.RH_FINISH_OL_STM_TEMP_RIGHT_1) != null) {
			hotRHSteamTemp += opDataMap.get(CommonConst.RH_FINISH_OL_STM_TEMP_RIGHT_1).getTagVal();
			hotRHSteamTempCount ++;
		} 
		if(opDataMap.get(CommonConst.RH_FINISH_OL_STM_TEMP_RIGHT_2) != null) {
			hotRHSteamTemp += opDataMap.get(CommonConst.RH_FINISH_OL_STM_TEMP_RIGHT_2).getTagVal();
			hotRHSteamTempCount ++;
		}
		*/
		
		if(hotRHSteamTempCount == 0) {
			pef.setHotRHSteamTemp(hotRHSteamTemp);			
		} else {
			pef.setHotRHSteamTemp(hotRHSteamTemp / hotRHSteamTempCount);
		}
		
		
		
		double hotRHSteamPress = 0;
		int hotRHSteamPressCount = 0;
		
		if(opDataMap.get(CommonConst.FINISH_RH_OUTLET_STEAM_PRESS_LEFT_1) != null) {
			hotRHSteamPress += opDataMap.get(CommonConst.FINISH_RH_OUTLET_STEAM_PRESS_LEFT_1).getTagVal();
			hotRHSteamPressCount ++;
		} 
		if(opDataMap.get(CommonConst.FINISH_RH_OUTLET_STEAM_PRESS_LEFT_2) != null) {
			hotRHSteamPress += opDataMap.get(CommonConst.FINISH_RH_OUTLET_STEAM_PRESS_LEFT_2).getTagVal();
			hotRHSteamPressCount ++;
		}
		if(opDataMap.get(CommonConst.FINISH_RH_OUTLET_STEAM_PRESS_RIGHT_1) != null) {
			hotRHSteamPress += opDataMap.get(CommonConst.FINISH_RH_OUTLET_STEAM_PRESS_RIGHT_1).getTagVal();
			hotRHSteamPressCount ++;
		} 
		if(opDataMap.get(CommonConst.FINISH_RH_OUTLET_STEAM_PRESS_RIGHT_2) != null) {
			hotRHSteamPress += opDataMap.get(CommonConst.FINISH_RH_OUTLET_STEAM_PRESS_RIGHT_2).getTagVal();
			hotRHSteamPressCount ++;
		}
		
		if(hotRHSteamPressCount == 0) {
			pef.setHotRHSteamPressure(hotRHSteamPress);
		} else {
			pef.setHotRHSteamPressure(hotRHSteamPress / hotRHSteamPressCount  * UNIT_TRANSFORM);
		}
		
		
		double hotRHFlow = (pef.getHotRHSteamPressure() + 1.013) * (efficiencyInfoMap.get(CommonConst.IP_LP_TBN_FLOW_K_DESIGN) != null ? efficiencyInfoMap.get(CommonConst.IP_LP_TBN_FLOW_K_DESIGN).getConfVal() : 0) ;
		pef.setHotRHFlow(hotRHFlow);
		
		double coldRHSteamTemp = 0;
		int coldRHSteamTempCount = 0;
		
		if(opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_LEFT_1) != null) {
			coldRHSteamTemp += opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_LEFT_1).getTagVal();
			coldRHSteamTempCount ++;
		} 
		if(opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_RIGHT_1) != null) {
			coldRHSteamTemp += opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_RIGHT_1).getTagVal();
			coldRHSteamTempCount ++;
		}
		if(opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_LEFT_2) != null) {
			coldRHSteamTemp += opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_LEFT_2).getTagVal();
			coldRHSteamTempCount ++;
		} 
		if(opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_RIGHT_2) != null) {
			coldRHSteamTemp += opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_RIGHT_2).getTagVal();
			coldRHSteamTempCount ++;
		}
		
		if(coldRHSteamTempCount == 0) {
			pef.setColdRHSteamTemp(coldRHSteamTemp);
		} else {
			pef.setColdRHSteamTemp(coldRHSteamTemp / coldRHSteamTempCount);
		}
		
		double coldRHSteamPress = 0;
		int coldRHSteamPressCount = 0;
		if(opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_PRESS_LEFT) != null) {
			coldRHSteamPress += opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_PRESS_LEFT).getTagVal();
			coldRHSteamPressCount ++;
		} 
		if(opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_PRESS_RIGHT) != null) {
			coldRHSteamPress += opDataMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_PRESS_RIGHT).getTagVal();
			coldRHSteamPressCount ++;
		}
		
		if(coldRHSteamPressCount == 0) {
	    	pef.setColdRHSteamPressure(coldRHSteamPress);
		} else {
			pef.setColdRHSteamPressure(coldRHSteamPress / coldRHSteamPressCount * UNIT_TRANSFORM);
		}
		
		pef.setColdRHFlow(pef.getHotRHFlow() - (opDataMap.get(CommonConst.RH_SPRAY_WTR_FLOW) != null ? opDataMap.get(CommonConst.RH_SPRAY_WTR_FLOW).getTagVal() : 0));
		
		double rhSprayWTRTemp = 0;
		int rhSprayWTRTempCount = 0;
		if(opDataMap.get(CommonConst.RH_EMER_DSH_SPRAY_COMMON_PIPE_T) != null) {
			rhSprayWTRTemp += opDataMap.get(CommonConst.RH_EMER_DSH_SPRAY_COMMON_PIPE_T).getTagVal();
			rhSprayWTRTempCount ++;
		} 
		if(opDataMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_WTR_TEMP) != null) {
			rhSprayWTRTemp += opDataMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_WTR_TEMP).getTagVal();
			rhSprayWTRTempCount ++;
		}
		
		if(rhSprayWTRTempCount == 0) {
			pef.setRhSprayWTRTemp(rhSprayWTRTemp);
		} else {
			pef.setRhSprayWTRTemp(rhSprayWTRTemp / rhSprayWTRTempCount);
		}
		
		
		double rhSprayWTRPress = 0;
		int rhSprayWTRPressCount = 0;
		if(opDataMap.get(CommonConst.RH_EMER_DSH_SPRAY_COMMON_PIPE_P) != null) {
			rhSprayWTRPress += opDataMap.get(CommonConst.RH_EMER_DSH_SPRAY_COMMON_PIPE_P).getTagVal();
			rhSprayWTRPressCount ++;
		} 
		if(opDataMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_WTR_PRESS) != null) {
			rhSprayWTRPress += opDataMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_WTR_PRESS).getTagVal();
			rhSprayWTRPressCount ++;
		}
		
		if(rhSprayWTRPressCount == 0) {
			pef.setRhSprayWTRPressure(rhSprayWTRPress);
		} else {
			pef.setRhSprayWTRPressure(rhSprayWTRPress / rhSprayWTRPressCount * UNIT_TRANSFORM);
		}
		
		pef.setRhSprayFlow(opDataMap.get(CommonConst.RH_SPRAY_WTR_FLOW) != null ? opDataMap.get(CommonConst.RH_SPRAY_WTR_FLOW).getTagVal() : 0);
		
		pef.setRhSprayLossK(efficiencyInfoMap.get(CommonConst.RH_SPRAY_LOSS_K) != null ? efficiencyInfoMap.get(CommonConst.RH_SPRAY_LOSS_K).getConfVal() : 0);
		
		pef.setGeneratorOutput(opDataMap.get(CommonConst.ACTIVE_POWER_OF_GENERATOR) != null ? opDataMap.get(CommonConst.ACTIVE_POWER_OF_GENERATOR).getTagVal() : 0);
		
		pef.setMakeupFlow(10.8);
		
		pef.setPipingLoss(efficiencyInfoMap.get(CommonConst.PIPING_LOSS_AS_HEAT_RATE) != null ? efficiencyInfoMap.get(CommonConst.PIPING_LOSS_AS_HEAT_RATE).getConfVal() : 0);
		
		pef.set_100TmcrMsFlow(efficiencyInfoMap.get(CommonConst._100_TMCR_MS_FLOW) != null ? efficiencyInfoMap.get(CommonConst._100_TMCR_MS_FLOW).getConfVal() : 0);
		
		pef.setTotalAuxPower(opDataMap.get(CommonConst.TOTAL_AUX_POWER_VAL) != null ? opDataMap.get(CommonConst.TOTAL_AUX_POWER_VAL).getTagVal() : 0);
		
		pef.setAmmoniaSavingK(efficiencyInfoMap.get(CommonConst.AMMONIA_SAVING_K) != null ? efficiencyInfoMap.get(CommonConst.AMMONIA_SAVING_K).getConfVal() : 0);
		
		
		// 4) Baseline Information
		HashMap<String, ConfDataVO> baselineInfoMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_BASELINE);
				
		pef.setStackNOxAvg(baselineInfoMap.get(CommonConst.STACK_NOX_AVG).getConfVal());
		pef.setCurrentNOx(opDataMap.get(CommonConst.NOx).getTagVal());
		
		
		// 5) KPI Information
		HashMap<String, ConfDataVO> kpiInfoMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_KPI);
		pef.setAmmoniaPrice(kpiInfoMap.get(CommonConst.AMMONIA_PRICE) != null ? kpiInfoMap.get(CommonConst.AMMONIA_PRICE).getConfVal() : 0);
		
	}
	
	public double getPlantEfficiency() { 
		return 859.99 * 100.0 * getUnitHeatRate(); 
	}
	
	
	/*private double getMakeupFlowLoss() {
		return 9.0 * pef.getMakeupFlow() / 10.8;
	}
	
	private double getPipingLoss() {
		return pef.getPipingLoss();
	}*/
	
	public double getUnitHeatRate() {
		//return (getTurbineCycleHeatRate() * 100 /  getBoilerEfficiency()) + getMakeupFlowLoss() + getPipingLoss();
		return getTurbineCycleHeatRate() * 100 / (100 - 100 * getTotalLoss() / (100 + getTotalHeatCredit()));
	}
	
	/*
	 * {(Main Steam Enthalpy – FW Enthalpy) X FW Flow + Hot RH Steam Enthalpy X Hot RH Flow – Cold RH Steam Enthalpy X Cold RH Flow – RH Spray WTR Enthalpy X RH Spray Flow} / {Generator Output(kwh) X 4.1861}
	 */
	public double getTurbineCycleHeatRate() {
		return ((getMainSteamEnthalpy() - getFWEnthalpy()) * pef.getMainSteamFlow() + getHotRHSteamEnthalpy() * pef.getHotRHFlow() - getColdRHSteamEnthalpy() * pef.getColdRHFlow() - getRHSprayWTREnthalpy() * pef.getRhSprayFlow()) / (pef.getGeneratorOutput() * 4.1861);
	}
	
	private double getTurbineCycleEfficiency() {
		return 859.99 * 100.0 / getTurbineCycleHeatRate();
	}
	
	// RH Spray Loss with Unit type selection
	public double getRHSprayLoss(RHSprayLossType type) {
		double rhSprayLoss = pef.getRhSprayLossK() * pef.getRhSprayFlow() / pef.get_100TmcrMsFlow(); 
		
		if(type == RHSprayLossType.PERCENT) {
			return rhSprayLoss;
		} else {
			double turbineCycleEfficiency = getTurbineCycleEfficiency();
			return 859.9 * 100.0 * (1/(turbineCycleEfficiency - rhSprayLoss) - 1 /turbineCycleEfficiency);
		}
			 
	}
		
	// Boiler Efficiency 
	public double getBoilerEfficiency() {
		//return 100 - (getDryGasLoss() + getOtherLoss());
		return bec.ηfuel_Gross();
	}
	
	// Dry Gas Loss
	public double getDryGasLoss() {
		return bec.QpLDFg();
	}
	
	// Other Loss 
	public double getOtherLoss() {
		return 100 - bec.ηfuel_Gross() - bec.QpLDFg();
	}
	
	// Coal Supply
	public double getCoalSupply( ) {
		return bef.getFuelConsumptionRate() / 1000.0;
	}
	
	// Economizer Outlet
	public double getEconomizerOutlet() {
		return bef.getEconomizerOutlet();
	}
	
	// O2 at A/H Outlet
	public double getO2AtAHOutlet() {
		return bef.getO2AtAHOutlet();
	}
	
	// Total Aux Power (Temporary)
	public double getTotalAuxPower() {
		return pef.getTotalAuxPower();
	}
	
	// CO at A/H Outlet
	public double getCOAtAHOutlet() {
		return bef.getCOAtAHOutlet();
	}
	
	// Ammonia Cost Saving Benefit
	public double getAmmoniaSavingBenefit() {
		return (pef.getAmmoniaSavingK() * (pef.getStackNOxAvg() - pef.getCurrentNOx()) / 1000) * pef.getAmmoniaPrice();
	}
	
	private double getTotalLoss() {
		return bec.QpL();
	}
	
	private double getTotalHeatCredit() {
		return bec.QpB();
	}
	
	public double getO2AtEconomizerOutlet() {
		return bef.getO2AtEconomizerOutlet();
	}
	
	private double getMainSteamEnthalpy() {
		return ec.enthalpyW(pef.getMainSteamTemp() + ENTALPHY_TEMP_ADD_VAL, pef.getMainSteamPressure() + ENTALPHY_PRESSURE_ADD_VAL);
	}
	
	private double getFWEnthalpy() {
		return ec.enthalpyW(pef.getFwTemp() + ENTALPHY_TEMP_ADD_VAL, pef.getFwPressure() + ENTALPHY_PRESSURE_ADD_VAL);
	}
	
	private double getHotRHSteamEnthalpy() {
		return ec.enthalpyW(pef.getHotRHSteamTemp() + ENTALPHY_TEMP_ADD_VAL, pef.getHotRHSteamPressure() + ENTALPHY_PRESSURE_ADD_VAL);
	}
	
	private double getColdRHSteamEnthalpy() {
		return ec.enthalpyW(pef.getColdRHSteamTemp() + ENTALPHY_TEMP_ADD_VAL, pef.getColdRHSteamPressure() + ENTALPHY_PRESSURE_ADD_VAL);
	}
	
	private double getRHSprayWTREnthalpy() {
		return ec.enthalpyW(pef.getRhSprayWTRTemp() + ENTALPHY_TEMP_ADD_VAL, pef.getRhSprayWTRPressure() + ENTALPHY_PRESSURE_ADD_VAL);
	}
	

	@Override
	public String toString() {
		return "PerformanceCalculation [cds=" + cds + ", ec=" + ec + ", pef=" + pef + ", bec=" + bec + ", bef=" + bef
				+ ", getPlantEfficiency(197912999.50)=" + getPlantEfficiency() + ", getUnitHeatRate(2301.34)=" + getUnitHeatRate()
				+ ", getTurbineCycleHeatRate(2014.33)=" + getTurbineCycleHeatRate() + ", getTurbineCycleEfficiency(42.69)="
				+ getTurbineCycleEfficiency() + ", getBoilerEfficiency(87.53)=" + getBoilerEfficiency()
				+ ", getDryGasLoss(5.06)=" + getDryGasLoss() + ", getOtherLoss(7.41)=" + getOtherLoss() + ", getCoalSupply(360.20)="
				+ getCoalSupply() + ", getEconomizerOutlet(350.76)=" + getEconomizerOutlet() + ", getO2AtAHOutlet(3.84)="
				+ getO2AtAHOutlet() + ", getTotalAuxPower(13.00)=" + getTotalAuxPower() + ", getCOAtAHOutlet(0.01)="
				+ getCOAtAHOutlet() + ", getTotalLoss(12.51)=" + getTotalLoss() + ", getTotalHeatCredit(0.31)="
				+ getTotalHeatCredit() + ", getO2AtEconomizerOutlet(3.42)=" + getO2AtEconomizerOutlet()
				+ ", getMainSteamEnthalpy(3400.26)=" + getMainSteamEnthalpy() + ", getFWEnthalpy(1236.51)=" + getFWEnthalpy() + ", getRHSprayWTREnthalpy(789.34)=" + getRHSprayWTREnthalpy() 
				+ ", getHotRHSteamEnthalpy(3604.81)=" + getHotRHSteamEnthalpy() + ", getColdRHSteamEnthalpy(2967.84)=" + getColdRHSteamEnthalpy() 				 
				+ ", getMainSteamTemp(566.99)=" + pef.getMainSteamTemp() + ", getMainSteamPressure(242.46)=" + pef.getMainSteamPressure()  				
				+ ", getFwTemp(281.42)=" + pef.getFwTemp() + ", getFwPressure(297.24)=" + pef.getFwPressure() 
				+ ", getHotRHSteamTemp(571.09)=" + pef.getHotRHSteamTemp() + ", getHotRHSteamPressure(43.20)=" + pef.getHotRHSteamPressure()
				+ ", getColdRHSteamTemp(309.29)=" + pef.getColdRHSteamTemp() + ", getColdRHSteamPressure(45.03)=" + pef.getColdRHSteamPressure()
				+ ", getRhSprayWTRTemp(184.93)=" + pef.getRhSprayWTRTemp() + ", getRhSprayWTRPressure(97.80)=" + pef.getRhSprayWTRPressure()
				+ ", getMainStreamFlow(2088.64)=" + pef.getMainSteamFlow() + ", getHotRHFlow(1741.46)=" + pef.getHotRHFlow() + ", getColdRHFlow(1691.22)=" + pef.getColdRHFlow() 
				+ ", getRhSprayFlow(50.24)=" + pef.getRhSprayFlow() + ", getGeneratorOutput(680.49)=" + pef.getGeneratorOutput() + ", getRHSprayLoss(KCAL_KWH)(12.00)=" + getRHSprayLoss(RHSprayLossType.KCAL_KWH)
				+ ", getRHSprayLoss(PERCENT)(0.25)=" + getRHSprayLoss(RHSprayLossType.PERCENT) + ", getAmmoniaSavingBenefit(-84.63)=" + getAmmoniaSavingBenefit()
				+ "]";
	}
}
