package dhi.common.model.db;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.
 */
@Entity
@Table(name="TB_CONFIG_INF")
public class ConfigInfEntity {

	@EmbeddedId
	private ConfigInfIdEntity configInfPK;
		
	@Column(name="CONF_NM")
	private String confNm;
	
	@Column(name="CONF_UNIT")
	private String confUnit;
	
	@Column(name="CONF_VAL")
	private double confVal;
	
	@Column(name="CONF_ORDER")
	private int confOrder;

	@Column(name="CATEGORY_NM")
	private String cateGoryNm;
	
	public ConfigInfIdEntity getConfigInfPK() {
		return configInfPK;
	}

	public void setConfigInfPK(ConfigInfIdEntity configInfPK) {
		this.configInfPK = configInfPK;
	}

	public String getConfNm() {
		return confNm;
	}

	public void setConfNm(String confNm) {
		this.confNm = confNm;
	}

	public String getConfUnit() {
		return confUnit;
	}

	public void setConfUnit(String confUnit) {
		this.confUnit = confUnit;
	}

	public double getConfVal() {
		return confVal;
	}

	public void setConfVal(double confVal) {
		this.confVal = confVal;
	}

	public int getConfOrder() {
		return confOrder;
	}

	public void setConfOrder(int confOrder) {
		this.confOrder = confOrder;
	}

	public String getCateGoryNm() {
		return cateGoryNm;
	}

	public void setCateGoryNm(String cateGoryNm) {
		this.cateGoryNm = cateGoryNm;
	}
}
