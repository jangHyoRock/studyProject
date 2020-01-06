package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_COST_SAVING_EFFECT")
public class CostSavingEffectEntity {
	
	@Id
	@Column(name="TIMESTAMP")
	private Date timestamp;	

	@Column(name="COAL_VAL")
	private double coalVal;
	
	@Column(name="PWR_VAL")
	private double powerVal;
	
	@Column(name="VALID_YN")
	private boolean validYn;
}
