package dhi.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.CommonControlEntity;
import dhi.common.model.db.CommonControlIdEntity;

@Repository
public interface CommonControlRepository extends JpaRepository<CommonControlEntity, CommonControlIdEntity> {
}