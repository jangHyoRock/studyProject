package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NavSettingEntity;

@Repository
public interface NavSettingRepository extends JpaRepository<NavSettingEntity, Integer> {
	
	NavSettingEntity findBySettingId(int confId);
}
