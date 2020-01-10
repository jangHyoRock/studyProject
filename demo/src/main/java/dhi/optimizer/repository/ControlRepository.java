package dhi.optimizer.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.model.db.ControlEntity;

/*
 * JAP control repository.
 */
@Repository
public interface ControlRepository extends JpaRepository<ControlEntity, Date> {
	
	@Query(value = "SELECT * FROM TB_CONTROL", nativeQuery = true)
	ControlEntity findControl();
	
	@Query(value = "SELECT A.P_UNIT_ID, B.SYSTEM_START, B.CONTROL_MODE, B.OPT_MODE, B.TIMESTAMP FROM TB_APP_INF A, TB_CONTROL B", nativeQuery = true)
	List<Object[]> getControlAndUnitId();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CONTROL SET SYSTEM_START = ?1, CONTROL_MODE = ?2, LEARNING_MODE = ?3, TIMESTAMP = NOW()", nativeQuery = true)
	void updateSystemStartValueOff(boolean isSystemStart, String learningMode, String controlMode);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CONTROL SET SYSTEM_START = ?1, TIMESTAMP = NOW()", nativeQuery = true)
	void updateSystemStartAndTimeStamp(boolean isSystemStart);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CONTROL SET CONTROL_MODE = ?1, TIMESTAMP = NOW()", nativeQuery = true)
	void updateControlModeAndTimeStamp(String controlMode);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CONTROL SET OPT_MODE = ?1, TIMESTAMP = NOW()", nativeQuery = true)
	void updateOptModeAndTimeStamp(String optMode);
		
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CONTROL SET LEARNING_MODE = ?1, TIMESTAMP = NOW()", nativeQuery = true)
	void updateLearningModeAndTimeStamp(String learningMode);
	
	@Query(value = "SELECT 'CONTROL_READY' AS ITEM, CASE WHEN CONTROL_READY = True THEN 'READY' ELSE 'NOT READY' END AS STATUS, CONTROL_READY_TS FROM TB_SYSTEM_CHECK "
			+ "UNION "
			+ "SELECT 'CONTROL_MODE', MIN(CONTROL_MODE), MIN(TIMESTAMP) AS TIMESTAMP "
			+ "FROM TB_CONTROL_HIST WHERE TIMESTAMP > "
			+ "( "
			+ "	SELECT MAX(TIMESTAMP) FROM TB_CONTROL_HIST "
			+ "	WHERE CONTROL_MODE <> (SELECT CONTROL_MODE FROM TB_CONTROL) "
			+ ") UNION "
			+ "SELECT 'OPT_MODE', MIN(OPT_MODE), MIN(TIMESTAMP) AS TIMESTAMP "
			+ "FROM TB_CONTROL_HIST WHERE TIMESTAMP >  "
			+ "( "
			+ "	SELECT MAX(TIMESTAMP) FROM TB_CONTROL_HIST "
			+ "	WHERE OPT_MODE <> (SELECT OPT_MODE FROM TB_CONTROL) "
			+ ")", nativeQuery = true)
	List<Object[]> getControlAndSystemCheckInfo();
}
