package org.orbit.infra.api.datacast;

public class ChannelStatus {

	public static ChannelStatus EMPTY = new ChannelStatus(0);
	public static ChannelStatus STARTED = new ChannelStatus(1 << 1); // 0010
	public static ChannelStatus STOPPED = new ChannelStatus(1 << 2); // 0100
	public static ChannelStatus SUSPENDED = new ChannelStatus(1 << 3); // 1000

	protected int mode = 0;

	public ChannelStatus(int value) {
		this.mode = value;
	}

	public int getMode() {
		return this.mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public boolean contains(int singleMode) {
		if ((this.mode & singleMode) == singleMode) {
			return true;
		}
		return false;
	}

	public boolean contains(ChannelStatus singleStatus) {
		if (singleStatus == null) {
			return false;
		}
		if ((this.mode & singleStatus.getMode()) == singleStatus.getMode()) {
			return true;
		}
		return false;
	}

	public boolean isIncludedBy(int combinedStatusValue) {
		if ((this.mode & combinedStatusValue) == this.mode) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param combinedStatus
	 * @return
	 */
	public boolean isIncludedBy(ChannelStatus combinedStatus) {
		if (combinedStatus == null) {
			return false;
		}
		if ((this.mode & combinedStatus.getMode()) == this.mode) {
			return true;
		}
		return false;
	}

	public boolean isStarted() {
		return STARTED.isIncludedBy(this);
	}

	public boolean isStopped() {
		return STOPPED.isIncludedBy(this);
	}

	public boolean isSuspended() {
		return SUSPENDED.isIncludedBy(this);
	}

}
