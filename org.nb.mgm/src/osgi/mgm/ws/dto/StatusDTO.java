package osgi.mgm.ws.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for status
 *
 */
@XmlRootElement
public class StatusDTO {

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

}
