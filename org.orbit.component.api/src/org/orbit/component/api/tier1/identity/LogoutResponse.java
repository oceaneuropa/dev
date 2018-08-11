package org.orbit.component.api.tier1.identity;

public class LogoutResponse {

	protected boolean succeed;
	protected String message;

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

}
