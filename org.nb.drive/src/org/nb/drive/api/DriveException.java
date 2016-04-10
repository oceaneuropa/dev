package org.nb.drive.api;

public class DriveException extends Exception {

	private static final long serialVersionUID = -46588674474424443L;

	protected String code;

	public DriveException(String code, String message) {
		super(message);
		this.code = code;
	}

	public DriveException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public DriveException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
