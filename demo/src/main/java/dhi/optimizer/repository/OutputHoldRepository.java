package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.OutputHoldEntity;

/*
 * JAP control repository.
 */
@Repository
public interface OutputHoldRepository extends JpaRepository<OutputHoldEntity, String> {
	
	OutputHoldEntity findByHoldId(String holdId);
	
}