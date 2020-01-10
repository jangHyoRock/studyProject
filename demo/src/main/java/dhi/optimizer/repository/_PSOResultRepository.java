package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db._PSOResultEntity;
import dhi.optimizer.model.db._PSOResultIdEntity;

/*
 * JAP control repository.
 */
@Repository
public interface _PSOResultRepository extends JpaRepository<_PSOResultEntity, _PSOResultIdEntity> {
}
