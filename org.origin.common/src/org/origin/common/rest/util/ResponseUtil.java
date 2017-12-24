package org.origin.common.rest.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getSimpleValue(Response response, String attributeName, Class<T> clazz) throws ClientException {
		T t = null;
		try {
			if (response != null) {
				String responseString = response.readEntity(String.class);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Map<?, ?> result = mapper.readValue(responseString, Map.class);
				if (result != null) {
					Object value = result.get(attributeName);
					if (value != null && clazz.isAssignableFrom(value.getClass())) {
						t = (T) value;
					}
				}
			}
		} catch (Exception e) {
			throw new ClientException(500, e.getMessage(), e);
		}
		return t;
	}

	/**
	 * Convert Exception to Error DTO.
	 * 
	 * @param e
	 * @param errorCode
	 * @return
	 */
	public static ErrorDTO toDTO(Exception e, String errorCode) {
		if (e == null) {
			return null;
		}

		ErrorDTO dto = new ErrorDTO();

		dto.setCode(errorCode);
		dto.setMessage(e.getMessage());

		if (e.getCause() != null) {
			String causeName = e.getCause().getClass().getName();
			String causeMessage = e.getCause().getMessage();
			dto.setDetail(causeName + " " + causeMessage);
		} else {
			String causeName = e.getClass().getName();
			dto.setDetail(causeName);
		}

		return dto;
	}

	/**
	 * Check whether a Response is successful or not.
	 * 
	 * @param response
	 * @return Return true if the Response is successful. Otherwise, return false.
	 */
	public static boolean isSuccessful(Response response) {
		if (response != null && response.getStatusInfo() != null) {
			if (Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Close the Closeable objects and <b>ignore</b> any {@link IOException} or null pointers. Must only be used for cleanup in exception handlers.
	 * 
	 * @param response
	 *            Close javax.ws.rs.core.Response.
	 * @param printStackTrace
	 */
	public static void closeQuietly(Response response, boolean printStackTrace) {
		try {
			if (response != null) {
				response.close();
			}
		} catch (Exception ioe) {
			if (printStackTrace) {
				ioe.printStackTrace();
			} else {
				// ignore
			}
		}
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	public static Responses parseResponses(Response response) {
		Responses responses = null;
		try {
			responses = response.readEntity(Responses.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (responses == null) {
			String responseString = response.readEntity(String.class);
			responses = parseResponses(responseString);
		}
		return responses;
	}

	/**
	 * {"requestName":"add_machine_config","parameters":{"machineId":"m006","name":"machine006","ipAddress":"192.168.0.6"},"machineId":"m006","name":
	 * "machine006","ipAddress":"192.168.0.6"}
	 * 
	 * @param requestString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Request parseRequest(String requestString) {
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

	/**
	 * 
	 * @param responsesString
	 * @return
	 */
	public static Responses parseResponses(String responsesString) {
		Responses responses = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Map<String, Object> map = mapper.readValue(responsesString, Map.class);
			if (map != null) {
				responses = new Responses();

				// requestName
				String requestName = null;
				Object requestNameObj = map.get("requestName");
				if (requestNameObj != null) {
					requestName = requestNameObj.toString();
				}
				responses.setRequestName(requestName);

				// responseMap
				Object responseMapObj = map.get("responseMap");
				if (responseMapObj instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Object> responseMap = (Map<String, Object>) responseMapObj;

					for (Iterator<String> keyItor = responseMap.keySet().iterator(); keyItor.hasNext();) {
						String key = keyItor.next();
						Object value = responseMap.get(key);

						org.origin.common.rest.model.Response response = convertToResponse(value);
						if (response != null) {
							responses.setResponse(key, response);
						} else {
							responses.setResponse(key, value);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return responses;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected static org.origin.common.rest.model.Response convertToResponse(Object value) {
		org.origin.common.rest.model.Response response = null;
		if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;

			String status = null;
			String message = null;
			Throwable exception = null;

			Object statusObj = map.get("status");
			if (statusObj != null) {
				status = statusObj.toString();
			}

			Object messageObj = map.get("message");
			if (messageObj != null) {
				message = messageObj.toString();
			}

			Object exceptionObj = map.get("exception");
			if (exceptionObj != null) {
				exception = convertToThrowable(exceptionObj);
			}

			Object bodyObj = map.get("body");

			if (status != null && (message != null || exception != null)) {
				response = new org.origin.common.rest.model.Response(status, message, exception);
				response.setBody(bodyObj);
			}
		}
		return response;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected static Throwable convertToThrowable(Object value) {
		Throwable throwable = null;
		if (value instanceof Throwable) {
			throwable = (Throwable) value;

		} else if (value instanceof Map) {
			Map<?, ?> causeMap = (Map<?, ?>) value;

			while (causeMap != null) {
				if (causeMap.containsKey("cause") && causeMap.containsKey("stackTrace")) {
					// cause
					String cause = null;
					Object causeObj = causeMap.get("cause");
					if (causeObj != null) {
						cause = causeObj.toString();
					}

					// stackTrace
					List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>();
					Object stackTraceObj = causeMap.get("stackTrace");
					if (stackTraceObj instanceof List) {
						List<?> stackTraceList = (List<?>) stackTraceObj;
						for (Object stackTraceItem : stackTraceList) {
							if (stackTraceItem instanceof Map) {
								Map<?, ?> stackTraceMap = (Map<?, ?>) stackTraceItem;

								String declareClassName = (String) stackTraceMap.get("className");
								String methodName = (String) stackTraceMap.get("methodName");
								String fileName = (String) stackTraceMap.get("fileName");
								int lineNumber = (int) stackTraceMap.get("lineNumber");

								StackTraceElement stackTraceElement = new StackTraceElement(declareClassName, methodName, fileName, lineNumber);
								stackTraceElements.add(stackTraceElement);
							}
						}
					}

					if (causeObj != null || stackTraceObj != null) {
						Exception exception = new Exception(cause);
						exception.setStackTrace(stackTraceElements.toArray(new StackTraceElement[stackTraceElements.size()]));

						throwable = exception;
						break;
					}
				}

				if (causeMap.get("cause") instanceof Map) {
					causeMap = (Map<?, ?>) causeMap.get("cause");
				}
			}
		}
		return throwable;
	}

}
