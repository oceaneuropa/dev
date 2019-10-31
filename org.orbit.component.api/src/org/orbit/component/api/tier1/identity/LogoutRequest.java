package org.orbit.component.api.tier1.identity;

public class LogoutRequest {

	protected String tokenType;
	protected String accessToken;
	protected String refreshToken;

	public LogoutRequest() {
	}

	/**
	 * 
	 * @param tokenType
	 * @param accessToken
	 * @param refreshToken
	 */
	public LogoutRequest(String tokenType, String accessToken, String refreshToken) {
		this.tokenType = tokenType;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return this.tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
