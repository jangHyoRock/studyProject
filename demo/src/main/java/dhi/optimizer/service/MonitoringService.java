package dhi.optimizer.service;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dhi.common.exception.InvalidParameterException;

import dhi.optimizer.model.json.AlarmAndEvent;
import dhi.optimizer.model.json.CombustionDynamics;
import dhi.optimizer.model.json.CombustionStatus;
import dhi.optimizer.model.json.CombustionStatusT;
import dhi.optimizer.model.json.ItemStatus;
import dhi.optimizer.model.json.Overview;
import dhi.optimizer.model.json.PerformanceAndEfficiency;

/*
 * Monitoring service interface.
 */
	
public interface MonitoringService {
	public Overview getOverview(String plantUnitId) throws InvalidParameterException;
	
	public PerformanceAndEfficiency getPerformanceAndEfficiency(String plantUnitId) throws InvalidParameterException;
	
	public Boolean isReportSearchExist(String plantUnitId, String startDateString, String endDateString) throws InvalidParameterException;
		
	public XSSFWorkbook getReportExcelWorkBook(String plantUnitId, InputStream inputStream, String startDateString, String endDateString) throws InvalidParameterException;
	
	public CombustionStatus getCombustionStatus(String plantUnitId) throws InvalidParameterException;
	
	public CombustionStatusT getCombustionStatusT(String plantUnitId, String ofaOption, String coalOption) throws InvalidParameterException;
	
	public CombustionDynamics getCombustionDynamics(String plantUnitId) throws InvalidParameterException;
	
	public AlarmAndEvent getAlarmAndEvent(String startDateString, String endDateString, int pageNo, int pageSize) throws InvalidParameterException;
		
	public List<ItemStatus> getSystemInformation();
}