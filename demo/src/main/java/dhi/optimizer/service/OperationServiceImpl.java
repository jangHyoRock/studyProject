package dhi.optimizer.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dhi.common.exception.InvalidParameterException;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.enumeration.ControllerModeStatus;
import dhi.optimizer.enumeration.LearningModeStatus;
import dhi.optimizer.enumeration.OptimizerFunctionModeStatus;
import dhi.optimizer.enumeration.PsoOptimizationFunctionModeStatus;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.model.db.ControlHistoryEntity;
import dhi.optimizer.model.db.SystemCheckEntity;
import dhi.optimizer.model.json.ItemStatus;
import dhi.optimizer.model.json.Operation;
import dhi.optimizer.repository.ControlHistoryRepository;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.repository.CtrDataOutputRepository;
import dhi.optimizer.repository.EffectCalculationRepository;
import dhi.optimizer.repository.SystemCheckRepository;
import dhi.optimizer.repository._NNModelExecStatusRepository;
import dhi.optimizer.schedule.executor.NNModelExecutor;

/*
 * Operation management service.
 */
@Service
@Transactional
public class OperationServiceImpl implements OperationService {
	
	static final Logger logger = LoggerFactory.getLogger(NNModelExecutor.class);
	static final String OptimizerFunctionMode = "Optimizer Function Mode";
	static final String LearningMode = "Learning Mode";
	static final String ControllerMode = "Controller Mode";
	static final String PsoOptimizerFunctionMode = "PSO Optimization Function Mode";
	static final String ControlReady = "Control Ready";
		
	@Autowired
	private ControlRepository controlRepository;
	
	@Autowired
	private ControlHistoryRepository controlHistoryRepository;
	
	@Autowired
	private SystemCheckRepository systemCheckRepository;
	
	@Autowired
	private EffectCalculationRepository effectCalculationRepository;
	
	@Autowired
	private CtrDataOutputRepository ctrDataOutputRepository;
	
	@Autowired
	private _NNModelExecStatusRepository nnModelExecStatusRepository;

	public Operation getOperationMode()	{
		Operation operation = new Operation();
		ControlEntity controlEntity = this.controlRepository.findControl();
		SystemCheckEntity systemCheckEntity = this.systemCheckRepository.findControl();
		
		ArrayList<ItemStatus> optimizeModeList = new ArrayList<ItemStatus>();
		optimizeModeList.add(new ItemStatus(OptimizerFunctionMode, OptimizerFunctionModeStatus.valueOf(controlEntity.getSystemStart().toString().toUpperCase()).getValue()));
		optimizeModeList.add(new ItemStatus(LearningMode, LearningModeStatus.valueOf(controlEntity.getLearningMode()).getValue()));
		optimizeModeList.add(new ItemStatus(ControllerMode, ControllerModeStatus.valueOf(controlEntity.getControlMode()).getValue()));
		optimizeModeList.add(new ItemStatus(PsoOptimizerFunctionMode, PsoOptimizationFunctionModeStatus.valueOf(controlEntity.getOptMode()).getValue()));
		optimizeModeList.add(new ItemStatus(ControlReady, String.valueOf(systemCheckEntity.getControlReady())));
		operation.setOptimizeModeList(optimizeModeList);
		
		return operation;
	}
	
	public void resetCumulatedCostSavingEffect() {
		effectCalculationRepository.resetCostSavingEffect();
	}
		
	public void saveOperationMode(ItemStatus optimizeMode) throws InvalidParameterException	{		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ControlEntity controlEntity = this.controlRepository.findControl();
		ControlHistoryEntity controlHistoryEntity = new ControlHistoryEntity(controlEntity.getSystemStart(), controlEntity.getControlMode(), controlEntity.getOptMode(), controlEntity.getLearningMode());
		controlHistoryEntity.setUserId(authentication.getName());
		
		switch (optimizeMode.getItem()) {
		case OptimizerFunctionMode:
			OptimizerFunctionModeStatus[] arrayOptimizerFunctionModeStatus = OptimizerFunctionModeStatus.values();
			OptimizerFunctionModeStatus currentOptimizerFunctionModeStatus = null;
			for (OptimizerFunctionModeStatus optimizerFunctionModeStatus : arrayOptimizerFunctionModeStatus) {
				if (optimizeMode.getStatus().equals(optimizerFunctionModeStatus.getValue())) {
					currentOptimizerFunctionModeStatus = optimizerFunctionModeStatus;
					break;
				}
			}

			if (currentOptimizerFunctionModeStatus == null) {
				throw new InvalidParameterException();
			}
			
			boolean isSystemStart = Boolean.parseBoolean(currentOptimizerFunctionModeStatus.name());
			if(isSystemStart)
				this.controlRepository.updateSystemStartAndTimeStamp(isSystemStart);
			else
			{
				this.controlRepository.updateSystemStartValueOff(isSystemStart, ControllerModeStatus.OL.name(), LearningModeStatus.OFF.name());
				
				// System OFF 인 경우, System Ready, Control Ready, Control Ready TM, AL => False로 변경(초기화). 
				this.systemCheckRepository.updateSetSystemReadyAndControlReadyFalse();
				
				// Control Output Data 값 변경, DCS HMI 상태변경을 위함.
				// 10SOLUTION_SYSTEM_FAULT_ON : 1, 10SOLUTION_CONTROL_READY : 0, 10SOLUTION_CONTROL_RUNNING : 0, 10SOLUTION_CONTROL_STOPPED : 1
				this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_SOLUTION_SYSTEM_FAULT_ON, 1);
				this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_SOLUTION_CONTROL_READY, 0);
				this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_SOLUTION_CONTROL_RUNNING, 0);
				this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_SOLUTION_CONTROL_STOPPED, 1);
				
				controlHistoryEntity.setControlMode(ControllerModeStatus.OL.name());
				controlHistoryEntity.setLearningMode(LearningModeStatus.OFF.name());
			}
			
			controlHistoryEntity.setSystemStart(Boolean.parseBoolean(currentOptimizerFunctionModeStatus.name()));
			
			break;
		case LearningMode:
			LearningModeStatus[] arrayLearningModeStatus = LearningModeStatus.values();
			LearningModeStatus currentLearningModeStatus = null;
			for (LearningModeStatus learningModeStatus : arrayLearningModeStatus) {
				if (optimizeMode.getStatus().equals(learningModeStatus.getValue())) {
					currentLearningModeStatus = learningModeStatus;
					break;
				}
			}

			if (currentLearningModeStatus == null) {
				throw new InvalidParameterException();
			}

			this.controlRepository.updateLearningModeAndTimeStamp(currentLearningModeStatus.name());
			controlHistoryEntity.setLearningMode(currentLearningModeStatus.name());
			
			// Learning Mode가 ON이 되면 All NNModel Executor를 다시 시작할 수 있도록  완료여부를 false로 변경한다.
			if (LearningModeStatus.ON.equals(currentLearningModeStatus)) {
				this.nnModelExecStatusRepository.updateNNModelExecutorAllEndInitialize();
			}
			
			break;
		case ControllerMode:
			ControllerModeStatus[] arrayControllerModeStatus = ControllerModeStatus.values();
			ControllerModeStatus currentControllerModeStatus = null;
			for (ControllerModeStatus controllerModeStatus : arrayControllerModeStatus) {
				if (optimizeMode.getStatus().equals(controllerModeStatus.getValue())) {
					currentControllerModeStatus = controllerModeStatus;
					break;
				}
			}

			if (currentControllerModeStatus == null) {
				throw new InvalidParameterException();
			}

			this.controlRepository.updateControlModeAndTimeStamp(currentControllerModeStatus.name());
			controlHistoryEntity.setControlMode(currentControllerModeStatus.name());
			
			// Control Output Data 값 변경, DCS HMI 상태변경을 위함. 
			// CTR_DATA_SOLUTION_CONTROL_RUNNING : 0, CTR_DATA_SOLUTION_CONTROL_STOPPED : 0
			int controlReadyRunningStatus = 0;
			int controlReadyStoppedStatus = 0;
			switch (currentControllerModeStatus) {
			case OL:
				controlReadyRunningStatus = 0;
				controlReadyStoppedStatus = 1;
				
				// Control Mode가 OL 인 경우,  Control Ready, Control Ready TM, AL => False로 변경(초기화). 
				this.systemCheckRepository.updateSetControlReadyFalse();
				this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_SOLUTION_CONTROL_READY, 0);
				break;
			case CL:
			
				controlReadyRunningStatus = 1;
				controlReadyStoppedStatus = 0;
				
				/* Control Mode가 CL 인 경우, CTR_DATA_OUTPUT_BIAS_INITIALIZE 값을  1로 변경한다.
				 * Run 인 경우 새로운 PSO결과로 Output Bias 값이 생성되기 전까지는  Output 되지 않게 하기 위한 값 설정.
				 * CTR_DATA_OUTPUT_BIAS_INITIALIZE 값이 0이 되는 시점은 OutputController에서 새로운 Output 값이 생성된 경우 0으로 변경함.
				*/
				this.ctrDataOutputRepository.updateCtrDataOutputByTagIdAndTagValDifferent(CommonConst.CTR_DATA_OUTPUT_BIAS_INITIALIZE, 1);
				
				/*
				 *  - Modbus Output update - Burner, OFA Bias 값 을 0으로 초기화 한다.
				 *  - Modbus Output update - Modbus Current Input O2 Bias 값을  Modbus Output Total Air Bias으로 변경하여 초기화 한다.
				 */
				this.ctrDataOutputRepository.updateCtrDataOutputDamperBiasInitialize();
				this.ctrDataOutputRepository.updateCtrDataOutputTotAirBiasInitialize();
				break;
			}
			
			this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_SOLUTION_CONTROL_RUNNING, controlReadyRunningStatus);
			this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_SOLUTION_CONTROL_STOPPED, controlReadyStoppedStatus);
			
			break;
		case PsoOptimizerFunctionMode:
			PsoOptimizationFunctionModeStatus[] arrayPsoOptimizationFunctionModeStatus = PsoOptimizationFunctionModeStatus
					.values();
			PsoOptimizationFunctionModeStatus currentPsoOptimizationFunctionModeStatus = null;
			for (PsoOptimizationFunctionModeStatus psoOptimizationFunctionModeStatus : arrayPsoOptimizationFunctionModeStatus) {
				if (optimizeMode.getStatus().equals(psoOptimizationFunctionModeStatus.getValue())) {
					currentPsoOptimizationFunctionModeStatus = psoOptimizationFunctionModeStatus;
					break;
				}
			}

			if (currentPsoOptimizationFunctionModeStatus == null) {
				throw new InvalidParameterException();
			}

			this.controlRepository.updateOptModeAndTimeStamp(currentPsoOptimizationFunctionModeStatus.name());
			controlHistoryEntity.setOptMode(currentPsoOptimizationFunctionModeStatus.name());
			
			// Control Output Data 값 변경, DCS HMI 상태변경을 위함. 
			// 10PROFIT_MODE_SELECTED : 0, 10EMISSION_MODE_SELECTED : 0, 10EQUIPMENT_MODE_SELECTED : 0
			int profitStatus = 0;
			int emissionStatus = 0;
			int equipmentStatus = 0;
			switch (currentPsoOptimizationFunctionModeStatus) {
			case P:
				profitStatus = 1;
				emissionStatus = 0;
				equipmentStatus = 0;
				break;
			case E:
				profitStatus = 0;
				emissionStatus = 1;
				equipmentStatus = 0;
				break;
			case S:
				profitStatus = 0;
				emissionStatus = 0;
				equipmentStatus = 1;
				break;
			}
			
			this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_PROFIT_MODE_SELECTED , profitStatus);
			this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_EMISSION_MODE_SELECTED, emissionStatus);
			this.ctrDataOutputRepository.updateCtrDataOutputByTagId(CommonConst.CTR_DATA_EQUIPMENT_MODE_SELECTED, equipmentStatus);				
						
			break;
		default:
			throw new InvalidParameterException();
		}
		
		this.controlHistoryRepository.insertAll(controlHistoryEntity.getSystemStart(), controlHistoryEntity.getControlMode(), controlHistoryEntity.getOptMode(), controlHistoryEntity.getLearningMode(), controlHistoryEntity.getUserId());
	}
}
