package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import dhi.optimizer.model.db.NNTrainDataConfEntity;

public interface NNTrainDataConfRepository extends JpaRepository<NNTrainDataConfEntity, String> {
	
	List<NNTrainDataConfEntity> findByIoTypeOrderByTagNmAsc(String ioType);
}