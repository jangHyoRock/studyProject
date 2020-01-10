package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NavTubeWWTempConfEntity;
import dhi.optimizer.model.db.NavTubeWWTempConfIdEntity;

@Repository
public interface NavTubeWWTempConfRepository extends JpaRepository<NavTubeWWTempConfEntity, NavTubeWWTempConfIdEntity> {
	
	@Query(value = "SELECT A.DIRECTION, A.TUBE_NO, A.TAG_VAL, CASE WHEN A.TAG_VAL IS NOT NULL THEN A.TAG_VAL * 100 / B.MAX_TAG_VAL END "
			+ "FROM "
			+ "( "		
			+ " SELECT A.DIRECTION, A.TUBE_NO, B.TAG_VAL "
			+ " FROM TB_NAV_TUBE_WW_TEMP_CONF A LEFT OUTER JOIN V_LAST_OP_DATA_INPUT B "
			+ " ON A.TAG_ID = B.TAG_ID "
			+ " WHERE A.TUBE_TYPE = ?1 "
			+ ") A, "
			+ "("
			+ " SELECT MAX(TAG_VAL) AS MAX_TAG_VAL "
			+ " FROM V_LAST_OP_DATA_INPUT "
			+ " WHERE TAG_ID IN (SELECT TAG_ID FROM TB_NAV_TUBE_WW_TEMP_CONF WHERE TUBE_TYPE IN ('Spiral', 'Vertical')) "
			+ ") B ORDER BY A.DIRECTION, A.TUBE_NO ", nativeQuery = true)
	List<Object[]> getNavTubeWWTempList(String tubeType);
}
