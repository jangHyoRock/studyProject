package dhi.optimizer.repository;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.model.db.CostSavingEffectEntity;

/*
 * JAP control repository.
 */
@Repository
public interface EffectCalculationRepository extends JpaRepository<CostSavingEffectEntity, Date> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO TB_COST_SAVING_EFFECT (TIMESTAMP, COAL_VAL, PWR_VAL, VALID_YN) "
			+ "VALUES(?1, ?2, ?3, ?4)", nativeQuery = true)
	void insertAll(Date timestamp, double coalVal, double powerVal, boolean validYn);
		
	@Query(value = "SELECT P_UNIT_ID FROM TB_APP_INF", nativeQuery = true)
	String findPlantUnitIdNativeQuery();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_COST_SAVING_EFFECT SET VALID_YN=false", nativeQuery = true)
	void resetCostSavingEffect();
	
	@Query(value = "SELECT HOLD_YN FROM TB_OUTPUT_HOLD WHERE HOLD_ID = 'ALL'", nativeQuery = true)
	String findAllHoldYnNativeQuery(); 
	
	@Query(value = "SELECT MAX(TIMESTAMP) AS TIMESTAMP FROM TB_COST_SAVING_EFFECT", nativeQuery = true)
	String findMaxTimestampNativeQuery();
}