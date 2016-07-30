package org.nb.mgm.exception;

public class ManagementException extends Exception {

	private static final long serialVersionUID = 8431290190713404092L;

	protected String code;

	public ManagementException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ManagementException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ManagementException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
