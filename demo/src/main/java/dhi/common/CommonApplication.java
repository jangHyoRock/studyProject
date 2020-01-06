package dhi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import dhi.common.schedule.ScheduleService;

@SpringBootApplication
@EnableJpaAuditing
public class CommonApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(CommonApplication.class);
	
	@Autowired
	ScheduleService scheduleService;
	
	@Value("${schedule.use}")
	private String scheduleUse;
	
	@Value("${schedule.system.start}")
	private String scheduleStart;
	
	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
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
	}
}
