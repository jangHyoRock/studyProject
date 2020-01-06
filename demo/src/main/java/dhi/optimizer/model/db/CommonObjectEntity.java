package dhi.optimizer.model.db;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CommonObjectEntity {
	
	@Id
	private String pUnitId;
	public String getpUnitId() {
		return pUnitId;
	}
	
	public void setpUnitId(String pUnitId) {
		this.pUnitId = pUnitId;
	}
}
