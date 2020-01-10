package dhi.optimizer.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.PSOMVInfoEntity;

/*
 * JAP control repository.
 */
@Repository
public interface PSOMVInfoRepository extends JpaRepository<PSOMVInfoEntity, String> {
		
	@Query(value = "SELECT COUNT(1) FROM TB_PSO_MV_INFO WHERE PSO_MV IN (SELECT PSO_MV from TB_NN_TRAIN_DATA_CONF)", nativeQuery = true)
	int findCountByPsoMVInTrainDataConfNativeQuery();
	
	List<PSOMVInfoEntity> findByPsoMVInOrderByPsoMVOrderAsc(Collection<String> psoMVs);
	
	@Query(value = "SELECT A.PSO_MV, B.BIAS_VAL, C.TAG_VAL AS HOLD_VAL, A.OUTPUT_BIAS_TAG_ID "
			+ "FROM TB_PSO_MV_INFO A, "
			+ "( "
			+ "	SELECT A.PSO_MV, A.HOLD_TAG_ID, B.TAG_VAL AS BIAS_VAL "
			+ "	FROM TB_PSO_MV_INFO A, V_LAST_CTR_DATA_OUTPUT B "
			+ "	WHERE A.INPUT_BIAS_TAG_ID = B.TAG_ID "
			+ "	AND A.PSO_MV_TYPE <>  'Air' "
			+ "	UNION ALL "
			+ "	SELECT A.PSO_MV, A.HOLD_TAG_ID, B.TAG_VAL AS BIAS_VAL "
			+ "	FROM TB_PSO_MV_INFO A, V_LAST_CTR_DATA_INPUT B "
			+ "	WHERE A.INPUT_BIAS_TAG_ID = B.TAG_ID "
			+ "	AND A.PSO_MV_TYPE = 'Air' "
			+ ") B, V_LAST_CTR_DATA_OUTPUT C "
			+ "WHERE A.PSO_MV = B.PSO_MV "
			+ "AND A.HOLD_TAG_ID = C.TAG_ID ", nativeQuery = true)
	List<Object[]> getPsoMvInfoValueBiasAndHold();	
}