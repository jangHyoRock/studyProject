package dhi.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.ConfigChkEntity;
import dhi.common.model.db.ConfigChkIdEntity;
/*
 * JAP user repository.
 */
@Repository
public interface ConfigChkRepository extends JpaRepository<ConfigChkEntity, ConfigChkIdEntity> {

	@Query(value = "SELECT * FROM TB_CONFIG_CHK WHERE P_UNIT_ID = ?1 AND CHK_ID = ?2", nativeQuery = true)
	ConfigChkEntity findByPlantUnitIdAndChkIdNativeQuery(String plantUnitId, String chkId);
	
	@Modifying
	@Query(value = "UPDATE TB_CONFIG_CHK SET CHK_VAL = ?3, TIMESTAMP=NOW() WHERE P_UNIT_ID =?1 AND CHK_ID=?2", nativeQuery = true)
	void updateConfigCheckNativeQuery(String plantUnitId, String chkId, boolean isCheck);
}
