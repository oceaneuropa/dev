package org.origin.common.launch.stream;

public interface StreamListener {

	void streamAppended(String text, StreamMonitor monitor);

}
