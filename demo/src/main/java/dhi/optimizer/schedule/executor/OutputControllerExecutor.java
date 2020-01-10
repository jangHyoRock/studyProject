package dhi.optimizer.schedule.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;
import dhi.optimizer.algorithm.pso.PsoMV.MVType;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.enumeration.ControllerModeStatus;
import dhi.optimizer.enumeration.CtrDataIOType;
import dhi.optimizer.enumeration.PsoOptimizationFunctionModeStatus;
import dhi.optimizer.model.MVTagDataVO;
import dhi.optimizer.model.PsoMVInfoVO;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.model.db.OutputBiasConfEntity;
import dhi.optimizer.model.db.OutputDataConfEntity;
import dhi.optimizer.model.db.OutputHoldEntity;
import dhi.optimizer.model.db.OutputMVTGTEntity;
import dhi.optimizer.model.db.PSOResultEntity;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.repository.CtrDataOutputRepository;
import dhi.optimizer.repository.OPDataInputRepository;
import dhi.optimizer.repository.OutputBiasConfRepository;
import dhi.optimizer.repository.OutputDataConfRepository;
import dhi.optimizer.repository.OutputHoldRepository;
import dhi.optimizer.repository.OutputMVTGTRepository;
import dhi.optimizer.repository.PSOMVInfoRepository;
import dhi.optimizer.repository.PSOResultRepository;
import dhi.optimizer.service.AlgorithmService;
import dhi.optimizer.service.CommonDataService;

/**
 * Output Controller Scheduler. <br>
 * : 10초 주기로  DCS에게 보낼  MV별 Bias값을 생성 한 후 CtrOutput 테이블에 등록함.  
 */
@Service
public class OutputControllerExecutor extends ScheduleExecutor {
 
	private static final Logger logger = LoggerFactory.getLogger(OutputControllerExecutor.class.getSimpleName());
	private static final double PER_MINUTE_CYCLE = 6;
	
	@Autowired
	private OutputHoldRepository outputHoldRepository;
	
	@Autowired
	private ControlRepository controlRepository;
	
	@Autowired
	private PSOResultRepository psoResultRepository;
	
	@Autowired
	private OutputDataConfRepository outputDataConfRepository;
	
	@Autowired
	private OutputBiasConfRepository outputBiasConfRepository;	
	
	@Autowired
	private OPDataInputRepository opDataInputRepository;
		
	@Autowired
	private CtrDataOutputRepository ctrDataOutputRepository;
	
	@Autowired
	private PSOMVInfoRepository psoMVInfoRepository;
	
	@Autowired
	private OutputMVTGTRepository outputMVTGTRepository;
	
	@Autowired
	private AlgorithmService algorithmService;
	
	@Autowired
	public CommonDataService commonDataService;
	
	public OutputControllerExecutor() {}
	
	public OutputControllerExecutor(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);
	}
	
	/**
	 * OutputControllerExecutor 시작함수.
	 */
	@Override
	public void start() {
		logger.info("### " + OutputControllerExecutor.class.getName() + " Start ###");
		try {
			this.outputDataControlProcess();
		} catch (Exception e) {
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			this.algorithmService.updateOutputControllerOpenLoopZeroBias();
			logger.info("### " + OutputControllerExecutor.class.getName() + " End ###");
		}
	}
	
	/**
	 * Output MV Bias Make Process Function.
	 */
	private void outputDataControlProcess() {
		PSOResultEntity psoResultEntity = this.psoResultRepository.findTop1ByOrderByTimestampDesc();
		if(psoResultEntity == null){
			logger.info("# 'OutputController' stopped running. \n Reason: No PSO result.");
			return;
		}
		
		// Control Data Output Bias Initialize 값이 1(초기화상태)이면, 초기화 상태 이전의 PSO값인 경우 실행을 종료한다.
		List<Object[]> objectBiasInitializeList = this.ctrDataOutputRepository.getCtrDataOutput(CommonConst.CTR_DATA_OUTPUT_BIAS_INITIALIZE);
		Object[] objectBiasInitialize = objectBiasInitializeList.get(0);
		double ctrDataOutputBiasInitializeTagVal = (Double)objectBiasInitialize[1];
		Date ctrDataOutputBiasInitializeTimestamp = (Date)objectBiasInitialize[2];
		if (ctrDataOutputBiasInitializeTagVal == 1 ) {
			int compare = ctrDataOutputBiasInitializeTimestamp.compareTo(psoResultEntity.getTimestamp());
			if (compare > 0) {
				logger.info("# 'OutputController' stopped running. \n Reason: No PSO results after Control Mode Run");
				return;
			}
		}
		
		// Output Hold 인 경우 실행을 종료한다.
		OutputHoldEntity outputHoldEntity = this.outputHoldRepository.findByHoldId("ALL");
		if (outputHoldEntity.getHoldYn()) {
			logger.info("# 'OutputController' stopped running. \n Reason: All hold status");
			return;
		}
		
		// ## PSO Setting history check.
		// # Optimizer Mode가 PSO가 실행되었을때와 지금 다르면 Output Controller을 중지한다.
		ControlEntity controlEntity = this.controlRepository.findControl();
		StringBuilder sb = new StringBuilder();
		if (!psoResultEntity.getPsoSystemStart() || !controlEntity.getSystemStart()) {
			sb.append("- The previous or current \"Optimizer Function Mode\" is not RUN.\n");
		}

		// # PSO 생성 시점의 Control Mode와 현재 Control Mode가 동일해야함.
		// # 현재 Control Mode가 CL 이어야함.
		ControllerModeStatus psoResultControllerMode = ControllerModeStatus.valueOf(psoResultEntity.getPsoControlMode());
		ControllerModeStatus currentControllerMode = ControllerModeStatus.valueOf(controlEntity.getControlMode());

		if (!psoResultControllerMode.equals(currentControllerMode)
				|| !ControllerModeStatus.CL.equals(currentControllerMode)) {
			sb.append("- The previous or current \"Controller Mode\" is not CL or It is different from the previous mode.\n");
		}
		
		// # PSO 생성 시점의 Opt Mode와 현재 Opt Mode가 동일해야함.
		PsoOptimizationFunctionModeStatus psoResultOptimizationFunctionMode = PsoOptimizationFunctionModeStatus.valueOf(psoResultEntity.getPsoOptMode());
		PsoOptimizationFunctionModeStatus currentOptimizationFunctionMode = PsoOptimizationFunctionModeStatus.valueOf(controlEntity.getOptMode());
		if (!psoResultOptimizationFunctionMode.equals(currentOptimizationFunctionMode)) {
			sb.append("- It is different from the previous \"PSO Optimizer Mode.\"\n");
		}
		
		// ## MV별, PSO 결과값, Bias, Hold 값 구하기 .
		// # MV별 PSO 결과값(탐색범위)을 가져온다.
		HashMap<String, PsoMVInfoVO> psoMVInfoMap = new HashMap<String, PsoMVInfoVO>();
		Collection<String> psoMVs = new ArrayList<String>();
		String[] psoMVResults = psoResultEntity.getOptimalPosition().split(",");
		
		// # MV별 PSO 결과값(탐색범위)을 사용하기 위하여 PsoMVInfoMap(PSO Map)에 넣는다.
		for (String psoMVResult : psoMVResults) {
			String[] psoMVResultArray = psoMVResult.split(":");
			String psoMV = psoMVResultArray[0];
			psoMVs.add(psoMV);
			
			PsoMVInfoVO psoMVInfoVO = new PsoMVInfoVO();
			psoMVInfoVO.setPsoMV(psoMV);
			psoMVInfoVO.setPsoResultVal(Double.parseDouble(String.valueOf(psoMVResultArray[1])));
			psoMVInfoVO.setTagSetPointBiasValList(new ArrayList<Double>());
			psoMVInfoVO.setPureSetPointValList(new ArrayList<Double>());
			psoMVInfoMap.put(psoMV, psoMVInfoVO);
		}
		
		// MV별 Bias와 Hold 값을 가져와서 PsoMVInfoMap(PSO Map)에 넣는다.
		List<Object[]> psoMVBiasList = this.psoMVInfoRepository.getPsoMvInfoValueBiasAndHold();
		for (Object[] psoMVBias : psoMVBiasList) {
			String psoMV = String.valueOf(psoMVBias[0]);
			PsoMVInfoVO psoMVInfoVO = psoMVInfoMap.get(psoMV);
			if (psoMVInfoVO != null) {
				psoMVInfoVO.setInputBiasVal((Double) psoMVBias[1]);
				double holdVal = (Double) psoMVBias[2];
				psoMVInfoVO.setHold(holdVal == 0 ? false : true);
				psoMVInfoVO.setOutputBiasTagId(String.valueOf(psoMVBias[3]));
			}
		}

		// DV, MV, Optimum set point check		
		// Output Controller OP Data Load
		List<MVTagDataVO> opLastNNTrainInputData = this.getConvertMVTagDataVO(this.opDataInputRepository.findByLastNNTrainInputOPDataNativeQuery());
		String[] psoInputDatas = psoResultEntity.getPsoInputData().split(",");		
		if (opLastNNTrainInputData.size() != psoInputDatas.length) {
			logger.info("- Result of PSO Train Input information and new Train Inpurt information do not match.\\n");
			return;
		}
		
		// PSO Input Data Load
		List<MVTagDataVO> psoResultNNTrainInputData = new ArrayList<MVTagDataVO>();
		int inputDataIndex = 0;
		for (MVTagDataVO tagDataVO : opLastNNTrainInputData) {
			psoResultNNTrainInputData.add(new MVTagDataVO(tagDataVO.getPsoMV(), tagDataVO.getPsoType(), tagDataVO.getTagNm(), Double.parseDouble(psoInputDatas[inputDataIndex++])));
		}

		OutputDataConfEntity outputDataConfEntity = this.outputDataConfRepository.findByConfId(OutputDataConfEntity.defaultID);
		for (int i = 0; i < psoResultNNTrainInputData.size(); i++) {
			MVTagDataVO psoResultInputData = psoResultNNTrainInputData.get(i);
			MVTagDataVO lastOpInputData = opLastNNTrainInputData.get(i);
							
			double difference = Math.abs(lastOpInputData.getTagVal() - psoResultInputData.getTagVal());
			double differenceRate = (difference / psoResultInputData.getTagVal()) * 100;
			if (!"".equals(psoResultInputData.getPsoMV())) { // MV check.
				
				PsoMVInfoVO psoMVInfoVO = psoMVInfoMap.get(psoResultInputData.getPsoMV());
				
				// MV가 Hold 이면  Output 하지 않음.(유효성 체크도 예외함.)
				if(psoMVInfoVO.isHold())
					continue;
				
				double psoResultMVValue = psoMVInfoVO.getPsoResultVal();
				double mvSetPointStar = 0.0;
				boolean isBreak = false;
				MVType mvType = MVType.valueOf(psoResultInputData.getPsoType());
				switch (mvType) {
				case Burner:
				case OFA:
					differenceRate = difference;
					if (differenceRate >= outputDataConfEntity.getMvDamperChgRate()) {
						sb.append("- There is more than " + outputDataConfEntity.getMvDamperChgRate() + "% change per MV(Manipulated Variable) damper tag.\n");
						isBreak = true;
					}
					
					mvSetPointStar = psoResultMVValue + psoResultInputData.getTagVal();
					differenceRate = Math.abs(lastOpInputData.getTagVal() - mvSetPointStar);
					
					// Optimum set point check.(damper)
					if (differenceRate >= outputDataConfEntity.getMvStarDamperChgRate()) {
						sb.append("- There is more than " + outputDataConfEntity.getMvStarDamperChgRate() + "% change per optimum MV(Manipulated Variable) damper tag.\n");
						isBreak = true;
					}

					break;
				case Air:
					if (differenceRate >= outputDataConfEntity.getMvAirChgRate()) {
						sb.append("- There is more than " + outputDataConfEntity.getMvAirChgRate() + "% change per MV(Manipulated Variable) total air tag.\n");
						isBreak = true;
					}
					
					mvSetPointStar = psoResultInputData.getTagVal() + (psoResultInputData.getTagVal() * (psoResultMVValue / 100));
					difference = Math.abs(lastOpInputData.getTagVal() - mvSetPointStar);
					differenceRate = (difference / lastOpInputData.getTagVal()) * 100;
					
					// Optimum set point check.(air)
					if (differenceRate >= outputDataConfEntity.getMvStarAirChgRate()) {
						sb.append("- There is more than " + outputDataConfEntity.getMvStarAirChgRate() + "% change per optimum MV(Manipulated Variable) total air tag.\n");
						isBreak = true;
					}
					
					break;
				}
				
				if(isBreak)
					break;
				
			} else { // DV check.
				if (difference > outputDataConfEntity.getDvMinChgVal()) {
					if (differenceRate >= outputDataConfEntity.getDvTagChgRate()) {
						sb.append("- There is more than " + outputDataConfEntity.getDvTagChgRate() +"% change per DV(Disturbance Variable) tag.\n");
						break;
					}
				}
			}
		}	

		if (sb.length() > 0) {
			logger.info("#### " + sb.toString());
			return;
		}
		
		OutputBiasConfEntity outputBiasConfEntity = this.outputBiasConfRepository.findByConfId(OutputBiasConfEntity.defaultID);		
		HashMap<String, Double> ctrDataInputMap = this.commonDataService.getCtrDataMap(CtrDataIOType.Input);
		
		// 새로운 PSO 결과값이 만들어지면 다음 PSO 결과가 나오기 전까지 사용하기 위한 Damper MV TGT 값을 생성한다.
		// PSO 결과값이 변경되지 않은 경우  MV TGT 값을 DB로 부터 가져온다.
		HashMap<String, Double> mvBiasTGTMap = new HashMap<String, Double>();
		if (psoResultEntity.getNewMVTGTYN()) {
			
			//새로운 MV TGT 값을 만든다.
			for (int i = 0; i < psoResultNNTrainInputData.size(); i++) {
				MVTagDataVO psoResultInputData = psoResultNNTrainInputData.get(i);
				MVTagDataVO lastOpInputData = opLastNNTrainInputData.get(i);

				if (!"".equals(psoResultInputData.getPsoMV())) {
					PsoMVInfoVO psoMVInfoVO = psoMVInfoMap.get(psoResultInputData.getPsoMV());
					double psoResultMVValue = psoMVInfoVO.getPsoResultVal(); // PSO MV 결과
					double mvBiasVal = psoMVInfoVO.getInputBiasVal(); // DCS MV Bias Value
					MVType mvType = MVType.valueOf(psoResultInputData.getPsoType());
					psoMVInfoVO.setPsoMVType(mvType);

					double mvSetPointStar = 0.0;
					double setPointBiasVal = 0.0;
					double minValue = 0;
					double maxValue = 0;

					switch (mvType) {
					case Burner:
					case OFA:
						mvSetPointStar = psoResultMVValue + psoResultInputData.getTagVal();
						setPointBiasVal = mvSetPointStar - lastOpInputData.getTagVal() + mvBiasVal;						
						if (MVType.Burner.equals(mvType)) {
							minValue = outputBiasConfEntity.getBnrDamperBiasMin();
							maxValue = outputBiasConfEntity.getBnrDamperBiasMax();
						} else {
							minValue = outputBiasConfEntity.getOfaDamperBiasMin();
							maxValue = outputBiasConfEntity.getOfaDamperBiasMax();
						}
						
						// Set Point Bias Add.
						setPointBiasVal = Utilities.valueOfDoubleRange(setPointBiasVal, minValue, maxValue);
						psoMVInfoVO.getTagSetPointBiasValList().add(setPointBiasVal);
						
						// Pure Set Point Add.
						psoMVInfoVO.getPureSetPointValList().add(lastOpInputData.getTagVal() - mvBiasVal);
						
						break;
					case Air:
						break;
					}
				}
			}
			
			// Damper(Burner, OFA)의  MV TGT 값을 만든다.
			for (Map.Entry<String, PsoMVInfoVO> psoMVInfo : psoMVInfoMap.entrySet()) {
				PsoMVInfoVO psoMVInfoVO = psoMVInfo.getValue();				
				switch (psoMVInfoVO.getPsoMVType()) {
				case Burner:
				case OFA:					
					double setPointBiasSum = 0.0; // SetPoint Sum
					double mvBiasTGT = 0.0; // Group Bias TGT
					double setPointValue = 0.0;
					double minPosition = 0;
					double maxPosition = 0;
					double minBias = 0;
					double maxBias = 0;
					for (Double setPointBias : psoMVInfoVO.getTagSetPointBiasValList()) {
						setPointBiasSum += setPointBias;
					}
					mvBiasTGT = setPointBiasSum / psoMVInfoVO.getTagSetPointBiasValList().size();
					
					if (psoMVInfoVO.getPsoMVType() == MVType.Burner) {
						minPosition = CommonConst.MIN_BURNER_POSITION;
						maxPosition = CommonConst.MAX_BURNER_POSITION;
						minBias = outputBiasConfEntity.getBnrDamperBiasMin();
						maxBias = outputBiasConfEntity.getBnrDamperBiasMax();
					} else {
						minPosition = CommonConst.MIN_OFA_POSITION;
						maxPosition = CommonConst.MAX_OFA_POSITION;
						minBias = outputBiasConfEntity.getOfaDamperBiasMin();
						maxBias = outputBiasConfEntity.getOfaDamperBiasMax();
					}
					
					// PureSetPoint Max값을 가져와 MVBaisTGT를 더하고 Max를 초과하지 못하도록 작업함.				
					setPointValue = Collections.max(psoMVInfoVO.getPureSetPointValList()) + mvBiasTGT;
					if (setPointValue > maxPosition) {
						mvBiasTGT -= (setPointValue - maxPosition);
					}
					
					// PureSetPoint Min값을 가져와 MVBaisTGT를 더하고 Min 미만으로 못하도록 작업함.
					setPointValue = Collections.min(psoMVInfoVO.getPureSetPointValList()) + mvBiasTGT;
					if (setPointValue < minPosition) {
						mvBiasTGT += (minPosition - setPointValue);
					}
					
					// MVBiasTGT Range 설정.
					mvBiasTGT = Utilities.valueOfDoubleRange(mvBiasTGT, minBias, maxBias);
					mvBiasTGTMap.put(psoMVInfo.getKey(), mvBiasTGT);
					
					/* Min, Max 에 대한 로그 값 찍기 테스트 시 주석 제거함.
					{
						double min = Collections.min(psoMVInfoVO.getPureSetPointValList());
						double max = Collections.max(psoMVInfoVO.getPureSetPointValList());
						logger.info("### Damper Min = " + min + ", Bias =" + mvBiasTGT + ", Sum = "
								+ (min + mvBiasTGT));
						logger.info("### Damper Max = " + max + ", Bias =" + mvBiasTGT + ", Sum = "
								+ (max + mvBiasTGT));
						for (double pureSetPoint : psoMVInfoVO.getPureSetPointValList()) {
							double testSetPoint = pureSetPoint + mvBiasTGT;
							if (testSetPoint > maxPosition || testSetPoint < minPosition)
								logger.info("### Damper Problem - PureSetPoint = " + pureSetPoint + ", BiasTGT = "
										+ mvBiasTGT + ", Sum = " + testSetPoint);
						}
					}					
					*/					
					break;

				case Air:
					break;
				}
			}
		} else {
			// DB로 부터 MV TGT 값을 가져온다.
			List<OutputMVTGTEntity> outputMVTGTEntityList = this.outputMVTGTRepository.findAll();
			for (OutputMVTGTEntity outputMVTGT : outputMVTGTEntityList) {
				mvBiasTGTMap.put(outputMVTGT.getMv(), outputMVTGT.getMvTGT());
			}
		}		
		
		// Total Air Bias 값을 만든다. (MV TGT값을 매번 계산한다.)
		double pureTotalAirFlow = 0.0;
		for (int i = 0; i < psoResultNNTrainInputData.size(); i++) {
			MVTagDataVO psoResultInputData = psoResultNNTrainInputData.get(i);
			MVTagDataVO lastOpInputData = opLastNNTrainInputData.get(i);

			if (!"".equals(psoResultInputData.getPsoMV())) {
				PsoMVInfoVO psoMVInfoVO = psoMVInfoMap.get(psoResultInputData.getPsoMV());
				double psoResultMVValue = psoMVInfoVO.getPsoResultVal(); // PSO MV 결과
				double mvBiasVal = psoMVInfoVO.getInputBiasVal(); // DCS MV Bias Value
				MVType mvType = MVType.valueOf(psoResultInputData.getPsoType());
				psoMVInfoVO.setPsoMVType(mvType);
				double mvSetPointStar = 0.0;
				double setPointBiasVal = 0.0;				
				switch (mvType) {
				case Burner:
				case OFA:
					break;
					
				case Air:					

					/*
					 * biasVal = 0.10; mvSetPointStar = 2300; double fgO2PV = 3.06; double
					 * totalAirVal = 2550; double fgO2SpTGT = 3.00;
					 */

					/*
					 * biasVal = -0.31; mvSetPointStar = 2550; double fgO2PV = 2.59; double
					 * totalAirVal = 2500; double fgO2SpTGT = 2.65;
					 */

					/*
					 * mvBiasVal = 0.10; mvSetPointStar = 1890; double fgO2PV = 3.56; double
					 * totalAirVal = 2100; double fgO2SpTGT = 3.60;
					 * 
					 * logger.info("### FG O2 PV                            : " + fgO2PV);
					 * logger.info("### Current Total Air Flow              : " + totalAirVal);
					 * logger.info("### New Total Air Flow(NN/PSO out)      : " + mvSetPointStar);
					 * logger.info("### fgO2SpTGT                           : " + fgO2SpTGT);
					 * logger.info("### FG O2 SP Bias                       : " + biasVal);
					 */

					// biasVal = lastOpInputData.getBiasVal();

					double fgO2PV = this.commonDataService.getOpDataMap(CommonConst.O2);
					double fgO2SpTGT = ctrDataInputMap.get(CommonConst.CTR_DATA_CURRENT_O2_SP);
					mvSetPointStar = psoResultInputData.getTagVal()	+ (psoResultInputData.getTagVal() * (psoResultMVValue / 100));				
										
					double totalAirVal = lastOpInputData.getTagVal();

					double cer1 = outputBiasConfEntity.getAirBiasCer0();
					double cer2 = outputBiasConfEntity.getAirBiasCer1();
					double cer3 = outputBiasConfEntity.getAirBiasCer2();
					double cer4 = outputBiasConfEntity.getAirBiasCer3();

					double currentExcessRatio = 100 * ((cer1 * Math.pow(fgO2PV, 3)) + (cer2 * Math.pow(fgO2PV, 2)) + (cer3 * fgO2PV) + cer4 - 1);
					double currentTheoreticalAirFlow = (100 * totalAirVal) / (100 + currentExcessRatio);
					double excessRatio = (100 * mvSetPointStar / currentTheoreticalAirFlow) - 100;
					
					double pureFgO2Sp = fgO2SpTGT - mvBiasVal;
					double pureExcessRatio =  100 * ((cer1 * Math.pow(pureFgO2Sp, 3)) + (cer2 * Math.pow(pureFgO2Sp, 2)) + (cer3 * pureFgO2Sp) + cer4 - 1);
					pureTotalAirFlow = ((pureExcessRatio + 100) * currentTheoreticalAirFlow) / 100;
					
					double nfo1 = outputBiasConfEntity.getAirBiasNfo0();
					double nfo2 = outputBiasConfEntity.getAirBiasNfo1();
					double nfo3 = outputBiasConfEntity.getAirBiasNfo2();
					double nfo4 = outputBiasConfEntity.getAirBiasNfo3();

					double nfoX = (100 + excessRatio) / 100;
					double fgO2SpTGTStar = (nfo1 * Math.pow(nfoX, 3)) + (nfo2 * Math.pow(nfoX, 2)) + (nfo3 * nfoX) + nfo4;
					mvBiasTGTMap.put(psoResultInputData.getPsoMV(), fgO2SpTGTStar);
					
					double fgO2SpBiasStar = fgO2SpTGTStar - fgO2SpTGT + mvBiasVal;
					setPointBiasVal = mvBiasVal;

					double chgPermitVal = outputBiasConfEntity.getAirBiasChgPermitVal() / PER_MINUTE_CYCLE;
					if (Math.abs(fgO2SpBiasStar - mvBiasVal) <= chgPermitVal) {
						setPointBiasVal = fgO2SpBiasStar;
					} else if (fgO2SpBiasStar - mvBiasVal < -chgPermitVal) {
						setPointBiasVal += -chgPermitVal;
					} else if (fgO2SpBiasStar - mvBiasVal > chgPermitVal) {
						setPointBiasVal += chgPermitVal;
					}

					setPointBiasVal = Utilities.valueOfDoubleRange(setPointBiasVal,	outputBiasConfEntity.getAirBiasMin(), outputBiasConfEntity.getAirBiasMax());
					psoMVInfoVO.setOutputBiasVal(setPointBiasVal);

					logger.info("=========================================");
					logger.info("### (A)Current Excess Ratio                :" + currentExcessRatio);
					logger.info("### (B)Current Theoritical Air Flow        :" + currentTheoreticalAirFlow);
					logger.info("### (C)New Excess Ratio                    :" + excessRatio);
					logger.info("### (D)New Fg O2 Target                    :" + fgO2SpTGTStar);
					logger.info("### (E)New FG O2 SP Bias t+1:" + fgO2SpBiasStar);
					logger.info("### (F)New FG O2 SP Bias t+1 (Output Controller):" + setPointBiasVal);
					
					logger.info("=========================================");
					logger.info("### DCS SP Bias (CTR Input)                :" + mvBiasVal);
					logger.info("### DCS SP (CTR Input)                		:" + fgO2SpTGT);
					logger.info("### Math.abs(fgO2SpBiasStar - mvBiasVal)   :" + Math.abs(fgO2SpBiasStar - mvBiasVal));
					logger.info("### chgPermitVal   :" + chgPermitVal);					
					
					// ========== 순수 Total Air Flow 계산용  ==============
					logger.info("### (G)Pure Excess Ratio:" + pureExcessRatio);
					logger.info("### (H)Pure Total Air Flow :" + pureTotalAirFlow);
					break;
				}
			}
		}

		// Damper(Burner, OFA) MV Bias 값을 계산한다.
		for (Map.Entry<String, PsoMVInfoVO> psoMVInfo : psoMVInfoMap.entrySet()) {
			PsoMVInfoVO psoMVInfoVO = psoMVInfo.getValue();
			
			double inputMVBiasVal = psoMVInfoVO.getInputBiasVal();
			double outputMVBiasVal = 0.0;
			double chgPermitVal = 0.0;

			switch (psoMVInfoVO.getPsoMVType()) {
			case Burner:
			case OFA:
				
				double mvBiasTGT = mvBiasTGTMap.get(psoMVInfoVO.getPsoMV());		
				if (MVType.Burner.equals(psoMVInfoVO.getPsoMVType())) {
					chgPermitVal = outputBiasConfEntity.getBnrDamperBiasChgPermitVal() / PER_MINUTE_CYCLE;
				} else {
					chgPermitVal = outputBiasConfEntity.getOfaDamperBiasChgPermitVal() / PER_MINUTE_CYCLE;
				}

				if (Math.abs(mvBiasTGT - inputMVBiasVal) <= chgPermitVal) {
					outputMVBiasVal = mvBiasTGT;
				} else if (mvBiasTGT - inputMVBiasVal < -chgPermitVal) {
					outputMVBiasVal = inputMVBiasVal - chgPermitVal;
				} else if (mvBiasTGT - inputMVBiasVal > chgPermitVal) {
					outputMVBiasVal = inputMVBiasVal + chgPermitVal;
				}

				psoMVInfoVO.setOutputBiasVal(outputMVBiasVal);

				break;

			case Air:				
				break;
			}
		}
		
		// Output Control Save
		algorithmService.saveOutputController(psoMVInfoMap, mvBiasTGTMap, psoResultEntity.getNewMVTGTYN(), pureTotalAirFlow);
	}
	
	/**
	 * 'Object List' 유형의  MVTag 정보를  'MVTagDataVO List' 유형으로 변환하기 위한  함수.
	 * @param objectList Object MVTag List.
	 * @return MVTagDataVO List.
	 */
	private List<MVTagDataVO> getConvertMVTagDataVO(List<Object[]> objectList) {
		List<MVTagDataVO> mvTagDataList = new ArrayList<MVTagDataVO>();		
		for (Object[] arrayObject : objectList) {
			MVTagDataVO mvTagDataVO = new MVTagDataVO();
			mvTagDataVO.setPsoMV(String.valueOf(arrayObject[0]));
			mvTagDataVO.setPsoType(String.valueOf(arrayObject[1]));
			mvTagDataVO.setTagNm(String.valueOf(arrayObject[2]));
			mvTagDataVO.setTagVal(Double.parseDouble(String.valueOf(arrayObject[3])));
		
			mvTagDataList.add(mvTagDataVO);
		}

		return mvTagDataList;
	}
}