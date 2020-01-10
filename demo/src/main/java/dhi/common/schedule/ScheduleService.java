package dhi.common.schedule;

import java.util.List;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dhi.common.exception.CustomException;
import dhi.common.model.db.ScheduleEntity;
import dhi.common.repository.ScheduleRepository;
import dhi.common.schedule.executor.ScheduleExecutor;
import dhi.common.util.StaticContext;

/*
 * Services that provide start and stop functions for using the scheduler in the system
 */
@Service
public class ScheduleService
{
	static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);
	
	@Autowired
	ScheduleRepository scheduleRepository;
		
	@Value("${schedule.use}")
	private String scheduleUse;
	
	@Value("${schedule.interval}")
	private String scheduleInterval;
		
	private Timer timer;
	
	private ScheduleTask scheduleTask;

	public void start() {
		
		try {
			boolean isUse = Boolean.parseBoolean(this.scheduleUse);
			if(!isUse) {
				throw new CustomException("The entry in schedule.use is 'false' in application.properties");
			}
			
			this.scheduleTask = new ScheduleTask();
			
			List<ScheduleEntity> scheduleEntityList = scheduleRepository.findAll();
			for (ScheduleEntity scheduleEntity : scheduleEntityList) {
				if ("Y".equals(scheduleEntity.getUseYN())) {
					Class<?> dynamicClass = Class.forName(scheduleEntity.getModuleClass());
					ScheduleExecutor scheduleExecutor = (ScheduleExecutor) StaticContext.getBean(dynamicClass);
					scheduleExecutor.setId(scheduleEntity.getScheduleId());
					scheduleExecutor.setInterval(scheduleEntity.getInterval());
					this.scheduleTask.addTask(scheduleExecutor);
				}
			}

			if (this.scheduleTask.isExecutorExist()) {
				this.timer = new Timer();
				this.timer.schedule(this.scheduleTask, 0, Integer.parseInt(this.scheduleInterval));
			} else {
				logger.info("There is no scheduler to run. Check the schedule DB.");
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void finalize() {
		if(this.timer != null) {
			this.timer.cancel();
			this.timer.purge();
		}
	}
	
	public boolean isScheduleStart() {
		return this.timer != null ? true : false;
	}

	public void stop() {
		this.timer.cancel();
		this.timer = null;
		this.scheduleTask.clearTask();
	}
}