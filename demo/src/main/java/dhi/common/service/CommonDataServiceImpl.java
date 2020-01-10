package dhi.common.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.common.model.ConfDataVO;
import dhi.common.model.TagDataVO;
import dhi.common.repository.CommonDataInputRepository;
import dhi.common.repository.ConfigInfRepository;

@Service
public class CommonDataServiceImpl implements CommonDataService{
	
	@Autowired 
	public CommonDataInputRepository commonDataInputRepository; 
	
	@Autowired 
	public ConfigInfRepository configInfRepository; 

	@Override
	public HashMap<TagDataVO, TagDataVO> getCommmonDataTagMap() {
		
		List<Object[]> opDataList = this.commonDataInputRepository.getAllLastCommonDataTag();
		HashMap<TagDataVO, TagDataVO> opDataMap = new HashMap<TagDataVO, TagDataVO>();
		for(Object[] tagInfo:opDataList) {
			
			TagDataVO tagDataKey = new TagDataVO();
			tagDataKey.setPlantUnitId(String.valueOf(tagInfo[0]));
			tagDataKey.setTagId(String.valueOf(tagInfo[1]));
			
			TagDataVO tagDataValue = new TagDataVO();
			tagDataValue.setTagVal(Double.parseDouble(String.valueOf(tagInfo[2])));
			tagDataValue.setUnit(String.valueOf(tagInfo[3]));			
			opDataMap.put(tagDataKey, tagDataValue);
		}
		
		return opDataMap;
	}
	
	@Override
	public HashMap<TagDataVO, TagDataVO> getCommmonDataTagMap(Collection<String> tagIds) {
		
		List<Object[]> opDataList = this.commonDataInputRepository.getLastCommonDataTag(tagIds);
		HashMap<TagDataVO, TagDataVO> opDataMap = new HashMap<TagDataVO, TagDataVO>();
		for(Object[] tagInfo:opDataList) {
			
			TagDataVO tagDataKey = new TagDataVO();
			tagDataKey.setPlantUnitId(String.valueOf(tagInfo[0]));
			tagDataKey.setTagId(String.valueOf(tagInfo[1]));
			
			TagDataVO tagDataValue = new TagDataVO();
			tagDataValue.setTagVal(Double.parseDouble(String.valueOf(tagInfo[2])));
			tagDataValue.setUnit(String.valueOf(tagInfo[3]));			
			opDataMap.put(tagDataKey, tagDataValue);
		}
		
		return opDataMap;
	}
	
	@Override 
	public HashMap<String, Double> getConfDataMap(String plantUnitId, String confType) {
		
		List<Object[]> confDataList = this.configInfRepository.getAllConfData(plantUnitId, confType);
		HashMap<String, Double> confDataMap = new HashMap<String, Double>();
		for(Object[] tagInfo:confDataList) {
			confDataMap.put(String.valueOf(tagInfo[0]), Double.parseDouble(String.valueOf(tagInfo[1])));			
		}
		
		return confDataMap;
	}
	
	@Override 
	public HashMap<ConfDataVO, Double> getConfDataMap(String confType) {
		
		List<Object[]> confDataList = this.configInfRepository.getAllConfData(confType);
		HashMap<ConfDataVO, Double> confDataMap = new HashMap<ConfDataVO, Double>();
		for(Object[] confData:confDataList) {
			
			ConfDataVO confDataKey = new ConfDataVO();
			confDataKey.setPlantUnitId(String.valueOf(confData[0]));
			confDataKey.setConfId(String.valueOf(confData[1]));
			
			confDataMap.put(confDataKey, Double.parseDouble(String.valueOf(confData[2])));
		}
		
		return confDataMap;
	}
}