package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.
 */
@Entity
@Table(name="TB_PLANT_UNIT")
public class PlantUnitEntity {

	@EmbeddedId
	private PlantUnitIdEntity plantUnitIdEntity;
		
	@Column(name="P_UNIT_NO")
	private String plantUnitNo;
	
	@Column(name="P_UNIT_ORDER")
	private String plantUnitOrder;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="P_UNIT_IP")
	private String plantUnitIP;
	
	@Column(name="P_UNIT_PORT")
	private String plantUnitPort;
	
	public PlantUnitIdEntity getPlantUnitIdEntity() {
		return plantUnitIdEntity;
	}

	public void setPlantUnitIdEntity(PlantUnitIdEntity plantUnitIdEntity) {
		this.plantUnitIdEntity = plantUnitIdEntity;
	}

	public String getPlantUnitNo() {
		return plantUnitNo;
	}

	public void setPlantUnitNo(String plantUnitNo) {
		this.plantUnitNo = plantUnitNo;
	}

	public String getPlantUnitOrder() {
		return plantUnitOrder;
	}

	public void setPlantUnitOrder(String plantUnitOrder) {
		this.plantUnitOrder = plantUnitOrder;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlantUnitIP() {
		return plantUnitIP;
	}

	public void setPlantUnitIP(String plantUnitIP) {
		this.plantUnitIP = plantUnitIP;
	}
	
	public String getPlantUnitPort() {
		return plantUnitPort;
	}

	public void setPlantUnitPort(String plantUnitPort) {
		this.plantUnitPort = plantUnitPort;
	}
}
