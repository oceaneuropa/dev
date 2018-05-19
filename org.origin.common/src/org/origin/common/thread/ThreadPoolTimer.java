package org.origin.common.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see org.apache.activemq.transport.AbstractInactivityMonitor
 *
 */
public class ThreadPoolTimer {

	protected static Logger LOG = LoggerFactory.getLogger(ThreadPoolTimer.class);
	protected static long DEFAULT_TIMER_INTERVAL_TIME_MILLS = 10 * 1000; // 10 seconds

	protected String name;
	protected Runnable runnable;
	protected long timerInterval;

	protected ScheduledExecutorService scheduler;
	protected ScheduledFuture<?> scheduleHandle;

	protected AtomicBoolean isStarted = new AtomicBoolean(false);
	protected boolean debug;

	/**
	 * 
	 * @param name
	 */
	public ThreadPoolTimer(String name) {
		this.name = name;
		this.timerInterval = DEFAULT_TIMER_INTERVAL_TIME_MILLS;
	}

	public long getInterval() {
		return this.timerInterval;
	}

	public void setInterval(long timerInterval) {
		this.timerInterval = timerInterval;
	}

	public String getName() {
		return this.name;
	}

	public Runnable getRunnable() {
		return this.runnable;
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public synchronized void start() {
		if (this.runnable == null) {
			throw new IllegalArgumentException("Runnable is null");
		}

		if (this.isStarted.get()) {
			return;
		}
		this.isStarted.set(true);

		this.scheduler = Executors.newScheduledThreadPool(1);

		Runnable runner = new Runnable() {
			public void run() {
				// if (debug) {
				LOG.debug(name + " -> run.");
				// }
				runnable.run();
			}
		};
		this.scheduleHandle = this.scheduler.scheduleAtFixedRate(runner, 0, this.timerInterval, TimeUnit.MILLISECONDS);
	}

	public synchronized void stop() {
		if (this.isStarted.compareAndSet(true, false)) {

			if (scheduleHandle != null) {
				this.scheduler.schedule(new Runnable() {
					public void run() {
						scheduleHandle.cancel(true);
					}
				}, 0, TimeUnit.SECONDS);
				this.scheduleHandle = null;

				// this.scheduler.shutdown();
				this.scheduler.shutdownNow();
			}

			dispose();
		}
	}

	public void dispose() {

	}

}
