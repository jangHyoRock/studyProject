package dhi.optimizer.schedule.executor.pso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;
import dhi.optimizer.algorithm.common.DoubleRange;
import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;
import dhi.optimizer.algorithm.neuralNet.SupervisedLearningProcessor;
import dhi.optimizer.algorithm.pso.PsoCalculationFunction;
import dhi.optimizer.algorithm.pso.PsoMV;
import dhi.optimizer.algorithm.pso.PsoMV.MVType;
import dhi.optimizer.algorithm.pso.PsoService;
import dhi.optimizer.algorithm.pso.PsoTag;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.model.PSOResultVO;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.model.db.OutputMVTGTEntity;
import dhi.optimizer.model.db.PSOConfigEntity;
import dhi.optimizer.model.db.PSOEffectPerItemEntity;
import dhi.optimizer.model.db._NNModelBaseListEntity;
import dhi.optimizer.model.db._NNModelListEntity;
import dhi.optimizer.model.db._NNTrainDataConfEntity;
import dhi.optimizer.model.db._PSOConfEntity;
import dhi.optimizer.model.db._PSOMVInfoEntity;
import dhi.optimizer.model.db._PSOResultEntity;
import dhi.optimizer.model.db._PSOResultIdEntity;
import dhi.optimizer.repository.AppInfRepository;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.repository.NNTrainInRepository;
import dhi.optimizer.repository.NNTrainOutRepository;
import dhi.optimizer.repository.OPDataInputRepository;
import dhi.optimizer.repository.OutputMVTGTRepository;
import dhi.optimizer.repository.PSOConfigRepository;
import dhi.optimizer.repository.SystemCheckRepository;
import dhi.optimizer.repository._NNModelBaseListRepository;
import dhi.optimizer.repository._NNModelConfRepository;
import dhi.optimizer.repository._NNModelListRepository;
import dhi.optimizer.repository._NNTrainDataConfRepository;
import dhi.optimizer.repository._PSOConfRepository;
import dhi.optimizer.repository._PSOMVInfoRepository;
import dhi.optimizer.schedule.executor.ScheduleExecutor;
import dhi.optimizer.service.AlgorithmService;
import dhi.optimizer.service.CommonDataService;

/**
 * Algorithm Executor Scheduler. <br> 
 * : Navigator & PSO Algorithm을 5분 주기로 수행한다.
 */
@Service
public class AlgorithmExecutor2 extends ScheduleExecutor {

	private enum UseModelType {
		Base, Learning
	}
	
	private static final Logger logger = LoggerFactory.getLogger(AlgorithmExecutor2.class.getSimpleName());
	
	@Autowired
	private ControlRepository controlRepository;

	@Autowired
	private _NNModelConfRepository nnModelConfRepository;
	
	@Autowired
	private _NNModelListRepository nnModelListRepository;
	
	@Autowired
	private _NNModelBaseListRepository nnModelBaseListRepository;

	@Autowired
	private _NNTrainDataConfRepository nnTrainDataConfRepository;
		
	@Autowired
	private NNTrainInRepository nnTrainInRepository;
	
	@Autowired
	private NNTrainOutRepository nnTrainOutRepository;
	
	@Autowired
	private OPDataInputRepository opDataInputRepository;

	@Autowired
	private PSOConfigRepository psoConfingRepository;
	
	@Autowired
	private _PSOConfRepository psoConfRepository;

	@Autowired
	private _PSOMVInfoRepository psoMVInfoRepository;

	@Autowired
	private SystemCheckRepository systemCheckRepository;

	@Autowired
	private CommonDataService commonDataService;
	
	@Autowired
	private OutputMVTGTRepository outputMVTGTRepository;
	
	@Autowired
	private AppInfRepository appInfRepository;
	
	@Autowired
	private AlgorithmService algorithmService;
	
	@Value("${algorithm.pso.computer.processing.unit}")
	private String processingUnit;
	
	@Value("${algorithm.pso.computer.processing.unit.gpu.swarmsize}")
	private int gpuUseSwarmsize;
	
	private List<_PSOMVInfoEntity> psoMVInfoEntityList;
	private List<_PSOConfEntity> psoConfEntityList;
	//private List<_NNModelConfEntity> nnModelConfEntityList;
	
	private List<Object[]> objectValidInputDataList;
	private List<Object[]> objectValidOutputDataList;	
	private List<Object[]> objectPSODataList;
	
	private HashMap<String, OutputMVTGTEntity> outputMVTGTMap;
	
	private List<_PSOResultEntity> psoResultEntityList;
	private HashMap<String, PSOResultVO> psoResultMap;
	
	private HashMap<String, List<_NNTrainDataConfEntity>> nnTrainDataConfInputMap;
	private HashMap<String, List<_NNTrainDataConfEntity>> nnTrainDataConfOutputMap;
	
	
	public AlgorithmExecutor2() {
	}

	public AlgorithmExecutor2(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);
	}

	/**
	 * AlgorithmExecutor 시작함수.
	 */
	@Override
	public void start() {
		logger.info("### " + AlgorithmExecutor2.class.getName() + " Start ###");
		try {
			
			// PSO MV Info Load
			this.psoMVInfoEntityList = this.psoMVInfoRepository.findAll();
			
			// PSO Config Load
			this.psoConfEntityList = this.psoConfRepository.findAllNativeQuery();
			
			// NN Model Config Load
			//this.nnModelConfEntityList = this.nnModelConfRepository.findAllNativeQuery();
			
			// Valid Input load
			this.objectValidInputDataList = this.nnTrainInRepository.getNNVaildInputDataNativeQuery();
			
			// Valid Output load
			this.objectValidOutputDataList = this.nnTrainOutRepository.getNNVaildOutputDataNativeQuery();
			
			// PSO Data - Train Input, Output 5분 평균.
			this.objectPSODataList = this.opDataInputRepository.getPSODataNativeQuery();
			
			// PSO MV에 포함된  Tag 정보 가져오기.
			this.nnTrainDataConfInputMap = new HashMap<String, List<_NNTrainDataConfEntity>>();
			this.nnTrainDataConfOutputMap = new HashMap<String, List<_NNTrainDataConfEntity>>();			
			List<_NNTrainDataConfEntity> nnTrainDataConfList = this.nnTrainDataConfRepository.findAll(new Sort(Sort.Direction.ASC, "TagIndex"));						
			for (_PSOConfEntity psoConfEntity : this.psoConfEntityList) {
				this.nnTrainDataConfInputMap.put(psoConfEntity.getPsoID(),
						nnTrainDataConfList.stream()
								.filter(n -> psoConfEntity.getPsoEntity().getModelId().equals(n.getId().getModelId()) && "Input".equals(n.getIoType()))
								.collect(Collectors.toList()));
				
				this.nnTrainDataConfOutputMap.put(psoConfEntity.getPsoID(),
						nnTrainDataConfList.stream()
								.filter(n -> psoConfEntity.getPsoEntity().getModelId().equals(n.getId().getModelId()) && "Output".equals(n.getIoType()))
								.collect(Collectors.toList()));
			}
			
			/*
			 * DCS에 보낸 누적된 MV Output Bias의 Sum과 Count를 Load 한다. (2019-01-26)
			 * MV Bias Sum과 Count로 5분 의 평균 Bias값을 계산하여, 탐색범위를 조절한다.
			 * 5분 의 평균 Bias값을 사용하면 Output Controller 에서 바로 등록해야 하기 때문에  MV Bias Sum, Count 값을 0으로 초기화 한다.
			 */			
			this.outputMVTGTMap = new HashMap<String, OutputMVTGTEntity>();
			List<OutputMVTGTEntity> outputMVTGTEntityList = this.outputMVTGTRepository.findAll();
			outputMVTGTEntityList.forEach(o -> this.outputMVTGTMap.put(o.getMv(), o));
			this.outputMVTGTRepository.updateOutputMVBiasSumCntInitialize();

			// PSO Result List 초기화.
			this.psoResultEntityList = new ArrayList<_PSOResultEntity>();
			this.psoResultMap = new HashMap<String, PSOResultVO>();
			

			// PSO Algorithm Start.
			this.psoConfEntityList.forEach(p -> this.PSOAlgorithm(p.getPsoID()));
			
			// PSO Result Save
			PSOResultVO saPSOResultVO = this.psoResultMap.get("PSO_SA");			
			PsoCalculationFunction calculationFunction = saPSOResultVO.getPsoCalculationFunction();
			
			// 최신 Train Input OP 정보를 NNModel을 통해서 나온 Output 값으로  calcCostFunction 값을 계산한다.
			// double[] beforePsoOutputKeyValues = calculationFunction.getCalcOutputKeyTagValue(outputModelResult); 
			
			// 최신 Train Output Data를 그대로 사용하여 계산함. (2018-12-04)
			double[] beforePsoOutputKeyValues = calculationFunction.getCalcOutputKeyTagValue(saPSOResultVO.getOutputDataValues());
			double[] afterNNOutputKeyValues = calculationFunction.getCalcOutputKeyTagValue(saPSOResultVO.getOutputModelResult());
			double[] afterPsoOutputKeyValues = calculationFunction.getCalcOutputKeyTagValue(saPSOResultVO.getGlobalBestOutput());
			String[] psoEffectPerItems = new String[] { "O2_LEFT", "O2_RIGHT", "RH_SPRAY_LEFT", "RH_SPRAY_RIGHT", "TEMP_LEFT", "TEMP_RIGHT", "NOX", "CO" };
			List<PSOEffectPerItemEntity> psoEffectPerItemEntityList = new ArrayList<PSOEffectPerItemEntity>();
			for (int i = 0; i < psoEffectPerItems.length; i++) {
				PSOEffectPerItemEntity psoEffectPerItemEntity = new PSOEffectPerItemEntity();
				psoEffectPerItemEntity.setItem(psoEffectPerItems[i]);
				psoEffectPerItemEntity.setBeforeVal(beforePsoOutputKeyValues[i]);
				psoEffectPerItemEntity.setAfterNNVal(afterNNOutputKeyValues[i]);
				psoEffectPerItemEntity.setAfterVal(afterPsoOutputKeyValues[i]);
				psoEffectPerItemEntity.setTimestamp(new Date());
				psoEffectPerItemEntityList.add(psoEffectPerItemEntity);
			}
			
			this.algorithmService.saveAlgorithmPSOResult2(this.psoResultEntityList, psoEffectPerItemEntityList);
			
		} catch (Exception e) {
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			logger.info("### " + AlgorithmExecutor2.class.getName() + " End ###");
		}
	}
	
	/**
	 * PSO 알고리즘.
	 */
	private void PSOAlgorithm(String psoId) {
		
		// PSO Configuration;
		_PSOConfEntity psoConfEntity = this.psoConfEntityList.stream()
				.filter(p -> p.getPsoID().equals(psoId))
				.findFirst().get();
				
		String nnModelId = psoConfEntity.getPsoEntity().getModelId();
		
		// # Valid OP input load
		List<Object[]> objectValidInputDataList = this.objectValidInputDataList.parallelStream()
				.filter(object -> nnModelId.equals(object[0]))
				.collect(Collectors.toList());
		
		List<String> nnModelValidInputList = new ArrayList<String>();
		objectValidInputDataList.forEach(object -> nnModelValidInputList.add((String) object[2]));
				
		if (nnModelValidInputList.size() <= 0) {
			this.systemCheckRepository.updateSetControlReadyAl(false);
			logger.info("# PSO can not start because there is no latest NN Train data.");
			return;
		}

		double[][] validInputDataValues = Utilities.convertRowsAndCommaColumnsToDouble(nnModelValidInputList);
		
		// # Train PSO input load
		List<Object[]> objectPSOInputDataList = this.objectPSODataList.parallelStream()
				.filter(object -> nnModelId.equals(object[0]) && "Input".equals(object[1]))
				.collect(Collectors.toList());
		
		List<String> nnModelPSOInputList = new ArrayList<String>();
		objectPSOInputDataList.forEach(object -> nnModelPSOInputList.add((String) object[3]));
		
		if (nnModelPSOInputList.size() <= 0) {
			this.systemCheckRepository.updateSetControlReadyAl(false);
			logger.info("# PSO can not start because there is no latest OP data.");
			return;
		}
		
		double[][] psoInputDataValues = Utilities.convertRowsAndCommaColumnsToDouble(nnModelPSOInputList);
		double[] inputDataValues = psoInputDataValues[0];		
		
		// # Valid OP output load
		List<Object[]> objectValidOutputDataList = this.objectValidOutputDataList.stream()
				.filter(object -> nnModelId.equals(object[0]))
				.collect(Collectors.toList());
		
		List<String> nnModelValidOutputList = new ArrayList<String>();
		objectValidOutputDataList.forEach(object -> nnModelValidOutputList.add((String) object[2]));
		
		double[][] validOutputDataValues = Utilities.convertRowsAndCommaColumnsToDouble(nnModelValidOutputList);
		
		// # Train PSO output load
		List<Object[]> objectPSOOutputDataList = this.objectPSODataList.stream()
				.filter(object -> nnModelId.equals(object[0]) && "Output".equals(object[1]))
				.collect(Collectors.toList());
		
		List<String> nnModelPSOOutputList = new ArrayList<String>();
		for (Object[] objectPSOOutputData : objectPSOOutputDataList) {
			nnModelPSOOutputList.add((String) objectPSOOutputData[3]);
		}
		
		double[][] psoOutputDataValues = Utilities.convertRowsAndCommaColumnsToDouble(nnModelPSOOutputList);				
		double[] outputDataValues = psoOutputDataValues[0];

		List<Object[]> networkInfoList = new ArrayList<Object[]>();

		// Base NN model load.
		List<_NNModelBaseListEntity> nnModelBaseEntityList = this.nnModelBaseListRepository.findById_ModelIdAndTrainStatus(nnModelId,  true);
		for (_NNModelBaseListEntity nnModelBaseEntity : nnModelBaseEntityList) {
			networkInfoList.add(new Object[] { UseModelType.Base.name(), nnModelBaseEntity.getId().getTimestamp(),(ActivationNetwork)Utilities.deSerialize(nnModelBaseEntity.getModel())});
		}

		// NN model load.
		List<_NNModelListEntity> nnModelEntityList = this.nnModelListRepository.findTop3ById_ModelIdAndTrainStatusOrderById_TimestampDesc(nnModelId, true);
		for (_NNModelListEntity nnModelEntity : nnModelEntityList) {
			networkInfoList.add(new Object[] { UseModelType.Learning.name(), nnModelEntity.getId().getTimestamp(),(ActivationNetwork)Utilities.deSerialize(nnModelEntity.getModel())});
		}

		if (networkInfoList.size() <= 0) {
			this.systemCheckRepository.updateSetControlReadyAl(false);
			logger.info("# The default NN model does not exist.");
			return;
		}

		// Get the criteria NNModel for the NN Model conformance test.
		// The newly generated model is compared with the reference model and the
		// Select the most appropriate model.
		SupervisedLearningProcessor nnLearningService = new SupervisedLearningProcessor();
		Object[] networkInfo = nnLearningService.selectAfterVerification(validInputDataValues, validOutputDataValues, networkInfoList);
		
		String modelType = String.valueOf(networkInfo[0]);
		Date modelTimestamp = (Date)networkInfo[1];
		ActivationNetwork model = (ActivationNetwork)networkInfo[2];
		
		// ## NN Model Availability Check.
		double[] outputModelResult = model.calcOutput(inputDataValues);

		double sumSquare = 0;
		boolean isControlReadyAl = false;
		double limitValue = Math.pow(psoConfEntity.getNnModelAllowValue(), 2);
		if (outputDataValues.length == outputModelResult.length) {			
			for (int i = 0; i < outputDataValues.length; i++) {
				sumSquare += Math.pow((outputDataValues[i] - outputModelResult[i]), 2);
			}

			if (sumSquare < limitValue)
				isControlReadyAl = true;
		}
		
		// Control Ready Status Check.	
		this.systemCheckRepository.updateSetControlReadyAl(isControlReadyAl);
		if (!isControlReadyAl) {
			logger.info("# Verification of NN Model(" + modelType + " :" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(modelTimestamp) + ") availability has failed.");
			return;
		}
		
		// # OutPut PsoTag Set		
		List<_NNTrainDataConfEntity> nnTrainDataConfOutputList = this.nnTrainDataConfOutputMap.get(psoId);
		List<PsoTag> outputTagList = new ArrayList<PsoTag>();		
		for (_NNTrainDataConfEntity nnTrainDataConf : nnTrainDataConfOutputList) {
			outputTagList.add(new PsoTag(nnTrainDataConf.getId().getTagId(), nnTrainDataConf.getTagIndex(), nnTrainDataConf.getZeroPlate()));			
		}
		
		double opOutputLoadMW = 0;
		double opInputLoadSetPointMW = 0;
		
		// # Input PsoTag Set
		List<_NNTrainDataConfEntity> nnTrainDataConfInputList = this.nnTrainDataConfInputMap.get(psoId);	
		if ("MILL".equals(psoConfEntity.getPsoEntity().getPsoType())) {

			// Mill 타입의 경우 Input 값을 Master에서 예측한 Model Output 값으로 변경한다.
			PSOResultVO masterPSOResult = this.psoResultMap.get("PSO_MASTER");
			List<_NNTrainDataConfEntity> masterNNTrainDataConfOutputList = this.nnTrainDataConfOutputMap.get("PSO_MASTER");
			for (_NNTrainDataConfEntity masterNNTrainDataConfOutput : masterNNTrainDataConfOutputList) {

				Optional<_NNTrainDataConfEntity> millInputTagInfo = nnTrainDataConfInputList.parallelStream()
						.filter(n -> n.getId().getTagId().equals(masterNNTrainDataConfOutput.getId().getTagId()))
						.findFirst();

				if (millInputTagInfo.isPresent()) {
					inputDataValues[millInputTagInfo.get().getTagIndex()] = masterPSOResult.getOutputModelResult()[masterNNTrainDataConfOutput.getTagIndex()];
				}
			}
		} else if("SA".equals(nnModelId)) {
			
			int opOutputLoadMWIndex = nnTrainDataConfOutputList.stream()
					.filter(n -> CommonConst.ACTIVE_POWER_OF_GENERATOR.equals(n.getId().getTagId()))
					.findFirst().get().getTagIndex();			
			opOutputLoadMW = outputDataValues[opOutputLoadMWIndex];

			int opInputLoadSetPointMWIndex = nnTrainDataConfInputList.stream()
					.filter(n -> CommonConst.ULD_LOCAL_SET_POINT.equals(n.getId().getTagId()))
					.findFirst().get().getTagIndex();			
			opInputLoadSetPointMW = inputDataValues[opInputLoadSetPointMWIndex];
		}
		
		// # Input PsoMV Set
		List<PsoMV> mvList = new ArrayList<PsoMV>();
		for (_PSOMVInfoEntity psoMVInfoEntity : this.psoMVInfoEntityList.stream().filter(p -> p.getPsoId().equals(psoId)).collect(Collectors.toList())) {
			MVType mvType = Enum.valueOf(MVType.class, psoMVInfoEntity.getPsoMVType());
			
			List<PsoTag> inputTagList = new ArrayList<PsoTag>();
			
			// 각 MV에 해당하는 Input Tag와 해당 위치의 NN Model Min, Max값을 설정함. 
			int tagIndex = 0;
			for (_NNTrainDataConfEntity nnTrainDataConf : nnTrainDataConfInputList) {
				if (psoMVInfoEntity.getPsoMV().equals(nnTrainDataConf.getPsoMV())) {
					tagIndex = nnTrainDataConf.getTagIndex();
					DoubleRange[] tagMinMax = model.getInputNormalizeInfo().getRanges();
					inputTagList.add(new PsoTag(nnTrainDataConf.getId().getTagId(), tagMinMax[tagIndex].getMin(), tagMinMax[tagIndex].getMax(), tagIndex));
				}
			}
			
			// DCS로 보낸 MV Bias 값을 찾는다.
			OutputMVTGTEntity outputMVTGTEntity = this.outputMVTGTMap.get(psoMVInfoEntity.getPsoMV());
			double mvBiasAvg = outputMVTGTEntity == null || outputMVTGTEntity.getMvBiasCnt() == 0 ? 0 : outputMVTGTEntity.getMvBiasSum() / outputMVTGTEntity.getMvBiasCnt();
			
			// TODO : 나중에 삭제해야함. 에러 발생 하드코딩. 삭제해야함.
			outputMVTGTEntity = new OutputMVTGTEntity();
			outputMVTGTEntity.setMvBiasCnt(0);
			outputMVTGTEntity.setMvBiasSum(0);		
			
			/* 
			 * Total Air 는 현재 탐색 위치를 찾기 위해 DCS에게 보낸 값을 역변환 한다. (아래 공식 사용)
			 * Total Air Flow bias(%) = 100 × (Pure Total Air Flow － Total Air Flow) ÷ (Pure Total Air Flow))
			 * 그 외 Burner, OFA는 위 MV Bias Sum, Count를 사용하여 평균값을 만들어 사용함.
			 */
			if (MVType.Air == mvType) {
				int totalAirIdx = inputTagList.stream().filter(t -> CommonConst.TOTAL_AIR_FLOW.equals(t.getId())).findFirst().get().getIndex();
				mvBiasAvg = mvBiasAvg == 0 ? 0 : 100 * (mvBiasAvg - inputDataValues[totalAirIdx]) / mvBiasAvg;
			}
			
			double psoMVMin = psoMVInfoEntity.getPsoMVMin() - mvBiasAvg;
			double psoMVMax = psoMVInfoEntity.getPsoMVMax() - mvBiasAvg;
			
			logger.info("# PSO MV - " + psoMVInfoEntity.getPsoMV() + " #");
			logger.info("# Original => MIN : " + psoMVInfoEntity.getPsoMVMin() + ", MAX : " + psoMVInfoEntity.getPsoMVMax() + " #");
			logger.info("# MV Bais Sum, Count, Avg : " + outputMVTGTEntity.getMvBiasSum() + ", " + outputMVTGTEntity.getMvBiasCnt() + ", " + mvBiasAvg + " => Bias avg apply PSO => MIN : "
					+ psoMVMin + ", MAX : " + psoMVMax + " #");			
	
			// BNR Corner CN1 ~ CN4 까지 기기 보호 조건 적용 로직.
			// 해당 Group에서 최소값이 25 이하이면 PSO Min 값을 0으로 설정, 최대값이 100 이상이면  PSO Max 값을  0으로 설정
			// 해당 Group에서 최소값이  Low Base 25% 미만로 못내려가기 위한 보호로직 적용. 예) (psoMin, psoMax, CN값 => 최종 PSOMin) -0.1, 5, 28 => -0.1 | -5, 5, 28 => -3 | -5, 5, 31 => -5
			// 해당 Group에서 최대값이  High Base 100% 초과를 못하기 하기 위한 보호로직 적용. 예)(psoMin, psoMax, CN값 => 최종 PSOMax) -5, 0.1, 98 => 0.1 | -5, 5, 98 => 2 | -5, 5, 94 => 5
			// 2018.12.10 적용함.
			double lowBaseRate = 0;
			double highBaseRate = 0;
			double minValue = Double.MAX_VALUE;
			double maxValue = Double.MIN_VALUE;
			
			switch (mvType) {
			case Burner:
			case OFA:
				if (mvType == MVType.Burner) {
					lowBaseRate = CommonConst.MIN_BURNER_POSITION;
					highBaseRate = CommonConst.MAX_BURNER_POSITION;
				} else {
					lowBaseRate = CommonConst.MIN_OFA_POSITION;
					highBaseRate = CommonConst.MAX_OFA_POSITION;
				}
				
				for (PsoTag inputTag : inputTagList) {
					double tagValue = inputDataValues[inputTag.getIndex()];
					if (minValue > tagValue)
						minValue = tagValue;

					if (maxValue < tagValue)
						maxValue = tagValue;
				}
				
				if (lowBaseRate >= minValue)
					psoMVMin = 0;
				else {
					double diffValue = lowBaseRate - minValue;
					psoMVMin = diffValue > psoMVMin ? diffValue : psoMVMin;
				}

				if (highBaseRate <= maxValue)
					psoMVMax = 0;
				else {
					double diffValue = highBaseRate - maxValue;
					psoMVMax = diffValue < psoMVMax ? diffValue : psoMVMax;
				}
				
				break;						
			case Air:
				break;
			}
			
			PsoMV psoMV = new PsoMV(psoMVInfoEntity.getPsoMV(), mvType, psoMVMin, psoMVMax);
			if (inputTagList.size() > 0) {
				psoMV.setInputTagList(inputTagList.toArray(new PsoTag[inputTagList.size()]));
				mvList.add(psoMV);
			}
		}

		ControlEntity controlEntity = this.controlRepository.findControl();
		
		// TODO : 나중에 삭제해야함. PsoCalculationFunction 도 추후에 수정해야함.
		PSOConfigEntity psoConfigEntity = new PSOConfigEntity();
		psoConfigEntity = this.psoConfingRepository.findByConfId(PSOConfigEntity.defaultID);
		
		// ## PSO Service Execute.
		PsoCalculationFunction calculationFunction = new PsoCalculationFunction(outputTagList, controlEntity, psoConfigEntity, opOutputLoadMW, opInputLoadSetPointMW);
		PsoService psoService = new PsoService(mvList, inputDataValues, model, calculationFunction, psoConfigEntity, processingUnit, gpuUseSwarmsize);
		int result = psoService.execute();
		if (result < 0) {
			this.systemCheckRepository.updateSetControlReadyAl(false);
			logger.info("# PSO Execute has failed.");
			return;
		}
		
		double globalBestValue = psoService.getGlobalBestValue();
		double[] globalBestMV = psoService.getGlobalBestMV();
		// double[] globalBestOutput = psoService.getGlobalBestOutput();
		String resultMV = CommonConst.StringEmpty;
		for (int i = 0; i < mvList.size(); i++) {
			PsoMV psoMV = mvList.get(i);
			resultMV += "," + psoMV.getName() + ":" + globalBestMV[i];
		}
		resultMV = resultMV.substring(1); // Purpose to remove the comma of the first character.
		
		// 최신 Train Input OP 정보를 NNModel을 통해서 나온 Output 값으로 calcCostFunction 값을 계산한다.
		double opDataFValue = calculationFunction.calcCostFunction(outputModelResult);
		_PSOResultEntity psoResultEntity = new _PSOResultEntity();
		psoResultEntity.setId(new _PSOResultIdEntity(new Date(), psoId));
		psoResultEntity.setIteration(psoConfigEntity.getIteration());
		psoResultEntity.setOptimalPosition(resultMV);
		psoResultEntity.setOptimalFVal(globalBestValue);
		psoResultEntity.setPsoSystemStart(controlEntity.getSystemStart());
		psoResultEntity.setPsoControlMode(controlEntity.getControlMode());
		psoResultEntity.setPsoOptMode(controlEntity.getOptMode());
		psoResultEntity.setPsoUsedModelTs(modelTimestamp);
		psoResultEntity.setPsoUsedModelType(modelType);
		psoResultEntity.setNewMVTGTYN(true);
		psoResultEntity.setErrorSum(sumSquare);
		psoResultEntity.setOpDataFValue(opDataFValue);
		psoResultEntity.setPsoInputData(nnModelPSOInputList.get(0));
		psoResultEntity.setPsoOutputData(nnModelPSOOutputList.get(0));
		this.psoResultEntityList.add(psoResultEntity);
		
		PSOResultVO psoResultVO = new PSOResultVO();
		psoResultVO.setPsoID(psoId);
		psoResultVO.setGlobalBestValue(psoService.getGlobalBestValue());
		psoResultVO.setGlobalBestMV(psoService.getGlobalBestMV());
		psoResultVO.setGlobalBestOutput(psoService.getGlobalBestOutput());
		psoResultVO.setMvValues(resultMV);		
		psoResultVO.setPsoCalculationFunction(calculationFunction);
		psoResultVO.setOutputDataValues(outputDataValues);
		psoResultVO.setOutputModelResult(outputModelResult);
		
		this.psoResultMap.put(psoId, psoResultVO);
	}
}