package dhi.common.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.CommonDataInputEntity;
import dhi.common.model.db.CommonDataInputIdEntity;

@Repository
public interface CommonDataInputRepository extends JpaRepository<CommonDataInputEntity, CommonDataInputIdEntity> {

	@Query(value = "SELECT P_UNIT_ID, TAG_ID, TAG_VAL, UNIT FROM V_LAST_COMMON_DATA_INPUT ", nativeQuery = true)
	List<Object[]> getAllLastCommonDataTag();
	
	@Query(value = "SELECT P_UNIT_ID, TAG_ID, TAG_VAL, UNIT FROM V_LAST_COMMON_DATA_INPUT WHERE TAG_ID IN :tagIds ", nativeQuery = true)
	List<Object[]> getLastCommonDataTag(@Param("tagIds") Collection<String> tagIds);
	
	@Query(value = "SELECT TO_VARCHAR(NOW(), 'YYYY-MM-DD HH24:MI')  || ':' ||LPAD(SECOND(NOW()) - MOD(SECOND(NOW()), ?1), 2, '00') AS  TIMESTAMP FROM DUMMY ", nativeQuery = true)
	String getNowTimestamp(int interval);
	
	@Query(value = "SELECT A.P_UNIT_ID, A.P_UNIT_NM, B.LOAD, B.LOAD_UNIT, B.FREQ, B.FREQ_UNIT, B.AF, B.AF_UNIT, B.FP, B.FP_UNIT, B.CF, B.CF_UNIT, B.O2, B.O2_UNIT, C.SYSTEM_START, C.CONTROL_MODE, C.OPT_MODE "
			+ "FROM TB_UNIT A INNER JOIN "
			+ "("
			+ "	SELECT P_UNIT_ID "
			+ " , MAX(CASE WHEN TAG_ID = :loadTagId THEN TAG_VAL ELSE NULL END) AS LOAD "
			+ " , MAX(CASE WHEN TAG_ID = :loadTagId THEN UNIT ELSE NULL END) AS LOAD_UNIT "
			+ "	, MAX(CASE WHEN TAG_ID = :freqTagId THEN TAG_VAL ELSE NULL END) AS FREQ "
			+ "	, MAX(CASE WHEN TAG_ID = :freqTagId THEN UNIT ELSE NULL END) AS FREQ_UNIT "
			+ "	, MAX(CASE WHEN TAG_ID = :afTagId THEN TAG_VAL ELSE NULL END) AS AF "
			+ "	, MAX(CASE WHEN TAG_ID = :afTagId THEN UNIT ELSE NULL END) AS AF_UNIT "
			+ "	, MAX(CASE WHEN TAG_ID = :fpTagId THEN TAG_VAL ELSE NULL END) AS FP "
			+ "	, MAX(CASE WHEN TAG_ID = :fpTagId THEN UNIT ELSE NULL END) AS FP_UNIT "
			+ "	, MAX(CASE WHEN TAG_ID = :cfTagId THEN TAG_VAL ELSE NULL END) AS CF "
			+ "	, MAX(CASE WHEN TAG_ID = :cfTagId THEN UNIT ELSE NULL END) AS CF_UNIT "
			+ "	, AVG(CASE WHEN TAG_ID = :gasO2R1TagId OR TAG_ID = :gasO2R2TagId OR TAG_ID = :gasO2L1TagId OR TAG_ID = :gasO2L2TagId THEN TAG_VAL ELSE NULL END) AS O2 "
			+ "	, MAX(CASE WHEN TAG_ID = :gasO2R1TagId THEN UNIT ELSE NULL END) AS O2_UNIT "
			+ "	FROM V_LAST_COMMON_DATA_INPUT "
			+ "	GROUP BY P_UNIT_ID "
			+ " ) B ON A.P_UNIT_ID = B.P_UNIT_ID LEFT OUTER JOIN TB_COMMON_CONTROL C "
			+ " ON A.P_UNIT_ID = C.P_UNIT_ID "
			+ "ORDER BY A.P_UNIT_ID ASC ", nativeQuery = true)
	List<Object[]> getCommonFooterData(@Param("loadTagId") String loadTagId, @Param("freqTagId") String freqTagId,
			@Param("afTagId") String afTagId, @Param("fpTagId") String fpTagId, @Param("cfTagId") String cfTagId,			
			@Param("gasO2R1TagId") String gasO2R1TagId, @Param("gasO2R2TagId") String gasO2R2TagId,
			@Param("gasO2L1TagId") String gasO2L1TagId, @Param("gasO2L2TagId") String gasO2L2TagId);
}
