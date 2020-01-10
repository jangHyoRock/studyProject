package dhi.optimizer.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.CtrDataInputEntity;
import dhi.optimizer.model.db.CtrDataInputIdEntity;

@Repository
public interface CtrDataInputRepository extends JpaRepository<CtrDataInputEntity, CtrDataInputIdEntity> {

	@Query(value = "SELECT TAG_ID, TAG_VAL FROM V_LAST_CTR_DATA_INPUT", nativeQuery = true)
	List<Object[]> getAllLastCtrDataInput();
	
	@Query(value = "SELECT TAG_ID, TAG_NM, TAG_VAL, UNIT, TIMESTAMP FROM V_LAST_CTR_DATA_INPUT", nativeQuery = true)
	List<Object[]> getAllLastCtrDataInputTag();
	
	@Query(value = "SELECT TAG_ID, TAG_NM, TAG_VAL, UNIT, TIMESTAMP FROM V_LAST_CTR_DATA_INPUT WHERE TAG_ID IN :tagIds ", nativeQuery = true)
	List<Object[]> getAllLastCtrDataInputTag(@Param("tagIds") Collection<String> tagIds);
	
	@Query(value = "SELECT MIN(B.TAG_VAL) AS TAG_VAL, MIN(TIMESTAMP) AS TIMESTAMP "
			+ "FROM TB_CTR_DATA_CONF A, TB_CTR_DATA_HIST B "
			+ "WHERE A.TAG_NM = B.TAG_NM "
			+ "AND A.TAG_ID = ?1 "
			+ "AND B.TIMESTAMP > "
			+ "("
			+ "	SELECT CASE WHEN MAX(B.TIMESTAMP) IS NULL THEN (SELECT MIN(TIMESTAMP) FROM TB_CTR_DATA_HIST) ELSE MAX(B.TIMESTAMP) END "
			+ "	FROM TB_CTR_DATA_CONF A, TB_CTR_DATA_HIST B "
			+ "	WHERE A.TAG_NM = B.TAG_NM "
			+ "	AND A.TAG_ID = ?1 "
			+ "	AND B.TAG_VAL <> "
			+ "	( "
			+ " 	SELECT B.TAG_VAL "
			+ " 	FROM TB_CTR_DATA_CONF A, TB_CTR_DATA_INPUT B "
			+ " 	WHERE A.TAG_NM = B.TAG_NM "
			+ " 	AND A.TAG_ID = ?1 "
			+ "	) "
			+ ") ", nativeQuery = true)
	List<Object[]> getDataInputFirstStatusChangeInfo(String tagId);
}