package dhi.optimizer.repository;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.model.db.SystemCheckEntity;

/*
 * JAP control repository.
 */
@Repository
public interface SystemCheckRepository extends JpaRepository<SystemCheckEntity, Date> {
	
	@Query(value = "SELECT * FROM TB_SYSTEM_CHECK", nativeQuery = true)
	SystemCheckEntity findControl();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_SYSTEM_CHECK SET SYSTEM_READY = false "
			+ ", SYSTEM_READY_TS = NOW() "
			+ ", CONTROL_READY = false "
			+ ", CONTROL_READY_TS = NOW() "
			+ ", CONTROL_READY_TM = false "
			+ ", CONTROL_READY_AL = false ", nativeQuery = true)
	void updateSetSystemReadyAndControlReadyFalse();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_SYSTEM_CHECK SET CONTROL_READY = false "
			+ ", CONTROL_READY_TS = NOW() "
			+ ", CONTROL_READY_TM = false "
			+ ", CONTROL_READY_AL = false ", nativeQuery = true)
	void updateSetControlReadyFalse();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_SYSTEM_CHECK SET CONTROL_READY_AL = ?1 ", nativeQuery = true)
	void updateSetControlReadyAl(boolean isReady);	
}
