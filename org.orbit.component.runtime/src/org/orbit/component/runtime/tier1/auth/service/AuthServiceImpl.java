package org.orbit.component.runtime.tier1.auth.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.orbit.component.model.tier1.account.UserAccount;
import org.orbit.component.model.tier1.account.UserRegistryException;
import org.orbit.component.model.tier1.auth.AuthException;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.runtime.Activator;
import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.origin.common.util.DateUtil;
import org.origin.common.util.JWTUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class AuthServiceImpl implements AuthService {

	private Map<Object, Object> properties = new HashMap<Object, Object>();
	private TokenManager tokenManager;
	private String tokenSecret;
	private ServiceRegistration<?> serviceRegistry;

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
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_AUTH_TOKEN_SECRET);

		update(configProps);

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
		this.tokenSecret = (String) this.properties.get(OrbitConstants.COMPONENT_AUTH_TOKEN_SECRET);

		// 2. init token manager
		String serviceFullName = getFullName();
		String clusterName = getNamespace() + ".token";
		this.tokenManager = new TokenManagerClusterImpl(serviceFullName, clusterName);
		this.tokenManager.activate();

		// 3. start beacon (useless?)
		// this.eventBus = EventBusUtil.getEventBus();
		// startBeacon();
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

		// stopBeacon();
		// this.eventBus = null;
	}

	@Override
	public String getNamespace() {
		return (String) this.properties.get(OrbitConstants.COMPONENT_AUTH_NAMESPACE);
	}

	@Override
	public String getName() {
		return (String) this.properties.get(OrbitConstants.COMPONENT_AUTH_NAME);
	}

	protected String getFullName() {
		if (getNamespace() == null || getNamespace().isEmpty()) {
			return getName();
		}
		return getNamespace() + "." + getName();
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

	@Override
	public AuthorizationResponse authorize(AuthorizationRequest request) throws AuthException {
		return null;
	}

	@Override
	public TokenResponse getToken(TokenRequest request) throws AuthException {
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
			throw new AuthException("Unsupported Grant Type", "Grant type is not supported: " + grantType);
		}
		return response;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AuthException
	 */
	private TokenResponse handleAppAuthorization(TokenRequest request) throws AuthException {
		return null;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws AuthException
	 */
	private TokenResponse handleUserAuthorization(TokenRequest request) throws AuthException {
		String username = request.getUsername();
		String password = request.getPassword();
		if (username == null || username.isEmpty()) {
			throw new AuthException("Bad Request", "Username is empty.");
		}
		if (password == null || password.isEmpty()) {
			throw new AuthException("Bad Request", "Password is empty.");
		}

		// Step1. Check username and password against UserRegistry.
		boolean matchUsernamePassword = false;
		UserAccount userAccount = null;
		try {
			UserRegistryService userRegistryService = Activator.getInstance().getUserRegistryService();
			if (userRegistryService == null) {
				throw new AuthException("Service Unavailable", "UserRegistry service is not available.");
			}
			userAccount = userRegistryService.getUserAccount(username);
			if (userAccount != null) {
				matchUsernamePassword = userRegistryService.matchUsernamePassword(username, password);
			}

		} catch (UserRegistryException e) {
			e.printStackTrace();
			throw new AuthException(e, "Authentication Failed", e.getMessage());
		}
		if (!matchUsernamePassword) {
			throw new AuthException("Unauthorized", "Username and password combination is invalid.");
		}

		// Step2. Create new UserToken and store it in the tokenManager
		UserToken userToken = null;
		try {
			userToken = createAccessToken(userAccount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuthException("Authentication Failed", e.getMessage());
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
	 * @throws AuthException
	 */
	private TokenResponse handleRefreshToken(TokenRequest request) throws AuthException {
		// Step1. Decode the JWT object
		// - Exception is thrown by JWT (JWTVerifier) API if the token has expired.
		String issuer = getFullName();
		String refreshToken = request.getRefreshToken();
		if (refreshToken == null || refreshToken.isEmpty()) {
			throw new AuthException("Bad Request", "Refresh token is empty.");
		}
		DecodedJWT jwt = null;
		try {
			jwt = JWTUtil.verifyToken(this.tokenSecret, issuer, refreshToken);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (jwt == null) {
			throw new AuthException("Unauthorized", "Refresh token is invalid.");
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
			throw new AuthException("Unauthorized", "Refresh token is invalid.");
		}

		// Step3. Find user account by username
		UserAccount userAccount = null;
		try {
			UserRegistryService userRegistryService = Activator.getInstance().getUserRegistryService();
			if (userRegistryService == null) {
				throw new AuthException("Service Unavailable", "UserRegistry service is not available.");
			}
			userAccount = userRegistryService.getUserAccount(username);
		} catch (UserRegistryException e) {
			e.printStackTrace();
			throw new AuthException(e, "User Registry Exception", e.getMessage());
		}
		if (userAccount == null) {
			throw new AuthException("Unauthorized", "Invalid username: " + username);
		}

		// Step4. Create new UserToken and store it in the tokenManager
		UserToken userToken = null;
		try {
			userToken = createAccessToken(userAccount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuthException("Authentication Failed", e.getMessage());
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

	/**
	 * 
	 * @param userAccount
	 * @return
	 * @throws Exception
	 */
	private UserToken createAccessToken(UserAccount userAccount) throws Exception {
		UserToken userToken = null;
		try {
			// String issuer = getFullName();
			String issuer = "orbit.auth";
			String subject1 = "user_access_token";
			String subject2 = "user_refresh_token";
			String audiences = userAccount.getUserId();

			// Access token expires in 30 minutes
			// Refresh token expires in 24 hours
			// - The access token should be updated/refreshed with each access to web services. Accessing web services should return response header for the new
			// access token with updated expiration time.
			Date now = new Date();
			Date accessTokenExpiresAt = DateUtil.addMinutes(now, 30);
			Date refreshTokenExpiresAt = DateUtil.addHours(now, 24);

			String username = userAccount.getUserId();
			String email = userAccount.getEmail();
			String firstName = userAccount.getFirstName();
			String lastName = userAccount.getLastName();
			String[][] keyValuePairs = new String[][] { new String[] { "username", username }, new String[] { "email", email }, new String[] { "firstName", firstName }, new String[] { "lastName", lastName } };

			String accessToken = JWTUtil.createToken(this.tokenSecret, issuer, subject1, audiences, accessTokenExpiresAt, keyValuePairs);
			String refreshToken = JWTUtil.createToken(this.tokenSecret, issuer, subject2, audiences, refreshTokenExpiresAt, keyValuePairs);

			userToken = new UserToken();
			userToken.setUsername(username);
			userToken.setAccessToken(accessToken);
			userToken.setRefreshToken(refreshToken);
			userToken.setAccessTokenExpireTime(accessTokenExpiresAt);
			userToken.setRefreshTokenExpireTime(refreshTokenExpiresAt);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return userToken;
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
