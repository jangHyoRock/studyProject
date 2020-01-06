package dhi.optimizer.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.OPDataInputEntity;
import dhi.optimizer.model.db.OPDataInputIdEntity;

@Repository
public interface OPDataInputRepository extends JpaRepository<OPDataInputEntity, OPDataInputIdEntity> {

	@Query(value = "SELECT IFNULL(C.PSO_MV, '') AS PSO_MV, C.PSO_MV_TYPE, A.TAG_NM, B.TAG_VAL " 
			+ "FROM TB_NN_TRAIN_DATA_CONF A INNER JOIN V_LAST_OP_DATA_INPUT B " 
			+ "ON A.TAG_NM = B.TAG_NM "
			+ "AND A.IO_TYPE = 'Input' LEFT OUTER JOIN TB_PSO_MV_INFO C "
			+ "ON A.PSO_MV = C.PSO_MV "
			+ "ORDER BY A.TAG_NO ", nativeQuery = true)
	List<Object[]> findByLastNNTrainInputOPDataNativeQuery();
	
	@Query(value = "SELECT B.TIMESTAMP, STRING_AGG(B.TAG_VAL,','ORDER BY A.TAG_NO ASC) AS DATAS " 
			+ "FROM TB_NN_TRAIN_DATA_CONF A INNER JOIN V_LAST_PSO_DATA_INPUT B " 
			+ "ON A.TAG_NM = B.TAG_NM "
			+ "AND A.IO_TYPE = 'Input' "
			+ "GROUP BY B.TIMESTAMP ", nativeQuery = true)
	List<Object[]> findByTimestampLastNNTrainInputPSODataNativeQuery();
	
	@Query(value = "SELECT A.MODEL_ID, A.IO_TYPE, B.TIMESTAMP, STRING_AGG(B.TAG_VAL,','ORDER BY A.TAG_INDEX ASC) AS DATAS " 
			+ "FROM DD_TB_NN_TRAIN_DATA_CONF A INNER JOIN V_LAST_PSO_DATA_INPUT B " 
			+ "ON A.TAG_NM = B.TAG_NM "
			+ "GROUP BY A.MODEL_ID, A.IO_TYPE, B.TIMESTAMP ", nativeQuery = true)
	List<Object[]> getPSODataNativeQuery();
	
	@Query(value = "SELECT B.TIMESTAMP, STRING_AGG(B.TAG_VAL,','ORDER BY A.TAG_NO ASC) AS DATAS " 
			+ "FROM TB_NN_TRAIN_DATA_CONF A INNER JOIN V_LAST_PSO_DATA_INPUT B "
			+ "ON A.TAG_NM = B.TAG_NM "
			+ "AND A.IO_TYPE = 'Output' "
			+ "GROUP BY B.TIMESTAMP ", nativeQuery = true)
	List<Object[]> findByTimestampLastNNTrainOutputPSODataNativeQuery();
	
	@Query(value = "SELECT TAG_ID, TAG_VAL FROM V_LAST_OP_DATA_INPUT", nativeQuery = true)
	List<Object[]> getAllLastOpData();
	
	@Query(value = "SELECT TAG_ID, TAG_VAL FROM V_LAST_OP_DATA_INPUT WHERE TAG_ID IN :tagIds ", nativeQuery = true)
	List<Object[]> getAllLastOpData(@Param("tagIds") Collection<String> tagIds);	
	
	@Query(value = "SELECT TAG_ID, TAG_NM, TAG_VAL, UNIT, TIMESTAMP FROM V_LAST_OP_DATA_INPUT", nativeQuery = true)
	List<Object[]> getAllLastOpDataTag();
	
	@Query(value = "SELECT TAG_ID, TAG_NM, TAG_VAL, UNIT FROM V_LAST_OP_DATA_INPUT WHERE TAG_ID IN :tagIds ", nativeQuery = true)
	List<Object[]> getAllLastOpDataTag(@Param("tagIds") Collection<String> tagIds);

	@Query(value = "SELECT A.TAG_ID, A.TAG_NM, AVG(B.TAG_VAL) AS TAG_VAL, MAX(A.UNIT) AS UNIT "
			+ "FROM TB_OP_DATA_CONF A, TB_OP_DATA_INPUT B "
			+ "WHERE A.TAG_NM = B.TAG_NM AND B.TIMESTAMP > ADD_SECONDS((SELECT MAX(TIMESTAMP) FROM TB_OP_DATA_INPUT), -1 * ?) "
			+ "GROUP BY A.TAG_ID, A.TAG_NM ", nativeQuery = true)
	List<Object[]> getAvgOpDataTag(@Param("seconds") int seconds);
	
	@Query(value = "SELECT TIMESTAMP, MAX(CASE WHEN TAG_NM = ?1 THEN TAG_VAL ELSE 0 END) AS TGA1, " 
			+ "MAX(CASE WHEN TAG_NM = ?2 THEN TAG_VAL ELSE 0 END) AS TGA2 "
			+ "FROM TB_TREND_OP_DATA WHERE TAG_NM IN (?1, ?2) " 
			+ "AND TIMESTAMP >= ?3 AND TIMESTAMP <= ?4 "
			+ "GROUP BY TIMESTAMP " 
			+ "HAVING MOD(HOUR(TIMESTAMP) * 3600 + MINUTE(TIMESTAMP) * 60 + SECOND(TIMESTAMP), ?5) = 0 "
			+ "ORDER BY TIMESTAMP ASC ", nativeQuery = true)
	List<Object[]> findBy2TagAndTimestampSecModNativeQuery(String trendTagNm1, String trendTagNm2, Date startDate, Date endDate, int secModNumber);
	
	@Query(value = "SELECT TIMESTAMP, STRING_AGG(C.TAG_VAL,',' ORDER BY B.CODE_ORDER ASC) AS DATAS "
			+ "FROM TB_OP_DATA_CONF A, TB_OPT_CODE_INF B, TB_TREND_OP_DATA C "
			+ "WHERE A.TAG_ID = B.TAG_ID "
			+ "AND B.GROUP_ID = 'trend_category' "
			+ "AND A.TAG_NM = C.TAG_NM "
			+ "AND C.TIMESTAMP >= ?1 AND C.TIMESTAMP <= ?2 "
			+ "GROUP BY C.TIMESTAMP "
			+ "HAVING MOD(HOUR(C.TIMESTAMP) * 3600 + MINUTE(C.TIMESTAMP) * 60 + SECOND(C.TIMESTAMP), ?3) = 0 "
			+ "ORDER BY C.TIMESTAMP ASC ", nativeQuery = true)
	List<Object[]> findByAllTagAndTimestampSecModNativeQuery(Date startDate, Date endDate, int secModNumber);
		
	@Query(value = "SELECT B.DATA_TYPE, A.TIMESTAMP, MAX(A.TAG_VAL) AS MAX_TAG_VAL " 
			+ "FROM TB_TREND_NAV_DATA A, TB_NAV_TREND_DATA_CONF B "
			+ "WHERE A.TIMESTAMP IN "
			+ "( "
			+ " SELECT TIMESTAMP "
			+ "	FROM TB_TREND_NAV_DATA "
			+ "	WHERE TIMESTAMP >= :startDate AND TIMESTAMP <= :endDate "
			+ "	AND TAG_NM = :defaultTagNm "
			+ "	AND MOD(HOUR(TIMESTAMP) * 3600 + MINUTE(TIMESTAMP) * 60 + SECOND(TIMESTAMP), :modSec) = 0 "
			+ ") AND A.TAG_NM = B.TAG_NM "
			+ "AND B.DATA_TYPE IN :dataTypes "
			+ "GROUP BY A.TIMESTAMP, B.DATA_TYPE "
			+ "ORDER BY A.TIMESTAMP DESC "
			, nativeQuery = true)
	List<Object[]> getNavigatorTrendMaxValueByAlias(@Param("defaultTagNm") String defaultTagNm, @Param("dataTypes") Collection<String> dataTypes, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("modSec") int modSec);
	
	@Query(value = "SELECT B.DATA_TYPE, A.TIMESTAMP, AVG(A.TAG_VAL) AS AVG_TAG_VAL " 
			+ "FROM TB_TREND_NAV_DATA A, TB_NAV_TREND_DATA_CONF B "
			+ "WHERE A.TIMESTAMP IN "
			+ "( "
			+ " SELECT TIMESTAMP "
			+ "	FROM TB_TREND_NAV_DATA "
			+ "	WHERE TIMESTAMP >= :startDate AND TIMESTAMP <= :endDate "
			+ "	AND TAG_NM = :defaultTagNm "
			+ "	AND MOD(HOUR(TIMESTAMP) * 3600 + MINUTE(TIMESTAMP) * 60 + SECOND(TIMESTAMP), :modSec) = 0 "
			+ ") AND A.TAG_NM = B.TAG_NM "
			+ "AND B.DATA_TYPE IN :dataTypes "
			+ "GROUP BY A.TIMESTAMP, B.DATA_TYPE "
			+ "ORDER BY A.TIMESTAMP DESC "
			, nativeQuery = true)
	List<Object[]> getNavigatorTrendAvgValueByAlias(@Param("defaultTagNm") String defaultTagNm, @Param("dataTypes") Collection<String> dataTypes, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("modSec") int modSec);
	
	@Query(value = "SELECT B.DATA_TYPE, A.TIMESTAMP, A.TAG_VAL " 
			+ "FROM TB_TREND_NAV_DATA A, TB_NAV_TREND_DATA_CONF B "
			+ "WHERE A.TIMESTAMP IN "
			+ "( "
			+ " SELECT TIMESTAMP "
			+ "	FROM TB_TREND_NAV_DATA "
			+ "	WHERE TIMESTAMP >= :startDate AND TIMESTAMP <= :endDate "
			+ "	AND TAG_NM = :defaultTagNm "
			+ "	AND MOD(HOUR(TIMESTAMP) * 3600 + MINUTE(TIMESTAMP) * 60 + SECOND(TIMESTAMP), :modSec) = 0 "
			+ ") AND A.TAG_NM = B.TAG_NM "
			+ "AND B.DATA_TYPE IN :dataTypes "			
			+ "ORDER BY A.TIMESTAMP DESC "
			, nativeQuery = true)
	List<Object[]> getNavigatorTrendValueByAlias(@Param("defaultTagNm") String defaultTagNm, @Param("dataTypes") Collection<String> dataTypes, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("modSec") int modSec);
	
	@Query(value = "SELECT A.TIMESTAMP, AVG(A.TAG_VAL) AS AVG_TAG_VAL, MAX(A.TAG_VAL) AS MAX_TAG_VAL, MIN(A.TAG_VAL) AS MIN_TAG_VAL " 
			+ "FROM TB_TREND_NAV_DATA A, TB_NAV_TREND_DATA_CONF B "
			+ "WHERE A.TIMESTAMP IN "
			+ "( "
			+ " SELECT TIMESTAMP "
			+ "	FROM TB_TREND_NAV_DATA "
			+ "	WHERE TIMESTAMP >= :startDate AND TIMESTAMP <= :endDate "
			+ "	AND TAG_NM = :defaultTagNm "
			+ "	AND MOD(HOUR(TIMESTAMP) * 3600 + MINUTE(TIMESTAMP) * 60 + SECOND(TIMESTAMP), :modSec) = 0 "
			+ ") AND A.TAG_NM = B.TAG_NM "
			+ "AND B.DATA_TYPE IN :dataTypes "
			+ "GROUP BY A.TIMESTAMP "
			+ "ORDER BY A.TIMESTAMP DESC "
			, nativeQuery = true)
	List<Object[]> getNavigatorTrendAvgMaxMinValueByAlias(@Param("defaultTagNm") String defaultTagNm, @Param("dataTypes") Collection<String> dataTypes, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("modSec") int modSec);
	
	@Query(value = "SELECT 'bzs' AS DATA_TYPE, A.TIMESTAMP, (A.PA_FLOW + (A.SA_FLOW - A.OFA_FLOW)) / (A.PA_FLOW + A.SA_FLOW) * B.EXCESS_AIR_RATIO AS BZS FROM ( "
			+ "SELECT A.TIMESTAMP " 
			+ ", SUM(CASE WHEN B.DATA_TYPE = 'pa_flow_a' OR B.DATA_TYPE = 'pa_flow_b' OR B.DATA_TYPE = 'pa_flow_c' OR B.DATA_TYPE = 'pa_flow_d' OR B.DATA_TYPE = 'pa_flow_e' OR B.DATA_TYPE = 'pa_flow_f' OR B.DATA_TYPE = 'pa_flow_g' THEN A.TAG_VAL END) AS PA_FLOW "
			+ ", SUM(CASE WHEN B.DATA_TYPE = 'sa_flow_left' OR B.DATA_TYPE = 'sa_flow_right' THEN A.TAG_VAL END) AS SA_FLOW "
			+ ", SUM(CASE WHEN B.DATA_TYPE = 'ofa_flow_left' OR B.DATA_TYPE = 'ofa_flow_right' OR B.DATA_TYPE = 'ofa_flow_front'  OR B.DATA_TYPE = 'ofa_flow_rear' THEN A.TAG_VAL END) AS OFA_FLOW "
			+ "FROM TB_TREND_NAV_DATA A, TB_NAV_TREND_DATA_CONF B "
			+ "WHERE A.TIMESTAMP IN "
			+ "( "
			+ " SELECT TIMESTAMP "
			+ "	FROM TB_TREND_NAV_DATA "
			+ "	WHERE TIMESTAMP >= :startDate AND TIMESTAMP <= :endDate "
			+ "	AND TAG_NM = :defaultTagNm "
			+ "	AND MOD(HOUR(TIMESTAMP) * 3600 + MINUTE(TIMESTAMP) * 60 + SECOND(TIMESTAMP), :modSec) = 0 "
			+ ") AND A.TAG_NM = B.TAG_NM "
			+ "AND B.DATA_TYPE IN ('pa_flow_a','pa_flow_b','pa_flow_c','pa_flow_d','pa_flow_e','pa_flow_f','pa_flow_g','sa_flow_left','sa_flow_right','ofa_flow_left','ofa_flow_right','ofa_flow_front','ofa_flow_rear') "
			+ "GROUP BY A.TIMESTAMP "			
			+ ") A, TB_NAV_SETTING B ORDER BY A.TIMESTAMP DESC "
			, nativeQuery = true)
	List<Object[]> getNavigatorTrendBZS(@Param("defaultTagNm") String defaultTagNm, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("modSec") int modSec);

}