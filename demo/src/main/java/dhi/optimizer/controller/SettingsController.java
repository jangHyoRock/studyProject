package dhi.optimizer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dhi.common.httpEntity.RestResponse;
import dhi.common.httpEntity.RestResponseEntityList;
import dhi.optimizer.common.CommonConst;
import dhi.optimizer.model.json.SettingsNNTrainDataConfig;
import dhi.optimizer.model.json.SettingsOpDataConfig;
import dhi.optimizer.model.json.SettingsPsoMVInfoConfig;
import dhi.optimizer.service.SettingsService;

/**
 * Settings Controller Class. <br>
 * : 연소최적화에서 사용하는 Setting 정보를 가져오거나, 저장하는  Rest API Controller.
 */
@RestController
@RequestMapping("/settings")
public class SettingsController {

	private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);

	private static final String SettingsOpDataConfCSVFileName = "OpDataConf.csv";
	private static final String SettingsCommonDataConfCSVFileName = "CommonDataConf.csv";
	private static final String SettingsControlDataConfCSVFileName = "ControlDataConf.csv";
	private static final String SettingsNNTrainDataConfCSVFileName = "NNTrainDataConf.csv";
	private static final String SettingsPsoMVInfoCSVFileName = "PsoMVInfo.csv";

	@Autowired
	private SettingsService settingsService;

	/**
	 * OP Data Configuration Export API. <br>
	 * Path : Algorithm & Settings -> Configuration -> OP Data -> Export.
	 * @param response HttpServletResponse.
	 */
	@GetMapping("/opdata/conf/download")
	public void downloadOpDataConfToCSV(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + SettingsOpDataConfCSVFileName);
		response.setCharacterEncoding("UTF-8");

		List<SettingsOpDataConfig> opDataConfigList = this.settingsService.getOpDataConfList();
		this.makeOpDataToCSV(response, opDataConfigList);
	}

	/**
	 * OP Data Configuration API. <br>
	 * Path : Algorithm & Settings -> Configuration -> OP Data.
	 * @return Response Result.
	 */
	@GetMapping("/opdata/conf")
	public RestResponseEntityList<SettingsOpDataConfig> opDataConf() {
		RestResponseEntityList<SettingsOpDataConfig> result;
		try {
			result = new RestResponseEntityList<SettingsOpDataConfig>(this.settingsService.getOpDataConfList());
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsOpDataConfig>(e);
		}

		return result;
	}

	/**
	 * OP Data Configuration Save API. <br>
	 * Path : Algorithm & Settings -> Configuration -> OP Data -> Save.  
	 * @param opDataConfigList SettingsOpDataConfig List
	 * @return Response Result.
	 */
	@PostMapping("/opdata/conf")
	public RestResponse opDataConf(@RequestBody List<SettingsOpDataConfig> opDataConfigList) {
		RestResponse result = null;
		try {
			this.settingsService.resetOpDataConf(opDataConfigList);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

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

		List<SettingsOpDataConfig> commonDataConfigList = this.settingsService.getCommonDataConfList();
		this.makeOpDataToCSV(response, commonDataConfigList);
	}

	/**
	 * Common Data Configuration API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Common Data.
	 * @return Response Result.
	 */
	@GetMapping("/commondata/conf")
	public RestResponseEntityList<SettingsOpDataConfig> commonDataConf() {
		RestResponseEntityList<SettingsOpDataConfig> result;
		try {
			result = new RestResponseEntityList<SettingsOpDataConfig>(this.settingsService.getCommonDataConfList());
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsOpDataConfig>(e);
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
	public RestResponse commonDataConf(@RequestBody List<SettingsOpDataConfig> commonDataConfigList) {
		RestResponse result = null;
		try {
			this.settingsService.resetCommonDataConf(commonDataConfigList);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	/**
	 * Control Data Configuration Export API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Control Data -> Export.
	 * @param response HttpServletResponse.
	 */
	@GetMapping("/controldata/conf/download")
	public void downloadControlDataConfToCSV(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + SettingsControlDataConfCSVFileName);
		response.setCharacterEncoding("UTF-8");

		List<SettingsOpDataConfig> controlDataConfigList = this.settingsService.getControlDataConfList();
		this.makeOpDataToCSV(response, controlDataConfigList);
	}

	/**
	 * Control Data Configuration API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Control Data.
	 * @return Response Result.
	 */
	@GetMapping("/controldata/conf")
	public RestResponseEntityList<SettingsOpDataConfig> controlDataConf() {
		RestResponseEntityList<SettingsOpDataConfig> result;
		try {
			result = new RestResponseEntityList<SettingsOpDataConfig>(this.settingsService.getControlDataConfList());
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsOpDataConfig>(e);
		}

		return result;
	}

	/**
	 * Control Data Configuration Save API. <br>
	 * Path : Algorithm & Settings -> Configuration -> Control Data -> Save API.
	 * @param controlDataConfigList SettingsControlDataConfig List.
	 * @return Response Result.
	 */
	@PostMapping("/controldata/conf")
	public RestResponse controlDataConf(@RequestBody List<SettingsOpDataConfig> controlDataConfigList) {
		RestResponse result = null;
		try {
			this.settingsService.resetControlDataConf(controlDataConfigList);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	/**
	 * NN Train Data Configuration Export API. <br>
	 * Path : Algorithm & Settings -> Configuration -> NNTrain Data -> Export.
	 * @param response HttpServletResponse
	 */
	@GetMapping("/nntraindata/conf/download")
	public void downloadNNTrainDataConfToCSV(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + SettingsNNTrainDataConfCSVFileName);
		response.setCharacterEncoding("UTF-8");
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Tag Id,");
			sb.append("Tag NM,");
			sb.append("Pso MV,");
			sb.append("Zero Plate,");
			sb.append("IO Type,");
			sb.append("Tag No");
			sb.append(System.getProperty("line.separator"));
			
			List<SettingsNNTrainDataConfig> nnTrainDataConfigList = this.settingsService.getNNTrainDataConfList();
			for (SettingsNNTrainDataConfig nnTrainDataConfig : nnTrainDataConfigList) {
				sb.append((nnTrainDataConfig.getTagId() == null ? CommonConst.StringEmpty :  nnTrainDataConfig.getTagId()) + ",");
				sb.append((nnTrainDataConfig.getTagNm() == null ? CommonConst.StringEmpty :  nnTrainDataConfig.getTagNm()) + ",");
				sb.append((nnTrainDataConfig.getPsoMV() == null ? CommonConst.StringEmpty :  nnTrainDataConfig.getPsoMV()) + ",");
				sb.append((nnTrainDataConfig.getZeroPlate() == null ? CommonConst.StringEmpty :  nnTrainDataConfig.getZeroPlate().toString().toLowerCase()) + ",");
				sb.append((nnTrainDataConfig.getIoType() == null ? CommonConst.StringEmpty :  nnTrainDataConfig.getIoType()) + ",");
				sb.append((nnTrainDataConfig.getTagNo()));
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
	 * NN Train Data Configuration API. <br>
	 * Path : Algorithm & Settings -> Configuration -> NNTrain Data.
	 * @return Response Result.
	 */
	@GetMapping("/nntraindata/conf")
	public RestResponseEntityList<SettingsNNTrainDataConfig> nnTrainDataConf() {
		RestResponseEntityList<SettingsNNTrainDataConfig> result;
		try {
			result = new RestResponseEntityList<SettingsNNTrainDataConfig>(this.settingsService.getNNTrainDataConfList());
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsNNTrainDataConfig>(e);
		}

		return result;
	}
	
	/**
	 * NN Train Data Configuration - NN Model Delete Check API. (Save 버튼 누르기 전 체크 로직) <br>
	 * : 등록 할 정보와 기존  NN Train Data가 동이하지 않은 경우  DB에 있는 NN Model를 삭제하기 위한 체크 로직 API. <br>
	 * Path : Algorithm & Settings -> Configuration -> NNTrain Data -> Save.
	 * @param nnTrainDataConfigList SettingsNNTrainDataConfig List.
	 * @return Response Result.
	 */
	@PostMapping("/nntraindata/nnmodel/delete/check")
	public RestResponse nnTrainDataNNModelDeleteCheck(@RequestBody List<SettingsNNTrainDataConfig> nnTrainDataConfigList) {
		RestResponse result;
		try {					
			result = new RestResponse(String.valueOf(this.settingsService.checkNNModelDelete(nnTrainDataConfigList)));
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	/**
	 * NN Train Data Configuration Save API. <br>
	 * : NN Train Data 정보를 Save 한다. (NN Model 삭제 여부가 있는 경우 DB의 NN Model을 삭제한다.) <br>
	 * Path : Algorithm & Settings -> Configuration -> NNTrain Data -> Save.
	 * @param nnTrainDataConfigList SettingsNNTrainDataConfig.
	 * @param isNNModelDelete NN Model 삭제여부.
	 * @return Response Result.
	 */
	@PostMapping("/nntraindata/conf/{is_nnmodel_delete}")
	public RestResponse nnTrainDataConf(@RequestBody List<SettingsNNTrainDataConfig> nnTrainDataConfigList, @PathVariable(value = "is_nnmodel_delete") boolean isNNModelDelete) {
		RestResponse result = null;
		try {
			this.settingsService.resetNNTrainDataConf(nnTrainDataConfigList, isNNModelDelete);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	/**
	 * PSO MV Configuration Export API. <br>
	 * Path : Algorithm & Settings -> Configuration -> PsoMv -> Export. 
	 * @param response HttpServletResponse.
	 */
	@GetMapping("/psomvinfo/conf/download")
	public void downloadPsoMVInfoConfToCSV(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + SettingsPsoMVInfoCSVFileName);
		response.setCharacterEncoding("UTF-8");
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("Pso MV,");
			sb.append("Pso MV Type,");
			sb.append("Pso MV Max,");
			sb.append("Pso MV Min,");
			sb.append("Pso MV Order,");
			sb.append("Auto Mode Tag Id,");
			sb.append("Hold Tag Id");
			sb.append("Input Bias Tag Id");
			sb.append("Output Bias Tag Id");
			sb.append(System.getProperty("line.separator"));
			
			List<SettingsPsoMVInfoConfig> psoMVInfoConfigList = this.settingsService.getPsoMVInfoConfList();
			for (SettingsPsoMVInfoConfig psoMVInfoConfig : psoMVInfoConfigList) {				
				sb.append((psoMVInfoConfig.getPsoMV() == null ? CommonConst.StringEmpty : psoMVInfoConfig.getPsoMV()) + ",");
				sb.append((psoMVInfoConfig.getPsoMVType() == null ? CommonConst.StringEmpty : psoMVInfoConfig.getPsoMVType()) + ",");
				sb.append(psoMVInfoConfig.getPsoMVMax() + ",");
				sb.append(psoMVInfoConfig.getPsoMVMin() + ",");
				sb.append(psoMVInfoConfig.getPsoMVOrder() + ",");
				sb.append(psoMVInfoConfig.getAutoModeTagId() + ",");
				sb.append(psoMVInfoConfig.getHoldTagId() + ",");
				sb.append(psoMVInfoConfig.getInputBiasTagId() + ",");
				sb.append(psoMVInfoConfig.getOutputBiasTagId());
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
	 * PSO MV Configuration API. <br>
	 * Path : Algorithm & Settings -> Configuration -> PsoMv.
	 * @return Response Result.
	 */
	@GetMapping("/psomvinfo/conf")
	public RestResponseEntityList<SettingsPsoMVInfoConfig> psoMVInfoConf() {
		RestResponseEntityList<SettingsPsoMVInfoConfig> result;
		try {
			result = new RestResponseEntityList<SettingsPsoMVInfoConfig>(this.settingsService.getPsoMVInfoConfList());
		} catch (Exception e) {
			result = new RestResponseEntityList<SettingsPsoMVInfoConfig>(e);
		}

		return result;
	}

	/**
	 * PSO MV Configuration Save API. <br>
	 * Path : Algorithm & Settings -> Configuration -> PsoMv -> Save.
	 * @param psoMVInfoConfigList SettingsPsoMVInfoConfig
	 * @return Response Result.
	 */
	@PostMapping("/psomvinfo/conf")
	public RestResponse psoMVInfoConf(@RequestBody List<SettingsPsoMVInfoConfig> psoMVInfoConfigList) {
		RestResponse result = null;
		try {
			this.settingsService.resetPsoMVInfoDataConf(psoMVInfoConfigList);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	/**
	 * OP Data CSV Response Function. <br>
	 * : OP Data Export 유형을  CSV 형태로 만드는 공통 함수. 
	 * @param response HttpServletResponse.
	 * @param opDataConfigList SettingsOpDataConfig.
	 */
	private void makeOpDataToCSV(HttpServletResponse response, List<SettingsOpDataConfig> opDataConfigList) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Tag Id,");
			sb.append("Tag NM,");
			sb.append("Description,");
			sb.append("Min Raw,");
			sb.append("Max Raw,");
			sb.append("Unit,");
			sb.append("Min EU,");
			sb.append("MAX EU,");
			sb.append("Plant Unit Id,");
			sb.append("Tag No");			
			sb.append(System.getProperty("line.separator"));
			
			for (SettingsOpDataConfig opDataConfig : opDataConfigList) {
				sb.append((opDataConfig.getTagId() == null ? CommonConst.StringEmpty :  opDataConfig.getTagId()) + ",");
				sb.append((opDataConfig.getTagNm() == null ? CommonConst.StringEmpty :  opDataConfig.getTagNm()) + ",");
				sb.append((opDataConfig.getDescription() == null ? CommonConst.StringEmpty :  opDataConfig.getDescription()) + ",");
				sb.append((opDataConfig.getMinRaw() == null ? CommonConst.StringEmpty :  opDataConfig.getMinRaw()) + ",");
				sb.append((opDataConfig.getMaxRaw() == null ? CommonConst.StringEmpty :  opDataConfig.getMaxRaw()) + ",");
				sb.append((opDataConfig.getUnit() == null ? CommonConst.StringEmpty :  opDataConfig.getUnit()) + ",");
				sb.append((opDataConfig.getMinEu() == null ? CommonConst.StringEmpty :  opDataConfig.getMinEu()) + ",");
				sb.append((opDataConfig.getMaxEu() == null ? CommonConst.StringEmpty :  opDataConfig.getMaxEu()) + ",");
				sb.append((opDataConfig.getPlantUnitId() == null ? CommonConst.StringEmpty :  opDataConfig.getPlantUnitId()) + ",");
				sb.append(opDataConfig.getTagNo());
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
