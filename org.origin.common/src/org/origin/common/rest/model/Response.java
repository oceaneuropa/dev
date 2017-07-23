package org.origin.common.rest.model;

public class Response {

	/** Constants for status */
	public static String SUCCESS = "success";
	public static String FAILURE = "failure";
	public static String EXCEPTION = "exception";

	protected String status;
	protected String message;
	protected Throwable exception;
	protected Object body;

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
	 * @param exception
	 */
	public Response(String status, String message, Throwable exception) {
		this.status = status;
		this.message = message;
		this.exception = exception;
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

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public boolean hasException() {
		return (this.exception != null) ? true : false;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", message=" + message + ", exception=" + exception + "]";
	}

	public String getSimpleLabel() {
		String label = "status=" + status + ", message=" + message;
		if (exception != null) {
			label += ", exception=" + exception.getMessage() + "]";
		}
		return label;
	}

}
