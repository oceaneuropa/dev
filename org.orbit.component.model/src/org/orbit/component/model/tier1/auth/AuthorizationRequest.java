package org.orbit.component.model.tier1.auth;

public class AuthorizationRequest {

	protected String client_id;
	protected String response_type;
	protected String redirect_url;
	protected String scope;
	protected String state;

	public AuthorizationRequest() {
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
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
