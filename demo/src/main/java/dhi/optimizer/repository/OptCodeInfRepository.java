package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.OptCodeInfEntity;
import dhi.optimizer.model.db.OptCodeInfIdEntity;

/*
 * JAP control repository.
 */
@Repository
public interface OptCodeInfRepository extends JpaRepository<OptCodeInfEntity, OptCodeInfIdEntity> {
	
	@Query(value = "SELECT CODE_ID, CODE_NM " 
			+ "FROM TB_OPT_CODE_INF "
			+ "WHERE GROUP_ID = ?1 "
			+ "ORDER BY CODE_ORDER ASC ", nativeQuery = true)
	List<Object[]> findByGroupIdOrderByCodeOrderAsc(String groupId);
	
	@Query(value = "SELECT A.CODE_ID, A.CODE_NM, B.TAG_NM, B.UNIT " 
			+ "FROM TB_OPT_CODE_INF A INNER JOIN TB_OP_DATA_CONF B "
			+ "ON A.TAG_ID = B.TAG_ID "
			+ "WHERE A.GROUP_ID = ?1 "
			+ "ORDER BY A.CODE_NM ASC ", nativeQuery = true)
	List<Object[]> findWithOPDataByGroupID(String groupId);

}
