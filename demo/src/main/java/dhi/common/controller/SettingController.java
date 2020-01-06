package dhi.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dhi.common.httpEntity.RestResponse;
import dhi.common.httpEntity.RestResponseEntityList;
import dhi.common.model.json.ConfigItem;
import dhi.common.model.json.Settings;
import dhi.common.model.json.SettingsEfficiency;
import dhi.common.service.SettingService;
import dhi.common.util.CommonConst;

/*
 * A controller that manages the required Setting APIs between systems.
 */
@RestController
@RequestMapping("/setting")
public class SettingController {

	private static final String ConfigChkCoalAutoCheck = "coal_auto_check";
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/coal/auto_check/{plant_unit_id}")
	public RestResponse getCoalAutoCheck(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponse result;
		try {
			boolean isCheck = this.settingService.isConfigCheck(plantUnitId, ConfigChkCoalAutoCheck);
			result = new RestResponse(String.valueOf(isCheck));
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	@PostMapping("/coal/auto_check")
	public RestResponse setCoalAutoCheck(@RequestBody Settings settings ) {
		RestResponse result;
		try {
			this.settingService.updateConfigCheck(settings.getPlantUnitId(), settings.getCheck(), ConfigChkCoalAutoCheck);
			result = new RestResponse();
		}
		catch (Exception e) {
			result = new RestResponse(e);
		}
		
		return result;
	}
	
	@GetMapping("/kpi/data/{plant_unit_id}" )
	public RestResponseEntityList<ConfigItem> getKPI(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntityList<ConfigItem> result;
		try {
			result = new RestResponseEntityList<ConfigItem>(this.settingService.getSettingInfo(plantUnitId, CommonConst.CONFING_SETTING_TYPE_KPI));
		} catch (Exception e) {
			result = new RestResponseEntityList<ConfigItem>(e);
		}
		
		return result;
	}
	
	@PostMapping("/kpi/data")
	public RestResponse setKPI(@RequestBody Settings settings) {
		RestResponse result = null;
		try {
			this.settingService.updateSettingInfo(CommonConst.CONFING_SETTING_TYPE_KPI, settings);
			result = new RestResponse();
		}catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	@GetMapping("/coal/data/{plant_unit_id}")
	public RestResponseEntityList<ConfigItem> getGoal(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntityList<ConfigItem> result;
		try {
			result = new RestResponseEntityList<ConfigItem>(this.settingService.getSettingInfo(plantUnitId, CommonConst.CONFING_SETTING_TYPE_COAL));
		} catch (Exception e) {
			result = new RestResponseEntityList<ConfigItem>(e);
		}
		
		return result;
	}
	
	@PostMapping("/coal/data")
	public RestResponse setGoal(@RequestBody Settings settings) {
		RestResponse result = null;
		try {
			this.settingService.updateSettingInfo(CommonConst.CONFING_SETTING_TYPE_COAL, settings);
			result = new RestResponse();
		}catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	@GetMapping("/efficiency/data/{plant_unit_id}")
	public RestResponseEntityList<SettingsEfficiency> getEfficiency(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntityList<SettingsEfficiency> result;
		try {
			result = new RestResponseEntityList<SettingsEfficiency>(this.settingService.getSettingEfficiencyInfo(plantUnitId, CommonConst.CONFING_SETTING_TYPE_EFFICIENCY));
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsEfficiency>(e);
		}
		
		return result;
	}
	
	@PostMapping("/efficiency/data")
	public RestResponse setEfficiency(@RequestBody Settings settings) {
		RestResponse result = null;
		try {
			this.settingService.updateSettingInfo(CommonConst.CONFING_SETTING_TYPE_EFFICIENCY, settings);
			result = new RestResponse();
		}catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	@GetMapping("/baseline/data/{plant_unit_id}")
	public RestResponseEntityList<ConfigItem> getBaseline(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntityList<ConfigItem> result;
		try {
			result = new RestResponseEntityList<ConfigItem>(this.settingService.getSettingInfo(plantUnitId, CommonConst.CONFING_SETTING_TYPE_BASELINE));
		} catch (Exception e) {
			result = new RestResponseEntityList<ConfigItem>(e);
		}
		
		return result;
	}
	
	@PostMapping("/baseline/data")
	public RestResponse setBaseline(@RequestBody Settings settings) {
		RestResponse result = null;
		try {
			this.settingService.updateSettingInfo(CommonConst.CONFING_SETTING_TYPE_BASELINE, settings);
			result = new RestResponse();
		}catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	@GetMapping("/optimizer_config/data/{plant_unit_id}")
	public RestResponseEntityList<ConfigItem> getOptimizerConfig(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntityList<ConfigItem> result;
		try {
			result = new RestResponseEntityList<ConfigItem>(this.settingService.getSettingInfo(plantUnitId, CommonConst.CONFING_SETTING_TYPE_OPTCONF));
		} catch (Exception e) {
			result = new RestResponseEntityList<ConfigItem>(e);
		}
		
		return result;
	}
	
	@PostMapping("/optimizer_config/data")
	public RestResponse setOptimizerConfig(@RequestBody Settings settings) {
		RestResponse result = null;
		try {
			this.settingService.updateSettingInfo(CommonConst.CONFING_SETTING_TYPE_OPTCONF, settings);
			result = new RestResponse();
		}catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
}