package dhi.optimizer.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.ReportEntity;
import dhi.optimizer.model.db.ReportIdEntity;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, ReportIdEntity> {

	@Query(value = "SELECT R_ID, SUM(R_SUM) AS REPORT_SUM, SUM(R_COUNT) AS REPORT_CNT FROM TB_REPORT "
			+ "WHERE TIMESTAMP >= ?1 AND TIMESTAMP <= ?2 "
			+ "GROUP BY R_ID ", nativeQuery = true)
	List<Object[]> getReportByPeriod(Date startDate, Date endDate);
		
	@Query(value = "SELECT COUNT(*) FROM TB_REPORT WHERE TIMESTAMP >= ?1 AND TIMESTAMP <= ?2 ", nativeQuery = true)
	int getReportCountByPeriod(Date startDate, Date endDate);	
}
