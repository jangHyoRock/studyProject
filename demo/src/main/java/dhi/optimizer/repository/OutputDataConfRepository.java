package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.OutputDataConfEntity;

/*
 * JAP control repository.
 */
@Repository
public interface OutputDataConfRepository extends JpaRepository<OutputDataConfEntity, Integer> {
	
	OutputDataConfEntity findByConfId(int confId);
}
