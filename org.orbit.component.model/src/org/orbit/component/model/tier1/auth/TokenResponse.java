package org.orbit.component.model.tier1.auth;

public class TokenResponse {

	protected String token_type; // e.g. "bearer"
	protected String access_token; // e.g. JWT
	protected long expires_in; // e.g. 3600 (60 seconds * 60 minutes = 1 hour). (The expire time can be pre-calculated and be put in the JWT).
	protected String refresh_token; // e.g. JWT
	protected String scope; // e.g. "create". Optional
	protected String state; // Optional

	public TokenResponse() {
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TokenResponse [\r\n");
		sb.append("    token_type=\"").append(this.token_type).append("\",\r\n");
		sb.append("    expires_in=").append(this.expires_in).append(" (minutes)").append(",\r\n");
		sb.append("    access_token=\"").append(this.access_token).append("\",\r\n");
		sb.append("    refresh_token=\"").append(this.refresh_token).append("\",\r\n");
		sb.append("    scope=\"").append(this.scope).append("\",\r\n");
		sb.append("    state=\"").append(this.state).append("\"\r\n");
		sb.append("]");
		return sb.toString();
	}

}
