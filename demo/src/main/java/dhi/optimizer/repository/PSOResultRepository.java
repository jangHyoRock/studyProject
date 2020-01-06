package dhi.optimizer.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.PSOResultEntity;

/*
 * JAP control repository.
 */
@Repository
public interface PSOResultRepository extends JpaRepository<PSOResultEntity, Date> {
	
	PSOResultEntity findTop1ByOrderByTimestampDesc();
	
	@Modifying
	@Query(value = "UPDATE TB_PSO_RESULT SET NEW_MV_TGT_YN = false WHERE NEW_MV_TGT_YN = true ", nativeQuery = true)
	void updateNewMVTGTYNFalse();
}
