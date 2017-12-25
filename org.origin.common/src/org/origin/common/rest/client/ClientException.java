package org.origin.common.rest.client;

import java.util.Map;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientException extends Exception {

	private static final long serialVersionUID = -688490128103671645L;

	protected int code;

	protected String errorCode = null;
	protected String errorMsg = null;
	protected String errorDetail = null;

	/**
	 * 
	 * @param code
	 * @param cause
	 */
	public ClientException(int code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * 
	 * @param code
	 * @param message
	 * @param cause
	 */
	public ClientException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * 
	 * @param response
	 */
	public ClientException(Response response) {
		super(response.getStatusInfo().getReasonPhrase(), null);
		this.code = response.getStatus();

		try {
			if (response != null) {
				String responseString = response.readEntity(String.class);
				if (responseString != null) {
					String errorCode = null;
					String errorMsg = null;
					String errorDetail = null;

					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

					Map<?, ?> result = mapper.readValue(responseString, Map.class);
					if (result != null) {
						Object errorCodeObject = result.get("code");
						Object errorMsgObject = result.get("message");
						Object errorDetailObject = result.get("detail");

						if (errorCodeObject != null) {
							errorCode = errorCodeObject.toString();
						}
						if (errorMsgObject != null) {
							errorMsg = errorMsgObject.toString();
						}
						if (errorDetailObject != null) {
							errorDetail = errorDetailObject.toString();
						}
					}

					this.errorCode = errorCode;
					this.errorMsg = errorMsg;
					this.errorDetail = errorDetail;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getMessage() {
		try {
			if (hasErrorCode() || hasErrorMsg() || hasErrorDetail()) {
				String fullMessage = "";
				if (hasErrorCode()) {
					fullMessage += this.errorCode;

					if (!fullMessage.endsWith(":") && !fullMessage.endsWith(".")) {
						if (hasErrorMsg() || hasErrorDetail()) {
							fullMessage += ":";
						} else {
							fullMessage += ".";
						}
					}
				}

				if (hasErrorMsg()) {
					if (!fullMessage.isEmpty()) {
						fullMessage += " ";
					}
					fullMessage += this.errorMsg;
					if (!fullMessage.endsWith(".")) {
						fullMessage += ".";
					}
				}

				if (hasErrorDetail()) {
					if (!fullMessage.isEmpty()) {
						fullMessage += " ";
					}
					fullMessage += this.errorDetail;
					if (!fullMessage.endsWith(".")) {
						fullMessage += ".";
					}
				}

				if (!fullMessage.isEmpty()) {
					return fullMessage;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.getMessage();
	}

	public int getCode() {
		return this.code;
	}

	public boolean hasErrorCode() {
		return (this.errorCode != null && !this.errorCode.isEmpty()) ? true : false;
	}

	public boolean hasErrorMsg() {
		return (this.errorMsg != null && !this.errorMsg.isEmpty()) ? true : false;
	}

	public boolean hasErrorDetail() {
		return (this.errorDetail != null && !this.errorDetail.isEmpty()) ? true : false;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public String getErrorDetail() {
		return this.errorDetail;
	}

	@Override
	public String toString() {
		return ClientException.class.getName() + ": " + getMessage() + " (" + getCode() + ")";
	}

}
