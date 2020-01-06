package dhi.optimizer.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.model.db.ControlHistoryEntity;

/*
 * JAP control repository.
 */
@Repository
public interface ControlHistoryRepository extends JpaRepository<ControlHistoryEntity, Date> {
		
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO TB_CONTROL_HIST (TIMESTAMP, SYSTEM_START, CONTROL_MODE, OPT_MODE, LEARNING_MODE, USER_ID) "
			+ "VALUES(NOW(), ?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
	void insertAll(boolean isSystemStart, String controlMode, String optMode, String learningMode, String userId);
		
	@Query(value = "SELECT * FROM TB_CONTROL_HIST WHERE TIMESTAMP >= ?1 AND TIMESTAMP <= ?2 ORDER BY TIMESTAMP ASC", nativeQuery = true)
	List<ControlHistoryEntity> findByTimestampNativeQuery(Date startDate, Date endDate);
	
	
	@Query(value = "SELECT TOP 1 * FROM TB_CONTROL_HIST WHERE TIMESTAMP < ?1 ORDER BY TIMESTAMP DESC", nativeQuery = true)
	ControlHistoryEntity findByRecentDataFromControlHistNativeQuery(Date startDate);
}