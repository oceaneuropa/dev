package org.orbit.component.api.cli;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.auth.GrantTypes;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.GlobalContext;
import org.origin.common.util.CLIHelper;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthCommand {

	protected static Logger LOG = LoggerFactory.getLogger(AuthCommand.class);

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
						// "authorize", //
						// "token", //
						"login" //
		});
		OSGiServiceUtil.register(bundleContext, AuthCommand.class.getName(), this, props);
	}

	public void stop(final BundleContext bundleContext) {
		System.out.println("AuthCommand.stop()");

		OSGiServiceUtil.unregister(AuthCommand.class.getName(), this);
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

		Auth auth = OrbitClients.getInstance().getAuth(url);

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

		Auth auth = OrbitClients.getInstance().getAuth(url);

		String result = auth.echo(message);
		System.out.println("Result is: " + result);
	}

	/**
	 * 
	 * @param realm
	 * @param url
	 * @param username
	 * @param password
	 * @throws ClientException
	 */
	@Descriptor("login")
	public void login( //
			@Descriptor("Realm") @Parameter(names = { "-realm", "--realm" }, absentValue = Parameter.UNSPECIFIED) String realm, //
			@Descriptor("Auth server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			// for user token
			@Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username, //
			@Descriptor("Password") @Parameter(names = { "-password", "--password" }, absentValue = Parameter.UNSPECIFIED) String password //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "login", //
				new String[] { "realm", realm }, //
				new String[] { "url", url }, //
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
			Auth auth = OrbitClients.getInstance().getAuth(realm, username, url);

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

				GlobalContext.getInstance().setCurrentUser(realm, username, password);
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

// @Descriptor("authorize")
// public void authorize( //
// @Descriptor("Realm") @Parameter(names = { "-realm", "--realm" }, absentValue = Parameter.UNSPECIFIED) String realm, //
// @Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username, //
//
// @Descriptor("Auth server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
// @Descriptor("Client ID") @Parameter(names = { "-client_id", "--client_id" }, absentValue = Parameter.UNSPECIFIED) String client_id, //
// @Descriptor("Response Type") @Parameter(names = { "-response_type", "--response_type" }, absentValue = Parameter.UNSPECIFIED) String
// response_type, //
// @Descriptor("Scope") @Parameter(names = { "-scope", "--scope" }, absentValue = Parameter.UNSPECIFIED) String scope, //
// @Descriptor("State") @Parameter(names = { "-state", "--state" }, absentValue = Parameter.UNSPECIFIED) String state //
// ) throws ClientException {
// CLIHelper.getInstance().printCommand(getScheme(), "authorize", //
// new String[] { "url", url }, //
// new String[] { "client_id", client_id }, //
// new String[] { "response_type", response_type }, //
// new String[] { "scope", scope }, //
// new String[] { "state", state } //
// );
// if (Parameter.UNSPECIFIED.equals(url)) {
// LOG.error("'-url' parameter is not set.");
// return;
// }
//
// Auth auth = getAuth(realm, username, url);
//
// AuthorizationRequest request = new AuthorizationRequest();
// request.setClient_id(client_id);
// request.setResponse_type(response_type);
// request.setScope(scope);
// request.setState(state);
//
// AuthorizationResponse response = auth.authorize(request);
// if (response == null) {
// System.out.println("AuthorizationResponse is null");
// } else {
// System.out.println("AuthorizationResponse is:");
// System.out.println(response);
// }
// }

// /**
// *
// * @param grant_type
// * e.g. "client_credentials" (for app login) or "user_credentials" (for user login) or "refresh_token" for refreshing token
// * @param client_id
// * @param client_secret
// * @param username
// * @param password
// * @param refresh_token
// * @param scope
// * @param state
// * @throws ClientException
// */
// @Descriptor("token")
// public void token( //
// @Descriptor("Realm") @Parameter(names = { "-realm", "--realm" }, absentValue = Parameter.UNSPECIFIED) String realm, //
//
// @Descriptor("Auth server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
// @Descriptor("Grant Type") @Parameter(names = { "-grant_type", "--grant_type" }, absentValue = Parameter.UNSPECIFIED) String grant_type,
// //
//
// // for app token
// @Descriptor("Client ID") @Parameter(names = { "-client_id", "--client_id" }, absentValue = Parameter.UNSPECIFIED) String client_id, //
// @Descriptor("Client Secret") @Parameter(names = { "-client_secret", "--client_secret" }, absentValue = Parameter.UNSPECIFIED) String
// client_secret, //
//
// // for user token
// @Descriptor("Username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username, //
// @Descriptor("Password") @Parameter(names = { "-password", "--password" }, absentValue = Parameter.UNSPECIFIED) String password, //
//
// // for refreshing token
// @Descriptor("Refresh Token") @Parameter(names = { "-refresh_token", "--refresh_token" }, absentValue = Parameter.UNSPECIFIED) String
// refresh_token, //
//
// // optional for all grand typs
// @Descriptor("Scope") @Parameter(names = { "-scope", "--scope" }, absentValue = Parameter.UNSPECIFIED) String scope, //
// @Descriptor("State") @Parameter(names = { "-state", "--state" }, absentValue = Parameter.UNSPECIFIED) String state //
// ) throws ClientException {
// CLIHelper.getInstance().printCommand(getScheme(), "token", //
// new String[] { "url", url }, //
// new String[] { "grant_type", grant_type }, //
// new String[] { "client_id", client_id }, //
// new String[] { "client_secret", client_secret }, //
// new String[] { "username", username }, //
// new String[] { "password", password }, //
// new String[] { "refresh_token", refresh_token }, //
// new String[] { "scope", scope }, //
// new String[] { "state", state } //
// );
// if (Parameter.UNSPECIFIED.equals(url)) {
// LOG.error("'-url' parameter is not set.");
// return;
// }
//
// Auth auth = getAuth(realm, username, url);
//
// TokenRequest request = new TokenRequest();
//
// if (GrantTypes.CLIENT_CREDENTIALS.equals(grant_type)) {
// if (Parameter.UNSPECIFIED.equals(client_id)) {
// System.out.println("client_id is not set.");
// return;
// }
// if (Parameter.UNSPECIFIED.equals(client_secret)) {
// System.out.println("client_secret is not set.");
// return;
// }
//
// request.setGrantType(grant_type);
// request.setClientId(client_id);
// request.setClientSecret(client_secret);
//
// } else if (GrantTypes.USER_CREDENTIALS.equals(grant_type)) {
// if (Parameter.UNSPECIFIED.equals(username)) {
// System.out.println("username is not set.");
// return;
// }
// if (Parameter.UNSPECIFIED.equals(password)) {
// System.out.println("password is not set.");
// return;
// }
//
// request.setGrantType(grant_type);
// request.setUsername(username);
// request.setPassword(password);
//
// } else if (GrantTypes.REFRESH_TOKEN.equals(grant_type)) {
// if (Parameter.UNSPECIFIED.equals(refresh_token)) {
// System.out.println("refresh_token is not set.");
// return;
// }
//
// request.setGrantType(grant_type);
// request.setRefreshToken(refresh_token);
//
// } else {
// System.out.println("Unsupported grand_type: " + grant_type);
// return;
// }
//
// // Set optional options
// if (!Parameter.UNSPECIFIED.equals(scope)) {
// request.setScope(scope);
// }
// if (!Parameter.UNSPECIFIED.equals(state)) {
// request.setState(state);
// }
//
// TokenResponse response = auth.getToken(request);
//
// if (response == null) {
// System.out.println("TokenResponse is null");
// } else {
// System.out.println("TokenResponse is:");
//
// System.out.println(response);
// }
// }
