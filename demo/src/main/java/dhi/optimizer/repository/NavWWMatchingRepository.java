package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NavWWMatchingEntity;
import dhi.optimizer.model.db.NavWWMatchingIdEntity;

@Repository
public interface NavWWMatchingRepository extends JpaRepository<NavWWMatchingEntity, NavWWMatchingIdEntity> {
		
	@Query(value = "SELECT * FROM ( "
			+ "SELECT A.P_1, A.P_2, A.P_3, A.P_4, 'Deviation' AS TYPE"
			+ ", IFNULL(A.C_1, 'H') || '_' || IFNULL(B.C_1, 'H') AS C_1"
			+ ", IFNULL(A.C_2, 'H') || '_' || IFNULL(B.C_2, 'H') AS C_2"
			+ ", IFNULL(A.C_3, 'H') || '_' || IFNULL(B.C_3, 'H') AS C_3"
			+ ", IFNULL(A.C_4, 'H') || '_' || IFNULL(B.C_4, 'H') AS C_4"
			+ ", A.MATCHING_ORDER, A.TIMESTAMP "
			+ "FROM TB_NAV_WW_MATCHING A, TB_NAV_WW_MATCHING B "
			+ "WHERE A.P_1 = B.P_1 "
			+ "AND A.P_2 = B.P_2 "
			+ "AND A.P_3 = B.P_3 "
			+ "AND A.P_4 = B.P_4 "
			+ "AND A.TYPE = 'aa' "
			+ "AND B.TYPE = 'ab_fg' "
			+ "UNION ALL "
			+ "SELECT P_1, P_2, P_3, P_4, 'HighLow', IFNULL(C_1, 'H'), IFNULL(C_2, 'H'), IFNULL(C_3, 'H'), IFNULL(C_4, 'H'), MATCHING_ORDER, TIMESTAMP "
			+ "FROM TB_NAV_WW_MATCHING  "
			+ "WHERE TYPE = 'gg' "
			+ ") ORDER BY TYPE, MATCHING_ORDER", nativeQuery = true)
	List<NavWWMatchingEntity> findAllOrderByTypeAscOrderByMatchingOrderAsc();
	
	@Modifying
	@Query(value = "UPDATE TB_NAV_WW_MATCHING SET C_1 = ?6, C_2 = ?7, C_3 = ?8, C_4 = ?9, TIMESTAMP = NOW() "
			+ "WHERE P_1 = ?1 AND P_2 = ?2 AND P_3 = ?3 AND P_4 = ?4 AND TYPE = ?5 ", nativeQuery = true)
	void updateByDeviation(String p1, String p2, String p3, String p4, String type, String c1, String c2, String c3, String c4);
	
	@Modifying
	@Query(value = "UPDATE TB_NAV_WW_MATCHING SET C_1 = ?4, C_2 = ?5, C_3 = ?6, C_4 = ?7, TIMESTAMP = NOW() "
			+ "WHERE P_1 = ?1 AND P_2 = ?2 AND TYPE = ?3 ", nativeQuery = true)
	void updateByHighLow(String p1, String p2, String type, String c1, String c2, String c3, String c4);
}