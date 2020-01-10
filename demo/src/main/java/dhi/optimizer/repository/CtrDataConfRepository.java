package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.CtrDataConfEntity;

@Repository
public interface CtrDataConfRepository extends JpaRepository<CtrDataConfEntity, String> {
}