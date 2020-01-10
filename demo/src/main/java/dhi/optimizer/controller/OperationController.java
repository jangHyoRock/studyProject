package dhi.optimizer.controller;

import dhi.common.exception.InvalidParameterException;
import dhi.common.httpEntity.RestResponse;
import dhi.common.httpEntity.RestResponseEntity;
import dhi.optimizer.common.StaticContext;
import dhi.optimizer.model.db.ScheduleEntity;
import dhi.optimizer.model.json.ItemStatus;
import dhi.optimizer.model.json.Operation;
import dhi.optimizer.repository.ScheduleRepository;
import dhi.optimizer.schedule.ScheduleService;
import dhi.optimizer.schedule.executor.ScheduleExecutor;
import dhi.optimizer.service.OperationService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Operation Controller Class. <br>
 * : 연소최적화  System, Learning, Control Mode, Optimization Mode 등을  조작하기 위한  Rest API Controller.
 */
@RestController
@RequestMapping("/operation")
public class OperationController {

	@Autowired
    protected EntityManagerFactory emFactory;
	
	@Autowired
	ScheduleService scheduleService;

	@Autowired
	OperationService operationService;
	
	@Autowired
	ScheduleRepository scheduleRepository;

	/**
	 * 연소최적화 스케줄러 시작 API.
	 * @return Response Result.
	 */
	@PutMapping("/schedule/start")
	public RestResponse ScheduleStart() {
		RestResponse result;
		try {
			if (!this.scheduleService.isScheduleStart()) {
				this.scheduleService.start();
				result = new RestResponse();
			} else {
				result = new RestResponse("Scheduler is currently running.");
			}
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * 연소최적화 스케줄러 중지 API.
	 * @return Response Result.
	 */
	@PutMapping("/schedule/stop")
	public RestResponse ScheduleStop() {
		RestResponse result;
		try {
			if (this.scheduleService.isScheduleStart()) {
				this.scheduleService.stop();
				result = new RestResponse();
			} else {
				result = new RestResponse("Scheduler is not currently running.");
			}

		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * 연소최적화 PSO Simulation 스케줄러 시작 API.
	 * @return Response Result.
	 */
	@PutMapping("/schedule/pso/simulation/start/{sec}")
	public RestResponse SchedulePsoSimulationStart(@PathVariable(value = "sec") Integer sec) {
		RestResponse result;
		try {
			if (!this.scheduleService.isScheduleStart()) {
				
				EntityManager em = this.emFactory.createEntityManager();
				try {	
					
					EntityTransaction tx = em.getTransaction();
					try {
						tx.begin();
						Query query = em.createNativeQuery("UPDATE TB_TEST_PSO_SIMULATION_INPUT SET EXECUTED_CNT = 0, LAST_EXECUTED_TS = NULL");
						query.executeUpdate();
						tx.commit();						
					} catch (Exception e) {
						tx.rollback();
						throw e;
					}		
					
				} catch (Exception e) {
					throw e;
				} finally {
					em.close();
				}
				
				this.scheduleService.startPSOSimulation(sec * 1000);
				
				result = new RestResponse();
			} else {
				result = new RestResponse("Scheduler is currently running.");
			}
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * 연소최적화 PSO Simulation On API.
	 * @return Response Result.
	 */
	@PutMapping("/schedule/pso_to_simulation/{sec}")
	public RestResponse SchedulePsoToSimulationn(@PathVariable(value = "sec") Integer sec) {
		RestResponse result;
		try {			
			if (this.scheduleService.isScheduleStart()) {			
				if (this.scheduleService.getScheduleTask().isExecutorExist()) {
					List<ScheduleExecutor> scheduleExecutorList = this.scheduleService.getScheduleTask().getScheduleExecutorList();
					int removeIndex = 0;
					for (ScheduleExecutor scheduleExecutor : scheduleExecutorList) {
						if (scheduleExecutor.getId() == 2000)					
							break;
						
						removeIndex++;
					}
					
					if (removeIndex < scheduleExecutorList.size()) {
						scheduleExecutorList.remove(removeIndex);
						
						EntityManager em = this.emFactory.createEntityManager();
						try {	
							
							EntityTransaction tx = em.getTransaction();
							try {
								tx.begin();
								Query query = em.createNativeQuery("UPDATE TB_TEST_PSO_SIMULATION_INPUT SET EXECUTED_CNT = 0, LAST_EXECUTED_TS = NULL");
								query.executeUpdate();
								tx.commit();
								
							} catch (Exception e) {
								tx.rollback();
								throw e;
							}						
						} catch (Exception e) {
							throw e;
						} finally {
							em.close();
						}
						
						List<ScheduleEntity> scheduleEntityList = scheduleRepository.findAll();
						for (ScheduleEntity scheduleEntity : scheduleEntityList) {
							if (scheduleEntity.getScheduleId() == 2000) {
								Class<?> dynamicClass = Class.forName("dhi.optimizer.schedule.executor.AlgorithmSimulationExecutor");
								ScheduleExecutor scheduleExecutor = (ScheduleExecutor) StaticContext.getBean(dynamicClass);
								scheduleExecutor.setId(scheduleEntity.getScheduleId());
								scheduleExecutor.setInterval(sec > 0 ? sec * 1000 : scheduleEntity.getInterval());
								scheduleExecutor.setSystemReadyCheck(scheduleEntity.isSystemReadyCheck());
								scheduleExecutor.setAlWaysRun(scheduleEntity.isAlWaysRun());
								scheduleExecutorList.add(scheduleExecutor);
							}
						}
					}
					result = new RestResponse();
				} else {
					result = new RestResponse("Scheduler no task.");
				}
			}
			else {
				result = new RestResponse("Scheduler is not currently running.");
			}
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * 연소최적화 PSO Simulation Off API.
	 * @return Response Result.
	 */
	@PutMapping("/schedule/simulation_to_pso")
	public RestResponse ScheduleSimulationToPso() {
		RestResponse result;
		try {
			
			if (this.scheduleService.isScheduleStart()) {
				if (this.scheduleService.getScheduleTask().isExecutorExist()) {				
					List<ScheduleExecutor> scheduleExecutorList = this.scheduleService.getScheduleTask().getScheduleExecutorList();
					int removeIndex = 0;
	
					for (ScheduleExecutor scheduleExecutor : scheduleExecutorList) {
						if (scheduleExecutor.getId() == 2000)					
							break;
						
						removeIndex++;
					}
					
					if (removeIndex < scheduleExecutorList.size()) {
						scheduleExecutorList.remove(removeIndex);
						
						List<ScheduleEntity> scheduleEntityList = scheduleRepository.findAll();
						for (ScheduleEntity scheduleEntity : scheduleEntityList) {
							if (scheduleEntity.getScheduleId() == 2000) {
								Class<?> dynamicClass = Class.forName(scheduleEntity.getModuleClass());
								ScheduleExecutor scheduleExecutor = (ScheduleExecutor) StaticContext.getBean(dynamicClass);
								scheduleExecutor.setId(scheduleEntity.getScheduleId());
								scheduleExecutor.setInterval(scheduleEntity.getInterval());
								scheduleExecutor.setSystemReadyCheck(scheduleEntity.isSystemReadyCheck());
								scheduleExecutor.setAlWaysRun(scheduleEntity.isAlWaysRun());
								scheduleExecutorList.add(scheduleExecutor);
							}
						}
					}
				
					result = new RestResponse();
				} else {
					result = new RestResponse("Scheduler no task.");
				}
			}
			else {
				result = new RestResponse("Scheduler is not currently running.");
			}
				
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
	
	/**
	 * 연소최적화 Schedule List API.
	 * @return Response Result.
	 */
	@GetMapping("/schedule/list")
	public RestResponse ScheduleList() {
		RestResponse result;
		try {
			
			if (this.scheduleService.getScheduleTask().isExecutorExist()) {				
				List<ScheduleExecutor> scheduleExecutorList = this.scheduleService.getScheduleTask().getScheduleExecutorList();				

				String schedultList = "";
				for (ScheduleExecutor scheduleExecutor : scheduleExecutorList) {					
					schedultList += scheduleExecutor.getId() + "|"  + scheduleExecutor.getInterval() + "###";
				}				
				result = new RestResponse(schedultList);
			} else {
				result = new RestResponse("Scheduler no task.");
			}
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	/**
	 * Operation(System ON/OFF, Learning Mode, Control Mode, Optimization Mode) Info API. <br> 
	 * Path : Operation.
	 * @return Response Result.
	 */
	@GetMapping("/data")
	public RestResponseEntity<?> getOperationMode() {
		RestResponseEntity<Operation> result = null;
		try {
			Operation operation = operationService.getOperationMode();
			result = new RestResponseEntity<Operation>(operation);

		} catch (Exception e) {
			result = new RestResponseEntity<Operation>(e);
		}

		return result;
	}

	/**
	 * Operation(System ON/OFF, Learning Mode, Control Mode, Optimization Mode) Save API. <br>
	 * Path : Operation -> Combustion Optimizer -> OFF/ON. <br>
	 * Path : Operation -> Learning Mode -> OFF/ON/Self ON. <br>
	 * Path : Operation -> Control Mode -> STOP/RUN. <br>
	 * Path : Operation -> Optimization Mode -> Profit/Emission/Equipment. <br>
	 * @param optimizeMode Operation Info.
	 * @return Response Result.
	 */
	@PostMapping("/data")
	public RestResponse saveOperationMode(@RequestBody ItemStatus optimizeMode) {
		RestResponse result = null;
		try {
			operationService.saveOperationMode(optimizeMode);
			result = new RestResponse();
		} catch (InvalidParameterException e) {
			result = new RestResponse(e);
		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}

	/**
	 * Cost Saving Reset API. <br>
	 * Path : Performance & Efficiency -> Cost Saving Information -> Reset.
	 * @return Response Result.
	 */
	@PostMapping("/reset_effect")
	public RestResponse resetCumulatedCostSavingEffect() {
		RestResponse result = null;
		try {
			operationService.resetCumulatedCostSavingEffect();
			result = new RestResponse();

		} catch (Exception e) {
			result = new RestResponse(e);
		}

		return result;
	}
}