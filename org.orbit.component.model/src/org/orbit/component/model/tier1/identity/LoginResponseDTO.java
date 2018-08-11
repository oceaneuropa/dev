package org.orbit.component.model.tier1.identity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginResponseDTO {

	@XmlElement
	protected boolean succeed;
	@XmlElement
	protected String message;
	@XmlElement
	protected String tokenType;
	@XmlElement
	protected String tokenValue;

	@XmlElement
	public boolean isSucceed() {
		return this.succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

	@XmlElement
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElement
	public String getTokenType() {
		return this.tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@XmlElement
	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

}
