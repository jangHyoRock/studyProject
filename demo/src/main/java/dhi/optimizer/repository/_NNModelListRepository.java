package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db._NNModelListEntity;
import dhi.optimizer.model.db._NNModelListIdEntity;

/*
 * JAP control repository.
 */
@Repository
public interface _NNModelListRepository extends JpaRepository<_NNModelListEntity, _NNModelListIdEntity> {
	
	List<_NNModelListEntity> findTop3ById_ModelIdAndTrainStatusOrderById_TimestampDesc(String modelId, boolean trainStatus);
	
	_NNModelListEntity findTop1ById_ModelIdOrderById_TimestampDesc(String modelId);
	
	@Modifying
	@Query(value = "UPDATE DD_TB_NN_MODEL_LIST SET TRAIN_STATUS = false, TIMESTAMP = NOW() WHERE MODEL_ID = ?1 AND TRAIN_STATUS = true ", nativeQuery = true)
	void updateTrainStatusFalse(String modelId);
}
