package org.orbit.component.model.tier2.appstore;

public class AppStoreException extends Exception {

	private static final long serialVersionUID = -9034125720997858775L;

	protected String code;

	public AppStoreException(String code, String message) {
		super(message);
		this.code = code;
	}

	public AppStoreException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public AppStoreException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
