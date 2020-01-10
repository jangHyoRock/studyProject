package dhi.common.service;

import java.util.List;

import dhi.common.model.json.ConfigItem;
import dhi.common.model.json.Settings;
import dhi.common.model.json.SettingsEfficiency;

/*
 * Setting management service interface.
 */
public interface SettingService {

	public boolean isConfigCheck(String plantUnitId, String checkId);
	
	public List<ConfigItem> getSettingInfo(String plantUnitId, String configType);
	
	public List<SettingsEfficiency> getSettingEfficiencyInfo(String plantUnitId, String configType);

	public void updateSettingInfo(String configType, Settings settings);
	
	public void updateConfigCheck(String plantUnitId, boolean isCheck, String checkId);
}