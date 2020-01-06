package dhi.optimizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.AppInfEntity;

/*
 * JAP control repository.
 */
@Repository
public interface AppInfRepository extends JpaRepository<AppInfEntity, String> {
	@Query(value = "SELECT P_UNIT_ID FROM TB_APP_INF ", nativeQuery = true)
	public AppInfEntity getAppInfo();
}
