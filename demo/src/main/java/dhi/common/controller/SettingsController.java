package dhi.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dhi.common.httpEntity.RestResponse;
import dhi.common.httpEntity.RestResponseEntityList;
import dhi.common.model.json.SettingsBaseDataConfig;
import dhi.common.model.json.SettingsCompanyDataConfig;
import dhi.common.model.json.SettingsPlantDataConfig;
import dhi.common.model.json.SettingsUnitDataConfig;
import dhi.common.service.SettingsService;
import dhi.common.util.CommonConst;
/**
 * Settings Controller Class. <br>
 * : 연소최적화에서 사용하는 Setting 정보를 가져오거나, 저장하는  Rest API Controller.
 */
@RestController
@RequestMapping("/settings")
public class SettingsController {

	private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

	private static final String SettingsCommonDataConfCSVFileName = "CommonDataConf.csv";
	private static final String SettingsCompanyDataConfCSVFileName = "CompanyDataConf.csv";
	private static final String SettingsPlantDataConfCSVFileName = "PlantDataConf.csv";
	private static final String SettingsUnitDataConfCSVFileName = "UnitDataConf.csv";

	@Autowired
	private SettingsService settingsService;
	
	/**
	 * Common Data Configuration Export API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Common Data -> Export.
	 * @param response HttpServletResponse.
	 */
	@GetMapping("/commondata/conf/download")
	public void downloadCommonDataConfToCSV(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + SettingsCommonDataConfCSVFileName);
		response.setCharacterEncoding("UTF-8");

		List<SettingsBaseDataConfig> commonDataConfigList = this.settingsService.getBaseDataConfList();
		this.makeBaseDataToCSV(response, commonDataConfigList);
	}
	
	/**
	 * Common Data Configuration Export API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Common Data -> Export.
	 * @param response HttpServletResponse.
	 */
	@GetMapping("/companydata/conf/download")
	public void downloadCompanyDataConfToCSV(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + SettingsCompanyDataConfCSVFileName);
		response.setCharacterEncoding("UTF-8");

		List<SettingsCompanyDataConfig> companyDataConfigList = this.settingsService.getCompanyDataConfList();
		this.makeCompanyDataToCSV(response, companyDataConfigList);
	}

	
	/**
	 * Common Data Configuration Export API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Common Data -> Export.
	 * @param response HttpServletResponse.
	 */
	@GetMapping("/plantdata/conf/download")
	public void downloadPlantDataConfToCSV(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + SettingsPlantDataConfCSVFileName);
		response.setCharacterEncoding("UTF-8");

		List<SettingsPlantDataConfig> plantDataConfigList = this.settingsService.getPlantDataConfList();
		this.makePlantDataToCSV(response, plantDataConfigList);
	}
	
	/**
	 * Common Data Configuration Export API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Common Data -> Export.
	 * @param response HttpServletResponse.
	 */
	@GetMapping("/unitdata/conf/download")
	public void downloadUnitDataConfToCSV(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + SettingsUnitDataConfCSVFileName);
		response.setCharacterEncoding("UTF-8");

		List<SettingsUnitDataConfig> plantDataConfigList = this.settingsService.getUnitDataConfList();
		this.makeUnitDataToCSV(response, plantDataConfigList);
	}

	
	/**
	 * Common Data Configuration API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Common Data.
	 * @return Response Result.
	 */
	@GetMapping("/commondata/conf")
	public RestResponseEntityList<SettingsBaseDataConfig> commonDataConf() {
		RestResponseEntityList<SettingsBaseDataConfig> result;
		try {
			result = new RestResponseEntityList<SettingsBaseDataConfig>(this.settingsService.getBaseDataConfList());
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsBaseDataConfig>(e);
		}

		return result;
	}

	/**
	 * Common Data Configuration Save API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Common Data -> Save.
	 * @param commonDataConfigList SettingsCommonConfig List.
	 * @return Response Result.
	 */
	@PostMapping("/commondata/conf")
	public RestResponse commonDataConf(@RequestBody List<SettingsBaseDataConfig> baseDataConfigList) {
		RestResponse result = null;
		try {
			this.settingsService.resetBaseDataConfList(baseDataConfigList);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	

	/**
	 * CompanyData Configuration API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Common Data.
	 * @return Response Result.
	 */
	@GetMapping("/companydata/conf")
	public RestResponseEntityList<SettingsCompanyDataConfig> companyDataConf() {
		RestResponseEntityList<SettingsCompanyDataConfig> result;
		try {
			result = new RestResponseEntityList<SettingsCompanyDataConfig>(this.settingsService.getCompanyDataConfList());
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsCompanyDataConfig>(e);
		}

		return result;
	}

	/**
	 * Common Data Configuration Save API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Common Data -> Save.
	 * @param commonDataConfigList SettingsCommonConfig List.
	 * @return Response Result.
	 */
	@PostMapping("/companydata/conf")
	public RestResponse companyDataConf(@RequestBody List<SettingsCompanyDataConfig> companyDataConfigList) {
		RestResponse result = null;
		try {
			this.settingsService.resetCompanyDataConfList(companyDataConfigList);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * Plant Data Configuration API. <br>
	 * Path : Data -> Plant Data
	 * @return Response Result.
	 */
	@GetMapping("/plantdata/conf")
	public RestResponseEntityList<SettingsPlantDataConfig> plantDataConf() {
		RestResponseEntityList<SettingsPlantDataConfig> result;
		try {
			result = new RestResponseEntityList<SettingsPlantDataConfig>(this.settingsService.getPlantDataConfList());
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsPlantDataConfig>(e);
		}

		return result;
	}

	/**
	 * Plant Data Configuration Save API. <br>
	 * Path : Data -> Plant Data -> Save.
	 * @param commonDataConfigList SettingsCommonConfig List.
	 * @return Response Result.
	 */
	@PostMapping("/plantdata/conf")
	public RestResponse plantDataConf(@RequestBody List<SettingsPlantDataConfig> plantDataConfigList) {
		RestResponse result = null;
		try {
			this.settingsService.resetPlantDataConfList(plantDataConfigList);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * Unit Data Configuration API. <br>
	 * Path : Data -> Unit Data
	 * @return Response Result.
	 */
	@GetMapping("/unitdata/conf")
	public RestResponseEntityList<SettingsUnitDataConfig> unitDataConf() {
		RestResponseEntityList<SettingsUnitDataConfig> result;
		try {
			result = new RestResponseEntityList<SettingsUnitDataConfig>(this.settingsService.getUnitDataConfList());
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsUnitDataConfig>(e);
		}

		return result;
	}

	/**
	 * Unit Data Configuration Save API. <br>
	 * Path : Data -> Unit Data -> Save.
	 * @param commonDataConfigList SettingsCommonConfig List.
	 * @return Response Result.
	 */
	@PostMapping("/unitdata/conf")
	public RestResponse unitDataConf(@RequestBody List<SettingsUnitDataConfig> unitDataConfigList) {
		RestResponse result = null;
		try {
			this.settingsService.resetUnitDataConfList(unitDataConfigList);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * Base Data CSV Response Function. <br>
	 * : OP Data Export 유형을  CSV 형태로 만드는 공통 함수. 
	 * @param response HttpServletResponse.
	 * @param opDataConfigList SettingsOpDataConfig.
	 */
	private void makeBaseDataToCSV(HttpServletResponse response, List<SettingsBaseDataConfig> baseDataConfigList) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Company Id,");
			sb.append("Plant Unit Id,");
			sb.append("Plant Id,");
			sb.append("Sys Id,");
			sb.append("Plant Unit IP,");
			sb.append("Plant Unit Port,");
			sb.append("Description,");
			sb.append("Plant Unit No");	
			sb.append(System.getProperty("line.separator"));
			
			for (SettingsBaseDataConfig baseDataConfig : baseDataConfigList) {
				sb.append((baseDataConfig.getCompanyId() == null ? CommonConst.StringEmpty :  baseDataConfig.getCompanyId()) + ",");
				sb.append((baseDataConfig.getpUnitId() == null ? CommonConst.StringEmpty :  baseDataConfig.getpUnitId()) + ",");
				sb.append((baseDataConfig.getPlantId() == null ? CommonConst.StringEmpty :  baseDataConfig.getPlantId()) + ",");
				sb.append((baseDataConfig.getSysId() == null ? CommonConst.StringEmpty :  baseDataConfig.getSysId()) + ",");
				sb.append((baseDataConfig.getpUnitIp() == null ? CommonConst.StringEmpty :  baseDataConfig.getpUnitIp()) + ",");
				sb.append((baseDataConfig.getpUnitPort() == null ? CommonConst.StringEmpty :  baseDataConfig.getpUnitPort()) + ",");
				sb.append((baseDataConfig.getDescription() == null ? CommonConst.StringEmpty :  baseDataConfig.getDescription()) + ",");
				sb.append((baseDataConfig.getpUnitNo()) == null ? CommonConst.StringEmpty :  baseDataConfig.getpUnitNo());
				
				sb.append(System.getProperty("line.separator"));
			}
			
			PrintWriter out = response.getWriter();
			out.write(sb.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
	
	/**
	 * Company Data CSV Response Function. <br>
	 * : OP Data Export 유형을  CSV 형태로 만드는 공통 함수. 
	 * @param response HttpServletResponse.
	 * @param opDataConfigList SettingsOpDataConfig.
	 */
	private void makeCompanyDataToCSV(HttpServletResponse response, List<SettingsCompanyDataConfig> companyDataConfigList) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Company Id,");
			sb.append("Company Name,");
			sb.append("Company Order,");
			sb.append("Description");	
			sb.append(System.getProperty("line.separator"));
			
			for (SettingsCompanyDataConfig companyDataConfig : companyDataConfigList) {
				sb.append((companyDataConfig.getCompanyId() == null ? CommonConst.StringEmpty :  companyDataConfig.getCompanyId()) + ",");
				sb.append((companyDataConfig.getCompanyNm() == null ? CommonConst.StringEmpty :  companyDataConfig.getCompanyNm()) + ",");
				sb.append((companyDataConfig.getCompanyOrder() == null ? CommonConst.StringEmpty :  companyDataConfig.getCompanyOrder()) + ",");
				sb.append((companyDataConfig.getDescription() == null ? CommonConst.StringEmpty :  companyDataConfig.getDescription()));
				
				sb.append(System.getProperty("line.separator"));
			}
			
			PrintWriter out = response.getWriter();
			out.write(sb.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
	
	
	/**
	 * Plant Data CSV Response Function. <br>
	 * : OP Data Export 유형을  CSV 형태로 만드는 공통 함수. 
	 * @param response HttpServletResponse.
	 * @param opDataConfigList SettingsOpDataConfig.
	 */
	private void makePlantDataToCSV(HttpServletResponse response, List<SettingsPlantDataConfig> plantDataConfigList) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Plant Id,");
			sb.append("Plant Name,");
			sb.append("Plant Order,");
			sb.append("Description");	
			sb.append(System.getProperty("line.separator"));
			
			for (SettingsPlantDataConfig plantDataConfig : plantDataConfigList) {
				sb.append((plantDataConfig.getPlantId() == null ? CommonConst.StringEmpty :  plantDataConfig.getPlantId()) + ",");
				sb.append((plantDataConfig.getPlantNm() == null ? CommonConst.StringEmpty :  plantDataConfig.getPlantNm()) + ",");
				sb.append((plantDataConfig.getPlantOrder() == null ? CommonConst.StringEmpty :  plantDataConfig.getPlantOrder()) + ",");
				sb.append((plantDataConfig.getDescription() == null ? CommonConst.StringEmpty :  plantDataConfig.getDescription()));
				
				sb.append(System.getProperty("line.separator"));
			}
			
			PrintWriter out = response.getWriter();
			out.write(sb.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
	
	/**
	 * Unit Data CSV Response Function. <br>
	 * : OP Data Export 유형을  CSV 형태로 만드는 공통 함수. 
	 * @param response HttpServletResponse.
	 * @param opDataConfigList SettingsOpDataConfig.
	 */
	private void makeUnitDataToCSV(HttpServletResponse response, List<SettingsUnitDataConfig> unitDataConfigList) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Plant Unit Id,");
			sb.append("Plant Unit Name,");
			sb.append("Plant Unit Order,");
			sb.append("Description");	
			sb.append(System.getProperty("line.separator"));
			
			for (SettingsUnitDataConfig unitDataConfig : unitDataConfigList) {
				sb.append((unitDataConfig.getpUnitId() == null ? CommonConst.StringEmpty :  unitDataConfig.getpUnitId()) + ",");
				sb.append((unitDataConfig.getpUnitNm() == null ? CommonConst.StringEmpty :  unitDataConfig.getpUnitNm()) + ",");
				sb.append((unitDataConfig.getpUnitOrder() == null ? CommonConst.StringEmpty :  unitDataConfig.getpUnitOrder()) + ",");
				sb.append((unitDataConfig.getDescription() == null ? CommonConst.StringEmpty :  unitDataConfig.getDescription()));
				
				sb.append(System.getProperty("line.separator"));
			}
			
			PrintWriter out = response.getWriter();
			out.write(sb.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
}
