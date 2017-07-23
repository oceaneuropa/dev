package org.nb.home.model.dto;

import org.origin.common.rest.model.Response;

public class PingResponse extends Response {

	protected Integer result;

	public PingResponse() {
	}

	/**
	 * 
	 * @param status
	 * @param message
	 * @param result
	 */
	public PingResponse(String status, String message, Integer result) {
		super(status, message);
		this.result = result;
	}

	/**
	 * 
	 * @param status
	 * @param message
	 * @param throwable
	 */
	public PingResponse(String status, String message, Throwable throwable) {
		super(status, message, throwable);
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

}
