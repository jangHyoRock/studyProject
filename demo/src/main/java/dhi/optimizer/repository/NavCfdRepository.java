package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NavCfdEntity;
import dhi.optimizer.model.db.NavCfdIdEntity;

/*
 * JAP control repository.
 */
@Repository
public interface NavCfdRepository extends JpaRepository<NavCfdEntity, NavCfdIdEntity> {

	/*SELECT *
	FROM  TB_NAV_CFD
	WHERE TO_INTEGER(SUBSTR(cfd_id, 0,3)) >= 660 - (660 * 0.1) AND TO_INTEGER(SUBSTR(cfd_id, 0,3)) <= 660 + (660 * 0.1)
	AND TO_INTEGER(SUBSTR(cfd_id, 5,3)) >= 126 - (126 * 0.1) AND TO_INTEGER(SUBSTR(cfd_id, 5,3)) <= 126 + (126 * 0.1)
	AND TO_INTEGER(SUBSTR(cfd_id, 9,2)) >= 15 - (15 * 0.1) AND TO_INTEGER(SUBSTR(cfd_id, 9,2)) <= 15 + (15 * 0.1)*/

	@Query(value = "SELECT * FROM TB_NAV_CFD "
			+ "WHERE TO_INTEGER(SUBSTR_BEFORE(cfd_id,'.')) >= ?1 - (?1 * ?4) AND TO_INTEGER(SUBSTR_BEFORE(cfd_id,'.')) <= ?1 + (?1 * ?4) "
			+ "AND TO_INTEGER(SUBSTR_BEFORE(SUBSTR_AFTER(cfd_id,'.'),'.')) >= ?2 - (?2 * ?4) AND TO_INTEGER(SUBSTR_BEFORE(SUBSTR_AFTER(cfd_id,'.'),'.')) <= ?2 + (?2 * ?4) "
			+ "AND TO_INTEGER(SUBSTR_AFTER(SUBSTR_AFTER(cfd_id,'.'),'.')) >= ?3 - (?3 * ?4) AND TO_INTEGER(SUBSTR_AFTER(SUBSTR_AFTER(cfd_id,'.'),'.')) <= ?3 + (?3 * ?4) ", nativeQuery = true)
	NavCfdEntity findByLoadAndMillAndUOFAYawRangeNativeQuery(int load, int mill, int uofaYaw, double range);
	

	/* 사용안함.
	 * @Query(value = "SELECT * FROM TB_NAV_CFD "
			+ "WHERE TO_INTEGER(SUBSTR_BEFORE(cfd_id,'.')) >= ?1 - (?1 * ?4) AND TO_INTEGER(SUBSTR_BEFORE(cfd_id,'.')) <= ?1 + (?1 * ?4) "
			+ "AND SUBSTR_BEFORE(SUBSTR_AFTER(cfd_id,'.'),'.') = ?2 "
			+ "AND SUBSTR_AFTER(SUBSTR_AFTER(cfd_id,'.'),'.') = ?3 "
			+ "AND ROW = 'RH' "
			+ "AND COL = ?5 ", nativeQuery = true)
	NavCfdEntity getSprayBalancingCFDNativeQuery2(int load, String mill, String uofaYaw, double range, String rhDirection);*/
	
	@Query(value = "SELECT * FROM TB_NAV_CFD "
			+ "WHERE TO_INTEGER(SUBSTR_BEFORE(cfd_id,'.')) >= ?1 - (?1 * ?4) AND TO_INTEGER(SUBSTR_BEFORE(cfd_id,'.')) <= ?1 + (?1 * ?4) "
			+ "AND SUBSTR_BEFORE(SUBSTR_AFTER(cfd_id,'.'),'.') = ?2 "
			+ "AND SUBSTR_AFTER(SUBSTR_AFTER(cfd_id,'.'),'.') = ?3 "
			+ "AND ROW = 'RH' ", nativeQuery = true)
	List<NavCfdEntity> getSprayBalancingCFDNativeQuery(int load, String mill, String uofaYaw, double range);
}
