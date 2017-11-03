package org.orbit.component.server.tier1.auth.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.model.tier1.account.UserAccountRTO;
import org.orbit.component.model.tier1.account.UserRegistryException;
import org.orbit.component.model.tier1.auth.AuthException;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.orbit.component.server.tier1.auth.service.AuthService;
import org.orbit.component.server.tier1.auth.service.TokenManager;
import org.orbit.component.server.tier1.auth.service.UserToken;
import org.origin.common.cluster.EventBus;
import org.origin.common.cluster.EventBusUtil;
import org.origin.common.thread.ThreadPoolTimer;
import org.origin.common.util.DateUtil;
import org.origin.common.util.JWTUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class AuthServiceImpl implements AuthService {

	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	private TokenManager tokenManager;
	private String userTokenSecret;

	protected EventBus eventBus;
	protected ThreadPoolTimer beaconTimer;
	protected Runnable beaconRunnable;

	protected ServiceRegistration<?> serviceRegistry;

	public AuthServiceImpl() {
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_AUTH_NAMESPACE);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_AUTH_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_AUTH_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_AUTH_CONTEXT_ROOT);

		update(configProps);

		this.eventBus = EventBusUtil.getEventBus();
		this.tokenManager = new TokenManagerClusterImpl(getClusterName() + ".token");
		this.tokenManager.activate();

		startBeacon();

		// Start service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(AuthService.class, this, props);
	}

	public synchronized void update(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// Stop service
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		if (this.tokenManager != null) {
			this.tokenManager.deactivate();
			this.tokenManager = null;
		}

		stopBeacon();

		this.eventBus = null;
	}

	protected void startBeacon() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					eventBus.setProperty(getName(), getName() + ".heartbeat", new Date(), getClusterName());

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					if (beaconRunnable != null) {
						beaconRunnable.run();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		this.beaconTimer = new ThreadPoolTimer(getClusterName() + "." + getName());
		this.beaconTimer.setRunnable(runnable);
		this.beaconTimer.setInterval(15 * 1000);
		this.beaconTimer.start();
	}

	protected void stopBeacon() {
		if (this.beaconTimer != null) {
			this.beaconTimer.stop();
		}

		String key = getName() + ".heartbeat";
		try {
			this.eventBus.removeProperty(getName(), key, getClusterName());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getNamespace() {
		return (String) this.properties.get(OrbitConstants.COMPONENT_AUTH_NAMESPACE);
	}

	@Override
	public String getName() {
		return (String) this.properties.get(OrbitConstants.COMPONENT_AUTH_NAME);
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.COMPONENT_AUTH_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(OrbitConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		return (String) this.properties.get(OrbitConstants.COMPONENT_AUTH_CONTEXT_ROOT);
	}

	protected String getClusterName() {
		return getNamespace();
	}

	@Override
	public AuthorizationResponse onAuthorize(AuthorizationRequest request) throws AuthException {
		return null;
	}

	@Override
	public TokenResponse onToken(TokenRequest request) throws AuthException {
		String grantType = request.getGrant_type();

		TokenResponse response = null;
		if (GRANT_TYPE__CLIENT_CREDENTIALS.equalsIgnoreCase(grantType)) {
			// app login
			response = onTokenForClientCredentials(request);

		} else if (GRANT_TYPE__USER_CREDENTIALS.equalsIgnoreCase(grantType)) {
			// user login
			response = onTokenForUserCredentials(request);

		} else if (GRANT_TYPE__REFRESH_TOKEN.equalsIgnoreCase(grantType)) {
			// app or user request to get a new token
			response = onTokenForRefreshToken(request);

		} else {
			throw new AuthException("Unknown Grant Type", "Grant type '" + grantType + "' is unknown.");
		}

		return response;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AuthException
	 */
	private TokenResponse onTokenForClientCredentials(TokenRequest request) throws AuthException {
		// app login
		String clientId = request.getClient_id();
		String clientSecret = request.getClient_secret();
		return null;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AuthException
	 */
	private TokenResponse onTokenForUserCredentials(TokenRequest request) throws AuthException {
		UserRegistryService userRegistryService = Activator.getUserRegistryService();
		if (userRegistryService == null) {
			throw new AuthException("Service Unavailable", "User registry service is not available.");
		}

		String username = request.getUsername();
		String password = request.getPassword();
		if (username == null || username.isEmpty()) {
			throw new AuthException("Empty Username", "Username is empty.");
		}
		if (password == null || password.isEmpty()) {
			throw new AuthException("Empty Password", "Password is empty.");
		}

		boolean isAuthorized = false;
		UserAccountRTO userAccount = null;
		try {
			userAccount = userRegistryService.getUserAccount(username);
			if (userAccount != null && password.equals(userAccount.getPassword())) {
				isAuthorized = true;
			}
		} catch (UserRegistryException e) {
			e.printStackTrace();
			throw new AuthException(e, "User Registry Exception", e.getMessage());
		}

		if (isAuthorized) {
			UserToken userToken = createUserToken(userAccount);
			this.tokenManager.setUserToken(username, userToken);

			TokenResponse response = new TokenResponse();
			response.setToken_type("Bearer");
			response.setAccess_token(userToken.getAccessToken());
			response.setExpires_in(userToken.getAccessTokenExpiresInMinutes());
			response.setRefresh_token(userToken.getRefreshToken());
			response.setState(request.getState());
			response.setScope(request.getScope());
			return response;

		} else {
			throw new AuthException("Authentication Failed", "Username and password combination is invalid.");
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AuthException
	 */
	private TokenResponse onTokenForRefreshToken(TokenRequest request) throws AuthException {
		String refreshToken = request.getRefresh_token();

		return null;
	}

	/**
	 * 
	 * @param userAccount
	 * @return
	 */
	private UserToken createUserToken(UserAccountRTO userAccount) {
		UserToken userToken = new UserToken();

		String issuer = getFullName();
		String username = userAccount.getUserId();
		String email = userAccount.getEmail();
		String firstName = userAccount.getFirstName();
		String lastName = userAccount.getLastName();

		Date now = new Date();

		// Access token expires in 30 minutes (gets updated/refreshed with each access to any web services)
		Date accessTokenExpiresAt = DateUtil.addMinutes(now, 30);
		String accessToken = JWTUtil.createToken_HMAC256(this.userTokenSecret, issuer, "user_access_token", username, accessTokenExpiresAt, //
				new String[] { "username", username }, //
				new String[] { "email", email }, //
				new String[] { "firstName", firstName }, //
				new String[] { "lastName", lastName } //
		);

		// Refresh token expires in 24 hours
		Date refreshTokenExpiresAt = DateUtil.addHours(now, 24);
		String refreshToken = JWTUtil.createToken_HMAC256(this.userTokenSecret, issuer, "user_refresh_token", username, refreshTokenExpiresAt, //
				new String[] { "username", username }, //
				new String[] { "email", email }, //
				new String[] { "firstName", firstName }, //
				new String[] { "lastName", lastName } //
		);

		userToken.setUsername(username);
		userToken.setAccessToken(accessToken);
		userToken.setAccessTokenExpireTime(accessTokenExpiresAt);
		userToken.setRefreshToken(refreshToken);
		userToken.setRefreshTokenExpireTime(refreshTokenExpiresAt);

		return userToken;
	}

	protected String getFullName() {
		if (getNamespace() == null || getNamespace().isEmpty()) {
			return getName();
		}
		return getNamespace() + "." + getName();
	}

}
