package org.orbit.infra.runtime.datacast.service;

public class ChannelStatus {

	public static ChannelStatus STARTED = new ChannelStatus(1 << 1); // 0010
	public static ChannelStatus STOPPED = new ChannelStatus(1 << 2); // 0100
	public static ChannelStatus SUSPENDED = new ChannelStatus(1 << 3); // 1000

	protected int value = 0;

	public ChannelStatus(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean hasStatus(int combinedStatusValue) {
		if ((this.value & combinedStatusValue) == this.value) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param combinedStatus
	 * @return
	 */
	public boolean hasStatus(ChannelStatus combinedStatus) {
		if (combinedStatus == null) {
			return false;
		}
		if ((this.value & combinedStatus.value()) == this.value) {
			return true;
		}
		return false;
	}

	public boolean isStarted() {
		return STARTED.hasStatus(this);
	}

	public boolean isStopped() {
		return STOPPED.hasStatus(this);
	}

	public boolean isSuspended() {
		return SUSPENDED.hasStatus(this);
	}

}
