package org.origin.common.rest.server;

public class ServerException extends Exception {

	private static final long serialVersionUID = -3670936481538756516L;
	protected String code;

	public ServerException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ServerException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ServerException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
