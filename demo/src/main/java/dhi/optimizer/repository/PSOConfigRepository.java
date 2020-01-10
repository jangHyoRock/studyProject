package dhi.optimizer.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.PSOConfigEntity;

/*
 * JAP control repository.
 */
@Repository
public interface PSOConfigRepository extends JpaRepository<PSOConfigEntity, Integer> {
	
	PSOConfigEntity findByConfId(int confId);
}