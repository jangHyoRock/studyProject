package dhi.optimizer.schedule.executor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;

/**
 * Task Manager Scheduler. <br>
 * : Task Manager Procedure를  1초 주기로 호출한다.
 */
@Service
public class TaskManagerExecutor extends ScheduleExecutor {
		
	private static final Logger logger = LoggerFactory.getLogger(TaskManagerExecutor.class.getSimpleName());
	private static final String TASK_MANAGER_PROCEDURE = "SP_ALGORITHM_TASK_MANAGER";
	
	public TaskManagerExecutor() {};

	public TaskManagerExecutor(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);
	}
	
	/**
	 * TaskManagerExecutor 시작함수.
	 */
	@Override
	public void start() {
		logger.info("### " + TaskManagerExecutor.class.getName() + " Start ###");
		EntityManager em = this.emFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			StoredProcedureQuery query = em.createStoredProcedureQuery(TASK_MANAGER_PROCEDURE);
			query.execute();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			em.close();
			logger.info("### " + TaskManagerExecutor.class.getName() + " End ###");
		}
	}
}