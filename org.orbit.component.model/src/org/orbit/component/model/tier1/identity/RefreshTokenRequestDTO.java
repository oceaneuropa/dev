package org.orbit.component.model.tier1.identity;

import javax.xml.bind.annotation.XmlElement;

public class RefreshTokenRequestDTO {

	@XmlElement
	protected String refreshToken;

	@XmlElement
	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
