package org.orbit.component.cli.tier1;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.auth.AuthConnector;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.CLIHelper;
import org.osgi.framework.BundleContext;

/**
 * @see TransferAgentCommand
 *
 */
public class AuthCommand implements Annotated {

	protected BundleContext bundleContext;

	@Dependency
	protected AuthConnector connector;

	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 */
	public AuthCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	protected String getScheme() {
		return "orbit";
	}

	public void start() {
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
		OSGiServiceUtil.register(this.bundleContext, AuthCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
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

	protected Auth getAuthService() throws ClientException {
		if (this.connector == null) {
			System.out.println("AuthConnector service is not available.");
			return null;
		}
		Auth auth = this.connector.getService();
		if (auth == null) {
			System.out.println("Auth service is not available.");
			return null;
		}
		return auth;
	}

	@Descriptor("auth_ping")
	public void auth_ping() throws ClientException {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "auth_ping");
		}

		Auth auth = getAuthService();
		if (auth == null) {
			return;
		}

		boolean result = auth.ping();
		System.out.println("Result is: " + result);
	}

	@Descriptor("auth_echo")
	public void auth_echo( //
			@Descriptor("ID") @Parameter(names = { "-message", "--message" }, absentValue = Parameter.UNSPECIFIED) String message) throws ClientException {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "auth_echo", new String[] { "message", message });
		}

		Auth auth = getAuthService();
		if (auth == null) {
			return;
		}

		String result = auth.echo(message);
		System.out.println("Result is: " + result);
	}

	@Descriptor("authorize")
	public void authorize( //
			@Descriptor("Client ID") @Parameter(names = { "-client_id", "--client_id" }, absentValue = Parameter.UNSPECIFIED) String client_id, //
			@Descriptor("Response Type") @Parameter(names = { "-response_type", "--response_type" }, absentValue = Parameter.UNSPECIFIED) String response_type, //
			@Descriptor("Scope") @Parameter(names = { "-scope", "--scope" }, absentValue = Parameter.UNSPECIFIED) String scope, //
			@Descriptor("State") @Parameter(names = { "-state", "--state" }, absentValue = Parameter.UNSPECIFIED) String state //
	) throws ClientException {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "authorize", //
					new String[] { "client_id", client_id }, //
					new String[] { "response_type", response_type }, //
					new String[] { "scope", scope }, //
					new String[] { "state", state } //
			);
		}

		Auth auth = getAuthService();
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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "token", //
					new String[] { "grant_type", grant_type }, //
					new String[] { "client_id", client_id }, //
					new String[] { "client_secret", client_secret }, //
					new String[] { "username", username }, //
					new String[] { "password", password }, //
					new String[] { "refresh_token", refresh_token }, //
					new String[] { "scope", scope }, //
					new String[] { "state", state } //
			);
		}

		Auth auth = getAuthService();
		if (auth == null) {
			return;
		}

		TokenRequest request = new TokenRequest();

		if (Auth.GRANT_TYPE__CLIENT_CREDENTIALS.equals(grant_type)) {
			if (Parameter.UNSPECIFIED.equals(client_id)) {
				System.out.println("client_id is not set.");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(client_secret)) {
				System.out.println("client_secret is not set.");
				return;
			}

			request.setGrant_type(grant_type);
			request.setClient_id(client_id);
			request.setClient_secret(client_secret);

		} else if (Auth.GRANT_TYPE__USER_CREDENTIALS.equals(grant_type)) {
			if (Parameter.UNSPECIFIED.equals(username)) {
				System.out.println("username is not set.");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(password)) {
				System.out.println("password is not set.");
				return;
			}

			request.setGrant_type(grant_type);
			request.setUsername(username);
			request.setPassword(password);

		} else if (Auth.GRANT_TYPE__REFRESH_TOKEN.equals(grant_type)) {
			if (Parameter.UNSPECIFIED.equals(refresh_token)) {
				System.out.println("refresh_token is not set.");
				return;
			}

			request.setGrant_type(grant_type);
			request.setRefresh_token(refresh_token);

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

		TokenResponse response = auth.token(request);

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
			// for user token
			@Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username, //
			@Descriptor("Password") @Parameter(names = { "-password", "--password" }, absentValue = Parameter.UNSPECIFIED) String password //
	) throws ClientException {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "user_token", //
					new String[] { "username", username }, //
					new String[] { "password", password } //
			);
		}

		Auth auth = getAuthService();
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

		request.setGrant_type(Auth.GRANT_TYPE__USER_CREDENTIALS);
		request.setUsername(username);
		request.setPassword(password);

		TokenResponse response = auth.token(request);
		if (response == null) {
			System.out.println("TokenResponse is null");
		} else {
			System.out.println("TokenResponse is:");
			System.out.println(response);
		}
	}

}
