package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.
 */
@Entity
@Table(name="TB_CONFIG_CHK")
public class ConfigChkEntity {

	@EmbeddedId
	private ConfigChkIdEntity ConfigChkId;
	
	@Column(name="CHK_VAL")
	private boolean chkVal;	

	public ConfigChkIdEntity getConfigChkId() {
		return ConfigChkId;
	}

	public void setConfigChkId(ConfigChkIdEntity configChkId) {
		ConfigChkId = configChkId;
	}

	public boolean isChkVal() {
		return chkVal;
	}

	public void setChkVal(boolean chkVal) {
		this.chkVal = chkVal;
	}	
}