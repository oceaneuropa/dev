package org.orbit.component.runtime.tier1.auth.service;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.runtime.ComponentsConstants;
import org.orbit.component.runtime.OrbitServices;
import org.orbit.component.runtime.common.token.TokenManager;
import org.orbit.component.runtime.common.token.TokenManagerClusterImpl;
import org.orbit.component.runtime.common.token.TokenUtil;
import org.orbit.component.runtime.common.token.UserToken;
import org.orbit.component.runtime.model.account.UserAccount;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.origin.common.Activator;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.JWTUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class AuthServiceImpl implements AuthService, LifecycleAware {

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected TokenManager tokenManager;
	protected String tokenSecret;
	protected ServiceRegistration<?> serviceRegistry;

	/**
	 * 
	 * @param initProperties
	 */
	public AuthServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, ComponentsConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentsConstants.COMPONENT_AUTH_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentsConstants.COMPONENT_AUTH_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentsConstants.COMPONENT_AUTH_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentsConstants.COMPONENT_AUTH_TOKEN_SECRET);

		update(properties);

		// Start service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(AuthService.class, this, props);
	}

	public synchronized void update(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;
		init();
	}

	protected void init() {
		// 1. retrieve token secret
		String realm = Activator.getDefault().getRealm();
		this.tokenSecret = (String) this.properties.get(ComponentsConstants.COMPONENT_AUTH_TOKEN_SECRET);

		// 2. init token manager
		this.tokenManager = new TokenManagerClusterImpl(getName(), realm);
		this.tokenManager.activate();

		// 3. start beacon (useless?)
		// this.eventBus = EventBusUtil.getEventBus();
		// startBeacon();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
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

		// stopBeacon();
		// this.eventBus = null;
	}

	@Override
	public String getName() {
		return (String) this.properties.get(ComponentsConstants.COMPONENT_AUTH_NAME);
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(ComponentsConstants.COMPONENT_AUTH_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(ComponentsConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		return (String) this.properties.get(ComponentsConstants.COMPONENT_AUTH_CONTEXT_ROOT);
	}

	@Override
	public AuthorizationResponse authorize(AuthorizationRequest request) throws ServerException {
		return null;
	}

	@Override
	public TokenResponse getToken(TokenRequest request) throws ServerException {
		String grantType = request.getGrantType();
		TokenResponse response = null;
		if (GRANT_TYPE__CLIENT_CREDENTIALS.equalsIgnoreCase(grantType)) {
			// app login
			response = handleAppAuthorization(request);

		} else if (GRANT_TYPE__USER_CREDENTIALS.equalsIgnoreCase(grantType)) {
			// user login
			response = handleUserAuthorization(request);

		} else if (GRANT_TYPE__REFRESH_TOKEN.equalsIgnoreCase(grantType)) {
			// user request to get a new access token using the refresh token
			response = handleRefreshToken(request);

		} else {
			throw new ServerException("Unsupported Grant Type", "Grant type is not supported: " + grantType);
		}
		return response;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServerException
	 */
	private TokenResponse handleAppAuthorization(TokenRequest request) throws ServerException {
		return null;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServerException
	 */
	private TokenResponse handleUserAuthorization(TokenRequest request) throws ServerException {
		String username = request.getUsername();
		String password = request.getPassword();
		if (username == null || username.isEmpty()) {
			throw new ServerException("Bad Request", "Username is empty.");
		}
		if (password == null || password.isEmpty()) {
			throw new ServerException("Bad Request", "Password is empty.");
		}

		// Step1. Check username and password against UserRegistry.
		boolean matchUsernamePassword = false;
		UserAccount userAccount = null;
		try {
			UserRegistryService userRegistryService = OrbitServices.getInstance().getUserRegistryService();
			if (userRegistryService == null) {
				throw new ServerException("Service Unavailable", "UserRegistry service is not available.");
			}
			userAccount = userRegistryService.getUserAccount(username);
			if (userAccount != null) {
				matchUsernamePassword = userRegistryService.matchUsernamePassword(username, password);
			}

		} catch (ServerException e) {
			e.printStackTrace();
			throw new ServerException("Authentication Failed", e.getMessage(), e);
		}
		if (!matchUsernamePassword) {
			throw new ServerException("Unauthorized", "Username and password combination is invalid.");
		}

		// Step2. Create new UserToken and store it in the tokenManager
		UserToken userToken = null;
		try {
			userToken = TokenUtil.createAccessTokenObject(this.tokenSecret, userAccount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerException("Authentication Failed", e.getMessage());
		}
		this.tokenManager.setUserToken(username, userToken);

		// Step3. Return the token response
		TokenResponse response = new TokenResponse();
		response.setTokenType("Bearer");
		response.setAccessToken(userToken.getAccessToken());
		response.setExpiresIn(userToken.getAccessTokenExpiresInMinutes());
		response.setRefreshToken(userToken.getRefreshToken());
		response.setState(request.getState());
		response.setScope(request.getScope());
		return response;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServerException
	 */
	private TokenResponse handleRefreshToken(TokenRequest request) throws ServerException {
		// Step1. Decode the JWT object
		// - Exception is thrown by JWT (JWTVerifier) API if the token has expired.
		String issuer = getName();
		String refreshToken = request.getRefreshToken();
		if (refreshToken == null || refreshToken.isEmpty()) {
			throw new ServerException("Bad Request", "Refresh token is empty.");
		}
		DecodedJWT jwt = null;
		try {
			jwt = JWTUtil.verifyToken(this.tokenSecret, issuer, refreshToken);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (jwt == null) {
			throw new ServerException("Unauthorized", "Refresh token is invalid.");
		}

		// Step2. Get username from JWT
		String username = null;
		Map<String, Claim> claimsMap = jwt.getClaims();
		if (claimsMap != null) {
			for (Iterator<String> claimItor = claimsMap.keySet().iterator(); claimItor.hasNext();) {
				String key = claimItor.next();
				Claim claim = claimsMap.get(key);
				String value = claim.asString();
				if ("username".equalsIgnoreCase(key)) {
					username = value;
				}
			}
		}
		if (username == null) {
			throw new ServerException("Unauthorized", "Refresh token is invalid.");
		}

		// Step3. Find user account by username
		UserAccount userAccount = null;
		try {
			UserRegistryService userRegistryService = OrbitServices.getInstance().getUserRegistryService();
			if (userRegistryService == null) {
				throw new ServerException("Service Unavailable", "UserRegistry service is not available.");
			}
			userAccount = userRegistryService.getUserAccount(username);
		} catch (ServerException e) {
			e.printStackTrace();
			throw new ServerException("User Registry Exception", e.getMessage(), e);
		}
		if (userAccount == null) {
			throw new ServerException("Unauthorized", "Invalid username: " + username);
		}

		// Step4. Create new UserToken and store it in the tokenManager
		UserToken userToken = null;
		try {
			userToken = TokenUtil.createAccessTokenObject(this.tokenSecret, userAccount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerException("Authentication Failed", e.getMessage(), e);
		}
		this.tokenManager.setUserToken(username, userToken);

		// Step5. Return the token response
		TokenResponse response = new TokenResponse();
		response.setTokenType("Bearer");
		response.setAccessToken(userToken.getAccessToken());
		response.setExpiresIn(userToken.getAccessTokenExpiresInMinutes());
		response.setRefreshToken(userToken.getRefreshToken());
		response.setState(request.getState());
		response.setScope(request.getScope());
		return response;
	}

}

// protected EventBus eventBus;
// protected ThreadPoolTimer beacon;
// protected Runnable beaconRunnable;
//
// protected void startBeacon() {
// Runnable runnable = new Runnable() {
// @Override
// public void run() {
// try {
// eventBus.setProperty(getName(), getName() + ".heartbeat", new Date(), getClusterName());
//
// } catch (Exception e) {
// e.printStackTrace();
// }
//
// try {
// if (beaconRunnable != null) {
// beaconRunnable.run();
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
// };
// this.beacon = new ThreadPoolTimer(getClusterName() + "." + getName());
// this.beacon.setRunnable(runnable);
// this.beacon.setInterval(15 * 1000);
// this.beacon.start();
// }
//
// protected void stopBeacon() {
// if (this.beacon != null) {
// this.beacon.stop();
// }
//
// String key = getName() + ".heartbeat";
// try {
// this.eventBus.removeProperty(getName(), key, getClusterName());
//
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
