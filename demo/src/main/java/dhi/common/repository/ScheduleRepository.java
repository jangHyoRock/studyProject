package dhi.common.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dhi.common.model.db.ScheduleEntity;

/*
 * JAP schedule repository.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {
	
	ScheduleEntity findByScheduleId(int scheduleId);
	
	@Transactional
	@Modifying	
	@Query(value = "UPDATE TB_SCHEDULE SET START_DT = ?2, STATUS = ?3 WHERE SCHEDULE_ID = ?1", nativeQuery = true)
	void updateStartDTAndStatusByScheduleId(int scheduleId, Date startDT, int status);
	
	@Transactional
	@Modifying	
	@Query(value = "UPDATE TB_SCHEDULE SET END_DT = ?2, STATUS = ?3 WHERE SCHEDULE_ID = ?1", nativeQuery = true)
	void updateEndDTAndStatusByScheduleId(int scheduleId, Date endDT, int status);
}
