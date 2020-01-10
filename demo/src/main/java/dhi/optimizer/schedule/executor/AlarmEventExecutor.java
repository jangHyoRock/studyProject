package dhi.optimizer.schedule.executor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dhi.common.util.Utilities;

/**
 * Alarm & Event Scheduler. <br>
 * : Alarm & Event 데이터 생성을 위해 Procedure를 5초 주기로 호출한다.
 */
@Service
public class AlarmEventExecutor extends ScheduleExecutor {
		
	private static final Logger logger = LoggerFactory.getLogger(AlarmEventExecutor.class.getSimpleName());
	private static final String ALARM_EVENT_PROCEDURE = "SP_ALARM_EVENT";

	public AlarmEventExecutor() {};

	public AlarmEventExecutor(int id, int interval, boolean isSystemReadyCheck) {
		super(id, interval, isSystemReadyCheck);
	}
	
	/**
	 * AlarmEventExecutor 시작함수.
	 */
	@Override
	public void start() {
		
		logger.info("### " + AlarmEventExecutor.class.getName() + " Start ###");
		EntityManager em = this.emFactory.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			StoredProcedureQuery query = em.createStoredProcedureQuery(ALARM_EVENT_PROCEDURE);			
			query.execute();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();	
			logger.error(Utilities.getStackTrace(e));
			throw e;
		} finally {
			em.close();
			logger.info("### " + AlarmEventExecutor.class.getName() + " End ###");
		}
	}
}