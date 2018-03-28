package org.orbit.component.model.tier1.auth;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthorizationResponseDTO {

	// 1. https://www.digitalocean.com/community/tutorials/an-introduction-to-oauth-2
	// (1) authorize
	// --------------------------------------------------------
	// https://cloud.digitalocean.com/v1/oauth/authorize?response_type=code&client_id=CLIENT_ID&redirect_uri=CALLBACK_URL&scope=read
	// https://cloud.digitalocean.com/v1/oauth/authorize: the API authorization endpoint
	// client_id=client_id: the application's client ID (how the API identifies the application)
	// redirect_uri=CALLBACK_URL: where the service redirects the user-agent after an authorization code is granted
	// response_type=code: specifies that your application is requesting an authorization code grant
	// scope=read: specifies the level of access that the application is requesting
	// --------------------------------------------------------
	// (2) token
	// --------------------------------------------------------
	// https://cloud.digitalocean.com/v1/oauth/token?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&grant_type=authorization_code&code=AUTHORIZATION_CODE&redirect_uri=CALLBACK_URL
	// --------------------------------------------------------

	// The client_id is the identifier for your app. You will have received a client_id when first registering your app with the service.
	@XmlElement
	protected String client_id;

	// response_type is set to code indicating that you want an authorization code as the response.
	@XmlElement
	protected String response_type;

	// redirect_uri (optional)
	// The redirect_uri is optional in the spec, but some services require it. This is the URL to which you want the user to be redirected after the
	// authorization is complete. This must match the redirect URL that you have previously registered with the service.
	@XmlElement
	protected String redirect_url;

	// scope (optional)
	// Include one or more scope values to request additional levels of access. The values will depend on the particular service.
	@XmlElement
	protected String scope;

	// state (recommended)
	// The state parameter serves two functions. When the user is redirected back to your app, whatever value you include as the state will also be included in
	// the redirect. This gives your app a chance to persist data between the user being directed to the authorization server and back again, such as using the
	// state parameter as a session key. This may be used to indicate what action in the app to perform after authorization is complete, for example, indicating
	// which of your app’s pages to redirect to after authorization. This also serves as a CSRF protection mechanism. When the user is redirected back to your
	// app, double check that the state value matches what you set it to originally. This will ensure an attacker can’t intercept the authorization flow.

	// Note that the lack of using a client secret means that using the state parameter is even more important for the single-page apps.
	@XmlElement
	protected String state;

	@XmlElement
	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	@XmlElement
	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	@XmlElement
	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
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
