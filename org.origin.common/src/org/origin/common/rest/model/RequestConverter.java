package org.origin.common.rest.model;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestConverter {

	/**
	 * {"requestName":"add_machine_config","parameters":{"machineId":"m006","name":"machine006","ipAddress":"192.168.0.6"},"machineId":"m006","name":
	 * "machine006","ipAddress":"192.168.0.6"}
	 * 
	 * @param requestString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Request parse(String requestString) {
		Request request = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Map<String, Object> result = mapper.readValue(requestString, Map.class);

			if (result != null && result.containsKey("requestName")) {
				String requestName = (String) result.get("requestName");
				Map<String, Object> parameters = null;
				if (result.containsKey("parameters")) {
					parameters = (Map<String, Object>) result.get("parameters");
				}

				if (requestName != null && !requestName.isEmpty()) {
					request = new Request(requestName, parameters);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return request;
	}

}
