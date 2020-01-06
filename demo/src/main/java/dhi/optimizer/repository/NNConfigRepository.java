package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NNConfigEntity;

/*
 * JAP control repository.
 */
@Repository
public interface NNConfigRepository extends JpaRepository<NNConfigEntity, Integer> {
	
	NNConfigEntity findByConfId(int confId);
}

