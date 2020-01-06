package dhi.optimizer.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db._NNModelExecStatusHistEntity;
import dhi.optimizer.model.db._NNModelExecStatusHistIdEntity;

/*
 * JAP control repository.
 */
@Repository
public interface _NNModelExecStatusHistRepository extends JpaRepository<_NNModelExecStatusHistEntity, _NNModelExecStatusHistIdEntity> {
		
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO DD_TB_NN_MODEL_EXEC_STATUS_HIST VALUES(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
	void insertAll(Date timestamp, String modelId, String modelType, int status, String error);
}