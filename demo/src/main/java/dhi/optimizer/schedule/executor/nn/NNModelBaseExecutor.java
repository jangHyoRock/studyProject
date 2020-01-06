package dhi.optimizer.schedule.executor.nn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import dhi.common.exception.CustomException;
import dhi.common.util.Utilities;

import dhi.optimizer.algorithm.common.LearningInfo;
import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;
import dhi.optimizer.algorithm.neuralNet.SupervisedLearningProcessor;
import dhi.optimizer.algorithm.neuralNet.SupervisedLearningService;
import dhi.optimizer.algorithm.neuralNet.SupervisedLearnings;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.enumeration.LearningModeStatus;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.model.db._NNModelBaseListEntity;
import dhi.optimizer.model.db._NNModelConfEntity;
import dhi.optimizer.model.db._NNModelExecStatusEntity;
import dhi.optimizer.model.db._NNModelListEntity;
import dhi.optimizer.model.db._NNModelListIdEntity;
import dhi.optimizer.repository.ControlHistoryRepository;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.repository.NNTrainInRepository;
import dhi.optimizer.repository.NNTrainOutRepository;
import dhi.optimizer.repository.NoticeHistoryRepository;
import dhi.optimizer.repository._NNModelBaseListRepository;
import dhi.optimizer.repository._NNModelConfRepository;
import dhi.optimizer.repository._NNModelExecStatusHistRepository;
import dhi.optimizer.repository._NNModelExecStatusRepository;
import dhi.optimizer.repository._NNModelListRepository;
import dhi.optimizer.schedule.IScheduleExecutorThread;
import dhi.optimizer.schedule.executor.NNModelExecutor;

/**
 * NN Model Scheduler. <br>
 * : Master, Mill, SA Type 의 NN Model을 만들기 위한 공통 Prototype 용 NNModel 서비스.
 */
@Service
@Scope(value="prototype")
public class NNModelBaseExecutor implements IScheduleExecutorThread {
	
	private static final Logger logger = LoggerFactory.getLogger(NNModelBaseExecutor.class.getSimpleName());
		
	@Autowired
	private ControlRepository controlRepository;
	
	@Autowired
	private ControlHistoryRepository controlHistoryRepository;
	
	@Autowired
	private _NNModelConfRepository nnModelConfRepository;
	
	@Autowired
	private _NNModelExecStatusRepository nnModelExecStatusRepository;
	
	@Autowired
	private _NNModelExecStatusHistRepository nnModelExecStatusHistRepository;
	
	@Autowired
	private _NNModelListRepository nnModelListRepository;
	
	@Autowired
	private _NNModelBaseListRepository nnModelBaseListRepository;
		
	@Autowired
	private NNTrainInRepository nnTrainInRepository;
	
	@Autowired
	private NNTrainOutRepository nnTrainOutRepository;
	
	@Autowired
	private NoticeHistoryRepository noticeHistoryRepository;
	
	private SupervisedLearningService nnLearningService = null;	
	private LearningInfo targetTrainInfo;	
	private double[][] trainInputData, trainTargetData, validInputData, validTargetData;
	
	private String testData;
			
	public String getTestData() {
		return testData;
	}

	public void setTestData(String testData) {
		this.testData = testData;
	}

	public NNModelBaseExecutor() {}	
	
	@Override
	public void start(Object param) {
		String modelId = (String)param;
		if (this.isNNModelEnable(modelId)) {
			this.createNNModel(modelId);
		} else {
			logger.info("### Use of " + NNModelBaseExecutor.class.getName() + "("+ modelId +")" +  " is false. ###");
		}
	}
	
	/**
	 * NN Model 생성이 가능한 상태인지를 확인하는 함수. <br>
	 * @return NNModel 수행 가능 상태여부.(true, false)
	 */
	public boolean isNNModelEnable(String modelId) {
		boolean isEnable = false;
		ControlEntity controlEntity = this.controlRepository.findControl();
		LearningModeStatus learningMode = (LearningModeStatus) LearningModeStatus.valueOf(controlEntity.getLearningMode());
		if (LearningModeStatus.ON == learningMode) {
			_NNModelExecStatusEntity nnModelExecStatusEntity = this.nnModelExecStatusRepository.findByModelId(modelId);
			if (!nnModelExecStatusEntity.getEndYn()) {
				isEnable = true;
			}
		} else if (LearningModeStatus.SO == learningMode || LearningModeStatus.SAO == learningMode) {
			isEnable = true;
		}

		return isEnable;
	}
	
	/**
	 * NN Model 생성 함수.
	 */
	public void createNNModel(String modelId) {
		
		int status = 1;
		String errorMessage = CommonConst.StringEmpty;
		_NNModelConfEntity nnModelConfEntity = null;
		try {
			
			// NN Model Executor Start Status 변경.
			this.nnModelExecStatusRepository.updateNNModelExecutorStartStatus(modelId, new Date(), status);
			
			// NN Model Config 정보 가져오기.
			nnModelConfEntity = this.nnModelConfRepository.findByModelIdNativeQuery(modelId);
			
			// Train Input, Output Tag Load. 
			this.loadNNModelInputData(nnModelConfEntity);
			this.loadNNModelTargetData(nnModelConfEntity);
	
			// Train Data 가 유효한지 체크함.
			if (!this.nnModelDataValidityCheck(nnModelConfEntity))
				throw new CustomException("The NN Model validation check did not pass.");
	
			// Train 하기 위한 설정값 Setting.
			this.targetTrainInfo = new LearningInfo();
			this.targetTrainInfo.setLearningRate(nnModelConfEntity.getLearningRate());
			this.targetTrainInfo.setMomentum(nnModelConfEntity.getMomentum());
			this.targetTrainInfo.setSigmoidAlphaValue(nnModelConfEntity.getSigmoidAlphaVal());
	
			List<Integer> neuronsInLayer = this.targetTrainInfo.getNeuronsInLayer();
			neuronsInLayer.set(0, nnModelConfEntity.getNeuronCntLayer1());
			neuronsInLayer.set(1, nnModelConfEntity.getNeuronCntLayer2());
	
			this.targetTrainInfo.setNeuronsInLayer(neuronsInLayer);
			this.targetTrainInfo.setIterations(nnModelConfEntity.getIterations());
			this.targetTrainInfo.setMu(nnModelConfEntity.getMu());
			this.targetTrainInfo.setMse(nnModelConfEntity.getTrainingMSE());
			this.targetTrainInfo.setValidMse(nnModelConfEntity.getValidationMSE());
			this.targetTrainInfo.setValidationFailedCount(nnModelConfEntity.getValidationCheck());
	
			this.nnLearningService = new SupervisedLearningProcessor();
			this.nnLearningService.setTestFileId(new SimpleDateFormat("yyyymmddHHmm").format(new Date()));
			
			if ("Backpropagation".equals(nnModelConfEntity.getTrainingAlgorithm()))
				this.nnLearningService.setSelectedlearningAlgorithm(SupervisedLearnings.BackPropagation);
			else if ("ResilientBackpropagation".equals(nnModelConfEntity.getTrainingAlgorithm()))
				this.nnLearningService.setSelectedlearningAlgorithm(SupervisedLearnings.ResilientBackpropagation);
			else
				this.nnLearningService.setSelectedlearningAlgorithm(SupervisedLearnings.BackPropagation);
	
			this.nnLearningService.setTargetTrainInfo(targetTrainInfo);
			this.nnLearningService.intialize(trainInputData, trainTargetData, validInputData, validTargetData);
			
			this.targetTrainInfo.setIterations(10); // TODO : 삭제해야함.
			this.nnLearningService.execute();
	
			// New NN model add (0. index)
			List<ActivationNetwork> networkList = new ArrayList<ActivationNetwork>();
			networkList.add(this.nnLearningService.getAvailableNetwork());
			
			// Base NN model load.
			List<_NNModelBaseListEntity> nnModelBaseListEntityList = this.nnModelBaseListRepository.findById_ModelIdAndTrainStatus(nnModelConfEntity.getModelId(), true);
			for (_NNModelBaseListEntity nnModelBaseListEntity : nnModelBaseListEntityList) {
				networkList.add((ActivationNetwork) Utilities.deSerialize(nnModelBaseListEntity.getModel()));
			}
			
			// History NN model load.		
			List<_NNModelListEntity> nnModelListEntityList = this.nnModelListRepository.findTop3ById_ModelIdAndTrainStatusOrderById_TimestampDesc(nnModelConfEntity.getModelId(), true);
			for (_NNModelListEntity nnModelListEntity : nnModelListEntityList) {
				networkList.add((ActivationNetwork) Utilities.deSerialize(nnModelListEntity.getModel()));
			}
	
			// Get the criteria NNModel for the NN Model conformance test.
			// The newly generated model is compared with the reference model and the
			// conformity is verified.
			_NNModelListEntity _nnModelListEntity = new _NNModelListEntity();
			_nnModelListEntity.setId(new _NNModelListIdEntity(new Date(), nnModelConfEntity.getModelId()));
			boolean isUseNNModel = nnLearningService.verification(validInputData, validTargetData, networkList);
			if (isUseNNModel) {
				_nnModelListEntity.setTrainStatus(true);
				this.noticeHistoryRepository.insertAll(CommonConst.AlarmNameNNMODEL + " (" + nnModelConfEntity.getNnModelEntity().getModelNm() + ")", "S", "E", "The NN Model has been updated successfully.");
			} else {
				_nnModelListEntity.setTrainStatus(false);
			}
			
			_nnModelListEntity.setIteration(nnLearningService.getTrainIterationCount());
			_nnModelListEntity.setTrainErrorRate(nnLearningService.getMinTrainMseAvg());
			_nnModelListEntity.setValidErrorRate(nnLearningService.getMinValidMseAvg());
			_nnModelListEntity.setModel(Utilities.serialize(nnLearningService.getAvailableNetwork()));
			this.nnModelListRepository.saveAndFlush(_nnModelListEntity);	
			status = 0;		
		} catch (CustomException e) {
			status = -1;
			errorMessage = e.getMessage();
		} catch (Exception e) {
			status = -1;
			errorMessage = e.getMessage();
			throw e;
		} finally {
			
			// NN Model Executor End Status 변경.
			Date endDT = new Date();
			this.nnModelExecStatusRepository.updateNNModelExecutorEndStatus(modelId, endDT, status);
			this.nnModelExecStatusHistRepository.insertAll(endDT, modelId, nnModelConfEntity.getNnModelEntity().getModelType(), status, errorMessage);
			
			// NN Model Executor 전체 완료 체크 - Learning Mode 가 ON 인 경우 OFF 하기 위함.
			int nnModelAllComplete = this.nnModelExecStatusRepository.getNNModelExecutorAllCompleteStatus();
			Boolean isNNModelAllComplete = nnModelAllComplete == 1 ? true : false;
			
			ControlEntity controlEntity = this.controlRepository.findControl();
			LearningModeStatus learningMode = (LearningModeStatus) LearningModeStatus.valueOf(controlEntity.getLearningMode());
			if (LearningModeStatus.ON == learningMode && isNNModelAllComplete) {
				this.controlRepository.updateLearningModeAndTimeStamp(LearningModeStatus.OFF.name());
				this.controlHistoryRepository.insertAll(controlEntity.getSystemStart(), controlEntity.getControlMode(), controlEntity.getOptMode(), LearningModeStatus.OFF.name(), NNModelExecutor.class.getName());
			}
		}
	}

	/**
	 * Train Input Tag 정보를 읽어서 Train, Valid Input Data를 생성함.
	 * @param nnConfigEntity NNModel Config 정보.
	 */
	private void loadNNModelInputData(_NNModelConfEntity nnModelConfEntity) {
		List<String> tempTrainData = new ArrayList<String>();
		List<String> tempValidData = new ArrayList<String>();

		List<Object[]> nnTrainDataList = this.nnTrainInRepository.getNNTrainInputDataNativeQuery(nnModelConfEntity.getModelId(), nnModelConfEntity.getTrainDataTime());
		this.getNNModelData(nnTrainDataList, nnModelConfEntity.getValidDataTime(), tempTrainData, tempValidData);

		this.trainInputData = Utilities.convertRowsAndCommaColumnsToDouble(tempTrainData);
		this.validInputData = Utilities.convertRowsAndCommaColumnsToDouble(tempValidData);
	}
	
	/**
	 * Train Target(Output) Tag 정보를 읽어서 Train, Valid Output Data를 생성함.
	 * @param nnConfigEntity NNModel Config 정보.
	 */
	private void loadNNModelTargetData(_NNModelConfEntity nnModelConfEntity) {
		List<String> tempTrainData = new ArrayList<String>();
		List<String> tempValidData = new ArrayList<String>();

		List<Object[]> nnTrainDataList = this.nnTrainOutRepository.getNNTrainOutputDataNativeQuery(nnModelConfEntity.getModelId(), nnModelConfEntity.getTrainDataTime());
		this.getNNModelData(nnTrainDataList, nnModelConfEntity.getValidDataTime(), tempTrainData, tempValidData);

		this.trainTargetData = Utilities.convertRowsAndCommaColumnsToDouble(tempTrainData);
		this.validTargetData = Utilities.convertRowsAndCommaColumnsToDouble(tempValidData);
	}
	
	/**
	 * NN Model의 학습 데이터를 받아 Train, Valid Data로 분리한다. 
	 * 
	 * @param nnTrainDataList 학습데이터 List
	 * @param validDateTime Valid를 분리하기 위한 최근시간.(초)
	 * @param trainData Train으로 분리된 Data List
	 * @param validData Valid로  분리된 Data List
	 */
	private void getNNModelData(List<Object[]> nnTrainDataList, int validDateTime, List<String> trainData, List<String> validData) {

		if (nnTrainDataList.size() <= 0)
			return;

		Object[] lastNNTrainData = nnTrainDataList.get(0);
		Calendar cal = Calendar.getInstance();
		cal.setTime((Date)lastNNTrainData[0]);
		cal.add(Calendar.SECOND, -validDateTime);
		Date validtMinDate = cal.getTime();

		for (Object[] nnTrainData : nnTrainDataList) {
			Date nnModelDate = (Date) nnTrainData[0];
			int compare = nnModelDate.compareTo(validtMinDate);

			if (compare > 0)
				validData.add((String) nnTrainData[1]);
			else
				trainData.add((String) nnTrainData[1]);
		}
	}
	
	/**
	 * NN Model의 수행이 유효한지를 검사하는 함수.
	 * @param nnConfigEntity NNModel Config.
	 * @return NNModel 수행 가능 상태여부.(true, false)
	 */
	private boolean nnModelDataValidityCheck(_NNModelConfEntity nnModelConfEntity)	{
		boolean isValid = true;
		
		StringBuilder sb = new StringBuilder();		
		if(nnModelConfEntity.getNeuronCntLayer1() <= 0)
			sb.append("Count of layer 1 is zero. \n");

		if(nnModelConfEntity.getNeuronCntLayer2() <= 0)
			sb.append("Count of layer 2 is zero. \n");

		if(nnModelConfEntity.getIterations() <= 0)
			sb.append("Iterations is zero. \n");
		
		if(this.trainInputData.length <= 0
				|| this.trainTargetData.length <= 0 
				|| this.validInputData.length <= 0
				|| this.validTargetData.length <= 0)
			sb.append("Training data is zero. \n");
		
		if(nnModelConfEntity.getTrainDataMinCnt() > this.trainInputData.length)
			sb.append("Counter of training input data is less than " + nnModelConfEntity.getTrainDataMinCnt() + ". \n");
		
		if(nnModelConfEntity.getTrainDataMinCnt() > this.trainTargetData.length)
			sb.append("Counter of training target data is less than " + nnModelConfEntity.getTrainDataMinCnt() + ". \n");
		
		if(nnModelConfEntity.getValidDataMinCnt() > this.validInputData.length)
			sb.append("Counter of valid input data is less than " + nnModelConfEntity.getValidDataMinCnt() + ". \n");
		
		if(nnModelConfEntity.getValidDataMinCnt() > this.validTargetData.length)
			sb.append("Counter of valid target data is less than " + nnModelConfEntity.getValidDataMinCnt() + ". \n");
		
		if(sb.length() > 0) {
			this.noticeHistoryRepository.insertAll(CommonConst.AlarmNameNNMODEL + " (" + nnModelConfEntity.getNnModelEntity().getModelNm() + ")", "S", "E", sb.substring(0, sb.length() - 1));
			isValid = false;
		}
		
		return isValid;
	}
}
