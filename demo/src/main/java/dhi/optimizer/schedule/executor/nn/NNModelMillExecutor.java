package dhi.optimizer.schedule.executor.nn;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;
import dhi.optimizer.repository._NNModelExecStatusRepository;
import dhi.optimizer.schedule.ScheduleExecutorThread;
import dhi.optimizer.schedule.executor.ScheduleExecutor;

@Service
public class NNModelMillExecutor extends ScheduleExecutor {

	private static final Logger logger = LoggerFactory.getLogger(NNModelMillExecutor.class.getSimpleName());
			
	@Autowired
	private _NNModelExecStatusRepository nnModelExecStatusRepository;
			
	public NNModelMillExecutor() {}
	
	public NNModelMillExecutor(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);		
	}
	
	/**
	 * 시작함수.
	 */
	@Override
	public void start() {
		logger.info("### " + NNModelMillExecutor.class.getName() + " Start ###");
		try {
			
			// EXEC_PRIORITY(우선순위) 값이 0 인 것만 실행한다. 
			List<Object[]> nnModelExecStatusList = nnModelExecStatusRepository.getNNModelMillExecuterStatusFirstPriorityNativeQuery();
			if (nnModelExecStatusList.size() <= 0) {
				logger.info("### There is no Mill Executor to run nnModel at this time. ###");
			}

			List<Thread> threadList = new ArrayList<Thread>();
			for (int i = 0; i < nnModelExecStatusList.size(); i++) {
				Object[] objNNModelExecStatus = nnModelExecStatusList.get(i);
				String modelId = (String) objNNModelExecStatus[0];
				
				Thread thread = new Thread(new ScheduleExecutorThread(modelId, NNModelBaseExecutor.class));
				threadList.add(thread);
				thread.start();							 
			}

			while (true) {
				int cnt = 0;
				for (Thread thread : threadList) {
					if (!thread.isAlive())
						cnt++;
				}

				if (cnt == threadList.size()) {
					break;
				}
			}	
			
			// EXEC_PRIORITY(우선순위) 값이 0 인 것을 맨 마지막 순서로 변경 및 우선순위를 업데이트 한다. 
			nnModelExecStatusRepository.updateNNModelMillExecuterPriorityChange();
			
		} catch (Exception e) {
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			logger.info("### " + NNModelMillExecutor.class.getName() + " End ###");
		}
	}
}