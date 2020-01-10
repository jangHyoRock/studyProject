package dhi.optimizer.schedule.executor;

import java.lang.Thread.State;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import dhi.common.util.Utilities;

import dhi.optimizer.common.CommonConst;
import dhi.optimizer.model.db.SystemCheckEntity;
import dhi.optimizer.repository.ScheduleHistoryRepository;
import dhi.optimizer.repository.ScheduleRepository;
import dhi.optimizer.repository.SystemCheckRepository;
import dhi.optimizer.schedule.ScheduleStatus;

/**
 * An abstract class for each Executor that handles all common tasks. <br>
 * : 스케줄러 추상 클래스.
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
	
	@Autowired
	private SystemCheckRepository systemCheckRepository;
	
	private Thread executorThread;	
	private int id;	
	private Date runAvailableDate;
	private Date startDate;
	private Date endDate;	
	private int interval;
	private int status;
	private boolean isFirstRun;
	private boolean systemReadyCheck;
	private boolean alWaysRun;
	private boolean scheduleAtSkipToNext;
	private int scheduleDuringNotRunningCount;
		
	public ScheduleExecutor() {
		this.isFirstRun = true;
		this.runAvailableDate = new Date(defaultDate);
		this.executorThread = new Thread(this);
	};
	
	public ScheduleExecutor(int id, int interval, boolean systemReadyCheck) {
		this();

		this.id = id;
		this.interval = interval;
		this.systemReadyCheck = systemReadyCheck;
	}
	
	/**
	 * ScheduleExecutor Override 실행 함수.
	 */
	@Override
	public void run() {
		try {
			
			// 스케줄 시작시간 log 시작 날짜.
			this.startDate = new Date();
						
			// 최초  시작시간으로 BaseDate를 정의함. 
			 if (this.isFirstRun) {
				this.isFirstRun = false;
				this.runAvailableDate = this.startDate;
			}
			
			long diffDate = this.startDate.getTime() - this.runAvailableDate.getTime();
			int increaseNumber = (int)(diffDate / this.interval) + 1;
			
			// 정확한 기준시간에 InterVal을 더하여 다음 시간에 호출할 수 있돌
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.runAvailableDate);
			cal.add(Calendar.MILLISECOND, increaseNumber * this.interval);
			this.runAvailableDate = cal.getTime();
			
			if (this.isSystemReadyCheck() && !this.isSystemReady()) {
				logger.info("### System Ready is false. System check is required. ###");
				return;
			}			
			
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
	
	/**
	 * System Ready Check. <br>
	 * : 호출 시 DB를 실시간으로 체크함.
	 */
	private boolean isSystemReady() {		
		SystemCheckEntity systemCheckEntity = this.systemCheckRepository.findControl();
		return systemCheckEntity.getSystemReady();
	}
	
	/**
	 * ScheduleExecutor 실행 함수.
	 */
	public void execute() {
			
		try {
			if (!this.executorThread.isAlive()) {
				if (this.executorThread.getState() == State.NEW) {
					this.executorThread.start();
				} else {
					this.executorThread = new Thread(this);
					this.executorThread.start();
				}
				this.scheduleDuringNotRunningCount = 0;
			} else {
				this.scheduleDuringNotRunningCount++;
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
	
	public Date getRunAvailableDate() {
		return this.runAvailableDate;
	}

	public Date getStartDate() {
		return this.startDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
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

	public boolean isSystemReadyCheck() {
		return systemReadyCheck;
	}

	public void setSystemReadyCheck(boolean systemReadyCheck) {
		this.systemReadyCheck = systemReadyCheck;
	}
	
	public boolean isAlWaysRun() {
		return alWaysRun;
	}

	public void setAlWaysRun(boolean alWaysRun) {
		this.alWaysRun = alWaysRun;
	}

	public boolean isScheduleAtSkipToNext() {
		return scheduleAtSkipToNext;
	}

	public void setScheduleAtSkipToNext(boolean scheduleAtSkipToNext) {
		this.scheduleAtSkipToNext = scheduleAtSkipToNext;
	}

	public int getScheduleDuringNotRunningCount() {
		return scheduleDuringNotRunningCount;
	}

	public abstract void start ();
}
