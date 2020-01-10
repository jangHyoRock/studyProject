package dhi.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dhi.common.model.db.ConfigChkEntity;
import dhi.common.model.db.ConfigInfEntity;
import dhi.common.model.json.ConfigItem;
import dhi.common.model.json.Settings;
import dhi.common.model.json.SettingsEfficiency;
import dhi.common.repository.ConfigChkRepository;
import dhi.common.repository.ConfigInfRepository;

/*
 * Setting(KPI, Coal) service.
 */
@Service
@Transactional
public class SettingServiceImpl implements SettingService {

	@Autowired
	ConfigChkRepository configChkRepository;

	@Autowired
	ConfigInfRepository configInfRepository;

	public boolean isConfigCheck(String plantUnitId, String checkId) {
		ConfigChkEntity configChkEntity = this.configChkRepository.findByPlantUnitIdAndChkIdNativeQuery(plantUnitId, checkId);
		return configChkEntity.isChkVal();
	}

	public void updateConfigCheck(String plantUnitId, boolean isCheck, String checkId) {
		this.configChkRepository.updateConfigCheckNativeQuery(plantUnitId, checkId, isCheck);
	}

	public List<ConfigItem> getSettingInfo(String plantUnitId, String configType) {

		List<ConfigItem> configItemList = new ArrayList<ConfigItem>();
		List<ConfigInfEntity> configInfEntityList = this.configInfRepository.findByConfTypeOrderByConfOrderAscNativeQuery(plantUnitId, configType);
		for (ConfigInfEntity configInfEntity : configInfEntityList) {
			ConfigItem configItem = new ConfigItem();
			configItem.setTagId(configInfEntity.getConfigInfPK().getConfId());
			configItem.setItem(configInfEntity.getConfNm());
			configItem.setUnit(configInfEntity.getConfUnit());
			configItem.setCurrent(configInfEntity.getConfVal());
			configItemList.add(configItem);
		}

		return configItemList;
	}

	public void updateSettingInfo(String configType, Settings settings) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		for (ConfigItem configItem : settings.getDataList()) {
			this.configInfRepository.updateConfigValueNativeQuery(settings.getPlantUnitId(), configItem.getTagId(), configType, configItem.getCurrent(), authentication.getName());
		}
	}
	
	public List<SettingsEfficiency> getSettingEfficiencyInfo(String plantUnitId, String configType) {

		List<SettingsEfficiency> settingsEfficiencyList = new ArrayList<SettingsEfficiency>();
		List<ConfigInfEntity> configInfEntityList = this.configInfRepository.findByConfTypeOrderByConfOrderAscNativeQuery(plantUnitId, configType);		
		List<String> categoryNmList = this.configInfRepository.findByConfTypeOrderByConfOrderAscDistinctCategoryNmNativeQuery(plantUnitId, configType);
		
		for (String categoryNm : categoryNmList) {
			SettingsEfficiency settingsEfficiency = new SettingsEfficiency();
			settingsEfficiency.setCategoryNm(categoryNm);

			List<ConfigItem> configItemList = new ArrayList<ConfigItem>();
			for (ConfigInfEntity configInfEntity : configInfEntityList) {
				if (categoryNm.equals(configInfEntity.getCateGoryNm())) {
					ConfigItem configItem = new ConfigItem();
					configItem.setTagId(configInfEntity.getConfigInfPK().getConfId());
					configItem.setItem(configInfEntity.getConfNm());
					configItem.setUnit(configInfEntity.getConfUnit());
					configItem.setCurrent(configInfEntity.getConfVal());
					configItemList.add(configItem);
				}
			}
			
			settingsEfficiency.setDataList(configItemList);
			settingsEfficiencyList.add(settingsEfficiency);
		}
		
		return settingsEfficiencyList;
	}
}