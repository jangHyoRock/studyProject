package dhi.optimizer.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.model.db.NoticeHistoryEntity;

public interface NoticeHistoryRepository extends JpaRepository<NoticeHistoryEntity, Date> {
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO TB_NOTICE_HIST(TIMESTAMP, NAME, TYPE, STATUS, DESCRIPTION) "
			+ "VALUES(NOW(), ?1, ?2, ?3, ?4)", nativeQuery = true)
	void insertAll(String name, String type, String status, String description);
	
	int countByStatus(String status);
	
	@Query(value = "SELECT TIMESTAMP, NAME "
			+ ", CASE WHEN TYPE = 'S' THEN 'SYSTEM' ELSE CASE WHEN TYPE = 'D' THEN 'DCS' ELSE '' END END AS TYPE "
			+ ", CASE WHEN STATUS = 'A' THEN 'Alarm' ELSE CASE WHEN STATUS = 'E' THEN 'Event' ELSE '' END END AS STATUS "
			+ ", DESCRIPTION "
			+ "FROM TB_NOTICE_HIST "
			+ "WHERE TIMESTAMP >= ?1 AND TIMESTAMP <= ?2 "
			+ "ORDER BY TIMESTAMP DESC LIMIT ?4 OFFSET ?3 ", nativeQuery = true)
	List<NoticeHistoryEntity> findByTimestampRange(Date startDate, Date endDate, int startIndex, int pageSize);
}