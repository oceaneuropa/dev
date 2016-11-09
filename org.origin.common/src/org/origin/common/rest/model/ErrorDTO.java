package org.origin.common.rest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for Error
 *
 */
@XmlRootElement
public class ErrorDTO {

	@XmlElement
	protected String code;
	@XmlElement
	protected String message;
	@XmlElement
	protected String exception;

	public ErrorDTO() {
	}

	/**
	 * 
	 * @param message
	 */
	public ErrorDTO(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @param code
	 * @param message
	 */
	public ErrorDTO(String code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * 
	 * @param code
	 * @param message
	 * @param exception
	 */
	public ErrorDTO(String code, String message, String exception) {
		this.code = code;
		this.message = message;
		this.exception = exception;
	}

	@XmlElement
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElement
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}
