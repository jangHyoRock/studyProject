package dhi.optimizer.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dhi.common.exception.InvalidParameterException;

import dhi.optimizer.common.CommonConst;
import dhi.optimizer.common.StaticMap;
import dhi.optimizer.enumeration.ControllerModeStatus;
import dhi.optimizer.model.OptCodeVO;
import dhi.optimizer.model.db.ControlHistoryEntity;
import dhi.optimizer.model.json.Chart;
import dhi.optimizer.model.json.KeyValue;
import dhi.optimizer.model.json.Optimizer;
import dhi.optimizer.model.json.Tag;
import dhi.optimizer.repository.ControlHistoryRepository;
import dhi.optimizer.repository.OPDataConfRepository;
import dhi.optimizer.repository.OPDataInputRepository;

@Service
@Transactional
public class TrendServiceImpl implements TrendService {

	@Autowired
	OPDataInputRepository opDataInputRepository;
	
	@Autowired
	OPDataConfRepository opDataConfRepository;
	
	@Autowired
	ControlHistoryRepository controlHistoryRepository;
	
	public List<KeyValue> getCategoryDDL() {
		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		for(Map.Entry<String, OptCodeVO> entry:StaticMap.TrendCategoryMap.entrySet()) {
			OptCodeVO optCode = entry.getValue();
			keyValueList.add(new KeyValue(optCode.getCodeId(), optCode.getCodeNm()));
		}
		
		return keyValueList;
	}
	
	public Chart getTrendOPData(String items, String startDateString, String endDateString) throws InvalidParameterException {
		String[] itemArray = items.split(",");
		boolean isMultiTag = itemArray.length > 1 ? true : false;
		
		Chart chart = new Chart();
		try {
			
			String trendTag1Item = itemArray[0];
			String trendTag2Item = isMultiTag ? itemArray[1] : CommonConst.StringEmpty;

			OptCodeVO tag1OptCodeVo = StaticMap.TrendCategoryMap.get(trendTag1Item);
			OptCodeVO tag2OptCodeVo = isMultiTag ? StaticMap.TrendCategoryMap.get(trendTag2Item) : new OptCodeVO();
			
			String trendTagNm1 = tag1OptCodeVo.getTagNm();
			String trendTagNm2 = tag2OptCodeVo.getTagNm();

			ArrayList<Tag> taglist = new ArrayList<Tag>();
			Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateString);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateString);
			
			int SecondsInterval = 30;
			long diff = endDate.getTime() - startDate.getTime();
			long diffSeconds = diff / 1000;
			int secModNumber = (int)((diffSeconds / CommonConst.DATE_SECONDS_OF_DAY ) + 1) * SecondsInterval;
			
			List<Object[]> opDataEntityList = this.opDataInputRepository.findBy2TagAndTimestampSecModNativeQuery(trendTagNm1, trendTagNm2, startDate, endDate, secModNumber);
			ArrayList<HashMap<String, Object>> listObj1 = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> listObj2 = new ArrayList<HashMap<String, Object>>();
			for (Object[] opPDataEntity : opDataEntityList) {
				
				Date trendDate = (Date) opPDataEntity[0];
				HashMap<String, Object> trendObject1 = new HashMap<String, Object>();
				HashMap<String, Object> trendObject2 = new HashMap<String, Object>();

				trendObject1.put(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDate), String.valueOf(opPDataEntity[1]));
				trendObject2.put(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDate),	String.valueOf(opPDataEntity[2]));
				
				listObj1.add(trendObject1);
				listObj2.add(trendObject2);
			}
			
			Tag tag1 = new Tag();
			tag1.setName(tag1OptCodeVo.getCodeNm());
			tag1.setUnit(tag1OptCodeVo.getUnit());
			tag1.setTrend(listObj1);
			taglist.add(tag1);
			
			if (isMultiTag) {
				Tag tag2 = new Tag();
				tag2.setName(tag2OptCodeVo.getCodeNm());
				tag2.setUnit(tag2OptCodeVo.getUnit());
				tag2.setTrend(listObj2);
				taglist.add(tag2);
			}
			
			chart.setTag(taglist.toArray(new Tag[taglist.size()]));

			ArrayList<Optimizer> optimizerlist = new ArrayList<Optimizer>();
			List<ControlHistoryEntity> controlHistoryEntityList = controlHistoryRepository.findByTimestampNativeQuery(startDate, endDate);
			ControlHistoryEntity recentControlHistoryEntityBeforeStartDate = controlHistoryRepository.findByRecentDataFromControlHistNativeQuery(startDate);
			boolean isOptimizerOn = false;
			boolean isOptimizerOnHistory = false;
			Date trendDataHistory = startDate;
			Date trendData = startDate;
			Date recentTimestampFromControlHist = recentControlHistoryEntityBeforeStartDate != null ? recentControlHistoryEntityBeforeStartDate.getTimestamp() : startDate;
			

			if (controlHistoryEntityList.size() > 0) {
				
				ControlHistoryEntity recentControlHistoryEntityBeforeNowDate = null;
				String recentControlModeFromControlHist = null;
				
				for (int i = 0; i < controlHistoryEntityList.size(); i++) {
					ControlHistoryEntity controlHistoryEntity = controlHistoryEntityList.get(i);
					
					
					if(i == 0) {
						recentControlHistoryEntityBeforeNowDate = controlHistoryRepository.findByRecentDataFromControlHistNativeQuery(controlHistoryEntity.getTimestamp());
						recentControlModeFromControlHist = recentControlHistoryEntityBeforeNowDate != null ? recentControlHistoryEntityBeforeNowDate.getControlMode() : "OL";
					} 
					
					
					if (controlHistoryEntity.getSystemStart() && ControllerModeStatus.CL.name().equals(recentControlModeFromControlHist)) {
						isOptimizerOnHistory = true;
					} else {
						isOptimizerOnHistory = false;
					}
					
					if(controlHistoryEntity.getSystemStart() && ControllerModeStatus.CL.name().equals(controlHistoryEntity.getControlMode())) {
						isOptimizerOn = true;
					} else {
						isOptimizerOn = false;
					}
						
					trendData = controlHistoryEntity.getTimestamp();
					
					if (i == 0) {
						trendDataHistory = startDate;
					}

					if (i == controlHistoryEntityList.size() - 1) {
						Optimizer optimizer1 = new Optimizer();
						optimizer1.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDataHistory));
						optimizer1.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendData));
						optimizer1.setStatus(isOptimizerOnHistory == true ? 1 : 0);
						optimizerlist.add(optimizer1);						
						
						Optimizer optimizer2 = new Optimizer();
						optimizer2.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendData));
						optimizer2.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(endDate));
						optimizer2.setStatus(isOptimizerOn == true ? 1 : 0);
						optimizerlist.add(optimizer2);
					}
					else if (isOptimizerOnHistory != isOptimizerOn) {

						Optimizer optimizer = new Optimizer();
						optimizer.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDataHistory));
						optimizer.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendData));
						optimizer.setStatus(isOptimizerOnHistory == true ? 1 : 0);	
						optimizerlist.add(optimizer);

						isOptimizerOnHistory = isOptimizerOn;
						trendDataHistory = trendData;
					}
					
					recentControlModeFromControlHist = controlHistoryEntity.getControlMode();
				}
			} else if ((recentTimestampFromControlHist.before(startDate)
					|| recentTimestampFromControlHist.equals(startDate))
					&& recentControlHistoryEntityBeforeStartDate != null
					&& recentControlHistoryEntityBeforeStartDate.getControlMode().equals("CL")) {
				Optimizer optimizer = new Optimizer();
				optimizer.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(startDate));
				optimizer.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(endDate));
				optimizer.setStatus(1);
				optimizerlist.add(optimizer);
			} else {
				Optimizer optimizer = new Optimizer();
				optimizer.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(startDate));
				optimizer.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(endDate));
				optimizer.setStatus(0);
				optimizerlist.add(optimizer);
			}

			chart.setOptimizer(optimizerlist.toArray(new Optimizer[optimizerlist.size()]));
			
		} catch (ParseException e) {
			throw new InvalidParameterException(e.getMessage());
		}
		
		return chart;
	}
	
	@SuppressWarnings("unchecked")
	public Chart getTrendOPDataAll(String startDateString, String endDateString) throws InvalidParameterException {
				
		Chart chart = new Chart();
		try {

			ArrayList<Tag> taglist = new ArrayList<Tag>();
			Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateString);
			Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateString);

			int SecondsInterval = 30;
			long diff = endDate.getTime() - startDate.getTime();
			long diffSeconds = diff / 1000;
			int secModNumber = (int) ((diffSeconds / CommonConst.DATE_SECONDS_OF_DAY) + 1) * SecondsInterval;

			List<Object[]> opDataEntityList = this.opDataInputRepository.findByAllTagAndTimestampSecModNativeQuery(startDate, endDate, secModNumber);

			int idx = 0;
			Object[] arrTrendObjectList = new Object[StaticMap.TrendCategoryMap.entrySet().size()];
			for (Entry<String, OptCodeVO> trendCategoryMap : StaticMap.TrendCategoryMap.entrySet()) {
				Tag tag = new Tag();
				tag.setName(trendCategoryMap.getValue().getCodeNm());
				tag.setUnit(trendCategoryMap.getValue().getUnit());
				taglist.add(tag);

				arrTrendObjectList[idx] = new ArrayList<HashMap<String, Object>>();
				idx++;
			}

			for (Object[] opPDataEntity : opDataEntityList) {

				Date trendDate = (Date) opPDataEntity[0];
				String[] tagVals = String.valueOf(opPDataEntity[1]).split(",");

				for (int i = 0; i < tagVals.length; i++) {
					HashMap<String, Object> trendObject = new HashMap<String, Object>();
					trendObject.put(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDate), String.valueOf(tagVals[i]));

					ArrayList<HashMap<String, Object>> listTrendObject = (ArrayList<HashMap<String, Object>>) arrTrendObjectList[i];
					listTrendObject.add(trendObject);
				}
			}

			for (int i = 0; i < StaticMap.TrendCategoryMap.entrySet().size(); i++) {
				taglist.get(i).setTrend((ArrayList<HashMap<String, Object>>) arrTrendObjectList[i]);
			}

			chart.setTag(taglist.toArray(new Tag[taglist.size()]));

			ArrayList<Optimizer> optimizerlist = new ArrayList<Optimizer>();
			List<ControlHistoryEntity> controlHistoryEntityList = controlHistoryRepository.findByTimestampNativeQuery(startDate, endDate);
			ControlHistoryEntity recentControlHistoryEntityBeforeStartDate = controlHistoryRepository.findByRecentDataFromControlHistNativeQuery(startDate);
			boolean isOptimizerOn = false;
			boolean isOptimizerOnHistory = false;
			Date trendDataHistory = startDate;
			Date trendData = startDate;
			Date recentTimestampFromControlHist = recentControlHistoryEntityBeforeStartDate != null
					? recentControlHistoryEntityBeforeStartDate.getTimestamp()
					: startDate;

			if (controlHistoryEntityList.size() > 0) {
				
				ControlHistoryEntity recentControlHistoryEntityBeforeNowDate = null;
				String recentControlModeFromControlHist = null;
				
				for (int i = 0; i < controlHistoryEntityList.size(); i++) {
					ControlHistoryEntity controlHistoryEntity = controlHistoryEntityList.get(i);
					
					if(i == 0) {
						recentControlHistoryEntityBeforeNowDate = controlHistoryRepository.findByRecentDataFromControlHistNativeQuery(controlHistoryEntity.getTimestamp());
						recentControlModeFromControlHist = recentControlHistoryEntityBeforeNowDate != null ? recentControlHistoryEntityBeforeNowDate.getControlMode() : "OL";
					} 
					
					
					if (controlHistoryEntity.getSystemStart() && ControllerModeStatus.CL.name().equals(recentControlModeFromControlHist)) {
						isOptimizerOnHistory = true;
					} else {
						isOptimizerOnHistory = false;
					}
					
					if(controlHistoryEntity.getSystemStart() && ControllerModeStatus.CL.name().equals(controlHistoryEntity.getControlMode())) {
						isOptimizerOn = true;
					} else {
						isOptimizerOn = false;
					}
						
					trendData = controlHistoryEntity.getTimestamp();
					
					if (i == 0) {
						trendDataHistory = startDate;
					}

					if (i == controlHistoryEntityList.size() - 1) {
						Optimizer optimizer1 = new Optimizer();
						optimizer1.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDataHistory));
						optimizer1.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendData));
						optimizer1.setStatus(isOptimizerOnHistory == true ? 1 : 0);
						optimizerlist.add(optimizer1);						
						
						Optimizer optimizer2 = new Optimizer();
						optimizer2.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendData));
						optimizer2.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(endDate));
						optimizer2.setStatus(isOptimizerOn == true ? 1 : 0);
						optimizerlist.add(optimizer2);
					}
					else if (isOptimizerOnHistory != isOptimizerOn) {

						Optimizer optimizer = new Optimizer();
						optimizer.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendDataHistory));
						optimizer.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(trendData));
						optimizer.setStatus(isOptimizerOnHistory == true ? 1 : 0);	
						optimizerlist.add(optimizer);

						isOptimizerOnHistory = isOptimizerOn;
						trendDataHistory = trendData;
					}
					
					recentControlModeFromControlHist = controlHistoryEntity.getControlMode();
				}
			} else if ((recentTimestampFromControlHist.before(startDate)  || recentTimestampFromControlHist.equals(startDate)) && recentControlHistoryEntityBeforeStartDate.getControlMode().equals("CL")) {
				Optimizer optimizer = new Optimizer();
				optimizer.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(startDate));
				optimizer.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(endDate));
				optimizer.setStatus(1);
				optimizerlist.add(optimizer);
			} else {
				Optimizer optimizer = new Optimizer();
				optimizer.setStart_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(startDate));
				optimizer.setEnd_date(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(endDate));
				optimizer.setStatus(0);
				optimizerlist.add(optimizer);
			}

			chart.setOptimizer(optimizerlist.toArray(new Optimizer[optimizerlist.size()]));
			
		} catch (ParseException e) {
			throw new InvalidParameterException(e.getMessage());
		}
		
		return chart;
	}	
}
