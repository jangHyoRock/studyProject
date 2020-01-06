package dhi.optimizer.controller;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dhi.common.httpEntity.RestResponse;
import dhi.common.httpEntity.RestResponseEntity;
import dhi.common.httpEntity.RestResponseEntityList;

import dhi.optimizer.common.CommonConst;
import dhi.optimizer.model.json.AlarmAndEvent;
import dhi.optimizer.model.json.CombustionDynamics;
import dhi.optimizer.model.json.CombustionStatus;
import dhi.optimizer.model.json.CombustionStatusT;
import dhi.optimizer.model.json.ItemStatus;
import dhi.optimizer.model.json.Overview;
import dhi.optimizer.model.json.PerformanceAndEfficiency;
import dhi.optimizer.service.MonitoringService;

/**
 * Monitoring Controller Class. <br>
 * : 연소최적화 화면에서 실시간 모니터링을 위한 Rest API Controller.
 */
@RestController
@RequestMapping("/")
public class MonitoringController {

	private static final String REPORT_FILENAME = "PerformanceReport.xlsx";
	
	@Autowired
	private MonitoringService monitoringService;
	
	/**
	 * Overview API. <br>
	 * Path : Overview.
	 * @param plantUnitId Plant Unit ID.
	 * @return Response Result.
	 */
	@GetMapping("/overview/data/{plant_unit_id}")
	public RestResponseEntity<?> getOverview(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntity<Overview> result = null;
		try {
			result = new RestResponseEntity<Overview>(this.monitoringService.getOverview(plantUnitId));
		} catch (Exception e) {
			result = new RestResponseEntity<Overview>(e);
		}

		return result;
	}
	
	/**
	 * Performance & Efficiency API. <br>
	 * Path : Performance & Efficiency.
	 * @param plantUnitId Plant Unit ID.
	 * @return Response Result.
	 */
	@GetMapping("/performance_efficiency/data/{plant_unit_id}")
	public RestResponseEntity<?> getPerformanceAndEfficiency(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntity<PerformanceAndEfficiency> result = null;
		try {
			result = new RestResponseEntity<PerformanceAndEfficiency>(this.monitoringService.getPerformanceAndEfficiency(plantUnitId));
		} catch (Exception e) {
			result = new RestResponseEntity<PerformanceAndEfficiency>(e);
		}

		return result;
	}
	
	/**
	 * Performance & Efficiency report download check API. <br>
	 * Path : Performance & Efficiency -> Report.
	 * @param plantUnitId Plant Unit ID.
	 * @param start_date Search Start Date.
	 * @param end_date Search End Date.
	 * @return Response Result.
	 */
	@GetMapping("/performance_efficiency/report/check/{plant_unit_id}")
	public RestResponse reportCheck(HttpServletResponse response
			, @PathVariable(value = "plant_unit_id") String plantUnitId
			, @RequestParam(value = "start_date") String startDate
			, @RequestParam(value = "end_date") String endDate) {
		RestResponse result = null;
		
		try {
			boolean isReportSearchExist = this.monitoringService.isReportSearchExist(plantUnitId, startDate, endDate);
			result = new RestResponse(String.valueOf(isReportSearchExist));
		} catch (Exception e) {			
			result = new RestResponse(e);
		}
		
		return result;
	}
	
	/**
	 * Performance & Efficiency Report Download API. <br>
	 * Path : Performance & Efficiency -> Report.
	 * @param plantUnitId Plant Unit ID.
	 * @param start_date Search Start Date.
	 * @param end_date Search End Date.
	 * @return Response Result.
	 */
	@GetMapping("/performance_efficiency/report/download/{plant_unit_id}")
	public RestResponse downloadReportExcel(HttpServletResponse response
			, @PathVariable(value = "plant_unit_id") String plantUnitId
			, @RequestParam(value = "start_date") String startDate
			, @RequestParam(value = "end_date") String endDate) {
		RestResponse result = null;
		response.setHeader("Set-Cookie", "fileDownload=true; path=/");
		
		try {
			ClassPathResource classPathResource = new ClassPathResource(REPORT_FILENAME);
			InputStream inputStream = classPathResource.getInputStream();
			XSSFWorkbook workbook = this.monitoringService.getReportExcelWorkBook(plantUnitId, inputStream, startDate, endDate);
			response.setHeader("Content-Disposition", String.format("attachment; filename=" + REPORT_FILENAME));
			workbook.write(response.getOutputStream());
			workbook.close();
			result = new RestResponse();
		} catch (Exception e) {
			response.setHeader("Content-Disposition", String.format("attachment; filename=NoData"));			
			result = new RestResponse(e);
		}
		
		return result;
	}
	
	/**
	 * Combustion Status API. <br>
	 * Path : Combustion Status.
	 * @param plantUnitId Plant Unit ID.
	 * @return Response Result.
	 */
	@GetMapping("/combustion_status_back/data/{plant_unit_id}")
	public RestResponseEntity<?> getCombustionStatus(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntity<CombustionStatus> result = null;
		try {
			result = new RestResponseEntity<CombustionStatus>(this.monitoringService.getCombustionStatus(plantUnitId));
		} catch (Exception e) {
			result = new RestResponseEntity<CombustionStatus>(e);
		}
		
		return result;
	}
	
	/**
	 * Combustion Status API. <br>
	 * Path : Combustion Status.
	 * @param plantUnitId Plant Unit ID.
	 * @return Response Result.
	 */
	@GetMapping("/combustion_status/data/{plant_unit_id}")
	public RestResponseEntity<?> getCombustionStatusT(@PathVariable(value = "plant_unit_id") String plantUnitId, @RequestParam(value = "ofa_option", required=false) String ofaOption, @RequestParam(value = "coal_option", required=false) String coalOption) {
		RestResponseEntity<CombustionStatusT> result = null;
		try {
			result = new RestResponseEntity<CombustionStatusT>(this.monitoringService.getCombustionStatusT(plantUnitId, ofaOption, coalOption));
		} catch (Exception e) {
			result = new RestResponseEntity<CombustionStatusT>(e);
		}
		
		return result;
	}
	
	/**
	 * Combustion Dynamics API. <br>
	 * Path : Combustion Dynamics.
	 * @param plantUnitId Plant Unit ID.
	 * @return Response Result.
	 */
	@GetMapping("/combustion_dynamics/data/{plant_unit_id}")
	public RestResponseEntity<?> getCombustionDynamics(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntity<CombustionDynamics> result = null;
		try {
			result = new RestResponseEntity<CombustionDynamics>(this.monitoringService.getCombustionDynamics(plantUnitId));
		} catch (Exception e) {
			result = new RestResponseEntity<CombustionDynamics>(e);
		}

		return result;
	}

	/**
	 * Alarm & Event API. <br>
	 * Path : Alarm & Event.
	 * @param startDate Start Date.
	 * @param endDate End Date.
	 * @param pageNo Page Number.
	 * @param pageSize Page List Size.
	 * @return Response Result.
	 */
	@GetMapping("/alarm_event/data")
	public RestResponseEntity<?> getAlarmAndEvent(@RequestParam(value = "start_date") String startDate,
			@RequestParam(value = "end_date") String endDate,
			@RequestParam(value = "page_no", required = false) Integer pageNo,
			@RequestParam(value = "page_size", required = false) Integer pageSize) {
		RestResponseEntity<AlarmAndEvent> result = null;
		try {
			result = new RestResponseEntity<AlarmAndEvent>(this.monitoringService.getAlarmAndEvent(startDate, endDate, pageNo == null ? 0 : pageNo, pageSize == null ? CommonConst.DEFAULT_PAGE_LIST_SIZE : pageSize));
		} catch (Exception e) {
			result = new RestResponseEntity<AlarmAndEvent>(e);
		}

		return result;
	}
	
	/**
	 * System Information(Memory, CPU, Disk, Disk Remained amount) API. <br>
	 * Path : Left Menu -> System Information.
	 * @return Response Result.
	 */
	@GetMapping("/common/system_information")
	public RestResponseEntityList<ItemStatus> getSystemInformation() {
		RestResponseEntityList<ItemStatus> result;
		try {
			result = new RestResponseEntityList<ItemStatus>(this.monitoringService.getSystemInformation());
		} catch (Exception e) {
			result = new RestResponseEntityList<ItemStatus>(e);
		}
		
		return result;
	}
}