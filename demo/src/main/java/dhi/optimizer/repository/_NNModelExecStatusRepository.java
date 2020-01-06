package dhi.optimizer.repository;

import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db._NNModelExecStatusEntity;

/*
 * JAP control repository.
 */
@Repository
public interface _NNModelExecStatusRepository extends JpaRepository<_NNModelExecStatusEntity, String> {
	
	_NNModelExecStatusEntity findByModelId(String modelId);
	
	@Query(value = "SELECT CASE WHEN SUM(CASE WHEN END_YN = TRUE THEN 1 ELSE 0 END) = COUNT(MODEL_ID) THEN 1 ELSE 0 END AS COMPLETE "
			+ "FROM DD_TB_NN_MODEL_EXEC_STATUS ", nativeQuery = true)	
	int getNNModelExecutorAllCompleteStatus();
	
	@Query(value = "SELECT A.MODEL_ID, A.MODEL_NM, B.EXEC_PRIORITY  "
			+ "FROM DD_TB_NN_MODEL A, DD_TB_NN_MODEL_EXEC_STATUS B "
			+ "WHERE A.MODEL_ID = B.MODEL_ID "
			+ "AND A.MODEL_TYPE = 'MILL' "
			+ "AND B.EXEC_PRIORITY = 0 ", nativeQuery = true)
	List<Object[]> getNNModelMillExecuterStatusFirstPriorityNativeQuery();
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE DD_TB_NN_MODEL_EXEC_STATUS SET END_YN = false ", nativeQuery = true)
	void updateNNModelExecutorAllEndInitialize();
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE DD_TB_NN_MODEL_EXEC_STATUS "
			+ "SET MODEL_STATUS = ?3, START_DT = ?2, END_YN = FALSE "
			+ "WHERE MODEL_ID = ?1 ", nativeQuery = true)
	void updateNNModelExecutorStartStatus(String modelId,  Date startDT, int status);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE DD_TB_NN_MODEL_EXEC_STATUS "
			+ "SET MODEL_STATUS = ?3, END_DT = ?2, END_YN = TRUE "
			+ "WHERE MODEL_ID = ?1 ", nativeQuery = true)
	void updateNNModelExecutorEndStatus(String modelId, Date endDT, int status);
	
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE DD_TB_NN_MODEL_EXEC_STATUS "
			+ "SET EXEC_PRIORITY = CASE WHEN EXEC_PRIORITY = 0 "
			+ "THEN (SELECT MAX(EXEC_PRIORITY) FROM DD_TB_NN_MODEL_EXEC_STATUS  WHERE MODEL_TYPE = 'MILL') "
			+ "ELSE EXEC_PRIORITY - 1 END "
			+ "WHERE MODEL_TYPE = 'MILL' ", nativeQuery = true)
	void updateNNModelMillExecuterPriorityChange();
}