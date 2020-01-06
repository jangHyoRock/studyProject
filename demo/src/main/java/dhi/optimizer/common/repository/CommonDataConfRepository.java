package dhi.optimizer.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.CommonDataConfEntity;
import dhi.optimizer.model.db.CommonDataConfIdEntity;

@Repository
public interface CommonDataConfRepository extends JpaRepository<CommonDataConfEntity, CommonDataConfIdEntity> {
}