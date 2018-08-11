package org.orbit.component.runtime.model.identity;

public class LoginResponse {

	protected boolean succeed;
	protected String message;
	protected String tokenType;
	protected String tokenValue;

	public LoginResponse() {
	}

	public boolean isSucceed() {
		return this.succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTokenType() {
		return this.tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

}
