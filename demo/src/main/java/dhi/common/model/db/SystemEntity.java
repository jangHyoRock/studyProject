package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.
 */
@Entity
@Table(name="TB_SYSTEM")
public class SystemEntity {

	@Id
	@Column(name="SYS_ID")
	private String systemId;
	
	@Column(name="SYS_NM")
	private String systemName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="TYPE")
	private String type;
	
	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
}
