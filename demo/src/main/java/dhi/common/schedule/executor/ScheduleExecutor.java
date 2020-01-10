package dhi.common.schedule.executor;

import java.lang.Thread.State;
import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dhi.common.repository.ScheduleHistoryRepository;
import dhi.common.repository.ScheduleRepository;
import dhi.common.schedule.ScheduleStatus;
import dhi.common.util.CommonConst;
import dhi.common.util.Utilities;

/*
 * An abstract class for each Executor that handles all common tasks. 
 */
public abstract class ScheduleExecutor implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleExecutor.class);
	private static final long defaultDate = 946684800000L;
	
	@Autowired
	protected EntityManagerFactory emFactory;
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private ScheduleHistoryRepository scheduleHistoryRepository;
		
	private Thread executorThread;	
	private int id;
	private Date startDate;
	private Date endDate;	
	private int interval;
	private int status;
		
	public ScheduleExecutor() {		
		this.startDate = new Date(defaultDate);
		this.executorThread = new Thread(this);
	};
	
	public ScheduleExecutor(int id, int interval) {
		this();

		this.id = id;
		this.interval = interval;
	}
	
	@Override
	public void run() {
		try {

			this.startDate = new Date();
			this.scheduleRepository.updateStartDTAndStatusByScheduleId(this.getId(), this.startDate, ScheduleStatus.START.getValue());
			this.scheduleHistoryRepository.insertAll(this.getId(), this.startDate, null, ScheduleStatus.START.getValue(), CommonConst.StringEmpty);

			start();

			this.endDate = new Date();
			this.scheduleRepository.updateEndDTAndStatusByScheduleId(this.getId(), this.endDate, ScheduleStatus.END.getValue());
			this.scheduleHistoryRepository.updateEndDTAndStatusByScheduleIdAndStartDT(this.getId(), this.startDate, this.endDate, ScheduleStatus.END.getValue());
		} catch (Exception e) {
			String errorMessage = Utilities.getStackTrace(e);
			errorMessage = errorMessage.substring(0, Math.min(5000, errorMessage.length()));
			
			this.scheduleRepository.updateEndDTAndStatusByScheduleId(this.getId(), this.endDate, ScheduleStatus.ERROR.getValue());
			this.scheduleHistoryRepository.updateEndDTAndStatusAndErrorByScheduleIdAndStartDT(this.getId(), this.startDate, this.endDate, ScheduleStatus.ERROR.getValue(), errorMessage);
		}
	}
	
	public void execute() {
			
		try {
			if(!this.executorThread.isAlive())
			{			
				if(this.executorThread.getState() == State.NEW) {
					this.executorThread.start();
				} else {
					this.executorThread = new Thread(this);
					this.executorThread.start();
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {	
		this.id = id;
	}
	
	public Date getStartDate() {
		return this.startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}
	
	public void setEndDate(Date endDate) {	
		this.endDate = endDate;
	}
	
	public int getInterval() {
		return this.interval;
	}
	
	public void setInterval(int interval) {	
		this.interval = interval;
	}
	
	public long getStatus() {
		return this.status;
	}
	
	public void setStatus(int status) {	
		this.status = status;
	}

	public abstract void start ();
}
