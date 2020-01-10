package dhi.optimizer.schedule.executor.nn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;
import dhi.optimizer.schedule.executor.ScheduleExecutor;

@Service
public class NNModelMasterExecutor extends ScheduleExecutor {
	
	private static final Logger logger = LoggerFactory.getLogger(NNModelMasterExecutor.class.getSimpleName());
	
	private static final String MODEL_ID = "NN_MASTER";

	@Autowired
	private NNModelBaseExecutor nnModelBaseExecutor;
	
	public NNModelMasterExecutor() {}	
	public NNModelMasterExecutor(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);		
	}
	
	/**
	 * 시작함수.
	 */
	@Override
	public void start() {
		logger.info("### " + NNModelMasterExecutor.class.getName() + " Start ###");
		try {	
			nnModelBaseExecutor.start(MODEL_ID);
		} catch (Exception e) {
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			logger.info("### " + NNModelMasterExecutor.class.getName() + " End ###");
		}
	}
}
