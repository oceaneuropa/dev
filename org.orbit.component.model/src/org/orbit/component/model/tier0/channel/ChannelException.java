package org.orbit.component.model.tier0.channel;

public class ChannelException extends Exception {

	private static final long serialVersionUID = 166216801254730288L;

	protected String code;

	public ChannelException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ChannelException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ChannelException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
