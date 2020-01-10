package dhi.optimizer.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import dhi.optimizer.enumeration.CtrDataIOType;
import dhi.optimizer.model.ConfDataVO;
import dhi.optimizer.model.OptCodeVO;
import dhi.optimizer.model.TagDataVO;

public interface CommonDataService {
	public HashMap<String, Double> getOpDataMap();
	
	public Double getOpDataMap(String tagId);
	
	public HashMap<String, Double> getOpDataMap(Collection<String> tagIds);
	
	public TagDataVO getOpDataConfMap(String tagId);
	
	public HashMap<String, TagDataVO> getOpDataConfMap(Collection<String> tagIds);
	
	public HashMap<String, TagDataVO> getOpDataTagMap();
	
	public HashMap<String, TagDataVO> getOpDataTagMap(Collection<String> tagIds);
	
	public HashMap<String, Double> getCtrDataMap(CtrDataIOType ctrDataIOType);
	
	public HashMap<String, TagDataVO> getCtrDataTagMap(CtrDataIOType ctrDataIOType);
	
	public HashMap<String, TagDataVO> getCtrDataTagMap(CtrDataIOType ctrDataIOType, Collection<String> tagIds);
	
	public HashMap<String, ConfDataVO> getConfDataMap(String plantUnitId, String confType);
	
	public void refConfDataMap(String plantUnitId, HashMap<String, HashMap<String, ConfDataVO>> refConfTypesMaps);
	
	public List<OptCodeVO> getOptCodeList(String groupId);
	
	public List<OptCodeVO> getOptCodeWithOPDataList(String groupId);
}
