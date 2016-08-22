package org.origin.common.rest.model;

import java.util.Map;

public class Response {

	public static String SUCCESS = "success";
	public static String FAILURE = "failure";
	public static String EXCEPTION = "exception";

	protected String status;
	protected String message;
	protected Throwable throwable;

	public Response() {
	}

	/**
	 * 
	 * @param status
	 * @param message
	 */
	public Response(String status, String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * 
	 * @param status
	 * @param message
	 * @param throwable
	 */
	public Response(String status, String message, Throwable throwable) {
		this.status = status;
		this.message = message;
		this.throwable = throwable;
	}

	/**
	 * 
	 * @param label
	 * @param responses
	 */
	public Response(String label, Responses responses) {
		Object response = responses.get(label);
		if (response instanceof Map) {
			Map<?, ?> responseMap = (Map<?, ?>) response;
			if (responseMap.get("status") instanceof String) {
				this.status = (String) responseMap.get("status");
			}
			if (responseMap.get("message") instanceof String) {
				this.message = (String) responseMap.get("message");
			}
			if (responseMap.get("throwable") instanceof Throwable) {
				this.throwable = (Throwable) responseMap.get("throwable");
			}
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public boolean hasException() {
		return (this.throwable != null) ? true : false;
	}

}
