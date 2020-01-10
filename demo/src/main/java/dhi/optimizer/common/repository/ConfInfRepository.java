package dhi.optimizer.common.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.ConfigInfEntity;
import dhi.optimizer.model.db.ConfigInfIdEntity;

@Repository
public interface ConfInfRepository extends JpaRepository<ConfigInfEntity, ConfigInfIdEntity>{

	@Query(value = "SELECT CONF_ID, CONF_NM, CONF_VAL, CONF_UNIT FROM TB_CONFIG_INF WHERE UPPER(P_UNIT_ID) = UPPER(:pUnitId) AND UPPER(CONF_TYPE) = UPPER(:confType)", nativeQuery = true)
	List<Object[]> getAllConfData(@Param("pUnitId") String pUnitId, @Param("confType") String confType);
	
	@Query(value = "SELECT CONF_ID, CONF_NM, CONF_VAL, CONF_UNIT, CONF_TYPE FROM TB_CONFIG_INF WHERE UPPER(P_UNIT_ID) = UPPER(:pUnitId) AND CONF_TYPE IN :confTypes ", nativeQuery = true)
	List<Object[]> getAllConfData(@Param("pUnitId") String pUnitId, @Param("confTypes") Collection<String> confTypes);
}