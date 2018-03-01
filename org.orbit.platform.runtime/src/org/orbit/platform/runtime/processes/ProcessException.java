package org.orbit.platform.runtime.processes;

public class ProcessException extends Exception {

	private static final long serialVersionUID = 3037913661930790958L;

	public ProcessException(String message) {
		super(message);
	}

	public ProcessException(String message, Throwable cause) {
		super(message, cause);
	}

}
