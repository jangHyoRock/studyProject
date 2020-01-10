package dhi.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dhi.common.model.json.SettingsBaseDataConfig;
import dhi.common.model.json.SettingsCompanyDataConfig;
import dhi.common.model.json.SettingsPlantDataConfig;
import dhi.common.model.json.SettingsUnitDataConfig;
import dhi.common.repository.PlantUnitRepository;


/*
 * Settings Service.
 */
@Service
@Transactional
public class SettingsServiceImpl implements SettingsService {
	
	@Autowired
	private PlantUnitRepository plantUnitRepository;

	/**
	 * Company - Plant Relation List 
	 */
	public List<SettingsBaseDataConfig> getBaseDataConfList() {
		List<SettingsBaseDataConfig> baseDataConfigList = new ArrayList<SettingsBaseDataConfig>();
		List<Object[]> companyPlantEntityList = this.plantUnitRepository.findAllCompanyAndPlantInfoNativeQuery();
		for(Object[] companyPlantEntity : companyPlantEntityList) {
			SettingsBaseDataConfig baseDataConf = new SettingsBaseDataConfig();
			
			baseDataConf.setCompanyId(String.valueOf(companyPlantEntity[0]));
			baseDataConf.setpUnitId(String.valueOf(companyPlantEntity[1]));
			baseDataConf.setPlantId(String.valueOf(companyPlantEntity[2]));
			baseDataConf.setSysId(String.valueOf(companyPlantEntity[3]));
			baseDataConf.setpUnitIp(String.valueOf(companyPlantEntity[6]));
			baseDataConf.setDescription(String.valueOf(companyPlantEntity[7]));
			baseDataConf.setpUnitPort(String.valueOf(companyPlantEntity[8]));
			baseDataConf.setpUnitNo(String.valueOf(companyPlantEntity[4]));
			
			baseDataConfigList.add(baseDataConf);
		}

		return baseDataConfigList;
	}
	
	/**
	 * Company - Plant Relation Reset
	 */
	@Transactional
	public void resetBaseDataConfList(List<SettingsBaseDataConfig> baseDataConfigList) {
		this.plantUnitRepository.deleteAllCompanyPlantUnit();	
		this.plantUnitRepository.deleteAllPlantUnit();
		
		String companyId = null;
		String pUnitId = null;
		String plantId = null;
		String sysId = null;
		String pUnitNo = null;
		int pUnitOrder = 0;
		String pUnitIp = null;
		String pUnitPort = null;
		String description = null;
		
		for (SettingsBaseDataConfig baseDataConfig : baseDataConfigList) {
			
			companyId = baseDataConfig.getCompanyId();
			pUnitId = baseDataConfig.getpUnitId();
			plantId = baseDataConfig.getPlantId();
			sysId = baseDataConfig.getSysId();
			pUnitNo = baseDataConfig.getpUnitNo();
			pUnitOrder = Integer.parseInt(pUnitNo);
			pUnitIp = baseDataConfig.getpUnitIp();
			pUnitPort = baseDataConfig.getpUnitPort();
			description = baseDataConfig.getDescription();
			
			this.plantUnitRepository.insertAllCompanyPlantUnit(companyId, pUnitId, plantId, sysId);
			this.plantUnitRepository.insertAllPlantUnit(pUnitId, pUnitNo, pUnitOrder, pUnitIp, description, sysId, pUnitPort);
		}
	}
	
	/**
	 * Retrieve Company List 
	 */
	public List<SettingsCompanyDataConfig> getCompanyDataConfList() {
		List<SettingsCompanyDataConfig> companyDataConfigList = new ArrayList<SettingsCompanyDataConfig>();
		List<Object[]> companyPlantEntityList = this.plantUnitRepository.findAllCompanyInfoNativeQuery();
		for(Object[] companyPlantEntity : companyPlantEntityList) {
			SettingsCompanyDataConfig companyDataConf = new SettingsCompanyDataConfig();
			
			companyDataConf.setCompanyId(String.valueOf(companyPlantEntity[0]));
			companyDataConf.setCompanyNm(String.valueOf(companyPlantEntity[1]));
			companyDataConf.setCompanyOrder(String.valueOf(companyPlantEntity[2]));
			companyDataConf.setDescription(String.valueOf(companyPlantEntity[3]));
			
			companyDataConfigList.add(companyDataConf);
		}

		return companyDataConfigList;
	}
	
	/**
	 * Company - Plant Relation Reset
	 */
	@Transactional
	public void resetCompanyDataConfList(List<SettingsCompanyDataConfig> companyDataConfigList) {
		this.plantUnitRepository.deleteAllCompany();	
		
		String companyId = null;
		String companyNm = null;
		int companyOrder = 0;
		String description = null;
		
		for (SettingsCompanyDataConfig companyDataConfig : companyDataConfigList) {
			
			companyId = companyDataConfig.getCompanyId();
			companyNm = companyDataConfig.getCompanyNm();
			companyOrder = Integer.parseInt(companyDataConfig.getCompanyOrder());
			description = companyDataConfig.getDescription();
			
			this.plantUnitRepository.insertAllCompany(companyId, companyNm, companyOrder, description);
		}
	}
	
	/**
	 * Retrieve Plant List 
	 */
	public List<SettingsPlantDataConfig> getPlantDataConfList() {
		List<SettingsPlantDataConfig> plantDataConfList = new ArrayList<SettingsPlantDataConfig>();
		List<Object[]> plantDataEntityList = this.plantUnitRepository.findAllPlantInfoNativeQuery();
		for(Object[] plantDataEntity : plantDataEntityList) {
			SettingsPlantDataConfig plantDataConf = new SettingsPlantDataConfig();
			
			plantDataConf.setPlantId(String.valueOf(plantDataEntity[0]));
			plantDataConf.setPlantNm(String.valueOf(plantDataEntity[1]));
			plantDataConf.setPlantOrder(String.valueOf(plantDataEntity[2]));
			plantDataConf.setDescription(String.valueOf(plantDataEntity[3]));
			
			plantDataConfList.add(plantDataConf);
		}

		return plantDataConfList;
	}
	
	/**
	 * Plant List Reset
	 */
	@Transactional
	public void resetPlantDataConfList(List<SettingsPlantDataConfig> plantDataConfigList) {
		this.plantUnitRepository.deleteAllPlant();	
		
		String plantId = null;
		String plantNm = null;
		int plantOrder = 0;
		String description = null;
		
		for (SettingsPlantDataConfig plantDataConfig : plantDataConfigList) {
			
			plantId = plantDataConfig.getPlantId();
			plantNm = plantDataConfig.getPlantNm();
			plantOrder = Integer.parseInt(plantDataConfig.getPlantOrder());
			description = plantDataConfig.getDescription();
			
			this.plantUnitRepository.insertAllPlant(plantId, plantNm, plantOrder, description);
		}
	}
	
	/**
	 * Retrieve Unit List 
	 */
	public List<SettingsUnitDataConfig> getUnitDataConfList() {
		List<SettingsUnitDataConfig> unitDataConfList = new ArrayList<SettingsUnitDataConfig>();
		List<Object[]> unitDataEntityList = this.plantUnitRepository.findAllUnitInfoNativeQuery();
		for(Object[] unitDataEntity : unitDataEntityList) {
			SettingsUnitDataConfig unitDataConf = new SettingsUnitDataConfig();
			
			unitDataConf.setpUnitId(String.valueOf(unitDataEntity[0]));
			unitDataConf.setpUnitNm(String.valueOf(unitDataEntity[1]));
			unitDataConf.setpUnitOrder(String.valueOf(unitDataEntity[2]));
			unitDataConf.setDescription(String.valueOf(unitDataEntity[3]));
			
			unitDataConfList.add(unitDataConf);
		}

		return unitDataConfList;
	}
	
	/**
	 * Unit List Reset
	 */
	@Transactional
	public void resetUnitDataConfList(List<SettingsUnitDataConfig> unitDataConfigList) {
		this.plantUnitRepository.deleteAllUnit();	
		
		String pUnitId = null;
		String pUnitNm = null;
		int pUnitOrder = 0;
		String description = null;
		
		for (SettingsUnitDataConfig unitDataConfig : unitDataConfigList) {
			
			pUnitId = unitDataConfig.getpUnitId();
			pUnitNm = unitDataConfig.getpUnitNm();
			pUnitOrder = Integer.parseInt(unitDataConfig.getpUnitOrder());
			description = unitDataConfig.getDescription();
			
			this.plantUnitRepository.insertAllUnit(pUnitId, pUnitNm, pUnitOrder, description);
		}
	}
}
