package dhi.optimizer.model;

public class ReportInfoVO {
	
	private String reportId;
	
	private double reportSum;
	
	private double reportCount;
	
	private double reportAvg;
	
	public ReportInfoVO (double reportSum, int reportCount)
	{
		this.reportSum = reportSum;
		this.reportCount = reportCount;
		this.reportAvg = reportSum / reportCount;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public double getReportSum() {
		return reportSum;
	}
	
	public double getReportCount() {
		return reportCount;
	}

	public double getReportAvg() {
		return reportAvg;
	}
}
