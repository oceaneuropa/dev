package org.origin.common.launch.stream;

public interface StreamMonitor {

	void addListener(StreamListener listener);

	String getContents();

	void removeListener(StreamListener listener);

	public void flushContents();

	public void setBuffered(boolean buffer);

	public boolean isBuffered();

}
