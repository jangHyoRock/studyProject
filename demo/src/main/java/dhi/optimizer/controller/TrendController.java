package dhi.optimizer.controller;

import dhi.common.httpEntity.RestResponseEntity;
import dhi.common.httpEntity.RestResponseEntityList;

import dhi.optimizer.model.json.Chart;
import dhi.optimizer.model.json.KeyValue;
import dhi.optimizer.service.TrendService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Trend Controller Class. <br>
 * : 연소최적화 Trend Chart 에 대한 Data 정보를 가져오는  Rest API Controller.
 */
@RestController
@RequestMapping("/trend")
public class TrendController {

	@Autowired
	TrendService trendService;

	/**
	 * Trend Category API. <br>
	 * Path : Trend.
	 * @return Response Result.
	 */
	@GetMapping("/category/ddl")
	public RestResponseEntityList<KeyValue> getTrendCategory() {
		RestResponseEntityList<KeyValue> result;
		try {
			result = new RestResponseEntityList<KeyValue>(this.trendService.getCategoryDDL());
		} catch (Exception e) {
			result = new RestResponseEntityList<KeyValue>(e);
		}

		return result;
	}

	/**
	 * Trend Chart Data API. <br>
	 * Path : Trend. 
	 * @param items Trend Item.
	 * @param startDate Trend Start Date.
	 * @param endDate Trend End Date.
	 * @return Response Result.
	 */
	@GetMapping("/data/chart/seconds_cycle")
	public RestResponseEntity<?> getDataChartTimeCycle(@RequestParam(value = "items") String items
			, @RequestParam(value = "start_date") String startDate
			, @RequestParam(value = "end_date") String endDate) {

		RestResponseEntity<Chart> result = null;
		try {
			result = new RestResponseEntity<Chart>(this.trendService.getTrendOPData(items, startDate, endDate));
		} catch (Exception e) {
			result = new RestResponseEntity<Chart>(e);
		}

		return result;
	}
	
	/**
	 * Trend All Item Chart Data API. <br>
	 * Path : Trend. 
	 * @param items Trend Item.
	 * @param startDate Trend Start Date.
	 * @param endDate Trend End Date.
	 * @return Response Result.
	 */
	@GetMapping("/data/chart/seconds_cycle/item/all")
	public RestResponseEntity<?> getDataChartTimeCycleItemAll(@RequestParam(value = "start_date") String startDate, @RequestParam(value = "end_date") String endDate) {

		RestResponseEntity<Chart> result = null;
		try {
			result = new RestResponseEntity<Chart>(this.trendService.getTrendOPDataAll(startDate, endDate));
		} catch (Exception e) {
			result = new RestResponseEntity<Chart>(e);
		}

		return result;
	}
}