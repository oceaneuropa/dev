package org.origin.common.launch;

import java.io.IOException;

import org.origin.common.launch.stream.StreamProxy;

public interface ProcessHandler {

	LaunchHandler getLaunch();

	StreamProxy getStreamProxy();

	boolean canTerminate();

	boolean isTerminated();

	void terminate() throws IOException;

	Process getSystemProcess();

}