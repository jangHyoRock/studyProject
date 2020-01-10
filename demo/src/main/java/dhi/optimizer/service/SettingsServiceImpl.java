package dhi.optimizer.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dhi.optimizer.common.repository.CommonDataConfRepository;
import dhi.optimizer.model.db.CommonDataConfEntity;
import dhi.optimizer.model.db.CommonDataConfIdEntity;
import dhi.optimizer.model.db.CtrDataConfEntity;
import dhi.optimizer.model.db.NNTrainDataConfEntity;
import dhi.optimizer.model.db.OPDataConfEntity;
import dhi.optimizer.model.db.PSOMVInfoEntity;
import dhi.optimizer.model.json.SettingsNNTrainDataConfig;
import dhi.optimizer.model.json.SettingsOpDataConfig;
import dhi.optimizer.model.json.SettingsPsoMVInfoConfig;
import dhi.optimizer.repository.CtrDataConfRepository;
import dhi.optimizer.repository.NNModelBaseRepository;
import dhi.optimizer.repository.NNModelRepository;
import dhi.optimizer.repository.NNTrainDataConfRepository;
import dhi.optimizer.repository.OPDataConfRepository;
import dhi.optimizer.repository.PSOMVInfoRepository;

/*
 * Settings Service.
 */
@Service
@Transactional
public class SettingsServiceImpl implements SettingsService {

	private final String TAG_DATA_SORT_COLUMN = "tagNo";
	private final String COMMON_PLANT_UNIT_ID_TAG_DATA_SORT_COLUMN = "commonDataConfIdEntity.plantUnitId";
	private final String MV_DATA_SORT_COLUMN = "psoMVOrder";
	
	@PersistenceContext
	private EntityManager em;
	
	@PersistenceContext(unitName= "commonEntityManagerFactory")
	private EntityManager commonEM;
	
	@Autowired
	private OPDataConfRepository opDataConfRepository;
	
	@Autowired
	private CtrDataConfRepository ctrDataConfRepository;
	
	@Autowired
	private CommonDataConfRepository commonDataConfRepository;
	
	@Autowired
	private NNTrainDataConfRepository nnTrainDataConfRepository;
	
	@Autowired	
	private PSOMVInfoRepository psoMVInfoRepository;
	
	@Autowired
	private NNModelBaseRepository nnModelBaseRepository;
	
	@Autowired
	private NNModelRepository nnModelRepository;
	
	public List<SettingsOpDataConfig> getOpDataConfList() {
		List<SettingsOpDataConfig> opDataConfigList = new ArrayList<SettingsOpDataConfig>();
		List<OPDataConfEntity> opDataConfEntityList = this.opDataConfRepository.findAll(new Sort(Sort.Direction.ASC, TAG_DATA_SORT_COLUMN));
		for (OPDataConfEntity opataConfEntity : opDataConfEntityList) {
			SettingsOpDataConfig opDataConfig = new SettingsOpDataConfig();
			opDataConfig.setTagId(opataConfEntity.getTagId());
			opDataConfig.setTagNm(opataConfEntity.getTagNm());
			opDataConfig.setDescription(opataConfEntity.getDescription());
			opDataConfig.setMinRaw(opataConfEntity.getMinRaw());
			opDataConfig.setMaxRaw(opataConfEntity.getMaxRaw());
			opDataConfig.setUnit(opataConfEntity.getUnit());
			opDataConfig.setMinEu(opataConfEntity.getMinEu());
			opDataConfig.setMaxEu(opataConfEntity.getMaxEu());
			opDataConfig.setPlantUnitId(opataConfEntity.getPlantUnitId());
			opDataConfig.setTagNo(opataConfEntity.getTagNo());
			opDataConfigList.add(opDataConfig);
		}

		return opDataConfigList;
	}
	
	@Transactional
	public void resetOpDataConf(List<SettingsOpDataConfig> opDataConfigList) {
		this.opDataConfRepository.deleteAll();		
		for (SettingsOpDataConfig opDataConfig : opDataConfigList) {
			OPDataConfEntity opDataConfEntity = new OPDataConfEntity();
			opDataConfEntity.setTagId(opDataConfig.getTagId());
			opDataConfEntity.setTagNm(opDataConfig.getTagNm());
			opDataConfEntity.setDescription(opDataConfig.getDescription());
			opDataConfEntity.setMinRaw(opDataConfig.getMinRaw());
			opDataConfEntity.setMaxRaw(opDataConfig.getMaxRaw());
			opDataConfEntity.setUnit(opDataConfig.getUnit());
			opDataConfEntity.setMinEu(opDataConfig.getMinEu());
			opDataConfEntity.setMaxEu(opDataConfig.getMaxEu());
			opDataConfEntity.setPlantUnitId(opDataConfig.getPlantUnitId());
			opDataConfEntity.setTagNo(opDataConfig.getTagNo());
			em.persist(opDataConfEntity);		
		}

		em.flush();
		em.clear();
		em.close();
	}
	
	public List<SettingsOpDataConfig> getControlDataConfList() {
		List<SettingsOpDataConfig> ctrDataConfigList = new ArrayList<SettingsOpDataConfig>();
		List<CtrDataConfEntity> ctrDataConfEntityList = this.ctrDataConfRepository.findAll(new Sort(Sort.Direction.ASC, TAG_DATA_SORT_COLUMN));
		for (CtrDataConfEntity ctrDataConfEntity : ctrDataConfEntityList) {
			SettingsOpDataConfig ctrDataConfig = new SettingsOpDataConfig();
			ctrDataConfig.setTagId(ctrDataConfEntity.getTagId());
			ctrDataConfig.setTagNm(ctrDataConfEntity.getTagNm());
			ctrDataConfig.setDescription(ctrDataConfEntity.getDescription());
			ctrDataConfig.setMinRaw(ctrDataConfEntity.getMinRaw());
			ctrDataConfig.setMaxRaw(ctrDataConfEntity.getMaxRaw());
			ctrDataConfig.setUnit(ctrDataConfEntity.getUnit());
			ctrDataConfig.setMinEu(ctrDataConfEntity.getMinEu());
			ctrDataConfig.setMaxEu(ctrDataConfEntity.getMaxEu());
			ctrDataConfig.setPlantUnitId(ctrDataConfEntity.getPlantUnitId());
			ctrDataConfig.setTagNo(ctrDataConfEntity.getTagNo());
			ctrDataConfigList.add(ctrDataConfig);
		}

		return ctrDataConfigList;
	}
	
	@Transactional
	public void resetControlDataConf(List<SettingsOpDataConfig> ctrDataConfigList) {
		this.ctrDataConfRepository.deleteAll();
		for (SettingsOpDataConfig ctrDataConfig : ctrDataConfigList) {
			CtrDataConfEntity ctrDataConfEntity = new CtrDataConfEntity();
			ctrDataConfEntity.setTagId(ctrDataConfig.getTagId());
			ctrDataConfEntity.setTagNm(ctrDataConfig.getTagNm());
			ctrDataConfEntity.setDescription(ctrDataConfig.getDescription());
			ctrDataConfEntity.setMinRaw(ctrDataConfig.getMinRaw());
			ctrDataConfEntity.setMaxRaw(ctrDataConfig.getMaxRaw());
			ctrDataConfEntity.setUnit(ctrDataConfig.getUnit());
			ctrDataConfEntity.setMinEu(ctrDataConfig.getMinEu());
			ctrDataConfEntity.setMaxEu(ctrDataConfig.getMaxEu());
			ctrDataConfEntity.setPlantUnitId(ctrDataConfig.getPlantUnitId());
			ctrDataConfEntity.setTagNo(ctrDataConfig.getTagNo());
			em.persist(ctrDataConfEntity);
		}

		em.flush();
		em.clear();
		em.close();
	}
	
	public List<SettingsOpDataConfig> getCommonDataConfList() {
		List<SettingsOpDataConfig> commonDataConfigList = new ArrayList<SettingsOpDataConfig>();
		List<CommonDataConfEntity> commonDataConfEntityList = this.commonDataConfRepository.findAll(new Sort(Sort.Direction.ASC, TAG_DATA_SORT_COLUMN, COMMON_PLANT_UNIT_ID_TAG_DATA_SORT_COLUMN));
		for (CommonDataConfEntity commonDataConfEntity : commonDataConfEntityList) {
			SettingsOpDataConfig commonDataConfig = new SettingsOpDataConfig();
			commonDataConfig.setTagId(commonDataConfEntity.getTagId());
			commonDataConfig.setTagNm(commonDataConfEntity.getCommonDataConfIdEntity().getTagNm());
			commonDataConfig.setDescription(commonDataConfEntity.getDescription());
			commonDataConfig.setMinRaw(commonDataConfEntity.getMinRaw());
			commonDataConfig.setMaxRaw(commonDataConfEntity.getMaxRaw());
			commonDataConfig.setUnit(commonDataConfEntity.getUnit());
			commonDataConfig.setMinEu(commonDataConfEntity.getMinEu());
			commonDataConfig.setMaxEu(commonDataConfEntity.getMaxEu());
			commonDataConfig.setPlantUnitId(commonDataConfEntity.getCommonDataConfIdEntity().getPlantUnitId());
			commonDataConfig.setTagNo(commonDataConfEntity.getTagNo());
			commonDataConfigList.add(commonDataConfig);
		}

		return commonDataConfigList;
	}
	
	@Transactional("commonTransactionManager")
	public void resetCommonDataConf(List<SettingsOpDataConfig> commonDataConfigList) {
		this.commonDataConfRepository.deleteAll();
		for (SettingsOpDataConfig commonDataConfig : commonDataConfigList) {
			CommonDataConfEntity commonDataConfEntity = new CommonDataConfEntity();
			commonDataConfEntity.setCommonDataConfIdEntity(new CommonDataConfIdEntity(commonDataConfig.getTagNm(), commonDataConfig.getPlantUnitId()));
			commonDataConfEntity.setTagId(commonDataConfig.getTagId());
			commonDataConfEntity.setDescription(commonDataConfig.getDescription());
			commonDataConfEntity.setMinRaw(commonDataConfig.getMinRaw());
			commonDataConfEntity.setMaxRaw(commonDataConfig.getMaxRaw());
			commonDataConfEntity.setUnit(commonDataConfig.getUnit());
			commonDataConfEntity.setMinEu(commonDataConfig.getMinEu());
			commonDataConfEntity.setMaxEu(commonDataConfig.getMaxEu());
			commonDataConfEntity.setTagNo(commonDataConfig.getTagNo());
			commonEM.persist(commonDataConfEntity);
		}
		
		commonEM.flush();
		commonEM.clear();
		commonEM.close();
	}
	
	public List<SettingsNNTrainDataConfig> getNNTrainDataConfList() {
		List<SettingsNNTrainDataConfig> nnTrainDataConfigList = new ArrayList<SettingsNNTrainDataConfig>();
		List<NNTrainDataConfEntity> nnTrainDataConfEntityList = this.nnTrainDataConfRepository.findAll(new Sort(Sort.Direction.ASC, TAG_DATA_SORT_COLUMN));
		for (NNTrainDataConfEntity nnTrainDataConfEntity : nnTrainDataConfEntityList) {
			SettingsNNTrainDataConfig nnTrainDataConfig = new SettingsNNTrainDataConfig();
			nnTrainDataConfig.setTagId(nnTrainDataConfEntity.getTagId());
			nnTrainDataConfig.setTagNm(nnTrainDataConfEntity.getTagNm());
			nnTrainDataConfig.setPsoMV(nnTrainDataConfEntity.getPsoMV());
			nnTrainDataConfig.setZeroPlate(nnTrainDataConfEntity.getZeroPlate());
			nnTrainDataConfig.setIoType(nnTrainDataConfEntity.getIoType());
			nnTrainDataConfig.setTagNo(nnTrainDataConfEntity.getTagNo());
			nnTrainDataConfigList.add(nnTrainDataConfig);
		}

		return nnTrainDataConfigList;
	}
	
	public boolean checkNNModelDelete(List<SettingsNNTrainDataConfig> nnTrainDataConfigList) {
		boolean isNNModelDelete = false;
		List<NNTrainDataConfEntity> nnTrainDataConfEntityList = this.nnTrainDataConfRepository.findAll(new Sort(Sort.Direction.ASC, TAG_DATA_SORT_COLUMN));
		if (nnTrainDataConfigList.size() != nnTrainDataConfEntityList.size())
			isNNModelDelete = true;

		if (!isNNModelDelete) {
			Collections.sort(nnTrainDataConfigList, new TagNoAscComparator());
			for (int i = 0; i < nnTrainDataConfigList.size(); i++) {
				SettingsNNTrainDataConfig nnTrainDataConf = nnTrainDataConfigList.get(i);
				NNTrainDataConfEntity nnTrainDataConfEntity = nnTrainDataConfEntityList.get(i);

				if (!nnTrainDataConf.getTagId().equals(nnTrainDataConfEntity.getTagId())
						|| !nnTrainDataConf.getTagNm().equals(nnTrainDataConfEntity.getTagNm())) {
					isNNModelDelete = true;
				}
			}
		}

		return isNNModelDelete;
	}
	
	@Transactional
	public void resetNNTrainDataConf(List<SettingsNNTrainDataConfig> nnTrainDataConfigList, boolean isNNModelDelete) {
		// NN Model Delete.
		if (isNNModelDelete) { 
			this.nnModelBaseRepository.updateTrainStatusFalse();
			this.nnModelRepository.updateTrainStatusFalse();
		}
		
		this.nnTrainDataConfRepository.deleteAll();
		for (SettingsNNTrainDataConfig nnTrainDataConfig : nnTrainDataConfigList) {
			NNTrainDataConfEntity nnTrainDataConfEntity = new NNTrainDataConfEntity();
			nnTrainDataConfEntity.setTagId(nnTrainDataConfig.getTagId());
			nnTrainDataConfEntity.setTagNm(nnTrainDataConfig.getTagNm());
			nnTrainDataConfEntity.setPsoMV(nnTrainDataConfig.getPsoMV());
			nnTrainDataConfEntity.setZeroPlate(nnTrainDataConfig.getZeroPlate());
			nnTrainDataConfEntity.setIoType(nnTrainDataConfig.getIoType());
			nnTrainDataConfEntity.setTagNo(nnTrainDataConfig.getTagNo());
			em.persist(nnTrainDataConfEntity);
		}

		em.flush();
		em.clear();
		em.close();
	}
	
	public List<SettingsPsoMVInfoConfig> getPsoMVInfoConfList() {
		List<SettingsPsoMVInfoConfig> psoMVInfoConfigList = new ArrayList<SettingsPsoMVInfoConfig>();
		List<PSOMVInfoEntity> psoMVInfoEntityList = this.psoMVInfoRepository.findAll(new Sort(Sort.Direction.ASC, MV_DATA_SORT_COLUMN));
		for (PSOMVInfoEntity psoMVInfoEntity : psoMVInfoEntityList) {
			SettingsPsoMVInfoConfig psoMVInfoConfig = new SettingsPsoMVInfoConfig();
			psoMVInfoConfig.setPsoMV(psoMVInfoEntity.getPsoMV());
			psoMVInfoConfig.setPsoMVType(psoMVInfoEntity.getPsoMVType());
			psoMVInfoConfig.setPsoMVMin(psoMVInfoEntity.getPsoMVMin());
			psoMVInfoConfig.setPsoMVMax(psoMVInfoEntity.getPsoMVMax());
			psoMVInfoConfig.setPsoMVOrder(psoMVInfoEntity.getPsoMVOrder());
			psoMVInfoConfig.setAutoModeTagId(psoMVInfoEntity.getAutoModeTagId());
			psoMVInfoConfig.setHoldTagId(psoMVInfoEntity.getHoldTagId());
			psoMVInfoConfig.setInputBiasTagId(psoMVInfoEntity.getInputBiasTagId());
			psoMVInfoConfig.setOutputBiasTagId(psoMVInfoEntity.getOutputBiasTagId());
			psoMVInfoConfigList.add(psoMVInfoConfig);
		}

		return psoMVInfoConfigList;
	}
	
	@Transactional
	public void resetPsoMVInfoDataConf(List<SettingsPsoMVInfoConfig> psoMVInfoConfigList) {
		this.psoMVInfoRepository.deleteAll();
		for (SettingsPsoMVInfoConfig psoMVInfoConfig : psoMVInfoConfigList) {
			PSOMVInfoEntity psoMVInfoEntity = new PSOMVInfoEntity();
			psoMVInfoEntity.setPsoMV(psoMVInfoConfig.getPsoMV());
			psoMVInfoEntity.setPsoMVType(psoMVInfoConfig.getPsoMVType());
			psoMVInfoEntity.setPsoMVMin(psoMVInfoConfig.getPsoMVMin());
			psoMVInfoEntity.setPsoMVMax(psoMVInfoConfig.getPsoMVMax());
			psoMVInfoEntity.setPsoMVOrder(psoMVInfoConfig.getPsoMVOrder());
			psoMVInfoEntity.setAutoModeTagId(psoMVInfoConfig.getAutoModeTagId());
			psoMVInfoEntity.setHoldTagId(psoMVInfoConfig.getHoldTagId());
			psoMVInfoEntity.setInputBiasTagId(psoMVInfoConfig.getInputBiasTagId());
			psoMVInfoEntity.setOutputBiasTagId(psoMVInfoConfig.getOutputBiasTagId());
			em.persist(psoMVInfoEntity);
		}

		em.flush();
		em.clear();
		em.close();
	}
	
	public class TagNoAscComparator implements Comparator<SettingsNNTrainDataConfig> {
		@Override
		public int compare(SettingsNNTrainDataConfig a, SettingsNNTrainDataConfig b) {
			return a.getTagNo() < b.getTagNo() ? -1 : a.getTagNo() == b.getTagNo() ? 0 : 1;
		}
	}
}
