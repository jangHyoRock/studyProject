package dhi.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.PlantUnitEntity;
import dhi.common.model.db.PlantUnitIdEntity;

/*
 * JAP user repository.
 */
@Repository
public interface PlantUnitRepository extends JpaRepository<PlantUnitEntity, PlantUnitIdEntity> {

	@Query(value = "SELECT A.P_UNIT_ID, B.P_UNIT_NM, CASE WHEN C.P_UNIT_INTERNAL_IP IS NULL THEN A.P_UNIT_IP ELSE C.P_UNIT_INTERNAL_IP END P_UNIT_IP, A.P_UNIT_PORT "
			+ "FROM TB_PLANT_UNIT A INNER JOIN TB_UNIT B "
			+ "ON A.P_UNIT_ID = B.P_UNIT_ID "
			+ "AND (A.P_UNIT_ID, A.SYS_ID) IN (SELECT P_UNIT_ID, SYS_ID FROM TB_COMPANY_PLANT_UNIT WHERE SYS_ID = ?1 AND PLANT_ID = ?2) "
			+ "LEFT OUTER JOIN TB_PLANT_UNIT_FOR_INTERNAL C "
			+ "ON A.SYS_ID = C.SYS_ID "
			+ "AND B.P_UNIT_ID = C.P_UNIT_ID "
			+ "AND C.REQUEST_IP = ?3 "
			+ "ORDER BY A.P_UNIT_ORDER ASC ", nativeQuery = true)
	List<Object[]> findByPlantUnitIdInCompanyPlantUnitBySystemIdAndPlantIdNativeQuery(String systemId, String plantId, String requestHost);
	
	@Query(value = "SELECT B.P_UNIT_ID, CASE WHEN C.P_UNIT_INTERNAL_IP IS NULL THEN B.P_UNIT_IP ELSE C.P_UNIT_INTERNAL_IP END P_UNIT_IP, B.P_UNIT_PORT, A.SYS_ID "
			+ "FROM TB_COMPANY_PLANT_UNIT A INNER JOIN TB_PLANT_UNIT B "
			+ "ON A.SYS_ID = B.SYS_ID "
			+ "AND A.P_UNIT_ID = B.P_UNIT_ID LEFT OUTER JOIN TB_PLANT_UNIT_FOR_INTERNAL C "
			+ "ON B.SYS_ID = C.SYS_ID "
			+ "AND B.P_UNIT_ID = C.P_UNIT_ID "
			+ "AND C.REQUEST_IP = ?1 "
			+ "ORDER BY B.P_UNIT_ORDER ASC ", nativeQuery = true)
	List<Object[]> findByAllPlantIdWithIpWithSysIdNativeQuery(String requestHost);
	
	@Query(value = "SELECT A.COMPANY_ID, A.P_UNIT_ID, A.PLANT_ID, A.SYS_ID, B.P_UNIT_NO, B.P_UNIT_ORDER, B.P_UNIT_IP, B.DESCRIPTION, B.P_UNIT_PORT "
			+ "FROM TB_COMPANY_PLANT_UNIT A INNER JOIN TB_PLANT_UNIT B ON A.SYS_ID = B.SYS_ID AND A.P_UNIT_ID = B.P_UNIT_ID "
			+ "ORDER BY A.COMPANY_ID, A.PLANT_ID, A.SYS_ID, A.P_UNIT_ID", nativeQuery = true)
	List<Object[]> findAllCompanyAndPlantInfoNativeQuery();
	
	@Query(value = "SELECT * FROM TB_COMPANY", nativeQuery = true)
	List<Object[]> findAllCompanyInfoNativeQuery();
	
	@Query(value = "SELECT * FROM TB_PLANT", nativeQuery = true)
	List<Object[]> findAllPlantInfoNativeQuery();
	
	@Query(value = "SELECT * FROM TB_UNIT ORDER BY P_UNIT_ID", nativeQuery = true)
	List<Object[]> findAllUnitInfoNativeQuery();
	
	@Query(value = "SELECT * FROM TB_PLANT_UNIT WHERE SYS_ID = ?1 ORDER BY P_UNIT_ORDER ASC ", nativeQuery = true)
	List<PlantUnitEntity> findBySysIdOrderByPlantUnitOrderAscNativeQuery(String systemId);
	
	@Query(value = "SELECT DISTINCT A.P_UNIT_ID, B.P_UNIT_NM FROM TB_PLANT_UNIT A, TB_UNIT B WHERE A.P_UNIT_ID = B.P_UNIT_ID ORDER BY A.P_UNIT_ID", nativeQuery = true)
	List<Object[]> findPlantUnitIdListNativeQuery();
	
	@Modifying
	@Query(value = "DELETE FROM TB_COMPANY_PLANT_UNIT", nativeQuery = true)
	void deleteAllCompanyPlantUnit();
	
	@Modifying
	@Query(value = "DELETE FROM TB_PLANT_UNIT", nativeQuery = true)
	void deleteAllPlantUnit();
	
	@Modifying
	@Query(value = "DELETE FROM TB_COMPANY", nativeQuery = true)
	void deleteAllCompany();
	
	@Modifying
	@Query(value = "DELETE FROM TB_UNIT", nativeQuery = true)
	void deleteAllUnit();
	
	@Modifying
	@Query(value = "DELETE FROM TB_PLANT", nativeQuery = true)
	void deleteAllPlant();
	
	@Modifying
	@Query(value = "INSERT INTO TB_COMPANY_PLANT_UNIT VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
	void insertAllCompanyPlantUnit(String companyId, String pUnitId, String plantId, String sysId);
	
	@Modifying
	@Query(value = "INSERT INTO TB_PLANT_UNIT VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
	void insertAllPlantUnit(String pUnitId, String pUnitNo, int pUnitOrder, String pUnitIp, String description, String sysId, String pUnitPort);
	
	@Modifying
	@Query(value = "INSERT INTO TB_COMPANY VALUES(?1, ?2, ?3, ?4)", nativeQuery = true)
	void insertAllCompany(String companyId, String companyNm, int companyOrder, String description);
	
	@Modifying
	@Query(value = "INSERT INTO TB_UNIT VALUES(?1, ?2, ?3, ?4)", nativeQuery = true)
	void insertAllUnit(String pUnitId, String pUnitNm, int pUnitOrder, String description);
	
	@Modifying
	@Query(value = "INSERT INTO TB_PLANT VALUES(?1, ?2, ?3, ?4)", nativeQuery = true)
	void insertAllPlant(String plantId, String plantNm, int plantOrder, String description);	
}