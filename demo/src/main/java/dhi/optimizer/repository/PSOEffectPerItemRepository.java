package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.PSOEffectPerItemEntity;

/*
 * JAP control repository.
 */
@Repository
public interface PSOEffectPerItemRepository extends JpaRepository<PSOEffectPerItemEntity, String> {
	
	List<PSOEffectPerItemEntity> findAll();
}