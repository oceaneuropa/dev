package org.orbit.component.api.tier1.identity;

public class LoginRequest {

	protected String clientId;
	protected String grantType;
	protected String username;
	protected String email;
	protected String password;

	public LoginRequest() {
	}

	/**
	 * 
	 * @param clientId
	 * @param grantType
	 * @param username
	 * @param email
	 * @param password
	 */
	public LoginRequest(String clientId, String grantType, String username, String email, String password) {
		this.clientId = clientId;
		this.grantType = grantType;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getGrantType() {
		return this.grantType;
	}

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
