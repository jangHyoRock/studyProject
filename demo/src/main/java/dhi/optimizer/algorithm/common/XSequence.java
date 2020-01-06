package dhi.optimizer.algorithm.common;

import java.lang.Thread.State;

public abstract class XSequence implements Runnable {
	
	private Thread threadForSequence = null;

	private int cyclicTime = 100; 
	
	private boolean needToStop = true;

	public Thread getThreadForSequence() {
		return threadForSequence;
	}

	public void setThreadForSequence(Thread threadForSequence) {
		this.threadForSequence = threadForSequence;
	}
	
	public int getCyclicTime() {
		return cyclicTime;
	}

	public void setCyclicTime(int cyclicTime) {
		this.cyclicTime = cyclicTime;
	}
	
	public boolean isNeedToStop() {
		return needToStop;
	}

	public void setNeedToStop(boolean needToStop) {
		this.needToStop = needToStop;
	}

	public State getState() {
		State state = this.threadForSequence.getState();
		return state;
	}
	
	public boolean isAlive() {
		boolean isAlive = this.threadForSequence.isAlive();
		return isAlive;
	}
	
	/**
	 * Constructor
	 */
	public XSequence() {
		threadForSequence = new Thread(this);
	}
	
	public XSequence(int cyclicTime) {
		this();
		this.cyclicTime = cyclicTime;
	}
	
	@Override
	public void run() {
		while(!needToStop) {
			try {
				
				Thread.sleep(cyclicTime);
				
				sequence();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void startSequence() {
		// start worker thread
		boolean isStarted = isAlive();
		if (!isStarted) {
			needToStop = false;
			if(threadForSequence.getState() == State.NEW) {
				threadForSequence.start();	
			} else {
				threadForSequence = new Thread(this);
				threadForSequence.start();	
			}
		}
	}

	public void stopSequence() {
		needToStop = true;
		try {
			threadForSequence.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		needToStop = false;
	}
	
	public abstract void sequence();
}