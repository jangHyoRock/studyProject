package dhi.optimizer.schedule.executor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;
import dhi.optimizer.algorithm.common.PerformanceCalculation;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.enumeration.ControllerModeStatus;
import dhi.optimizer.enumeration.RHSprayLossType;
import dhi.optimizer.model.ConfDataVO;
import dhi.optimizer.model.TagDataVO;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.repository.CtrDataOutputRepository;
import dhi.optimizer.repository.EffectCalculationRepository;
import dhi.optimizer.service.CommonDataService;

/**
 * Performance & Efficiency Cost Saving Scheduler. <br>
 * : Performance & Efficiency Cost Saving.
 */
@Service
public class EffectCumulationBatchExecutor extends ScheduleExecutor {

	private static final Logger logger = LoggerFactory.getLogger(EffectCumulationBatchExecutor.class.getSimpleName());

	private static final String REPORT_COLLECTION_PROCEDURE = "SP_REPORT_DATA_COLLECTION";
	
	//private static Date PreventDuplication_CostSavingTimestamp = new Date();
		
	@Autowired
	private EffectCalculationRepository effectCalculationRepository;
	
	@Autowired
	private ControlRepository controlRepository;
	
	@Autowired
	private CtrDataOutputRepository ctrDataOutputRepository;

	@Autowired
	private CommonDataService commonDataService;
	
	@Autowired
	private PerformanceCalculation performanceCalculation;
	
	public EffectCumulationBatchExecutor() {
	};

	public EffectCumulationBatchExecutor(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);
	}
	
	/**
	 *  EffectCumulationBatchExecutor 시작함수.
	 */
	@Override
	public void start() {
		logger.info("### " + EffectCumulationBatchExecutor.class.getName() + " Start ###");
		try {
			this.effectCumulationProcess();
		} catch (Exception e) {
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			logger.info("### " + EffectCumulationBatchExecutor.class.getName() + " End ###");
		}
	}
	
	/**
	 * EffectCumulation 수행함수.
	 */
	private void effectCumulationProcess() {
		HashMap<String, HashMap<String, ConfDataVO>> refConfTypesMap = new HashMap<String, HashMap<String, ConfDataVO>>();
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_BASELINE, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_COAL, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_EFFICIENCY, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_KPI, new HashMap<String, ConfDataVO>());
		String plantUnitId = effectCalculationRepository.findPlantUnitIdNativeQuery();
		this.commonDataService.refConfDataMap(plantUnitId, refConfTypesMap);
		
		HashMap<String, TagDataVO> opDataMap = this.commonDataService.getOpDataTagMap();
		HashMap<String, ConfDataVO> baselineDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_BASELINE);
		HashMap<String, ConfDataVO> kpiDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_KPI);
		performanceCalculation.init(opDataMap, refConfTypesMap);
		
		double unitHeatRateBenefitBySolution = (baselineDataMap.get(CommonConst.BLR_DRY_GAS_LOSS).getConfVal() - performanceCalculation.getDryGasLoss()) * performanceCalculation.getUnitHeatRate() / performanceCalculation.getBoilerEfficiency() + (baselineDataMap.get(CommonConst.RH_SPRAY_LOSS).getConfVal() - performanceCalculation.getRHSprayLoss(RHSprayLossType.KCAL_KWH)) * 100 / performanceCalculation.getBoilerEfficiency();
		double costSaving = unitHeatRateBenefitBySolution * opDataMap.get(CommonConst.TOTAL_COAL_FLOW).getTagVal() / baselineDataMap.get(CommonConst.UNIT_HEAT_RATE).getConfVal();
		Date costSavingTimestamp = opDataMap.get(CommonConst.TOTAL_COAL_FLOW).getTimeStamp();
		
		double auxPowerSaving = baselineDataMap.get(CommonConst.TOTAL_AUX_POWER).getConfVal() - performanceCalculation.getTotalAuxPower();
		double ammoniaCostSaving = performanceCalculation.getAmmoniaSavingBenefit();
		double electricityPricing = kpiDataMap.get(CommonConst.ELECTRICITY_PRICE).getConfVal();
		double fuelPricing = kpiDataMap.get(CommonConst.FUEL_PRICE).getConfVal();
		double auxPowerSavingBenefit = auxPowerSaving * 1000 * electricityPricing;
		double totalSavingBenefit = costSaving * fuelPricing + ammoniaCostSaving + auxPowerSavingBenefit;
		
		ControlEntity controlEntity = this.controlRepository.findControl();

		boolean controllerRunningCondition = false;
		ControllerModeStatus controllerMode = (ControllerModeStatus) ControllerModeStatus.valueOf(controlEntity.getControlMode());
		if (ControllerModeStatus.CL == controllerMode) {
			controllerRunningCondition = true;
		}
		
		boolean outputControllerHoldCondition = false;
		outputControllerHoldCondition = effectCalculationRepository.findAllHoldYnNativeQuery().equals("1") ? true : false;
		
		String maxTimestampStr = effectCalculationRepository.findMaxTimestampNativeQuery();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		String costSavingTimestampStr = formatter.format(costSavingTimestamp);
		// When "control mode" is running and "output controller" is not hold condition, add performance effect   
		// To prevent duplication when a new OpData is run again before it is created.
		if((!costSavingTimestampStr.equals(maxTimestampStr) 
				&& controllerRunningCondition 
				&& !outputControllerHoldCondition)) {
			effectCalculationRepository.insertAll(costSavingTimestamp, Utilities.valueOfDoubleRange(costSaving, -9.99, 99.99), Utilities.valueOfDoubleRange(totalSavingBenefit, -99.9, 999.9), true);
		}
		//PreventDuplication_CostSavingTimestamp = costSavingTimestamp;
		
		// DCS에 Control Data 전달. (CURRENT_BOILER_EFFICIENCY, CURRENT_UNIT_HEAT_RATE);
		this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_CURRENT_BOILER_EFFICIENCY, Math.round(performanceCalculation.getBoilerEfficiency() * 1000000) / 1000000.0);
		this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_CURRENT_UNIT_HEAT_RATE, Math.round(performanceCalculation.getUnitHeatRate() * 1000000 / 1000000.0));
				
		// Common ConfigTable에서 Coal Supply 값을  가져온 후, Optimizer ReportTemp 테이블에 등록한다.
		HashMap<String, ConfDataVO> coalConfDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_COAL);
		coalConfDataMap.get(CommonConst.TOTAL_MOISTURE).getConfVal();
		coalConfDataMap.get(CommonConst.ASH_CONTENTS).getConfVal();
		coalConfDataMap.get(CommonConst.VOLATILE_MATTER).getConfVal();
		coalConfDataMap.get(CommonConst.FIXED_CARBON).getConfVal();
		coalConfDataMap.get(CommonConst.GROSS_CALORIFIC_VALUE).getConfVal();
		
		// Report Data Collection Procedure Call. 
		EntityManager em = this.emFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {			
			logger.info("### " + REPORT_COLLECTION_PROCEDURE + " Procedure Start ###");
			tx.begin();
			StoredProcedureQuery query = em.createStoredProcedureQuery(REPORT_COLLECTION_PROCEDURE);
			query.registerStoredProcedureParameter("p_blr_efficiency", double.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("p_unit_heatrate", double.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("p_total_moistrue", double.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("p_ash_contents", double.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("p_volatile_matter", double.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("p_fixed_carbone", double.class, ParameterMode.IN);
			query.registerStoredProcedureParameter("p_gross_calorific_value", double.class, ParameterMode.IN);			
			query.setParameter("p_blr_efficiency", performanceCalculation.getBoilerEfficiency());
			query.setParameter("p_unit_heatrate", performanceCalculation.getUnitHeatRate());
			query.setParameter("p_total_moistrue", coalConfDataMap.get(CommonConst.TOTAL_MOISTURE).getConfVal());
			query.setParameter("p_ash_contents", coalConfDataMap.get(CommonConst.ASH_CONTENTS).getConfVal());
			query.setParameter("p_volatile_matter", coalConfDataMap.get(CommonConst.VOLATILE_MATTER).getConfVal());
			query.setParameter("p_fixed_carbone", coalConfDataMap.get(CommonConst.FIXED_CARBON).getConfVal());
			query.setParameter("p_gross_calorific_value", coalConfDataMap.get(CommonConst.GROSS_CALORIFIC_VALUE).getConfVal());
			query.execute();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			em.close();
			logger.info("### " + REPORT_COLLECTION_PROCEDURE + " Procedure End ###");
		}
	}
}
