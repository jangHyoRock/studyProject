package dhi.optimizer.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.model.db.CtrDataOutputEntity;
import dhi.optimizer.model.db.CtrDataOutputIdEntity;

@Repository
public interface CtrDataOutputRepository extends JpaRepository<CtrDataOutputEntity, CtrDataOutputIdEntity> {
	
	@Query(value = "SELECT TAG_ID, TAG_VAL, TIMESTAMP FROM V_LAST_CTR_DATA_OUTPUT WHERE TAG_ID = ?1 ", nativeQuery = true)
	List<Object[]> getCtrDataOutput(String tagId);
	
	@Query(value = "SELECT TAG_ID, TAG_VAL FROM V_LAST_CTR_DATA_OUTPUT", nativeQuery = true)
	List<Object[]> getAllLastCtrDataOutput();
	
	@Query(value = "SELECT TAG_ID, TAG_NM, TAG_VAL, UNIT, TIMESTAMP FROM V_LAST_CTR_DATA_OUTPUT", nativeQuery = true)
	List<Object[]> getAllLastCtrDataOutputTag();
	
	@Query(value = "SELECT TAG_ID, TAG_NM, TAG_VAL, UNIT, TIMESTAMP FROM V_LAST_CTR_DATA_OUTPUT WHERE TAG_ID IN :tagIds ", nativeQuery = true)
	List<Object[]> getAllLastCtrDataOutputTag(@Param("tagIds") Collection<String> tagIds);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CTR_DATA_OUTPUT A SET A.TAG_VAL = ?2, A.TIMESTAMP = NOW() "
			+ "FROM TB_CTR_DATA_OUTPUT A, TB_CTR_DATA_CONF B "
			+ "WHERE A.TAG_NM = B.TAG_NM "
			+ "AND B.TAG_ID = ?1 ", nativeQuery = true)
	void updateCtrDataOutputByTagId(String tagId, double tagVal);
	
	@Query(value = "SELECT COUNT(*) "
			+ "FROM V_LAST_CTR_DATA_OUTPUT "
			+ "WHERE TAG_ID IN (SELECT OUTPUT_BIAS_TAG_ID FROM TB_PSO_MV_INFO) "
			+ "AND ABS(TAG_VAL) > 0", nativeQuery = true)
	int getCtrDataOutputBiasNoZeroCount();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CTR_DATA_OUTPUT A SET A.TAG_VAL = 0, A.TIMESTAMP = NOW() "
			+ "FROM TB_CTR_DATA_OUTPUT A, TB_CTR_DATA_CONF B "
			+ "WHERE A.TAG_NM = B.TAG_NM "
			+ "AND B.TAG_ID IN (SELECT OUTPUT_BIAS_TAG_ID FROM TB_PSO_MV_INFO) ", nativeQuery = true)
	void updateCtrDataOutputBiasInitialize();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CTR_DATA_OUTPUT A SET A.TAG_VAL = 0, A.TIMESTAMP = NOW() "
			+ "FROM TB_CTR_DATA_OUTPUT A, TB_CTR_DATA_CONF B "
			+ "WHERE A.TAG_NM = B.TAG_NM "
			+ "AND B.TAG_ID IN (SELECT OUTPUT_BIAS_TAG_ID FROM TB_PSO_MV_INFO WHERE PSO_MV_TYPE <> 'Air') ", nativeQuery = true)
	void updateCtrDataOutputDamperBiasInitialize();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CTR_DATA_OUTPUT A SET A.TAG_VAL = C.TAG_VAL, A.TIMESTAMP = NOW() "
			+ "FROM TB_CTR_DATA_OUTPUT A, TB_CTR_DATA_CONF B, ( "
			+ "SELECT C.OUTPUT_BIAS_TAG_ID AS TAG_ID, A.TAG_VAL "
			+ "FROM TB_CTR_DATA_INPUT A, TB_CTR_DATA_CONF B, TB_PSO_MV_INFO C "
			+ "WHERE A.TAG_NM = B.TAG_NM "
			+ "AND B.TAG_ID = C.INPUT_BIAS_TAG_ID "
			+ "AND C.PSO_MV_TYPE = 'Air' "
			+ ") C WHERE A.TAG_NM = B.TAG_NM "
			+ "AND B.TAG_ID = C.TAG_ID ", nativeQuery = true)
	void updateCtrDataOutputTotAirBiasInitialize();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CTR_DATA_OUTPUT A SET A.TAG_VAL = ?2, A.TIMESTAMP = NOW() "
			+ "FROM TB_CTR_DATA_OUTPUT A, TB_CTR_DATA_CONF B "
			+ "WHERE A.TAG_NM = B.TAG_NM "
			+ "AND B.TAG_ID = ?1 "
			+ "AND A.TAG_VAL <> ?2", nativeQuery = true)
	void updateCtrDataOutputByTagIdAndTagValDifferent(String tagId, double tagVal);
}