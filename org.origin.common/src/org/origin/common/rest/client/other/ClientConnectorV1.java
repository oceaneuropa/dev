package org.origin.common.rest.client.other;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.origin.common.rest.client.Pingable;
import org.origin.common.util.DateUtil;
import org.origin.common.util.TimeUtil;

public class ClientConnectorV1 {

	// ping again in 3 seconds
	public static long DEFAULT_PING_INTERVAL = 3;

	protected Pingable pingable;
	protected long pingInterval;
	protected TimeUnit pingTimeUnit;
	protected ScheduledThreadPoolExecutor scheduler;
	protected ScheduledFuture<?> future;

	protected boolean isStarted;
	protected boolean isStarting;
	protected boolean isStopping;
	protected boolean isDisposed;
	protected boolean isDisposing;

	protected Map<Integer, Date> pingStatusMap;

	/**
	 * 
	 * @param pingable
	 * @param pingInterval
	 * @param pingTimeUnit
	 */
	public ClientConnectorV1(Pingable pingable, long pingInterval, TimeUnit pingTimeUnit) {
		this.pingable = pingable;
		this.pingInterval = checkPingInterval(pingInterval);
		this.pingTimeUnit = pingTimeUnit;
		this.pingStatusMap = new LinkedHashMap<Integer, Date>();

		// this.scheduler = Executors.newScheduledThreadPool(1);
		this.scheduler = new ScheduledThreadPoolExecutor(1);
		this.scheduler.setRemoveOnCancelPolicy(true);
	}

	protected long checkPingInterval(long pingInterval) {
		if (pingInterval <= 0) {
			return DEFAULT_PING_INTERVAL;
		}
		return pingInterval;
	}

	public long getPingInterval() {
		return pingInterval;
	}

	public TimeUnit getPingTimeUnit() {
		return pingTimeUnit;
	}

	/**
	 * 
	 * @param pingInterval
	 * @param pingTimeUnit
	 * @param reschedule
	 *            Whether to reschedule the ping runner. If reschedule is false, ping runner will not be rescheduled. If reschedule is true, ping runner will be
	 *            rescheduled when pingInterval or pingTimeUnit is changed.
	 */
	public synchronized void setPingInterval(long pingInterval, TimeUnit pingTimeUnit, boolean reschedule) {
		long oldPingInterval = this.pingInterval;
		TimeUnit oldPingTimeUnit = this.pingTimeUnit;

		this.pingInterval = pingInterval;
		this.pingTimeUnit = pingTimeUnit;

		if (reschedule) {
			if (oldPingInterval != pingInterval || oldPingTimeUnit != pingTimeUnit) {
				stop();
				start();
			}
		}
	}

	public synchronized boolean start() {
		if (isDisposed() || isDisposing()) {
			System.err.println("ClientConnector has been disposed.");
			return false;
		}
		if (isStarted() || isStarting()) {
			System.err.println("ClientConnector is already started.");
			return false;
		}

		this.isStarting = true;
		try {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					ClientConnectorV1.this.ping();
				}
			};
			this.future = this.scheduler.scheduleAtFixedRate(runnable, 0, this.pingInterval, this.pingTimeUnit);

			this.isStarted = true;

		} finally {
			this.isStarting = false;
		}
		return true;
	}

	/**
	 * Cancel the ping runner.
	 * 
	 */
	public synchronized boolean stop() {
		if (isDisposed() || isDisposing()) {
			System.err.println("ClientConnector has been disposed.");
			return false;
		}
		if (isStopped() || isStopping()) {
			System.err.println("ClientConnector is already stopped.");
			return false;
		}

		this.isStarting = true;
		try {
			if (this.future != null) {
				this.future.cancel(false);
				this.future = null;
			}

			this.isStarted = false;
		} finally {
			this.isStopping = false;
		}
		return true;
	}

	/**
	 * Cancel the ping runner and terminate the scheduler.
	 */
	public synchronized void dispose() {
		if (isDisposed() || isDisposing()) {
			System.err.println("ClientConnector has been disposed.");
			return;
		}

		this.isDisposing = true;
		try {
			if (isStarted()) {
				stop();
			}

			try {
				this.scheduler.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.isDisposed = true;
		} finally {
			this.isDisposing = false;
		}
	}

	/**
	 * Let the client to ping the server and keep the ping result.
	 */
	protected void ping() {
		if (isDisposed() || isDisposing()) {
			System.err.println("ClientConnector has been disposed.");
			return;
		}
		if (isStopped() || isStopping()) {
			System.err.println("ClientConnector is already stopped.");
			return;
		}

		try {
			int pingStatus = this.pingable.ping();

			Date responseTime = new Date();
			String timeString = DateUtil.toString(responseTime, DateUtil.SIMPLE_DATE_FORMAT2);
			System.out.println("PING >>> >>> " + timeString + " " + this.pingable.toString() + " ping result: " + pingStatus);

			this.pingStatusMap.put(pingStatus, responseTime);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the latest last ResponseTime of all ping status larger than 0.
	 * 
	 * @return
	 */
	public Date getLastActiveResponseTime() {
		Date lastActiveTime = null;
		for (Iterator<Integer> pingStatusItor = this.pingStatusMap.keySet().iterator(); pingStatusItor.hasNext();) {
			int pingStatus = pingStatusItor.next();
			Date responseTime = this.pingStatusMap.get(pingStatus);
			if (pingStatus > 0) {
				if (lastActiveTime == null || responseTime.after(lastActiveTime)) {
					lastActiveTime = responseTime;
				}
			}
		}
		if (lastActiveTime == null) {
			lastActiveTime = new Date(Long.MIN_VALUE);
		}
		return lastActiveTime;
	}

	/**
	 * Returns last ResponseTime with specified ping status.
	 * 
	 * @param pingStatus
	 * @return
	 */
	public Date getLastActiveResponseTime(int pingStatus) {
		Date lastActiveTime = this.pingStatusMap.get(pingStatus);
		if (lastActiveTime == null) {
			lastActiveTime = new Date(Long.MIN_VALUE);
		}
		return lastActiveTime;
	}

	/**
	 * 
	 * @param expireDuration
	 * @param timeUnit
	 * @return
	 */
	public boolean isExpired(long expireDuration, TimeUnit timeUnit) {
		Date lastActiveTime = getLastActiveResponseTime();
		Date expireTime = TimeUtil.addTimeToDate(lastActiveTime, expireDuration, timeUnit);
		Date timeNow = new Date();
		if (timeNow.after(expireTime)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param pingStatus
	 * @param expireDuration
	 * @param timeUnit
	 * @return
	 */
	public boolean isExpired(int pingStatus, long expireDuration, TimeUnit timeUnit) {
		Date lastActiveTime = getLastActiveResponseTime(pingStatus);
		Date expireTime = TimeUtil.addTimeToDate(lastActiveTime, expireDuration, timeUnit);
		Date timeNow = new Date();
		if (timeNow.after(expireTime)) {
			return true;
		}
		return false;
	}

	public boolean isStarting() {
		return this.isStarting;
	}

	public boolean isStarted() {
		return this.isStarted;
	}

	public boolean isStopping() {
		return this.isStopping;
	}

	public boolean isStopped() {
		return this.isStarted ? false : true;
	}

	public boolean isDisposing() {
		return this.isDisposing;
	}

	public boolean isDisposed() {
		return this.isDisposed;
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(Long.MIN_VALUE));
		System.out.println(calendar.get(Calendar.ERA));

		Date d = new Date(Long.MIN_VALUE);
		DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy G HH:mm:ss Z");
		System.out.println(df.format(d));
	}

}
