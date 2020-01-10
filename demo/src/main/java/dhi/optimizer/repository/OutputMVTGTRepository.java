package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.model.db.OutputMVTGTEntity;

public interface OutputMVTGTRepository extends JpaRepository<OutputMVTGTEntity, String>{
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_OUTPUT_MV_TGT SET MV_TGT = ?2, TIMESTAMP = NOW() WHERE MV = ?1 ", nativeQuery = true)
	void updateOutputMVTGT(String mv, double mvTGTVal);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_OUTPUT_MV_TGT SET MV_BIAS_SUM = MV_BIAS_SUM + ?2, MV_BIAS_CNT = MV_BIAS_CNT + 1 WHERE MV = ?1 ", nativeQuery = true)
	void updateOutputMVBiasSumCnt(String mv, double mvBias);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_OUTPUT_MV_TGT SET MV_BIAS_SUM = 0, MV_BIAS_CNT = 0 ", nativeQuery = true)
	void updateOutputMVBiasSumCntInitialize();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_OUTPUT_MV_TGT SET MV_TGT = 0, TIMESTAMP = NOW(), MV_BIAS_SUM = 0, MV_BIAS_CNT = 0 ", nativeQuery = true)
	void updateMVTGTInitialize();
}
