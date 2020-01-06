package dhi.optimizer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerformanceConstSaving {

	private String item;
	
	private String hourly;
	
	private String daily;
	
	private String monthly;	
	
	private String annually;
	
	@JsonProperty("hourly_unit")
	private String hourlyUnit;
	
	@JsonProperty("daily_unit")
	private String dailyUnit;
	
	@JsonProperty("monthly_unit")
	private String monthlyUnit;	
	
	@JsonProperty("annually_unit")
	private String annuallyUnit;

	public PerformanceConstSaving() {};
	
	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getHourly() {
		return hourly;
	}

	public void setHourly(String hourly) {
		this.hourly = hourly;
	}

	public String getDaily() {
		return daily;
	}

	public void setDaily(String daily) {
		this.daily = daily;
	}

	public String getMonthly() {
		return monthly;
	}

	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}

	public String getAnnually() {
		return annually;
	}

	public void setAnnually(String annually) {
		this.annually = annually;
	}	

	public String getHourlyUnit() {
		return hourlyUnit;
	}

	public void setHourlyUnit(String hourlyUnit) {
		this.hourlyUnit = hourlyUnit;
	}

	public String getDailyUnit() {
		return dailyUnit;
	}

	public void setDailyUnit(String dailyUnit) {
		this.dailyUnit = dailyUnit;
	}

	public String getMonthlyUnit() {
		return monthlyUnit;
	}

	public void setMonthlyUnit(String monthlyUnit) {
		this.monthlyUnit = monthlyUnit;
	}

	public String getAnnuallyUnit() {
		return annuallyUnit;
	}

	public void setAnnuallyUnit(String annuallyUnit) {
		this.annuallyUnit = annuallyUnit;
	}
}
