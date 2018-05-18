package org.origin.common.launch.stream;

import java.io.IOException;

public interface StreamProxy {

	StreamMonitor getOutputStreamMonitor();

	StreamMonitor getErrorStreamMonitor();

	void write(String input) throws IOException;

	void closeInputStream() throws IOException;

}
