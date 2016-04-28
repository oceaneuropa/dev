package org.origin.common.rest.client;

public class ClientException extends Exception {

	private static final long serialVersionUID = -688490128103671645L;

	protected int code;

	/**
	 * 
	 * @param code
	 * @param message
	 * @param cause
	 */
	public ClientException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return ClientException.class.getName() + ": " + getMessage() + " (" + getCode() + ")";
	}

}
