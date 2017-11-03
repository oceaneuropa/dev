package org.orbit.component.model.tier1.auth.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * https://www.oauth.com/oauth2-servers/access-tokens/access-token-response/
 * 
 */
@XmlRootElement
public class TokenResponseDTO {

	// https://www.oauth.com/oauth2-servers/access-tokens/access-token-response/
	// (1) Successful response
	// ----------------------------------------------------------------
	// HTTP/1.1 200 OK
	// Content-Type: application/json
	// Cache-Control: no-store
	// Pragma: no-cache
	//
	// {
	// "access_token":"MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3",
	// "token_type":"bearer",
	// "expires_in":3600,
	// "refresh_token":"IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk",
	// "scope":"create",
	// "state":"12345678"
	// }
	// ----------------------------------------------------------------

	// (2) Failed response
	// ----------------------------------------------------------------
	// HTTP/1.1 400 Bad Request
	// Content-Type: application/json;charset=UTF-8
	// Cache-Control: no-store
	// Pragma: no-cache
	//
	// {
	// "error": "invalid_request",
	// "error_description": "Request was missing the 'redirect_uri' parameter.",
	// "error_uri": "See the full API docs at https://authorization-server.com/docs/access_token"
	// }
	// ----------------------------------------------------------------

	@XmlElement
	protected String token_type; // e.g. "bearer"
	@XmlElement
	protected String access_token; // e.g. JWT
	@XmlElement
	protected long expires_in; // e.g. 3600 (60 seconds * 60 minutes = 1 hour). (The expire time can be pre-calculated and be put in the JWT).
	@XmlElement
	protected String refresh_token; // e.g. JWT
	@XmlElement
	protected String scope; // e.g. "create". Optional
	@XmlElement
	protected String state; // Optional

	@XmlElement
	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	@XmlElement
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	@XmlElement
	public long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	@XmlElement
	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@XmlElement
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@XmlElement
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
