package com.example.app.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.common.model.db.OverviewEntity;

@Repository
public interface OverviewRepository extends JpaRepository<OverviewEntity, String> {
	
	/*
	 * overview - leftMenu
	 */
//	 @Query(value =
//	            "	SELECT P_UNIT_ID " +
//	            "        , P_UNIT_NM " +
//	            "        , P_UNIT_ORDER " +
//	            "        , DESCRIPTION " +
//	            "        , TURBINE_TYPE " +
//	            "	FROM   DHI_DVS.TB_DVS_PLANT_UNIT " +
//	            "	ORDER BY P_UNIT_ORDER " +
//	            "", nativeQuery = true)
//	    List<Map<String, Object>> getLeftMenu();
//	
}
