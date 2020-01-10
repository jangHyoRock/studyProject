package dhi.optimizer.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NNTrainInEntity;

@Repository
public interface NNTrainInRepository extends JpaRepository<NNTrainInEntity, String> {
	
	@Query(value = "SELECT B.TIMESTAMP, STRING_AGG(B.TAG_VAL,','ORDER BY A.TAG_INDEX ASC) AS DATAS FROM DD_TB_NN_TRAIN_DATA_CONF A, TB_NN_TRAIN_IN B "
			+ "WHERE A.MODEL_ID = ?1 "			
			+ "AND B.TIMESTAMP >= ADD_SECONDS(NOW(), -?2) "
			+ "AND A.TAG_NM = B.TAG_NM "
			+ "GROUP BY B.TIMESTAMP "
			+ "ORDER BY B.TIMESTAMP DESC ", nativeQuery = true)
	List<Object[]> getNNTrainInputDataNativeQuery(String modelId, int lastTimeSec);
	
	@Query(value = "SELECT B.MODEL_ID, C.TIMESTAMP, STRING_AGG(C.TAG_VAL,','ORDER BY B.TAG_INDEX ASC) AS DATAS FROM DD_TB_NN_MODEL_CONF A, DD_TB_NN_TRAIN_DATA_CONF B, TB_NN_TRAIN_IN C "
			+ "WHERE A.MODEL_ID = B.MODEL_ID "
			+ "AND B.TAG_NM = C.TAG_NM "
			+ "AND B.IO_TYPE = 'Input' "
			+ "AND C.TIMESTAMP >= ADD_SECONDS(NOW(), -A.VALID_DATA_TIME) "
			+ "GROUP BY C.TIMESTAMP, B.MODEL_ID "
			+ "ORDER BY B.MODEL_ID ASC, C.TIMESTAMP DESC ", nativeQuery = true)
	List<Object[]> getNNVaildInputDataNativeQuery();
	
	@Query(value = "SELECT B.TIMESTAMP, STRING_AGG(B.TAG_VAL,','ORDER BY A.TAG_NO ASC) AS DATAS FROM TB_NN_TRAIN_DATA_CONF A, TB_NN_TRAIN_IN B "
			+ "WHERE A.TAG_NM = B.TAG_NM "
			+ "AND B.TIMESTAMP >= ADD_SECONDS(NOW(), -?1) "
			+ "GROUP BY B.TIMESTAMP "
			+ "ORDER BY B.TIMESTAMP DESC ", nativeQuery = true)
	List<Object[]> findByTimestampLastNNTrainInputDataNativeQuery(int lastTimeSec);
	
	@Query(value = "SELECT TIMESTAMP, TAG_VAL FROM TB_NN_TRAIN_IN "
			+ "WHERE TIMESTAMP >= ?2 AND TIMESTAMP <= ?3 "
			+ "AND TAG_ID = ?1 "
			+ "ORDER BY TIMESTAMP ASC ", nativeQuery = true)
	List<Object[]> findByTagIdNNTrainInputDataNativeQuery(String tagId, Date startDate, Date endDate);
		
	@Query(value = "SELECT B.TIMESTAMP || ',' || STRING_AGG(B.TAG_VAL,','ORDER BY A.TAG_NO ASC) AS DATAS FROM TB_NN_TRAIN_DATA_CONF A, TB_NN_TRAIN_IN B "
			+ "WHERE A.TAG_NM = B.TAG_NM "
			+ "AND B.TIMESTAMP >= ?1 AND B.TIMESTAMP <= ?2 "
			+ "GROUP BY B.TIMESTAMP "
			+ "ORDER BY B.TIMESTAMP ASC ", nativeQuery = true)
	List<String> getCSVDownLoadNNTrainInputDataNativeQuery(Date startDate, Date endDate);
}
