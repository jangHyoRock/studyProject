package dhi.optimizer.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NNTrainOutEntity;

@Repository
public interface NNTrainOutRepository extends JpaRepository<NNTrainOutEntity, String> {

	@Query(value = "SELECT B.TIMESTAMP, STRING_AGG(B.TAG_VAL,','ORDER BY A.TAG_INDEX ASC) AS DATAS FROM DD_TB_NN_TRAIN_DATA_CONF A, TB_NN_TRAIN_OUT B "
			+ "WHERE A.MODEL_ID = ?1 "
			+ "AND B.TIMESTAMP >= ADD_SECONDS(NOW(), -?2) "
			+ "AND A.TAG_NM = B.TAG_NM "
			+ "GROUP BY B.TIMESTAMP "
			+ "ORDER BY B.TIMESTAMP DESC ", nativeQuery = true)
	List<Object[]> getNNTrainOutputDataNativeQuery(String modelId, int lastTimeSec);
	
	@Query(value = "SELECT B.MODEL_ID, C.TIMESTAMP, STRING_AGG(C.TAG_VAL,','ORDER BY B.TAG_INDEX ASC) AS DATAS FROM DD_TB_NN_MODEL_CONF A, DD_TB_NN_TRAIN_DATA_CONF B, TB_NN_TRAIN_OUT C "
			+ "WHERE A.MODEL_ID = B.MODEL_ID "
			+ "AND B.TAG_NM = C.TAG_NM "
			+ "AND B.IO_TYPE = 'Output' "
			+ "AND C.TIMESTAMP >= ADD_SECONDS(NOW(), -A.VALID_DATA_TIME) "
			+ "GROUP BY C.TIMESTAMP, B.MODEL_ID "
			+ "ORDER BY B.MODEL_ID ASC, C.TIMESTAMP DESC ", nativeQuery = true)
	List<Object[]> getNNVaildOutputDataNativeQuery();
	
	@Query(value = "SELECT B.TIMESTAMP, STRING_AGG(B.TAG_VAL,','ORDER BY A.TAG_NO ASC) AS DATAS FROM TB_NN_TRAIN_DATA_CONF A, TB_NN_TRAIN_OUT B "
			+ "WHERE B.TIMESTAMP >= ADD_SECONDS(NOW(), -?1) "
			+ "AND A.TAG_NM = B.TAG_NM "
			+ "GROUP BY B.TIMESTAMP "
			+ "ORDER BY B.TIMESTAMP DESC ", nativeQuery = true)
	List<Object[]> findByTimestampLastNNTrainOutputDataNativeQuery(int lastTimeSec);
	
	@Query(value = "SELECT TIMESTAMP, TAG_VAL FROM TB_NN_TRAIN_OUT "
			+ "WHERE TIMESTAMP >= ?2 AND TIMESTAMP <= ?3 "
			+ "AND TAG_ID = ?1 "
			+ "ORDER BY TIMESTAMP ASC ", nativeQuery = true)
	List<Object[]> findByTagIdNNTrainOutputDataNativeQuery(String tagId, Date startDate, Date endDate);
	
	@Query(value = "SELECT B.TIMESTAMP || ',' || STRING_AGG(B.TAG_VAL,','ORDER BY A.TAG_NO ASC) AS DATAS FROM TB_NN_TRAIN_DATA_CONF A, TB_NN_TRAIN_OUT B "
			+ "WHERE B.TIMESTAMP >= ?1 AND B.TIMESTAMP <= ?2 "
			+ "AND A.TAG_NM = B.TAG_NM "		
			+ "GROUP BY B.TIMESTAMP "
			+ "ORDER BY B.TIMESTAMP ASC ", nativeQuery = true)
	List<String> getCSVDownLoadNNTrainOutputDataNativeQuery(Date startDate, Date endDate);
}
