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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;
import dhi.optimizer.algorithm.common.DoubleRange;
import dhi.optimizer.algorithm.common.PerformanceCalculation;
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
import dhi.optimizer.model.ConfDataVO;
import dhi.optimizer.model.NavigatorTubeWWTempVO;
import dhi.optimizer.model.TagDataVO;
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
import dhi.optimizer.model.db._NNModelConfEntity;
import dhi.optimizer.model.json.KeyValue;
import dhi.optimizer.repository.AppInfRepository;
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
import dhi.optimizer.repository._NNModelConfRepository;
import dhi.optimizer.schedule.IScheduleExecutorThread;
import dhi.optimizer.service.AlgorithmService;
import dhi.optimizer.service.CommonDataService;

@Service
@Scope(value="prototype")
public class PSOBaseExecutor implements IScheduleExecutorThread {

	private enum UseModelType {
		Base, Learning
	}
	
	private static final Logger logger = LoggerFactory.getLogger(PSOBaseExecutor.class.getSimpleName());
		
	private static final double NAVIGATOR_MIN_MAX_LIMIT = 0.1;
	
	@Autowired
	private ControlRepository controlRepository;

	@Autowired
	private NNConfigRepository nnConfigRepository;
	
	@Autowired
	private _NNModelConfRepository nnModelConfRepository;
	
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
	private OutputMVTGTRepository outputMVTGTRepository;
	
	@Autowired
	private AppInfRepository appInfRepository;
	
	@Autowired
	private AlgorithmService algorithmService;
	
	@Value("${algorithm.pso.computer.processing.unit}")
	private String processingUnit;
	
	@Value("${algorithm.pso.computer.processing.unit.gpu.swarmsize}")
	private int gpuUseSwarmsize;
	
	public PSOBaseExecutor() {
	}

	/**
	 * AlgorithmExecutor 시작함수.
	 */
	@Override
	public void start(Object param) {
		logger.info("### " + PSOBaseExecutor.class.getName() + " Start ###");
		try {
			for(int i=0;i<8;i++)
			{
			
				switch ("MASTER")
				{
				case "MASTER":
					
				//	this.PSOAlgorithm("pso_master");
					break;
					
				case "MILL":				
			//		this.PSOAlgorithm("pso_master", "psoID");				
					break;
				case "SA":
			//		this.PSOAlgorithm("pso_sa");
					break;				
				}
			}
			
		} catch (Exception e) {
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			logger.info("### " + PSOBaseExecutor.class.getName() + " End ###");
		}
	}
}
