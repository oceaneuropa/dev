package org.nb.home.model.dto;

import java.util.Map;

import org.origin.common.rest.model.Responses;

public class ResponseParser {

	public static ResponseParser INSTANCE = new ResponseParser();

	public static ResponseParser getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * @param responses
	 * @return
	 */
	public PingResponse parse(Responses responses) {
		PingResponse pingResponse = new PingResponse();

		Object response = responses.getResponse("ping");

		String status = null;
		String message = null;
		Throwable throwable = null;
		Integer result = null;

		if (response instanceof Map) {
			Map<?, ?> responseMap = (Map<?, ?>) response;
			if (responseMap.get("status") instanceof String) {
				status = (String) responseMap.get("status");
			}
			if (responseMap.get("message") instanceof String) {
				message = (String) responseMap.get("message");
			}
			if (responseMap.get("throwable") instanceof Throwable) {
				throwable = (Throwable) responseMap.get("throwable");
			}
			if (responseMap.get("result") instanceof Integer) {
				result = (Integer) ((Map<?, ?>) response).get("result");
			}
		}

		pingResponse.setStatus(status);
		pingResponse.setMessage(message);
		pingResponse.setException(throwable);
		pingResponse.setResult(result);

		return pingResponse;
	}

}
