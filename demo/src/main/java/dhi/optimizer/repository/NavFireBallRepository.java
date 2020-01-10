package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NavFireBallEntity;


@Repository
public interface NavFireBallRepository extends JpaRepository<NavFireBallEntity, String> {

	NavFireBallEntity findByTypeAndDirection(String type, String direction);
	
	List<NavFireBallEntity> findByTypeOrderByDirectionOrderAsc(String type);
	
	@Modifying
	@Query(value = "UPDATE TB_NAV_FIREBALL SET CORNER_1 = ?3, CORNER_2 = ?4, CORNER_3 = ?5, CORNER_4 = ?6 WHERE TYPE = ?1 AND DIRECTION = ?2 ", nativeQuery = true)
	void updateByDirectionSetFireBallCenter(String type, String direction, boolean corner1, boolean corner2, boolean corner3, boolean corner4);
}