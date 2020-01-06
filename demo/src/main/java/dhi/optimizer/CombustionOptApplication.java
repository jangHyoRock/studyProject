package dhi.optimizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import dhi.optimizer.common.StaticMap;
import dhi.optimizer.schedule.ScheduleService;

/**
 * Combustion Optimizer Application Start Run. <br>
 * : 연소최적화 Start Run.
 */
@SpringBootApplication
@EnableJpaAuditing
public class CombustionOptApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(CombustionOptApplication.class);
	
	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	StaticMap staticMap;
	
	@Value("${schedule.use}")
	private String scheduleUse;
	
	@Value("${schedule.system.start}")
	private String scheduleStart;
	
	public static void main(String[] args) {
		SpringApplication.run(CombustionOptApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		boolean isUse = Boolean.parseBoolean(this.scheduleUse);
		if(isUse) {
			boolean isScheduleStart = Boolean.parseBoolean(this.scheduleStart);
			if(isScheduleStart) {
				this.scheduleService.start();
			}
			else{
				logger.info("The entry in schedule.system.start is 'false' in application.properties");
			}
		}
		else
			logger.info("The entry in schedule.use is 'false' in application.properties");
				
		// Common map load
		this.staticMap.initializeMap();
	}
}
