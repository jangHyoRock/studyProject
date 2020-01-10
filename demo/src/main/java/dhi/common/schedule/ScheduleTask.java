package dhi.common.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import dhi.common.schedule.executor.ScheduleExecutor;

/*
 * Classes that register and run tasks in the scheduler.
 */
public class ScheduleTask extends TimerTask {
	
	private ArrayList<ScheduleExecutor> scheduleExecutorList;
		
	public ScheduleTask() {
		this.scheduleExecutorList = new ArrayList<ScheduleExecutor>();
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

	@Override
	public void run() {
	
		Calendar cal = Calendar.getInstance();
				
		for (ScheduleExecutor scheduleExecutor : this.scheduleExecutorList) {
			
			cal.setTime(scheduleExecutor.getStartDate());
			cal.add(Calendar.MILLISECOND, scheduleExecutor.getInterval());
			int dateExpireCompare = new Date().compareTo(cal.getTime());
			if (dateExpireCompare >= 0) {
				scheduleExecutor.execute();
			}
		}
	}
}
