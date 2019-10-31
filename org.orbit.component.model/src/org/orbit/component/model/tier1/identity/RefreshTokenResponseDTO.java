package org.orbit.component.model.tier1.identity;

import javax.xml.bind.annotation.XmlElement;

public class RefreshTokenResponseDTO {

	@XmlElement
	protected boolean succeed;
	@XmlElement
	protected String message;
	@XmlElement
	protected String tokenType;
	@XmlElement
	protected String accessToken;
	@XmlElement
	protected String refreshToken;

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
	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@XmlElement
	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
