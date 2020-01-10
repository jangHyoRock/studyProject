package dhi.optimizer.model.db;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="TB_REPORT")
public class ReportEntity {

	@EmbeddedId
	private ReportIdEntity reportIdEntity;
	
	@Column(name="R_TYPE")
	private String rType;
	
	@Column(name="R_SUM")
	private double rSum;
	
	@Column(name="R_COUNT")
	private int rCount;

	public ReportIdEntity getReportIdEntity() {
		return reportIdEntity;
	}

	public void setReportIdEntity(ReportIdEntity reportIdEntity) {
		this.reportIdEntity = reportIdEntity;
	}

	public String getrType() {
		return rType;
	}

	public void setrType(String rType) {
		this.rType = rType;
	}

	public double getrSum() {
		return rSum;
	}

	public void setrSum(double rSum) {
		this.rSum = rSum;
	}

	public int getrCount() {
		return rCount;
	}

	public void setrCount(int rCount) {
		this.rCount = rCount;
	}	
}
