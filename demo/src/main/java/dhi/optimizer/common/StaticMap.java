package dhi.optimizer.common;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.optimizer.model.OptCodeVO;
import dhi.optimizer.service.CommonDataService;

/**
 * Static Map Class. <br>
 * : 시스템 시작시 최초 DB에서 읽어  Map에 등록 후  메모리에서 영역에서 위한 Map Class. 
 */
@Service
public class StaticMap {

	@Autowired
	CommonDataService commonDataService;
	
	public StaticMap() {};
	
	public static LinkedHashMap<String, OptCodeVO> TrendCategoryMap = null;
	public static LinkedHashMap<String, OptCodeVO> NavigatorTrendCategoryMap = null;
	
	/**
	 * Initialize Map 함수
	 * : DB로 부터 필요한 Map 정보를 Load 한다.
	 */
	public void initializeMap() {
		
		// Initialize trend category map.
		if (TrendCategoryMap == null) {
			TrendCategoryMap = new LinkedHashMap<String, OptCodeVO>();
			List<OptCodeVO> optCodeList = this.commonDataService.getOptCodeWithOPDataList(CommonConst.OptCodeTrednCategoryGrouID);
			for (OptCodeVO optCode : optCodeList) {
				TrendCategoryMap.put(optCode.getCodeId(), optCode);
			}
		}
		
		// Initialize navigator trend category map.
		if (NavigatorTrendCategoryMap == null) {
			NavigatorTrendCategoryMap = new LinkedHashMap<String, OptCodeVO>();
			List<OptCodeVO> optCodeList = this.commonDataService.getOptCodeList(CommonConst.OptCodeNavigatorTrendCategoryGrouID);
			for (OptCodeVO optCode : optCodeList) {
				NavigatorTrendCategoryMap.put(optCode.getCodeId(), optCode);
			}
		}
	}
}
