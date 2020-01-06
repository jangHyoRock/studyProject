package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db._NNModelBaseListEntity;
import dhi.optimizer.model.db._NNModelBaseListIdEntity;

/*
 * JAP control repository.
 */
@Repository
public interface _NNModelBaseListRepository extends JpaRepository<_NNModelBaseListEntity, _NNModelBaseListIdEntity> {
	
	List<_NNModelBaseListEntity> findById_ModelIdAndTrainStatus(String modelId, Boolean trainStatus);
	
	@Modifying
	@Query(value = "UPDATE DD_TB_NN_MODEL_BASE_LIST SET TRAIN_STATUS = false, TIMESTAMP = NOW() WHERE TRAIN_STATUS = true AND MODEL_ID = ?1 ", nativeQuery = true)
	void updateTrainStatusFalse(String modelId);
}