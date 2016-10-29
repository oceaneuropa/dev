package org.origin.common.thread;

import java.util.TimerTask;

/**
 * A TimeTask for a Runnable object
 *
 * @see org.apache.activemq.thread.SchedulerTimerTask
 */
public class SchedulerTimerTask extends TimerTask {
	private final Runnable task;

	public SchedulerTimerTask(Runnable task) {
		this.task = task;
	}

	public void run() {
		this.task.run();
	}
}
