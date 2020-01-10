package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_APP_INF)
 */
@Entity
@Table(name="TB_APP_INF")
public class AppInfEntity {

	@Id
	@Column(name="P_UNIT_ID")
	private String pUnitId;

	public String getpUnitId() {
		return pUnitId;
	}

	public void setpUnitId(String pUnitId) {
		this.pUnitId = pUnitId;
	}
}
