package org.orbit.component.model.tier1.config;

public class ConfigRegistryException extends Exception {

	private static final long serialVersionUID = -8616604582142916259L;

	protected String code;

	public ConfigRegistryException(String code, String message) {
		super(message);
		this.code = code;
	}

	public ConfigRegistryException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public ConfigRegistryException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
