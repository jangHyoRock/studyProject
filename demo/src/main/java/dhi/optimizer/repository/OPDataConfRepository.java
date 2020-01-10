package dhi.optimizer.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.OPDataConfEntity;

@Repository
public interface OPDataConfRepository extends JpaRepository<OPDataConfEntity, String> {
	
	List<OPDataConfEntity> findByTagNmIn(Collection<String> tagNms);
	
	@Query(value = "SELECT TAG_ID, TAG_NM, UNIT FROM TB_OP_DATA_CONF WHERE TAG_ID IN :tagIds ", nativeQuery = true)
	List<Object[]> getOpDataConf(@Param("tagIds") Collection<String> tagIds);
}
