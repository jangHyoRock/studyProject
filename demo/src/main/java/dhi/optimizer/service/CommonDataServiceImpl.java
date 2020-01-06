package dhi.optimizer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.optimizer.common.repository.CommonControlRepository;
import dhi.optimizer.common.repository.ConfInfRepository;
import dhi.optimizer.enumeration.CtrDataIOType;
import dhi.optimizer.model.ConfDataVO;
import dhi.optimizer.model.OptCodeVO;
import dhi.optimizer.model.TagDataVO;
import dhi.optimizer.repository.CtrDataInputRepository;
import dhi.optimizer.repository.CtrDataOutputRepository;
import dhi.optimizer.repository.OPDataConfRepository;
import dhi.optimizer.repository.OPDataInputRepository;
import dhi.optimizer.repository.OptCodeInfRepository;

@Service
public class CommonDataServiceImpl implements CommonDataService{

	@Autowired
	public OPDataConfRepository opDataConfRepository;
	
	@Autowired 
	public OPDataInputRepository opDataInputRepository;
	
	@Autowired
	public CtrDataInputRepository ctrDataInputRepository;
	
	@Autowired
	public CtrDataOutputRepository ctrDataOutputRepository;
	
	@Autowired 
	public ConfInfRepository confInfRepository;
	
	@Autowired 
	public OptCodeInfRepository optCodeInfRepository; 
	
	@Autowired
	public CommonControlRepository ControlRepository;
		
	@Override
	public TagDataVO getOpDataConfMap(String tagId) {
		Collection<String> tagIds = new ArrayList<String>();
		tagIds.add(tagId);
		
		HashMap<String, TagDataVO> opDataConfMap = this.getOpDataConfMap(tagIds);
		return (TagDataVO)opDataConfMap.get(tagId);
	}
	
	@Override
	public HashMap<String, TagDataVO> getOpDataConfMap(Collection<String> tagIds) {
		List<Object[]> opDataConfList = this.opDataConfRepository.getOpDataConf(tagIds);
		HashMap<String, TagDataVO> opDataMap = new HashMap<String, TagDataVO>();
		for(Object[] tagInfo:opDataConfList) {
			TagDataVO tagDataVO = new TagDataVO();
			tagDataVO.setTagId(String.valueOf(tagInfo[0]));
			tagDataVO.setTagNm(String.valueOf(tagInfo[1]));
			tagDataVO.setUnit(String.valueOf(tagInfo[2]));
			opDataMap.put(String.valueOf(tagInfo[0]), tagDataVO);
		}
		
		return opDataMap;
	}
	
	@Override
	public HashMap<String, Double> getOpDataMap() {		
		List<Object[]> opDataList = this.opDataInputRepository.getAllLastOpData();
		HashMap<String, Double> opDataMap = new HashMap<String, Double>();
		for(Object[] tagInfo:opDataList) {
			opDataMap.put(String.valueOf(tagInfo[0]), tagInfo[1] != null ? Double.parseDouble(String.valueOf(tagInfo[1])) : 0);
		}
		
		return opDataMap;
	}
	
	@Override
	public Double getOpDataMap(String tagId) {	
		Collection<String> tagIds = new ArrayList<String>();
		tagIds.add(tagId);
		
		HashMap<String, Double> opDataMap = this.getOpDataMap(tagIds);
		return (Double)opDataMap.get(tagId);
	}
	
	@Override
	public HashMap<String, Double> getOpDataMap(Collection<String> tagIds) {
		List<Object[]> opDataList = this.opDataInputRepository.getAllLastOpData(tagIds);
		HashMap<String, Double> opDataMap = new HashMap<String, Double>();
		for(Object[] tagInfo:opDataList) {
			opDataMap.put(String.valueOf(tagInfo[0]), tagInfo[1] != null ? Double.parseDouble(String.valueOf(tagInfo[1])) : 0);
		}
		
		return opDataMap;
	}
	
	@Override
	public HashMap<String, TagDataVO> getOpDataTagMap() {		
		List<Object[]> opDataList = this.opDataInputRepository.getAllLastOpDataTag();
		HashMap<String, TagDataVO> opDataMap = new HashMap<String, TagDataVO>();
		for(Object[] tagInfo:opDataList) {
			TagDataVO tagDataVO = new TagDataVO();
			tagDataVO.setTagId(String.valueOf(tagInfo[0]));
			tagDataVO.setTagNm(String.valueOf(tagInfo[1]));
			tagDataVO.setTagVal(tagInfo[2] != null ? Double.parseDouble(String.valueOf(tagInfo[2])) : 0);
			tagDataVO.setUnit(String.valueOf(tagInfo[3]));		
			tagDataVO.setTimeStamp((Date)tagInfo[4]);
			opDataMap.put(String.valueOf(tagInfo[0]), tagDataVO);
		}
		
		return opDataMap;
	}
	
	@Override
	public HashMap<String, TagDataVO> getOpDataTagMap(Collection<String> tagIds) {		
		List<Object[]> opDataList = this.opDataInputRepository.getAllLastOpDataTag(tagIds);
		HashMap<String, TagDataVO> opDataMap = new HashMap<String, TagDataVO>();
		for(Object[] tagInfo:opDataList) {
			TagDataVO tagDataVO = new TagDataVO();
			tagDataVO.setTagId(String.valueOf(tagInfo[0]));
			tagDataVO.setTagNm(String.valueOf(tagInfo[1]));
			tagDataVO.setTagVal(tagInfo[2] != null ? Double.parseDouble(String.valueOf(tagInfo[2])): 0);
			tagDataVO.setUnit(String.valueOf(tagInfo[3]));			
			opDataMap.put(String.valueOf(tagInfo[0]), tagDataVO);
		}
		
		return opDataMap;
	}
	
	@Override
	public HashMap<String, Double> getCtrDataMap(CtrDataIOType ctrDataIOType) {		
		List<Object[]> ctrDataList = null;
		switch (ctrDataIOType) {
		case Input:
			ctrDataList = this.ctrDataInputRepository.getAllLastCtrDataInput();
			break;
		case Output:
			ctrDataList = this.ctrDataOutputRepository.getAllLastCtrDataOutput();
			break;
		}

		HashMap<String, Double> ctrDataMap = new HashMap<String, Double>();
		for (Object[] tagInfo : ctrDataList) {
			ctrDataMap.put(String.valueOf(tagInfo[0]), tagInfo[1] != null ? Double.parseDouble(String.valueOf(tagInfo[1])): 0);
		}

		return ctrDataMap;
	}
	
	@Override
	public HashMap<String, TagDataVO> getCtrDataTagMap(CtrDataIOType ctrDataIOType) {		
		List<Object[]> ctrDataList = null;
		switch (ctrDataIOType) {
		case Input:
			ctrDataList = this.ctrDataInputRepository.getAllLastCtrDataInputTag();
			break;
		case Output:
			ctrDataList = this.ctrDataOutputRepository.getAllLastCtrDataOutputTag();
			break;
		}
		
		HashMap<String, TagDataVO> ctrDataMap = new HashMap<String, TagDataVO>();
		for(Object[] tagInfo:ctrDataList) {
			TagDataVO tagDataVO = new TagDataVO();
			tagDataVO.setTagId(String.valueOf(tagInfo[0]));
			tagDataVO.setTagNm(String.valueOf(tagInfo[1]));
			tagDataVO.setTagVal(tagInfo[2] != null ? Double.parseDouble(String.valueOf(tagInfo[2])) : 0 );
			tagDataVO.setUnit(String.valueOf(tagInfo[3]));		
			tagDataVO.setTimeStamp((Date)tagInfo[4]);
			ctrDataMap.put(String.valueOf(tagInfo[0]), tagDataVO);
		}
		
		return ctrDataMap;
	}
	
	@Override
	public HashMap<String, TagDataVO> getCtrDataTagMap(CtrDataIOType ctrDataIOType, Collection<String> tagIds) {
		List<Object[]> ctrDataList = null;
		switch (ctrDataIOType) {
		case Input:
			ctrDataList = this.ctrDataInputRepository.getAllLastCtrDataInputTag(tagIds);
			break;
		case Output:
			ctrDataList = this.ctrDataOutputRepository.getAllLastCtrDataOutputTag(tagIds);
			break;
		}
		
		HashMap<String, TagDataVO> ctrDataMap = new HashMap<String, TagDataVO>();
		for(Object[] tagInfo:ctrDataList) {
			TagDataVO tagDataVO = new TagDataVO();
			tagDataVO.setTagId(String.valueOf(tagInfo[0]));
			tagDataVO.setTagNm(String.valueOf(tagInfo[1]));
			tagDataVO.setTagVal(tagInfo[2] != null ? Double.parseDouble(String.valueOf(tagInfo[2])): 0);
			tagDataVO.setUnit(String.valueOf(tagInfo[3]));		
			tagDataVO.setTimeStamp((Date)tagInfo[4]);
			ctrDataMap.put(String.valueOf(tagInfo[0]), tagDataVO);
		}
		
		return ctrDataMap;
	}
	
	@Override 
	public HashMap<String, ConfDataVO> getConfDataMap(String plantUnitId, String confType) {
		List<Object[]> confDataList = this.confInfRepository.getAllConfData(plantUnitId, confType);
		HashMap<String, ConfDataVO> confDataMap = new HashMap<String, ConfDataVO>();
		for(Object[] tagInfo:confDataList) {
			ConfDataVO confDataVO = new ConfDataVO();
			confDataVO.setConfId(String.valueOf(tagInfo[0]));
			confDataVO.setConfNm(String.valueOf(tagInfo[1]));
			confDataVO.setConfVal(tagInfo[2] != null ? Double.parseDouble(String.valueOf(tagInfo[2])) : 0);
			confDataVO.setUnit(String.valueOf(tagInfo[3]));
			confDataMap.put(String.valueOf(tagInfo[0]), confDataVO);
		}
		
		return confDataMap;
	}
	
	@Override 
	public void refConfDataMap(String plantUnitId, HashMap<String, HashMap<String, ConfDataVO>> refConfTypesMaps) {
		Collection<String> confTypes = new ArrayList<String>();		
		for(Entry<String, HashMap<String, ConfDataVO>> refConfTypesMap : refConfTypesMaps.entrySet()) {
			confTypes.add(refConfTypesMap.getKey());
		}
		
		List<Object[]> confDataList = this.confInfRepository.getAllConfData(plantUnitId, confTypes);
		for (Entry<String, HashMap<String, ConfDataVO>> refConfTypesMap : refConfTypesMaps.entrySet()) {
			HashMap<String, ConfDataVO> confDataMap = new HashMap<String, ConfDataVO>();
			for (Object[] tagInfo : confDataList) {				
				if (refConfTypesMap.getKey().equals(String.valueOf(tagInfo[4]))) {
					ConfDataVO confDataVO = new ConfDataVO();
					confDataVO.setConfId(String.valueOf(tagInfo[0]));
					confDataVO.setConfNm(String.valueOf(tagInfo[1]));
					confDataVO.setConfVal(tagInfo[2] != null ? Double.parseDouble(String.valueOf(tagInfo[2])) : 0);
					confDataVO.setUnit(String.valueOf(tagInfo[3]));
					confDataMap.put(String.valueOf(tagInfo[0]), confDataVO);
				}
			}
			
			refConfTypesMap.setValue(confDataMap);
		}
	}
	
	public List<OptCodeVO> getOptCodeList(String groupId) {
		List<OptCodeVO> optCodeList = new ArrayList<OptCodeVO>();
		List<Object[]> optCodeObjectList = this.optCodeInfRepository.findByGroupIdOrderByCodeOrderAsc(groupId);
		for (Object[] optCodeObject : optCodeObjectList) {
			OptCodeVO optCodeVO = new OptCodeVO();
			optCodeVO.setCodeId(String.valueOf(optCodeObject[0]));
			optCodeVO.setCodeNm(String.valueOf(optCodeObject[1]));
			optCodeList.add(optCodeVO);
		}
		
		return optCodeList;
	}
	
	public List<OptCodeVO> getOptCodeWithOPDataList(String groupId) {
		List<OptCodeVO> optCodeList = new ArrayList<OptCodeVO>();
		List<Object[]> optCodeObjectList = this.optCodeInfRepository.findWithOPDataByGroupID(groupId);
		for (Object[] optCodeObject : optCodeObjectList) {
			OptCodeVO optCodeVO = new OptCodeVO();
			optCodeVO.setCodeId(String.valueOf(optCodeObject[0]));
			optCodeVO.setCodeNm(String.valueOf(optCodeObject[1]));
			optCodeVO.setTagNm(String.valueOf(optCodeObject[2]));
			optCodeVO.setUnit(String.valueOf(optCodeObject[3]));
			optCodeList.add(optCodeVO);
		}
		
		return optCodeList;
	}	
}