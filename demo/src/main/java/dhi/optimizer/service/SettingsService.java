package dhi.optimizer.service;

import java.util.List;

import dhi.optimizer.model.json.SettingsNNTrainDataConfig;
import dhi.optimizer.model.json.SettingsOpDataConfig;
import dhi.optimizer.model.json.SettingsPsoMVInfoConfig;

public interface SettingsService {

	public List<SettingsOpDataConfig> getOpDataConfList();
	public void resetOpDataConf(List<SettingsOpDataConfig> opDataConfigList);
	
	public List<SettingsOpDataConfig> getControlDataConfList();
	public void resetControlDataConf(List<SettingsOpDataConfig> ctrDataConfigList);
	
	public List<SettingsOpDataConfig> getCommonDataConfList();
	public void resetCommonDataConf(List<SettingsOpDataConfig> commonDataConfigList);
	
	public List<SettingsNNTrainDataConfig> getNNTrainDataConfList();
	public boolean checkNNModelDelete(List<SettingsNNTrainDataConfig> nnTrainDataConfigList);
	public void resetNNTrainDataConf(List<SettingsNNTrainDataConfig> nnTrainDataConfigList, boolean isNNModelDelete);
		
	public List<SettingsPsoMVInfoConfig> getPsoMVInfoConfList();
	public void resetPsoMVInfoDataConf(List<SettingsPsoMVInfoConfig> psoMVInfoConfigList);
}
