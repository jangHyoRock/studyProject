package dhi.common.service;

import java.util.List;

import dhi.common.model.json.SettingsBaseDataConfig;
import dhi.common.model.json.SettingsCompanyDataConfig;
import dhi.common.model.json.SettingsPlantDataConfig;
import dhi.common.model.json.SettingsUnitDataConfig;

public interface SettingsService {

	public List<SettingsBaseDataConfig> getBaseDataConfList();
	public void resetBaseDataConfList(List<SettingsBaseDataConfig> baseDataConfigList);
	
	public List<SettingsCompanyDataConfig> getCompanyDataConfList();
	public void resetCompanyDataConfList(List<SettingsCompanyDataConfig> companyDataConfigList);
	
	public List<SettingsPlantDataConfig> getPlantDataConfList();
	public void resetPlantDataConfList(List<SettingsPlantDataConfig> plantDataConfigList);
	
	public List<SettingsUnitDataConfig> getUnitDataConfList();
	public void resetUnitDataConfList(List<SettingsUnitDataConfig> unitDataConfigList);
}
