package org.orbit.os.runtime.world;

public class WorldException extends Exception {

	private static final long serialVersionUID = 3731496982302201008L;

	protected String code;

	public WorldException(String code, String message) {
		super(message);
		this.code = code;
	}

	public WorldException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public WorldException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
