package dhi.optimizer.algorithm.common;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

public class XThreadPool implements UncaughtExceptionHandler {
	
	private int threadCount;
	private String errorMessage;
	List<Thread> threadList;
	boolean isLoop = false;
	boolean isComplete = false;
	private int poolSize = 2; 
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public XThreadPool(int threadCount) {
		this.threadCount = threadCount;
		this.threadList = new ArrayList<Thread>();
		this.isComplete = false;
		this.isLoop = true;
		this.poolSize = 2; // default poolSize.

		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public void submit(Runnable runnable) {
		try {
			Thread thread = new Thread(runnable);
			this.threadList.add(thread);
			thread.start();
		} catch (Exception e) {
			this.errorMessage = e.getMessage();
			this.threadList.clear();
			this.isLoop = false;
			this.isComplete = false;
		}
	}
	
	public void submitList(List<Thread> threadList, int poolSize ) {
		try {
			this.threadList = threadList;
			this.poolSize = poolSize;
			this.startThreadPool();
		} catch (Exception e) {
			this.errorMessage = e.getMessage();
			this.threadList.clear();
			this.isLoop = false;
			this.isComplete = false;
		}
	}	
	
	private void startThreadPool() {
		int threadIndex = 0;
		while (isLoop) {
			
			int cnt = 0;
			for (Thread thread : threadList) {
				if (thread.isAlive())
					cnt++;
			}

			if (cnt < this.poolSize) {
				Thread thread = this.threadList.get(threadIndex);
				thread.start();
				threadIndex++;
			}
			
			if(this.threadList.size() <= threadIndex)
				break;
		}
	}

	public boolean waitForComplete() {
		while (isLoop) {
			
			int cnt = 0;
			for (Thread thread : threadList) {
				if (!thread.isAlive())
					cnt++;
			}

			if (cnt == this.threadCount) {
				this.isLoop = false;
				this.isComplete = true;
			}
		}

		return isComplete;
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		this.isLoop = false;
		this.errorMessage = e.getMessage();
		this.isComplete = false;
	}
}
