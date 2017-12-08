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
	protected String detail;

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
	 * @param detail
	 */
	public ErrorDTO(String code, String message, String detail) {
		this.code = code;
		this.message = message;
		this.detail = detail;
	}

	@XmlElement
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElement
	public String getDetail() {
		return this.detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
