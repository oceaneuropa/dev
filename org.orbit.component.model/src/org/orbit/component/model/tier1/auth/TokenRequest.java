package org.orbit.component.model.tier1.auth;

public class TokenRequest {

	protected String grant_type; // e.g. "client_credentials" (for app login) or "user_credentials" (for user login) or "refresh_token" for refreshing token
	protected String client_id;
	protected String client_secret;
	protected String username; // required when grant_type is "user_credentials"
	protected String password; // required when grant_type is "user_credentials"
	protected String refresh_token; // required when grant_type is "refresh_token"
	protected String scope; // optional
	protected String state; // optional

	public TokenRequest() {
	}

	public String getGrantType() {
		return this.grant_type;
	}

	public void setGrantType(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getClientId() {
		return this.client_id;
	}

	public void setClientId(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return this.client_secret;
	}

	public void setClientSecret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRefreshToken() {
		return this.refresh_token;
	}

	public void setRefreshToken(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
