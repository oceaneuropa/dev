package org.origin.common.launch.stream;

import org.origin.common.launch.util.SafeRunnable;

public class SafeRunner {

	public static void run(SafeRunnable code) {
		if (code == null) {
			return;
		}
		try {
			code.run();
		} catch (Exception e) {
			handleException(code, e);
		} catch (LinkageError e) {
			handleException(code, e);
		} catch (AssertionError e) {
			handleException(code, e);
		}
	}

	private static void handleException(SafeRunnable code, Throwable e) {
		code.handleException(e);
	}

}
