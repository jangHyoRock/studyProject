package dhi.optimizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.optimizer.model.db.NavTrendItemChartEntity;

@Repository
public interface NavTrendItemChartRepository extends JpaRepository<NavTrendItemChartEntity, String> {
	
	List<NavTrendItemChartEntity> findByItemOrderByAliasOrderAsc(String item);
}