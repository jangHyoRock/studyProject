package dhi.optimizer.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NNModelEntity;

/*
 * TODO : 다단 적용. 삭제예정
 * JAP control repository.
 */
@Repository
public interface NNModelRepository extends JpaRepository<NNModelEntity, Date> {
	
	List<NNModelEntity> findTop3ByTrainStatusOrderByTimestampDesc(boolean trainStatus);
	
	NNModelEntity findTop1ByOrderByTimestampDesc();	
	
	@Modifying
	@Query(value = "UPDATE TB_NN_MODEL SET TRAIN_STATUS = false, TIMESTAMP = NOW() WHERE TRAIN_STATUS = true ", nativeQuery = true)
	void updateTrainStatusFalse();
}
