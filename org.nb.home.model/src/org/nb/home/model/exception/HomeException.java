package org.nb.home.model.exception;

public class HomeException extends Exception {

	private static final long serialVersionUID = -4464851578103932938L;

	protected String code;

	public HomeException(String code, String message) {
		super(message);
		this.code = code;
	}

	public HomeException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public HomeException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
