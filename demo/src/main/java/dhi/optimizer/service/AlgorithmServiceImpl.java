package dhi.optimizer.service;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dhi.common.exception.InvalidParameterException;
import dhi.common.util.Utilities;
import dhi.optimizer.algorithm.common.PerformanceCalculation;
import dhi.optimizer.algorithm.neuralNet.ActivationNetwork;
import dhi.optimizer.algorithm.neuralNet.NetworkXmlHelper;
import dhi.optimizer.algorithm.pso.PsoMV.MVType;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.common.StaticMap;
import dhi.optimizer.enumeration.ControllerModeStatus;
import dhi.optimizer.enumeration.CtrDataIOType;
import dhi.optimizer.enumeration.FireBallCenterType;
import dhi.optimizer.enumeration.MetalTempTubeType;
import dhi.optimizer.enumeration.NNTrainDataIOType;
import dhi.optimizer.enumeration.PsoOptimizationFunctionModeStatus;
import dhi.optimizer.model.AlgorithmStatusInfoVO;
import dhi.optimizer.model.ConfDataVO;
import dhi.optimizer.model.NavigatorTubeWWTempVO;
import dhi.optimizer.model.OptCodeVO;
import dhi.optimizer.model.PsoMVInfoVO;
import dhi.optimizer.model.TagDataVO;
import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.model.db.NNConfigEntity;
import dhi.optimizer.model.db.NNModelBaseEntity;
import dhi.optimizer.model.db.NNModelEntity;
import dhi.optimizer.model.db.NavCfdEntity;
import dhi.optimizer.model.db.NavConfEntity;
import dhi.optimizer.model.db.NavFireBallEntity;
import dhi.optimizer.model.db.NavResultEntity;
import dhi.optimizer.model.db.NavResultIdEntity;
import dhi.optimizer.model.db.NavSettingEntity;
import dhi.optimizer.model.db.NavTrendItemChartEntity;
import dhi.optimizer.model.db.NavWWMatchingEntity;
import dhi.optimizer.model.db.NavWWMatchingIdEntity;
import dhi.optimizer.model.db.OutputBiasConfEntity;
import dhi.optimizer.model.db.OutputDataConfEntity;
import dhi.optimizer.model.db.PSOConfigEntity;
import dhi.optimizer.model.db.PSOEffectPerItemEntity;
import dhi.optimizer.model.db.PSOResultEntity;
import dhi.optimizer.model.db._PSOResultEntity;
import dhi.optimizer.model.json.AlgorithOutputControllerConfig;
import dhi.optimizer.model.json.AlgorithmItemStatus;
import dhi.optimizer.model.json.AlgorithmNNConfig;
import dhi.optimizer.model.json.AlgorithmOptimizerMVStatus;
import dhi.optimizer.model.json.AlgorithmPSOConfig;
import dhi.optimizer.model.json.AlgorithmProcessStatus;
import dhi.optimizer.model.json.AlgorithmProcessStatusNNModel;
import dhi.optimizer.model.json.AlgorithmProcessStatusNNModelDetail;
import dhi.optimizer.model.json.AlgorithmProcessStatusOutputController;
import dhi.optimizer.model.json.AlgorithmProcessStatusPSO;
import dhi.optimizer.model.json.AlgorithmProcessStatusPreProcess;
import dhi.optimizer.model.json.AlgorithmSolutionStatus;
import dhi.optimizer.model.json.Chart;
import dhi.optimizer.model.json.ItemStatus;
import dhi.optimizer.model.json.KeyValue;
import dhi.optimizer.model.json.Navigator;
import dhi.optimizer.model.json.NavigatorCombustionStatus;
import dhi.optimizer.model.json.NavigatorConfig;
import dhi.optimizer.model.json.NavigatorFireBallCenter;
import dhi.optimizer.model.json.NavigatorGasAnalyzerItem;
import dhi.optimizer.model.json.NavigatorGasPressAndTemp;
import dhi.optimizer.model.json.NavigatorLimit;
import dhi.optimizer.model.json.NavigatorLimitsAndFireBallCenter;
import dhi.optimizer.model.json.NavigatorTubeWallItem;
import dhi.optimizer.model.json.NavigatorWallMatchingTable;
import dhi.optimizer.model.json.NavigatorTubeWallMetalTemp;
import dhi.optimizer.model.json.NavigatorTubeWallTempItem;
import dhi.optimizer.model.json.Position;
import dhi.optimizer.model.json.NavigatorCombustionCoalSupplyStatus;
import dhi.optimizer.model.json.NavigatorCombustionItem;
import dhi.optimizer.model.json.NavigatorCombustionItemStatus;
import dhi.optimizer.model.json.NavigatorCombustionSpiralWaterWall;
import dhi.optimizer.model.json.Tag;
import dhi.optimizer.repository.CommonObjectRepository;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.repository.CtrDataInputRepository;
import dhi.optimizer.repository.CtrDataOutputRepository;
import dhi.optimizer.repository.NNConfigRepository;
import dhi.optimizer.repository.NNModelBaseRepository;
import dhi.optimizer.repository.NNModelRepository;
import dhi.optimizer.repository.NNTrainInRepository;
import dhi.optimizer.repository.NNTrainOutRepository;
import dhi.optimizer.repository.NavCfdRepository;
import dhi.optimizer.repository.NavConfRepository;
import dhi.optimizer.repository.NavFireBallRepository;
import dhi.optimizer.repository.NavResultRepository;
import dhi.optimizer.repository.NavSettingRepository;
import dhi.optimizer.repository.NavTubeWWTempConfRepository;
import dhi.optimizer.repository.NavTrendItemChartRepository;
import dhi.optimizer.repository.NavWWMatchingRepository;
import dhi.optimizer.repository.OPDataInputRepository;
import dhi.optimizer.repository.OutputBiasConfRepository;
import dhi.optimizer.repository.OutputDataConfRepository;
import dhi.optimizer.repository.OutputDataHistRepository;
import dhi.optimizer.repository.OutputMVTGTRepository;
import dhi.optimizer.repository.PSOConfigRepository;
import dhi.optimizer.repository.PSOEffectPerItemRepository;
import dhi.optimizer.repository.PSOMVInfoRepository;
import dhi.optimizer.repository.PSOResultRepository;
import dhi.optimizer.repository._PSOResultRepository;

@Service
@Transactional
public class AlgorithmServiceImpl implements AlgorithmService {
		
	private static final Logger logger = LoggerFactory.getLogger(AlgorithmService.class);
	
	private static final String TAG_NAME_ULD_INPUT = "ULD Input";
	private static final String TAG_NAME_LOAD = "Load";
	
	private static final String NAV_ITEM_FIRE_DISTANCE_CHG = "fire_distance_chg";
	private static final String NAV_ITEM_VRT_WTR_WL_TEMP_MEAN = "vrt_wtr_wl_temp_mean";
	private static final String NAV_ITEM_SPR_WTR_WL_TEMP_MEAN = "spr_wtr_wl_temp_mean";
	private static final String NAV_ITEM_VRT_WTR_WL_TEMP_MAX = "vrt_wtr_wl_temp_max";
	private static final String NAV_ITEM_SPR_WTR_WL_TEMP_MAX = "spr_wtr_wl_temp_max";
	private static final String NAV_ITEM_PA_FLOW = "pa_flow";
	private static final String NAV_ITEM_SA_FLOW = "sa_flow";
	private static final String NAV_ITEM_OFA_FLOW = "ofa_flow";
	private static final String NAV_ITEM_BZS = "bzs";
	private static final String NAV_ITEM_STACK_NOX = "stack_nox";
	private static final String NAV_ITEM_STACK_O2 = "stack_o2";
	private static final String NAV_ITEM_STACK_CO = "stack_co";
	private static final String NAV_ITEM_FN_SH_TU_TEMP = "fn_sh_tu_temp";
	private static final String NAV_ITEM_SH_SP_1_FLOW = "sh_sp_1_flow";
	private static final String NAV_ITEM_SH_SP_2_FLOW = "sh_sp_2_flow";
	private static final String NAV_ITEM_FN_RH_TU_TEMP = "fn_rh_tu_temp";
	private static final String NAV_ITEM_RH_SP_1_FLOW = "rh_sp_1_flow";
	private static final String NAV_ITEM_RH_SP_2_FLOW = "rh_sp_2_flow";
	
	private static final double PSO_MAXIMUM_THREAD_LOAD_RATE = 90;
		
	@Autowired
	private NNConfigRepository nnConfigRepository;
	
	@Autowired
	private PSOConfigRepository psoConfigRepository;
	
	@Autowired 
	private PSOMVInfoRepository psoMVInfoRepository;
	
	@Autowired
	private PSOResultRepository psoResultRepository;
	
	@Autowired
	private _PSOResultRepository _psoResultRepository;
	
	@Autowired
	private ControlRepository controlRepository;

	@Autowired
	private NNTrainInRepository nnTrainInRepository;
	
	@Autowired
	private NNTrainOutRepository nnTrainOutRepository;
	
	@Autowired
	private OutputDataHistRepository outputDataHistRepository;
		
	@Autowired
	private NNModelBaseRepository nnModelBaseRepository;
	
	@Autowired
	private NNModelRepository nnModelRepository;	
	
	@Autowired
	private NavConfRepository navConfRepository;
	
	@Autowired
	private NavFireBallRepository navFireBallRepository;
	
	@Autowired
	private NavResultRepository navResultRespository;
	
	@Autowired
	private OPDataInputRepository opDataInputRepository;
	
	@Autowired
	private NavTrendItemChartRepository navTrendItemChartRepository;
	
	@Autowired
	private NavSettingRepository navSettingRepository;
	
	@Autowired
	private CtrDataOutputRepository ctrDataOutputRepository;
	
	@Autowired
	private CtrDataInputRepository ctrDataInputRepository;
	
	@Autowired
	private OutputDataConfRepository outputDataConfRepository;
	
	@Autowired
	private OutputBiasConfRepository outputBiasConfRepository;
	
	@Autowired
	private OutputMVTGTRepository outputMVTGTRepository;
	
	@Autowired
	private NavTubeWWTempConfRepository navTubeWWTempConfRepository;	
	
	@Autowired
	private NavWWMatchingRepository navWWMatchingRepository;
	
	@Autowired
	private NavCfdRepository navCfdRepository;
	
	@Autowired
	private CommonObjectRepository commonObjectRepository;	
	
	@Autowired
	private PSOEffectPerItemRepository psoEffectPerItemRepository;
	
	@Autowired
	private CommonDataService commonDataService;
	
	@Autowired
	private PerformanceCalculation performanceCalculation;
	
	/*
	 * XML로 되어있는 NNModel을 Java NNModel로 변환하여 DB에 저장함. 
	 */
	public void uploadNNModel(InputStream inputStream) throws InvalidParameterException {
		try {
			NetworkXmlHelper xmlHelper = new NetworkXmlHelper();
			ActivationNetwork network = xmlHelper.XMLDecoder(inputStream);
			
			NNModelBaseEntity nnModelBaseEntity = new NNModelBaseEntity();
			nnModelBaseEntity.setTrainStatus(true);
			nnModelBaseEntity.setTimestamp(new Date());
			nnModelBaseEntity.setModel(Utilities.serialize(network));
			this.nnModelBaseRepository.saveAndFlush(nnModelBaseEntity);
			
		} catch (Exception e) {
			throw new InvalidParameterException(e.getMessage());
		}
	}

	/*
	 * Algorithm NNModel Trend Chant Data 가져오기.  
	 */
	public Chart getTrendNNModelData(String startDateString, String endDateString) throws InvalidParameterException {
		Chart chart = new Chart();
		try {

			ArrayList<Tag> taglist = new ArrayList<Tag>();

			Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateString);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateString);

			long diff = endDate.getTime() - startDate.getTime();
			long diffSeconds = diff / 1000;
			int secondsInterval = 300;
			int secModNumber = (int) ((diffSeconds / CommonConst.DATE_SECONDS_OF_DAY) + 1) * secondsInterval;

			Collection<String> tagIds = new ArrayList<String>();
			tagIds.add(CommonConst.ULD_LOCAL_SET_POINT);
			tagIds.add(CommonConst.ACTIVE_POWER_OF_GENERATOR);
			HashMap<String, TagDataVO> opDataConfMap = this.commonDataService.getOpDataConfMap(tagIds);

			Tag tagUldInput = this.getTrendTagInfo(NNTrainDataIOType.Input, CommonConst.ULD_LOCAL_SET_POINT, startDate, endDate, secModNumber);
			tagUldInput.setName(TAG_NAME_ULD_INPUT);
			tagUldInput.setUnit(opDataConfMap.get(CommonConst.ULD_LOCAL_SET_POINT).getUnit());
			taglist.add(tagUldInput);

			Tag tagLoad = this.getTrendTagInfo(NNTrainDataIOType.Output, CommonConst.ACTIVE_POWER_OF_GENERATOR, startDate, endDate, secModNumber);
			tagLoad.setName(TAG_NAME_LOAD);
			tagLoad.setUnit(opDataConfMap.get(CommonConst.ACTIVE_POWER_OF_GENERATOR).getUnit());
			taglist.add(tagLoad);

			chart.setTag(taglist.toArray(new Tag[taglist.size()]));

		} catch (ParseException e) {
			throw new InvalidParameterException(e.getMessage());
		}

		return chart;
	}
		
	public List<String> getCSVDownLoadNNModelData(NNTrainDataIOType nnModelDataIOType, String startDateString, String endDateString) throws InvalidParameterException {

		List<String> nnModelTrainDataList = null;
		try {
			Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateString);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateString);

			switch (nnModelDataIOType) {
			case Input:
				nnModelTrainDataList = this.nnTrainInRepository.getCSVDownLoadNNTrainInputDataNativeQuery(startDate, endDate);
				break;

			case Output:
				nnModelTrainDataList = this.nnTrainOutRepository.getCSVDownLoadNNTrainOutputDataNativeQuery(startDate, endDate);
				break;
			}
		} catch (ParseException e) {
			throw new InvalidParameterException(e.getMessage());
		}
		
		return nnModelTrainDataList;
	}
	
	public String getXmlDownLoadNNModel(String modelType, String timestamp) throws InvalidParameterException {
		String modelXmlToString = CommonConst.StringEmpty;
		try {
			Date timestampDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(timestamp);
			ActivationNetwork network = null;

			switch (modelType) {
			case "base":
				NNModelBaseEntity nnModelBaseEntity = this.nnModelBaseRepository.getOne(timestampDate);
				if (nnModelBaseEntity != null) {
					network = (ActivationNetwork) Utilities.deSerialize(nnModelBaseEntity.getModel());
				}
				break;

			case "normal":
				NNModelEntity nnModelEntity = this.nnModelRepository.getOne(timestampDate);
				if (nnModelEntity != null) {
					network = (ActivationNetwork) Utilities.deSerialize(nnModelEntity.getModel());
				}
				
				break;
			}

			if (network != null) {
				NetworkXmlHelper helper = new NetworkXmlHelper();
				modelXmlToString = helper.XMLEncoder(network);
			}
			
		} catch (ParseException e) {
			throw new InvalidParameterException(e.getMessage());
		}
		
		return modelXmlToString;
	}
	
	/*
	 * Navigator Main 화면에 필요한 정보 가져오기.
	 * : Burner, OFA, 각 종 Flow 정보를 DB로 읽어 로직처리함.
	 */
	public Navigator getNavigatorInfo() {
		
		// OP Data Map Load 
		HashMap<String, TagDataVO> opDataTagMap = this.commonDataService.getOpDataTagMap();
		
		// Navigator Burner, OFA 정보 Load
		List<NavResultEntity> navResultEntityList = this.navResultRespository.findAll();
		HashMap<NavResultIdEntity, NavResultEntity> navResultMap = new HashMap<NavResultIdEntity, NavResultEntity>();
		for (NavResultEntity navResultEntity : navResultEntityList) {
			navResultMap.put(navResultEntity.getNavResultIdEntity(), navResultEntity);
		}
		
		TagDataVO tagDataVO;
		Navigator navigator = new Navigator();
		NavigatorCombustionStatus combustionStatus = new NavigatorCombustionStatus();
		
		// Spiral Water Wall의 온도 정보를 가져온다.
		NavSettingEntity navSettingEntity = this.navSettingRepository.findBySettingId(NavSettingEntity.defaultID);
		List<Object[]> navTubeWWTempList = this.navTubeWWTempConfRepository.getNavTubeWWTempList(MetalTempTubeType.Spiral.name());
		List<Double> frontTempList = new ArrayList<Double>();
		List<Double> rearTempList = new ArrayList<Double>();
		List<Double> leftTempList = new ArrayList<Double>();
		List<Double> rightTempList = new ArrayList<Double>();
		
		// 각 Wall 별로 온도를 분리한다.
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
		
		// Burner, OFA의 FDE 방향을 만든다.		
		// F (fireballCentering) 방향 만들기.
		// 로직 :
		// 1. Burner AA, AB~FG - 4개  Water Wall 의 높은 온도 순으로 우선순위를 만든다.
		// 2. Burner GG - High, Low 우선선위 로직적용.
		// 3. 우선순위로 Water Wall Matching 테이블을 조회하여  Burner - Up, Down, Hold 값을가져옴.
		NavigatorTubeWWTempVO tubeWWTempVO = new NavigatorTubeWWTempVO(frontTempList, rearTempList, leftTempList, rightTempList, navSettingEntity.getMetalTempDevRate(), navSettingEntity.getAllowableMetalTemp());
		List<KeyValue> deviationPriorityList = tubeWWTempVO.getDeviationPriorityList();
		List<KeyValue> highLowPriorityList = tubeWWTempVO.getHighLowPriorityList();
		
		Optional<NavWWMatchingEntity> navWWMatchingEntityAA = Optional.empty();
		Optional<NavWWMatchingEntity> navWWMatchingEntityABFG = Optional.empty();
		Optional<NavWWMatchingEntity> navWWMatchingEntityGG = Optional.empty();
		List<NavWWMatchingEntity> navWWMatchingEntityList = this.navWWMatchingRepository.findAll();
		if (deviationPriorityList != null) {
			navWWMatchingEntityAA = navWWMatchingEntityList.stream().filter(
					t -> t.getNavWWMatchingIdEntity().getP1().equals(deviationPriorityList.get(0).getKey())
							&& t.getNavWWMatchingIdEntity().getP2().equals(deviationPriorityList.get(1).getKey())
							&& t.getNavWWMatchingIdEntity().getP3().equals(deviationPriorityList.get(2).getKey())
							&& t.getNavWWMatchingIdEntity().getP4().equals(deviationPriorityList.get(3).getKey())
							&& t.getNavWWMatchingIdEntity().getType().equals("aa"))
					.findFirst();

			navWWMatchingEntityABFG = navWWMatchingEntityList.stream().filter(
					t -> t.getNavWWMatchingIdEntity().getP1().equals(deviationPriorityList.get(0).getKey())
							&& t.getNavWWMatchingIdEntity().getP2().equals(deviationPriorityList.get(1).getKey())
							&& t.getNavWWMatchingIdEntity().getP3().equals(deviationPriorityList.get(2).getKey())
							&& t.getNavWWMatchingIdEntity().getP4().equals(deviationPriorityList.get(3).getKey())
							&& t.getNavWWMatchingIdEntity().getType().equals("ab_fg"))
					.findFirst();
		}

		if (highLowPriorityList != null) {
			NavWWMatchingEntity navWWMatchingEntity = new NavWWMatchingEntity();
			for (KeyValue highLowPriority : highLowPriorityList) {
				Optional<NavWWMatchingEntity> navWWMatchingEntityDetailGG = navWWMatchingEntityList.stream()
						.filter(t -> t.getNavWWMatchingIdEntity().getP1().equals(highLowPriority.getKey())
								&& t.getNavWWMatchingIdEntity().getP2().equals(highLowPriority.getValue())
								&& t.getNavWWMatchingIdEntity().getType().equals("gg"))
						.findFirst();

				if (navWWMatchingEntityDetailGG.isPresent()) {
					if (!"H".equals(navWWMatchingEntityDetailGG.get().getC1()))
						navWWMatchingEntity.setC1(navWWMatchingEntityDetailGG.get().getC1());
					else if (!"H".equals(navWWMatchingEntityDetailGG.get().getC2()))
						navWWMatchingEntity.setC2(navWWMatchingEntityDetailGG.get().getC2());
					else if (!"H".equals(navWWMatchingEntityDetailGG.get().getC3()))
						navWWMatchingEntity.setC3(navWWMatchingEntityDetailGG.get().getC3());
					else
						navWWMatchingEntity.setC4(navWWMatchingEntityDetailGG.get().getC4());
				}
			}
			
			// 우선순위가 안나오는 경우 Null 일 수 있음. Null 인 경우 Hold로 변경.
			if(navWWMatchingEntity.getC1() == null)
				navWWMatchingEntity.setC1("H");
			
			if(navWWMatchingEntity.getC2() == null)
				navWWMatchingEntity.setC2("H");
			
			if(navWWMatchingEntity.getC3() == null)
				navWWMatchingEntity.setC3("H");
			
			if(navWWMatchingEntity.getC4() == null)
				navWWMatchingEntity.setC4("H");
			
			navWWMatchingEntityGG = Optional.ofNullable(navWWMatchingEntity);			
		}

		// FDE 방향 Map
		// FDE 를 배열로 만들고 [0] = F, [1] = D, [2]= E 번째 위치 값에 각 FDE의 실제값을 저장한다.
		HashMap<String, String[]> navigatorFDEMap = new HashMap<String, String[]>();
		
		// Burner 항목 배열.
		String[] saTypes = new String[] { "C1", "C2", "C3", "C4" };
		String[] saItemIds = new String[] { "GG", "FG", "EF", "DE", "CD", "BC", "AB", "AA" };
		String[] coalItemIds = new String[] { "G", "F", "E", "D", "C", "B", "A"};
		// String[][] coalItemIdStandBySA = new String[][] {{"FG","GG"}, {"EF","FG"}, {"DE","EF"}, {"CD","DE"}, {"BC","CD"}, {"AB","BC"}, {"AA","AB"}};
		
		// OFA 항목 배열.
		String[] uofaTypes = new String[] { "LEFT", "REAR", "RIGHT", "FRONT" };
		String[] uofaConsts = new String[] { "LFT", "REAR", "RT", "FRT" };
		String[] uofaItemsIds = new String[] { "UOFA3", "UOFA2", "UOFA1" };
		String[] uofaItemNames = new String[] { "Ⅲ", "Ⅱ", "Ⅰ" };

		// 'F'의 방향을 찾아 등록한다.
		for (String saType : saTypes) {
			for (String saItemId : saItemIds) {
				String f = CommonConst.StringEmpty;
				String d = CommonConst.StringEmpty;
				String e = CommonConst.StringEmpty;
				switch (saType) {
				case "C1":
					if ("AA".equals(saItemId) && navWWMatchingEntityAA.isPresent())
						f = navWWMatchingEntityAA.get().getC1();
					else if ("GG".equals(saItemId) && navWWMatchingEntityGG.isPresent())
						f = navWWMatchingEntityGG.get().getC1();
					else if (navWWMatchingEntityABFG.isPresent())
						f = navWWMatchingEntityABFG.get().getC1();

					break;
				case "C2":
					if ("AA".equals(saItemId) && navWWMatchingEntityAA.isPresent())
						f = navWWMatchingEntityAA.get().getC2();
					else if ("GG".equals(saItemId) && navWWMatchingEntityGG.isPresent())
						f = navWWMatchingEntityGG.get().getC2();
					else if (navWWMatchingEntityABFG.isPresent())
						f = navWWMatchingEntityABFG.get().getC2();

					break;
				case "C3":
					if ("AA".equals(saItemId) && navWWMatchingEntityAA.isPresent())
						f = navWWMatchingEntityAA.get().getC3();
					else if ("GG".equals(saItemId) && navWWMatchingEntityGG.isPresent())
						f = navWWMatchingEntityGG.get().getC3();
					else if (navWWMatchingEntityABFG.isPresent())
						f = navWWMatchingEntityABFG.get().getC3();

					break;
				case "C4":
					if ("AA".equals(saItemId) && navWWMatchingEntityAA.isPresent())
						f = navWWMatchingEntityAA.get().getC4();
					else if ("GG".equals(saItemId) && navWWMatchingEntityGG.isPresent())
						f = navWWMatchingEntityGG.get().getC4();
					else if (navWWMatchingEntityABFG.isPresent())
						f = navWWMatchingEntityABFG.get().getC4();

					break;
				}

				navigatorFDEMap.put(saType + "_" + saItemId, new String[] { f, d, e });
			}
		}
		
		/* *** 아래 로직 사용하지 않음. 사유: 2018-12-13 18:33분 새로운 로직으로 전달 받음. Allowable Flue Gas Temp. Dev(Deg),Allowable Spray Flow Dev(t/h) 사용함.		
		// D (Deviation) 방향 만들기. -  Spray Balancing 알고리즘 적용.
		// 로직 :
		// 1. Left RH Spray Flow > Right RH Spray Flow 
		// : Mill Stand By => Down, OFA => Up
		// 2. Left RH Spray Flow < Right RH Spray Flow
		// : Mill Stand By => Up, OFA => Down
		// 3. Left RH Spray Flow == Right RH Spray Flow
		// : Mill Stand By => Hold, OFA => Hold
		// 4. CFD 해석 데이터를 조회하여 해석 데이터가 있는 경우
		// : 위 1, 2, 3 에서 변경한 Burner를 제외하고 "Up" 으로 변경함.
		// : CFD 해석의 OFA는  Up 인 경우 Hold로 변경. 그외에는 해석된 데이터로 등록함.
		String f = CommonConst.StringEmpty;
		String ofaD = CommonConst.StringEmpty;
		String burnerD = CommonConst.StringEmpty;
		String e = CommonConst.StringEmpty;
		String rhCFDDirection = "HOLD";
		double leftRHSprayFlow = opDataTagMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_L_3).getTagVal();
		double rightRHSprayFlow = opDataTagMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_R_3).getTagVal();
		if (leftRHSprayFlow > rightRHSprayFlow) {
			ofaD = "U";
			burnerD = "D";
			rhCFDDirection = "LEFT";
		} else if (leftRHSprayFlow < rightRHSprayFlow) {
			ofaD = "D";
			burnerD = "U";
			rhCFDDirection = "RIGHT";
		}
		
		// Burner - FDE 중 'D' 값을 설정한다.
		// CFD DB 조회에 필요한  Mill(ON) 2진수 값을 구한다.
		int cfdMillValue = 0;
		try {
			for (int i = 0; i < coalItemIds.length; i++) {
				tagDataVO = opDataTagMap.get(CommonConst.class.getField("COAL_FEEDER_" + coalItemIds[i] + "_FEEDRATE").get(null));
				if (tagDataVO.getTagVal() < 10.0) {
					String[] standBySA = coalItemIdStandBySA[i];
					for (String saType : saTypes) {
						String[] arrayFDE0 = navigatorFDEMap.get(saType + "_" + standBySA[0]);
						arrayFDE0[1] = burnerD;
						navigatorFDEMap.put(saType + "_" + standBySA[0], arrayFDE0);
						
						String[] arrayFDE1 = navigatorFDEMap.get(saType + "_" + standBySA[1]);
						arrayFDE1[1] = burnerD;
						navigatorFDEMap.put(saType + "_" + standBySA[1], arrayFDE1);
					}
				} else
					cfdMillValue += Math.pow(2, i);
			}

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		// OFA - FDE 중 'D' 값을 설정한다.
		for(String uofa : uofaTypes)
			navigatorFDEMap.put(uofa, new String[] { f, ofaD, e });
		
		// Spray Balancing CFD DB를 조회한다.
		// 부하(Load) 값을 설정함.
		double load = opDataTagMap.get(CommonConst.ACTIVE_POWER_OF_GENERATOR).getTagVal();
		
		// 각 데이터 별로 +- 10% 범위내의 데이터 탐색
		// CFD 해석 데이터 가 존재하는 경우. Navigator은 UP 한다.
		// arrayFDE[1] => 공백이면  UP 한다.
		// arrayFDE[1] => 서로 같지 않으면 Hold 한다.
		String cfdControl = "U";
		NavCfdEntity navCfdEntity = this.navCfdRepository.getSprayBalancingCFDNativeQuery((int) load, String.valueOf(cfdMillValue), "00", 0.1, rhCFDDirection);
		if (navCfdEntity != null) {
			// burner 인 경우, Mill Stand by를 통해서 변경된 사항은 제외하고, CFD 해석에서 1우선 순위의 정보로 갱신한다.
			String saType = navCfdEntity.getBnrP1().toUpperCase();
			for (int i = 0; i < saItemIds.length; i++) {
				String[] arrayFDE = navigatorFDEMap.get(saType + "_" + saItemIds[i]);
				if (CommonConst.StringEmpty.equals(arrayFDE[1])) {
					arrayFDE[1] = cfdControl;
				}
			}

			// ofa 인경우, 위 에서 선택된 OFA 유량제어가 서로 다르면 Hold, 같으면 같은 방향.
			String uofaType = navCfdEntity.getOfaP1().toUpperCase();
			String[] arrayFDE = navigatorFDEMap.get(uofaType);
			if (CommonConst.StringEmpty.equals(arrayFDE[1]))
				arrayFDE[1] = cfdControl;
			else if (!cfdControl.equals(arrayFDE[1])) {
				arrayFDE[1] = "H";
			}
		}
		*/		
		
		// D (Deviation) 방향 만들기. - Spray Balancing 알고리즘 적용.
		// Swirl 제어.
		// : Horizon Flue Gas Temp. 좌/우 편차가 “Allowable Horizon Gas Temp. Deviation”보다 크거나 “CFD DB가 존재하고 Spray 좌/우 유량 편차가 Allowable Spray Flow Dev”보다 클 경우에 UP/Down 표시
		String f = CommonConst.StringEmpty;
		String ofaD = CommonConst.StringEmpty;
		String e = CommonConst.StringEmpty;		
		double leftFGTemp = opDataTagMap.get(CommonConst.HORIZON_GAS_DUCT_FLUE_GAS_T_L).getTagVal();
		double rightFGTemp = opDataTagMap.get(CommonConst.HORIZON_GAS_DUCT_FLUE_GAS_T_R).getTagVal();
		if (Math.abs(leftFGTemp - rightFGTemp) > navSettingEntity.getAllowableFlueGasTempDev()) {
			if (leftFGTemp > rightFGTemp) {
				ofaD = "U";			
			} else if (leftFGTemp < rightFGTemp) {
				ofaD = "D";
			}
		}
		
		logger.info("## leftFGTemp :" + leftFGTemp + ", rightFGTemp :" + rightFGTemp + " = " + ofaD + " (No 1 OFA)");

		// OFA - FDE 중 'D' 값을 설정한다.
		for(String uofa : uofaTypes)
			navigatorFDEMap.put(uofa, new String[] { f, ofaD, e });
		
		// Spray Balancing CFD DB를 조회한다.
		// 부하(Load) 값을 설정함.
		double load = opDataTagMap.get(CommonConst.ACTIVE_POWER_OF_GENERATOR).getTagVal();

		// CFD DB 조회에 필요한  Mill(ON) 2진수 값을 구한다.
		int cfdMillValue = 0;
		try {
			for (int i = 0; i < coalItemIds.length; i++) {
				tagDataVO = opDataTagMap.get(CommonConst.class.getField("COAL_FEEDER_" + coalItemIds[i] + "_FEEDRATE").get(null));
				if (tagDataVO.getTagVal() >= 10.0) {
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

			double leftRHMSprayFlowLeft = opDataTagMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_L_3).getTagVal();
			double leftRHESprayFlowLeft = opDataTagMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_L_3).getTagVal();
			double rightRHMSprayFlowRight = opDataTagMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_R_3).getTagVal();
			double rightRHESprayFlowRight = opDataTagMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_R_3).getTagVal();

			double leftRHSprayFlow = leftRHMSprayFlowLeft + leftRHESprayFlowLeft;
			double rightRHSprayFlow = rightRHMSprayFlowRight + rightRHESprayFlowRight;

			String cfdLeftSaD = CommonConst.StringEmpty;
			String cfdLeftOfaD = CommonConst.StringEmpty;
			String cfdRightSaD = CommonConst.StringEmpty;
			String cfdRightOfaD = CommonConst.StringEmpty;
			if (Math.abs(leftRHSprayFlow - rightRHSprayFlow) > navSettingEntity.getAllowableSprayFlowDev()) {
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
			
			for (String saItemId : saItemIds) {				
				navigatorFDEMap.get(cfdLeftSaP1 + "_" + saItemId)[1] = cfdLeftSaD;
				navigatorFDEMap.get(cfdRightSaP1 + "_" + saItemId)[1] = cfdRightSaD;
			}
			
			// OFA는 위에서 Swirl 제어 시 이미 등록된 OFA 제어방향이 있음, 유량 제어 방향과 반대일 경우 해당 OFA는 아래 표에 따름.
			// 표 : No1.   No2.   Final
			//    Down   Down    down
			//    Up     Down    Hold
			//    Down   Up      Hold
			//    Down   Hold    Down
			//    Up     Hold    Up 
			//    Hold   Hold    Hold
			String[] cfdLeftArrayFDE = navigatorFDEMap.get(cfdLeftOfaP1);
			if (CommonConst.StringEmpty.equals(cfdLeftArrayFDE[1])) // No1 값이 Hold 이면 No2 값으로 대체한다.
				cfdLeftArrayFDE[1] = cfdLeftOfaD;
			else if (!CommonConst.StringEmpty.equals(cfdLeftOfaD)) { // No2 값이 Hold 이면 No1 값그대로 내보냄. 
				if (!cfdLeftOfaD.equals(cfdLeftArrayFDE[1])) // No1, No2 값이 서로 같이 않으면  Hold로 만듬.
					cfdLeftArrayFDE[1] = "H";
			}

			String[] cfdRighArrayFDE = navigatorFDEMap.get(cfdRightOfaP1); // No1 값이 Hold 이면 No2 값으로 대체한다.
			if (CommonConst.StringEmpty.equals(cfdRighArrayFDE[1]))
				cfdRighArrayFDE[1] = cfdRightOfaD;
			else if (!CommonConst.StringEmpty.equals(cfdRightOfaD)) { // No1, No2 값이 서로 같이 않으면  Hold로 만듬.
				if (!cfdRightOfaD.equals(cfdRighArrayFDE[1]))
					cfdRighArrayFDE[1] = "H";
			}
			
			logger.info("## cfdLeftSaP1 = " + cfdLeftSaP1 + ", cfdLeftOfaP1 = " + cfdLeftOfaP1 + ", cfdRightSaP1 = " + cfdRightSaP1 + ", cfdRightOfaP1 = " + cfdRightOfaP1 );
			logger.info("## cfdLeftSaD = " + cfdLeftSaD + ", cfdLeftOfaD = " + cfdLeftOfaD + ", cfdRightSaD = " + cfdRightSaD + ", cfdRightOfaD = " + cfdRightOfaD );
			logger.info("## OFA Left = " + cfdLeftArrayFDE[1] + ", OFA Right = " + cfdRighArrayFDE[1] );
		}

		// E (Emission) 방향 만들기.
		// 로직 : 현재는 없음.
		
		// Navigator FDE Map에서 값이 빈 값인 경우는 "Hold"로 변경한다.
		for (Map.Entry<String, String[]> entry : navigatorFDEMap.entrySet()) {
			String[] value = entry.getValue();			
			for (int i = 0; i < value.length; i++) {
				if (CommonConst.StringEmpty.equals(value[i]))
					value[i] = "H";
			}
		}		
		
		// SA 1~4 까지의 정보를 생성한다.
		List<NavigatorCombustionItem> saCombustionItemList = new ArrayList<NavigatorCombustionItem>();
		try {			
			for (String saType : saTypes) {
				NavigatorCombustionItem saCombustionItem = new NavigatorCombustionItem();
				saCombustionItem.setType(saType);
				List<NavigatorCombustionItemStatus> saItemStatusList = new ArrayList<NavigatorCombustionItemStatus>();
				for (String saItemId : saItemIds) {					
					String[] navigatorFDE = navigatorFDEMap.get(saType + "_" + saItemId);
					NavResultEntity navResultEntity = navResultMap.get(new NavResultIdEntity(saType, saItemId));
					saItemStatusList.add(new NavigatorCombustionItemStatus(navResultEntity.getNavResultIdEntity().getItemId()
							, saItemId
							, navResultEntity.getYaw()
							, Utilities.roundSecondToString(opDataTagMap.get(CommonConst.class.getField(saItemId + "_DAMPER_OPEN_" + saType).get(null)).getTagVal())
							, navigatorFDE[0] + navigatorFDE[1] + navigatorFDE[2]));					
				}
				
				saCombustionItem.setSaItemStatusList(saItemStatusList);
				saCombustionItemList.add(saCombustionItem);
			}
			
			combustionStatus.setCombustionSAList(saCombustionItemList);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}		
		
		// UOFA - Left, Right, Front, Rear의 값을 생성한다.
		List<NavigatorCombustionItem> uofaCombustionItemList = new ArrayList<NavigatorCombustionItem>();
		try {
			int typeIndex = 0;
			for (String uofaType : uofaTypes) {				
				NavigatorCombustionItem uofaCombustionItem = new NavigatorCombustionItem();
				uofaCombustionItem.setType(uofaType);
				
				List<NavigatorCombustionItemStatus> uofaItemStatusList = new ArrayList<NavigatorCombustionItemStatus>();
				int itemIndex = 0;
				String[] navigatorFDE = navigatorFDEMap.get(uofaType);
				for (String uofaItemsId : uofaItemsIds) {
					NavResultEntity navResultEntity = navResultMap.get(new NavResultIdEntity(uofaType, uofaItemsId));
					uofaItemStatusList.add(new NavigatorCombustionItemStatus(
							navResultEntity.getNavResultIdEntity().getItemId(), uofaItemNames[itemIndex]
							, navResultEntity.getYaw()
							, Utilities.roundSecondToString(opDataTagMap.get(CommonConst.class.getField("W_"+uofaConsts[typeIndex]+"_"+uofaItemsId+"_DMPR_POS").get(null)).getTagVal())
							, navigatorFDE[0] + navigatorFDE[1] + navigatorFDE[2]));
					
					itemIndex++;
				}
				
				uofaItemStatusList.add(new NavigatorCombustionItemStatus("Total", "Total", "N/A", "-", "-"));
				
				tagDataVO = opDataTagMap.get(CommonConst.class.getField(uofaTypes[typeIndex] +"WALL_OFA_FLOW").get(null));
				uofaItemStatusList.add(new NavigatorCombustionItemStatus("FLOW", "Flow", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
				
				typeIndex++;
				
				uofaCombustionItem.setOfaItemStatusList(uofaItemStatusList);
				uofaCombustionItemList.add(uofaCombustionItem);
			}
			
			combustionStatus.setCombustionUOFAList(uofaCombustionItemList);			
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		// Left, Right, SA Flow.
		List<NavigatorCombustionItem> combustionFlowList = new ArrayList<NavigatorCombustionItem>();
		NavigatorCombustionItem leftCombustionFlow = new NavigatorCombustionItem();		
		leftCombustionFlow.setType("LEFT");	
		List<NavigatorCombustionItemStatus> leftSAFlowItemStatusList = new ArrayList<NavigatorCombustionItemStatus>();
		tagDataVO = opDataTagMap.get(CommonConst.APH_A_OUTLET_SA_FLOW);
		leftSAFlowItemStatusList.add(new NavigatorCombustionItemStatus("LEFT_SA_FLOW", "Left SA Flow", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		leftCombustionFlow.setFlowItemStatusList(leftSAFlowItemStatusList);		
		combustionFlowList.add(leftCombustionFlow);
		
		NavigatorCombustionItem rightCombustionFlow = new NavigatorCombustionItem();		
		rightCombustionFlow.setType("RIGHT");
		List<NavigatorCombustionItemStatus> rightSAFlowItemStatusList = new ArrayList<NavigatorCombustionItemStatus>();
		tagDataVO = opDataTagMap.get(CommonConst.APH_B_OUTLET_SA_FLOW);
		rightSAFlowItemStatusList.add(new NavigatorCombustionItemStatus("RIGHT_SA_FLOW", "Right SA Flow", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		rightCombustionFlow.setFlowItemStatusList(rightSAFlowItemStatusList);		
		combustionFlowList.add(rightCombustionFlow);
		
		NavigatorCombustionItem groupCombustionFlow = new NavigatorCombustionItem();
		groupCombustionFlow.setType("GROUP");	
		List<NavigatorCombustionItemStatus> groupFlowItemStatusList = new ArrayList<NavigatorCombustionItemStatus>();
		
		// Total Coal Flow
		tagDataVO = opDataTagMap.get(CommonConst.COAL_FEEDER_A_FEEDRATE);
		double totalCoalFlow = tagDataVO.getTagVal();
		totalCoalFlow += opDataTagMap.get(CommonConst.COAL_FEEDER_B_FEEDRATE).getTagVal();
		totalCoalFlow += opDataTagMap.get(CommonConst.COAL_FEEDER_C_FEEDRATE).getTagVal();
		totalCoalFlow += opDataTagMap.get(CommonConst.COAL_FEEDER_D_FEEDRATE).getTagVal();
		totalCoalFlow += opDataTagMap.get(CommonConst.COAL_FEEDER_E_FEEDRATE).getTagVal();
		totalCoalFlow += opDataTagMap.get(CommonConst.COAL_FEEDER_F_FEEDRATE).getTagVal();
		totalCoalFlow += opDataTagMap.get(CommonConst.COAL_FEEDER_G_FEEDRATE).getTagVal();
		groupFlowItemStatusList.add(new NavigatorCombustionItemStatus("COAL_FLOW", "Total Coal Flow",  tagDataVO.getUnit(), Utilities.roundSecondToString(totalCoalFlow)));
		
		// Total PA Flow
		tagDataVO = opDataTagMap.get(CommonConst.MILL_A_INL_PRI_AIR_FLOW_2);
		double totalPAFlow = tagDataVO.getTagVal();		
		totalPAFlow += opDataTagMap.get(CommonConst.MILL_B_INL_PRI_AIR_FLOW_2).getTagVal();
		totalPAFlow += opDataTagMap.get(CommonConst.MILL_C_INL_PRI_AIR_FLOW_2).getTagVal();
		totalPAFlow += opDataTagMap.get(CommonConst.MILL_D_INL_PRI_AIR_FLOW_2).getTagVal();
		totalPAFlow += opDataTagMap.get(CommonConst.MILL_E_INL_PRI_AIR_FLOW_2).getTagVal();
		totalPAFlow += opDataTagMap.get(CommonConst.MILL_F_INL_PRI_AIR_FLOW_2).getTagVal();
		totalPAFlow += opDataTagMap.get(CommonConst.MILL_G_INL_PRI_AIR_FLOW_2).getTagVal();
		groupFlowItemStatusList.add(new NavigatorCombustionItemStatus("PA_FLOW", "Total PA Flow",  tagDataVO.getUnit(), Utilities.roundSecondToString(totalPAFlow)));

		// Total SA Flow
		tagDataVO = opDataTagMap.get(CommonConst.APH_A_OUTLET_SA_FLOW);
		double totalSAFlow = tagDataVO.getTagVal();
		totalSAFlow += opDataTagMap.get(CommonConst.APH_B_OUTLET_SA_FLOW).getTagVal();
		groupFlowItemStatusList.add(new NavigatorCombustionItemStatus("SA_FLOW", "Total SA Flow", tagDataVO.getUnit(), Utilities.roundSecondToString(totalSAFlow)));

		// Total OFA Flow
		tagDataVO = opDataTagMap.get(CommonConst.LEFTWALL_OFA_FLOW);
		double totalOFAFlow = tagDataVO.getTagVal();
		totalOFAFlow += opDataTagMap.get(CommonConst.RIGHTWALL_OFA_FLOW).getTagVal();
		totalOFAFlow += opDataTagMap.get(CommonConst.FRONTWALL_OFA_FLOW).getTagVal();
		totalOFAFlow += opDataTagMap.get(CommonConst.REARWALL_OFA_FLOW).getTagVal();
		groupFlowItemStatusList.add(new NavigatorCombustionItemStatus("OFA_FLOW", "Total OFA Flow", tagDataVO.getUnit(), Utilities.roundSecondToString(totalOFAFlow)));
		
		// Total BNR SA Flow
		double totalBNRSAFlow = totalSAFlow - totalOFAFlow;
		groupFlowItemStatusList.add(new NavigatorCombustionItemStatus("BNR_SA_FLOW", "Total BNR SA Flow", tagDataVO.getUnit(), Utilities.roundSecondToString(totalBNRSAFlow)));
		
		// BZS (Burner Zone Stoich)		
		double bzs = (totalPAFlow + totalBNRSAFlow) / (totalPAFlow + totalSAFlow) * navSettingEntity.getExcessAirRatio();
		groupFlowItemStatusList.add(new NavigatorCombustionItemStatus("BZS", "BZS", "", Utilities.roundSecondToString(bzs)));
		groupCombustionFlow.setFlowItemStatusList(groupFlowItemStatusList);
		combustionFlowList.add(groupCombustionFlow);
		
		combustionStatus.setCombustionFlowList(combustionFlowList);
		
		// Coal Supply Status.
		List<NavigatorCombustionCoalSupplyStatus> combustionCoalSupplyStatusList = new ArrayList<NavigatorCombustionCoalSupplyStatus>();
		try {			
			for (String coalItemId : coalItemIds) {
				NavResultEntity navResultEntityC1 = navResultMap.get(new NavResultIdEntity("C1", coalItemId));
				NavResultEntity navResultEntityC2 = navResultMap.get(new NavResultIdEntity("C2", coalItemId));
				NavResultEntity navResultEntityC3 = navResultMap.get(new NavResultIdEntity("C3", coalItemId));
				NavResultEntity navResultEntityC4 = navResultMap.get(new NavResultIdEntity("C4", coalItemId));
	
				tagDataVO = opDataTagMap.get(CommonConst.class.getField("COAL_FEEDER_"+coalItemId+"_FEEDRATE").get(null));
				combustionCoalSupplyStatusList.add(new NavigatorCombustionCoalSupplyStatus(coalItemId
						, coalItemId
						, tagDataVO.getTagVal() >= 10.0 ? "warning" : "normal"
						, Utilities.roundSecondToString(tagDataVO.getTagVal())
						, tagDataVO.getUnit()
						, navResultEntityC1.getYaw()
						, "-"
						, navResultEntityC2.getYaw()
						, "-"
						, navResultEntityC3.getYaw()
						, "-"
						, navResultEntityC4.getYaw()
						, "-"));
			}
			
			combustionStatus.setCoalSupplyStatusList(combustionCoalSupplyStatusList);
			
		} catch (Exception ex) {
			throw new RuntimeException(e);
		}
		
		int x = (int) ((tubeWWTempVO.getRearTempAvg() / (tubeWWTempVO.getRearTempAvg() + tubeWWTempVO.getFrontTempAvg())) * 100);
		int y = (int) ((tubeWWTempVO.getLeftTempAvg() / (tubeWWTempVO.getLeftTempAvg() + tubeWWTempVO.getRightTempAvg())) * 100);		
		NavigatorCombustionSpiralWaterWall spiralWaterWall = new NavigatorCombustionSpiralWaterWall();		
		List<ItemStatus> spiralWaterWallTempList = new ArrayList<ItemStatus>();
		spiralWaterWallTempList.add(new ItemStatus("front", CommonConst.StringEmpty, Utilities.roundFirstToString(tubeWWTempVO.getFrontTempAvg())));
		spiralWaterWallTempList.add(new ItemStatus("rear", CommonConst.StringEmpty, Utilities.roundFirstToString(tubeWWTempVO.getRearTempAvg())));
		spiralWaterWallTempList.add(new ItemStatus("left", CommonConst.StringEmpty, Utilities.roundFirstToString(tubeWWTempVO.getLeftTempAvg())));
		spiralWaterWallTempList.add(new ItemStatus("right", CommonConst.StringEmpty, Utilities.roundFirstToString(tubeWWTempVO.getRightTempAvg())));
		spiralWaterWall.setSpiralWaterWallTempList(spiralWaterWallTempList);
		spiralWaterWall.setFireBallPostion(new Position(x, y));
		
		combustionStatus.setNavigatorCombustionSpiralWaterWall(spiralWaterWall);
		navigator.setCombustionStatus(combustionStatus);
		
		// Gas Press And Temp Data.
		NavigatorGasPressAndTemp gasPressAndTemp = new NavigatorGasPressAndTemp();
		List<ItemStatus> dataList = new ArrayList<ItemStatus>();
		
		String dummyTempUnit = opDataTagMap.get(CommonConst.HRZN_FG_temp_R).getUnit();
		String dummyPressUnit = opDataTagMap.get(CommonConst.FINAL_RH_OL_FG_Press_L).getUnit();
		dataList.add(new ItemStatus("platen_sh_out_temp_left", dummyTempUnit, "-"));		
		dataList.add(new ItemStatus("platen_sh_out_temp_right", dummyTempUnit, "-"));
		dataList.add(new ItemStatus("platen_sh_out_press_left", dummyPressUnit, "-"));
		dataList.add(new ItemStatus("platen_sh_out_press_right", dummyPressUnit, "-"));
		tagDataVO = opDataTagMap.get(CommonConst.SH_DSH2_SPRAY_FLOW_3_L);		
		dataList.add(new ItemStatus("sh_dsh2_spray_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.SH_DSH2_SPRAY_FLOW_3_R);
		dataList.add(new ItemStatus("sh_dsh2_spray_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_L_3);
		dataList.add(new ItemStatus("rh_dsh2_spray_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.RH_M_DSH_SPRAY_FLOW_R_3);				
		dataList.add(new ItemStatus("rh_dsh2_spray_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.HRZN_FG_temp_L);
		dataList.add(new ItemStatus("horizontal_gas_temp_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));

		tagDataVO = opDataTagMap.get(CommonConst.HRZN_FG_temp_R);
		dataList.add(new ItemStatus("horizontal_gas_temp_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.HRZN_FG_DRAFT_L);
		dataList.add(new ItemStatus("horizontal_gas_press_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.HRZN_FG_DRAFT_R);
		dataList.add(new ItemStatus("horizontal_gas_press_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.FINAL_RH_OL_FG_temp_L);
		dataList.add(new ItemStatus("rh_finish_out_temp_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.FINAL_RH_OL_FG_temp_R);
		dataList.add(new ItemStatus("rh_finish_out_temp_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.FINAL_RH_OL_FG_Press_L);
		dataList.add(new ItemStatus("rh_finish_out_press_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.FINAL_RH_OL_FG_Press_R);
		dataList.add(new ItemStatus("rh_finish_out_press_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.PRM_RH_IN_FG_Press_L);
		dataList.add(new ItemStatus("primry_rh_in_flue_gas_press_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.PRM_RH_IL_FG_Press_R);
		dataList.add(new ItemStatus("primry_rh_in_flue_gas_press_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.PRM_RH_OUT_FG_Press_L);
		dataList.add(new ItemStatus("primry_rh_out_flue_gas_press_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.PRM_RH_OL_FG_Press_R);
		dataList.add(new ItemStatus("primry_rh_out_flue_gas_press_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.PRM_SH_IN_FG_Press_L);
		dataList.add(new ItemStatus("primry_sh_in_flue_gas_press_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.PRM_SH_IL_FG_Press_R);
		dataList.add(new ItemStatus("primry_sh_in_flue_gas_press_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.PRM_SH_OUT_FG_Press_L);
		dataList.add(new ItemStatus("primry_sh_out_flue_gas_press_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.PRM_SH_OL_FG_Press_R);
		dataList.add(new ItemStatus("primry_sh_out_flue_gas_press_rigt", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_L_3);
		dataList.add(new ItemStatus("rh_dsh1_spray_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.RH_E_DSH_SPRAY_FLOW_R_3);		
		dataList.add(new ItemStatus("rh_dsh1_spray_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));		
		
		tagDataVO = opDataTagMap.get(CommonConst.SH_DSH1_SPRAY_FLOW_3_L);
		dataList.add(new ItemStatus("sh_dsh1_spray_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.SH_DSH1_SPRAY_FLOW_3_R);		
		dataList.add(new ItemStatus("sh_dsh1_spray_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));	
		
		tagDataVO = opDataTagMap.get(CommonConst.LFT_FG_ADJ_DMPR_A_POS);
		dataList.add(new ItemStatus("econ_gas_damper_position_a_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.LFT_FG_ADJ_DMPR_B_POS);
		dataList.add(new ItemStatus("econ_gas_damper_position_b_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.LFT_FG_ADJ_DMPR_C_POS);
		dataList.add(new ItemStatus("econ_gas_damper_position_c_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.RGT_FG_ADJ_DMPR_A_POS);
		dataList.add(new ItemStatus("econ_gas_damper_position_a_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.RGT_FG_ADJ_DMPR_B_POS);
		dataList.add(new ItemStatus("econ_gas_damper_position_b_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.RGT_FG_ADJ_DMPR_C_POS);
		dataList.add(new ItemStatus("econ_gas_damper_position_c_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.ECON_OL_FG_Press_L);
		dataList.add(new ItemStatus("econ_out_flue_gas_press_left", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		tagDataVO = opDataTagMap.get(CommonConst.ECON_OL_FG_Press_R);
		dataList.add(new ItemStatus("econ_out_flue_gas_press_right", tagDataVO.getUnit(), Utilities.roundSecondToString(tagDataVO.getTagVal())));
		
		gasPressAndTemp.setDataList(dataList);
		
		// gas Analyzer Item (o2, co, nox, temp)
		List<NavigatorGasAnalyzerItem> gasAnalyzerItemList = new ArrayList<NavigatorGasAnalyzerItem>();
		gasAnalyzerItemList.add(new NavigatorGasAnalyzerItem("O2"));
		gasAnalyzerItemList.add(new NavigatorGasAnalyzerItem("CO"));
		gasAnalyzerItemList.add(new NavigatorGasAnalyzerItem("NOx"));
		gasAnalyzerItemList.add(new NavigatorGasAnalyzerItem("T"));	
		gasPressAndTemp.setGasAnalyzerItemList(gasAnalyzerItemList);
		
		navigator.setGasPressAndTemp(gasPressAndTemp);
		
		return navigator;
	}
	
	@Transactional
	public void saveNavigatorInfo(Navigator navigator) {
		NavigatorCombustionStatus combustionStatus = navigator.getCombustionStatus();
		
		// SA Yaw Update.
		List<NavigatorCombustionItem> saCombustionItemList = combustionStatus.getCombustionSAList();
		for (NavigatorCombustionItem saCombustionItem : saCombustionItemList) {
			for (NavigatorCombustionItemStatus saItemStatus : saCombustionItem.getSaItemStatusList()) {
				this.navResultRespository.updateByGroupIdAndItemIdSetYaw(saCombustionItem.getType(), saItemStatus.getItemId(), saItemStatus.getYaw());
			}
		}
		
		// UOFA Yaw Update.
		List<NavigatorCombustionItem> uofaCombustionItemList = combustionStatus.getCombustionUOFAList();
		for (NavigatorCombustionItem uofaCombustionItem : uofaCombustionItemList) {
			for (NavigatorCombustionItemStatus uofaItemStatus : uofaCombustionItem.getOfaItemStatusList()) {
				this.navResultRespository.updateByGroupIdAndItemIdSetYaw(uofaCombustionItem.getType(), uofaItemStatus.getItemId(), uofaItemStatus.getYaw());
			}
		}
		
		// Coal Yaw Update.
		List<NavigatorCombustionCoalSupplyStatus> combustionCoalSupplyStatusList = combustionStatus.getCoalSupplyStatusList();
		for (NavigatorCombustionCoalSupplyStatus combustionCoalSupplyStatus : combustionCoalSupplyStatusList) {
			this.navResultRespository.updateByGroupIdAndItemIdSetYaw("C1", combustionCoalSupplyStatus.getItemId(), combustionCoalSupplyStatus.getYaw1());
			this.navResultRespository.updateByGroupIdAndItemIdSetYaw("C2", combustionCoalSupplyStatus.getItemId(), combustionCoalSupplyStatus.getYaw2());
			this.navResultRespository.updateByGroupIdAndItemIdSetYaw("C3", combustionCoalSupplyStatus.getItemId(), combustionCoalSupplyStatus.getYaw3());
			this.navResultRespository.updateByGroupIdAndItemIdSetYaw("C4", combustionCoalSupplyStatus.getItemId(), combustionCoalSupplyStatus.getYaw4());
		}
	}	

	public NavigatorConfig getNavigatorConfig() {
		NavigatorConfig navigatorConfig = new NavigatorConfig();
		NavSettingEntity navSettingEntity = this.navSettingRepository.findBySettingId(NavSettingEntity.defaultID);
		if (navSettingEntity != null) {
			navigatorConfig.setExcessAirRatio(navSettingEntity.getExcessAirRatio());
			navigatorConfig.setMetalTempFilterRate(navSettingEntity.getMetalTempDevRate());
			navigatorConfig.setMetalTempAllowableVlaue(navSettingEntity.getAllowableMetalTemp());
			navigatorConfig.setAllowableFlueGasTempDev(navSettingEntity.getAllowableFlueGasTempDev());
			navigatorConfig.setAllowableSprayFlowDev(navSettingEntity.getAllowableSprayFlowDev());
		}

		return navigatorConfig;
	}

	public void saveNavigatorConfig(NavigatorConfig navigatorConfig) {

		NavSettingEntity navSettingEntity = this.navSettingRepository.findBySettingId(NavSettingEntity.defaultID);
		if (navSettingEntity == null)
			navSettingEntity = new NavSettingEntity();

		navSettingEntity.setExcessAirRatio(navigatorConfig.getExcessAirRatio());
		navSettingEntity.setMetalTempDevRate(navigatorConfig.getMetalTempFilterRate());
		navSettingEntity.setAllowableMetalTemp(navigatorConfig.getMetalTempAllowableVlaue());
		navSettingEntity.setAllowableFlueGasTempDev(navigatorConfig.getAllowableFlueGasTempDev());
		navSettingEntity.setAllowableSprayFlowDev(navigatorConfig.getAllowableSprayFlowDev());
		
		this.navSettingRepository.save(navSettingEntity);
	}
	
	public NavigatorTubeWallMetalTemp getNavigatorTubeWallMetalTempInfo(MetalTempTubeType metalTempTubeType) {
		NavigatorTubeWallMetalTemp spiralWallMetalTemp = new NavigatorTubeWallMetalTemp();

		// Front, Rear, Left, Right 벽면의 평균 온도를 구하고, Metal Temp. Dev. For Filtering'설정
		List<Object[]> navTubeWWTempList = this.navTubeWWTempConfRepository.getNavTubeWWTempList(metalTempTubeType.name());
		NavSettingEntity navSettingEntity = this.navSettingRepository.findBySettingId(NavSettingEntity.defaultID);		
		List<NavigatorTubeWallTempItem> tubeWallTempFrontItemList = new ArrayList<NavigatorTubeWallTempItem>();
		List<NavigatorTubeWallTempItem> tubeWallTempRearItemList = new ArrayList<NavigatorTubeWallTempItem>();
		List<NavigatorTubeWallTempItem> tubeWallTempLeftItemList = new ArrayList<NavigatorTubeWallTempItem>();
		List<NavigatorTubeWallTempItem> tubeWallTempRightItemList = new ArrayList<NavigatorTubeWallTempItem>();
		
		double maxTemp = Double.MIN_VALUE;
		for (Object[] objectTempArray : navTubeWWTempList) {
			String direction = String.valueOf(objectTempArray[0]);
			int tubeNo = (int) objectTempArray[1];
			Object objTempVal = objectTempArray[2];
			String tempValString = CommonConst.StringEmpty;
			String percentage = CommonConst.StringEmpty;
			
			if (objTempVal != null) {
				double tempVal = (double) objTempVal;
				tempValString = Utilities.roundFirstToString(tempVal);
				percentage = String.valueOf((int)((double)objectTempArray[3]));
				
				if(maxTemp < tempVal)
					maxTemp = tempVal;
			}
			
			switch (direction) {
			case "FRONT":
				tubeWallTempFrontItemList.add(new NavigatorTubeWallTempItem(tubeNo, tempValString, percentage));
				break;
			case "REAR":
				tubeWallTempRearItemList.add(new NavigatorTubeWallTempItem(tubeNo, tempValString, percentage));
				break;
			case "LEFT":
				tubeWallTempLeftItemList.add(new NavigatorTubeWallTempItem(tubeNo, tempValString, percentage));
				break;
			case "RIGHT":
				tubeWallTempRightItemList.add(new NavigatorTubeWallTempItem(tubeNo, tempValString, percentage));
				break;
			}
		}		
		
		spiralWallMetalTemp.setMaxMetalTemp(Utilities.roundFirstToString(maxTemp));
		
		List<Double> frontTempList = new ArrayList<Double>();
		List<Double> rearTempList = new ArrayList<Double>();
		List<Double> leftTempList = new ArrayList<Double>();
		List<Double> rightTempList = new ArrayList<Double>();
		for (Object[] objectTempArray : navTubeWWTempList) {
			Object objTempVal = objectTempArray[2];
			if (objTempVal != null) {
				String direction = String.valueOf(objectTempArray[0]);
				switch (direction) {
				case "FRONT":
					frontTempList.add((double) objTempVal);
					break;
				case "REAR":
					rearTempList.add((double) objTempVal);
					break;
				case "LEFT":
					leftTempList.add((double) objTempVal);
					break;
				case "RIGHT":
					rightTempList.add((double) objTempVal);
					break;
				}
			}
		}
		
		NavigatorTubeWWTempVO tubeWWTempVO = new NavigatorTubeWWTempVO(frontTempList, rearTempList, leftTempList, rightTempList, navSettingEntity.getMetalTempDevRate(), navSettingEntity.getAllowableMetalTemp());
		spiralWallMetalTemp.setTubeWallFrontItem(new NavigatorTubeWallItem(Utilities.roundFirstToString(tubeWWTempVO.getFrontTempAvg()), tubeWallTempFrontItemList));
		spiralWallMetalTemp.setTubeWallRearItem(new NavigatorTubeWallItem(Utilities.roundFirstToString(tubeWWTempVO.getRearTempAvg()), tubeWallTempRearItemList));
		spiralWallMetalTemp.setTubeWallLeftItem(new NavigatorTubeWallItem(Utilities.roundFirstToString(tubeWWTempVO.getLeftTempAvg()), tubeWallTempLeftItemList));
		spiralWallMetalTemp.setTubeWallRightItem(new NavigatorTubeWallItem(Utilities.roundFirstToString(tubeWWTempVO.getRightTempAvg()), tubeWallTempRightItemList));

		return spiralWallMetalTemp;
	}
	
	public NavigatorWallMatchingTable getNavigatorWallMatchingTableInfo() {
		NavigatorWallMatchingTable wallMatchingTable = new NavigatorWallMatchingTable();

		List<NavigatorWallMatchingTable> wallMatchingTableDeviationList = new ArrayList<NavigatorWallMatchingTable>();
		List<NavigatorWallMatchingTable> wallMatchingTableHighLowList = new ArrayList<NavigatorWallMatchingTable>();
		
		List<NavWWMatchingEntity> navWWMatchingEntityList = this.navWWMatchingRepository.findAllOrderByTypeAscOrderByMatchingOrderAsc();
		for (NavWWMatchingEntity navWWMatchingEntity : navWWMatchingEntityList) {

			NavigatorWallMatchingTable wallMatchingTableDeviation;
			NavigatorWallMatchingTable wallMatchingTableHighLow;			
			NavWWMatchingIdEntity navWWMatchingIdEntity = navWWMatchingEntity.getNavWWMatchingIdEntity();
			switch (navWWMatchingIdEntity.getType()) {
			case "Deviation":
				wallMatchingTableDeviation = new NavigatorWallMatchingTable();
				wallMatchingTableDeviation.setP1(navWWMatchingIdEntity.getP1());
				wallMatchingTableDeviation.setP2(navWWMatchingIdEntity.getP2());
				wallMatchingTableDeviation.setP3(navWWMatchingIdEntity.getP3());
				wallMatchingTableDeviation.setP4(navWWMatchingIdEntity.getP4());

				String arrayc1[] = navWWMatchingEntity.getC1().split("_");
				String arrayc2[] = navWWMatchingEntity.getC2().split("_");
				String arrayc3[] = navWWMatchingEntity.getC3().split("_");
				String arrayc4[] = navWWMatchingEntity.getC4().split("_");

				wallMatchingTableDeviation.setAa1(arrayc1[0]);
				wallMatchingTableDeviation.setAa2(arrayc2[0]);
				wallMatchingTableDeviation.setAa3(arrayc3[0]);
				wallMatchingTableDeviation.setAa4(arrayc4[0]);

				wallMatchingTableDeviation.setAbfg1(arrayc1[1]);
				wallMatchingTableDeviation.setAbfg2(arrayc2[1]);
				wallMatchingTableDeviation.setAbfg3(arrayc3[1]);
				wallMatchingTableDeviation.setAbfg4(arrayc4[1]);
				
				wallMatchingTableDeviationList.add(wallMatchingTableDeviation);				
				break;
			case "HighLow":
				wallMatchingTableHighLow = new NavigatorWallMatchingTable();

				wallMatchingTableHighLow.setP1(navWWMatchingIdEntity.getP1());
				wallMatchingTableHighLow.setP2(navWWMatchingIdEntity.getP2());

				wallMatchingTableHighLow.setGg1(navWWMatchingEntity.getC1());
				wallMatchingTableHighLow.setGg2(navWWMatchingEntity.getC2());
				wallMatchingTableHighLow.setGg3(navWWMatchingEntity.getC3());
				wallMatchingTableHighLow.setGg4(navWWMatchingEntity.getC4());

				wallMatchingTableHighLowList.add(wallMatchingTableHighLow);
				break;
			}
		}

		wallMatchingTable.setWallMatchingTableDeviationList(wallMatchingTableDeviationList);
		wallMatchingTable.setWallMatchingTableHighLowList(wallMatchingTableHighLowList);

		return wallMatchingTable;
	}
	
	@Transactional
	public void saveNavigatorWallMatchingTable(NavigatorWallMatchingTable wallMatchingTable) {
		// C1, C2, C3, C4 => null 있으면 버그..		
		// 유효성 체크함.
		/*
		boolean isDuplicationVaild = false;
		String[] highCornerDuplicationArray = new String[4];
		String[] lowCornerDuplicationArray = new String[4];
		
		for (NavigatorWallMatchingTable wallMatchingTableHighLow : wallMatchingTable.getWallMatchingTableHighLowList()) {
			switch (wallMatchingTableHighLow.getP2()) {
			case "Hight":
				highCornerDuplicationArray[0] = wallMatchingTableHighLow.getGg1();
				highCornerDuplicationArray[1] = wallMatchingTableHighLow.getGg2();
				highCornerDuplicationArray[2] = wallMatchingTableHighLow.getGg3();
				highCornerDuplicationArray[3] = wallMatchingTableHighLow.getGg4();			
				
				break;
			case "Low":
				lowCornerDuplicationArray[0] = wallMatchingTableHighLow.getGg1();
				lowCornerDuplicationArray[1] = wallMatchingTableHighLow.getGg2();
				lowCornerDuplicationArray[2] = wallMatchingTableHighLow.getGg3();
				lowCornerDuplicationArray[3] = wallMatchingTableHighLow.getGg4();
				break;
			}			
		}
		
		// 단면 , High 값 중 C1, C2, C3, C4 에 Hold가 있는 경우 에러처리한 
		for (String highCornerDuplication : highCornerDuplicationArray) {
			if (CommonConst.StringEmpty.equals(highCornerDuplication) || "H".equals(highCornerDuplication)) {
				isDuplicationVaild = true;
				break;
			}
		}
		
		if (!isDuplicationVaild) {
			for (String lowCornerDuplication : lowCornerDuplicationArray) {
				if (CommonConst.StringEmpty.equals(lowCornerDuplication) || "H".equals(lowCornerDuplication)) {
					isDuplicationVaild = true;
					break;
				}
			}
		}*/
		
		for (NavigatorWallMatchingTable wallMatchingTableDeviation : wallMatchingTable.getWallMatchingTableDeviationList()) {
			this.navWWMatchingRepository.updateByDeviation(wallMatchingTableDeviation.getP1()
					, wallMatchingTableDeviation.getP2()
					, wallMatchingTableDeviation.getP3()
					, wallMatchingTableDeviation.getP4()
					, "aa"
					, wallMatchingTableDeviation.getAa1()
					, wallMatchingTableDeviation.getAa2()
					, wallMatchingTableDeviation.getAa3()
					, wallMatchingTableDeviation.getAa4());

			this.navWWMatchingRepository.updateByDeviation(wallMatchingTableDeviation.getP1()
					, wallMatchingTableDeviation.getP2()
					, wallMatchingTableDeviation.getP3()
					, wallMatchingTableDeviation.getP4()
					, "ab_fg"
					, wallMatchingTableDeviation.getAbfg1()
					, wallMatchingTableDeviation.getAbfg2()
					, wallMatchingTableDeviation.getAbfg3()
					, wallMatchingTableDeviation.getAbfg4());
		}

		for (NavigatorWallMatchingTable wallMatchingTableHighLow : wallMatchingTable.getWallMatchingTableHighLowList()) {
			this.navWWMatchingRepository.updateByHighLow(wallMatchingTableHighLow.getP1()
					, wallMatchingTableHighLow.getP2()
					, "gg"
					, wallMatchingTableHighLow.getGg1()
					, wallMatchingTableHighLow.getGg2()
					, wallMatchingTableHighLow.getGg3()
					, wallMatchingTableHighLow.getGg4());
		}
	}

	public NavigatorLimitsAndFireBallCenter getNavigatorLimitAndFireBallCenter() {
		NavigatorLimitsAndFireBallCenter limitsAndFireBallCenter = new NavigatorLimitsAndFireBallCenter();
		List<NavigatorLimit> limitList = new ArrayList<NavigatorLimit>();

		List<NavConfEntity> navConfEntityList = this.navConfRepository.findByOrderByItemOrderAsc();
		for (NavConfEntity navConfEntity : navConfEntityList) {
			NavigatorLimit limit = new NavigatorLimit();
			limit.setItem(navConfEntity.getItemId());
			limit.setTitle(navConfEntity.getItemNm());
			limit.setHigh(navConfEntity.getHighVal());
			limit.setMargin(navConfEntity.getMarginVal());
			limit.setUnit(navConfEntity.getUnit());
			limit.setType(navConfEntity.getConfType());
			limit.setUse(navConfEntity.getUseYn());
			limitList.add(limit);
		}

		limitsAndFireBallCenter.setLimitList(limitList);

		List<NavigatorFireBallCenter> fireBallCenterList = new ArrayList<NavigatorFireBallCenter>();
		List<NavFireBallEntity> navFireBallEntityList = this.navFireBallRepository.findByTypeOrderByDirectionOrderAsc(FireBallCenterType.FTMS.name());
		for (NavFireBallEntity navFireBallEntity : navFireBallEntityList) {
			NavigatorFireBallCenter fireBallCenter = new NavigatorFireBallCenter();
			fireBallCenter.setDirection(navFireBallEntity.getDirection());
			fireBallCenter.setCorner1(navFireBallEntity.isCorner1());
			fireBallCenter.setCorner2(navFireBallEntity.isCorner2());
			fireBallCenter.setCorner3(navFireBallEntity.isCorner3());
			fireBallCenter.setCorner4(navFireBallEntity.isCorner4());
			fireBallCenterList.add(fireBallCenter);
		}

		limitsAndFireBallCenter.setFireBallCenterList(fireBallCenterList);

		return limitsAndFireBallCenter;
	}
	
	public List<KeyValue> getNavigatorTrendCategoryDDL() {
		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		for(Map.Entry<String, OptCodeVO> entry:StaticMap.NavigatorTrendCategoryMap.entrySet()) {
			OptCodeVO optCode = entry.getValue();
			keyValueList.add(new KeyValue(optCode.getCodeId(), optCode.getCodeNm()));
		}
		
		return keyValueList;
	}
	
	public Chart getNavigatorTrendData(String item, String startDateString, String endDateString) throws InvalidParameterException {
		Chart chart = new Chart();
		try {

			Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateString);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateString);

			int SecondsInterval = 30;
			long diff = endDate.getTime() - startDate.getTime();
			long diffSeconds = diff / 1000;
			int modSec = (int) ((diffSeconds / CommonConst.DATE_SECONDS_OF_DAY) + 1) * SecondsInterval;
			
			List<NavTrendItemChartEntity> navTrendItemChartEntityList = this.navTrendItemChartRepository.findByItemOrderByAliasOrderAsc(item);
			List<Object[]> trendDataList;
			ArrayList<Tag> taglist = new ArrayList<Tag>();
			
			Collection<String> dataTypes = new ArrayList<String>();
			for(NavTrendItemChartEntity navTrendItemChartEntity:navTrendItemChartEntityList) {
				dataTypes.add(navTrendItemChartEntity.getDataType());
			}
			
			// Timestamp의 값을 Group by 할 때 다수의 태그로 하면 느리기 때문에 하나의 태그로 날짜를 추출하여 사용하기 위함.
			String defaultTagNm = this.commonDataService.getOpDataConfMap(CommonConst.TOTAL_COAL_FLOW).getTagNm();
			switch (item) {
			case NAV_ITEM_FIRE_DISTANCE_CHG:
				break;
				
			case NAV_ITEM_VRT_WTR_WL_TEMP_MEAN:
			case NAV_ITEM_SPR_WTR_WL_TEMP_MEAN:
				trendDataList = this.opDataInputRepository.getNavigatorTrendAvgValueByAlias(defaultTagNm, dataTypes, startDate, endDate, modSec);
				taglist = this.navigatorTrendChartTag(navTrendItemChartEntityList, trendDataList);
				break;
				
			case NAV_ITEM_VRT_WTR_WL_TEMP_MAX:
			case NAV_ITEM_SPR_WTR_WL_TEMP_MAX:
				trendDataList = this.opDataInputRepository.getNavigatorTrendMaxValueByAlias(defaultTagNm, dataTypes, startDate, endDate, modSec);
				taglist = this.navigatorTrendChartTag(navTrendItemChartEntityList, trendDataList);
				break;
				
			case NAV_ITEM_PA_FLOW:
			case NAV_ITEM_SA_FLOW:
			case NAV_ITEM_OFA_FLOW:
			case NAV_ITEM_SH_SP_1_FLOW:
			case NAV_ITEM_SH_SP_2_FLOW:
			case NAV_ITEM_RH_SP_1_FLOW:
			case NAV_ITEM_RH_SP_2_FLOW:
			case NAV_ITEM_STACK_NOX:
			case NAV_ITEM_STACK_O2:
			case NAV_ITEM_STACK_CO:
				trendDataList = this.opDataInputRepository.getNavigatorTrendValueByAlias(defaultTagNm, dataTypes, startDate, endDate, modSec);
				taglist = this.navigatorTrendChartTag(navTrendItemChartEntityList, trendDataList);
				break;				
			case NAV_ITEM_BZS:
				trendDataList = this.opDataInputRepository.getNavigatorTrendBZS(defaultTagNm, startDate, endDate, modSec);
				taglist = this.navigatorTrendChartTag(navTrendItemChartEntityList, trendDataList);
				break;				
			case NAV_ITEM_FN_SH_TU_TEMP:				
			case NAV_ITEM_FN_RH_TU_TEMP:
				dataTypes = new ArrayList<String>();
				dataTypes.add(item);
				
				trendDataList = this.opDataInputRepository.getNavigatorTrendAvgMaxMinValueByAlias(defaultTagNm, dataTypes, startDate, endDate, modSec);
				ArrayList<HashMap<String, Object>> trandDataObjAvg = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> trandDataObjMax = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> trandDataObjMin = new ArrayList<HashMap<String, Object>>();
				for (Object[] trendDataEntity : trendDataList) {
					Date trendDate = (Date) trendDataEntity[0];
					HashMap<String, Object> trendObjectAvg = new HashMap<String, Object>();
					HashMap<String, Object> trendObjectMax = new HashMap<String, Object>();
					HashMap<String, Object> trendObjectMin = new HashMap<String, Object>();

					trendObjectAvg.put(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDate), String.valueOf(trendDataEntity[1]));
					trandDataObjAvg.add(trendObjectAvg);

					trendObjectMax.put(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDate), String.valueOf(trendDataEntity[2]));
					trandDataObjMax.add(trendObjectMax);

					trendObjectMin.put(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDate), String.valueOf(trendDataEntity[3]));
					trandDataObjMin.add(trendObjectMin);
				}

				Tag tagAvg = new Tag();
				NavTrendItemChartEntity navTrendItemChartEntity = navTrendItemChartEntityList.get(0);
				tagAvg.setName(navTrendItemChartEntity.getAlias());
				tagAvg.setUnit(navTrendItemChartEntity.getUnit());
				tagAvg.setTrend(trandDataObjAvg);
				taglist.add(tagAvg);

				Tag tagMax = new Tag();
				navTrendItemChartEntity = navTrendItemChartEntityList.get(1);
				tagMax.setName(navTrendItemChartEntity.getAlias());
				tagMax.setUnit(navTrendItemChartEntity.getUnit());
				tagMax.setTrend(trandDataObjMax);
				taglist.add(tagMax);

				Tag tagMin = new Tag();
				navTrendItemChartEntity = navTrendItemChartEntityList.get(2);
				tagMin.setName(navTrendItemChartEntity.getAlias());
				tagMin.setUnit(navTrendItemChartEntity.getUnit());
				tagMin.setTrend(trandDataObjMin);
				taglist.add(tagMin);

				break;	
			}
			
			chart.setTag(taglist.toArray(new Tag[taglist.size()]));

		} catch (ParseException e) {
			throw new InvalidParameterException(e.getMessage());
		}

		return chart;
	}
	
	private ArrayList<Tag> navigatorTrendChartTag(List<NavTrendItemChartEntity> navTrendItemChartEntityList, List<Object[]> trendDataList) {
		ArrayList<Tag> taglist = new ArrayList<Tag>();
		for (NavTrendItemChartEntity navTrendItemChartEntity : navTrendItemChartEntityList) {

			Tag tag = new Tag();
			tag.setName(navTrendItemChartEntity.getAlias());
			tag.setUnit(navTrendItemChartEntity.getUnit());

			ArrayList<HashMap<String, Object>> trandDataObj = new ArrayList<HashMap<String, Object>>();
			for (Object[] trendDataEntity : trendDataList) {

				if (navTrendItemChartEntity.getDataType().equals(String.valueOf(trendDataEntity[0]))) {
					Date trendDate = (Date) trendDataEntity[1];
					HashMap<String, Object> trendObject = new HashMap<String, Object>();
					trendObject.put(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDate), String.valueOf(trendDataEntity[2]));
					trandDataObj.add(trendObject);
				}
			}

			tag.setTrend(trandDataObj);
			taglist.add(tag);
		}

		return taglist;
	}
	
	@Transactional
	public void saveNavigatorLimitAndFireBallCenter(NavigatorLimitsAndFireBallCenter limitsAndFireBallCenter) {
		
		List<NavigatorLimit> limitList = limitsAndFireBallCenter.getLimitList();
		for (NavigatorLimit limit : limitList) {
			this.navConfRepository.updateByItemIdSetHighAndMarginAndUseYn(limit.getItem(), limit.getHigh().doubleValue(), limit.getMargin().doubleValue(), limit.isUse());
		}

		List<NavigatorFireBallCenter> fireBallCenterList = limitsAndFireBallCenter.getFireBallCenterList();
		for (NavigatorFireBallCenter fireBallCenter : fireBallCenterList) {
			this.navFireBallRepository.updateByDirectionSetFireBallCenter(FireBallCenterType.FTMS.name(), fireBallCenter.getDirection(), fireBallCenter.isCorner1(), fireBallCenter.isCorner2(), fireBallCenter.isCorner3(), fireBallCenter.isCorner4());
		}
	}
	
	public AlgorithmNNConfig getAlgorithmNNConfig() {
		AlgorithmNNConfig nnConfig = new AlgorithmNNConfig();

		NNConfigEntity nnConfigEntity = this.nnConfigRepository.findByConfId(NNConfigEntity.defaultID);
		if (nnConfigEntity != null) {
			nnConfig.setLearningRate(nnConfigEntity.getLearningRate());
			nnConfig.setMomentum(nnConfigEntity.getMomentum());
			nnConfig.setSigmoidAlphaValue(nnConfigEntity.getSigmoidAlphaVal());
			nnConfig.setNeruons1stLayer(nnConfigEntity.getNeuronCntLayer1());
			nnConfig.setNeruons2ndLayer(nnConfigEntity.getNeuronCntLayer2());
			nnConfig.setTrainingAlgorithm(nnConfigEntity.getTrainingAlgorithm());
			nnConfig.setIterations(nnConfigEntity.getIterations());
			nnConfig.setTrainTime(Utilities.convertIntToTimeString(nnConfigEntity.getTrainingTime()));
			nnConfig.setMu(nnConfigEntity.getMu());
			nnConfig.setValidationCheck(nnConfigEntity.getValidationCheck());
			nnConfig.setTrainingMSE(nnConfigEntity.getTrainingMSE());
			nnConfig.setValidationMSE(nnConfigEntity.getValidationMSE());

			int hours = nnConfigEntity.getValidDataTime() / CommonConst.DATE_SECONDS_OF_HOUR;
			nnConfig.setValidationDataLastHours(hours);
			nnConfig.setValidationDataMinCount(nnConfigEntity.getValidDataMinCnt());

			int days = nnConfigEntity.getTrainDataTime() / CommonConst.DATE_SECONDS_OF_DAY;
			nnConfig.setTrainingDataLastDays(days);
			nnConfig.setTrainingDataMinCount(nnConfigEntity.getTrainDataMinCnt());
		} else
			nnConfig.setTrainTime("00:00:00");

		return nnConfig;
	}
	
	public void saveAlgorithmNNConfig(AlgorithmNNConfig nnConfig) throws InvalidParameterException {
		// Config info check.
		if (nnConfig.getTrainingAlgorithm() == null)
			throw new InvalidParameterException("Item Null.");

		if (nnConfig.getTrainTime().split(":").length != 3)
			throw new InvalidParameterException("TrainTime format(00:00:00) wrong.");

		if (nnConfig.getIterations() <= 0 || nnConfig.getNeruons1stLayer() <= 0 || nnConfig.getNeruons2ndLayer() <= 0)
			throw new InvalidParameterException("Item 0 or more required.");

		NNConfigEntity nnConfigEntity = this.nnConfigRepository.findByConfId(NNConfigEntity.defaultID);
		if (nnConfigEntity == null)
			nnConfigEntity = new NNConfigEntity();

		nnConfigEntity.setTimestamp(new Date());
		nnConfigEntity.setLearningRate(nnConfig.getLearningRate());
		nnConfigEntity.setMomentum(nnConfig.getMomentum());
		nnConfigEntity.setSigmoidAlphaVal(nnConfig.getSigmoidAlphaValue());
		nnConfigEntity.setNeuronCntLayer1(nnConfig.getNeruons1stLayer());
		nnConfigEntity.setNeuronCntLayer2(nnConfig.getNeruons2ndLayer());
		nnConfigEntity.setTrainingAlgorithm(nnConfig.getTrainingAlgorithm());
		nnConfigEntity.setIterations(nnConfig.getIterations());
		nnConfigEntity.setTrainingTime(Utilities.convertTimeStringToInt(nnConfig.getTrainTime()));
		nnConfigEntity.setMu(nnConfig.getMu());
		nnConfigEntity.setValidationCheck(nnConfig.getValidationCheck());
		nnConfigEntity.setTrainingMSE(nnConfig.getTrainingMSE());
		nnConfigEntity.setValidationMSE(nnConfig.getValidationMSE());
		
		int secondsOfHour = nnConfig.getValidationDataLastHours() * CommonConst.DATE_SECONDS_OF_HOUR;
		nnConfigEntity.setValidDataTime(secondsOfHour);
		nnConfigEntity.setValidDataMinCnt(nnConfig.getValidationDataMinCount());
		
		int secondsOfDay = nnConfig.getTrainingDataLastDays() * CommonConst.DATE_SECONDS_OF_DAY;
		nnConfigEntity.setTrainDataTime(secondsOfDay);
		nnConfigEntity.setTrainDataMinCnt(nnConfig.getTrainingDataMinCount());

		this.nnConfigRepository.save(nnConfigEntity);
	}
	
	public AlgorithmPSOConfig getAlgorithmPSOConfig() {
		AlgorithmPSOConfig psoConfig = new AlgorithmPSOConfig();

		ControlEntity controlEntity = this.controlRepository.findControl();
		PSOConfigEntity psoConfigEntity = this.psoConfigRepository.findByConfId(PSOConfigEntity.defaultID);
		if (psoConfigEntity != null) {

			psoConfig.setCorrectionFactor(psoConfigEntity.getCorrectionFactor());
			psoConfig.setInertia(psoConfigEntity.getInertia());
			psoConfig.setParticlesCount(psoConfigEntity.getParticleCntPerMv());
			psoConfig.setIterations(psoConfigEntity.getIteration());

			String profitMaxWeight = psoConfigEntity.getProfitMaxWeight();
			if (!"".equals(profitMaxWeight)) {
				String[] profitMaxWeightArray = profitMaxWeight.split(":");
				psoConfig.setOptProfitMaxProfitWeight(Double.parseDouble(profitMaxWeightArray[0]));
				psoConfig.setOptProfitMaxEmissionWeight(Double.parseDouble(profitMaxWeightArray[1]));
				psoConfig.setOptProfitMaxEquipmentWeight(Double.parseDouble(profitMaxWeightArray[2]));
			}

			String emissioMinWeight = psoConfigEntity.getEmissionMinWeight();
			if (!"".equals(emissioMinWeight)) {
				String[] emissioMinWeightArray = emissioMinWeight.split(":");
				psoConfig.setOptEmissionMinProfitWeight(Double.parseDouble(emissioMinWeightArray[0]));
				psoConfig.setOptEmissionMinEmissionWeight(Double.parseDouble(emissioMinWeightArray[1]));
				psoConfig.setOptEmissionMinEquipmentWeight(Double.parseDouble(emissioMinWeightArray[2]));
			}

			String equipmentDuraWeight = psoConfigEntity.getEquipmentDuraWeight();
			if (!"".equals(equipmentDuraWeight)) {
				String[] equipmentDuraWeightArray = equipmentDuraWeight.split(":");
				psoConfig.setOptEquipmentDuraProfitWeight(Double.parseDouble(equipmentDuraWeightArray[0]));
				psoConfig.setOptEquipmentDuraEmissionWeight(Double.parseDouble(equipmentDuraWeightArray[1]));
				psoConfig.setOptEquipmentDuraEquipmentWeight(Double.parseDouble(equipmentDuraWeightArray[2]));
			}
		}

		psoConfig.setPsoControlMode(psoConfigEntity.getPsoCLMode());
		psoConfig.setOptMode(controlEntity.getOptMode());
		psoConfig.setMvCount(this.psoMVInfoRepository.findCountByPsoMVInTrainDataConfNativeQuery());
		psoConfig.setNnModelAllowValue(psoConfigEntity.getNnModelAllowValue());
		psoConfig.setRhSparyK(psoConfigEntity.getRhSprayK());
		psoConfig.setO2AvgK(psoConfigEntity.getO2AvgK());
		psoConfig.setCoK(psoConfigEntity.getCoK());
		psoConfig.setNoxK(psoConfigEntity.getNoxK());
		psoConfig.setFgtK(psoConfigEntity.getFgtK());
		psoConfig.setRhSparyDiffK(psoConfigEntity.getRhSprayDiffK());
		psoConfig.setO2DiffK(psoConfigEntity.getO2DiffK());
		psoConfig.setO2AvgBoundary(psoConfigEntity.getO2AvgBoundary());
		psoConfig.setO2AvgPenalyWeight(psoConfigEntity.getO2AvgPenaltyWeight());
		psoConfig.setO2MinBoundary(psoConfigEntity.getO2MinBoundary());
		psoConfig.setO2MinPenalyWeight(psoConfigEntity.getO2MinPenaltyWeight());
		psoConfig.setLoadPenalyWeight(psoConfigEntity.getLoadPenaltyWeight());
		psoConfig.setLoadSetPontPenalyWeight(psoConfigEntity.getLoadSetPointPenaltyWeight());
		psoConfig.setNoxBoundary(psoConfigEntity.getNoxBoundary());
		psoConfig.setNoxPenaltyWeight(psoConfigEntity.getNoxPenaltyWeight());
		psoConfig.setStackCoBoundary(psoConfigEntity.getStackCoBoundary());
		psoConfig.setStackCoPenaltyWeight(psoConfigEntity.getStackCoPenaltyWeight());
		psoConfig.setFgTempBoundary(psoConfigEntity.getFgTempBoundary());
		psoConfig.setFgTempPenaltyWeight(psoConfigEntity.getFgTempPenaltyWeight());
		psoConfig.setCpuUsageLimitValue(psoConfigEntity.getCpuUsageLimitValue());
		psoConfig.setCpuUsageLimitMaxValue((int) (Runtime.getRuntime().availableProcessors() * (PSO_MAXIMUM_THREAD_LOAD_RATE / 100)));

		return psoConfig;
	}

	public void saveAlgorithmPSOConfig(AlgorithmPSOConfig psoConfig) throws InvalidParameterException {
		if (psoConfig.getIterations() <= 0 || psoConfig.getParticlesCount() <= 0)
			throw new InvalidParameterException("Item 0 or more required.");

		PSOConfigEntity psoConfigEntity = this.psoConfigRepository.findByConfId(PSOConfigEntity.defaultID);
		if (psoConfigEntity == null)
			psoConfigEntity = new PSOConfigEntity();
		
		psoConfigEntity.setCorrectionFactor(psoConfig.getCorrectionFactor());
		psoConfigEntity.setInertia(psoConfig.getInertia());
		psoConfigEntity.setParticleCntPerMv(psoConfig.getParticlesCount());
		psoConfigEntity.setIteration(psoConfig.getIterations());
		psoConfigEntity.setProfitMaxWeight(psoConfig.getOptProfitMaxProfitWeight() + ":" + psoConfig.getOptProfitMaxEmissionWeight() + ":" + psoConfig.getOptProfitMaxEquipmentWeight());
		psoConfigEntity.setEmissionMinWeight(psoConfig.getOptEmissionMinProfitWeight() + ":" + psoConfig.getOptEmissionMinEmissionWeight() + ":" + psoConfig.getOptEmissionMinEquipmentWeight());
		psoConfigEntity.setEquipmentDuraWeight(psoConfig.getOptEquipmentDuraProfitWeight() + ":" + psoConfig.getOptEquipmentDuraEmissionWeight() + ":" + psoConfig.getOptEquipmentDuraEquipmentWeight());		
		psoConfigEntity.setNnModelAllowValue(psoConfig.getNnModelAllowValue());
		psoConfigEntity.setRhSprayK(psoConfig.getRhSparyK());
		psoConfigEntity.setO2AvgK(psoConfig.getO2AvgK());
		psoConfigEntity.setCoK(psoConfig.getCoK());
		psoConfigEntity.setNoxK(psoConfig.getNoxK());
		psoConfigEntity.setFgtK(psoConfig.getFgtK());
		psoConfigEntity.setRhSprayDiffK(psoConfig.getRhSparyDiffK());
		psoConfigEntity.setO2DiffK(psoConfig.getO2DiffK());
		psoConfigEntity.setO2AvgBoundary(psoConfig.getO2AvgBoundary());
		psoConfigEntity.setO2AvgPenaltyWeight(psoConfig.getO2AvgPenalyWeight());
		psoConfigEntity.setO2MinBoundary(psoConfig.getO2MinBoundary());
		psoConfigEntity.setO2MinPenaltyWeight(psoConfig.getO2MinPenalyWeight());
		psoConfigEntity.setLoadPenaltyWeight(psoConfig.getLoadPenalyWeight());
		psoConfigEntity.setLoadSetPointPenaltyWeight(psoConfig.getLoadSetPontPenalyWeight());
		psoConfigEntity.setNoxBoundary(psoConfig.getNoxBoundary());
		psoConfigEntity.setNoxPenaltyWeight(psoConfig.getNoxPenaltyWeight());
		psoConfigEntity.setStackCoBoundary(psoConfig.getStackCoBoundary());
		psoConfigEntity.setStackCoPenaltyWeight(psoConfig.getStackCoPenaltyWeight());
		psoConfigEntity.setFgTempBoundary(psoConfig.getFgTempBoundary());
		psoConfigEntity.setFgTempPenaltyWeight(psoConfig.getFgTempPenaltyWeight());
		psoConfigEntity.setCpuUsageLimitValue(psoConfig.getCpuUsageLimitValue());
		psoConfigEntity.setPsoCLMode(psoConfig.getPsoControlMode());
		this.psoConfigRepository.save(psoConfigEntity);
	}
	
	public AlgorithOutputControllerConfig getAlgorithOutputControllerConfig() {
		AlgorithOutputControllerConfig outputControllerConfig = new AlgorithOutputControllerConfig();
		
		OutputDataConfEntity outputDataConfEntity = this.outputDataConfRepository.findByConfId(OutputDataConfEntity.defaultID);
		OutputBiasConfEntity outputBiasConfEntity = this.outputBiasConfRepository.findByConfId(OutputBiasConfEntity.defaultID);
		
		outputControllerConfig.setDvTagChangValue(outputDataConfEntity.getDvMinChgVal());
		outputControllerConfig.setDvTagChangRate(outputDataConfEntity.getDvTagChgRate());
		outputControllerConfig.setMvDamperChangeRate(outputDataConfEntity.getMvDamperChgRate());
		outputControllerConfig.setMvDamperStarChangeRate(outputDataConfEntity.getMvStarDamperChgRate());
		outputControllerConfig.setMvTotalAirChangeRate(outputDataConfEntity.getMvAirChgRate());
		outputControllerConfig.setMvTotalAirStarChangeRate(outputDataConfEntity.getMvStarAirChgRate());
		
		outputControllerConfig.setBurnerPermitValuePerMinutes(outputBiasConfEntity.getBnrDamperBiasChgPermitVal());
		outputControllerConfig.setBurnerBiasMinValue(outputBiasConfEntity.getBnrDamperBiasMin());
		outputControllerConfig.setBurnerBiasMaxValue(outputBiasConfEntity.getBnrDamperBiasMax());
		outputControllerConfig.setOfaPermitValuePerMinutes(outputBiasConfEntity.getOfaDamperBiasChgPermitVal());
		outputControllerConfig.setOfaBiasMinValue(outputBiasConfEntity.getOfaDamperBiasMin());
		outputControllerConfig.setOfaBiasMaxValue(outputBiasConfEntity.getOfaDamperBiasMax());
		outputControllerConfig.setTotalAirPermitValuePerMinutes(outputBiasConfEntity.getAirBiasChgPermitVal());
		outputControllerConfig.setTotalAirBiasMinValue(outputBiasConfEntity.getAirBiasMin());
		outputControllerConfig.setTotalAirBiasMaxValue(outputBiasConfEntity.getAirBiasMax());
		
		outputControllerConfig.setTotalAirCER1(outputBiasConfEntity.getAirBiasCer0());
		outputControllerConfig.setTotalAirCER2(outputBiasConfEntity.getAirBiasCer1());
		outputControllerConfig.setTotalAirCER3(outputBiasConfEntity.getAirBiasCer2());
		outputControllerConfig.setTotalAirCER4(outputBiasConfEntity.getAirBiasCer3());
		
		outputControllerConfig.setTotalAirNFO1(outputBiasConfEntity.getAirBiasNfo0());
		outputControllerConfig.setTotalAirNFO2(outputBiasConfEntity.getAirBiasNfo1());
		outputControllerConfig.setTotalAirNFO3(outputBiasConfEntity.getAirBiasNfo2());
		outputControllerConfig.setTotalAirNFO4(outputBiasConfEntity.getAirBiasNfo3());
		
		return outputControllerConfig;
	}
	
	public void saveAlgorithOutputControllerConfig(AlgorithOutputControllerConfig outputControllerConfig) {
		
		OutputDataConfEntity outputDataConfEntity = this.outputDataConfRepository.findByConfId(OutputDataConfEntity.defaultID);
		if (outputDataConfEntity == null)
			outputDataConfEntity = new OutputDataConfEntity();
		
		OutputBiasConfEntity outputBiasConfEntity = this.outputBiasConfRepository.findByConfId(OutputBiasConfEntity.defaultID);
		if (outputBiasConfEntity == null)
			outputBiasConfEntity = new OutputBiasConfEntity();

		outputDataConfEntity.setDvMinChgVal(outputControllerConfig.getDvTagChangValue());
		outputDataConfEntity.setDvTagChgRate(outputControllerConfig.getDvTagChangRate());
		outputDataConfEntity.setMvDamperChgRate(outputControllerConfig.getMvDamperChangeRate());
		outputDataConfEntity.setMvStarDamperChgRate(outputControllerConfig.getMvDamperStarChangeRate());
		outputDataConfEntity.setMvAirChgRate(outputControllerConfig.getMvTotalAirChangeRate());
		outputDataConfEntity.setMvStarAirChgRate(outputControllerConfig.getMvTotalAirStarChangeRate());
		
		outputBiasConfEntity.setBnrDamperBiasChgPermitVal(outputControllerConfig.getBurnerPermitValuePerMinutes());
		outputBiasConfEntity.setBnrDamperBiasMin(outputControllerConfig.getBurnerBiasMinValue());
		outputBiasConfEntity.setBnrDamperBiasMax(outputControllerConfig.getBurnerBiasMaxValue());		
		outputBiasConfEntity.setOfaDamperBiasChgPermitVal(outputControllerConfig.getOfaPermitValuePerMinutes());
		outputBiasConfEntity.setOfaDamperBiasMin(outputControllerConfig.getOfaBiasMinValue());
		outputBiasConfEntity.setOfaDamperBiasMax(outputControllerConfig.getOfaBiasMaxValue());
		outputBiasConfEntity.setAirBiasChgPermitVal(outputControllerConfig.getTotalAirPermitValuePerMinutes());
		outputBiasConfEntity.setAirBiasMin(outputControllerConfig.getTotalAirBiasMinValue());
		outputBiasConfEntity.setAirBiasMax(outputControllerConfig.getTotalAirBiasMaxValue());
		
		outputBiasConfEntity.setAirBiasCer0(outputControllerConfig.getTotalAirCER1());
		outputBiasConfEntity.setAirBiasCer1(outputControllerConfig.getTotalAirCER2());
		outputBiasConfEntity.setAirBiasCer2(outputControllerConfig.getTotalAirCER3());
		outputBiasConfEntity.setAirBiasCer3(outputControllerConfig.getTotalAirCER4());
		
		outputBiasConfEntity.setAirBiasNfo0(outputControllerConfig.getTotalAirNFO1());
		outputBiasConfEntity.setAirBiasNfo1(outputControllerConfig.getTotalAirNFO2());
		outputBiasConfEntity.setAirBiasNfo2(outputControllerConfig.getTotalAirNFO3());
		outputBiasConfEntity.setAirBiasNfo3(outputControllerConfig.getTotalAirNFO4());
				
		this.outputDataConfRepository.save(outputDataConfEntity);
		this.outputBiasConfRepository.save(outputBiasConfEntity);
	}
	
	public void saveAlgorithmPSOResult(PSOResultEntity psoResultEntity,List<PSOEffectPerItemEntity> psoEffectPerItemEntityList) {
		this.psoResultRepository.save(psoResultEntity);
		this.psoEffectPerItemRepository.saveAll(psoEffectPerItemEntityList);
	}

	public void saveAlgorithmPSOResult2(List<_PSOResultEntity> psoResultEntityList, List<PSOEffectPerItemEntity> psoEffectPerItemEntityList) {
		this._psoResultRepository.saveAll(psoResultEntityList);
		this.psoEffectPerItemRepository.saveAll(psoEffectPerItemEntityList);
	}

	public void saveOutputController(HashMap<String, PsoMVInfoVO> psoMVInfoMap, HashMap<String, Double> mvTGTMap, boolean isNewMVTGTYN, double pureTotalAirFlow) {

		// PSO가 최적화된 탐색괄과를 새로 만든 경우 MV Bias TGT 값을 얻데이트 한다. (Burner, OFA Damper만 해당됨 Total Air는 매번 TGT가 변경됨)
		if (isNewMVTGTYN) {
			for (Map.Entry<String, Double> mvTGT : mvTGTMap.entrySet()) {
				this.outputMVTGTRepository.updateOutputMVTGT(mvTGT.getKey(), mvTGT.getValue());
			}
			
			// BaisTGT 업데이트 후  PSO가 새로운 최적화 값을 뽑기 전까지는 업데이트 하지 않기 위해 플래그를 False로 변경함.  
			this.psoResultRepository.updateNewMVTGTYNFalse();
		}
		else {
			// Total Air - MV TGT Set (매번 한다.)
			String airMVName = psoMVInfoMap.entrySet().stream().filter(e -> e.getValue().getPsoMVType() == MVType.Air).findFirst().get().getKey();
			this.outputMVTGTRepository.updateOutputMVTGT(airMVName, mvTGTMap.get(airMVName));
		}		
		
		// Control Output Set
		for (Map.Entry<String, PsoMVInfoVO> psoMVInfo : psoMVInfoMap.entrySet()) {
			PsoMVInfoVO psoMVInfoVO = psoMVInfo.getValue();
			
			// 개별 홀드가 아닌 경우(AutoMode이면) Bias값을 업데이트 함.
			if (!psoMVInfoVO.isHold()) {
				this.ctrDataOutputRepository.updateCtrDataOutputByTagId(psoMVInfoVO.getOutputBiasTagId(), (int)(psoMVInfoVO.getOutputBiasVal() * 1000000) / 1000000.0);

				switch (psoMVInfoVO.getPsoMVType()) {
				case Burner:
				case OFA:					
					// Burner, OFA는  Bias의 값으로  Sum, Count 값을 업데이트 함.
					this.outputMVTGTRepository.updateOutputMVBiasSumCnt(psoMVInfo.getKey(), (int)(psoMVInfoVO.getOutputBiasVal() * 1000000) / 1000000.0);
					break;

				case Air:
					// Total Air는  Bias 값을  Pure Total Air Flow의 값으로  Sum, Count를 업데이트 함. 
					this.outputMVTGTRepository.updateOutputMVBiasSumCnt(psoMVInfo.getKey(), pureTotalAirFlow);
					break;
				}
			}
			
			// Output Data History
			this.outputDataHistRepository.insertHistory(psoMVInfoVO.getOutputBiasVal(), psoMVInfo.getKey(),	psoMVInfoVO.isHold() ? "Y" : "N");
		}		
					
		// Control Mode가 최초 Run이 되었을 때 INITIALIZE 모드가 발동함. Output Controller가 성공적으로 등록이 된 경우  INITIALIZE를 해제함.
		this.ctrDataOutputRepository.updateCtrDataOutputByTagIdAndTagValDifferent(CommonConst.CTR_DATA_OUTPUT_BIAS_INITIALIZE, 0);
	}

	public void updateOutputControllerOpenLoopZeroBias() {
				
		// Control Mode가 'OL'이면 0으로 변경하는데, 이전에 '0'으로 변경하지 않는 경우만  '0' 으로 업데이트 한다.
		ControlEntity controlEntity = this.controlRepository.findControl();
		int tagValNoZeroCount = this.ctrDataOutputRepository.getCtrDataOutputBiasNoZeroCount();
		if (ControllerModeStatus.OL.name().equals(controlEntity.getControlMode()) && tagValNoZeroCount > 0) {
			this.ctrDataOutputRepository.updateCtrDataOutputBiasInitialize();
			this.outputMVTGTRepository.updateMVTGTInitialize();
		}
	}
	
	public AlgorithmSolutionStatus getAlgorithmSolutionStatus(String plantUnitId) throws InvalidParameterException {
		AlgorithmSolutionStatus solutionStatus = new AlgorithmSolutionStatus();
		
		if (CommonConst.StringEmpty.equals(plantUnitId)) {
			throw new InvalidParameterException("The plantUnitId parameter is missing.");
		}
		
		Object[] algorithmStatusResult = this.commonObjectRepository.getAlgorithmSolutionStatusResult().get(0);
		Object objNNModelTime = algorithmStatusResult[0];
		String nnModelTime = CommonConst.StringEmpty;
		if (objNNModelTime != null)
			nnModelTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format((Date)objNNModelTime);

		Object objPsoResultTime = algorithmStatusResult[1];
		String psoResultTime = CommonConst.StringEmpty;
		if (objPsoResultTime != null)
			psoResultTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(objPsoResultTime);
		
		// BaseLine Load
		HashMap<String, HashMap<String, ConfDataVO>> refConfTypesMap = new HashMap<String, HashMap<String, ConfDataVO>>();
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_BASELINE, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_OPTCONF, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_COAL, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_EFFICIENCY, new HashMap<String, ConfDataVO>());
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_KPI, new HashMap<String, ConfDataVO>());
		
		this.commonDataService.refConfDataMap( plantUnitId, refConfTypesMap);		
		HashMap<String, ConfDataVO> baselineConfDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_BASELINE);		
		HashMap<String, TagDataVO> opDataTagMap = this.commonDataService.getOpDataTagMap();
		
		performanceCalculation.init(opDataTagMap, refConfTypesMap);
				
		// Control Output Data Load		
		HashMap<String, TagDataVO> ctrOutputDataMap = this.commonDataService.getCtrDataTagMap(CtrDataIOType.Output);
		
		// NN Model, PSO Result Time
		solutionStatus.setLastModelGenerationTime(nnModelTime);
		solutionStatus.setLastPSORunningTime(psoResultTime);
		
		// Boiler Status		
		List<Object[]> ctrBoilerDangerAlarmInfo = this.ctrDataInputRepository.getDataInputFirstStatusChangeInfo(CommonConst.CTR_DATA_BLR_PROCESS_DANGER_ALARM_ON);
		if (ctrBoilerDangerAlarmInfo != null) {
			String boilerDangerAlarmOnTagVal = String.valueOf(ctrBoilerDangerAlarmInfo.get(0)[0]);
			Date boilerDangerAlarmOnTimeStamp = (Date) ctrBoilerDangerAlarmInfo.get(0)[1];
			solutionStatus.setBoilerProcessDangerStatus(boilerDangerAlarmOnTagVal);
			solutionStatus.setBoilerProcessDangerLastChangeTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(boilerDangerAlarmOnTimeStamp));
		}

		// OPTIMIZATION PERFORMANCE OVERVIEW
		ConfDataVO o2BaselineVO = baselineConfDataMap.get(CommonConst.FLUE_GAS_O2_AVG);
		ConfDataVO coBaselineVO = baselineConfDataMap.get(CommonConst.FLUE_GAS_CO_AVG);
		ConfDataVO rhSprayFlowAvgBaselineVO = baselineConfDataMap.get(CommonConst.RH_SPRAY_FOW_AVG);
		ConfDataVO noxBaselineVO = baselineConfDataMap.get(CommonConst.STACK_NOX_AVG);
		ConfDataVO tempBaselineVO = baselineConfDataMap.get(CommonConst.FLUE_GAS_TEMP_LR_DEV);
		
		List<AlgorithmItemStatus> optimizationPerformanceOverviewList = new ArrayList<AlgorithmItemStatus>();

		double beforeBaselineRate = 70;
		
		// FG O2		
		double ecoOutletO2 = Utilities.round(performanceCalculation.getO2AtEconomizerOutlet(), 1);
		double o2MaxValue = (100 / beforeBaselineRate) * o2BaselineVO.getConfVal();
		double beforeRate = Utilities.valueOfDoubleRange(beforeBaselineRate, 0, 100);
		double afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * ecoOutletO2 / o2MaxValue, 1), 0, 100);
		AlgorithmItemStatus o2ItemStatus = new AlgorithmItemStatus();
		o2ItemStatus.setItem("FG O2'");
		o2ItemStatus.setUnit(o2BaselineVO.getUnit());
		o2ItemStatus.setBefore(Utilities.roundFirstToString(o2BaselineVO.getConfVal()));
		o2ItemStatus.setAfter(Utilities.roundFirstToString(ecoOutletO2));
		o2ItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		o2ItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));
		optimizationPerformanceOverviewList.add(o2ItemStatus);

		// FG CO
		double ecoOutletCO = Utilities.round(performanceCalculation.getCOAtAHOutlet() * 12500, 0);
		double coMaxValue = (100 / beforeBaselineRate) * coBaselineVO.getConfVal();
		beforeRate = Utilities.valueOfDoubleRange(beforeBaselineRate, 0, 100);
		afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * ecoOutletCO / coMaxValue, 1), 0, 100);
		AlgorithmItemStatus coItemStatus = new AlgorithmItemStatus();
		coItemStatus.setItem("FG CO'");
		coItemStatus.setUnit(coBaselineVO.getUnit());
		coItemStatus.setBefore(Utilities.roundFirstToString(coBaselineVO.getConfVal()));
		coItemStatus.setAfter(Utilities.roundFirstToString(ecoOutletCO));
		coItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		coItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));
		optimizationPerformanceOverviewList.add(coItemStatus);		
				
		// RH SPRAY		
		TagDataVO rhSprayFlowAvgOpDataVO = opDataTagMap.get(CommonConst.RH_SPRAY_WTR_FLOW);
		double rhSparyFlowAvgMaxValue = (100 / beforeBaselineRate) * rhSprayFlowAvgBaselineVO.getConfVal();
		beforeRate = Utilities.valueOfDoubleRange(beforeBaselineRate, 0, 100);
		afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * rhSprayFlowAvgOpDataVO.getTagVal() / rhSparyFlowAvgMaxValue, 1), 0, 100);		
		AlgorithmItemStatus rhSprayItemStatus = new AlgorithmItemStatus();
		rhSprayItemStatus.setItem("RH SPRAY");
		rhSprayItemStatus.setUnit(rhSprayFlowAvgBaselineVO.getUnit());
		rhSprayItemStatus.setBefore(Utilities.roundFirstToString(rhSprayFlowAvgBaselineVO.getConfVal()));
		rhSprayItemStatus.setAfter(Utilities.roundFirstToString(rhSprayFlowAvgOpDataVO.getTagVal()));
		rhSprayItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		rhSprayItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));
		optimizationPerformanceOverviewList.add(rhSprayItemStatus);

		// TEMP DEV'
		double tempAbs = Math.abs(opDataTagMap.get(CommonConst.HRZN_FG_temp_L).getTagVal() - opDataTagMap.get(CommonConst.HRZN_FG_temp_R).getTagVal());
		double tempMaxValue = (100 / beforeBaselineRate) * tempBaselineVO.getConfVal();
		beforeRate = Utilities.valueOfDoubleRange(beforeBaselineRate, 0, 100);
		afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * tempAbs / tempMaxValue, 1), 0, 100);
		AlgorithmItemStatus tempDevItemStatus = new AlgorithmItemStatus();
		tempDevItemStatus.setItem("TEMP DEV'");
		tempDevItemStatus.setUnit(tempBaselineVO.getUnit());
		tempDevItemStatus.setBefore(Utilities.roundFirstToString(tempBaselineVO.getConfVal()));
		tempDevItemStatus.setAfter(Utilities.roundFirstToString(tempAbs));
		tempDevItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		tempDevItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));
		optimizationPerformanceOverviewList.add(tempDevItemStatus);

		// NOX
		TagDataVO noxOpDataVO = opDataTagMap.get(CommonConst.NOx);
		double noxMaxValue = (100 / beforeBaselineRate) * noxBaselineVO.getConfVal();
		beforeRate = Utilities.valueOfDoubleRange(beforeBaselineRate, 0, 100);
		afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * noxOpDataVO.getTagVal() / noxMaxValue, 1), 0, 100);
		AlgorithmItemStatus noxDevItemStatus = new AlgorithmItemStatus();
		noxDevItemStatus.setItem("NOX");
		noxDevItemStatus.setUnit(noxBaselineVO.getUnit());
		noxDevItemStatus.setBefore(Utilities.roundFirstToString(noxBaselineVO.getConfVal()));
		noxDevItemStatus.setAfter(Utilities.roundFirstToString(noxOpDataVO.getTagVal()));
		noxDevItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		noxDevItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));		
		optimizationPerformanceOverviewList.add(noxDevItemStatus);		
		solutionStatus.setOptimizationPerformanceOverviewList(optimizationPerformanceOverviewList);
				
		// EFFICIENCY STATUS
		List<ItemStatus> efficiencyStatusList  = new ArrayList<ItemStatus>();
		
		ConfDataVO baselineVO = baselineConfDataMap.get(CommonConst.UNIT_HEAT_RATE);
		TagDataVO tagDataVO = ctrOutputDataMap.get(CommonConst.CTR_DATA_CURRENT_UNIT_HEAT_RATE);
		
		ItemStatus unitHeatRateItemStatus = new ItemStatus();
		unitHeatRateItemStatus.setItem("Unit Heat Rate");
		unitHeatRateItemStatus.setBaseline(Utilities.roundIntToString(baselineVO.getConfVal()));
		unitHeatRateItemStatus.setCurrent(Utilities.roundIntToString(tagDataVO.getTagVal()));
		unitHeatRateItemStatus.setUnit(baselineVO.getUnit());
		efficiencyStatusList.add(unitHeatRateItemStatus);
		
		baselineVO = baselineConfDataMap.get(CommonConst.BLR_EFFICIENCY);
		tagDataVO = ctrOutputDataMap.get(CommonConst.CTR_DATA_CURRENT_BOILER_EFFICIENCY);		
		ItemStatus boilerEfficiencyItemStatus = new ItemStatus();
		boilerEfficiencyItemStatus.setItem("Boiler Efficiency");
		boilerEfficiencyItemStatus.setBaseline(Utilities.roundFirstToString(baselineVO.getConfVal()));
		boilerEfficiencyItemStatus.setCurrent(Utilities.roundFirstToString(tagDataVO.getTagVal()));
		boilerEfficiencyItemStatus.setUnit(baselineVO.getUnit());
		efficiencyStatusList.add(boilerEfficiencyItemStatus);
		
		solutionStatus.setEfficiencyStatusList(efficiencyStatusList);
		
		// SOLUTION MODE CONTROL
		List<AlgorithmItemStatus> solutionModeControlList = new ArrayList<AlgorithmItemStatus>();
		List<Object[]> controlAndSystemCheckList = this.controlRepository.getControlAndSystemCheckInfo();
		for (Object[] controlAndSystemCheck : controlAndSystemCheckList) {
			switch (String.valueOf(controlAndSystemCheck[0])) {
			case "CONTROL_READY":
				solutionModeControlList.add(new AlgorithmItemStatus("Combustion Optimizer", String.valueOf(controlAndSystemCheck[1])
						, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format((Date) controlAndSystemCheck[2])));
				break;

			case "CONTROL_MODE":
				ControllerModeStatus controllerModeStatus = ControllerModeStatus.valueOf(String.valueOf(controlAndSystemCheck[1]));
				solutionModeControlList.add(new AlgorithmItemStatus("Controller Mode", controllerModeStatus.getValue()
						, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format((Date) controlAndSystemCheck[2])));
				break;

			case "OPT_MODE":
				PsoOptimizationFunctionModeStatus optimizationFunctionModeStatus = PsoOptimizationFunctionModeStatus.valueOf(String.valueOf(controlAndSystemCheck[1]));
				solutionModeControlList.add(new AlgorithmItemStatus("Optimization Mode", optimizationFunctionModeStatus.getValue()
						, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format((Date) controlAndSystemCheck[2])));
				break;
			}
		}
		
		solutionStatus.setSolutionModeControlList(solutionModeControlList);
		
		// SOLUTION DIAGNOSTIC
		List<AlgorithmItemStatus> solutionDiagnosticList = new ArrayList<AlgorithmItemStatus>();		
		
		String dcsHeartbeatStatus = String.valueOf((int)algorithmStatusResult[3]);
		solutionDiagnosticList.add(new AlgorithmItemStatus("DCS Modbus Heartbeat", dcsHeartbeatStatus));
		
		String opcHeartbeatStatus = String.valueOf((int)algorithmStatusResult[2]);
		solutionDiagnosticList.add(new AlgorithmItemStatus("OPC Heartbeat", opcHeartbeatStatus));
				
		tagDataVO = ctrOutputDataMap.get(CommonConst.CTR_DATA_SOLUTION_SYSTEM_FAULT_ON);
		solutionDiagnosticList.add(new AlgorithmItemStatus("System Fault", Utilities.roundIntToString(tagDataVO.getTagVal())));
		
		String solutionHeartbeatStatus = String.valueOf((int)algorithmStatusResult[4]);
		solutionDiagnosticList.add(new AlgorithmItemStatus("Heartbeat Status", solutionHeartbeatStatus));
		
		solutionStatus.setSolutionDiagnosticList(solutionDiagnosticList);

		// OPTIMIZER MV STATUS		
		AlgorithmOptimizerMVStatus optimizerMVStatus = new AlgorithmOptimizerMVStatus();		
		List<AlgorithmItemStatus> optimizerMVStatusList = new ArrayList<AlgorithmItemStatus>();
		
		// OUTPUT CONROLLER
		List<Object[]> solutionStatusMVOutputList = this.commonObjectRepository.getAlgorithmSolutionStatusMVOutputResult();
		for (Object[] objectSolutionStatusMVOutput : solutionStatusMVOutputList) {
			String item = Utilities.ObjectToString(objectSolutionStatusMVOutput[0]);
			String bias = Utilities.roundFourToString(Utilities.ObjectToDouble(objectSolutionStatusMVOutput[2]));
			String status = Utilities.roundIntToString(Utilities.ObjectToDouble(objectSolutionStatusMVOutput[3]));
			MVType mvType = MVType.valueOf(Utilities.ObjectToString(objectSolutionStatusMVOutput[1]));
			switch (mvType) {
			case Burner:
				optimizerMVStatusList.add(new AlgorithmItemStatus("SA DAMPER", item + " BIAS", bias, status));
				break;
			case OFA:
				optimizerMVStatusList.add(new AlgorithmItemStatus("OFA DAMPER", item + " BIAS", bias, status));
				break;
			case Air:
				optimizerMVStatusList.add(new AlgorithmItemStatus("AIR FLOW", "O2 TRIM SP BIAS", bias, status));
				break;
			}
		}
		optimizerMVStatus.setOptimizerMVStatusList(optimizerMVStatusList);		
		optimizerMVStatus.setOptimizerMVStatusList(optimizerMVStatusList);
		
		tagDataVO = ctrOutputDataMap.get(CommonConst.CTR_DATA_OPTIMIZER_OUTPUT_CTRL_FULL_HOLD);		
		optimizerMVStatus.setAllHoldStatus(Utilities.roundIntToString(tagDataVO.getTagVal()));
		
		tagDataVO = ctrOutputDataMap.get(CommonConst.CTR_DATA_OUTPUT_BIAS_INITIALIZE);		
		optimizerMVStatus.setInitializeStatus(Utilities.roundIntToString(tagDataVO.getTagVal()));
		
		solutionStatus.setAlgorithmOptimizerMVStatus(optimizerMVStatus);
		return solutionStatus;
	}
	
	public AlgorithmProcessStatus getAlgorithmProcessStatus(String plantUnitId) throws InvalidParameterException {
		AlgorithmProcessStatus processStatus = new AlgorithmProcessStatus();
		
		PSOResultEntity psoResultEntity = this.psoResultRepository.findTop1ByOrderByTimestampDesc();
		
		Object[] solutionStatusResult = this.commonObjectRepository.getAlgorithmProcessStatusResult().get(0);		
		AlgorithmStatusInfoVO algorithmStatusInfoVO = new AlgorithmStatusInfoVO(solutionStatusResult);
						
		// DATA PREPROCESSOR
		AlgorithmProcessStatusPreProcess algorithmprocessStatusPreProcess = new AlgorithmProcessStatusPreProcess();
		algorithmprocessStatusPreProcess.setLastProcessingTime(algorithmStatusInfoVO.getTrainProcessStartTime());
		algorithmprocessStatusPreProcess.setLastSavingTrainingTime(algorithmStatusInfoVO.getLastNNTrainTime());
		algorithmprocessStatusPreProcess.setAnyByPassValveOpenStatus(algorithmStatusInfoVO.getAnyByPassValueOpen());
		algorithmprocessStatusPreProcess.setAnyOilBnrFiringOnStatus(algorithmStatusInfoVO.getAnyOilBurnerFiringOn());
		algorithmprocessStatusPreProcess.setRunbackActiveOnStatus(algorithmStatusInfoVO.getRunbackActionOn());
		algorithmprocessStatusPreProcess.setUnitLoadTargetLowStatus(algorithmStatusInfoVO.getUnitLoadTargetLow());
		algorithmprocessStatusPreProcess.setLoadChangeDetectedStatus(algorithmStatusInfoVO.getLoadChangeDetected());
		algorithmprocessStatusPreProcess.setFreqCorrectionOnStatus(algorithmStatusInfoVO.getFreqCorrectionOn());
		processStatus.setAlgorithmprocessStatusPreProcess(algorithmprocessStatusPreProcess);
		
		// NN MODEL GENERATOR
		AlgorithmProcessStatusNNModel algorithmprocessStatusNNModel = new AlgorithmProcessStatusNNModel();		
		algorithmprocessStatusNNModel.setPreModelGenerationTime(algorithmStatusInfoVO.getPrevNNModelTime());
		algorithmprocessStatusNNModel.setLastModelGenerationTime(algorithmStatusInfoVO.getLastNNModelTime());
		
		boolean seedModel1Used = false;
		boolean seedModel2Used = false;
		boolean fruitModel1Used = false;
		boolean fruitModel2Used = false;
		boolean fruitModel3Used = false;
		String psoUsedModelTs = Utilities.ObjectToTimeString(psoResultEntity.getPsoUsedModelTs(), "yyyy/MM/dd HH:mm:ss");
		if ("Base".equals(psoResultEntity.getPsoUsedModelType())) {
			if (psoUsedModelTs.equals(algorithmStatusInfoVO.getSeedNNModel1Time()))
				seedModel1Used = true;
			else if (psoUsedModelTs.equals(algorithmStatusInfoVO.getSeedNNModel2Time()))
				seedModel2Used = true;
		} else {
			if (psoUsedModelTs.equals(algorithmStatusInfoVO.getFruitNNModel1Time()))
				fruitModel1Used = true;
			else if (psoUsedModelTs.equals(algorithmStatusInfoVO.getFruitNNModel2Time()))
				fruitModel2Used = true;
			else if (psoUsedModelTs.equals(algorithmStatusInfoVO.getFruitNNModel3Time()))
				fruitModel3Used = true;
		}

		algorithmprocessStatusNNModel.setSeedModel1SaveTime(new AlgorithmProcessStatusNNModelDetail(algorithmStatusInfoVO.getSeedNNModel1Time(), seedModel1Used));
		algorithmprocessStatusNNModel.setSeedModel2SaveTime(new AlgorithmProcessStatusNNModelDetail(algorithmStatusInfoVO.getSeedNNModel2Time(), seedModel2Used));		
		algorithmprocessStatusNNModel.setLastModelValidError(Utilities.roundFourToString(algorithmStatusInfoVO.getNnModelValidErrorRate()));		
		algorithmprocessStatusNNModel.setFruitModel1SaveTime(new AlgorithmProcessStatusNNModelDetail(algorithmStatusInfoVO.getFruitNNModel1Time(), fruitModel1Used));
		algorithmprocessStatusNNModel.setFruitModel2SaveTime(new AlgorithmProcessStatusNNModelDetail(algorithmStatusInfoVO.getFruitNNModel2Time(), fruitModel2Used));
		algorithmprocessStatusNNModel.setFruitModel3SaveTime(new AlgorithmProcessStatusNNModelDetail(algorithmStatusInfoVO.getFruitNNModel3Time(), fruitModel3Used));
		processStatus.setAlgorithmprocessStatusNNModel(algorithmprocessStatusNNModel);
		
		// STATIC OPTIMIZER (PSO)
		AlgorithmProcessStatusPSO algorithmprocessStatusPSO = new AlgorithmProcessStatusPSO();
		algorithmprocessStatusPSO.setLastPSORunningTime(algorithmStatusInfoVO.getLastPsoRunningTime());
		algorithmprocessStatusPSO.setModelTestErrorSum(Utilities.roundFirstToString(algorithmStatusInfoVO.getLastPsoErrorSum()));
		double limitValue = Math.pow(algorithmStatusInfoVO.getPsoConfAllowValue(), 2);		
		algorithmprocessStatusPSO.setModelTestErrorLimit(String.valueOf(limitValue));
		algorithmprocessStatusPSO.setObjectFuntionFVal(Utilities.roundFirstToString(algorithmStatusInfoVO.getLastPsoOpDataFValue()));
		algorithmprocessStatusPSO.setObjectFuntionOptimalFVal(Utilities.roundFirstToString(algorithmStatusInfoVO.getLastPsoOptimalFValue()));
		
		// OPTIMIMAL BIAS
		String[] psoMVOptimalPositons = algorithmStatusInfoVO.getLastPsoOptimalPositon().split(",");
		String[] optimalBias = new String[psoMVOptimalPositons.length];
		for (int i = 0; i < psoMVOptimalPositons.length; i++) {
			String psoMVOptimalPositon = psoMVOptimalPositons[i];
			String[] mvOptimals = psoMVOptimalPositon.split(":");
			optimalBias[i] = Utilities.roundFourToString(Double.parseDouble(mvOptimals[1]));
		}
		algorithmprocessStatusPSO.setArrayOptimalBias(optimalBias);
		
		// BaseLine Load
		HashMap<String, HashMap<String, ConfDataVO>> refConfTypesMap = new HashMap<String, HashMap<String, ConfDataVO>>();
		refConfTypesMap.put(CommonConst.CONFING_SETTING_TYPE_BASELINE, new HashMap<String, ConfDataVO>());
		
		this.commonDataService.refConfDataMap( plantUnitId, refConfTypesMap);		
		HashMap<String, ConfDataVO> baselineConfDataMap = refConfTypesMap.get(CommonConst.CONFING_SETTING_TYPE_BASELINE);
		
		List<AlgorithmItemStatus> optimizationPerformancePredictionList = new ArrayList<AlgorithmItemStatus>();
		
		double baselineRatio = 70;
		
		double o2LeftBefore = 0.0;
		double o2LeftAfterNN = 0.0;
		double o2LeftAfter = 0.0;
		
		double o2RightBefore = 0.0;
		double o2RightAfterNN = 0.0;
		double o2RightAfter = 0.0;
		
		double o2AvgBefore = 0.0;
		double o2AvgAfterNN = 0.0;
		double o2AvgAfter = 0.0;
		
		double coBefore = 0.0;
		double coAfterNN = 0.0;
		double coAfter = 0.0;

		double rhSprayLeftBefore = 0.0;
		double rhSprayLeftAfterNN = 0.0;
		double rhSprayLeftAfter = 0.0;
				
		double rhSprayRightBefore = 0.0;
		double rhSprayRightAfterNN = 0.0;
		double rhSprayRightAfter = 0.0;
		
		double rhSpraySumBefore = 0.0;
		double rhSpraySumAfterNN = 0.0;
		double rhSpraySumAfter = 0.0;
		
		double tempLeftBefore = 0.0;
		double tempLeftAfterNN = 0.0;
		double tempLeftAfter = 0.0;
				
		double tempRightBefore = 0.0;
		double tempRightAfterNN = 0.0;
		double tempRightAfter = 0.0;
		
		double tempDevBefore = 0.0;
		double tempDevAfterNN = 0.0;
		double tempDevAfter = 0.0;
		
		double noxBefore = 0.0;
		double noxAfterNN = 0.0;
		double noxAfter = 0.0;
		
		List<PSOEffectPerItemEntity> psoEffectPerItemEntityList = this.psoEffectPerItemRepository.findAll();
		for (PSOEffectPerItemEntity psoEffectPerItemEntity : psoEffectPerItemEntityList) {

			switch (psoEffectPerItemEntity.getItem()) {
			case "O2_LEFT":
				o2LeftBefore = psoEffectPerItemEntity.getBeforeVal();
				o2LeftAfterNN = psoEffectPerItemEntity.getAfterNNVal();
				o2LeftAfter = psoEffectPerItemEntity.getAfterVal();
				break;
				
			case "O2_RIGHT":
				o2RightBefore = psoEffectPerItemEntity.getBeforeVal();
				o2RightAfterNN = psoEffectPerItemEntity.getAfterNNVal();
				o2RightAfter = psoEffectPerItemEntity.getAfterVal();
				break;
				
			case "CO":
				coBefore = psoEffectPerItemEntity.getBeforeVal();
				coAfterNN = psoEffectPerItemEntity.getAfterNNVal();
				coAfter = psoEffectPerItemEntity.getAfterVal();
				break;
				
			case "RH_SPRAY_LEFT":
				rhSprayLeftBefore = psoEffectPerItemEntity.getBeforeVal();
				rhSprayLeftAfterNN = psoEffectPerItemEntity.getAfterNNVal();
				rhSprayLeftAfter = psoEffectPerItemEntity.getAfterVal();
				break;
				
			case "RH_SPRAY_RIGHT":
				rhSprayRightBefore = psoEffectPerItemEntity.getBeforeVal();
				rhSprayRightAfterNN = psoEffectPerItemEntity.getAfterNNVal();
				rhSprayRightAfter = psoEffectPerItemEntity.getAfterVal();
				break;
				
			case "TEMP_LEFT":
				tempLeftBefore = psoEffectPerItemEntity.getBeforeVal();
				tempLeftAfterNN = psoEffectPerItemEntity.getAfterNNVal();
				tempLeftAfter = psoEffectPerItemEntity.getAfterVal();
				break;
				
			case "TEMP_RIGHT":
				tempRightBefore = psoEffectPerItemEntity.getBeforeVal();
				tempRightAfterNN = psoEffectPerItemEntity.getAfterNNVal();
				tempRightAfter = psoEffectPerItemEntity.getAfterVal();
				break;
				
			case "NOX":
				noxBefore = psoEffectPerItemEntity.getBeforeVal();
				noxAfterNN = psoEffectPerItemEntity.getAfterNNVal();
				noxAfter = psoEffectPerItemEntity.getAfterVal();
				break;
			}
		}

		ConfDataVO o2BaselineVO = baselineConfDataMap.get(CommonConst.FLUE_GAS_O2_AVG);
		ConfDataVO coBaselineVO = baselineConfDataMap.get(CommonConst.FLUE_GAS_CO_AVG);
		ConfDataVO rhSprayFlowAvgBaselineVO = baselineConfDataMap.get(CommonConst.RH_SPRAY_FOW_AVG);
		ConfDataVO noxBaselineVO = baselineConfDataMap.get(CommonConst.STACK_NOX_AVG);
		ConfDataVO tempBaselineVO = baselineConfDataMap.get(CommonConst.FLUE_GAS_TEMP_LR_DEV);
				
		o2AvgBefore = (o2LeftBefore + o2RightBefore) / 2;
		o2AvgAfterNN = (o2LeftAfterNN + o2RightAfterNN) / 2;
		o2AvgAfter = (o2LeftAfter + o2RightAfter) / 2;
		
		rhSpraySumBefore = rhSprayLeftBefore + rhSprayRightBefore;
		rhSpraySumAfterNN = rhSprayLeftAfterNN + rhSprayRightAfterNN;
		rhSpraySumAfter = rhSprayLeftAfter + rhSprayRightAfter;
		
		tempDevBefore = Math.abs(tempLeftBefore - tempRightBefore);
		tempDevAfterNN = Math.abs(tempLeftAfterNN - tempRightAfterNN);
		tempDevAfter = Math.abs(tempLeftAfter - tempRightAfter);
		
		double maxValue = (100 / baselineRatio) * o2AvgBefore;
		double beforeRate = Utilities.valueOfDoubleRange(baselineRatio, 0, 100);
		double afterNNRate = Utilities.valueOfDoubleRange(Utilities.round(100 * o2AvgAfterNN / maxValue, 1), 0, 100);
		double afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * o2AvgAfter / maxValue, 1), 0, 100);
		double impact = (o2AvgAfter / o2AvgBefore * 100) - 100;
		
		AlgorithmItemStatus o2ItemStatus = new AlgorithmItemStatus();
		o2ItemStatus.setItem("FG O2");
		o2ItemStatus.setUnit(o2BaselineVO.getUnit());
		o2ItemStatus.setBefore(Utilities.roundFirstToString(o2AvgBefore));
		o2ItemStatus.setAfterNN(Utilities.roundFirstToString(o2AvgAfterNN));
		o2ItemStatus.setAfter(Utilities.roundFirstToString(o2AvgAfter));
		o2ItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		o2ItemStatus.setAfterNNRate(Utilities.roundIntToString(afterNNRate));
		o2ItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));
		o2ItemStatus.setImpact(Utilities.roundSecondToString(impact));
		o2ItemStatus.setBeforeLeft(Utilities.roundFirstToString(o2LeftBefore));
		o2ItemStatus.setBeforeRight(Utilities.roundFirstToString(o2RightBefore));
		o2ItemStatus.setAfterNNLeft(Utilities.roundFirstToString(o2LeftAfterNN));
		o2ItemStatus.setAfterNNRight(Utilities.roundFirstToString(o2RightAfterNN));		
		o2ItemStatus.setAfterLeft(Utilities.roundFirstToString(o2LeftAfter));
		o2ItemStatus.setAfterRight(Utilities.roundFirstToString(o2RightAfter));		
		optimizationPerformancePredictionList.add(o2ItemStatus);
		
		maxValue = (100 / baselineRatio) * coBefore;
		beforeRate = Utilities.valueOfDoubleRange(baselineRatio, 0, 100);
		afterNNRate = Utilities.valueOfDoubleRange(Utilities.round(100 * coAfterNN / maxValue, 1), 0, 100);
		afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * coAfter / maxValue, 1), 0, 100);
		impact = (coAfter / coBefore * 100) - 100;
		AlgorithmItemStatus coItemStatus = new AlgorithmItemStatus();
		coItemStatus.setItem("FG CO");
		coItemStatus.setUnit(coBaselineVO.getUnit());
		coItemStatus.setBefore(Utilities.roundFirstToString(coBefore));
		coItemStatus.setAfterNN(Utilities.roundFirstToString(coAfterNN));		
		coItemStatus.setAfter(Utilities.roundFirstToString(coAfter));		
		coItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		coItemStatus.setAfterNNRate(Utilities.roundIntToString(afterNNRate));		
		coItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));
		coItemStatus.setImpact(Utilities.roundSecondToString(impact));
		optimizationPerformancePredictionList.add(coItemStatus);
		
		maxValue = (100 / baselineRatio) * rhSpraySumBefore;
		beforeRate = Utilities.valueOfDoubleRange(baselineRatio, 0, 100);
		afterNNRate = Utilities.valueOfDoubleRange(Utilities.round(100 * rhSpraySumAfterNN / maxValue, 1), 0, 100);
		afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * rhSpraySumAfter / maxValue, 1), 0, 100);
		impact = (rhSpraySumAfter / rhSpraySumBefore * 100) - 100;
		AlgorithmItemStatus rhSprayItemStatus = new AlgorithmItemStatus();
		rhSprayItemStatus.setItem("RH SPRAY");
		rhSprayItemStatus.setUnit(rhSprayFlowAvgBaselineVO.getUnit());
		rhSprayItemStatus.setBefore(Utilities.roundFirstToString(rhSpraySumBefore));
		rhSprayItemStatus.setAfterNN(Utilities.roundFirstToString(rhSpraySumAfterNN));
		rhSprayItemStatus.setAfter(Utilities.roundFirstToString(rhSpraySumAfter));
		rhSprayItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		rhSprayItemStatus.setAfterNNRate(Utilities.roundIntToString(afterNNRate));
		rhSprayItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));
		rhSprayItemStatus.setImpact(Utilities.roundSecondToString(impact));
		rhSprayItemStatus.setBeforeLeft(Utilities.roundFirstToString(rhSprayLeftBefore));
		rhSprayItemStatus.setBeforeRight(Utilities.roundFirstToString(rhSprayRightBefore));
		rhSprayItemStatus.setAfterNNLeft(Utilities.roundFirstToString(rhSprayLeftAfterNN));
		rhSprayItemStatus.setAfterNNRight(Utilities.roundFirstToString(rhSprayRightAfterNN));
		rhSprayItemStatus.setAfterLeft(Utilities.roundFirstToString(rhSprayLeftAfter));
		rhSprayItemStatus.setAfterRight(Utilities.roundFirstToString(rhSprayRightAfter));
		optimizationPerformancePredictionList.add(rhSprayItemStatus);
		
		maxValue = (100 / baselineRatio) * tempDevBefore;
		beforeRate = Utilities.valueOfDoubleRange(baselineRatio, 0, 100);
		afterNNRate = Utilities.valueOfDoubleRange(Utilities.round(100 * tempDevAfterNN / maxValue, 1), 0, 100);
		afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * tempDevAfter / maxValue, 1), 0, 100);
		impact = (tempDevAfter / tempDevBefore * 100) - 100;
		AlgorithmItemStatus tempDevItemStatus = new AlgorithmItemStatus();
		tempDevItemStatus.setItem("TEMP DEV'");
		tempDevItemStatus.setUnit(tempBaselineVO.getUnit());
		tempDevItemStatus.setBefore(Utilities.roundFirstToString(tempDevBefore));
		tempDevItemStatus.setAfterNN(Utilities.roundFirstToString(tempDevAfterNN));
		tempDevItemStatus.setAfter(Utilities.roundFirstToString(tempDevAfter));		
		tempDevItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		tempDevItemStatus.setAfterNNRate(Utilities.roundIntToString(afterNNRate));		
		tempDevItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));		
		tempDevItemStatus.setImpact(Utilities.roundSecondToString(impact));
		tempDevItemStatus.setBeforeLeft(Utilities.roundFirstToString(tempLeftBefore));
		tempDevItemStatus.setBeforeRight(Utilities.roundFirstToString(tempRightBefore));		
		tempDevItemStatus.setAfterNNLeft(Utilities.roundFirstToString(tempLeftAfterNN));
		tempDevItemStatus.setAfterNNRight(Utilities.roundFirstToString(tempRightAfterNN));		
		tempDevItemStatus.setAfterLeft(Utilities.roundFirstToString(tempLeftAfter));
		tempDevItemStatus.setAfterRight(Utilities.roundFirstToString(tempRightAfter));		
		optimizationPerformancePredictionList.add(tempDevItemStatus);
		
		maxValue = (100 / baselineRatio) * noxBefore;
		beforeRate = Utilities.valueOfDoubleRange(baselineRatio, 0, 100);
		afterNNRate = Utilities.valueOfDoubleRange(Utilities.round(100 * noxAfterNN / maxValue, 1), 0, 100);
		afterRate = Utilities.valueOfDoubleRange(Utilities.round(100 * noxAfter / maxValue, 1), 0, 100);
		impact = (noxAfter / noxBefore * 100) - 100;
		AlgorithmItemStatus noxItemStatus = new AlgorithmItemStatus();
		noxItemStatus.setItem("NOX");
		noxItemStatus.setUnit(noxBaselineVO.getUnit());
		noxItemStatus.setBefore(Utilities.roundFirstToString(noxBefore));
		noxItemStatus.setAfterNN(Utilities.roundFirstToString(noxAfterNN));
		noxItemStatus.setAfter(Utilities.roundFirstToString(noxAfter));
		noxItemStatus.setBeforeRate(Utilities.roundIntToString(beforeRate));
		noxItemStatus.setAfterNNRate(Utilities.roundIntToString(afterNNRate));
		noxItemStatus.setAfterRate(Utilities.roundIntToString(afterRate));
		noxItemStatus.setImpact(Utilities.roundSecondToString(impact));
		optimizationPerformancePredictionList.add(noxItemStatus);
		
		algorithmprocessStatusPSO.setOptimizationPerformancePredictionList(optimizationPerformancePredictionList);
		processStatus.setAlgorithmprocessStatusPSO(algorithmprocessStatusPSO);
		
		// OUTPUT CONROLLER		
		List<Object[]> solutionStatusMVOutputList = this.commonObjectRepository.getAlgorithmProcessStatusMVOutputResult();
		
		AlgorithmProcessStatusOutputController algorithmProcessStatusOutputController = new AlgorithmProcessStatusOutputController();
		algorithmProcessStatusOutputController.setLastCalculationTime(algorithmStatusInfoVO.getLastOutputControllerTime());
		
		AlgorithmOptimizerMVStatus algorithmOptimizerMVStatus = new AlgorithmOptimizerMVStatus();

		List<AlgorithmItemStatus> optimizerMVStatusList = new ArrayList<AlgorithmItemStatus>();
		for (Object[] objectSolutionStatusMVOutput : solutionStatusMVOutputList) {			
			String item = Utilities.ObjectToString(objectSolutionStatusMVOutput[0]);
			String min =  Utilities.ObjectToString(objectSolutionStatusMVOutput[1]);
			String bias = Utilities.roundFourToString(Utilities.ObjectToDouble(objectSolutionStatusMVOutput[2]));
			String status = Utilities.roundIntToString(Utilities.ObjectToDouble(objectSolutionStatusMVOutput[3]));
			String max =  Utilities.ObjectToString(objectSolutionStatusMVOutput[4]);
			String target = Utilities.roundFourToString(Utilities.ObjectToDouble(objectSolutionStatusMVOutput[5]));
			optimizerMVStatusList.add(new AlgorithmItemStatus(item, status, min, bias, target, max));
		}		
		algorithmOptimizerMVStatus.setOptimizerMVStatusList(optimizerMVStatusList);
		
		algorithmOptimizerMVStatus.setAllHoldStatus(algorithmStatusInfoVO.getOutputControllerFullHold());
		algorithmOptimizerMVStatus.setInitializeStatus(algorithmStatusInfoVO.getOutputControllerInitialize());		
		algorithmProcessStatusOutputController.setAlgorithmOptimizerMVStatus(algorithmOptimizerMVStatus);		
		processStatus.setAlgorithmProcessStatusOutputController(algorithmProcessStatusOutputController);
		
		return processStatus;
	}
	
	private Tag getTrendTagInfo(NNTrainDataIOType nnTrainDataIOType, String tagId, Date startDate, Date endDate, int secModNumber) {
		Tag tag = new Tag();
			
		List<Object[]> nnTrainDataList = null;
		switch (nnTrainDataIOType) {
		case Input:
			nnTrainDataList = this.nnTrainInRepository.findByTagIdNNTrainInputDataNativeQuery(tagId, startDate, endDate);
			break;
			
		case Output:
			nnTrainDataList = this.nnTrainOutRepository.findByTagIdNNTrainOutputDataNativeQuery(tagId, startDate, endDate);
			break;
		}

		ArrayList<HashMap<String, Object>> trendObjectList = new ArrayList<HashMap<String, Object>>();
		for (Object[] nnTrainData : nnTrainDataList) {
			Date nnTrainDate = (Date)nnTrainData[0];
			HashMap<String, Object> trendObject = new HashMap<String, Object>();
			trendObject.put(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(nnTrainDate),
					String.valueOf(nnTrainData[1]));
			trendObjectList.add(trendObject);
		}

		tag.setTrend(trendObjectList);

		return tag;
	}	
}
