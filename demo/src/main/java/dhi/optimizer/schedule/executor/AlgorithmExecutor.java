package dhi.optimizer.schedule.executor;

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
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;

import dhi.optimizer.algorithm.common.DoubleRange;
import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;
import dhi.optimizer.algorithm.neuralNet.SupervisedLearningProcessor;
import dhi.optimizer.algorithm.pso.PsoCalculationFunction;
import dhi.optimizer.algorithm.pso.PsoMV;
import dhi.optimizer.algorithm.pso.PsoService;
import dhi.optimizer.algorithm.pso.PsoTag;
import dhi.optimizer.algorithm.pso.PsoMV.MVType;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.enumeration.FireBallCenterType;
import dhi.optimizer.enumeration.MetalTempTubeType;
import dhi.optimizer.enumeration.NNTrainDataIOType;
import dhi.optimizer.enumeration.PsoOptimizationFunctionModeStatus;
import dhi.optimizer.model.NavigatorTubeWWTempVO;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.model.db.NNConfigEntity;
import dhi.optimizer.model.db.NNModelBaseEntity;
import dhi.optimizer.model.db.NNModelEntity;
import dhi.optimizer.model.db.NNTrainDataConfEntity;
import dhi.optimizer.model.db.NavCfdEntity;
import dhi.optimizer.model.db.NavConfEntity;
import dhi.optimizer.model.db.NavSettingEntity;
import dhi.optimizer.model.db.NavWWMatchingEntity;
import dhi.optimizer.model.db.OutputMVTGTEntity;
import dhi.optimizer.model.db.PSOConfigEntity;
import dhi.optimizer.model.db.PSOEffectPerItemEntity;
import dhi.optimizer.model.db.PSOMVInfoEntity;
import dhi.optimizer.model.db.PSOResultEntity;
import dhi.optimizer.model.json.KeyValue;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.repository.NNConfigRepository;
import dhi.optimizer.repository.NNModelBaseRepository;
import dhi.optimizer.repository.NNModelRepository;
import dhi.optimizer.repository.NNTrainDataConfRepository;
import dhi.optimizer.repository.NNTrainInRepository;
import dhi.optimizer.repository.NNTrainOutRepository;
import dhi.optimizer.repository.NavCfdRepository;
import dhi.optimizer.repository.NavConfRepository;
import dhi.optimizer.repository.NavSettingRepository;
import dhi.optimizer.repository.NavTubeWWTempConfRepository;
import dhi.optimizer.repository.NavWWMatchingRepository;
import dhi.optimizer.repository.OPDataInputRepository;
import dhi.optimizer.repository.OutputMVTGTRepository;
import dhi.optimizer.repository.PSOConfigRepository;
import dhi.optimizer.repository.PSOMVInfoRepository;
import dhi.optimizer.repository.SystemCheckRepository;
import dhi.optimizer.service.AlgorithmService;
import dhi.optimizer.service.CommonDataService;

/**
 * Algorithm Executor Scheduler. <br> 
 * : Navigator & PSO Algorithm을 5분 주기로 수행한다.
 */
@Service
public class AlgorithmExecutor extends ScheduleExecutor {

	private enum UseModelType {
		Base, Learning
	}
	
	private static final Logger logger = LoggerFactory.getLogger(AlgorithmExecutor.class.getSimpleName());
	
	private static final String PSO_NAVIGATOR_RUN_MODE = "CLA";
	
	private static final double NAVIGATOR_MIN_MAX_LIMIT = 0.1;
	
	@Autowired
	private ControlRepository controlRepository;

	@Autowired
	private NNConfigRepository nnConfigRepository;
	
	@Autowired
	private NNModelRepository nnModelRepository;
	
	@Autowired
	private NNModelBaseRepository nnModelBaseRepository;

	@Autowired
	private NNTrainDataConfRepository nnTrainDataConfRepository;

	@Autowired
	private NNTrainInRepository nnTrainInRepository;
	
	@Autowired
	private NNTrainOutRepository nnTrainOutRepository;
	
	@Autowired
	private OPDataInputRepository opDataInputRepository;

	@Autowired
	private PSOConfigRepository psoConfigRepository;

	@Autowired
	private PSOMVInfoRepository psoMVInfoRepository;

	@Autowired
	private SystemCheckRepository systemCheckRepository;

	@Autowired
	private CommonDataService commonDataService;

	@Autowired
	private NavConfRepository navConfRepository;
	
	@Autowired
	private NavSettingRepository navSettingRepository;
	
	@Autowired
	private NavWWMatchingRepository navWWMatchingRepository;
	
	@Autowired 
	private NavTubeWWTempConfRepository navTubeWWTempConfRepository;

	@Autowired
	private NavCfdRepository navCfdRepository;
	
	@Autowired
	private OutputMVTGTRepository outputMVTGTRepository;
	
	@Autowired
	private AlgorithmService algorithmService;
	
	@Value("${algorithm.pso.computer.processing.unit}")
	private String processingUnit;
	
	@Value("${algorithm.pso.computer.processing.unit.gpu.swarmsize}")
	private int gpuUseSwarmsize;
	
	private List<PSOMVInfoEntity> psoMVInfoEntityList;
	private HashMap<String, String> navigatorMap;	
	private PSOConfigEntity psoConfigEntity;
	
	public AlgorithmExecutor() {
	}

	public AlgorithmExecutor(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);
	}

	/**
	 * AlgorithmExecutor 시작함수.
	 */
	@Override
	public void start() {
		logger.info("### " + AlgorithmExecutor.class.getName() + " Start ###");
		try {
			
			// PSO MV Info Load
			this.psoMVInfoEntityList = this.psoMVInfoRepository.findAll();
			
			// PSO Config Load
			this.psoConfigEntity = this.psoConfigRepository.findByConfId(PSOConfigEntity.defaultID);
			
			// Navigator Up, Down 의 값을 담는  Map 생성.
			this.navigatorMap = new HashMap<String, String>();
			
			// PSO Config의 CL 모드의 값이 'CLA'이면 Navigator 알고리즘을 수행한다. 
			if(PSO_NAVIGATOR_RUN_MODE.equals(this.psoConfigEntity.getPsoCLMode()))
				this.navigatorAlgorithm();
			
			// PSO 알고리즘 수행.
			this.PSOAlgorithm();
			
		} catch (Exception e) {
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			logger.info("### " + AlgorithmExecutor.class.getName() + " End ###");
		}
	}
	
	/**
	 * Navigator Algorithm Function. <br>
	 * 1. FireBallCentering, SprayBalancing, EmissionBalancing Bad 체크 함. <br>
	 * 2. FireBallCentering Bad 인 경우 - Burner(CN1 ~ CN4) 조정. <br>
	 * 3. SprayBalancing Bad 인 경우 - OFA(Front, Rear, Left, Right 조정) <br>
	 * : CFD 가 있는 경우 추가 조정. <br>
	 * 4. EmissionBalancing Bad 인 경우, Sasan 로직없음. <br>
	 */
	private void navigatorAlgorithm() {
		
		// Spiral Water Wall의 온도 정보를 가져온다.
		List<Object[]> navTubeWWTempList = this.navTubeWWTempConfRepository.getNavTubeWWTempList(MetalTempTubeType.Spiral.name());
		List<Double> frontTempList = new ArrayList<Double>();
		List<Double> rearTempList = new ArrayList<Double>();
		List<Double> leftTempList = new ArrayList<Double>();
		List<Double> rightTempList = new ArrayList<Double>();
		for (Object[] objectTempArray : navTubeWWTempList) {
			Object objTempVal = objectTempArray[2];
			String direction = String.valueOf(objectTempArray[0]);
			Double tempVal = null;
			if (objTempVal != null) {
				tempVal = (double) objTempVal;
			}

			switch (direction) {
			case "FRONT":
				frontTempList.add(tempVal);
				break;
			case "REAR":
				rearTempList.add(tempVal);
				break;
			case "LEFT":
				leftTempList.add(tempVal);
				break;
			case "RIGHT":
				rightTempList.add(tempVal);
				break;
			}
		}
		
		// Spiral Water Wall의 평균값을 구한다.
		NavSettingEntity navSettingEntity = this.navSettingRepository.findBySettingId(NavSettingEntity.defaultID);
		NavigatorTubeWWTempVO tubeWWTempVO = new NavigatorTubeWWTempVO(frontTempList, rearTempList, leftTempList, rightTempList, navSettingEntity.getMetalTempDevRate(), navSettingEntity.getAllowableMetalTemp());

		// 최신 OP Data를 가져옴.
		HashMap<String, Double> opDataMap = this.commonDataService.getOpDataMap();
		
		// 1 FireBallCenteringBad, 2 SprayBalancingBad, 3 EmissionBalancingBad 우선 순위에 의해서 Bad에 따른 Process를 처리함.
		boolean isNavigatorBad = false;
		List<NavConfEntity> navConfEntityList = this.navConfRepository.findAll();		
		if (isNavigatorBad = this.isFireBallCenteringBadCheck(navConfEntityList, tubeWWTempVO))
			this.fireBallCenteringProcess(tubeWWTempVO);
		else if (isNavigatorBad = this.isSprayBalancingBadCheck(navConfEntityList, opDataMap))
			this.sprayBalancingProcess(opDataMap, navSettingEntity.getAllowableFlueGasTempDev(), navSettingEntity.getAllowableSprayFlowDev());
		else if (isNavigatorBad = this.isEmissionBalancingBadCheck(navConfEntityList, opDataMap))
			this.emissionBalancingProcess(opDataMap);

		// Navigator Bad가 아닌 경우, Optimization Mode에 따라서 Process 처리함.
		// Optimization Mode - Profit (SprayBalancing, EmissionBalancing)
		// Optimization Mode - Equipment (SprayBalancing)
		// Optimization Mode - Emission (EmissionBalancing)
		if (!isNavigatorBad) {
			ControlEntity controlEntity = this.controlRepository.findControl();
			PsoOptimizationFunctionModeStatus optMode = PsoOptimizationFunctionModeStatus.valueOf(controlEntity.getOptMode());
			if (PsoOptimizationFunctionModeStatus.P.equals(optMode)) {
				this.sprayBalancingProcess(opDataMap, navSettingEntity.getAllowableFlueGasTempDev(), navSettingEntity.getAllowableSprayFlowDev());
				this.emissionBalancingProcess(opDataMap);
			} else if (PsoOptimizationFunctionModeStatus.S.equals(optMode)) {
				this.sprayBalancingProcess(opDataMap, navSettingEntity.getAllowableFlueGasTempDev(), navSettingEntity.getAllowableSprayFlowDev());
			} else if (PsoOptimizationFunctionModeStatus.E.equals(optMode)) {
				this.emissionBalancingProcess(opDataMap);
			}
		}
	}
	
	/**
	 * Fire Ball Centering Bad Check.
	 */
	private boolean isFireBallCenteringBadCheck(List<NavConfEntity> navConfEntityList, NavigatorTubeWWTempVO tubeWWTempVO) {
		boolean isFireBallCenteringBad = false;
		double navConfEVATubeTempHigh = 0.0;
		double navConfEVATubeTempMargin = 0.0;
		
		List<NavConfEntity> tempNavConfEntityList = navConfEntityList.stream()
				.filter(n -> n.getUseYn() == true && "F".equals(n.getItemGroup()))
				.collect(Collectors.toList());
		
		for (NavConfEntity navConfEntity : tempNavConfEntityList) {
			navConfEVATubeTempHigh = navConfEntity.getHighVal();
			navConfEVATubeTempMargin = navConfEntity.getMarginVal();
			
			// 'spiral_water_tube_temp' 인 것만  적용함. - 2018.12.11
			if ("spiral_water_tube_temp".equals(navConfEntity.getItemId())) {
				// 단면 평균 값이 Setting 값을 넘는 경우 Bad = true;
				double evTuveTemp = navConfEVATubeTempHigh - navConfEVATubeTempMargin;
				if (tubeWWTempVO.getFrontTempAvgNoFilter() > evTuveTemp
						|| tubeWWTempVO.getRearTempAvgNoFilter() > evTuveTemp
						|| tubeWWTempVO.getLeftTempAvgNoFilter() > evTuveTemp
						|| tubeWWTempVO.getRightTempAvgNoFilter() > evTuveTemp) {
					isFireBallCenteringBad = true;
					break;
				}
			}
		}
		
		return isFireBallCenteringBad;
	}
	
	/**
	 * Fire Ball Centering Up, Down.
	 */
	private void fireBallCenteringProcess(NavigatorTubeWWTempVO tubeWWTempVO)	{
		
		FireBallCenterType fireBallCenterType = FireBallCenterType.TubeWallTemperature;
		if (FireBallCenterType.TubeWallTemperature.equals(fireBallCenterType)) {
			
			// Navigator WEB 화면에서는 'aa', 'ab~fg', 'gg'를  분리하지만, 알고리즘에서는  'ab~fg' 항목으로 PSO MV CN1 ~ CN4 까지 적용함. (DeviationPriority 방식만 사용함)
			List<KeyValue> deviationPriorityList = tubeWWTempVO.getDeviationPriorityList();
			Optional<NavWWMatchingEntity> navWWMatchingEntity = Optional.empty();
			List<NavWWMatchingEntity> navWWMatchingEntityList = this.navWWMatchingRepository.findAll();
			if (deviationPriorityList != null) {
				navWWMatchingEntity = navWWMatchingEntityList.stream()
						.filter(t -> t.getNavWWMatchingIdEntity().getP1().equals(deviationPriorityList.get(0).getKey())
								&& t.getNavWWMatchingIdEntity().getP2().equals(deviationPriorityList.get(1).getKey())
								&& t.getNavWWMatchingIdEntity().getP3().equals(deviationPriorityList.get(2).getKey())
								&& t.getNavWWMatchingIdEntity().getP4().equals(deviationPriorityList.get(3).getKey())
								&& t.getNavWWMatchingIdEntity().getType().equals("ab_fg"))
						.findFirst();
			}
			
			if (navWWMatchingEntity.isPresent()) {
				// CN1 ~ CN4 별로 설정된 Up, Down 등록함.
				for (PSOMVInfoEntity psoMVInfoEntity : psoMVInfoEntityList) {
					MVType mvType = Enum.valueOf(MVType.class, psoMVInfoEntity.getPsoMVType());

					switch (mvType) {
					case Burner:
						if ("CN1".equals(psoMVInfoEntity.getPsoMV().toUpperCase()))
							this.navigatorMap.put(psoMVInfoEntity.getPsoMV(), navWWMatchingEntity.get().getC1());
						else if ("CN2".equals(psoMVInfoEntity.getPsoMV().toUpperCase()))
							this.navigatorMap.put(psoMVInfoEntity.getPsoMV(), navWWMatchingEntity.get().getC2());
						else if ("CN3".equals(psoMVInfoEntity.getPsoMV().toUpperCase()))
							this.navigatorMap.put(psoMVInfoEntity.getPsoMV(), navWWMatchingEntity.get().getC3());
						else if ("CN4".equals(psoMVInfoEntity.getPsoMV().toUpperCase()))
							this.navigatorMap.put(psoMVInfoEntity.getPsoMV(), navWWMatchingEntity.get().getC4());
					case OFA:
					case Air:
						break;
					}
				}
			}
		}
		else {
			/*
			// TODO : 현재 구현 되지 않음. 아래 코드는 향우 만들어질 예저 코드임.
			double[][] fireTempLocation = new double[9][9];
			Random rnd = new Random();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					fireTempLocation[i][j] = rnd.nextInt(100);
				}
			}

			// find max value
			int maxX = 0;
			int maxY = 0;
			double currentValue = 0;
			double maxValue = 0;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					currentValue = fireTempLocation[i][j];
					if (maxValue < currentValue) {
						maxX = j;
						maxY = i;
						maxValue = currentValue;
					}
				}
			}

			// find direction
			int xLength = fireTempLocation.length;
			int yLength = fireTempLocation[0].length;

			String direction = "";
			if (xLength - maxX > 0)
				direction += "W";
			else if (xLength - maxX < 0)
				direction += "E";

			if (yLength - maxY > 0)
				direction += "N";
			else if (yLength - maxY < 0)
				direction += "S";

			NavFireBallEntity navFireBallEntity = navFireBallRepository.findByTypeAndDirection(fireBallCenterType.name(), direction);
			if (navFireBallEntity != null) {
				// True 인 경우 유량을 증가시킴
				// Corner1 => BNR1
				if (navFireBallEntity.isCorner1()) {

				}

				// Corner2 => BNR2
				if (navFireBallEntity.isCorner2()) {

				}

				// Corner3 => BNR3
				if (navFireBallEntity.isCorner3()) {

				}

				// Corner4 => BNR4
				if (navFireBallEntity.isCorner4()) {

				}
			}*/				
		}
	}
	
	/**
	 * Spray Balancing Bad Check.
	 */
	private boolean isSprayBalancingBadCheck(List<NavConfEntity> navConfEntityList, HashMap<String, Double> opDataMap) {
		boolean isSprayBalancingBad = false;	

		List<NavConfEntity> tempNavConfEntityList = navConfEntityList.stream()
				.filter(n -> n.getUseYn() == true && "D".equals(n.getItemGroup()))
				.collect(Collectors.toList());
		
		for (NavConfEntity navConfEntity : tempNavConfEntityList) {
			// RH Spray 유량만 편차를 계산하고, SH Spray 유량은 편차계산을 하지 않음.
			// Final RH는 Primary RH Spray유량도 포함하여 편차를 계산함. - 2018.12.11
			if ("final_rh_dsh".equals(navConfEntity.getItemId())) {
				double rhMSprayFlowLeft = opDataMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_L_3);
				double rhESprayFlowLeft = opDataMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_L_3);
				double rhMSprayFlowRight = opDataMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_R_3);
				double rhESprayFlowRight = opDataMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_R_3);
				double sprayFlowDifferenceAbs = Math.abs((rhMSprayFlowLeft + rhESprayFlowLeft) - (rhMSprayFlowRight + rhESprayFlowRight));			
				if (sprayFlowDifferenceAbs > navConfEntity.getHighVal()) {
					isSprayBalancingBad = true;
					break;
				}
			}
		}

		return isSprayBalancingBad;
	}

	/*
	 * Spray Balancing Up Down. 사용안함. 
	 * 아래 로직 사용하지 않음. 사유: 2018-12-13 18:33분 새로운 로직으로 전달 받음. Allowable Flue Gas Temp. Dev(Deg),Allowable Spray Flow Dev(t/h) 사용함.	
	 */
	/*
	private void sprayBalancingProcess(HashMap<String, Double> opDataMap) {
		
		double leftRHSprayFlow = opDataMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_L_3);
		double rightRHSprayFlow = opDataMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_R_3);
		
		String ofaUpDown = "H";
		String rhCFDDirection = "HOLD";
		
		if (leftRHSprayFlow > rightRHSprayFlow) {
			ofaUpDown = "U";
			rhCFDDirection = "LEFT";
		} else if (leftRHSprayFlow < rightRHSprayFlow) {
			ofaUpDown = "D";
			rhCFDDirection = "RIGHT";
		}
		
		// OFA - Front, Rear, Left, Right에  Up, Down 등록함.
		for (PSOMVInfoEntity psoMVInfoEntity : psoMVInfoEntityList) {
			MVType mvType = Enum.valueOf(MVType.class, psoMVInfoEntity.getPsoMVType());
			switch (mvType) {
			case OFA:
				this.navigatorMap.put(psoMVInfoEntity.getPsoMV(), ofaUpDown);
				break;
			case Burner:
			case Air:
				break;
			}
		}
		
		// CFD DB 조회에 필요한  Mill(ON) 2진수 값을 구한다.
		int cfdMillValue = 0;
		try {
			double coalFeedRateTagVal = 0;
			String[] coalItemIds = new String[] { "G", "F", "E", "D", "C", "B", "A" };
			for (int i = 0; i < coalItemIds.length; i++) {
				coalFeedRateTagVal = opDataMap.get(CommonConst.class.getField("COAL_FEEDER_" + coalItemIds[i] + "_FEEDRATE").get(null));
				if (coalFeedRateTagVal >= 10.0) {
					cfdMillValue += Math.pow(2, i);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		 
		// CFD DB 조회에 필요한  부하(Load) 값을 가져온다.
		double load = opDataMap.get(CommonConst.ACTIVE_POWER_OF_GENERATOR);
		
		// 각 데이터 별로 +- 10% = 10/100  = 0.1 범위내의 데이터 탐색
		// CFD 해석 데이터가 있으면 UP 한다.
		// Burner은 우선순위 1개의 Up을 등록하여, OFA 의 Up, Down 중에서 CFD 해석의 우선순위에 나온 Up, Down가 서로 다른 경우 Hold 한다.
		String cfdControl = "U";
		NavCfdEntity navCfdEntity = this.navCfdRepository.getSprayBalancingCFDNativeQuery((int) load, String.valueOf(cfdMillValue), "00", 0.1, rhCFDDirection);
		if (navCfdEntity != null) {
			// CFD 우선순위에 해당하는 Burner Navigator 값이 없으면 Up 
			String burnerNavigator = this.navigatorMap.get(navCfdEntity.getBnrP1().toUpperCase());
			burnerNavigator = burnerNavigator == null ? CommonConst.StringEmpty : burnerNavigator;
			if (CommonConst.StringEmpty.equals(burnerNavigator))
				this.navigatorMap.put(navCfdEntity.getBnrP1().toUpperCase(), cfdControl);

			// CFD 우선순위에 해당하는 Ofa Navigator 값이 없으면 Up, 기존에 등록된 값이 있고 , 서로 값이 다르면 Hold
			String ofaNavigator = this.navigatorMap.get(navCfdEntity.getOfaP1().toUpperCase());
			ofaNavigator = ofaNavigator == null ? CommonConst.StringEmpty : ofaNavigator;
			if (CommonConst.StringEmpty.equals(ofaNavigator))
				this.navigatorMap.put(navCfdEntity.getOfaP1().toUpperCase(), cfdControl);
			else if (!cfdControl.equals(ofaNavigator))
				this.navigatorMap.put(navCfdEntity.getOfaP1().toUpperCase(), "H");
		}
	}*/	

	/**
	 * Spray Balancing Up Down.
	 */
	private void sprayBalancingProcess(HashMap<String, Double> opDataMap, double allowableFlueGasTempDev, double allowableSprayFlowDev) {
		
		double leftFGTemp = opDataMap.get(CommonConst.HORIZON_GAS_DUCT_FLUE_GAS_T_L);
		double rightFGTemp = opDataMap.get(CommonConst.HORIZON_GAS_DUCT_FLUE_GAS_T_R);		
		String ofaUpDown = CommonConst.StringEmpty;
		if (Math.abs(leftFGTemp - rightFGTemp) > allowableFlueGasTempDev) {
			if (leftFGTemp > rightFGTemp) {
				ofaUpDown = "U";
			} else if (leftFGTemp < rightFGTemp) {
				ofaUpDown = "D";
			}
		}

		// OFA - Front, Rear, Left, Right에  Up, Down 등록함.
		for (PSOMVInfoEntity psoMVInfoEntity : psoMVInfoEntityList) {
			MVType mvType = Enum.valueOf(MVType.class, psoMVInfoEntity.getPsoMVType());
			switch (mvType) {
			case OFA:
				this.navigatorMap.put(psoMVInfoEntity.getPsoMV(), ofaUpDown);
				break;
			case Burner:
			case Air:
				break;
			}
		}
		
		// CFD DB 조회에 필요한  부하(Load) 값을 가져온다.
		double load = opDataMap.get(CommonConst.ACTIVE_POWER_OF_GENERATOR);
		
		// CFD DB 조회에 필요한  Mill(ON) 2진수 값을 구한다.
		int cfdMillValue = 0;
		try {
			double coalFeedRateTagVal = 0;
			String[] coalItemIds = new String[] { "G", "F", "E", "D", "C", "B", "A" };
			for (int i = 0; i < coalItemIds.length; i++) {
				coalFeedRateTagVal = opDataMap.get(CommonConst.class.getField("COAL_FEEDER_" + coalItemIds[i] + "_FEEDRATE").get(null));
				if (coalFeedRateTagVal >= 10.0) {
					cfdMillValue += Math.pow(2, i);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		// CFD 해석 데이터 조회
		// 각 데이터 별로 +- 10% 범위내의 데이터 탐색
		List<NavCfdEntity> navCfdEntityList = this.navCfdRepository.getSprayBalancingCFDNativeQuery((int) load,	String.valueOf(cfdMillValue), "00", 0.1);
		if (navCfdEntityList != null && navCfdEntityList.size() > 1) {
			
			// SA, OFA 우선순위 찾기.
			String cfdLeftSaP1 = CommonConst.StringEmpty;
			String cfdLeftOfaP1 = CommonConst.StringEmpty;
			String cfdRightSaP1 = CommonConst.StringEmpty;
			String cfdRightOfaP1 = CommonConst.StringEmpty;
			for (NavCfdEntity navCfdEntity : navCfdEntityList) {
				switch (navCfdEntity.getNavCfdIdEntity().getCol()) {
				case "LEFT":
					cfdLeftSaP1 = navCfdEntity.getBnrP1().toUpperCase();
					cfdLeftOfaP1 = navCfdEntity.getOfaP1().toUpperCase();
					break;
				case "RIGHT":
					cfdRightSaP1 = navCfdEntity.getBnrP1().toUpperCase();
					cfdRightOfaP1 = navCfdEntity.getOfaP1().toUpperCase();
					break;
				}
			}
			
			double leftRHMSprayFlowLeft = opDataMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_L_3);
			double leftRHESprayFlowLeft = opDataMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_L_3);
			double rightRHMSprayFlowRight = opDataMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_R_3);
			double rightRHESprayFlowRight = opDataMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_R_3);

			double leftRHSprayFlow = leftRHMSprayFlowLeft + leftRHESprayFlowLeft;
			double rightRHSprayFlow = rightRHMSprayFlowRight + rightRHESprayFlowRight;

			String cfdLeftSaD = CommonConst.StringEmpty;
			String cfdLeftOfaD = CommonConst.StringEmpty;
			String cfdRightSaD = CommonConst.StringEmpty;
			String cfdRightOfaD = CommonConst.StringEmpty;
			if (Math.abs(leftRHSprayFlow - rightRHSprayFlow) > allowableSprayFlowDev) {
				if (leftRHSprayFlow > rightRHSprayFlow) {
					cfdLeftSaD = "D";
					cfdLeftOfaD = "D";
					cfdRightSaD = "U";
					cfdRightOfaD = "U";
				} else if (leftRHSprayFlow < rightRHSprayFlow) {
					cfdLeftSaD = "U";
					cfdLeftOfaD = "U";
					cfdRightSaD = "D";
					cfdRightOfaD = "D";
				}
			}
			
			// CFD DB Left, Right SA 1순위 제어방향 제시			
			this.navigatorMap.put(cfdLeftSaP1, cfdLeftSaD);
			this.navigatorMap.put(cfdRightSaP1, cfdRightSaD);
			
			// OFA는 위에서 Swirl 제어 시 이미 등록된 OFA 제어방향이 있음, 유량 제어 방향과 반대일 경우 해당 OFA는 아래 표에 따름.
			// 표 : No1.   No2.   Final
			//    Down   Down    down
			//    Up     Down    Hold
			//    Down   Up      Hold
			//    Down   Hold    Down
			//    Up     Hold    Up 
			//    Hold   Hold    Hold			
			// CFD DB Left OFA 1순위 제어방향 제시
			ofaUpDown = this.navigatorMap.get(cfdLeftOfaP1);
			if (CommonConst.StringEmpty.equals(ofaUpDown)) // No1 값이 Hold 이면 No2 값으로 대체한다.
				ofaUpDown = cfdLeftOfaD;
			else if (!CommonConst.StringEmpty.equals(cfdLeftOfaD)) { // No2 값이 Hold 이면 No1 값그대로 내보냄. 
				if (!cfdLeftOfaD.equals(ofaUpDown)) // No1, No2 값이 서로 같이 않으면  Hold로 만듬.
					ofaUpDown = CommonConst.StringEmpty;
			}			
			this.navigatorMap.put(cfdLeftOfaP1, ofaUpDown);
			
			// CFD DB Right OFA 1순위 제어 방향 제시
			ofaUpDown = this.navigatorMap.get(cfdRightOfaP1);
			if (CommonConst.StringEmpty.equals(ofaUpDown)) // No1 값이 Hold 이면 No2 값으로 대체한다.
				ofaUpDown = cfdRightOfaD;
			else if (!CommonConst.StringEmpty.equals(cfdRightOfaD)) { // No2 값이 Hold 이면 No1 값그대로 내보냄. 
				if (!cfdLeftOfaD.equals(ofaUpDown)) // No1, No2 값이 서로 같이 않으면  Hold로 만듬.
					ofaUpDown = CommonConst.StringEmpty;
			}			
			this.navigatorMap.put(cfdRightOfaP1, ofaUpDown);
		}
	}	

	/**
	 * E-mission Balancing Bad Check.
	 */
	private boolean isEmissionBalancingBadCheck(List<NavConfEntity> navConfEntityList, HashMap<String, Double> opDataMap) {
		boolean isEmissionBalancingBad = false;
		double navConfO2EmissionHigh = 0.0;
		double navConfCoEmissionHigh = 0.0;
		double navConfNoxEmissionHigh = 0.0;
		
		List<NavConfEntity> tempNavConfEntityList = navConfEntityList.stream()
				.filter(n -> n.getUseYn() == true && "E".equals(n.getItemGroup()))
				.collect(Collectors.toList());
		
		for (NavConfEntity navConfEntity : tempNavConfEntityList) {
			if ("o2_emission".equals(navConfEntity.getItemId())) {
				navConfO2EmissionHigh = navConfEntity.getHighVal();
			} else if ("co_emission".equals(navConfEntity.getItemId())) {
				navConfCoEmissionHigh = navConfEntity.getHighVal();
			} else if ("nox_emission".equals(navConfEntity.getItemId())) {
				navConfNoxEmissionHigh = navConfEntity.getHighVal();
			}
		}

		// E-mission Bad 체크. (Sasan 에서 E-mission 은 제외되었음. 아래 로직은 PPT 문서로 로직만 적용하였음.)
		// : Bad 체크는 최종 로직으로 적용되어있음. Sasan 에서는 E-mission 제외함.
		List<String> o2DataList = new ArrayList<String>();
		o2DataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);
		o2DataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);
		o2DataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);
		o2DataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);

		List<String> coDataList = new ArrayList<String>();
		coDataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);
		coDataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);
		coDataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);
		coDataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);

		List<String> noxDataList = new ArrayList<String>();
		noxDataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);
		noxDataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);
		noxDataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);
		noxDataList.add(CommonConst.FN_RT_SPRL_EVAP_MTL_TEMP_2);

		double o2Value = 0.0, o2Total = 0.0, o2Avg = 0.0;
		double coValue = 0.0, coTotal = 0.0, coAvg = 0.0;
		double noxValue = 0.0, noxTotal = 0.0, noxAvg = 0.0;

		for (String o2Data : o2DataList) {
			o2Total += opDataMap.get(o2Data);
		}
		o2Avg = o2Total / o2DataList.size();
		for (String o2Data : o2DataList) {
			o2Value = opDataMap.get(o2Data) - o2Avg / o2DataList.size() * o2Avg;
		}

		for (String coData : coDataList) {
			coTotal += opDataMap.get(coData);
		}
		coAvg = coTotal / coDataList.size();
		for (String coData : coDataList) {
			coValue = opDataMap.get(coData) - coAvg / coDataList.size() * coAvg;
		}

		for (String noxData : noxDataList) {
			noxTotal += opDataMap.get(noxData);
		}
		noxAvg = noxTotal / noxDataList.size();
		for (String noxData : noxDataList) {
			noxValue = opDataMap.get(noxData) - coAvg / noxDataList.size() * noxAvg;
		}

		if (o2Value > navConfO2EmissionHigh || coValue > navConfCoEmissionHigh || noxValue > navConfNoxEmissionHigh)
			isEmissionBalancingBad = true;

		return isEmissionBalancingBad;
	}
	
	/**
	 * Emission Balancing Process
	 * : Sasan 에서는 제외됨.
	 */
	private void emissionBalancingProcess(HashMap<String, Double> opDataMap) {}	
	
	/**
	 * PSO 알고리즘.
	 */
	private void PSOAlgorithm() {		
		/*
		 * DCS에 보낸 누적된 MV Output Bias의 Sum과 Count를 Load 한다. (2019-01-26)
		 * MV Bias Sum과 Count로 5분 의 평균 Bias값을 계산하여, 탐색범위를 조절한다.
		 * 5분 의 평균 Bias값을 사용하면 Output Contoller에서 바로 등록해야 하기 때문에  MV Bias Sum, Count 값을 0으로 초기화 한다.
		 */			 
		List<OutputMVTGTEntity> outputMVTGTEntityList = this.outputMVTGTRepository.findAll();
		this.outputMVTGTRepository.updateOutputMVBiasSumCntInitialize();
		
		NNConfigEntity nnConfigEntity = this.nnConfigRepository.findByConfId(NNConfigEntity.defaultID);
		
		// # Valid input load
		List<Object[]> objectValidInputDataList = this.nnTrainInRepository.findByTimestampLastNNTrainInputDataNativeQuery(nnConfigEntity.getValidDataTime());
		List<String> nnModelValidInputList = new ArrayList<String>();
		for (Object[] objectValidInputData : objectValidInputDataList) {
			nnModelValidInputList.add((String) objectValidInputData[1]);
		}
		
		if (nnModelValidInputList.size() <= 0) {
			this.systemCheckRepository.updateSetControlReadyAl(false);
			logger.info("# PSO can not start because there is no latest NN Train data.");
			return;
		}

		double[][] validInputDataValues = Utilities.convertRowsAndCommaColumnsToDouble(nnModelValidInputList);
		
		// # Train PSO input load
		List<Object[]> objectOpInputDataList = this.opDataInputRepository.findByTimestampLastNNTrainInputPSODataNativeQuery();
		List<String> nnModelOpInputList = new ArrayList<String>();
		for (Object[] objectOpInputData : objectOpInputDataList) {
			nnModelOpInputList.add((String) objectOpInputData[1]);
		}
		
		if (nnModelOpInputList.size() <= 0) {
			this.systemCheckRepository.updateSetControlReadyAl(false);
			logger.info("# PSO can not start because there is no latest OP data.");
			return;
		}
		
		double[][] opInputDataValues = Utilities.convertRowsAndCommaColumnsToDouble(nnModelOpInputList);
		double[] inputDataValues = opInputDataValues[0];
		
		// # Valid output load
		List<Object[]> objectValidOutputDataList = this.nnTrainOutRepository.findByTimestampLastNNTrainOutputDataNativeQuery(nnConfigEntity.getValidDataTime());
		List<String> nnModelValidOutputList = new ArrayList<String>();
		for (Object[] objectValidOutputData : objectValidOutputDataList) {
			nnModelValidOutputList.add((String) objectValidOutputData[1]);
		}
		
		double[][] validOutputDataValues = Utilities.convertRowsAndCommaColumnsToDouble(nnModelValidOutputList);
		
		// # Train PSO output load
		List<Object[]> objectOpOutputDataList = this.opDataInputRepository.findByTimestampLastNNTrainOutputPSODataNativeQuery();
		List<String> nnModelOpOutputList = new ArrayList<String>();
		for (Object[] objectOpOutputData : objectOpOutputDataList) {
			nnModelOpOutputList.add((String) objectOpOutputData[1]);
		}
		
		double[][] opOutputDataValues = Utilities.convertRowsAndCommaColumnsToDouble(nnModelOpOutputList);				
		double[] outputDataValues = opOutputDataValues[0];

		List<Object[]> networkInfoList = new ArrayList<Object[]>();

		// Base NN model load.
		List<NNModelBaseEntity> nnModelBaseEntityList = this.nnModelBaseRepository.findByTrainStatus(true);
		for (NNModelBaseEntity nnModelBaseEntity : nnModelBaseEntityList) {
			networkInfoList.add(new Object[] { UseModelType.Base.name(), nnModelBaseEntity.getTimestamp(),(ActivationNetwork)Utilities.deSerialize(nnModelBaseEntity.getModel())});
		}

		// NN model load.
		List<NNModelEntity> nnModelEntityList = this.nnModelRepository.findTop3ByTrainStatusOrderByTimestampDesc(true);
		for (NNModelEntity nnModelEntity : nnModelEntityList) {
			networkInfoList.add(new Object[] { UseModelType.Learning.name(), nnModelEntity.getTimestamp(),(ActivationNetwork)Utilities.deSerialize(nnModelEntity.getModel())});
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
		double limitValue = Math.pow(psoConfigEntity.getNnModelAllowValue(), 2);
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
		double opOutputLoadMW = 0;
		List<NNTrainDataConfEntity> nnTrainDataConfOutputList = this.nnTrainDataConfRepository.findByIoTypeOrderByTagNmAsc(NNTrainDataIOType.Output.name());
		List<PsoTag> outputTagList = new ArrayList<PsoTag>();
		int tagIndex = 0;
		for (NNTrainDataConfEntity nnTrainDataConf : nnTrainDataConfOutputList) {
			outputTagList.add(new PsoTag(nnTrainDataConf.getTagId(), tagIndex, nnTrainDataConf.getZeroPlate()));
			if (CommonConst.ACTIVE_POWER_OF_GENERATOR.equals(nnTrainDataConf.getTagId()))
				opOutputLoadMW = outputDataValues[tagIndex];

			tagIndex++;
		}

		// # Input 에서  opInputLoadSetPointMW(ULD_LOCAL_SET_POINT) 추출함. 
		double opInputLoadSetPointMW = 0;		
		tagIndex = 0;
		List<NNTrainDataConfEntity> nnTrainDataConfInputList = this.nnTrainDataConfRepository.findByIoTypeOrderByTagNmAsc(NNTrainDataIOType.Input.name());
		for (NNTrainDataConfEntity nnTrainDataConf : nnTrainDataConfInputList) {
			if (CommonConst.ULD_LOCAL_SET_POINT.equals(nnTrainDataConf.getTagId())) {
				opInputLoadSetPointMW = inputDataValues[tagIndex];
				break;
			}
			
			tagIndex++;
		}
		
		// # Input PsoMV Set
		List<PsoMV> mvList = new ArrayList<PsoMV>();		
		for (PSOMVInfoEntity psoMVInfoEntity : this.psoMVInfoEntityList) {
			MVType mvType = Enum.valueOf(MVType.class, psoMVInfoEntity.getPsoMVType());
			
			List<PsoTag> inputTagList = new ArrayList<PsoTag>();
			
			// 각 MV에 해당하는 Input Tag와 해당 위치의 NN Model Min, Max값을 설정함. 
			tagIndex = 0;
			for (NNTrainDataConfEntity nnTrainDataConf : nnTrainDataConfInputList) {				
				if (psoMVInfoEntity.getPsoMV().equals(nnTrainDataConf.getPsoMV())) {
					DoubleRange[] tagMinMax = model.getInputNormalizeInfo().getRanges();
					inputTagList.add(new PsoTag(nnTrainDataConf.getTagId(), tagMinMax[tagIndex].getMin(), tagMinMax[tagIndex].getMax(), tagIndex));
				}
				
				tagIndex++;
			}
			
			// DCS로 보낸 MV Bias 값을 찾는다.
			OutputMVTGTEntity outputMVTGTEntity = outputMVTGTEntityList.stream().filter(b -> b.getMv().equals(psoMVInfoEntity.getPsoMV())).findFirst().get();
			double mvBiasAvg = outputMVTGTEntity.getMvBiasCnt() == 0 ? 0 : outputMVTGTEntity.getMvBiasSum() / outputMVTGTEntity.getMvBiasCnt();
			
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
			
			/*
			 * Navigator 탐색 범위 변경.
			 * : U => - 0.1 값으로  Min을 설정함.
			 * : D => + 0.1  값으로 Max를 설정함 .
			 */			
			String navigator = this.navigatorMap.get(psoMVInfoEntity.getPsoMV().toUpperCase());
			navigator = navigator == null ? CommonConst.StringEmpty : navigator;
			switch (navigator) {
			case "U":
				psoMVMin = psoMVMin == 0 ? 0 : -NAVIGATOR_MIN_MAX_LIMIT;
				break;
			case "D":
				psoMVMax = psoMVMin == 0 ? 0 : +NAVIGATOR_MIN_MAX_LIMIT;
				break;
			}
			
			logger.info("# Navigator Direction : " + navigator + "Apply PSO => MIN : " + psoMVMin + ", MAX : " + psoMVMax + " #");
			
			// BNR Corner CN1 ~ CN4 까지 기기 보호 조건 적용 로직.
			// 해당 Group에서 최소값이 25 이하이면 PSO Min 값을 0으로 설정, 최대값이 100 이상이면  PSO Max 값을  0으로 설정
			// 해당 Group에서 최소값이  Low Base 25% 미만로 못내려가기 위한 보호로직 적용. 예) (psoMin, psoMax, CN값 => 최종 PSOMin) -0.1, 5, 28 => -0.1 | -5, 5, 28 => -3 | -5, 5, 31 => -5
			// 해당 Group에서 최대값이  High Base 100% 초과를 못하기 하기 위한 보호로직 적용. 예)(psoMin, psoMax, CN값 => 최종 PSOMax) -5, 0.1, 98 => 0.1 | -5, 5, 98 => 5 | -5, 5, 94 => 5
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
		
		// ## PSO CalculationFunction.
		ControlEntity controlEntity = this.controlRepository.findControl();
		PsoCalculationFunction calculationFunction = new PsoCalculationFunction(outputTagList, controlEntity, psoConfigEntity, opOutputLoadMW, opInputLoadSetPointMW);

		// ## PSO Service Execute.
		PsoService psoService = new PsoService(mvList, inputDataValues, model, calculationFunction, psoConfigEntity, processingUnit, gpuUseSwarmsize);
		int result = psoService.execute();
		if (result < 0) {
			this.systemCheckRepository.updateSetControlReadyAl(false);
			logger.info("# PSO Execute has failed.");
			return;
		}

		double globalBestValue = psoService.getGlobalBestValue();
		double[] globalBestMV = psoService.getGlobalBestMV();
		double[] globalBestOutput = psoService.getGlobalBestOutput();
		String resultMV = CommonConst.StringEmpty;
		for (int i = 0; i < mvList.size(); i++) {
			PsoMV psoMV = mvList.get(i);
			resultMV += "," + psoMV.getName() + ":" + globalBestMV[i];
		}
		resultMV = resultMV.substring(1); // Purpose to remove the comma of the first character.
		
		// 최신 Train Output Data를 그대로 사용하여 계산함. (2018-12-04)
		// double opDataFValue =  calculationFunction.calcCostFunction(outputDataValues); 
		
		// 최신 Train Input OP 정보를 NNModel을 통해서 나온 Output 값으로  calcCostFunction 값을 계산한다.
		double opDataFValue =  calculationFunction.calcCostFunction(outputModelResult);
		PSOResultEntity psoResultEntity = new PSOResultEntity();
		psoResultEntity.setTimestamp(new Date());
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
		psoResultEntity.setPsoInputData(nnModelOpInputList.get(0));
		psoResultEntity.setPsoOutputData(nnModelOpOutputList.get(0));
		
		// 최신 Train Input OP 정보를 NNModel을 통해서 나온 Output 값으로  calcCostFunction 값을 계산한다.
		// double[] beforePsoOutputKeyValues = calculationFunction.getCalcOutputKeyTagValue(outputModelResult); 
		
		// 최신 Train Output Data를 그대로 사용하여 계산함. (2018-12-04)
		double[] beforePsoOutputKeyValues = calculationFunction.getCalcOutputKeyTagValue(outputDataValues);
		double[] afterNNOutputKeyValues = calculationFunction.getCalcOutputKeyTagValue(outputModelResult);
		double[] afterPsoOutputKeyValues = calculationFunction.getCalcOutputKeyTagValue(globalBestOutput);
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
		
		this.algorithmService.saveAlgorithmPSOResult(psoResultEntity, psoEffectPerItemEntityList);		
	}
}
