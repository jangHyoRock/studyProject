package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.
 */
@Entity
@Table(name="TB_PLANT")
public class PlantEntity {

	@Id
	@Column(name="PLANT_ID")
	private String plantId;
	
	@Column(name="PLANT_NM")
	private String plantNm;
	
	@Column(name="PLANT_ORDER")
	private int plantOrder;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	public String getPlantId() {
		return plantId;
	}

	public void setPlantId(String plantId) {
		this.plantId = plantId;
	}

	public String getPlantNm() {
		return plantNm;
	}

	public void setPlantNm(String plantNm) {
		this.plantNm = plantNm;
	}

	public int getPlantOrder() {
		return plantOrder;
	}

	public void setPlantOrder(int plantOrder) {
		this.plantOrder = plantOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
