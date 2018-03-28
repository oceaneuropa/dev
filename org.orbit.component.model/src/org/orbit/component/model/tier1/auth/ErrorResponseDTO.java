package org.orbit.component.model.tier1.auth;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @see https://www.oauth.com/oauth2-servers/access-tokens/access-token-response/
 *
 */
@XmlRootElement
public class ErrorResponseDTO {

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

	// (3) error and error description examples
	// ----------------------------------------------------------------
	// invalid_request – The request is missing a parameter so the server can’t proceed with the request. This may also be returned if the request includes an
	// unsupported parameter or repeats a parameter.
	//
	// invalid_client – Client authentication failed, such as if the request contains an invalid client ID or secret. Send an HTTP 401 response in this case.
	//
	// invalid_grant – The authorization code (or user’s password for the password grant type) is invalid or expired. This is also the error you would return if
	// the redirect URL given in the authorization grant does not match the URL provided in this access token request.
	//
	// invalid_scope – For access token requests that include a scope (password or client_credentials grants), this error indicates an invalid scope value in
	// the request.
	//
	// unauthorized_client – This client is not authorized to use the requested grant type. For example, if you restrict which applications can use the Implicit
	// grant, you would return this error for the other apps.
	//
	// unsupported_grant_type – If a grant type is requested that the authorization server doesn’t recognize, use this code. Note that unknown grant types also
	// use this specific error code rather than using the invalid_request above.
	// ----------------------------------------------------------------

	@XmlElement
	protected String error;
	@XmlElement
	protected String error_description;
	@XmlElement
	protected String error_uri;

	@XmlElement
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement
	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	@XmlElement
	public String getError_uri() {
		return error_uri;
	}

	public void setError_uri(String error_uri) {
		this.error_uri = error_uri;
	}

}
