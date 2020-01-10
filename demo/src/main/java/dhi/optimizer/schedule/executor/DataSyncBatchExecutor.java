package dhi.optimizer.schedule.executor;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.common.repository.CommonControlRepository;
import dhi.optimizer.repository.ControlRepository;

/**
 * Data Sync Scheduler. <br>
 * : Optimizer 시스템에 있는 'System Start', 'Control Mode', 'OPT Mode' 값을 Common 시스템에 5초 주기로  동기화 한다. (Footer 데이터 동기)
 */
@Service
public class DataSyncBatchExecutor extends ScheduleExecutor {

	private static final Logger logger = LoggerFactory.getLogger(DataSyncBatchExecutor.class.getSimpleName());
	
	@Autowired
	private ControlRepository controlRepository;
	
	@Autowired
	private CommonControlRepository commonControlRepository;
	
	public DataSyncBatchExecutor() {
	};

	public DataSyncBatchExecutor(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);
	}
	
	/**
	 * DataSyncBatchExecutor 시작함수.
	 */
	@Override
	public void start() {
		logger.info("### " + DataSyncBatchExecutor.class.getName() + " Start ###");
		try {
			this.commonDataToSystemControlProcess();
		} catch (Exception e) {
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			logger.info("### " + DataSyncBatchExecutor.class.getName() + " End ###");
		}
	}

	/**
	 * Optimizer System Control 정보를  Common Data 정보에 이관한다.
	 */
	private void commonDataToSystemControlProcess() {

		try {

			List<Object[]> resultList = this.controlRepository.getControlAndUnitId();
			boolean isSystemStart = false;
			String plantUnitId = CommonConst.StringEmpty;
			String controlMode = CommonConst.StringEmpty;
			String optMode = CommonConst.StringEmpty;
			Date timestamp = null;
			if (resultList != null) {
				Object[] result = resultList.get(0);
				plantUnitId = String.valueOf(result[0]);
				isSystemStart = String.valueOf(result[1]).equals("1") ? true : false;
				controlMode = String.valueOf(result[2]);
				optMode = String.valueOf(result[3]);
				timestamp = (Date) result[4];
			}

			if (!CommonConst.StringEmpty.equals(controlMode) && !CommonConst.StringEmpty.equals(optMode)) {
				this.commonControlRepository.updateCommonControl(isSystemStart, controlMode, optMode, timestamp,
						plantUnitId);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
