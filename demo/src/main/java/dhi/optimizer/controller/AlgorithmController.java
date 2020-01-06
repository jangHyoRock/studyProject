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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dhi.common.exception.InvalidParameterException;
import dhi.common.httpEntity.RestResponse;
import dhi.common.httpEntity.RestResponseEntity;
import dhi.common.httpEntity.RestResponseEntityList;
import dhi.optimizer.enumeration.MetalTempTubeType;
import dhi.optimizer.enumeration.NNTrainDataIOType;
import dhi.optimizer.model.json.AlgorithOutputControllerConfig;
import dhi.optimizer.model.json.AlgorithmNNConfig;
import dhi.optimizer.model.json.AlgorithmPSOConfig;
import dhi.optimizer.model.json.AlgorithmProcessStatus;
import dhi.optimizer.model.json.AlgorithmSolutionStatus;
import dhi.optimizer.model.json.Chart;
import dhi.optimizer.model.json.KeyValue;
import dhi.optimizer.model.json.Navigator;
import dhi.optimizer.model.json.NavigatorConfig;
import dhi.optimizer.model.json.NavigatorLimitsAndFireBallCenter;
import dhi.optimizer.model.json.NavigatorWallMatchingTable;
import dhi.optimizer.model.json.NavigatorTubeWallMetalTemp;
import dhi.optimizer.model.json.NavigatorTubeWallMetalTempAll;
import dhi.optimizer.service.AlgorithmService;

/**
 * Algorithm Controller Class. <br>
 * : 연소최적화 화면에서 Algorithm 메뉴를 선택하였을 때 호출되는 Rest API Controller.
 */
@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {
	
	private static final Logger logger = LoggerFactory.getLogger(AlgorithmController.class);
	
	private static final String NNModelTrainInputCSVFileName = "NNModelTrainInputData.csv";
	private static final String NNModelValidInputCSVFileName = "NNModelValidInputData.csv";
	private static final String NNModelTrainOutputCSVFileName = "NNModelTrainOutputData.csv";
	private static final String NNModelValidOutputCSVFileName = "NNModelValidOutputData.csv";
	private static final String NNModelDownloadXmlFileName = "NNModel.xml";

	@Autowired
	private AlgorithmService algorithmService;
	
	/**
	 * NN Model Setting - Model Upload API. <br>
	 * Path : Algorithm & Setting -> Settings -> NN -> Upload.
	 * @param file Model File.
	 * @return Response Result.
	 */
	@PostMapping("/nn/model/upload")
	public RestResponse uploadNNModel(@RequestParam("file") MultipartFile file)	{
		RestResponse result = null;
		try {
			this.algorithmService.uploadNNModel(file.getInputStream());
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}
		
		return result;
	}
	
	/**
	 * NN Model Setting - Model Download API. <br>
	 * Path : No UI
	 * @param model_type base or normal
	 * @param model_type model timestamp id
	 */
	@GetMapping("/nn/model/download")
	public void downloadNNModel(HttpServletResponse response, @RequestParam(value = "model_type") String modelType, @RequestParam(value = "timestamp") String timestamp) {
	
		response.addHeader("Content-Type", "application/xml");
		response.addHeader("Content-Disposition", "attachment; filename=" + NNModelDownloadXmlFileName);
		response.setCharacterEncoding("UTF-8");
		
		try {			
			String modelXmlToString = this.algorithmService.getXmlDownLoadNNModel(modelType, timestamp);

			PrintWriter out = response.getWriter();
			out.write(modelXmlToString);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.toString());			
		} catch (InvalidParameterException e) {
			logger.error(e.toString());
		}
	}
	
	/**
	 * NN Model Setting - Data Studio Trend API. <br>
	 * Path : Algorithm & Setting -> Settings -> NN -> Data Studio -> Search.
	 * @param startDate Trend Start Date.
	 * @param endDate Trend End Date.
	 * @return Response Result.
	 */
	@GetMapping("/nn/input/trend/data")
	public RestResponseEntity<?> getNNModelInputData(@RequestParam(value = "start_date") String startDate, @RequestParam(value = "end_date") String endDate) {
		RestResponseEntity<Chart> result = null;
		try {
			result = new RestResponseEntity<Chart>(this.algorithmService.getTrendNNModelData(startDate, endDate));
		} catch (Exception e) {
			result = new RestResponseEntity<Chart>(e);
		}
		
		return result;
	}		
	
	/**
	 * NN Model Setting - Data Studio Training Input Data Download API. <br>
	 * Path : Algorithm & Setting -> Settings -> NN -> Data Studio -> Training Data Download.
	 * @param response HttpServletResponse.
	 * @param startDate Download Start Date.
	 * @param endDate DownLoad End Date.
	 */
	@GetMapping("/nn/input/download")
	public void downloadNNModelTrainInputDataToCSV(HttpServletResponse response, @RequestParam(value = "start_date") String startDate, @RequestParam(value = "end_date") String endDate) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + NNModelTrainInputCSVFileName);
		response.setCharacterEncoding("UTF-8");
		
		this.makeNNModelDataToCSV(response, NNTrainDataIOType.Input, startDate, endDate);
	}
	
	/**
	 * NN Model Setting - Data Studio Valid Input Data Download API. <br>
	 * Path : Algorithm & Setting -> Settings -> NN -> Data Studio -> Validation Data Download.
	 * @param response HttpServletResponse.
	 * @param startDate Download Start Date.
	 * @param endDate DownLoad End Date.
	 */
	@GetMapping("/nn/valid_input/download")
	public void downloadNNModelValidInputDataToCSV(HttpServletResponse response, @RequestParam(value = "start_date") String startDate, @RequestParam(value = "end_date") String endDate) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + NNModelValidInputCSVFileName);
		response.setCharacterEncoding("UTF-8");
		
		this.makeNNModelDataToCSV(response, NNTrainDataIOType.Input, startDate, endDate);
	}
	
	/**
	 * NN Model Setting - Data Studio Train Output Data Download API. <br>
	 * Path : Algorithm & Setting -> Settings -> NN -> Data Studio -> Training Data Download.
	 * @param response HttpServletResponse.
	 * @param startDate Download Start Date.
	 * @param endDate DownLoad End Date.
	 */
	@GetMapping("/nn/output/download")
	private void downloadNNModelOutputDataToCSV(HttpServletResponse response, @RequestParam(value = "start_date") String startDate, @RequestParam(value = "end_date") String endDate) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + NNModelTrainOutputCSVFileName);
		response.setCharacterEncoding("UTF-8");			
		this.makeNNModelDataToCSV(response, NNTrainDataIOType.Output, startDate, endDate);
	}
	
	/**
	 * NN Model Setting - Data Studio Train Output Data Download API. <br>
	 * Path : Algorithm & Setting -> Settings -> NN -> Data Studio -> Validation Data Download.
	 * @param response HttpServletResponse.
	 * @param startDate Download Start Date.
	 * @param endDate DownLoad End Date.
	 */
	@GetMapping("/nn/valid_output/download")
	private void downloadNNModelValidOutputDataToCSV(HttpServletResponse response, @RequestParam(value = "start_date") String startDate, @RequestParam(value = "end_date") String endDate) {		
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=" + NNModelValidOutputCSVFileName);
		response.setCharacterEncoding("UTF-8");
		
		this.makeNNModelDataToCSV(response, NNTrainDataIOType.Output, startDate, endDate);
	}
	
	/**
	 * Navigator Info API <br>
	 * Path : Algorithm & Setting -> Navigator.
	 * @return Response Result.
	 */
	@GetMapping("/navigator/info")
	public RestResponseEntity<Navigator> getNavigator() {
		RestResponseEntity<Navigator> result = null;
		try {
			result = new RestResponseEntity<Navigator>(this.algorithmService.getNavigatorInfo());
		} catch (Exception e) {
			result = new RestResponseEntity<Navigator>(e);
		}

		return result;
	}
	
	/**
	 * Navigator Info Save API. <br>
	 * Path : Algorithm & Setting -> Navigator -> Save.
	 * @param navigator Save Navigator Info
	 * @return Response Result.
	 */	
	@PostMapping("/navigator/info/setting")
	public RestResponse saveNavigator(@RequestBody Navigator navigator) {
		RestResponse result = null;
		try {
			this.algorithmService.saveNavigatorInfo(navigator);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}
		 
		return result;
	}
	
	/**
	 * Navigator Limits Setting API. <br>
	 * Path : Algorithm & Setting -> Navigator -> Limits.
	 * @return Response Result.
	 */
	@GetMapping("/navigator/limit_fireball")
	public RestResponseEntity<NavigatorLimitsAndFireBallCenter> getLimitsAndFireBallSetting() {
		RestResponseEntity<NavigatorLimitsAndFireBallCenter> result = null;
		try {
			result = new RestResponseEntity<NavigatorLimitsAndFireBallCenter>(this.algorithmService.getNavigatorLimitAndFireBallCenter());
		} catch (Exception e) {
			result = new RestResponseEntity<NavigatorLimitsAndFireBallCenter>(e);
		}

		return result;
	}	
	
	/**
	 * Navigator Limit Setting Save API <br>
	 * Path : Algorithm & Setting -> Navigator -> Limits -> Save.
	 * @param limitsAndFireBallCenter LimitsFireBallCenter Info.
	 * @return Response Result.
	 */
	@PostMapping("/navigator/limit_fireball/setting")
	public RestResponse saveLimitsAndFireBallSetting(@RequestBody NavigatorLimitsAndFireBallCenter limitsAndFireBallCenter) {
		RestResponse result = null;
		try {
			this.algorithmService.saveNavigatorLimitAndFireBallCenter(limitsAndFireBallCenter);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}
		 
		return result;
	}
	
	/**
	 * Navigator Settings API. <br>
	 * Path : Algorithm & Setting -> Navigator -> Settings.
	 * @return Response Result.
	 */
	@GetMapping("/navigator/conf")
	public RestResponseEntity<NavigatorConfig> getNavigatorConfig() {
		RestResponseEntity<NavigatorConfig> result = null;
		try {
			result = new RestResponseEntity<NavigatorConfig>(this.algorithmService.getNavigatorConfig());
		} catch (Exception e) {
			result = new RestResponseEntity<NavigatorConfig>(e);
		}

		return result;
	}
	
	/**
	 * Navigator Settings Save API. <br>
	 * Path : Algorithm & Setting -> Navigator -> Settings -> Save.
	 * @param navigatorConfig NavigatorConfig Info.
	 * @return Response Result.
	 */
	@PostMapping("/navigator/conf/setting")
	public RestResponse saveNavigatorConfSetting(@RequestBody NavigatorConfig navigatorConfig) {
		RestResponse result = null;
		try {
			this.algorithmService.saveNavigatorConfig(navigatorConfig);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}
		
		return result;
	}
	
	/**
	 * Navigator Spiral MT Temp API. (호출하지 않음.) <br>
	 * Path : Algorithm & Setting -> Navigator -> MT Temp -> Spiral.
	 * @return Response Result.
	 */
	@GetMapping("/navigator/spiral_water_wall_metal_temp/info")
	public RestResponseEntity<NavigatorTubeWallMetalTemp> getNavigatorSpiralWaterWallMetalTempInfo() {
		RestResponseEntity<NavigatorTubeWallMetalTemp> result = null;
		try {
			result = new RestResponseEntity<NavigatorTubeWallMetalTemp>(this.algorithmService.getNavigatorTubeWallMetalTempInfo(MetalTempTubeType.Spiral));
		} catch (Exception e) {
			result = new RestResponseEntity<NavigatorTubeWallMetalTemp>(e);
		}

		return result;
	}
	
	/**
	 * Navigator Vertical MT Temp API. (호출하지 않음.) <br>
	 * Path : Algorithm & Setting -> Navigator -> MT Temp -> Vertical.
	 * @return Response Result.
	 */
	@GetMapping("/navigator/vertical_water_wall_metal_temp/info")
	public RestResponseEntity<NavigatorTubeWallMetalTemp> getNavigatorVerticalWaterWallMetalTempInfo() {
		RestResponseEntity<NavigatorTubeWallMetalTemp> result = null;
		try {
			result = new RestResponseEntity<NavigatorTubeWallMetalTemp>(this.algorithmService.getNavigatorTubeWallMetalTempInfo(MetalTempTubeType.Vertical));
		} catch (Exception e) {
			result = new RestResponseEntity<NavigatorTubeWallMetalTemp>(e);
		}

		return result;
	}
	
	/**
	 * Navigator Both MT Temp API. <br>
	 * Path : Algorithm & Setting -> Navigator -> MT Temp -> Both.
	 * @return Response Result.
	 */
	@GetMapping("/navigator/spiral_vertical_water_wall_metal_temp/info")
	public RestResponseEntity<NavigatorTubeWallMetalTempAll> getNavigatorSpiralVerticalWaterWallMetalTempInfo() {
		RestResponseEntity<NavigatorTubeWallMetalTempAll> result = null;
		try {
			NavigatorTubeWallMetalTempAll tubeWallMetalTempAll = new NavigatorTubeWallMetalTempAll();
			tubeWallMetalTempAll.setSpiralTubeWallMetalTemp(this.algorithmService.getNavigatorTubeWallMetalTempInfo(MetalTempTubeType.Spiral));
			tubeWallMetalTempAll.setVerticalTubeWallMetalTemp(this.algorithmService.getNavigatorTubeWallMetalTempInfo(MetalTempTubeType.Vertical));
			
			result = new RestResponseEntity<NavigatorTubeWallMetalTempAll>(tubeWallMetalTempAll);
		} catch (Exception e) {
			result = new RestResponseEntity<NavigatorTubeWallMetalTempAll>(e);
		}

		return result;
	}
	
	/**
	 * Navigator Tube Project Set - Water Wall Matching Table API. <br>
	 * Path : Algorithm & Setting -> Navigator -> Tube Project Set.
	 * @return Response Result.
	 */
	@GetMapping("/navigator/water_wall_matching_table/info")
	public RestResponseEntity<NavigatorWallMatchingTable> getNavigatorWaterWallMatchingTableInfo() {
		RestResponseEntity<NavigatorWallMatchingTable> result = null;
		try {
			result = new RestResponseEntity<NavigatorWallMatchingTable>(this.algorithmService.getNavigatorWallMatchingTableInfo());
		} catch (Exception e) {
			result = new RestResponseEntity<NavigatorWallMatchingTable>(e);
		}

		return result;
	}
	
	/**
	 * Navigator Tube Project Set - Water Wall Matching Table Save API. <br>
	 * Path : Algorithm & Setting -> Navigator -> Tube Project Set -> Save.
	 * @param WallMatchingTable NavigatorWallMatchingTable Info.
	 * @return
	 */
	@PostMapping("/navigator/water_wall_matching_table/setting")
	public RestResponse saveNavigatorWaterWallMatchingTableInfo(@RequestBody NavigatorWallMatchingTable WallMatchingTable) {
		RestResponse result = null;
		try {
			this.algorithmService.saveNavigatorWallMatchingTable(WallMatchingTable);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * Navigator Trend Category API. <br>
	 * Path : Algorithm & Setting -> Navigator -> Trend.
	 * @return Response Result.
	 */	
	@GetMapping("/navigator/trend/category/ddl")
	public RestResponseEntityList<KeyValue> getNavigatorTrendCategory() {
		RestResponseEntityList<KeyValue> result;
		try {
			result = new RestResponseEntityList<KeyValue>(this.algorithmService.getNavigatorTrendCategoryDDL());
		} catch (Exception e) {
			result = new RestResponseEntityList<KeyValue>(e);
		}
		
		return result;
	}
	
	/**
	 * Navigator Trend Data API. <br>
	 * Path : Algorithm & Setting -> Navigator -> Trend.
	 * @param item Trend Item
	 * @param startDate Trend Start Date.
	 * @param endDate Trend End Date.
	 * @return
	 */
	@GetMapping("/navigator/trend/data")
	public RestResponseEntity<?> getNavigatorTrendChartTimeCycle(@RequestParam(value = "item") String item
			, @RequestParam(value = "start_date") String startDate
			, @RequestParam(value = "end_date") String endDate) {
		RestResponseEntity<Chart> result = null;
		try {
			result = new RestResponseEntity<Chart>(
					this.algorithmService.getNavigatorTrendData(item, startDate, endDate));
		} catch (Exception e) {
			result = new RestResponseEntity<Chart>(e);
		}

		return result;
	}
	
	/**
	 * NN Model Settings API. <br>
	 * Path : Algorithm & Setting -> Settings -> NN.
	 * @return Response Result.
	 */
	@GetMapping("/nn/config")
	public RestResponseEntity<AlgorithmNNConfig> getNNModelConfig() {
		RestResponseEntity<AlgorithmNNConfig> result = null;
		try {
			result = new RestResponseEntity<AlgorithmNNConfig>(this.algorithmService.getAlgorithmNNConfig());
		} catch (Exception e) {
			result = new RestResponseEntity<AlgorithmNNConfig>(e);
		}

		return result;
	}

	/**
	 * NN Model Settings Save API. <br>
	 * Path : Algorithm & Setting -> Settings -> NN -> Save.
	 * @param nnConfig AlgorithmNNConfig Info.
	 * @return Response Result.
	 */
	@PostMapping("/nn/config")
	public RestResponse saveNNModelConfig(@RequestBody AlgorithmNNConfig nnConfig) {
		RestResponse result = null;
		try {
			this.algorithmService.saveAlgorithmNNConfig(nnConfig);
			result = new RestResponse();
		} catch (InvalidParameterException e) {
			result = new RestResponse(e);
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * PSO Settings API. <br>
	 * Path : Algorithm & Setting -> Settings -> PSO.
	 * @return Response Result.
	 */
	@GetMapping("/pso/config")
	public RestResponseEntity<AlgorithmPSOConfig> getPSOModelConfig() {
		RestResponseEntity<AlgorithmPSOConfig> result = null;
		try {
			result = new RestResponseEntity<AlgorithmPSOConfig>(this.algorithmService.getAlgorithmPSOConfig());
		} catch (Exception e) {
			result = new RestResponseEntity<AlgorithmPSOConfig>(e);
		}

		return result;
	}
	
	/**
	 * PSO Settings Save API. <br>
	 * Path : Algorithm & Setting -> Settings -> PSO -> Save.
	 * @param psoConfig PSOConfig Info.
	 * @return Response Result.
	 */
	@PostMapping("/pso/config")
	public RestResponse savePSOModelConfig(@RequestBody AlgorithmPSOConfig psoConfig) {
		RestResponse result = null;
		try {
			this.algorithmService.saveAlgorithmPSOConfig(psoConfig);
			result = new RestResponse();
		} catch (InvalidParameterException e) {
			result = new RestResponse(e);
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * Output Controller Settings API. <br>
	 * Path : Algorithm & Setting -> Settings -> Output Controller.
	 * @return Response Result.
	 */
	@GetMapping("/output_controller/config")
	public RestResponseEntity<AlgorithOutputControllerConfig> getOutputControllerConfig() {
		RestResponseEntity<AlgorithOutputControllerConfig> result = null;
		try {
			result = new RestResponseEntity<AlgorithOutputControllerConfig>(this.algorithmService.getAlgorithOutputControllerConfig());
		} catch (Exception e) {
			result = new RestResponseEntity<AlgorithOutputControllerConfig>(e);
		}

		return result;
	}
	
	/**
	 * Output Controller Settings Save API. <br>
	 * Path : Algorithm & Setting -> Settings -> Output Controller -> Save.
	 * @param outputControllerConfig OutputControllerConfig Info.
	 * @return Response Result.
	 */
	@PostMapping("/output_controller/config")
	public RestResponse saveAlgorithOutputControllerConfig(
			@RequestBody AlgorithOutputControllerConfig outputControllerConfig) {
		RestResponse result = null;
		try {
			this.algorithmService.saveAlgorithOutputControllerConfig(outputControllerConfig);
			result = new RestResponse();
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * Algorithm Solution Status API. <br>
	 * Path : Algorithm & Setting -> Status -> Solution.
	 * @param plantUnitId Plant Unit Id.
	 * @return Response Result.
	 */
	@GetMapping("/solution_status/{plant_unit_id}")
	public RestResponseEntity<AlgorithmSolutionStatus> getAlgorithmSolutionStatus(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntity<AlgorithmSolutionStatus> result = null;
		try {
			result = new RestResponseEntity<AlgorithmSolutionStatus>(this.algorithmService.getAlgorithmSolutionStatus(plantUnitId));
		} catch (Exception e) {
			result = new RestResponseEntity<AlgorithmSolutionStatus>(e);
		}

		return result;
	}	
	
	/**
	 * Algorithm Process Status API. <br>
	 * Path : Algorithm & Setting -> Status -> Algorithm.
	 * @param plantUnitId Plant Unit Id.
	 * @return Response Result.
	 */
	@GetMapping("/process_status/{plant_unit_id}")
	public RestResponseEntity<AlgorithmProcessStatus> getAlgorithmProcessStatus(@PathVariable(value = "plant_unit_id") String plantUnitId) {
		RestResponseEntity<AlgorithmProcessStatus> result = null;
		try {
			result = new RestResponseEntity<AlgorithmProcessStatus>(this.algorithmService.getAlgorithmProcessStatus(plantUnitId));
		} catch (Exception e) {
			result = new RestResponseEntity<AlgorithmProcessStatus>(e);
		}

		return result;
	}	
	
	/**
	 * NN Model - Train Data CSV Download Function.
	 * @param response HttpServletResponse.
	 * @param NNTrainDataIOType Input, Output Type.
	 * @param startDate Train Start Date.
	 * @param endDate Train End Date.
	 */
	private void makeNNModelDataToCSV(HttpServletResponse response, NNTrainDataIOType NNTrainDataIOType, String startDate, String endDate) {
		try {
			List<String> nnModelDataList = this.algorithmService.getCSVDownLoadNNModelData(NNTrainDataIOType, startDate, endDate);

			PrintWriter out = response.getWriter();
			for (String nnModelData : nnModelDataList) {
				out.write(nnModelData);
				out.write("\n");
			}

			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.toString());

		} catch (InvalidParameterException e) {
			logger.error(e.toString());
		}
	}
}
