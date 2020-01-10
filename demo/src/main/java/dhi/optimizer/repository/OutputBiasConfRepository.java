package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.OutputBiasConfEntity;

/*
 * JAP control repository.
 */
@Repository
public interface OutputBiasConfRepository extends JpaRepository<OutputBiasConfEntity, Integer> {
	
	OutputBiasConfEntity findByConfId(int confId);
}
