package dhi.common.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dhi.common.enumeration.ControllerModeStatus;
import dhi.common.enumeration.OptimizerFunctionModeStatus;
import dhi.common.enumeration.PsoOptimizationFunctionModeStatus;
import dhi.common.model.ConfDataVO;
import dhi.common.model.TagDataVO;
import dhi.common.model.db.PlantUnitEntity;
import dhi.common.model.json.CommonData;
import dhi.common.model.json.FooterInfo;
import dhi.common.model.json.ItemStatus;
import dhi.common.model.json.KeyValue;
import dhi.common.model.json.LeftMenu;
import dhi.common.model.json.PlantInfo;
import dhi.common.repository.CommonDataInputRepository;
import dhi.common.repository.CompanyRepository;
import dhi.common.repository.PlantUnitRepository;
import dhi.common.util.CommonConst;
import dhi.common.util.Utilities;
/*
 * Common(LeftMenu, SystemInfo, Footer) service.
 */
@Service
public class CommonServiceImpl implements CommonService {

	private static final int TIME_INTERVAL = 10;
		
	@Autowired
	CommonDataService commonDataService;
	
	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	PlantUnitRepository plantUnitRepository;
	
	@Autowired
	CommonDataInputRepository commonDataInputRepository;
		
	public LeftMenu getLeftMenu(String systemId, String requestHost) {

		LeftMenu leftMenu = new LeftMenu();
		List<PlantInfo> companyList = new ArrayList<PlantInfo>();
		List<Object[]> companyToPlantEnitityList = this.companyRepository.findDistinctCompanyToPlantNativeQuery(systemId);
		for (Object[] companyToPlant : companyToPlantEnitityList) {

			PlantInfo company = new PlantInfo();
			company.setCompanyName(companyToPlant[0] + " " + companyToPlant[1]);
			
			List<PlantInfo> plantInfoList = new ArrayList<PlantInfo>();
			List<Object[]> plantUnitEntityList = this.plantUnitRepository.findByPlantUnitIdInCompanyPlantUnitBySystemIdAndPlantIdNativeQuery(systemId, companyToPlant[2].toString(), requestHost);
			for (Object[] objectPlantUnitEntityArray : plantUnitEntityList) {
				plantInfoList.add(new PlantInfo(String.valueOf(objectPlantUnitEntityArray[0]), String.valueOf(objectPlantUnitEntityArray[1]), String.valueOf(objectPlantUnitEntityArray[2]), String.valueOf(objectPlantUnitEntityArray[3])));
			}

			company.setPlantInfoList(plantInfoList);
			companyList.add(company);
		}

		leftMenu.setCompanyList(companyList);
		
		return leftMenu;
	}
	
	public CommonData getCommonData(String systemId, String plantUnitId) {
		
		CommonData commonData = new CommonData();
		
		if(CommonConst.SYSTEM_NAME_OPTIMIZER.equals(systemId)) {
			
			HashMap<ConfDataVO, Double> baseLineConfMap = this.commonDataService.getConfDataMap(CommonConst.CONFING_SETTING_TYPE_BASELINE);
			HashMap<ConfDataVO, Double> optConfMap = this.commonDataService.getConfDataMap(CommonConst.CONFING_SETTING_TYPE_OPTCONF);
			
			Collection<String> tagIds = new ArrayList<String>();
			tagIds.add(CommonConst.ECON_OUT_FLUE_GAS_O2_1_RIGHT);
			tagIds.add(CommonConst.ECON_OUT_FLUE_GAS_O2_2_RIGHT);
			tagIds.add(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_1_LEFT);
			tagIds.add(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_2_LEFT);
			tagIds.add(CommonConst.ECO_OUT_FLUE_GAS_CO_L);
			tagIds.add(CommonConst.ECO_OUT_FLUE_GAS_CO_R);
			tagIds.add(CommonConst.HRZN_FG_TEMP_L);
			tagIds.add(CommonConst.HRZN_FG_TEMP_R);
			tagIds.add(CommonConst.NOx);
			HashMap<TagDataVO, TagDataVO> commonDataMap = this.commonDataService.getCommmonDataTagMap(tagIds);
			
			if (commonDataMap.size() > 0) {
				List<PlantInfo> plantInfoList = new ArrayList<PlantInfo>();
				List<PlantUnitEntity> plantUnitEntityList = this.plantUnitRepository.findBySysIdOrderByPlantUnitOrderAscNativeQuery(systemId);
				for (PlantUnitEntity plantUnitEntity : plantUnitEntityList) {
					ConfDataVO confDataMapKey = new ConfDataVO(plantUnitEntity.getPlantUnitIdEntity().getPlantUnitId(), CommonConst.BASELINE_FLUE_GAS_O2_AVG);
					confDataMapKey.setPlantUnitId(plantUnitEntity.getPlantUnitIdEntity().getPlantUnitId());					
					double o2BaseLine = baseLineConfMap.get(confDataMapKey);
					
					confDataMapKey.setConfId(CommonConst.BASELINE_FLUE_GAS_CO_AVG);
					double coBaseLine = baseLineConfMap.get(confDataMapKey);
					
					confDataMapKey.setConfId(CommonConst.BASELINE_FLUE_GAS_TEMP_LR_DEV);
					double tempBaseLine = baseLineConfMap.get(confDataMapKey);
					
					confDataMapKey.setConfId(CommonConst.BASELINE_STACK_NOX_AVG);
					double noxBaseLine = baseLineConfMap.get(confDataMapKey);
					
					confDataMapKey.setConfId(CommonConst.OPTCONF_O2_STATUS_NORMAL_EXCESS_RATIO);
					double o2BaseLineExcessRatio = optConfMap.get(confDataMapKey);
					
					confDataMapKey.setConfId(CommonConst.OPTCONF_CO_STATUS_NORMAL_EXCESS_RATIO);
					double coBaseLineExcessRatio = optConfMap.get(confDataMapKey);
					
					confDataMapKey.setConfId(CommonConst.OPTCONF_TEMP_STATUS_NORMAL_EXCESS_RATIO);
					double tempBaseLineExcessRatio = optConfMap.get(confDataMapKey);
				
					confDataMapKey.setConfId(CommonConst.OPTCONF_NOX_STATUS_NORMAL_EXCESS_RATIO);
					double noxBaseLineExcessRatio = optConfMap.get(confDataMapKey);
					
					int o2ValueCount = 0;
					TagDataVO tagDataMapKey = new TagDataVO(plantUnitEntity.getPlantUnitIdEntity().getPlantUnitId(), CommonConst.ECON_OUT_FLUE_GAS_O2_1_RIGHT);					
					double o2Value = 0;
					if(commonDataMap.get(tagDataMapKey) != null) {
						o2Value = commonDataMap.get(tagDataMapKey).getTagVal();
						o2ValueCount++;
					}
					
					tagDataMapKey.setTagId(CommonConst.ECON_OUT_FLUE_GAS_O2_2_RIGHT);
					if(commonDataMap.get(tagDataMapKey) != null) {
						o2Value += commonDataMap.get(tagDataMapKey).getTagVal();
						o2ValueCount++;
					}
					
					tagDataMapKey.setTagId(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_1_LEFT);
					if(commonDataMap.get(tagDataMapKey) != null) {
						o2Value += commonDataMap.get(tagDataMapKey).getTagVal();
						o2ValueCount++;
					}
					
					tagDataMapKey.setTagId(CommonConst.ECON_OUT_FLUE_GAS_OXYGE_2_LEFT);
					if(commonDataMap.get(tagDataMapKey) != null) {
						o2Value += commonDataMap.get(tagDataMapKey).getTagVal();
						o2ValueCount++;
					}
					
					if(o2ValueCount != 0) {
						o2Value = o2Value / o2ValueCount;
					}
					
					
					double coValue = 0;
					
					int coValueCount = 0;
					tagDataMapKey.setTagId(CommonConst.ECO_OUT_FLUE_GAS_CO_L);
					if(commonDataMap.get(tagDataMapKey) != null) {
						coValue += commonDataMap.get(tagDataMapKey).getTagVal();
						coValueCount++;
					}
										
					tagDataMapKey.setTagId(CommonConst.ECO_OUT_FLUE_GAS_CO_R);
					if(commonDataMap.get(tagDataMapKey) != null) {
						coValue += commonDataMap.get(tagDataMapKey).getTagVal();
						coValueCount++;
					}				
					
					if(coValueCount != 0) {
						coValue = coValue / coValueCount;
					}
					
					
					tagDataMapKey.setTagId(CommonConst.HRZN_FG_TEMP_L);
					TagDataVO tempLTagDataVO = commonDataMap.get(tagDataMapKey);
					
					tagDataMapKey.setTagId(CommonConst.HRZN_FG_TEMP_R);
					TagDataVO tempRTagDataVO = commonDataMap.get(tagDataMapKey);
					
					tagDataMapKey.setTagId(CommonConst.NOx);
					TagDataVO noxTagDataVO = commonDataMap.get(tagDataMapKey);
		
					o2BaseLine += (o2BaseLine * (o2BaseLineExcessRatio * 0.01));
					coBaseLine += (coBaseLine * (coBaseLineExcessRatio * 0.01));
					tempBaseLine += (tempBaseLine * (tempBaseLineExcessRatio * 0.01));
					noxBaseLine += (noxBaseLine * (noxBaseLineExcessRatio * 0.01));
		
					double tempValue = 0;
					if(tempLTagDataVO != null && tempRTagDataVO != null) {
						tempValue = Math.abs(tempLTagDataVO.getTagVal() - tempRTagDataVO.getTagVal());
					}
				
					String currentStatus = CommonConst.SYSTEM_CURRENT_STATUS_NORMAL;
					
					double noxTagDataVal = 0;
					if(noxTagDataVO != null) {
						noxTagDataVal = noxTagDataVO.getTagVal();
					}
					if(o2BaseLine <= o2Value
							|| coBaseLine <= coValue
							|| tempBaseLine <= tempValue
							|| noxBaseLine <= noxTagDataVal)
						currentStatus = CommonConst.SYSTEM_CURRENT_STATUS_WARNING;
					
					plantInfoList.add(new PlantInfo(plantUnitEntity.getPlantUnitIdEntity().getPlantUnitId(), currentStatus));
				}
				
				commonData.setPlantUnitStatus(plantInfoList);
			}
			
			List<Object[]> objectFooterList = this.commonDataInputRepository.getCommonFooterData(
					CommonConst.ACTIVE_POWER_OF_GENERATOR, CommonConst.FREQUENCY_OF_GENERATOR, CommonConst.TOTAL_AIR_FLOW,
					CommonConst.FURNACE_DRAFT, CommonConst.TOTAL_COAL_FLOW, CommonConst.ECON_OUT_FLUE_GAS_O2_1_RIGHT,
					CommonConst.ECON_OUT_FLUE_GAS_O2_2_RIGHT, CommonConst.ECON_OUT_FLUE_GAS_OXYGE_1_LEFT,
					CommonConst.ECON_OUT_FLUE_GAS_OXYGE_2_LEFT);
			
			String combstOptUnit = OptimizerFunctionModeStatus.TRUE.getValue() + "/" + OptimizerFunctionModeStatus.FALSE.getValue();
			String ctrlModeUnit = ControllerModeStatus.CL.getValue() + "/" + ControllerModeStatus.OL.getValue();
			String optModeUnit = "PFT/EQT/EM";
			
			HashMap<String, String> plantUnitKey = new HashMap<String, String>();
			List<FooterInfo> footerInfoList = new ArrayList<FooterInfo>();
			for (Object[] objectFooter : objectFooterList) {
				String plantUnitName = String.valueOf(objectFooter[1]);
				plantUnitKey.put(String.valueOf(objectFooter[0]), plantUnitName);

				List<ItemStatus> footerItemList = new ArrayList<ItemStatus>();
				footerItemList.add(new ItemStatus("Load", String.valueOf(objectFooter[3]), Utilities.roundIntToString((double)objectFooter[2])));
				footerItemList.add(new ItemStatus("FREQ", String.valueOf(objectFooter[5]), Utilities.roundSecondToString((double)objectFooter[4])));
				footerItemList.add(new ItemStatus("AF", String.valueOf(objectFooter[7]), Utilities.roundIntToString((double)objectFooter[6])));
				footerItemList.add(new ItemStatus("FP", String.valueOf(objectFooter[9]), Utilities.roundIntToString((double)objectFooter[8])));
				footerItemList.add(new ItemStatus("CF", String.valueOf(objectFooter[11]), Utilities.roundIntToString((double)objectFooter[10])));
				footerItemList.add(new ItemStatus("O2", String.valueOf(objectFooter[13]), Utilities.roundFirstToString((double)objectFooter[12])));

				String combstOpt = "-";
				if (objectFooter[14] != null) {
					combstOpt = String.valueOf(objectFooter[14]);
					if ("1".equals(combstOpt))
						combstOpt = OptimizerFunctionModeStatus.TRUE.getValue();
					else
						combstOpt = OptimizerFunctionModeStatus.FALSE.getValue();
				}
				
				String ctrlMode = "-";
				if (objectFooter[15] != null) {
					ControllerModeStatus controlMode = ControllerModeStatus.valueOf(String.valueOf(objectFooter[15]));
					ctrlMode = controlMode.getValue();
				}

				String optMode = "-";
				if (objectFooter[16] != null) {
					PsoOptimizationFunctionModeStatus optModeStatus = PsoOptimizationFunctionModeStatus.valueOf(String.valueOf(objectFooter[16]));
					optMode = optModeStatus.getValue();
				}
				
				footerItemList.add(new ItemStatus("Combst Opt", combstOptUnit, combstOpt));
				footerItemList.add(new ItemStatus("Ctrl Mode", ctrlModeUnit, ctrlMode));
				footerItemList.add(new ItemStatus("Opt Mode", optModeUnit, optMode));
				footerInfoList.add(new FooterInfo(plantUnitName, footerItemList));
			}
			
			FooterInfo findFooterInfo = null;
			for (FooterInfo footerInfo : footerInfoList) {				
				String plantUnitName;		
				plantUnitName = plantUnitKey.get(plantUnitId);
				if(plantUnitName == null)
					plantUnitName = "";
				
				if (plantUnitName.equals(footerInfo.getPlant())) {
					findFooterInfo = footerInfo;
					break;
				}
			}
			
			if (findFooterInfo != null) {
				footerInfoList.remove(findFooterInfo);
				footerInfoList.add(0, findFooterInfo);
			}
			
			commonData.setAlwaysDisplay(footerInfoList);
		}
		else if(CommonConst.SYSTEM_NAME_BTMS.equals(systemId)) {
		}		
		
		commonData.setCurrentTime(this.commonDataInputRepository.getNowTimestamp(TIME_INTERVAL));
		return commonData;
	}
	
	public List<KeyValue> getPlantUnitDDL() {		
		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		List<Object[]> plantUnitEntityList = this.plantUnitRepository.findPlantUnitIdListNativeQuery();
		for (Object[] plantUnitEntity : plantUnitEntityList) {
			keyValueList.add(new KeyValue(plantUnitEntity[0].toString(), plantUnitEntity[1].toString()));
		}

		return keyValueList;
	}
}
