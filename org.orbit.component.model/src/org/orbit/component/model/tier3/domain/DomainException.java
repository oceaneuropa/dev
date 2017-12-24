package org.orbit.component.model.tier3.domain;

public class DomainException extends Exception {

	private static final long serialVersionUID = 1930133737582854014L;

	protected String code;

	public DomainException(String code, String message) {
		super(message);
		this.code = code;
	}

	public DomainException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public DomainException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
