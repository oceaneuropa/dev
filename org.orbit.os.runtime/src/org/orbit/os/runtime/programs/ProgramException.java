package org.orbit.os.runtime.programs;

public class ProgramException extends Exception {

	private static final long serialVersionUID = -2303760055056636242L;

	public ProgramException() {
	}

	public ProgramException(String message) {
		super(message);
	}

	public ProgramException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProgramException(Throwable cause) {
		super(cause);
	}

	protected ProgramException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
