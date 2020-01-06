package dhi.optimizer.common.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.model.db.CommonControlEntity;

@Repository
public interface CommonControlRepository extends JpaRepository<CommonControlEntity, Date> {

	@Transactional("commonTransactionManager")
	@Modifying
	@Query(value = "UPDATE TB_COMMON_CONTROL SET SYSTEM_START = :isSystemStart, CONTROL_MODE = :controlMode, OPT_MODE = :optMode, TIMESTAMP = :timestamp WHERE P_UNIT_ID = :plantUnitId AND TIMESTAMP < :timestamp ", nativeQuery = true)
	void updateCommonControl(@Param("isSystemStart") boolean isSystemStart
			, @Param("controlMode") String controlMode
			, @Param("optMode") String optMode
			, @Param("timestamp") Date timestamp
			, @Param("plantUnitId") String plantUnitId);
}