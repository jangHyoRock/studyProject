package dhi.optimizer.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import dhi.optimizer.model.db.ControlEntity;
import dhi.optimizer.repository.ControlRepository;
import dhi.optimizer.schedule.executor.ScheduleExecutor;

/**
 * Classes that register and run tasks in the scheduler.
 */
public class ScheduleTask extends TimerTask {
	
	private ControlRepository controlRepository;
	private List<ScheduleExecutor> scheduleExecutorList;
		
	public ScheduleTask(ControlRepository controlRepository) {		
		this.scheduleExecutorList = new ArrayList<ScheduleExecutor>();
		this.controlRepository = controlRepository;
	}
	
	public void addTask(ScheduleExecutor executor) {
		this.scheduleExecutorList.add(executor);
	}
	
	public void clearTask() {
		this.scheduleExecutorList.clear();
	}
	
	public boolean isExecutorExist() {
		return this.scheduleExecutorList.size() > 0 ? true : false;
	}
	
	public List<ScheduleExecutor> getScheduleExecutorList() {
		return this.scheduleExecutorList;
	}
	
	@Override
	public void run() {		
		ControlEntity controlEntity = this.controlRepository.findControl();
		for (ScheduleExecutor scheduleExecutor : this.scheduleExecutorList) {
			
			if (!scheduleExecutor.isAlWaysRun() && !controlEntity.getSystemStart()) {				
				continue;
			}
				
			int dateExpireCompare = new Date().compareTo(scheduleExecutor.getRunAvailableDate());
			if (dateExpireCompare >= 0) {
				scheduleExecutor.execute();
			}
		}
	}
}
