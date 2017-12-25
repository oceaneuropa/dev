package org.orbit.component.cli;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.auth.GrantTypes;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.util.CLIHelper;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthCommand implements Annotated {

	protected static Logger LOG = LoggerFactory.getLogger(AuthCommand.class);

	protected ServiceConnectorAdapter<Auth> authConnector;

	private String default_realm = ClientConfiguration.DEFAULT_REALM;
	private String default_username = ClientConfiguration.UNKNOWN_USERNAME;

	protected String getScheme() {
		return "orbit";
	}

	public void start(final BundleContext bundleContext) {
		System.out.println("AuthCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function",
				new String[] { //
						"auth_ping", //
						"auth_echo", //
						"authorize", //
						"token", //
						"user_token" //
		});
		OSGiServiceUtil.register(bundleContext, AuthCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);

		this.authConnector = new ServiceConnectorAdapter<Auth>(Auth.class);
		this.authConnector.start(bundleContext);
	}

	public void stop(final BundleContext bundleContext) {
		System.out.println("AuthCommand.stop()");

		OSGiServiceUtil.unregister(AuthCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void connectorSet() {
		System.out.println("AuthConnector is set.");
	}

	@DependencyUnfullfilled
	public void connectorUnset() {
		System.out.println("AuthConnector is unset.");
	}

	protected Auth getAuth(String realm, String username, String url) throws ClientException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		Auth auth = this.authConnector.getService(properties);
		if (auth == null) {
			LOG.error("Auth is not available.");
		}
		return auth;
	}

	@Descriptor("auth_ping")
	public void auth_ping( //
			@Descriptor("Auth server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "auth_ping", new String[] { "url", url });
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'-url' parameter is not set.");
			return;
		}

		Auth auth = getAuth(this.default_realm, this.default_username, url);
		if (auth == null) {
			return;
		}

		boolean result = auth.ping();
		System.out.println("Result is: " + result);
	}

	@Descriptor("auth_echo")
	public void auth_echo( //
			@Descriptor("Auth server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Message") @Parameter(names = { "-m", "--m" }, absentValue = Parameter.UNSPECIFIED) String message //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "auth_echo", new String[] { "url", url }, new String[] { "m", message });
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'-url' parameter is not set.");
			return;
		}

		Auth auth = getAuth(this.default_realm, this.default_username, url);
		if (auth == null) {
			return;
		}

		String result = auth.echo(message);
		System.out.println("Result is: " + result);
	}

	@Descriptor("authorize")
	public void authorize( //
			@Descriptor("Auth server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Client ID") @Parameter(names = { "-client_id", "--client_id" }, absentValue = Parameter.UNSPECIFIED) String client_id, //
			@Descriptor("Response Type") @Parameter(names = { "-response_type", "--response_type" }, absentValue = Parameter.UNSPECIFIED) String response_type, //
			@Descriptor("Scope") @Parameter(names = { "-scope", "--scope" }, absentValue = Parameter.UNSPECIFIED) String scope, //
			@Descriptor("State") @Parameter(names = { "-state", "--state" }, absentValue = Parameter.UNSPECIFIED) String state //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "authorize", //
				new String[] { "url", url }, //
				new String[] { "client_id", client_id }, //
				new String[] { "response_type", response_type }, //
				new String[] { "scope", scope }, //
				new String[] { "state", state } //
		);
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'-url' parameter is not set.");
			return;
		}

		Auth auth = getAuth(this.default_realm, this.default_username, url);
		if (auth == null) {
			return;
		}

		AuthorizationRequest request = new AuthorizationRequest();
		request.setClient_id(client_id);
		request.setResponse_type(response_type);
		request.setScope(scope);
		request.setState(state);

		AuthorizationResponse response = auth.authorize(request);
		if (response == null) {
			System.out.println("AuthorizationResponse is null");
		} else {
			System.out.println("AuthorizationResponse is:");
			System.out.println(response);
		}
	}

	/**
	 * 
	 * @param grant_type
	 *            e.g. "client_credentials" (for app login) or "user_credentials" (for user login) or "refresh_token" for refreshing token
	 * @param client_id
	 * @param client_secret
	 * @param username
	 * @param password
	 * @param refresh_token
	 * @param scope
	 * @param state
	 * @throws ClientException
	 */
	@Descriptor("token")
	public void token( //
			@Descriptor("Auth server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Grant Type") @Parameter(names = { "-grant_type", "--grant_type" }, absentValue = Parameter.UNSPECIFIED) String grant_type, //

			// for app token
			@Descriptor("Client ID") @Parameter(names = { "-client_id", "--client_id" }, absentValue = Parameter.UNSPECIFIED) String client_id, //
			@Descriptor("Client Secret") @Parameter(names = { "-client_secret", "--client_secret" }, absentValue = Parameter.UNSPECIFIED) String client_secret, //

			// for user token
			@Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username, //
			@Descriptor("Password") @Parameter(names = { "-password", "--password" }, absentValue = Parameter.UNSPECIFIED) String password, //

			// for refreshing token
			@Descriptor("Refresh Token") @Parameter(names = { "-refresh_token", "--refresh_token" }, absentValue = Parameter.UNSPECIFIED) String refresh_token, //

			// optional for all grand typs
			@Descriptor("Scope") @Parameter(names = { "-scope", "--scope" }, absentValue = Parameter.UNSPECIFIED) String scope, //
			@Descriptor("State") @Parameter(names = { "-state", "--state" }, absentValue = Parameter.UNSPECIFIED) String state //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "token", //
				new String[] { "url", url }, //
				new String[] { "grant_type", grant_type }, //
				new String[] { "client_id", client_id }, //
				new String[] { "client_secret", client_secret }, //
				new String[] { "username", username }, //
				new String[] { "password", password }, //
				new String[] { "refresh_token", refresh_token }, //
				new String[] { "scope", scope }, //
				new String[] { "state", state } //
		);
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'-url' parameter is not set.");
			return;
		}

		String theUsername = username != null ? username : this.default_username;
		Auth auth = getAuth(this.default_realm, theUsername, url);
		if (auth == null) {
			return;
		}

		TokenRequest request = new TokenRequest();

		if (GrantTypes.CLIENT_CREDENTIALS.equals(grant_type)) {
			if (Parameter.UNSPECIFIED.equals(client_id)) {
				System.out.println("client_id is not set.");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(client_secret)) {
				System.out.println("client_secret is not set.");
				return;
			}

			request.setGrantType(grant_type);
			request.setClientId(client_id);
			request.setClientSecret(client_secret);

		} else if (GrantTypes.USER_CREDENTIALS.equals(grant_type)) {
			if (Parameter.UNSPECIFIED.equals(username)) {
				System.out.println("username is not set.");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(password)) {
				System.out.println("password is not set.");
				return;
			}

			request.setGrantType(grant_type);
			request.setUsername(username);
			request.setPassword(password);

		} else if (GrantTypes.REFRESH_TOKEN.equals(grant_type)) {
			if (Parameter.UNSPECIFIED.equals(refresh_token)) {
				System.out.println("refresh_token is not set.");
				return;
			}

			request.setGrantType(grant_type);
			request.setRefreshToken(refresh_token);

		} else {
			System.out.println("Unsupported grand_type: " + grant_type);
			return;
		}

		// Set optional options
		if (!Parameter.UNSPECIFIED.equals(scope)) {
			request.setScope(scope);
		}
		if (!Parameter.UNSPECIFIED.equals(state)) {
			request.setState(state);
		}

		TokenResponse response = auth.getToken(request);

		if (response == null) {
			System.out.println("TokenResponse is null");
		} else {
			System.out.println("TokenResponse is:");
			System.out.println(response);
		}
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @throws ClientException
	 */
	@Descriptor("user_token")
	public void user_token( //
			@Descriptor("Auth server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			// for user token
			@Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username, //
			@Descriptor("Password") @Parameter(names = { "-password", "--password" }, absentValue = Parameter.UNSPECIFIED) String password //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "user_token", //
				new String[] { "username", username }, //
				new String[] { "password", password } //
		);
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'-url' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(username)) {
			LOG.error("'-username' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(password)) {
			LOG.error("'-password' parameter is not set.");
			return;
		}

		try {
			Auth auth = getAuth(this.default_realm, username, url);
			if (auth == null) {
				return;
			}

			TokenRequest request = new TokenRequest();

			if (Parameter.UNSPECIFIED.equals(username)) {
				System.out.println("username is not set.");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(password)) {
				System.out.println("password is not set.");
				return;
			}

			request.setGrantType(GrantTypes.USER_CREDENTIALS);
			request.setUsername(username);
			request.setPassword(password);

			TokenResponse response = auth.getToken(request);
			if (response == null) {
				System.out.println("TokenResponse is null");
			} else {
				System.out.println("TokenResponse is:");
				System.out.println(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// protected BundleContext bundleContext;
// @Dependency
// protected AuthConnector authConnector;

// protected Auth getAuthService() throws ClientException {
// if (this.connector == null) {
// System.out.println("AuthConnector service is not available.");
// return null;
// }
// Auth auth = this.connector.getService();
// if (auth == null) {
// System.out.println("Auth service is not available.");
// return null;
// }
// return auth;
// }
