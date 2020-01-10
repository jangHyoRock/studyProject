package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db._NNTrainDataConfEntity;
import dhi.optimizer.model.db._NNTrainDataConfIdEntity;
/*
 * JAP control repository.
 */
@Repository
public interface _NNTrainDataConfRepository extends JpaRepository<_NNTrainDataConfEntity, _NNTrainDataConfIdEntity> {
	
	List<_NNTrainDataConfEntity> findById_ModelIdAndIoTypeOrderByTagIndexAsc(String modelId, String ioType);
	
}
