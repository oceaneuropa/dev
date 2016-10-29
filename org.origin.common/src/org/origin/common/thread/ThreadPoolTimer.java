package org.origin.common.thread;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPoolTimer {

	// monitor every 10 seconds
	protected static long DEFAULT_TIMER_INTERVAL_TIME_MILLS = 10 * 1000; // 10 seconds
	protected static int DEFAULT_MIN_POOL_SIZE = 0;
	protected static int DEFAULT_MAX_POOL_SIZE = 10;
	protected static int DEFAULT_KEEP_ALIVE_SECONDS = 20;

	protected String name;
	protected Runnable runnable;

	protected ThreadPoolExecutor threadPoolExecutor;
	protected int minPoolSize = DEFAULT_MIN_POOL_SIZE;
	protected int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
	protected int keepAliveSeconds = DEFAULT_KEEP_ALIVE_SECONDS;

	protected Timer timer;
	protected long timerInterval = DEFAULT_TIMER_INTERVAL_TIME_MILLS;

	protected AtomicBoolean isStarted = new AtomicBoolean(false);
	protected long lastExecTime;

	/**
	 * 
	 * @param name
	 * @param runnable
	 */
	public ThreadPoolTimer(String name, Runnable runnable) {
		if (runnable == null) {
			throw new IllegalArgumentException("runnable is null");
		}
		this.name = name;
		this.runnable = runnable;
	}

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}

	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}

	public long getInterval() {
		return this.timerInterval;
	}

	public void setInterval(long timerInterval) {
		this.timerInterval = timerInterval;
	}

	protected ThreadPoolExecutor createThreadPoolExecutor() {
		ThreadFactory threadFactory = getThreadFactory();

		// corePoolSize (0)
		// --- the number of threads to keep in the pool, even if they are idle, unless allowCoreThreadTimeOut is set.
		// maximumPoolSize (10)
		// --- max number of pooled threads
		// keepAliveTime (20 seconds)
		// --- when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating.
		// workQueue (SynchronousQueue)
		// --- the queue to use for holding tasks before they are executed. This queue will hold only the Runnable tasks submitted by the execute method.
		// threadFactory (ThreadFactory)
		// --- the factory to use when the executor creates a new thread

		// This threadPoolExecutor doesn't keep idle threads in the pool.
		// Every idle thread wait for 20 seconds and then gets terminated.
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(this.minPoolSize, this.maxPoolSize, this.keepAliveSeconds, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
		threadPoolExecutor.allowCoreThreadTimeOut(true);
		return threadPoolExecutor;
	}

	protected ThreadFactory getThreadFactory() {
		ThreadFactory threadFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable runnable) {
				Thread thread = new Thread(runnable, name + " Worker");
				thread.setDaemon(true);
				return thread;
			}
		};
		return threadFactory;
	}

	/**
	 * Check whether the monitor has been started and is running.
	 * 
	 * @return
	 */
	public boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	/**
	 * Start monitoring.
	 */
	public synchronized void start() {
		if (this.isStarted.get()) {
			return;
		}
		this.isStarted.set(true);

		// 1. Create thread pool executor to async execute monitoring tasks
		// This threadPoolExecutor doesn't keep idle threads in the pool.
		// Every idle thread wait for 20 seconds and then gets terminated.
		this.threadPoolExecutor = createThreadPoolExecutor();

		// 2. Create Timer which executes a TimerTask (Runnable) every 10 seconds (determined by the monitorInterval)
		// When a TimerTask runs, it uses the thread pool executor to execute a runnable to monitor the IndexService.
		if (this.timerInterval > 0) {
			this.timer = new Timer(name + " Timer", true); // daemon timer
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					long now = System.currentTimeMillis();
					try {
						// time elapsed since last running
						long elapsed = (now - lastExecTime);

						if (lastExecTime != 0) {
							System.out.println(toString() + " " + elapsed + " ms elapsed since last running.");
						}

						// If less than 20% of the interval time elapsed since last monitoring, abort this round of monitoring.
						// Perhaps the timer executed previous timer task late and then executes this timer task on time, which causes the time elapsed between two rounds of monitoring to be small.
						if (elapsed < (timerInterval * 2 / 10)) {
							return;
						}

						threadPoolExecutor.execute(new Runnable() {
							@Override
							public void run() {
								try {
									runnable.run();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} finally {
						lastExecTime = now;
					}
				}

				@Override
				public String toString() {
					return name + " TimerTask";
				}
			};
			// task (monitorTimerTask)
			// --- task to be scheduled.
			// delay (0)
			// --- delay in milliseconds before task is to be executed.
			// period (monitorInterval)
			// --- time in milliseconds between successive task executions.
			this.timer.schedule(timerTask, 0, this.timerInterval);
		}
	}

	/**
	 * Stop monitoring.
	 */
	public synchronized void stop() {
		if (this.isStarted.compareAndSet(true, false)) {
			// 1. Stop the Timer
			if (this.timer != null) {
				this.timer.cancel();
				this.timer = null;
			}

			// 2. Shutdown the thread pool executor
			if (this.threadPoolExecutor != null) {
				ThreadPoolUtils.shutdown(this.threadPoolExecutor);
				this.threadPoolExecutor = null;
			}
		}
	}

}
