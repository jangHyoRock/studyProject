package dhi.common.service;

import java.util.List;

import dhi.common.model.json.CommonData;
import dhi.common.model.json.KeyValue;
import dhi.common.model.json.LeftMenu;

/*
 * Common(LeftMenu, SystemInfo, Footer) service interface.
 */
public interface CommonService {

	public LeftMenu getLeftMenu(String systemId, String requestHost);
	
	public CommonData getCommonData(String systemId, String plantUnitId);
	
	public List<KeyValue> getPlantUnitDDL();
}
