package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NavConfEntity;

@Repository	
public interface NavConfRepository extends JpaRepository<NavConfEntity, String> {
	
	public List<NavConfEntity> findByOrderByItemOrderAsc();
	
	@Modifying
	@Query(value = "UPDATE TB_NAV_CONF SET HIGH_VAL = ?2, MARGIN_VAL = ?3, USE_YN = ?4 WHERE ITEM_ID = ?1 ", nativeQuery = true)
	void updateByItemIdSetHighAndMarginAndUseYn(String itemId, double highValue, double marginValue, boolean isUse);
}
