package dhi.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.CompanyEntity;
/*
 * JAP user repository.
 */
@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
	
	@Query(value = " SELECT COMPANY_NM, PLANT_NM, PLANT_ID FROM ( " 
			+ "SELECT DISTINCT A.COMPANY_NM, C.PLANT_NM, C.PLANT_ID, A.COMPANY_ORDER, C.PLANT_ORDER  "
			+ "FROM TB_COMPANY A INNER JOIN TB_COMPANY_PLANT_UNIT B "
			+ "ON A.COMPANY_ID = B.COMPANY_ID AND B.SYS_ID = ?1 INNER JOIN TB_PLANT C "
			+ "ON B.PLANT_ID = C.PLANT_ID) "
			+ "ORDER BY COMPANY_ORDER, PLANT_ORDER ASC ", nativeQuery = true)
	List<Object[]> findDistinctCompanyToPlantNativeQuery(String systemId);
}