package dhi.common.schedule.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dhi.common.repository.ConfigInfRepository;

@Service
public class CoalAutoTransferExecutor extends ScheduleExecutor {

	private static final Logger logger = LoggerFactory.getLogger(CoalAutoTransferExecutor.class.getSimpleName());

	@Autowired 
	public ConfigInfRepository configInfRepository; 
	
	public CoalAutoTransferExecutor() {
	};

	public CoalAutoTransferExecutor(int id, int interval) {
		super(id, interval);
	}
	
	@Override
	public void start() {
		logger.info("### " + CoalAutoTransferExecutor.class.getName() + " Start ###");
		try {
			this.configInfRepository.updateAutoCoalTransferNativeQuery();
		} catch (Exception e) {
			throw e;
		} finally {
			logger.info("### " + CoalAutoTransferExecutor.class.getName() + " End ###");
		}
	}
}
