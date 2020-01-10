package dhi.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dhi.common.model.db.PlantEntity;

/*
 * JAP user repository.
 */
@Repository
public interface PlantRepository extends JpaRepository<PlantEntity, String> {

	List<PlantEntity> findAllByOrderByPlantOrderAsc();
}
