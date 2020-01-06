package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db._NNModelConfEntity;

/*
 * JAP control repository.
 */
@Repository
public interface _NNModelConfRepository extends JpaRepository<_NNModelConfEntity, String> {
 
	_NNModelConfEntity findByModelId(String modelId);
	
	@Query(value = "SELECT A.*, B.* "
			+ "FROM DD_TB_NN_MODEL A, DD_TB_NN_MODEL_CONF B "
			+ "WHERE A.MODEL_ID = B.MODEL_ID "
			+ "AND A.MODEL_ID = ?1 ", nativeQuery = true)
	_NNModelConfEntity findByModelIdNativeQuery(String modelId);
	
	@Query(value = "SELECT A.*, B.* "
			+ "FROM DD_TB_NN_MODEL A, DD_TB_NN_MODEL_CONF B "
			+ "WHERE A.MODEL_ID = B.MODEL_ID ", nativeQuery = true)
	List<_NNModelConfEntity> findAllNativeQuery();
}
