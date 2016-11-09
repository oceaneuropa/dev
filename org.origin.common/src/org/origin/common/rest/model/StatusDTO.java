package org.origin.common.rest.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * DTO for status
 *
 */
@XmlRootElement
public class StatusDTO {

	// @see https://httpstatuses.com/
	@XmlTransient
	public static final String RESP_200 = "200"; // OK
	@XmlTransient
	public static final String RESP_201 = "201"; // Created
	@XmlTransient
	public static final String RESP_304 = "304"; // Not Modified
	@XmlTransient
	public static final String RESP_400 = "400"; // Bad Request
	@XmlTransient
	public static final String RESP_404 = "404"; // Not Found
	@XmlTransient
	public static final String RESP_500 = "500"; // Internal Server Error
	@XmlTransient
	public static final String RESP_501 = "501"; // Not Implemented
	@XmlTransient
	public static final String RESP_502 = "502"; // Bad Gateway
	@XmlTransient
	public static final String RESP_503 = "503"; // Service Unavailable

	@XmlTransient
	public static final String SUCCESS = "success";
	@XmlTransient
	public static final String FAILED = "failed";

	@XmlElement
	protected String code;
	@XmlElement
	protected String message;
	@XmlElement
	protected String status;

	public StatusDTO() {
	}

	/**
	 * 
	 * @param code
	 * @param status
	 * @param message
	 */
	public StatusDTO(String code, String status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	@XmlElement
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlTransient
	public boolean success() {
		return (SUCCESS.equalsIgnoreCase(this.status)) ? true : false;
	}

}
