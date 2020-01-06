package dhi.optimizer.schedule.executor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;

import dhi.optimizer.algorithm.common.LearningInfo;
import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;
import dhi.optimizer.algorithm.neuralNet.SupervisedLearningProcessor;
import dhi.optimizer.algorithm.neuralNet.SupervisedLearningService;
import dhi.optimizer.algorithm.neuralNet.SupervisedLearnings;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.enumeration.LearningModeStatus;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.model.db.NNConfigEntity;
import dhi.optimizer.model.db.NNModelBaseEntity;
import dhi.optimizer.model.db.NNModelEntity;
import dhi.optimizer.repository.ControlHistoryRepository;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.repository.NNConfigRepository;
import dhi.optimizer.repository.NNModelBaseRepository;
import dhi.optimizer.repository.NNModelRepository;
import dhi.optimizer.repository.NNTrainInRepository;
import dhi.optimizer.repository.NNTrainOutRepository;
import dhi.optimizer.repository.NoticeHistoryRepository;

/**
 * NN Model Scheduler. <br>
 * : Train Input, Output Tag 정보를 10분 주기로 읽어 Model를 생성함. 
 */
@Service
public class NNModelExecutor extends ScheduleExecutor {
	
	private static final Logger logger = LoggerFactory.getLogger(NNModelExecutor.class.getSimpleName());
		
	@Autowired
	private ControlRepository controlRepository;
	
	@Autowired
	private ControlHistoryRepository controlHistoryRepository;
	
	@Autowired
	private NNConfigRepository nnConfigRepository;
	
	@Autowired
	private NNModelRepository nnModelRepository;
	
	@Autowired
	private NNModelBaseRepository nnModelBaseRepository;
		
	@Autowired
	private NNTrainInRepository nnTrainInRepository;
	
	@Autowired
	private NNTrainOutRepository nnTrainOutRepository;
	
	@Autowired
	private NoticeHistoryRepository noticeHistoryRepository;
	
	private SupervisedLearningService nnLearningService = null;	
	private LearningInfo targetTrainInfo;	
	private double[][] trainInputData, trainTargetData, validInputData, validTargetData;
	
	public NNModelExecutor() {}
	
	public NNModelExecutor(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);		
	}
	
	/**
	 * NNModelExecutor 시작함수.
	 */
	@Override
	public void start() {
		logger.info("### " + NNModelExecutor.class.getName() + " Start ###");
		try {
			if (this.isNNModelEnable()) {
				this.nnModelModule();
			} else {
				logger.info("### Use of " + NNModelExecutor.class.getName() + " is false. ###");
			}
		} catch (Exception e) {
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			logger.info("### " + NNModelExecutor.class.getName() + " End ###");
		}
	}
	
	/**
	 * NN Model 생성이 가능한 상태인지를 확인하는 함수. <br>
	 * @return NNModel 수행 가능 상태여부.(true, false)
	 */
	private boolean isNNModelEnable() {
		boolean isEnable = false;
		ControlEntity controlEntity = this.controlRepository.findControl();

		LearningModeStatus learningMode = (LearningModeStatus) LearningModeStatus.valueOf(controlEntity.getLearningMode());
		if (LearningModeStatus.ON == learningMode 
				|| LearningModeStatus.SO == learningMode
				|| LearningModeStatus.SAO == learningMode) {
			isEnable = true;
		}

		return isEnable;
	}
	
	/**
	 * NN Model 생성 함수.
	 */
	private void nnModelModule() {
		NNConfigEntity nnConfigEntity = this.nnConfigRepository.findByConfId(NNConfigEntity.defaultID);

		// Train Input, Output Tag Load. 
		this.loadNNModelInputData(nnConfigEntity);
		this.loadNNModelTargetData(nnConfigEntity);

		// Train Data 가 유효한지 체크함.
		if (!this.nnModelDataValidityCheck(nnConfigEntity))
			return;

		// Train 하기 위한 설정값 Setting.
		this.targetTrainInfo = new LearningInfo();
		this.targetTrainInfo.setLearningRate(nnConfigEntity.getLearningRate());
		this.targetTrainInfo.setMomentum(nnConfigEntity.getMomentum());
		this.targetTrainInfo.setSigmoidAlphaValue(nnConfigEntity.getSigmoidAlphaVal());

		List<Integer> neuronsInLayer = this.targetTrainInfo.getNeuronsInLayer();
		neuronsInLayer.set(0, nnConfigEntity.getNeuronCntLayer1());
		neuronsInLayer.set(1, nnConfigEntity.getNeuronCntLayer2());

		this.targetTrainInfo.setNeuronsInLayer(neuronsInLayer);
		this.targetTrainInfo.setIterations(nnConfigEntity.getIterations());
		this.targetTrainInfo.setMu(nnConfigEntity.getMu());
		this.targetTrainInfo.setMse(nnConfigEntity.getTrainingMSE());
		this.targetTrainInfo.setValidMse(nnConfigEntity.getValidationMSE());
		this.targetTrainInfo.setValidationFailedCount(nnConfigEntity.getValidationCheck());

		this.nnLearningService = new SupervisedLearningProcessor();
		this.nnLearningService.setTestFileId(new SimpleDateFormat("yyyymmddHHmm").format(new Date()));
		
		if ("Backpropagation".equals(nnConfigEntity.getTrainingAlgorithm()))
			this.nnLearningService.setSelectedlearningAlgorithm(SupervisedLearnings.BackPropagation);
		else if ("ResilientBackpropagation".equals(nnConfigEntity.getTrainingAlgorithm()))
			this.nnLearningService.setSelectedlearningAlgorithm(SupervisedLearnings.ResilientBackpropagation);
		else
			this.nnLearningService.setSelectedlearningAlgorithm(SupervisedLearnings.BackPropagation);

		this.nnLearningService.setTargetTrainInfo(targetTrainInfo);
		this.nnLearningService.intialize(trainInputData, trainTargetData, validInputData, validTargetData);
		this.nnLearningService.execute();

		// New NN model add (0. index)
		List<ActivationNetwork> networkList = new ArrayList<ActivationNetwork>();
		networkList.add(this.nnLearningService.getAvailableNetwork());
		
		// Base NN model load.
		List<NNModelBaseEntity> nnModelBaseEntityList = this.nnModelBaseRepository.findByTrainStatus(true);
		for (NNModelBaseEntity nnModelBaseEntity : nnModelBaseEntityList) {
			networkList.add((ActivationNetwork) Utilities.deSerialize(nnModelBaseEntity.getModel()));
		}

		// History NN model load.
		List<NNModelEntity> nnModelEntityList = this.nnModelRepository.findTop3ByTrainStatusOrderByTimestampDesc(true);
		for (NNModelEntity nnModelEntity : nnModelEntityList) {
			networkList.add((ActivationNetwork) Utilities.deSerialize(nnModelEntity.getModel()));
		}

		// Get the criteria NNModel for the NN Model conformance test.
		// The newly generated model is compared with the reference model and the
		// conformity is verified.
		NNModelEntity nnModelEntity = new NNModelEntity();
		boolean isUseNNModel = nnLearningService.verification(validInputData, validTargetData, networkList);
		if (isUseNNModel) {
			nnModelEntity.setTrainStatus(true);
			this.noticeHistoryRepository.insertAll(CommonConst.AlarmNameNNMODEL, "S", "E", "The NN Model has been updated successfully.");
		} else
			nnModelEntity.setTrainStatus(false);

		nnModelEntity.setTimestamp(new Date());
		nnModelEntity.setIteration(nnLearningService.getTrainIterationCount());
		nnModelEntity.setTrainErrorRate(nnLearningService.getMinTrainMseAvg());
		nnModelEntity.setValidErrorRate(nnLearningService.getMinValidMseAvg());
		nnModelEntity.setModel(Utilities.serialize(nnLearningService.getAvailableNetwork()));
		this.nnModelRepository.saveAndFlush(nnModelEntity);

		ControlEntity controlEntity = this.controlRepository.findControl();
		LearningModeStatus learningMode = (LearningModeStatus) LearningModeStatus.valueOf(controlEntity.getLearningMode());
		if (LearningModeStatus.ON == learningMode) {
			this.controlRepository.updateLearningModeAndTimeStamp(LearningModeStatus.OFF.name());
			this.controlHistoryRepository.insertAll(controlEntity.getSystemStart(), controlEntity.getControlMode(),	controlEntity.getOptMode(), LearningModeStatus.OFF.name(), NNModelExecutor.class.getName());
		}
	}

	/**
	 * Train Input Tag 정보를 읽어서 Train, Valid Input Data를 생성함.
	 * @param nnConfigEntity NNModel Config 정보.
	 */
	private void loadNNModelInputData(NNConfigEntity nnConfigEntity) {
		List<String> tempTrainData = new ArrayList<String>();
		List<String> tempValidData = new ArrayList<String>();

		List<Object[]> nnTrainDataList = this.nnTrainInRepository.findByTimestampLastNNTrainInputDataNativeQuery(nnConfigEntity.getTrainDataTime());
		this.getNNModelData(nnTrainDataList, nnConfigEntity.getValidDataTime(), tempTrainData, tempValidData);

		this.trainInputData = Utilities.convertRowsAndCommaColumnsToDouble(tempTrainData);
		this.validInputData = Utilities.convertRowsAndCommaColumnsToDouble(tempValidData);
	}
	
	/**
	 * Train Target(Output) Tag 정보를 읽어서 Train, Valid Output Data를 생성함.
	 * @param nnConfigEntity NNModel Config 정보.
	 */
	private void loadNNModelTargetData(NNConfigEntity nnConfigEntity) {
		List<String> tempTrainData = new ArrayList<String>();
		List<String> tempValidData = new ArrayList<String>();

		List<Object[]> nnTrainDataList = this.nnTrainOutRepository.findByTimestampLastNNTrainOutputDataNativeQuery(nnConfigEntity.getTrainDataTime());
		this.getNNModelData(nnTrainDataList, nnConfigEntity.getValidDataTime(), tempTrainData, tempValidData);

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
	private boolean nnModelDataValidityCheck(NNConfigEntity nnConfigEntity)	{
		boolean isValid = true;
		
		StringBuilder sb = new StringBuilder();		
		if(nnConfigEntity.getNeuronCntLayer1() <= 0)
			sb.append("Count of layer 1 is zero. \n");

		if(nnConfigEntity.getNeuronCntLayer2() <= 0)
			sb.append("Count of layer 2 is zero. \n");

		if(nnConfigEntity.getIterations() <= 0)
			sb.append("Iterations is zero. \n");
		
		if(this.trainInputData.length <= 0
				|| this.trainTargetData.length <= 0 
				|| this.validInputData.length <= 0
				|| this.validTargetData.length <= 0)
			sb.append("Training data is zero. \n");
		
		if(nnConfigEntity.getTrainDataMinCnt() > this.trainInputData.length)
			sb.append("Counter of training input data is less than " + nnConfigEntity.getTrainDataMinCnt() + ". \n");
		
		if(nnConfigEntity.getTrainDataMinCnt() > this.trainTargetData.length)
			sb.append("Counter of training target data is less than " + nnConfigEntity.getTrainDataMinCnt() + ". \n");
		
		if(nnConfigEntity.getValidDataMinCnt() > this.validInputData.length)
			sb.append("Counter of valid input data is less than " + nnConfigEntity.getValidDataMinCnt() + ". \n");
		
		if(nnConfigEntity.getValidDataMinCnt() > this.validTargetData.length)
			sb.append("Counter of valid target data is less than " + nnConfigEntity.getValidDataMinCnt() + ". \n");
		
		if(sb.length() > 0) {
			this.noticeHistoryRepository.insertAll(CommonConst.AlarmNameNNMODEL, "S", "E", sb.substring(0, sb.length() - 1));
			isValid = false;
		}
		
		return isValid;
	}
}
