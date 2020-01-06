package dhi.optimizer.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.model.db.ScheduleHistoryEntity;

@Repository
public interface ScheduleHistoryRepository extends JpaRepository<ScheduleHistoryEntity, Integer> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO TB_SCHEDULE_HIST VALUES(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
	void insertAll(int scheduleId, Date startDT, Date endDT, int status, String error);
	
	@Transactional
	@Modifying	
	@Query(value = "UPDATE TB_SCHEDULE_HIST SET END_DT = ?3, STATUS = ?4 WHERE SCHEDULE_ID = ?1 AND START_DT = ?2 ", nativeQuery = true)
	void updateEndDTAndStatusByScheduleIdAndStartDT(int scheduleId, Date startDT, Date endDT, int status);
	
	@Transactional
	@Modifying	
	@Query(value = "UPDATE TB_SCHEDULE_HIST SET END_DT = ?3, STATUS = ?4, Error = ?5 WHERE SCHEDULE_ID = ?1 AND START_DT = ?2 ", nativeQuery = true)
	void updateEndDTAndStatusAndErrorByScheduleIdAndStartDT(int scheduleId, Date startDT, Date endDT, int status, String error);
}
