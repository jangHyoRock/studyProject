package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NN_CONF)
 */
@Entity
@Table(name="TB_NAV_TREND_ITEM_CHART")
public class NavTrendItemChartEntity {
	
	@Column(name="ITEM")
	private String item;
	
	@Id
	@Column(name="ALIAS")
	private String alias;	

	@Column(name="UNIT")
	private String unit;
		
	@Column(name="DATA_TYPE")
	private String dataType;
	
	@Column(name="ALIAS_ORDER")
	private int aliasOrder;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getAliasOrder() {
		return aliasOrder;
	}

	public void setAliasOrder(int aliasOrder) {
		this.aliasOrder = aliasOrder;
	}
}
