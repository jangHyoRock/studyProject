package dhi.optimizer.service;

import java.util.List;

import dhi.common.exception.InvalidParameterException;

import dhi.optimizer.model.json.Chart;
import dhi.optimizer.model.json.KeyValue;

public interface TrendService {

	public List<KeyValue> getCategoryDDL();
	
	public Chart getTrendOPData(String items, String startDateString, String endDateString) throws InvalidParameterException;
	
	public Chart getTrendOPDataAll(String startDateString, String endDateString) throws InvalidParameterException;
}
