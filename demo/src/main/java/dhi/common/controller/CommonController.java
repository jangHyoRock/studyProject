package dhi.common.controller;

import java.net.URL;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dhi.common.httpEntity.RestResponseEntity;
import dhi.common.httpEntity.RestResponseEntityList;
import dhi.common.model.json.CommonData;
import dhi.common.model.json.KeyValue;
import dhi.common.model.json.LeftMenu;
import dhi.common.service.CommonService;
/*
 * A controller that manages the common APIs required between systems.
 */
@RestController
@RequestMapping("/common")
public class CommonController {

	static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CommonService commonService;

	@GetMapping("/left_menu/info/{system_id}")
	public RestResponseEntity<LeftMenu> getLeftMenu(@PathVariable(value = "system_id") String systemId, ServletRequest servletRequest) {
		RestResponseEntity<LeftMenu> result;
		try {
			
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;			
			URL urlInfo = new URL(httpServletRequest.getRequestURL().toString());
			
			result = new RestResponseEntity<LeftMenu>(this.commonService.getLeftMenu(systemId, urlInfo.getHost()));
		} catch (Exception e) {
			result = new RestResponseEntity<LeftMenu>(e);
		}
		
		return result;
	}
	
	@GetMapping("/data/{system_id}/{plant_unit_id}")
	public RestResponseEntity<CommonData> getCommonData(@PathVariable(value = "system_id") String systemId, @PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntity<CommonData> result;		
		try {
			result = new RestResponseEntity<CommonData>(this.commonService.getCommonData(systemId, plantUnitId));
		} catch (Exception e) {
			result = new RestResponseEntity<CommonData>(e);
		}
		
		return result;
	}
	
    @GetMapping("/plant_unit/ddl")
	public RestResponseEntityList<KeyValue> getTrendCategory() {
		RestResponseEntityList<KeyValue> result;
		try {
			result = new RestResponseEntityList<KeyValue>(this.commonService.getPlantUnitDDL());
		} catch (Exception e) {
			result = new RestResponseEntityList<KeyValue>(e);
		}
		
		return result;
	}
}