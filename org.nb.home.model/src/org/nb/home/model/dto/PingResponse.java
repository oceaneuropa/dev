package org.nb.home.model.dto;

import java.util.Map;

import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;

public class PingResponse extends Response {

	protected Integer result;

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

	/**
	 * 
	 * @param responses
	 */
	public PingResponse(Responses responses) {
		super("ping", responses);
		Object response = responses.get("ping");
		if (response instanceof Map) {
			Map<?, ?> responseMap = (Map<?, ?>) response;
			if (responseMap.get("result") instanceof Integer) {
				this.result = (Integer) ((Map<?, ?>) response).get("result");
			}
		}
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

}
