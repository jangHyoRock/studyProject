package dhi.optimizer.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.OutputDataHistEntity;

/*
 * JAP control repository.
 */
@Repository
public interface OutputDataHistRepository extends JpaRepository<OutputDataHistEntity, Date> {

	@Modifying
	@Query(value = "INSERT INTO TB_OUTPUT_DATA_HIST (TIMESTAMP, CONTROL_VALUE, CONTROL_MV, CONTROL_VALUE_HOLD_YN) "
			+ "VALUES (NOW(),?1,?2,?3) ", nativeQuery = true)
	void insertHistory(double mvBiasVal, String psoMV, String holdYN);
}