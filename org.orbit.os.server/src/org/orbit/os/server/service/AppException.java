package org.orbit.os.server.service;

public class AppException extends Exception {

	private static final long serialVersionUID = -2303760055056636242L;

	public AppException() {
	}

	public AppException(String message) {
		super(message);
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(Throwable cause) {
		super(cause);
	}

	protected AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
