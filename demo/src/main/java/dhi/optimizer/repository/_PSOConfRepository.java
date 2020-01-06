package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db._PSOConfEntity;

/*
 * JAP control repository.
 */
@Repository
public interface _PSOConfRepository extends JpaRepository<_PSOConfEntity, String> {
	
	@Query(value = "SELECT A.*, B.* "
			+ "FROM DD_TB_PSO A, DD_TB_PSO_CONF B "
			+ "WHERE A.PSO_ID = B.PSO_ID ", nativeQuery = true)
	List<_PSOConfEntity> findAllNativeQuery();
}
