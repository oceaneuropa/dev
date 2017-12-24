package org.origin.common.rest.client;

public class Token {

	public static final String UNKNOWN_TOKEN_TYPE = "Bearer";

	protected String tokenType;
	protected String accessToken;

	public Token() {
	}

	public Token(String tokenType, String accessToken) {
		this.tokenType = tokenType;
		this.accessToken = accessToken;
	}

	public synchronized String getTokenType() {
		if (this.tokenType == null) {
			this.tokenType = UNKNOWN_TOKEN_TYPE;
		}
		return this.tokenType;
	}

	public synchronized void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public synchronized String getAccessToken() {
		return this.accessToken;
	}

	public synchronized void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
