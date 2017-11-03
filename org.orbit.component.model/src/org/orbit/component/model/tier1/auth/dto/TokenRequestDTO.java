package org.orbit.component.model.tier1.auth.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenRequestDTO {

	@XmlElement
	protected String grant_type; // e.g. "client_credentials" (for app login) or "user_credentials" (for user login) or "refresh_token" for refreshing token
	@XmlElement
	protected String client_id;
	@XmlElement
	protected String client_secret;
	@XmlElement
	protected String username; // required when grant_type is "user_credentials"
	@XmlElement
	protected String password; // required when grant_type is "user_credentials"
	@XmlElement
	protected String refresh_token; // required when grant_type is "refresh_token"
	@XmlElement
	protected String scope; // optional
	@XmlElement
	protected String state; // optional

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

}
