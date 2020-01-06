package dhi.optimizer.model.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * DB Table Model Class using JPA.(TB_OUTPUT_MV_TGT)
 */
@Entity
@Table(name="TB_OUTPUT_MV_TGT")
public class OutputMVTGTEntity {

	@Id
	@Column(name="MV")
	private String mv;
	
	@Column(name="MV_TGT")
	private double mvTGT;
	
	@Column(name="TIMESTAMP")
	private Date timestamp;
	
	@Column(name="MV_BIAS_SUM")
	private double mvBiasSum;
	
	@Column(name="MV_BIAS_CNT")
	private int mvBiasCnt;	

	public OutputMVTGTEntity() {
	}

	public OutputMVTGTEntity(String mv, double mvTGT) {
		this.mv = mv;
		this.mvTGT = mvTGT;
		this.timestamp = new Date();
	}
	
	public String getMv() {
		return mv;
	}

	public void setMv(String mv) {
		this.mv = mv;
	}

	public double getMvTGT() {
		return mvTGT;
	}

	public void setMvTGT(double mvTGT) {
		this.mvTGT = mvTGT;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public double getMvBiasSum() {
		return mvBiasSum;
	}

	public void setMvBiasSum(double mvBiasSum) {
		this.mvBiasSum = mvBiasSum;
	}

	public int getMvBiasCnt() {
		return mvBiasCnt;
	}

	public void setMvBiasCnt(int mvBiasCnt) {
		this.mvBiasCnt = mvBiasCnt;
	}	
}
