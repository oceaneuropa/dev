package org.origin.common.util;

public class Timer {

	public static final long SECOND = 1000;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;

	private long totalTime = 0;
	private long startTime = 0;
	private long stopTime = 0;

	protected boolean isStarted = false;

	public void start() {
		if (this.isStarted) {
			return;
		}
		this.isStarted = true;

		this.startTime = System.currentTimeMillis();
	}

	public void stop() {
		if (!this.isStarted) {
			return;
		}
		this.isStarted = false;

		this.stopTime = System.currentTimeMillis();
		this.totalTime += (this.stopTime - this.startTime);
	}

	public void reset() {
		this.totalTime = 0;
		this.isStarted = false;
	}

	public long totalTime() {
		return this.totalTime;
	}

}
