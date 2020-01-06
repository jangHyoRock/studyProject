package dhi.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dhi.common.model.db.ConfigInfEntity;
import dhi.common.model.db.ConfigInfIdEntity;

/*
 * JAP user repository.
 */
@Repository
public interface ConfigInfRepository extends JpaRepository<ConfigInfEntity, ConfigInfIdEntity> {

	@Query(value = "SELECT * FROM TB_CONFIG_INF WHERE P_UNIT_ID = ?1 AND CONF_TYPE=?2 ORDER BY CONF_ORDER ASC", nativeQuery = true)
	List<ConfigInfEntity> findByConfTypeOrderByConfOrderAscNativeQuery(String plantUnitId, String configType);
	
	@Query(value = "SELECT CATEGORY_NM FROM TB_CONFIG_INF WHERE P_UNIT_ID = ?1 AND CONF_TYPE=?2 GROUP BY CATEGORY_NM ORDER BY MIN(CONF_ORDER) ASC", nativeQuery = true)
	List<String> findByConfTypeOrderByConfOrderAscDistinctCategoryNmNativeQuery(String plantUnitId, String configType);
	
	@Modifying
	@Query(value = "UPDATE TB_CONFIG_INF SET CONF_VAL = ?4, USER_ID = ?5, TIMESTAMP=NOW() WHERE P_UNIT_ID = ?1 AND CONF_ID=?2 AND CONF_TYPE=?3", nativeQuery = true)
	void updateConfigValueNativeQuery(String plantUnitId, String configId, String ConfigType, double configValue, String userId);
	
	@Query(value = "SELECT CONF_ID, CONF_VAL FROM TB_CONFIG_INF WHERE UPPER(P_UNIT_ID) = UPPER(:pUnitId) AND UPPER(CONF_TYPE) = UPPER(:confType)",nativeQuery = true)
	List<Object[]> getAllConfData(@Param("pUnitId")String pUnitId, @Param("confType")String confType);
	
	@Query(value = "SELECT P_UNIT_ID, CONF_ID, CONF_VAL FROM TB_CONFIG_INF WHERE UPPER(CONF_TYPE) = UPPER(:confType)",nativeQuery = true)
	List<Object[]> getAllConfData(@Param("confType")String confType);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_CONFIG_INF A SET A.CONF_VAL = B.TAG_VAL "
			+ "FROM TB_CONFIG_INF A, V_LAST_COMMON_DATA_INPUT B "
			+ "WHERE A.CONF_TYPE = 'Coal' "
			+ "AND A.P_UNIT_ID = (SELECT P_UNIT_ID FROM TB_CONFIG_CHK WHERE CHK_ID = 'coal_auto_check' AND CHK_VAL = TRUE) "
			+ "AND A.P_UNIT_ID = B.P_UNIT_ID "
			+ "AND A.TAG_ID = B.TAG_ID ", nativeQuery = true)
	void updateAutoCoalTransferNativeQuery();
}