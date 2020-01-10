package dhi.optimizer.model.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_NAV_CONF)
 */
@Entity
@Table(name="TB_NAV_CONF")
public class NavConfEntity {
	
	@Id
	@Column(name="ITEM_ID")
	private String itemId;
	
	@Column(name="ITEM_NM")
	private String itemNm;
	
	@Column(name="HIGH_VAL")
	private Double highVal;
	
	@Column(name="MARGIN_VAL")
	private Double marginVal;
	
	@Column(name="ITEM_ORDER")
	private int itemOrder;
	
	@Column(name="UNIT")
	private String unit;
	
	@Column(name="CONF_TYPE")
	private String confType;
	
	@Column(name="USE_YN")
	private Boolean useYn;
	
	@Column(name="ITEM_GROUP")
	private String itemGroup;
			
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemNm() {
		return itemNm;
	}

	public void setItemNm(String itemNm) {
		this.itemNm = itemNm;
	}

	public Double getHighVal() {
		return highVal;
	}

	public void setHighVal(Double highVal) {
		this.highVal = highVal;
	}

	public Double getMarginVal() {
		return marginVal;
	}

	public void setMarginVal(Double marginVal) {
		this.marginVal = marginVal;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getConfType() {
		return confType;
	}

	public void setConfType(String confType) {
		this.confType = confType;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

	public Boolean getUseYn() {
		return useYn;
	}

	public void setUseYn(Boolean useYn) {
		this.useYn = useYn;
	}

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}
}
