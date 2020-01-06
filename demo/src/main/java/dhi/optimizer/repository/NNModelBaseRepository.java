package dhi.optimizer.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NNModelBaseEntity;

/*
 * TODO : 다단 적용. 삭제예정
 * JAP control repository.
 */
@Repository
public interface NNModelBaseRepository extends JpaRepository<NNModelBaseEntity, Date> {
	
	List<NNModelBaseEntity> findByTrainStatus(Boolean trainStatus);
	
	@Modifying
	@Query(value = "UPDATE TB_NN_MODEL_BASE SET TRAIN_STATUS = false, TIMESTAMP = NOW() WHERE TRAIN_STATUS = true ", nativeQuery = true)
	void updateTrainStatusFalse();
}
