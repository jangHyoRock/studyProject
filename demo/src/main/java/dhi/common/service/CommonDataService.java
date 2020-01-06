package dhi.common.service;

import java.util.Collection;
import java.util.HashMap;

import dhi.common.model.ConfDataVO;
import dhi.common.model.TagDataVO;

public interface CommonDataService {
	public HashMap<TagDataVO, TagDataVO> getCommmonDataTagMap();
	public HashMap<TagDataVO, TagDataVO> getCommmonDataTagMap(Collection<String> tagIds);
	public HashMap<String, Double> getConfDataMap(String plantUnitId, String confType);
	public HashMap<ConfDataVO, Double> getConfDataMap(String confType);
}
