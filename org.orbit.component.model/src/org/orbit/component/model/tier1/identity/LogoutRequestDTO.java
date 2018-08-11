package org.orbit.component.model.tier1.identity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogoutRequestDTO {

	@XmlElement
	protected String tokenType;
	@XmlElement
	protected String tokenValue;

	@XmlElement
	public String getTokenType() {
		return this.tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	@XmlElement
	public String getTokenValue() {
		return this.tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

}
