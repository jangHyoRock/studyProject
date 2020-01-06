package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NavResultEntity;
import dhi.optimizer.model.db.NavResultIdEntity;

@Repository
public interface NavResultRepository extends JpaRepository<NavResultEntity, NavResultIdEntity> {
	
	@Modifying
	@Query(value = "UPDATE TB_NAV_RESULT SET YAW = ?3, TIMESTAMP = NOW() WHERE GROUP_ID = ?1 AND ITEM_ID = ?2 ", nativeQuery = true)
	void updateByGroupIdAndItemIdSetYaw(String group, String itemId, String yaw);
}