package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_PSO_EFFECT_PER_ITEM)
 */
@Entity
@Table(name="TB_PSO_EFFECT_PER_ITEM")
public class PSOEffectPerItemEntity {

	@Id
	@Column(name="ITEM")
	private String item;
	
	@Column(name="BEFORE_VAL")
	private Double beforeVal;
	
	@Column(name="AFTER_NN_VAL")
	private Double afterNNVal;
	
	@Column(name="AFTER_VAL")
	private Double afterVal;
	
	@Column(name="TIMESTAMP")
	private Date timestamp;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Double getBeforeVal() {
		return beforeVal;
	}

	public void setBeforeVal(Double beforeVal) {
		this.beforeVal = beforeVal;
	}

	public Double getAfterNNVal() {
		return afterNNVal;
	}

	public void setAfterNNVal(Double afterNNVal) {
		this.afterNNVal = afterNNVal;
	}

	public Double getAfterVal() {
		return afterVal;
	}

	public void setAfterVal(Double afterVal) {
		this.afterVal = afterVal;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}	
}
