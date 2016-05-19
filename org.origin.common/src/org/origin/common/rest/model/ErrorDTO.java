package org.origin.common.rest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for Error
 *
 */
@XmlRootElement
public class ErrorDTO {

	public static ErrorDTO newInstance(String message) {
		return new ErrorDTO(message);
	}

	public static ErrorDTO newInstance(String code, String message) {
		return new ErrorDTO(code, message);
	}

	public static ErrorDTO newInstance(String code, String message, String exception) {
		return new ErrorDTO(code, message, exception);
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
			dto.setException(causeName + " " + causeMessage);
		} else {
			String causeName = e.getClass().getName();
			dto.setException(causeName);
		}

		return dto;
	}

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
