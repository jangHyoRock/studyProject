package dhi.optimizer.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import dhi.common.exception.InvalidParameterException;
import dhi.optimizer.enumeration.MetalTempTubeType;
import dhi.optimizer.enumeration.NNTrainDataIOType;
import dhi.optimizer.model.PsoMVInfoVO;
import dhi.optimizer.model.db.PSOEffectPerItemEntity;
import dhi.optimizer.model.db.PSOResultEntity;
import dhi.optimizer.model.db._PSOResultEntity;
import dhi.optimizer.model.json.AlgorithOutputControllerConfig;
import dhi.optimizer.model.json.AlgorithmNNConfig;
import dhi.optimizer.model.json.AlgorithmPSOConfig;
import dhi.optimizer.model.json.AlgorithmProcessStatus;
import dhi.optimizer.model.json.AlgorithmSolutionStatus;
import dhi.optimizer.model.json.Chart;
import dhi.optimizer.model.json.KeyValue;
import dhi.optimizer.model.json.Navigator;
import dhi.optimizer.model.json.NavigatorConfig;
import dhi.optimizer.model.json.NavigatorLimitsAndFireBallCenter;
import dhi.optimizer.model.json.NavigatorWallMatchingTable;
import dhi.optimizer.model.json.NavigatorTubeWallMetalTemp;

public interface AlgorithmService {

	public void uploadNNModel(InputStream modelInputStream) throws InvalidParameterException;
	
	public Chart getTrendNNModelData(String startDateString, String endDateString) throws InvalidParameterException;
	
	public List<String> getCSVDownLoadNNModelData(NNTrainDataIOType nnTrainDataIOType, String startDateString, String endDateString) throws InvalidParameterException;
	
	public String getXmlDownLoadNNModel(String modelType, String timestamp) throws InvalidParameterException;
	
	public Navigator getNavigatorInfo();
	
	public void saveNavigatorInfo(Navigator navigator);
		
	public NavigatorLimitsAndFireBallCenter getNavigatorLimitAndFireBallCenter();
	
	public void saveNavigatorLimitAndFireBallCenter(NavigatorLimitsAndFireBallCenter limitsAndFireBallCenter);
	
	public NavigatorConfig getNavigatorConfig();
	
	public void saveNavigatorConfig(NavigatorConfig navigatorConfig);
	
	public NavigatorWallMatchingTable getNavigatorWallMatchingTableInfo();
	
	public NavigatorTubeWallMetalTemp getNavigatorTubeWallMetalTempInfo(MetalTempTubeType metalTempTubeType);
	
	public void saveNavigatorWallMatchingTable(NavigatorWallMatchingTable spiralWallMatchingTable);
	
	public List<KeyValue> getNavigatorTrendCategoryDDL();
	
	public Chart getNavigatorTrendData(String item, String startDateString, String endDateString) throws InvalidParameterException;
	
	public AlgorithmNNConfig getAlgorithmNNConfig();
	
	public void saveAlgorithmNNConfig(AlgorithmNNConfig nnConfig) throws InvalidParameterException;
	
	public AlgorithmPSOConfig getAlgorithmPSOConfig();
	
	public void saveAlgorithmPSOConfig(AlgorithmPSOConfig psoConfig) throws InvalidParameterException;
	
	public AlgorithOutputControllerConfig getAlgorithOutputControllerConfig();
	
	public void saveAlgorithOutputControllerConfig(AlgorithOutputControllerConfig outputControllerConfig);
	
	public void saveAlgorithmPSOResult(PSOResultEntity psoResultEntity,List<PSOEffectPerItemEntity> psoEffectPerItemEntityList);
	
	public void saveAlgorithmPSOResult2(List<_PSOResultEntity> psoResultEntity, List<PSOEffectPerItemEntity> psoEffectPerItemEntityList);
	
	/**
	 * Output Controller 의 결과를 DCS에 보내기 위해  DB에 저장함.
	 * @param psoMVInfoMap MV명, MV탐색범위값, DCS에 보낼 Bias 값
	 * @param mvBiasTGTMap MV Bias TGT 값.
	 * @param isNewMVTGTYN PSO 최적화 값이 새로 생성되면 True, 아니면 False.
	 * @param pureTotalAirFlow DCS에 보낸 PureTotalAirFlow 값.
	 */
	public void saveOutputController(HashMap<String, PsoMVInfoVO> psoMVInfoMap, HashMap<String, Double> mvBiasTGTMap, boolean isNewMVTGTYN, double pureTotalAirFlow);
	
	/**
	 * Control Mode가 OL이면, DCS DMD 태그값을 0으로 초기화 한다.
	 * Control Mode가 OL이면, DCS에 0값으로 보내기 위함.
	 */	
	public void updateOutputControllerOpenLoopZeroBias();
	
	public AlgorithmSolutionStatus getAlgorithmSolutionStatus(String plantUnitId) throws InvalidParameterException;
	
	public AlgorithmProcessStatus getAlgorithmProcessStatus(String plantUnitId) throws InvalidParameterException;
}