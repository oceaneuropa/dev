package org.orbit.component.api.tier1.identity;

public class RegisterRequest {

	protected String username;
	protected String email;
	protected String password;

	public RegisterRequest() {
	}

	public RegisterRequest(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
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
