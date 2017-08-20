package org.orbit.component.model.tier3.transferagent;

public class TransferAgentException extends Exception {

	private static final long serialVersionUID = 5108346096229011906L;

	protected String code;

	public TransferAgentException(String code, String message) {
		super(message);
		this.code = code;
	}

	public TransferAgentException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public TransferAgentException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
