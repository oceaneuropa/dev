package org.orbit.component.model.tier1.account;

public class UserRegistryException extends Exception {

	private static final long serialVersionUID = -6204890275809282715L;

	protected String code;

	public UserRegistryException(String code, String message) {
		super(message);
		this.code = code;
	}

	public UserRegistryException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public UserRegistryException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
