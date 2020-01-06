package dhi.optimizer.schedule;

import dhi.optimizer.common.StaticContext;

public class ScheduleExecutorThread implements Runnable {

	private IScheduleExecutorThread scheduleExecutorThread;	
	private Object param;	

	public ScheduleExecutorThread(Object param, Class<?> clazz) {
		this.param = param;
		this.scheduleExecutorThread = (IScheduleExecutorThread) StaticContext.getBean(clazz);	
	}

	public IScheduleExecutorThread getScheduleExecutorThread() {
		return scheduleExecutorThread;
	}

	@Override
	public void run() {
		this.scheduleExecutorThread.start((String) this.param);
	}
}

