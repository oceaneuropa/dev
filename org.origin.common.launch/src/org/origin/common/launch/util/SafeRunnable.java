package org.origin.common.launch.util;

public interface SafeRunnable {

	public void handleException(Throwable exception);

	public void run() throws Exception;

}
