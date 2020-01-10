package dhi.optimizer.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.StoredProcedureQuery;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dhi.common.exception.InvalidParameterException;
import dhi.common.util.Utilities;
import dhi.optimizer.algorithm.common.PerformanceCalculation;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.enumeration.ControllerModeStatus;
import dhi.optimizer.enumeration.OptimizerFunctionModeStatus;
import dhi.optimizer.enumeration.PsoOptimizationFunctionModeStatus;
import dhi.optimizer.enumeration.RHSprayLossType;
import dhi.optimizer.model.ConfDataVO;
import dhi.optimizer.model.ReportInfoVO;
import dhi.optimizer.model.TagDataVO;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.model.db.NoticeHistoryEntity;
import dhi.optimizer.model.db.SystemCheckEntity;
import dhi.optimizer.model.json.AlarmAndEvent;
import dhi.optimizer.model.json.BoilerPart;
import dhi.optimizer.model.json.CoalInfo;
import dhi.optimizer.model.json.CoalProperty;
import dhi.optimizer.model.json.CoalSupplyStatus;
import dhi.optimizer.model.json.CombustionDynamics;
import dhi.optimizer.model.json.CombustionStatus;
import dhi.optimizer.model.json.CombustionStatusPosition;
import dhi.optimizer.model.json.CombustionStatusPositionCoal;
import dhi.optimizer.model.json.CombustionStatusPositionCoalSupply;
import dhi.optimizer.model.json.CombustionStatusPositionOFA;
import dhi.optimizer.model.json.CombustionStatusPositionOFASupply;
import dhi.optimizer.model.json.CombustionStatusPositionT;
import dhi.optimizer.model.json.CombustionStatusT;
import dhi.optimizer.model.json.ContourData;
import dhi.optimizer.model.json.ItemStatus;
import dhi.optimizer.model.json.OFASupplyStatus;
import dhi.optimizer.model.json.Overview;
import dhi.optimizer.model.json.PerformanceAndEfficiency;
import dhi.optimizer.model.json.PerformanceConstSaving;
import dhi.optimizer.model.json.PerformanceEmissionStatus;
import dhi.optimizer.model.json.PerformanceItemStatus;
import dhi.optimizer.model.json.PerformanceProcessStatus;
import dhi.optimizer.model.json.Position;
import dhi.optimizer.model.json.RHSpraySupplyStatus;
import dhi.optimizer.model.json.SASupplyStatus;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.repository.NoticeHistoryRepository;
import dhi.optimizer.repository.ReportRepository;
import dhi.optimizer.repository.SystemCheckRepository;

/*
 * Monitoring service.
 */
@Service
@Transactional
public class MonitoringServiceImpl implements MonitoringService {
	
	private static final Logger logger = LoggerFactory.getLogger(MonitoringService.class);
	private static final String PROCEDURE_NAME_COST_SAVING_EFFECT = "SP_COST_SAVING_EFFECT";
	
	private static final int DAYS_ROW_INDEX = 2;
	private static final int DAYS_CELL_INDEX = 10;
	
	private static final int DATE_ROW_INDEX = 4;	
	private static final int DATE_FROM_CELL_INDEX = 9;
	private static final int DATE_TO_CELL_INDEX = 11;
	
	private static final int KPI_BASELINE_ROW_INDEX = 7;
	private static final int KPI_TAG_VAL_ROW_INDEX = 9;	
	
	private static final int KPI_TAG_VAL_RH_SPRAY_CELL_INDEX = 3;
	private static final int KPI_TAG_VAL_FG_O2_CELL_INDEX = 4;
	private static final int KPI_TAG_VAL_FG_TEMP_DEV_CELL_INDEX = 5;
	private static final int KPI_TAG_VAL_NOX_CELL_INDEX = 6;
	private static final int KPI_TAG_VAL_BLR_EFFICIENCY_CELL_INDEX = 8;
	private static final int KPI_TAG_VAL_UNIT_HEATRATE_CELL_INDEX = 9;
	private static final int KPI_TAG_VAL_AUX_POWER_CELL_INDEX = 10;
	
	private static final int MILL_TAG_VAL_ROW_INDEX = 25;
	private static final int MILL_TAG_VAL_A_CELL_INDEX = 3;
	private static final int MILL_TAG_VAL_B_CELL_INDEX = 4;
	private static final int MILL_TAG_VAL_C_CELL_INDEX = 5;
	private static final int MILL_TAG_VAL_D_CELL_INDEX = 6;
	private static final int MILL_TAG_VAL_E_CELL_INDEX = 7;
	private static final int MILL_TAG_VAL_F_CELL_INDEX = 8;
	private static final int MILL_TAG_VAL_G_CELL_INDEX = 9;	
	private static final int MILL_TAG_VAL_GEN_MW_CELL_INDEX = 10;
	private static final int MILL_TAG_VAL_COAL_FLOW_CELL_INDEX = 11;
	
	private static final int COAL_SUPPLY_ROW_INDEX = 33;
	private static final int COAL_SUPPLY_TOTAL_MOISTRUE_CELL_INDEX = 4;
	private static final int COAL_SUPPLY_ASH_CONTENTS_CELL_INDEX = 5;
	private static final int COAL_SUPPLY_VOLATILE_MATTER_CELL_INDEX = 6;
	private static final int COAL_SUPPLY_FIXED_CARBONE_CELL_INDEX = 7;
	private static final int COAL_SUPPLY_GROSS_CALORIFIC_VALUE_CELL_INDEX = 8;
	
	@Autowired
    protected EntityManagerFactory emFactory;
	
	@Autowired
	private ControlRepository controlRepository;
	
	@Autowired
	private NoticeHistoryRepository noticeHistoryRepository;
	
	@Autowired
	private SystemCheckRepository systemCheckRepository;
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private CommonDataService commonDataService;
	
	@Autowired
	private PerformanceCalculation performanceCalculation;

	public Overview getOverview(String plantUnitId) throws InvalidParameterException {
		
		Overview overView = new Overview();

		if (CommonConst.StringEmpty.equals(plantUnitId)) {
			throw new InvalidParameterException("The plantUnitId parameter is missing.");
		}
		
		HashMap<String, HashMap<String, ConfDataVO>> refConfTypesMap = new HashMap<String, HashMap<String, ConfDataVO>>();
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_BASELINE, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_OPTCONF, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_COAL, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_EFFICIENCY, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_KPI, new HashMap<String, ConfDataVO>());
		
		this.commonDataService.refConfDataMap(plantUnitId, refConfTypesMap);
		HashMap<String, ConfDataVO> baselineConfDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_BASELINE);
		HashMap<String, ConfDataVO> optConfDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_OPTCONF);
		HashMap<String, TagDataVO> opDataTagMap = this.commonDataService.getOpDataTagMap();
		
		performanceCalculation.init(opDataTagMap, refConfTypesMap);

		// Efficiency Status.
		ArrayList<ItemStatus> efficiencyStatusList = new ArrayList<ItemStatus>();
		
		ItemStatus unitHeatRateItemStatus = new ItemStatus();

		ConfDataVO unitHeatRateBaselineVO = baselineConfDataMap.get(CommonConst.UNIT_HEAT_RATE);
		unitHeatRateItemStatus.setItem(unitHeatRateBaselineVO.getConfNm());
		unitHeatRateItemStatus.setBaseline(Utilities.roundIntToString(unitHeatRateBaselineVO.getConfVal()));
		unitHeatRateItemStatus.setCurrent(Utilities.roundIntToString(performanceCalculation.getUnitHeatRate()));
		unitHeatRateItemStatus.setUnit(unitHeatRateBaselineVO.getUnit());
		efficiencyStatusList.add(unitHeatRateItemStatus);
		
		ConfDataVO blrEfficiencyBaselineVO = baselineConfDataMap.get(CommonConst.BLR_EFFICIENCY);
		ItemStatus boilerEfficiencyItemStatus = new ItemStatus();
		boilerEfficiencyItemStatus.setItem(blrEfficiencyBaselineVO.getConfNm());		
		boilerEfficiencyItemStatus.setBaseline(Utilities.roundFirstToString(blrEfficiencyBaselineVO.getConfVal()));
		boilerEfficiencyItemStatus.setCurrent(Utilities.roundFirstToString(performanceCalculation.getBoilerEfficiency()));
		boilerEfficiencyItemStatus.setUnit(blrEfficiencyBaselineVO.getUnit());		
		efficiencyStatusList.add(boilerEfficiencyItemStatus);
		
		overView.setEfficiencyStatus(efficiencyStatusList);

		TagDataVO noxOpDataVO = opDataTagMap.get(CommonConst.NOx);
		
		double ecoOutletO2 = Utilities.round(performanceCalculation.getO2AtEconomizerOutlet(), 1);
		double ecoOutletCO = Utilities.round(performanceCalculation.getCOAtAHOutlet() * 12500, 0);
				
		ConfDataVO o2BaselineVO = baselineConfDataMap.get(CommonConst.FLUE_GAS_O2_AVG);
		ConfDataVO coBaselineVO = baselineConfDataMap.get(CommonConst.FLUE_GAS_CO_AVG);
		ConfDataVO noxBaselineVO = baselineConfDataMap.get(CommonConst.STACK_NOX_AVG);
		ConfDataVO tempBaselineVO = baselineConfDataMap.get(CommonConst.FLUE_GAS_TEMP_LR_DEV);
		
		ConfDataVO o2BaselineRatioVO = optConfDataMap.get(CommonConst.OPTCONF_POLAR_O2_BASELINE_RATIO);
		ConfDataVO coBaselineRatioVO = optConfDataMap.get(CommonConst.OPTCONF_POLAR_CO_BASELINE_RATIO);
		ConfDataVO noxBaselineRatioVO = optConfDataMap.get(CommonConst.OPTCONF_POLAR_NOX_BASELINE_RATIO);
		ConfDataVO tempBaselineRatioVO = optConfDataMap.get(CommonConst.OPTCONF_POLAR_TEMP_BASELINE_RATIO);

		// Emission Status.
		ArrayList<ItemStatus> emissionStatusList = new ArrayList<ItemStatus>();
		ItemStatus o2ItemStatus = new ItemStatus();
		o2ItemStatus.setItem("O2");
		o2ItemStatus.setBaseline(Utilities.roundFirstToString(o2BaselineVO.getConfVal()));
		o2ItemStatus.setCurrent(Utilities.roundFirstToString(ecoOutletO2));
		o2ItemStatus.setUnit(o2BaselineVO.getUnit());
		emissionStatusList.add(o2ItemStatus);
	
		ItemStatus coItemStatus = new ItemStatus();
		coItemStatus.setItem("CO");		
		coItemStatus.setBaseline(Utilities.roundIntToString(coBaselineVO.getConfVal()));
		coItemStatus.setCurrent(Utilities.roundIntToString(ecoOutletCO));
		coItemStatus.setUnit(coBaselineVO.getUnit());
		emissionStatusList.add(coItemStatus);
		
		ItemStatus noxItemStatus = new ItemStatus();
		noxItemStatus.setItem("NOx");		
		noxItemStatus.setBaseline(Utilities.roundIntToString(noxBaselineVO.getConfVal()));
		noxItemStatus.setCurrent(Utilities.roundIntToString(noxOpDataVO.getTagVal()));
		noxItemStatus.setUnit(noxOpDataVO.getUnit());
		emissionStatusList.add(noxItemStatus);
		
		overView.setEmissionStatus(emissionStatusList);
		
		// Slagging & Fouling Status
		ArrayList<ItemStatus> slaggingFoulingStatusList = new ArrayList<ItemStatus>();
		
		ItemStatus pollutionDegreeStatus = new ItemStatus();
		pollutionDegreeStatus.setItem("Pollution Degree");
		pollutionDegreeStatus.setUnit("%");
		pollutionDegreeStatus.setBaseline("0.81");
		pollutionDegreeStatus.setCurrent("0.91");
		slaggingFoulingStatusList.add(pollutionDegreeStatus);
		
		ItemStatus fegtItemStatus = new ItemStatus();
		fegtItemStatus.setItem("FEGT");
		fegtItemStatus.setUnit("");
		fegtItemStatus.setBaseline("730");
		fegtItemStatus.setCurrent("735");
		slaggingFoulingStatusList.add(fegtItemStatus);
		overView.setSlaggingFoulingStatus(slaggingFoulingStatusList);

		// Fire ball Status. - Sasan Plant 는 FireBall 값이 없음 Dummy;
		/*ArrayList<ItemStatus> fireballStatusList = new ArrayList<ItemStatus>();
		ItemStatus peakTempItemStatus = new ItemStatus();
		peakTempItemStatus.setItem("Peak Temp");
		peakTempItemStatus.setUnit("℃");
		peakTempItemStatus.setCurrent("-");
		fireballStatusList.add(peakTempItemStatus);
				
		ItemStatus offCenteringRatioItemStatus = new ItemStatus();
		offCenteringRatioItemStatus.setItem("Off Centering Ratio");
		offCenteringRatioItemStatus.setUnit("%");
		
		String xString = "-";
		// String yString = "-";
		// offCenteringRatioItemStatus.setCurrent(xString + "," + yString);
		offCenteringRatioItemStatus.setCurrent(xString);
		fireballStatusList.add(offCenteringRatioItemStatus);
		overView.setFireballStatus(fireballStatusList);
		*/
		
		// Combustion & Emission Status.	
		ControlEntity ControlEntity = this.controlRepository.findControl();
		PsoOptimizationFunctionModeStatus optFunctionModeStatus = PsoOptimizationFunctionModeStatus.valueOf(ControlEntity.getOptMode());
		
		ArrayList<ItemStatus> itemStatusList = new ArrayList<ItemStatus>();
		itemStatusList.add(new ItemStatus("Combst Opt", ControlEntity.getSystemStart() ? OptimizerFunctionModeStatus.TRUE.getValue() : OptimizerFunctionModeStatus.FALSE.getValue()));
		itemStatusList.add(new ItemStatus("Ctrl Mode", !ControllerModeStatus.OL.name().equals(ControlEntity.getControlMode()) ? ControllerModeStatus.CL.getValue() : ControllerModeStatus.OL.getValue()));
		itemStatusList.add(new ItemStatus("Opt Mode", optFunctionModeStatus.getValue()));
		
		SystemCheckEntity systemCheckEntity = this.systemCheckRepository.findControl();
		itemStatusList.add(new ItemStatus("Control Ready", String.valueOf(systemCheckEntity.getControlReady())));
		
		overView.setOptimizerMode(itemStatusList);

		ArrayList<ItemStatus> optimumZoneList = new ArrayList<ItemStatus>();
		double target = optConfDataMap.get(CommonConst.OPTCONF_POLAR_OPTIMUM_ZONE_RATIO).getConfVal();
		
		ItemStatus optZoneTempItemStatus = new ItemStatus();		
		double tempAbs = Math.abs(opDataTagMap.get(CommonConst.HRZN_FG_temp_L).getTagVal() - opDataTagMap.get(CommonConst.HRZN_FG_temp_R).getTagVal());
		double tempMaxValue = (100 / tempBaselineRatioVO.getConfVal()) * tempBaselineVO.getConfVal();
		double after =  Utilities.valueOfDoubleRange(Utilities.round(100 * tempAbs / tempMaxValue, 1), 1, 99);
		double before = Utilities.valueOfDoubleRange(tempBaselineRatioVO.getConfVal(), 1, 99);
		
		optZoneTempItemStatus.setItem("TEMP");
		optZoneTempItemStatus.setBefore(before);
		optZoneTempItemStatus.setAfter(after);
		optZoneTempItemStatus.setTarget(String.valueOf(target));
		optZoneTempItemStatus.setImpact(Utilities.roundFirstToString(after - before));
		optZoneTempItemStatus.setUnit(tempBaselineRatioVO.getUnit());
		optimumZoneList.add(optZoneTempItemStatus);
		
		ItemStatus optZoneNOxItemStatus = new ItemStatus();
		double noxMaxValue = (100 / noxBaselineRatioVO.getConfVal()) * noxBaselineVO.getConfVal();
		after = Utilities.valueOfDoubleRange(Utilities.round(100 * noxOpDataVO.getTagVal() / noxMaxValue, 1), 1, 99);
		before = Utilities.valueOfDoubleRange(noxBaselineRatioVO.getConfVal(), 1, 99);
		
		optZoneNOxItemStatus.setItem("NOx");
		optZoneNOxItemStatus.setBefore(before);
		optZoneNOxItemStatus.setAfter(after);
		optZoneNOxItemStatus.setTarget(String.valueOf(target));
		optZoneNOxItemStatus.setImpact(Utilities.roundFirstToString(after - before));
		optZoneNOxItemStatus.setUnit(noxBaselineRatioVO.getUnit());
		optimumZoneList.add(optZoneNOxItemStatus);
		
		ItemStatus optZoneCOItemStatus = new ItemStatus();
		double coMaxValue = (100 / coBaselineRatioVO.getConfVal()) * coBaselineVO.getConfVal();
		after = Utilities.valueOfDoubleRange(Utilities.round(100 * ecoOutletCO / coMaxValue, 1), 1, 99);
		before = Utilities.valueOfDoubleRange(coBaselineRatioVO.getConfVal(), 1, 99);
		
		optZoneCOItemStatus.setItem("CO");
		optZoneCOItemStatus.setBefore(before);
		optZoneCOItemStatus.setAfter(after);
		optZoneCOItemStatus.setTarget(String.valueOf(target));
		optZoneCOItemStatus.setImpact(Utilities.roundFirstToString(after - before));
		optZoneCOItemStatus.setUnit(coBaselineRatioVO.getUnit());
		optimumZoneList.add(optZoneCOItemStatus);
		
		ItemStatus optZoneO2ItemStatus = new ItemStatus();
		double o2MaxValue = (100 / o2BaselineRatioVO.getConfVal()) * o2BaselineVO.getConfVal();
		after = Utilities.valueOfDoubleRange(Utilities.round(100 * ecoOutletO2 / o2MaxValue, 1), 1, 99);
		before = Utilities.valueOfDoubleRange(o2BaselineRatioVO.getConfVal(), 1, 99);
		
		optZoneO2ItemStatus.setItem("O2");
		optZoneO2ItemStatus.setBefore(before);
		optZoneO2ItemStatus.setAfter(after);
		optZoneO2ItemStatus.setTarget(String.valueOf(target));
		optZoneO2ItemStatus.setImpact(Utilities.roundFirstToString(after - before));
		optZoneO2ItemStatus.setUnit(o2BaselineRatioVO.getUnit());
		optimumZoneList.add(optZoneO2ItemStatus); 
		
		overView.setOptimumZone(optimumZoneList);
		
		return overView;
	}
	
	@SuppressWarnings("unchecked")
	public PerformanceAndEfficiency getPerformanceAndEfficiency(String plantUnitId) throws InvalidParameterException {
		PerformanceAndEfficiency performanceAndEfficiency = new PerformanceAndEfficiency();

		if (CommonConst.StringEmpty.equals(plantUnitId)) {
			throw new InvalidParameterException("The plantUnitId parameter is missing.");
		}
		
		HashMap<String, HashMap<String, ConfDataVO>> refConfTypesMap = new HashMap<String, HashMap<String, ConfDataVO>>();
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_BASELINE, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_COAL, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_EFFICIENCY, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_KPI, new HashMap<String, ConfDataVO>());
		this.commonDataService.refConfDataMap(plantUnitId, refConfTypesMap);
		
		HashMap<String, TagDataVO> opDataMap = this.commonDataService.getOpDataTagMap();
		HashMap<String, ConfDataVO> baselineDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_BASELINE);
		HashMap<String, ConfDataVO> coalDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_COAL);
		HashMap<String, ConfDataVO> kpiDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_KPI);
		performanceCalculation.init(opDataMap, refConfTypesMap);
		
		// ## Efficiency KPI.
		ArrayList<PerformanceItemStatus> efficiencyKPIList = new ArrayList<PerformanceItemStatus>();

		PerformanceItemStatus unitHeatRateItemStatus = new PerformanceItemStatus();
		ConfDataVO UnitHeatRate = baselineDataMap.get(CommonConst.UNIT_HEAT_RATE);
		unitHeatRateItemStatus.setItem(UnitHeatRate.getConfNm());		
		unitHeatRateItemStatus.setBaseline(Utilities.roundIntToString(UnitHeatRate.getConfVal()));
		unitHeatRateItemStatus.setCurrent(Utilities.roundIntToString(performanceCalculation.getUnitHeatRate()));
		unitHeatRateItemStatus.setUnit(UnitHeatRate.getUnit());
		efficiencyKPIList.add(unitHeatRateItemStatus);

		ArrayList<PerformanceItemStatus> blrAdditionalList = new ArrayList<PerformanceItemStatus>();
		
		PerformanceItemStatus dryGassLossItemStatus = new PerformanceItemStatus();
		ConfDataVO BlrDryGasLoss = baselineDataMap.get(CommonConst.BLR_DRY_GAS_LOSS);
		dryGassLossItemStatus.setItem(BlrDryGasLoss.getConfNm());		
		dryGassLossItemStatus.setBaseline(Utilities.roundSecondToString(BlrDryGasLoss.getConfVal()));
		dryGassLossItemStatus.setCurrent(Utilities.roundSecondToString(performanceCalculation.getDryGasLoss()));
		dryGassLossItemStatus.setUnit(BlrDryGasLoss.getUnit());
		blrAdditionalList.add(dryGassLossItemStatus);

		PerformanceItemStatus ohterLossItemStatus = new PerformanceItemStatus();
		ConfDataVO BlrOtherLoss = baselineDataMap.get(CommonConst.BLR_OTHER_LOSS);
		ohterLossItemStatus.setItem(BlrOtherLoss.getConfNm());				
		ohterLossItemStatus.setBaseline(Utilities.roundSecondToString(BlrOtherLoss.getConfVal()));
		ohterLossItemStatus.setCurrent(Utilities.roundSecondToString(100.0 - (performanceCalculation.getBoilerEfficiency() + performanceCalculation.getDryGasLoss())));
		ohterLossItemStatus.setUnit(BlrOtherLoss.getUnit());
		blrAdditionalList.add(ohterLossItemStatus);

		PerformanceItemStatus blrEfficiencyItemStatus = new PerformanceItemStatus();
		ConfDataVO BlrEfficiency = baselineDataMap.get(CommonConst.BLR_EFFICIENCY);
		blrEfficiencyItemStatus.setItem(BlrEfficiency.getConfNm());
		blrEfficiencyItemStatus.setBaseline(Utilities.roundFirstToString(BlrEfficiency.getConfVal()));
		blrEfficiencyItemStatus.setCurrent(Utilities.roundFirstToString(performanceCalculation.getBoilerEfficiency()));
		blrEfficiencyItemStatus.setUnit(BlrEfficiency.getUnit());
		blrEfficiencyItemStatus.setAdditionalInformationList(blrAdditionalList);
		efficiencyKPIList.add(blrEfficiencyItemStatus);

		ArrayList<PerformanceItemStatus> cycleAdditionalList = new ArrayList<PerformanceItemStatus>();

		PerformanceItemStatus rhSprayLossItemStatus = new PerformanceItemStatus();
		ConfDataVO RhSprayLoss = baselineDataMap.get(CommonConst.RH_SPRAY_LOSS);
		rhSprayLossItemStatus.setItem(RhSprayLoss.getConfNm());		
		rhSprayLossItemStatus.setBaseline(Utilities.roundFirstToString(RhSprayLoss.getConfVal()));
		rhSprayLossItemStatus.setCurrent(Utilities.roundFirstToString(performanceCalculation.getRHSprayLoss(RHSprayLossType.KCAL_KWH)));
		rhSprayLossItemStatus.setUnit(RhSprayLoss.getUnit());
		cycleAdditionalList.add(rhSprayLossItemStatus);

		PerformanceItemStatus cycleHearRateItemStatus = new PerformanceItemStatus();
		ConfDataVO CycleHeatRate = baselineDataMap.get(CommonConst.TBN_CYCLE_HEAT_RATE);
		cycleHearRateItemStatus.setItem(CycleHeatRate.getConfNm());		
		cycleHearRateItemStatus.setBaseline(Utilities.roundIntToString(CycleHeatRate.getConfVal()));
		cycleHearRateItemStatus.setCurrent(Utilities.roundIntToString(performanceCalculation.getTurbineCycleHeatRate()));
		cycleHearRateItemStatus.setUnit(CycleHeatRate.getUnit());
		cycleHearRateItemStatus.setAdditionalInformationList(cycleAdditionalList);
		efficiencyKPIList.add(cycleHearRateItemStatus);
		
		performanceAndEfficiency.setEfficiencyKPI(efficiencyKPIList);
		
		logger.info(performanceCalculation.toString());
		
		// ## Fuel Information.
		ArrayList<PerformanceItemStatus> fuelInformationList = new ArrayList<PerformanceItemStatus>();
		double GCV = coalDataMap.get(CommonConst.GROSS_CALORIFIC_VALUE).getConfVal();
		double FR = coalDataMap.get(CommonConst.FIXED_CARBON).getConfVal() / coalDataMap.get(CommonConst.VOLATILE_MATTER).getConfVal();
		double CombustibilityIndex = GCV/FR * (115 - coalDataMap.get(CommonConst.ASH_CONTENTS).getConfVal()) / 105;
		double CurrentGCV = performanceCalculation.getUnitHeatRate() * opDataMap.get(CommonConst.ACTIVE_POWER_OF_GENERATOR).getTagVal() / performanceCalculation.getCoalSupply();				
		fuelInformationList.add(new PerformanceItemStatus("Coal Supply", "t/h", Utilities.roundIntToString(performanceCalculation.getCoalSupply())));
		fuelInformationList.add(new PerformanceItemStatus("GCV", "kcal/kg", Utilities.roundIntToString(GCV)));
		fuelInformationList.add(new PerformanceItemStatus("Combustibility Index", "kcal/kg", Utilities.roundIntToString(CombustibilityIndex)));
		fuelInformationList.add(new PerformanceItemStatus("Current GCV", "kcal/kg", Utilities.roundIntToString(CurrentGCV)));
		fuelInformationList.add(new PerformanceItemStatus("F/R", "", Utilities.roundSecondToString(FR)));
		performanceAndEfficiency.setFuelInformation(fuelInformationList);
		
		// ## Combustion Condition.
		ArrayList<PerformanceItemStatus> combustionConditionList = new ArrayList<PerformanceItemStatus>();
		TagDataVO TotalAirFlowVO = opDataMap.get(CommonConst.TOTAL_AIR_FLOW);
		double TotalAirFlow = TotalAirFlowVO.getTagVal();
		String TotalAirFlowUnit = TotalAirFlowVO.getUnit();
		double PAFlow = opDataMap.get(CommonConst.MILL_A_INL_PRI_AIR_FLOW).getTagVal()
						+ opDataMap.get(CommonConst.MILL_B_INL_PRI_AIR_FLOW).getTagVal()
						+ opDataMap.get(CommonConst.MILL_C_INL_PRI_AIR_FLOW).getTagVal()
						+ opDataMap.get(CommonConst.MILL_D_INL_PRI_AIR_FLOW).getTagVal()
						+ opDataMap.get(CommonConst.MILL_E_INL_PRI_AIR_FLOW).getTagVal()
						+ opDataMap.get(CommonConst.MILL_F_INL_PRI_AIR_FLOW).getTagVal()
						+ opDataMap.get(CommonConst.MILL_G_INL_PRI_AIR_FLOW).getTagVal();
		double OFAFlow = opDataMap.get(CommonConst.LEFTWALL_OFA_FLOW).getTagVal()
						+ opDataMap.get(CommonConst.RIGHTWALL_OFA_FLOW).getTagVal()
						+ opDataMap.get(CommonConst.FRONTWALL_OFA_FLOW).getTagVal()
						+ opDataMap.get(CommonConst.REARWALL_OFA_FLOW).getTagVal();
		
		combustionConditionList.add(new PerformanceItemStatus("Total Air Flow", TotalAirFlowUnit, Utilities.roundIntToString(TotalAirFlow)));
		combustionConditionList.add(new PerformanceItemStatus("Total SA Flow", TotalAirFlowUnit, Utilities.roundIntToString(TotalAirFlow - PAFlow)));
		combustionConditionList.add(new PerformanceItemStatus("PA Flow", TotalAirFlowUnit, Utilities.roundIntToString(PAFlow)));
		combustionConditionList.add(new PerformanceItemStatus("OFA Flow", TotalAirFlowUnit, Utilities.roundIntToString(OFAFlow)));
		
		String [] coalFeederList = { "A", "B", "C", "D", "E", "F", "G" };
		List<String> newCoalFeederArrayList = new ArrayList<String>();
		int count = 0;
		try { 
			for (int i = 0; i < CommonConst.TOTAL_COALFEEDER_COUNT; i++) {
				 //COAL_FEEDER_A_FEEDRATE
				if(opDataMap.get(CommonConst.class.getField("COAL_FEEDER_" + coalFeederList[i] + "_FEEDRATE").get(null)).getTagVal() >= 10.0) {
					count ++;
					newCoalFeederArrayList.add(coalFeederList[i]);
				}
			}
			
			String [] newCoalFeederList = new String[newCoalFeederArrayList.size()];
			newCoalFeederList = newCoalFeederArrayList.toArray(newCoalFeederList);
			PerformanceItemStatus millPerformanceItemStatus = new PerformanceItemStatus();
			millPerformanceItemStatus.setItem("Mill Operation");
			millPerformanceItemStatus.setCurrent(String.valueOf(count));
			millPerformanceItemStatus.setTotalMills(coalFeederList);
			millPerformanceItemStatus.setOperationMills(newCoalFeederList);
			combustionConditionList.add(millPerformanceItemStatus);
			performanceAndEfficiency.setCombustionCondition(combustionConditionList);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		// ## Process Status.
		PerformanceProcessStatus processStatus = new PerformanceProcessStatus();
		ArrayList<PerformanceItemStatus> furnaceTemperatureList = new ArrayList<PerformanceItemStatus>();
		
		TagDataVO HorizonGasDucFlueGasTLeft = opDataMap.get(CommonConst.HORIZON_GAS_DUCT_FLUE_GAS_T_L);
		TagDataVO HorizonGasDucFlueGasTRight = opDataMap.get(CommonConst.HORIZON_GAS_DUCT_FLUE_GAS_T_R);
		
		double leftFT = HorizonGasDucFlueGasTLeft.getTagVal();
		double rightFT = HorizonGasDucFlueGasTRight.getTagVal();
		
		furnaceTemperatureList.add(new PerformanceItemStatus("Left", HorizonGasDucFlueGasTLeft.getUnit() , Utilities.roundIntToString(leftFT)));
		furnaceTemperatureList.add(new PerformanceItemStatus("Dev'", HorizonGasDucFlueGasTRight.getUnit(), Utilities.roundIntToString(Math.abs(leftFT - rightFT))));
		furnaceTemperatureList.add(new PerformanceItemStatus("Right", HorizonGasDucFlueGasTLeft.getUnit() , Utilities.roundIntToString(rightFT)));
		processStatus.setFurnaceTemperatureList(furnaceTemperatureList);

		TagDataVO RhSprayWtrFlow = opDataMap.get(CommonConst.RH_SPRAY_WTR_FLOW);
		
		ArrayList<PerformanceItemStatus> rhSprayFlowList = new ArrayList<PerformanceItemStatus>();
		rhSprayFlowList.add(new PerformanceItemStatus("Total Flow", RhSprayWtrFlow.getUnit(), Utilities.roundIntToString(RhSprayWtrFlow.getTagVal())));
		rhSprayFlowList.add(new PerformanceItemStatus("1st stg L", RhSprayWtrFlow.getUnit(), Utilities.roundIntToString(opDataMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_L_3).getTagVal())));
		rhSprayFlowList.add(new PerformanceItemStatus("1st stg R", RhSprayWtrFlow.getUnit(), Utilities.roundIntToString(opDataMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_R_3).getTagVal())));
		rhSprayFlowList.add(new PerformanceItemStatus("2nd stg L", RhSprayWtrFlow.getUnit(), Utilities.roundIntToString(opDataMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_FLOW_L).getTagVal())));
		rhSprayFlowList.add(new PerformanceItemStatus("2nd stg R", RhSprayWtrFlow.getUnit(), Utilities.roundIntToString(opDataMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_FLOW_R).getTagVal())));
		processStatus.setRhSprayFlow(rhSprayFlowList);
		performanceAndEfficiency.setProcessStatus(processStatus);

		// ## Emission Status.		
		PerformanceEmissionStatus emissionStatus = new PerformanceEmissionStatus();
		
		TagDataVO StackNOx = opDataMap.get(CommonConst.NOx);
		TagDataVO StackCO = opDataMap.get(CommonConst.Stack_CO);
		TagDataVO StackO2 = opDataMap.get(CommonConst.Stack_O2);
		TagDataVO APHAFlueGasTemp = opDataMap.get(CommonConst.APH_A_INLET_FLUE_GAS_TEMP_1);
		
		ArrayList<PerformanceItemStatus> ecoOutletFlueGasList = new ArrayList<PerformanceItemStatus>();
		ecoOutletFlueGasList.add(new PerformanceItemStatus("Temp", APHAFlueGasTemp.getUnit(), Utilities.roundIntToString(performanceCalculation.getEconomizerOutlet())));
		ecoOutletFlueGasList.add(new PerformanceItemStatus("CO", StackCO.getUnit(), Utilities.roundIntToString((performanceCalculation.getCOAtAHOutlet() * 12500))));
		ecoOutletFlueGasList.add(new PerformanceItemStatus("O2", StackO2.getUnit(), Utilities.roundFirstToString(performanceCalculation.getO2AtEconomizerOutlet())));
		emissionStatus.setEcoOutletFlueGasList(ecoOutletFlueGasList);

		ArrayList<PerformanceItemStatus> stackFlueGasList = new ArrayList<PerformanceItemStatus>();
		stackFlueGasList.add(new PerformanceItemStatus("NOx", StackNOx.getUnit(), Utilities.roundIntToString(StackNOx.getTagVal())));
		stackFlueGasList.add(new PerformanceItemStatus("CO", StackCO.getUnit(), Utilities.roundIntToString(StackCO.getTagVal())));
		stackFlueGasList.add(new PerformanceItemStatus("O2", StackO2.getUnit(), Utilities.roundFirstToString(StackO2.getTagVal())));
		emissionStatus.setStackFlueGasList(stackFlueGasList);
		performanceAndEfficiency.setEmissionStatus(emissionStatus);

		// ## Cost saving information.
		ArrayList<PerformanceConstSaving> costSavingList = new ArrayList<PerformanceConstSaving>();	
		
		double unitHeatRateBenefitBySolution = (baselineDataMap.get(CommonConst.BLR_DRY_GAS_LOSS).getConfVal() - performanceCalculation.getDryGasLoss()) * performanceCalculation.getUnitHeatRate() / performanceCalculation.getBoilerEfficiency() + (baselineDataMap.get(CommonConst.RH_SPRAY_LOSS).getConfVal() - performanceCalculation.getRHSprayLoss(RHSprayLossType.KCAL_KWH)) * 100 / performanceCalculation.getBoilerEfficiency();
		double costSaving = unitHeatRateBenefitBySolution * opDataMap.get(CommonConst.TOTAL_COAL_FLOW).getTagVal() / baselineDataMap.get(CommonConst.UNIT_HEAT_RATE).getConfVal();
		
		EntityManager em = this.emFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		List<Object[]> resultList = null;
		try {
			tx.begin();
			StoredProcedureQuery query = em.createStoredProcedureQuery(PROCEDURE_NAME_COST_SAVING_EFFECT);
			resultList = query.getResultList();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			em.close();
		}
		
		String dailyCoalSaving = CommonConst.StringEmpty;
		String monthlyCoalSaving = CommonConst.StringEmpty;
		String yearlyCoalSaving = CommonConst.StringEmpty;
		String dailyPowerSaving = CommonConst.StringEmpty;
		String monthlyPowerSaving = CommonConst.StringEmpty;
		String yearlyPowerSaving = CommonConst.StringEmpty;
		
		if (resultList != null) {
			Object[] result = resultList.get(0);
			dailyCoalSaving = String.valueOf(result[0]);
			monthlyCoalSaving = String.valueOf(result[1]);
			yearlyCoalSaving = String.valueOf(result[2]);
			dailyPowerSaving = String.valueOf(result[3]);
			monthlyPowerSaving = String.valueOf(result[4]);
			yearlyPowerSaving = String.valueOf(result[5]);
		}
		
		/* Cost Saving Information 표기 방법
		 * 1. The Current Effect: Coal Hourly, NH3 Hourly, Total Hourly (화면단에서 Total이 Power로 표시됨) 
		 * 2. The Cumulative Effects: 
		 *  - Coal Daily, Coal Monthly, Coal Yearly
		 *  - Total Daily, Total Monthly, Total Yearly
		 */
		PerformanceConstSaving coalPerformanceConstSaving = new PerformanceConstSaving();
		coalPerformanceConstSaving.setItem("Coal");	
		coalPerformanceConstSaving.setHourly(Utilities.roundFirstToString(Utilities.valueOfDoubleRange(costSaving, -9.9, 99.9)));
		coalPerformanceConstSaving.setDaily(Utilities.roundFirstToString(Utilities.valueOfDoubleRange(Double.parseDouble(dailyCoalSaving), -99.9, 999.9)));
		coalPerformanceConstSaving.setMonthly(Utilities.roundSecondToString(Utilities.valueOfDoubleRange(Double.parseDouble(monthlyCoalSaving), -9.99, 99.99)));
		coalPerformanceConstSaving.setAnnually(Utilities.roundFirstToString(Utilities.valueOfDoubleRange(Double.parseDouble(yearlyCoalSaving), -99.9, 999.9)));
		coalPerformanceConstSaving.setHourlyUnit("ton/hr");
		coalPerformanceConstSaving.setDailyUnit("ton");
		coalPerformanceConstSaving.setMonthlyUnit("kton");
		coalPerformanceConstSaving.setAnnuallyUnit("kton");
		costSavingList.add(coalPerformanceConstSaving);
		
		PerformanceConstSaving ammoniaPerformanceConstSaving = new PerformanceConstSaving();
		ammoniaPerformanceConstSaving.setItem("NH3");		
		ammoniaPerformanceConstSaving.setHourly(Utilities.roundIntToString(Utilities.valueOfDoubleRange(performanceCalculation.getAmmoniaSavingBenefit(), -99, 999)));
		ammoniaPerformanceConstSaving.setHourlyUnit("$/hr");
		costSavingList.add(ammoniaPerformanceConstSaving);
		
		double auxPowerSaving = baselineDataMap.get(CommonConst.TOTAL_AUX_POWER).getConfVal() - performanceCalculation.getTotalAuxPower();
		double electricityPricing = kpiDataMap.get(CommonConst.ELECTRICITY_PRICE).getConfVal();
		double auxPowerSavingBenefit = auxPowerSaving * 1000 * electricityPricing;
		
		PerformanceConstSaving powerPerformanceConstSaving = new PerformanceConstSaving();
		powerPerformanceConstSaving.setItem("Total");		
		powerPerformanceConstSaving.setHourly(Utilities.roundIntToString(Utilities.valueOfDoubleRange(auxPowerSavingBenefit, -99, 999)));
		powerPerformanceConstSaving.setDaily(Utilities.roundToStringAuto(Utilities.valueOfDoubleRange(Double.parseDouble(dailyPowerSaving), -99.9, 9999), 4, 1));
		powerPerformanceConstSaving.setMonthly(Utilities.roundFirstToString(Utilities.valueOfDoubleRange(Double.parseDouble(monthlyPowerSaving), -99.9, 999.9)));
		powerPerformanceConstSaving.setAnnually(Utilities.roundIntToString(Utilities.valueOfDoubleRange(Double.parseDouble(yearlyPowerSaving), -999, 9999)));
		powerPerformanceConstSaving.setHourlyUnit("$/hr");
		powerPerformanceConstSaving.setDailyUnit("$");
		powerPerformanceConstSaving.setMonthlyUnit("k$");
		powerPerformanceConstSaving.setAnnuallyUnit("k$");
		costSavingList.add(powerPerformanceConstSaving);
		
		performanceAndEfficiency.setCostSavingInformationVersionSasan(costSavingList);

		return performanceAndEfficiency;
	}
	
	/**
	 * Ability to check the results of a report query. <br>
	 * @param plantUnitId Plant Unit ID.
	 * @param inputStream Base Excel Input Stream.
	 * @param startDateString EndDate.
	 * @param Whether the result is confirmed.
	 * @return Whether report results are being viewed.
	 */
	public Boolean isReportSearchExist(String plantUnitId, String startDateString, String endDateString) throws InvalidParameterException {		
		
		if (CommonConst.StringEmpty.equals(plantUnitId)) {
			throw new InvalidParameterException("The plantUnitId parameter is missing.");
		}
		
		Boolean isReportSearchExist = false;		
		try {
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);
			
			int reportResultCount = this.reportRepository.getReportCountByPeriod(startDate, endDate);
			if(reportResultCount > 0)
				isReportSearchExist = true;			    
	    
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
	    return isReportSearchExist;
	}
	
	/**
	 * Make Excel WorkBook for Report. <br>
	 * @param plantUnitId Plant Unit ID.
	 * @param inputStream Base Excel Input Stream.
	 * @param startDateString EndDate.
	 * @param endDateString EndDate.
	 * @return Excel workbook for report.
	 */
	public XSSFWorkbook getReportExcelWorkBook(String plantUnitId, InputStream inputStream, String startDateString, String endDateString) throws InvalidParameterException {		
		if (CommonConst.StringEmpty.equals(plantUnitId)) {
			throw new InvalidParameterException("The plantUnitId parameter is missing.");
		}
		
		XSSFWorkbook workbook = null;

		try {
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDateString + " 23:59:59");
			
			HashMap<String, ReportInfoVO> reportInfoMap = new HashMap<String, ReportInfoVO>();
			List<Object[]> reportList = this.reportRepository.getReportByPeriod(startDate, endDate);
			
			if(reportList == null)
				return null;

			for (Object[] report : reportList) {
				reportInfoMap.put(String.valueOf(report[0]), new ReportInfoVO((double) report[1], (int) report[2]));
			}
			
			HashMap<String, HashMap<String, ConfDataVO>> refConfTypesMap = new HashMap<String, HashMap<String, ConfDataVO>>();
			refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_BASELINE, new HashMap<String, ConfDataVO>());
			this.commonDataService.refConfDataMap(plantUnitId, refConfTypesMap);
			
			HashMap<String, ConfDataVO> baselineDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_BASELINE);
			
			workbook = new XSSFWorkbook(inputStream);
		    XSSFSheet worksheet = workbook.getSheetAt(0);
		    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();		    
		    
		    // Days
			long diffDate = (endDate.getTime() + 1000) - startDate.getTime();
			int diffDays = (int)(diffDate / (24 * 60 * 60 * 1000));
			this.setWorkSheet(DAYS_ROW_INDEX, DAYS_CELL_INDEX, diffDays, worksheet);
		    
		    // Date From To		    
		    SimpleDateFormat fromFormat = new SimpleDateFormat("dd. MMM. yyyy", new Locale("en", "US"));
		    this.setWorkSheetString(DATE_ROW_INDEX, DATE_FROM_CELL_INDEX, fromFormat.format(startDate), worksheet);
		    this.setWorkSheetString(DATE_ROW_INDEX, DATE_TO_CELL_INDEX, fromFormat.format(endDate), worksheet);
		    
		    // - KPIs Result
		    this.setWorkSheet(KPI_BASELINE_ROW_INDEX, KPI_TAG_VAL_RH_SPRAY_CELL_INDEX, baselineDataMap.get(CommonConst.RH_SPRAY_FOW_AVG).getConfVal(), worksheet);
		    this.setWorkSheet(KPI_BASELINE_ROW_INDEX, KPI_TAG_VAL_FG_O2_CELL_INDEX, baselineDataMap.get(CommonConst.FLUE_GAS_O2_AVG).getConfVal(), worksheet);
		    this.setWorkSheet(KPI_BASELINE_ROW_INDEX, KPI_TAG_VAL_FG_TEMP_DEV_CELL_INDEX, baselineDataMap.get(CommonConst.FLUE_GAS_TEMP_LR_DEV).getConfVal(), worksheet);
		    this.setWorkSheet(KPI_BASELINE_ROW_INDEX, KPI_TAG_VAL_NOX_CELL_INDEX, baselineDataMap.get(CommonConst.STACK_NOX_AVG).getConfVal(), worksheet);
		    this.setWorkSheet(KPI_BASELINE_ROW_INDEX, KPI_TAG_VAL_BLR_EFFICIENCY_CELL_INDEX, baselineDataMap.get(CommonConst.BLR_EFFICIENCY).getConfVal(), worksheet);
		    this.setWorkSheet(KPI_BASELINE_ROW_INDEX, KPI_TAG_VAL_UNIT_HEATRATE_CELL_INDEX, baselineDataMap.get(CommonConst.UNIT_HEAT_RATE).getConfVal(), worksheet);
		    this.setWorkSheet(KPI_BASELINE_ROW_INDEX, KPI_TAG_VAL_AUX_POWER_CELL_INDEX, baselineDataMap.get(CommonConst.TOTAL_AUX_POWER).getConfVal(), worksheet);
		    		    
		    // - KPIs Result
		    this.setWorkSheet(KPI_TAG_VAL_ROW_INDEX, KPI_TAG_VAL_RH_SPRAY_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_RH_SPRAY).getReportAvg(), worksheet);
		    this.setWorkSheet(KPI_TAG_VAL_ROW_INDEX, KPI_TAG_VAL_FG_O2_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_FG_O2).getReportAvg(), worksheet);
		    this.setWorkSheet(KPI_TAG_VAL_ROW_INDEX, KPI_TAG_VAL_FG_TEMP_DEV_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_FG_TEMP_DEV).getReportAvg(), worksheet);
		    this.setWorkSheet(KPI_TAG_VAL_ROW_INDEX, KPI_TAG_VAL_NOX_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_NOX).getReportAvg(), worksheet);
		    this.setWorkSheet(KPI_TAG_VAL_ROW_INDEX, KPI_TAG_VAL_BLR_EFFICIENCY_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_BLR_EFFICIENCY).getReportAvg(), worksheet);
		    this.setWorkSheet(KPI_TAG_VAL_ROW_INDEX, KPI_TAG_VAL_UNIT_HEATRATE_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_UNIT_HEATRATE).getReportAvg(), worksheet);
		    this.setWorkSheet(KPI_TAG_VAL_ROW_INDEX, KPI_TAG_VAL_AUX_POWER_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_AUX_POWER).getReportAvg(), worksheet);
		    
		    // - Mill Combination & Load Condition
		    ReportInfoVO reportInfoVO = reportInfoMap.get(CommonConst.REPORT_ITEM_MILL_MILL_A);
		    double millValue = reportInfoVO.getReportSum() / reportInfoVO.getReportCount() * 100;
		    this.setWorkSheet(MILL_TAG_VAL_ROW_INDEX, MILL_TAG_VAL_A_CELL_INDEX, millValue, worksheet);
		    
		    reportInfoVO = reportInfoMap.get(CommonConst.REPORT_ITEM_MILL_MILL_B);
		    millValue = reportInfoVO.getReportSum() / reportInfoVO.getReportCount() * 100;
		    this.setWorkSheet(MILL_TAG_VAL_ROW_INDEX, MILL_TAG_VAL_B_CELL_INDEX, millValue, worksheet);
		    
		    reportInfoVO = reportInfoMap.get(CommonConst.REPORT_ITEM_MILL_MILL_C);
		    millValue = reportInfoVO.getReportSum() / reportInfoVO.getReportCount() * 100;
		    this.setWorkSheet(MILL_TAG_VAL_ROW_INDEX, MILL_TAG_VAL_C_CELL_INDEX, millValue, worksheet);
		    
		    reportInfoVO = reportInfoMap.get(CommonConst.REPORT_ITEM_MILL_MILL_D);
		    millValue = reportInfoVO.getReportSum() / reportInfoVO.getReportCount() * 100;
		    this.setWorkSheet(MILL_TAG_VAL_ROW_INDEX, MILL_TAG_VAL_D_CELL_INDEX, millValue, worksheet);
		    
		    reportInfoVO = reportInfoMap.get(CommonConst.REPORT_ITEM_MILL_MILL_E);
		    millValue = reportInfoVO.getReportSum() / reportInfoVO.getReportCount() * 100;
		    this.setWorkSheet(MILL_TAG_VAL_ROW_INDEX, MILL_TAG_VAL_E_CELL_INDEX, millValue, worksheet);
		    
		    reportInfoVO = reportInfoMap.get(CommonConst.REPORT_ITEM_MILL_MILL_F);
		    millValue = reportInfoVO.getReportSum() / reportInfoVO.getReportCount() * 100;
		    this.setWorkSheet(MILL_TAG_VAL_ROW_INDEX, MILL_TAG_VAL_F_CELL_INDEX, millValue, worksheet);
		    
		    reportInfoVO = reportInfoMap.get(CommonConst.REPORT_ITEM_MILL_MILL_G);
		    millValue = reportInfoVO.getReportSum() / reportInfoVO.getReportCount() * 100;
		    this.setWorkSheet(MILL_TAG_VAL_ROW_INDEX, MILL_TAG_VAL_G_CELL_INDEX, millValue, worksheet);
		    
		    this.setWorkSheet(MILL_TAG_VAL_ROW_INDEX, MILL_TAG_VAL_GEN_MW_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_MILL_MILL_GEN_MW).getReportAvg(), worksheet);
		    this.setWorkSheet(MILL_TAG_VAL_ROW_INDEX, MILL_TAG_VAL_COAL_FLOW_CELL_INDEX , reportInfoMap.get(CommonConst.REPORT_ITEM_MILL_MILL_COAL_FLOW).getReportAvg(), worksheet);
 
		    // Coal Supply Condition
		    this.setWorkSheet(COAL_SUPPLY_ROW_INDEX, COAL_SUPPLY_TOTAL_MOISTRUE_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_TOTAL_MOISTURE).getReportAvg(), worksheet);
		    this.setWorkSheet(COAL_SUPPLY_ROW_INDEX, COAL_SUPPLY_ASH_CONTENTS_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_ASH_CONTENTS).getReportAvg(), worksheet);
		    this.setWorkSheet(COAL_SUPPLY_ROW_INDEX, COAL_SUPPLY_VOLATILE_MATTER_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_VOLATILE_MATTER).getReportAvg(), worksheet);
		    this.setWorkSheet(COAL_SUPPLY_ROW_INDEX, COAL_SUPPLY_FIXED_CARBONE_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORRAL_FIXED_CARBONE).getReportAvg(), worksheet);
		    this.setWorkSheet(COAL_SUPPLY_ROW_INDEX, COAL_SUPPLY_GROSS_CALORIFIC_VALUE_CELL_INDEX, reportInfoMap.get(CommonConst.REPORT_ITEM_NORMAL_GROSS_CALORIFIC_VALUE).getReportAvg(), worksheet);
		    
		    evaluator.evaluateAll();
	    
		} catch (Exception e) {			
			try {
				if (workbook != null)
					workbook.close();
			} catch (IOException e1) {
				logger.error(e1.getMessage());
			}
			
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} 
	
	    return workbook;		
	}
	
	public CombustionStatus getCombustionStatus(String plantUnitId) throws InvalidParameterException {
		
		if (CommonConst.StringEmpty.equals(plantUnitId)) {
			throw new InvalidParameterException("The plantUnitId parameter is missing.");
		}
		
		HashMap<String, TagDataVO> opDataTagMap = this.commonDataService.getOpDataTagMap();
		HashMap<String, ConfDataVO> coalDataMap = this.commonDataService.getConfDataMap(plantUnitId, CommonConst.CONFING_SETTING_TYPE_COAL);
		TagDataVO tagDataVO = null;
		
		CombustionStatus combustionStatus = new CombustionStatus();
		
		// ## Coal Property.
		CoalProperty coalProperty = new CoalProperty();
		
		// Coal Property - Chemical composition.
		ArrayList<ItemStatus> chemicalCompositionList = new ArrayList<ItemStatus>();
		chemicalCompositionList.add(new ItemStatus("C", coalDataMap.get(CommonConst.C).getUnit(), Utilities.roundIntToString(coalDataMap.get(CommonConst.C).getConfVal())));
		chemicalCompositionList.add(new ItemStatus("H", coalDataMap.get(CommonConst.H).getUnit(), Utilities.roundFirstToString(coalDataMap.get(CommonConst.H).getConfVal())));
		chemicalCompositionList.add(new ItemStatus("N", coalDataMap.get(CommonConst.N).getUnit(), Utilities.roundFirstToString(coalDataMap.get(CommonConst.N).getConfVal())));
		chemicalCompositionList.add(new ItemStatus("S", coalDataMap.get(CommonConst.S).getUnit(), Utilities.roundFirstToString(coalDataMap.get(CommonConst.S).getConfVal())));
		coalProperty.setChemicalCompositionList(chemicalCompositionList);
		
		// Coal Property - Humidity, Calories, Ash.
		CoalInfo coalInfo = new CoalInfo();
		coalInfo.setMoisture(Utilities.roundIntToString(coalDataMap.get(CommonConst.TOTAL_MOISTURE).getConfVal()));
		coalInfo.setMoistureUnit(coalDataMap.get(CommonConst.TOTAL_MOISTURE).getUnit());
		coalInfo.setGcv(Utilities.roundIntToString(coalDataMap.get(CommonConst.GROSS_CALORIFIC_VALUE).getConfVal()));
		coalInfo.setGcvUnit(coalDataMap.get(CommonConst.GROSS_CALORIFIC_VALUE).getUnit());
		coalInfo.setAsh(Utilities.roundIntToString(coalDataMap.get(CommonConst.ASH_CONTENTS).getConfVal()));
		coalInfo.setAshUnit(coalDataMap.get(CommonConst.ASH_CONTENTS).getUnit());
		coalProperty.setCoalInfo(coalInfo);
		
		combustionStatus.setCoalProperty(coalProperty);

		// ## Position.
		List<CombustionStatusPosition> combustionStatusPositionList = new ArrayList<CombustionStatusPosition>();

		String[] wallNames = new String[] { "left", "rear", "right", "front" };
		String[] constWalls = new String[] { "LFT", "REAR", "RT", "FRT" };
		String[] constCorners = new String[] { "C1", "C2", "C3", "C4" };
		
		try {			
			for (int i = 0; i < wallNames.length; i++) {
				String wallName = wallNames[i];
				String constWall = constWalls[i];
				String constCorner = constCorners[i];

				CombustionStatusPosition combustionStatusPosition = new CombustionStatusPosition();
				combustionStatusPosition.setItem(wallName);

				// Position - OFA Supply.
				List<OFASupplyStatus> ofaSupplyStatusList = new ArrayList<OFASupplyStatus>();

				OFASupplyStatus ofaSupplyStatus1 = new OFASupplyStatus();
				tagDataVO = opDataTagMap.get(CommonConst.class.getField("W_" + constWall + "_UOFA1_DMPR_POS").get(null));
				ofaSupplyStatus1.setItem("UOFA1");
				ofaSupplyStatus1.setDamperOpenCurrnet(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				ofaSupplyStatus1.setDamperUnit(tagDataVO.getUnit());
				ofaSupplyStatus1.setYawCurrent(Utilities.roundFirstToString(-15));
				ofaSupplyStatus1.setYawUnit("˚");
				ofaSupplyStatusList.add(ofaSupplyStatus1);

				OFASupplyStatus ofaSupplyStatus2 = new OFASupplyStatus();
				tagDataVO = opDataTagMap.get(CommonConst.class.getField("W_" + constWall + "_UOFA2_DMPR_POS").get(null));
				ofaSupplyStatus2.setItem("UOFA2");
				ofaSupplyStatus2.setDamperOpenCurrnet(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				ofaSupplyStatus2.setDamperUnit(tagDataVO.getUnit());
				ofaSupplyStatus2.setYawCurrent(Utilities.roundFirstToString(-15));
				ofaSupplyStatus2.setYawUnit("˚");
				ofaSupplyStatusList.add(ofaSupplyStatus2);

				OFASupplyStatus ofaSupplyStatus3 = new OFASupplyStatus();
				tagDataVO = opDataTagMap.get(CommonConst.class.getField("W_" + constWall + "_UOFA3_DMPR_POS").get(null));
				ofaSupplyStatus3.setItem("UOFA3");
				ofaSupplyStatus3.setDamperOpenCurrnet(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				ofaSupplyStatus3.setDamperUnit(tagDataVO.getUnit());
				ofaSupplyStatus3.setYawCurrent(Utilities.roundFirstToString(-15));
				ofaSupplyStatus3.setYawUnit("˚");
				ofaSupplyStatusList.add(ofaSupplyStatus3);

				combustionStatusPosition.setOfaSupplyStatusList(ofaSupplyStatusList);

				// Left Position - Coal & PA Supply G-A.
				List<CoalSupplyStatus> coalSupplyStatusList = new ArrayList<CoalSupplyStatus>();

				// Mill G
				CoalSupplyStatus coalSupplyStatus1 = new CoalSupplyStatus();
				
				tagDataVO = opDataTagMap.get(CommonConst.COAL_FEEDER_G_FEEDRATE);

				coalSupplyStatus1.setCoalCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus1.setCoalUnit(tagDataVO.getUnit());
				coalSupplyStatus1.setMillNumber("G");
				
				String coalSupplyStatus = Double.parseDouble(coalSupplyStatus1.getCoalCurrent()) >= 10.0 ? "warning" : "normal";
				coalSupplyStatus1.setStatus(coalSupplyStatus);
				
				tagDataVO = opDataTagMap.get(CommonConst.G_PA);
				coalSupplyStatus1.setPaCurrent(Utilities.roundIntToString(tagDataVO.getTagVal()));
				coalSupplyStatus1.setPaUnit(tagDataVO.getUnit());
				
				tagDataVO = opDataTagMap.get(CommonConst.class.getField("G_SAD_" + constCorner).get(null));
				coalSupplyStatus1.setSadCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus1.setSadUnit(tagDataVO.getUnit());

				coalSupplyStatusList.add(coalSupplyStatus1);

				// Mill F
				CoalSupplyStatus coalSupplyStatus2 = new CoalSupplyStatus();
				
				tagDataVO = opDataTagMap.get(CommonConst.COAL_FEEDER_F_FEEDRATE);
				coalSupplyStatus2.setCoalCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus2.setCoalUnit(tagDataVO.getUnit());
				
				coalSupplyStatus2.setMillNumber("F");
				coalSupplyStatus = Double.parseDouble(coalSupplyStatus2.getCoalCurrent()) >= 10.0 ? "warning" : "normal";
				coalSupplyStatus2.setStatus(coalSupplyStatus);

				tagDataVO = opDataTagMap.get(CommonConst.F_PA);
				coalSupplyStatus2.setPaCurrent(Utilities.roundIntToString(tagDataVO.getTagVal()));
				coalSupplyStatus2.setPaUnit(tagDataVO.getUnit());

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("F_SAD_" + constCorner).get(null));
				coalSupplyStatus2.setSadCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus2.setSadUnit(tagDataVO.getUnit());
				coalSupplyStatusList.add(coalSupplyStatus2);

				// Mill E
				CoalSupplyStatus coalSupplyStatus3 = new CoalSupplyStatus();
				
				tagDataVO = opDataTagMap.get(CommonConst.COAL_FEEDER_E_FEEDRATE);
				coalSupplyStatus3.setCoalCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus3.setCoalUnit(tagDataVO.getUnit());
				
				coalSupplyStatus3.setMillNumber("E");
				coalSupplyStatus = Double.parseDouble(coalSupplyStatus3.getCoalCurrent()) >= 10.0 ? "warning" : "normal";
				coalSupplyStatus3.setStatus(coalSupplyStatus);

				tagDataVO = opDataTagMap.get(CommonConst.E_PA);
				coalSupplyStatus3.setPaCurrent(Utilities.roundIntToString(tagDataVO.getTagVal()));
				coalSupplyStatus3.setPaUnit(tagDataVO.getUnit());

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("E_SAD_" + constCorner).get(null));
				coalSupplyStatus3.setSadCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus3.setSadUnit(tagDataVO.getUnit());
				coalSupplyStatusList.add(coalSupplyStatus3);

				// Mill D
				CoalSupplyStatus coalSupplyStatus4 = new CoalSupplyStatus();
				
				tagDataVO = opDataTagMap.get(CommonConst.COAL_FEEDER_D_FEEDRATE);
				coalSupplyStatus4.setCoalCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus4.setCoalUnit(tagDataVO.getUnit());

				coalSupplyStatus4.setMillNumber("D");
				coalSupplyStatus = Double.parseDouble(coalSupplyStatus4.getCoalCurrent()) >= 10.0 ? "warning" : "normal";
				coalSupplyStatus4.setStatus(coalSupplyStatus);
				
				tagDataVO = opDataTagMap.get(CommonConst.D_PA);
				coalSupplyStatus4.setPaCurrent(Utilities.roundIntToString(tagDataVO.getTagVal()));
				coalSupplyStatus4.setPaUnit(tagDataVO.getUnit());

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("D_SAD_" + constCorner).get(null));
				coalSupplyStatus4.setSadCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus4.setSadUnit(tagDataVO.getUnit());
				coalSupplyStatusList.add(coalSupplyStatus4);

				// Mill C
				CoalSupplyStatus coalSupplyStatus5 = new CoalSupplyStatus();
				
				tagDataVO = opDataTagMap.get(CommonConst.COAL_FEEDER_C_FEEDRATE);
				coalSupplyStatus5.setCoalCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus5.setCoalUnit(tagDataVO.getUnit());
				
				coalSupplyStatus5.setMillNumber("C");
				coalSupplyStatus = Double.parseDouble(coalSupplyStatus5.getCoalCurrent()) >= 10.0 ? "warning" : "normal";
				coalSupplyStatus5.setStatus(coalSupplyStatus);

				tagDataVO = opDataTagMap.get(CommonConst.C_PA);
				coalSupplyStatus5.setPaCurrent(Utilities.roundIntToString(tagDataVO.getTagVal()));
				coalSupplyStatus5.setPaUnit(tagDataVO.getUnit());

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("C_SAD_" + constCorner).get(null));
				coalSupplyStatus5.setSadCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus5.setSadUnit(tagDataVO.getUnit());
				coalSupplyStatusList.add(coalSupplyStatus5);

				// Mill B
				CoalSupplyStatus coalSupplyStatus6 = new CoalSupplyStatus();
				
				tagDataVO = opDataTagMap.get(CommonConst.COAL_FEEDER_B_FEEDRATE);
				coalSupplyStatus6.setCoalCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus6.setCoalUnit(tagDataVO.getUnit());

				coalSupplyStatus6.setMillNumber("B");
				coalSupplyStatus = Double.parseDouble(coalSupplyStatus6.getCoalCurrent()) >= 10.0 ? "warning" : "normal";
				coalSupplyStatus6.setStatus(coalSupplyStatus);
				
				tagDataVO = opDataTagMap.get(CommonConst.B_PA);
				coalSupplyStatus6.setPaCurrent(Utilities.roundIntToString(tagDataVO.getTagVal()));
				coalSupplyStatus6.setPaUnit(tagDataVO.getUnit());

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("B_SAD_" + constCorner).get(null));
				coalSupplyStatus6.setSadCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus6.setSadUnit(tagDataVO.getUnit());
				coalSupplyStatusList.add(coalSupplyStatus6);

				// Mill A
				CoalSupplyStatus coalSupplyStatus7 = new CoalSupplyStatus();
				
				tagDataVO = opDataTagMap.get(CommonConst.COAL_FEEDER_A_FEEDRATE);
				coalSupplyStatus7.setCoalCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus7.setCoalUnit(tagDataVO.getUnit());
				
				coalSupplyStatus7.setMillNumber("A");
				coalSupplyStatus = Double.parseDouble(coalSupplyStatus7.getCoalCurrent()) >= 10.0 ? "warning" : "normal";
				coalSupplyStatus7.setStatus(coalSupplyStatus);

				tagDataVO = opDataTagMap.get(CommonConst.A_PA);
				coalSupplyStatus7.setPaCurrent(Utilities.roundIntToString(tagDataVO.getTagVal()));
				coalSupplyStatus7.setPaUnit(tagDataVO.getUnit());

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("A_SAD_" + constCorner).get(null));
				coalSupplyStatus7.setSadCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
				coalSupplyStatus7.setSadUnit(tagDataVO.getUnit());
				coalSupplyStatusList.add(coalSupplyStatus7);

				combustionStatusPosition.setCoalSupplyStatusList(coalSupplyStatusList);

				// Left Position - Coal & SA Supply GG-AA.
				List<SASupplyStatus> saSupplyStatusList = new ArrayList<SASupplyStatus>();
				tagDataVO = opDataTagMap.get(CommonConst.class.getField("GG_DAMPER_OPEN_" + constCorner).get(null));
				saSupplyStatusList.add(new SASupplyStatus("GG", Utilities.roundFirstToString(tagDataVO.getTagVal()), tagDataVO.getUnit()));

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("FG_DAMPER_OPEN_" + constCorner).get(null));
				saSupplyStatusList.add(new SASupplyStatus("FG", Utilities.roundFirstToString(tagDataVO.getTagVal()), tagDataVO.getUnit()));

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("EF_DAMPER_OPEN_" + constCorner).get(null));
				saSupplyStatusList.add(new SASupplyStatus("EF", Utilities.roundFirstToString(tagDataVO.getTagVal()), tagDataVO.getUnit()));

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("DE_DAMPER_OPEN_" + constCorner).get(null));
				saSupplyStatusList.add(new SASupplyStatus("DE", Utilities.roundFirstToString(tagDataVO.getTagVal()), tagDataVO.getUnit()));

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("CD_DAMPER_OPEN_" + constCorner).get(null));
				saSupplyStatusList.add(new SASupplyStatus("CD", Utilities.roundFirstToString(tagDataVO.getTagVal()), tagDataVO.getUnit()));

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("BC_DAMPER_OPEN_" + constCorner).get(null));
				saSupplyStatusList.add(new SASupplyStatus("BC", Utilities.roundFirstToString(tagDataVO.getTagVal()), tagDataVO.getUnit()));

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("AB_DAMPER_OPEN_" + constCorner).get(null));
				saSupplyStatusList.add(new SASupplyStatus("AB", Utilities.roundFirstToString(tagDataVO.getTagVal()), tagDataVO.getUnit()));

				tagDataVO = opDataTagMap.get(CommonConst.class.getField("AA_DAMPER_OPEN_" + constCorner).get(null));
				saSupplyStatusList.add(new SASupplyStatus("AA", Utilities.roundFirstToString(tagDataVO.getTagVal()), tagDataVO.getUnit()));
				combustionStatusPosition.setSaSupplyStatusList(saSupplyStatusList);

				combustionStatusPositionList.add(combustionStatusPosition);
			}
			
			combustionStatus.setPositionList(combustionStatusPositionList);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
		// ## RH Spray Supply.
		ArrayList<RHSpraySupplyStatus> rhSpraySupplyStatusList = new ArrayList<RHSpraySupplyStatus>(); 

		TagDataVO rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_FN_OL_STM_TEMP_RIGHT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("Final RH Right Temp", "final_rh_right_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICRO_DSH_OL_STM_TEMP_LEFT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 2nd Spray Outlet Temp", "rh_left_2nd_spray_outlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICRO_DSH_OL_STM_TEMP_RIGHT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 2nd Spray Outlet Temp", "rh_right_2nd_spray_outlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICRO_DSH_IL_STM_TEMP_RIGHT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 2nd Spray Inlet Temp", "rh_right_2nd_spray_inlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_RIGHT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 1st Spray Outlet Temp", "rh_right_1st_spray_outlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.HP_CYLYNDER_EXHED_STM_TEMP); 
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 1st Spray Inlet Temp", "rh_right_1st_spray_inlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 1st Spray Inlet Temp", "rh_left_1st_spray_inlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_R_3); // RH Spray Flow - 1st stg R
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 1st Spray Flow", "rh_right_1st_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_L_3); // RH Spray Flow - 1st stg L
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 1st Spray Flow", "rh_left_1st_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_LEFT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 1st Spray Outlet Temp", "rh_left_1st_spray_outlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_SPRAY_WTR_FLOW); // RH Spray Flow - Total Flow
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("Total RH Spray Flow", "total_rh_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));		
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_FLOW_R); // RH Spray Flow 2st stg R
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 2nd Spray Flow", "rh_right_2nd_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICRO_DSH_IL_STM_TEMP_LEFT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 2nd Spray Inlet Temp", "rh_left_2nd_spray_inlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_FLOW_L); // RH Spray Flow 2st stg L
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 2nd Spray Flow", "rh_left_2nd_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_FN_OL_STM_TEMP_LEFT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("Final RH Left Temp", "final_rh_left_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		combustionStatus.setRhSpraySupplyList(rhSpraySupplyStatusList);
		
		// ## Fire ball Position. - Sasan Plant 는 FireBall이 없음. - dummy
		ArrayList<Position> fireballPostionList = new ArrayList<Position>();
		
		/* Dummy Random Fire Ball Data
		Random rndLoop = new Random();		
		int loop = rndLoop.nextInt(10);
		
		for (int i = 0; i < loop; i++) {
			fireballPostionList.add(new Position(Utilities.round(Utilities.getDoubleRandom(0, 10), 1), Utilities.round(Utilities.getDoubleRandom(0, 10), 1)));
		}*/
		
		combustionStatus.setFireballPosition(fireballPostionList);

		return new CombustionStatus(combustionStatus);
	}
	
	public CombustionStatusT getCombustionStatusT(String plantUnitId, String ofaOption, String coalOption) throws InvalidParameterException {
		
		if (CommonConst.StringEmpty.equals(plantUnitId)) {
			throw new InvalidParameterException("The plantUnitId parameter is missing.");
		}
		
		HashMap<String, TagDataVO> opDataTagMap = this.commonDataService.getOpDataTagMap();
		HashMap<String, ConfDataVO> coalDataMap = this.commonDataService.getConfDataMap(plantUnitId, CommonConst.CONFING_SETTING_TYPE_COAL);
		TagDataVO tagDataVO = null;
		
		CombustionStatusT combustionStatus = new CombustionStatusT();
		
		// ## Coal Property.
		CoalProperty coalProperty = new CoalProperty();
		
		// Coal Property - Chemical composition.
		ArrayList<ItemStatus> chemicalCompositionList = new ArrayList<ItemStatus>();
		chemicalCompositionList.add(new ItemStatus("C", coalDataMap.get(CommonConst.C).getUnit(), Utilities.roundIntToString(coalDataMap.get(CommonConst.C).getConfVal())));
		chemicalCompositionList.add(new ItemStatus("H", coalDataMap.get(CommonConst.H).getUnit(), Utilities.roundFirstToString(coalDataMap.get(CommonConst.H).getConfVal())));
		chemicalCompositionList.add(new ItemStatus("N", coalDataMap.get(CommonConst.N).getUnit(), Utilities.roundFirstToString(coalDataMap.get(CommonConst.N).getConfVal())));
		chemicalCompositionList.add(new ItemStatus("S", coalDataMap.get(CommonConst.S).getUnit(), Utilities.roundFirstToString(coalDataMap.get(CommonConst.S).getConfVal())));
		coalProperty.setChemicalCompositionList(chemicalCompositionList);
		
		// Coal Property - Humidity, Calories, Ash.
		CoalInfo coalInfo = new CoalInfo();
		coalInfo.setMoisture(Utilities.roundIntToString(coalDataMap.get(CommonConst.TOTAL_MOISTURE).getConfVal()));
		coalInfo.setMoistureUnit(coalDataMap.get(CommonConst.TOTAL_MOISTURE).getUnit());
		coalInfo.setGcv(Utilities.roundIntToString(coalDataMap.get(CommonConst.GROSS_CALORIFIC_VALUE).getConfVal()));
		coalInfo.setGcvUnit(coalDataMap.get(CommonConst.GROSS_CALORIFIC_VALUE).getUnit());
		coalInfo.setAsh(Utilities.roundIntToString(coalDataMap.get(CommonConst.ASH_CONTENTS).getConfVal()));
		coalInfo.setAshUnit(coalDataMap.get(CommonConst.ASH_CONTENTS).getUnit());
		coalProperty.setCoalInfo(coalInfo);
		
		combustionStatus.setCoalProperty(coalProperty);

		// ## Position.
		
		// ofa=2_front_rear
						
		// Position - OFA Supply Status.		
		CombustionStatusPositionT combustionStatusPosition = new CombustionStatusPositionT();				
		List<CombustionStatusPositionOFA> combustionStatusPositionOFAList = new ArrayList<CombustionStatusPositionOFA>();
		
		String[] ofaItems = new String[] {"Front", "Rear", "Left", "Right", "CN1", "CN2", "CN3", "CN4"};				
	//	String ofaOption = "2_front_rear";
		int ofaDepth = -1;
		
		if(ofaOption != null && !"".equals(ofaOption))
		{
			String[] ofaOptions = ofaOption.split("_");			
			ofaDepth = Integer.parseInt(ofaOptions[0]);
			
			ofaItems = new String[ofaOptions.length - 1];
			
			for (int i = 0; i < ofaItems.length; i++) {		
				ofaItems[i] = ofaOptions[i+1];
			}			
		}
		
		for (int i = 0; i < ofaItems.length; i++) {		
			
			CombustionStatusPositionOFA combustionStatusPositionOFA = new CombustionStatusPositionOFA();
			combustionStatusPositionOFA.setLocation(ofaItems[i]);
			
			List<CombustionStatusPositionOFASupply> combustionStatusPositionOFASupplyList = new ArrayList<CombustionStatusPositionOFASupply>();	
			
			String[] ofaDepthItems = new String[] { ofaItems[i] + "-A", ofaItems[i] + "-B"};
			if(ofaDepth != -1)
			{
				ofaDepthItems = new String[ofaDepth];
				for (int j = 0; j <ofaDepth; j++) {		
					ofaDepthItems[j] = ofaItems[i] + "-" + Character.toString ((char) (65 + j));;
				}	
			}
			
			
			for (int j = 0; j < ofaDepthItems.length; j++) {
				
				CombustionStatusPositionOFASupply combustionStatusPositionOFASupply = new CombustionStatusPositionOFASupply();
				combustionStatusPositionOFASupply.setItem(ofaDepthItems[j]);
				
				// OFA Front Supply
				List<OFASupplyStatus> ofaSupplyStatusList = new ArrayList<OFASupplyStatus>();

				OFASupplyStatus ofaSupplyStatus1 = new OFASupplyStatus();
				ofaSupplyStatus1.setItem("UOFA1");
				ofaSupplyStatus1.setDamperOpenCurrnet("77.3");
				ofaSupplyStatus1.setDamperUnit("%");
				ofaSupplyStatus1.setYawCurrent(Utilities.roundFirstToString(-15));
				ofaSupplyStatus1.setYawUnit("˚");
				ofaSupplyStatusList.add(ofaSupplyStatus1);

				OFASupplyStatus ofaSupplyStatus2 = new OFASupplyStatus();
				ofaSupplyStatus2.setItem("UOFA2");
				ofaSupplyStatus2.setDamperOpenCurrnet("86.0");
				ofaSupplyStatus2.setDamperUnit("%");
				ofaSupplyStatus2.setYawCurrent(Utilities.roundFirstToString(-15));
				ofaSupplyStatus2.setYawUnit("˚");
				ofaSupplyStatusList.add(ofaSupplyStatus2);
				
				OFASupplyStatus ofaSupplyStatus3 = new OFASupplyStatus();
				ofaSupplyStatus3.setItem("UOFA3");
				ofaSupplyStatus3.setDamperOpenCurrnet("99.0");
				ofaSupplyStatus3.setDamperUnit("%");
				ofaSupplyStatus3.setYawCurrent(Utilities.roundFirstToString(-15));
				ofaSupplyStatus3.setYawUnit("˚");
				ofaSupplyStatusList.add(ofaSupplyStatus3);

				combustionStatusPositionOFASupply.setOfaSupplyStatusList(ofaSupplyStatusList);			
				combustionStatusPositionOFASupplyList.add(combustionStatusPositionOFASupply);
			}
			
			combustionStatusPositionOFA.setOfaSupplyList(combustionStatusPositionOFASupplyList);
			combustionStatusPositionOFAList.add(combustionStatusPositionOFA);
		}		
		combustionStatusPosition.setCombustionStatusPositionOFAList(combustionStatusPositionOFAList);
		
		// Position - Coal Supply Status.
		List<CombustionStatusPositionCoal> combustionStatusPositionCoalList = new ArrayList<CombustionStatusPositionCoal>();		
		String[] coaltems = new String[] { "Front", "Rear", "Left", "Right", "CN1", "CN2", "CN3", "CN4" };
		
		//String coalOption = "2_front_rear";
		int coalDepth = -1;
		
		if(coalOption != null && !"".equals(coalOption))
		{
			String[] coalOptions = coalOption.split("_");			
			coalDepth = Integer.parseInt(coalOptions[0]);
			
			coaltems = new String[coalOptions.length - 1];
			
			for (int i = 0; i < coaltems.length; i++) {		
				coaltems[i] = coalOptions[i+1];
			}			
		}
		
		
		for (int i = 0; i < coaltems.length; i++) {	
		
			CombustionStatusPositionCoal combustionStatusPositionCoal = new CombustionStatusPositionCoal();
			combustionStatusPositionCoal.setLocation(coaltems[i]);
			
			List<CombustionStatusPositionCoalSupply> combustionStatusPositionCoalSupplyList = new ArrayList<CombustionStatusPositionCoalSupply>();		
			
			String[] coalPaSaItems = new String[] { coaltems[i] + "-A", coaltems[i] + "-B", coaltems[i] + "-C", coaltems[i] + "-D" };
			if(coalDepth != -1)
			{
				coalPaSaItems = new String[coalDepth];
				for (int j = 0; j <coalDepth; j++) {		
					coalPaSaItems[j] = coaltems[i] + "-" + Character.toString ((char) (65 + j));;
				}	
			}
			
			for (int j = 0; j < coalPaSaItems.length; j++) {
				
				CombustionStatusPositionCoalSupply combustionStatusPositionCoalSupply = new CombustionStatusPositionCoalSupply();
				combustionStatusPositionCoalSupply.setItem(coalPaSaItems[j]);
				
				// OFA Front Supply
				List<ItemStatus> coalPaSaSupplyStatusList = new ArrayList<ItemStatus>();

				ItemStatus coalPaSaSupplyStatusPaFlow = new ItemStatus();
				coalPaSaSupplyStatusPaFlow.setItem("PA Flow");
				coalPaSaSupplyStatusPaFlow.setCurrent("71");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusPaFlow);
				
				ItemStatus coalPaSaSupplyStatusPaInTemp = new ItemStatus();
				coalPaSaSupplyStatusPaInTemp.setItem("PA In Temp");
				coalPaSaSupplyStatusPaInTemp.setCurrent("72");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusPaInTemp);
				
				ItemStatus coalPaSaSupplyStatusPaOutTemp = new ItemStatus();
				coalPaSaSupplyStatusPaOutTemp.setItem("PA Out Temp");
				coalPaSaSupplyStatusPaOutTemp.setCurrent("73");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusPaOutTemp);
				
				ItemStatus coalPaSaSupplyStatusCoalFpr= new ItemStatus();
				coalPaSaSupplyStatusCoalFpr.setItem("Coal FPR");
				coalPaSaSupplyStatusCoalFpr.setCurrent("74");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusCoalFpr);
				
				ItemStatus coalPaSaSupplyStatusCla = new ItemStatus();
				coalPaSaSupplyStatusCla.setItem("CLA");
				coalPaSaSupplyStatusCla.setCurrent("75");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusCla);
				
				ItemStatus coalPaSaSupplyStatusHpu = new ItemStatus();
				coalPaSaSupplyStatusHpu.setItem("HPU");
				coalPaSaSupplyStatusHpu.setCurrent("76");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusHpu);
				
				ItemStatus coalPaSaSupplyStatusHotDmp2 = new ItemStatus();
				coalPaSaSupplyStatusHotDmp2.setItem("Hot DMP 2");
				coalPaSaSupplyStatusHotDmp2.setCurrent("77");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusHotDmp2);
				
				ItemStatus coalPaSaSupplyStatusColdDmp2 = new ItemStatus();
				coalPaSaSupplyStatusColdDmp2.setItem("Cold DMP 2");
				coalPaSaSupplyStatusColdDmp2.setCurrent("78");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusColdDmp2);
				
				ItemStatus coalPaSaSupplyStatusSaFlow = new ItemStatus();
				coalPaSaSupplyStatusSaFlow.setItem("SA Flow");
				coalPaSaSupplyStatusSaFlow.setCurrent("79");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusSaFlow);
				
				ItemStatus coalPaSaSupplyStatusSadL2 = new ItemStatus();
				coalPaSaSupplyStatusSadL2.setItem("SAD L2");
				coalPaSaSupplyStatusSadL2.setCurrent("80");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusSadL2);
				
				ItemStatus coalPaSaSupplyStatusSadRe = new ItemStatus();
				coalPaSaSupplyStatusSadRe.setItem("SAD RE");
				coalPaSaSupplyStatusSadRe.setCurrent("81");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusSadRe);
				
				ItemStatus coalPaSaSupplyStatusMillCurrent = new ItemStatus();
				coalPaSaSupplyStatusMillCurrent.setItem("Mill current");
				coalPaSaSupplyStatusMillCurrent.setCurrent("82");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusMillCurrent);
				
				ItemStatus coalPaSaSupplyStatusBowlDt = new ItemStatus();
				coalPaSaSupplyStatusBowlDt.setItem("Bowl Dt");
				coalPaSaSupplyStatusBowlDt.setCurrent("83");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusBowlDt);
				
				ItemStatus coalPaSaSupplyStatusTemp = new ItemStatus();
				coalPaSaSupplyStatusTemp.setItem("PA Flow");
				coalPaSaSupplyStatusTemp.setCurrent("84");
				coalPaSaSupplyStatusList.add(coalPaSaSupplyStatusTemp);	
				
				combustionStatusPositionCoalSupply.setCoalPaSaSupplyStatusList(coalPaSaSupplyStatusList);				
				combustionStatusPositionCoalSupplyList.add(combustionStatusPositionCoalSupply);				
			}
			
			combustionStatusPositionCoal.setCoalSupplyList(combustionStatusPositionCoalSupplyList);
			combustionStatusPositionCoalList.add(combustionStatusPositionCoal);			
		}		
		
		combustionStatusPosition.setCombustionStatusPositionCoalList(combustionStatusPositionCoalList);
		
		combustionStatus.setCombustionStatusPosition(combustionStatusPosition);
	
		// ## RH Spray Supply.
		ArrayList<RHSpraySupplyStatus> rhSpraySupplyStatusList = new ArrayList<RHSpraySupplyStatus>(); 

		TagDataVO rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_FN_OL_STM_TEMP_RIGHT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("Final RH Right Temp", "final_rh_right_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICRO_DSH_OL_STM_TEMP_LEFT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 2nd Spray Outlet Temp", "rh_left_2nd_spray_outlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICRO_DSH_OL_STM_TEMP_RIGHT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 2nd Spray Outlet Temp", "rh_right_2nd_spray_outlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICRO_DSH_IL_STM_TEMP_RIGHT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 2nd Spray Inlet Temp", "rh_right_2nd_spray_inlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_RIGHT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 1st Spray Outlet Temp", "rh_right_1st_spray_outlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.HP_CYLYNDER_EXHED_STM_TEMP); 
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 1st Spray Inlet Temp", "rh_right_1st_spray_inlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 1st Spray Inlet Temp", "rh_left_1st_spray_inlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_R_3); // RH Spray Flow - 1st stg R
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 1st Spray Flow", "rh_right_1st_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_L_3); // RH Spray Flow - 1st stg L
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 1st Spray Flow", "rh_left_1st_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.LT_RH_IN_HDR_IL_STM_TEMP_LEFT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 1st Spray Outlet Temp", "rh_left_1st_spray_outlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_SPRAY_WTR_FLOW); // RH Spray Flow - Total Flow
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("Total RH Spray Flow", "total_rh_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));		
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_FLOW_R); // RH Spray Flow 2st stg R
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Right 2nd Spray Flow", "rh_right_2nd_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICRO_DSH_IL_STM_TEMP_LEFT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 2nd Spray Inlet Temp", "rh_left_2nd_spray_inlet_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_MICROFLOW_DSH_SPRAY_FLOW_L); // RH Spray Flow 2st stg L
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("RH Left 2nd Spray Flow", "rh_left_2nd_spray_flow", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		rhSprayTagDataVO = opDataTagMap.get(CommonConst.RH_FN_OL_STM_TEMP_LEFT);
		rhSpraySupplyStatusList.add(new RHSpraySupplyStatus("Final RH Left Temp", "final_rh_left_temp", Utilities.roundIntToString(rhSprayTagDataVO.getTagVal()), rhSprayTagDataVO.getUnit()));
		
		combustionStatus.setRhSpraySupplyList(rhSpraySupplyStatusList);
		
		return new CombustionStatusT(combustionStatus);
	}

	public CombustionDynamics getCombustionDynamics(String plantUnitId) throws InvalidParameterException {
		if (CommonConst.StringEmpty.equals(plantUnitId)) {
			throw new InvalidParameterException("The plantUnitId parameter is missing.");
		}

		//HashMap<String, Double> opDataMap = this.commonDataService.getOpDataMap();
		
		String[] partArray = new String[] { "O2", "NOx", "CO", "Temp", "FOT"};
		int[] partSizeArray = new int[] { 5, 5, 5, 5, 7};

		CombustionDynamics combustionDynamics = new CombustionDynamics();
		ArrayList<BoilerPart> boilerPartList = new ArrayList<BoilerPart>();
		Random generatorValue = new Random();
		Random generatorValueFloat = new Random();

		for (int k = 0; k < partArray.length; k++) {
			String part = partArray[k];
			int partSize = partSizeArray[k];
			Number[] x = new Number[partSize];
			Number[] y = new Number[partSize];
			Number[][] value = new Number[x.length][y.length];
			
			for(int i = 0 ; i < partSize; i++) {
				x[i] = i;
				y[i] = i;
			}

			/*x = Utilities.getRandomNumbers(partSize);
			y = Utilities.getRandomNumbers(partSize);*/
			
			if(part.equals("FOT")) {
				
				/*
				 * Fireball Centering 관련 
				 * 
				 */
				// int posX = (int)((double)opDataMap.get(CommonConst.CD_CONTOUR_X_POS));
				// int posY = (int)((double)opDataMap.get(CommonConst.CD_CONTOUR_Y_POS));
				
				int posX = 1;
				int posY = 1;

				String highTempdata = 100 + "." + generatorValueFloat.nextInt(9);
				String midTempdata = 70 + "." + generatorValueFloat.nextInt(9);
				String lowTempdata = 50 + "." + generatorValueFloat.nextInt(9);
				String otherTempdata = 30 + "." + generatorValueFloat.nextInt(9);
				
				for (int i = 0; i < x.length; i++) {
					for (int j = 0; j < y.length; j++) {
						
						int midX = partSize / 2 + posX;
						int midY = partSize / 2 + posY;
						
						if(posX >= 2 || posY <= -2) {
							if(i == midX) { 
								if(j == midY + 1) {
									value[i][j] = Double.parseDouble(midTempdata);
								} else if(j== midY + 2) {
									value[i][j] = Double.parseDouble(lowTempdata);
								} else if(j == midY) {
									value[i][j] = Double.parseDouble(highTempdata);
								} else {
									value[i][j] = Double.parseDouble(otherTempdata);
								}
							} else if(i == midX - 1) {
								if(j == midY) {
									value[i][j] = Double.parseDouble(midTempdata);
								} else {
									value[i][j] = Double.parseDouble(otherTempdata);
								}
							} else {
								value[i][j] = Double.parseDouble(otherTempdata);
							}
						} else if(posX == 1 || posY == -1) { 
							if(i == midX) { 
								if(j == midY - 1 || j == midY + 1) {
									value[i][j] = Double.parseDouble(midTempdata);
								} else if(j== midY + 2) {
									value[i][j] = Double.parseDouble(lowTempdata);
								} else if(j == midY) {
									value[i][j] = Double.parseDouble(highTempdata);
								} else {
									value[i][j] = Double.parseDouble(otherTempdata);
								}
							} else if(i == midX - 1 || i == midX + 1) {
								if(j == midY) {
									value[i][j] = Double.parseDouble(midTempdata);
								} else {
									value[i][j] = Double.parseDouble(otherTempdata);
								}
							} else {
								value[i][j] = Double.parseDouble(otherTempdata);
							}
						} else {
							if(i == midX) { 
								if(j == midY - 1 || j == midY + 1) {
									value[i][j] = Double.parseDouble(midTempdata);
								} else if(j == midY) {
									value[i][j] = Double.parseDouble(highTempdata);
								} else {
									value[i][j] = Double.parseDouble(otherTempdata);
								}
							} else if(i == midX - 1 || i == midX + 1) {
								if(j == midY) {
									value[i][j] = Double.parseDouble(midTempdata);
								} else {
									value[i][j] = Double.parseDouble(otherTempdata);
								}
							} else {
								value[i][j] = Double.parseDouble(otherTempdata);
							}
						}
					}
				
				}
			}else {
				for (int i = 0; i < x.length; i++) {
					for (int j = 0; j < y.length; j++) {
						String data = generatorValue.nextInt(100) + "." + generatorValueFloat.nextInt(999);
						value[i][j] = Double.parseDouble(data);
					}
				
				}
			}
			
			Random rnd = new Random();

			ContourData contourData = new ContourData(x, y, value);
			contourData.setMessage(part + "는 현재 정상 상태 입니다.");
			contourData.setFotRate(String.valueOf(rnd.nextInt(100)));
			contourData.setMeanValue(String.valueOf(rnd.nextInt(1000)));
			contourData.setMaxValue(String.valueOf(rnd.nextInt(1000)));
			contourData.setMinValue(String.valueOf(rnd.nextInt(1000)));
			contourData.setLeftMean(String.valueOf(rnd.nextInt(1000)));
			contourData.setRightMean(String.valueOf(rnd.nextInt(1000)));
			contourData.setStandardDeviation("50");
			
			BoilerPart boilerPart = new BoilerPart(part, contourData);
			boilerPartList.add(boilerPart);
		}

		combustionDynamics.setBoilerPartList(boilerPartList);

		return combustionDynamics;
	}
	
	public AlarmAndEvent getAlarmAndEvent(String startDateString, String endDateString, int pageNo, int pageSize) throws InvalidParameterException {
		
		AlarmAndEvent alarmAndEvent = new AlarmAndEvent();
		
		try {
			int startIndex = pageNo * pageSize;
			Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateString);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateString);
		
			ArrayList<AlarmAndEvent> alarmAndEventList = new ArrayList<AlarmAndEvent>();
			List<NoticeHistoryEntity> noticeHistoryEntityList = this.noticeHistoryRepository.findByTimestampRange(startDate, endDate, startIndex, pageSize);
			
			for (NoticeHistoryEntity noticeHistoryEntity : noticeHistoryEntityList) {
				AlarmAndEvent alarmAndEventNew = new AlarmAndEvent();
				alarmAndEventNew.setDatetime(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(noticeHistoryEntity.getTimestamp()));
				alarmAndEventNew.setType(noticeHistoryEntity.getType());
				alarmAndEventNew.setItem(noticeHistoryEntity.getName());
				alarmAndEventNew.setStatus(noticeHistoryEntity.getStatus());
				alarmAndEventNew.setDescription(noticeHistoryEntity.getDescription());
				alarmAndEventList.add(alarmAndEventNew);
			}
			
			alarmAndEvent.setAlarmAndEventList(alarmAndEventList);
		
		} catch (ParseException e) {
			throw new InvalidParameterException(e.getMessage());
		}

		return alarmAndEvent;
	}
	
	public List<ItemStatus> getSystemInformation() {
		
		List<ItemStatus> itemStatusList = new ArrayList<ItemStatus>();
		
		SystemCheckEntity systemCheckEntity = this.systemCheckRepository.findControl();
		itemStatusList.add(new ItemStatus("memory", systemCheckEntity.getUnit(), systemCheckEntity.getMemoryUsage()));
		itemStatusList.add(new ItemStatus("cpu", systemCheckEntity.getUnit(), systemCheckEntity.getCpuUsage()));
		itemStatusList.add(new ItemStatus("disk", systemCheckEntity.getUnit(), systemCheckEntity.getDiskUsage()));
		itemStatusList.add(new ItemStatus("disk_remained_amount", systemCheckEntity.getRadUnit(), systemCheckEntity.getRemainedDiskAmount()));
		
		return itemStatusList;
	}
	
	private void setWorkSheet(int rowIndex, int cellIndex, Number value, XSSFSheet worksheet) {
		XSSFRow row = worksheet.getRow(rowIndex);
		row.getCell(cellIndex).setCellValue(value.doubleValue());
	}
	
	private void setWorkSheetString(int rowIndex, int cellIndex, String value, XSSFSheet worksheet) {
		XSSFRow row = worksheet.getRow(rowIndex);
		row.getCell(cellIndex).setCellValue(value);
	}
}