package org.orbit.component.runtime.tier1.identity.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.model.account.UserAccount;
import org.orbit.component.runtime.model.identity.LoginRequest;
import org.orbit.component.runtime.model.identity.LoginResponse;
import org.orbit.component.runtime.model.identity.LogoutRequest;
import org.orbit.component.runtime.model.identity.LogoutResponse;
import org.orbit.component.runtime.model.identity.RegisterRequest;
import org.orbit.component.runtime.model.identity.RegisterResponse;
import org.orbit.component.runtime.tier1.account.service.UserAccountPersistence;
import org.orbit.component.runtime.tier1.account.service.UserAccountPersistenceFactory;
import org.orbit.platform.sdk.http.JWTTokenHandler;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.orbit.platform.sdk.util.ExtensionUtil;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.UUIDUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdentityServiceImpl implements IdentityService, LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(IdentityServiceImpl.class);

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistry;
	protected UserAccountPersistence userAccountPersistence;

	/**
	 * 
	 * @param initProperties
	 */
	public IdentityServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
	}

	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_IDENTITY_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_IDENTITY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_IDENTITY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_IDENTITY_JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_IDENTITY_JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_IDENTITY_JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_IDENTITY_JDBC_PASSWORD);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.COMPONENT_IDENTITY_JWT_TOKEN_SECRET);

		update(properties);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(IdentityService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	/**
	 * 
	 * @param properties
	 */
	public synchronized void update(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;

		String driver = (String) this.properties.get(ComponentConstants.COMPONENT_IDENTITY_JDBC_DRIVER);
		String url = (String) this.properties.get(ComponentConstants.COMPONENT_IDENTITY_JDBC_URL);
		String username = (String) this.properties.get(ComponentConstants.COMPONENT_IDENTITY_JDBC_USERNAME);
		String password = (String) this.properties.get(ComponentConstants.COMPONENT_IDENTITY_JDBC_PASSWORD);
		Properties databaseProperties = DatabaseUtil.getProperties(driver, url, username, password);

		try {
			this.userAccountPersistence = UserAccountPersistenceFactory.getInstance().create(databaseProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected UserAccountPersistence getUserAccountPersistence() {
		if (this.userAccountPersistence == null) {
			throw new IllegalStateException("userAccountPersistence is null");
		}
		return this.userAccountPersistence;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(ComponentConstants.COMPONENT_IDENTITY_NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(ComponentConstants.COMPONENT_IDENTITY_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(ComponentConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.properties.get(ComponentConstants.COMPONENT_IDENTITY_CONTEXT_ROOT);
		return contextRoot;
	}

	protected String getJWTTokenSecret() {
		String tokenSecret = (String) this.properties.get(ComponentConstants.COMPONENT_IDENTITY_JWT_TOKEN_SECRET);
		if (tokenSecret == null) {
			tokenSecret = "12345678";
		}
		return tokenSecret;
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleException(Exception e) throws ServerException {
		e.printStackTrace();
		if (e instanceof ServerException) {
			throw (ServerException) e;
		}
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	/**
	 * 
	 * @param username
	 * @throws ServerException
	 */
	protected void checkUsername(String username) throws ServerException {
		if (username == null || username.isEmpty()) {
			throw new ServerException(StatusDTO.RESP_400, "username is empty.");
		}
	}

	/**
	 * 
	 * @param email
	 * @throws ServerException
	 */
	protected void checkEmail(String email) throws ServerException {
		if (email == null || email.isEmpty()) {
			throw new ServerException(StatusDTO.RESP_400, "email is empty.");
		}
	}

	/**
	 * 
	 * @param password
	 * @throws ServerException
	 */
	protected void checkPassword(String password) throws ServerException {
		if (password == null || password.isEmpty()) {
			throw new ServerException(StatusDTO.RESP_400, "password is empty.");
		}
	}

	@Override
	public boolean usernameExists(String username) throws ServerException {
		boolean exists = false;
		try {
			exists = getUserAccountPersistence().usernameExists(username);

		} catch (Exception e) {
			handleException(e);
		}
		return exists;
	}

	@Override
	public boolean emailExists(String email) throws ServerException {
		boolean exists = false;
		try {
			exists = getUserAccountPersistence().emailExists(email);

		} catch (Exception e) {
			handleException(e);
		}
		return exists;
	}

	@Override
	public RegisterResponse register(RegisterRequest request) throws ServerException {
		boolean succeed = false;
		String message = null;
		try {
			String username = request.getUsername();
			String email = request.getEmail();
			String password = request.getPassword();

			checkUsername(username);
			checkEmail(email);
			checkPassword(password);

			String accountId = UUIDUtil.generateBase64EncodedUUID();
			boolean acountIdExists = this.userAccountPersistence.accountIdExists(accountId);
			if (acountIdExists) {
				int retryCount = 0;
				int maxRetry = 10;
				while (acountIdExists) {
					accountId = UUIDUtil.generateBase64EncodedUUID();
					acountIdExists = this.userAccountPersistence.accountIdExists(accountId);
					if (!acountIdExists) {
						break;
					}
					retryCount++;
					if (retryCount == maxRetry) {
						throw new ServerException(StatusDTO.RESP_500, "Cannot create account Id.");
					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			boolean usernameExists = this.userAccountPersistence.usernameExists(username);
			if (usernameExists) {
				throw new ServerException(StatusDTO.RESP_400, "Username already exists.");
			}
			boolean emailExists = this.userAccountPersistence.emailExists(email);
			if (emailExists) {
				throw new ServerException(StatusDTO.RESP_400, "Email already exists.");
			}

			UserAccount userAccount = getUserAccountPersistence().createUserAccount(accountId, username, password, email, null, null, null);
			if (userAccount != null) {
				succeed = true;
				message = "user is registered successfully.";
			} else {
				succeed = false;
				message = "user is not registered.";
			}

		} catch (Exception e) {
			handleException(e);
		}

		RegisterResponse response = new RegisterResponse();
		response.setSucceed(succeed);
		response.setMessage(message);
		return response;
	}

	@Override
	public LoginResponse login(LoginRequest request) throws ServerException {
		LoginResponse response = null;

		// String clientId = request.getClientId();
		String grantType = request.getGrantType();
		String username = request.getUsername();
		String email = request.getEmail();
		String password = request.getPassword();

		try {
			if ("password".equalsIgnoreCase(grantType)) {
				boolean succeed = false;
				String message = null;
				String tokenType = null;
				String tokenValue = null;

				UserAccount userAccount = null;

				if (username != null && !username.isEmpty() && getUserAccountPersistence().usernameExists(username)) {
					userAccount = getUserAccountPersistence().getUserAccountByUsername(username);
					if (userAccount != null) {
						String userPassword = userAccount.getPassword();
						if (userPassword != null && userPassword.equals(password)) {
							succeed = true;
							message = "Login succeed.";
						}
					}
				}
				if (!succeed) {
					if (email != null && !email.isEmpty() && getUserAccountPersistence().emailExists(email)) {
						userAccount = getUserAccountPersistence().getUserAccountByEmail(email);
						if (userAccount != null) {
							String userPassword = userAccount.getPassword();
							if (userPassword != null && userPassword.equals(password)) {
								succeed = true;
								message = "Login succeed.";
							}
						}
					}
				}

				if (succeed) {
					// Generate token
					tokenType = "Bearer";

					JWTTokenHandler tokenHandler = ExtensionUtil.JWT.getTokenHandler(ComponentConstants.TOKEN_PROVIDER__ORBIT);
					if (tokenHandler == null) {
						throw new ServerException("500", "JWTTokenHandler is null.");
					}

					// Get user basic information
					String _accountId = userAccount.getAccountId();
					String _username = userAccount.getUsername();
					String _email = userAccount.getEmail();
					String firstName = userAccount.getFirstName();
					String lastName = userAccount.getLastName();

					// Need to get roles, securityLevel and classificationLevels associated with UserAccount.
					String roles = OrbitRoles.SYSTEM_ADMIN + "," + OrbitRoles.USER;
					int securityLevel = Secured.SecurityLevels.LEVEL_1;
					String classificationLevels = Secured.ClassificationLevels.TOP_SECRET + "," + Secured.ClassificationLevels.SECRET + "," + Secured.ClassificationLevels.CONFIDENTIAL;

					Map<String, String> payload = new LinkedHashMap<String, String>();
					payload.put(JWTTokenHandler.PAYLOAD__ACCOUNT_ID, _accountId);
					payload.put(JWTTokenHandler.PAYLOAD__USERNAME, _username);
					payload.put(JWTTokenHandler.PAYLOAD__EMAIL, _email);
					payload.put(JWTTokenHandler.PAYLOAD__FIRST_NAME, firstName);
					payload.put(JWTTokenHandler.PAYLOAD__LAST_NAME, lastName);
					payload.put(JWTTokenHandler.PAYLOAD__ROLES, roles);
					payload.put(JWTTokenHandler.PAYLOAD__SECURITY_LEVEL, String.valueOf(securityLevel));
					payload.put(JWTTokenHandler.PAYLOAD__CLASSIFICATION_LEVELS, classificationLevels);

					tokenValue = tokenHandler.createToken(payload);

				} else {
					message = "Invalid username or password.";
				}

				response = new LoginResponse();
				response.setSucceed(succeed);
				response.setMessage(message);
				if (succeed) {
					response.setTokenType(tokenType);
					response.setTokenValue(tokenValue);
				}
			}

		} catch (Exception e) {
			handleException(e);
		}
		return response;
	}

	@Override
	public LogoutResponse logout(LogoutRequest request) throws ServerException {
		String tokenType = request.getTokenType();
		String tokenValue = request.getTokenValue();

		return null;
	}

}

// tokenValue = TokenUtil.createAccessToken(getJWTTokenSecret(), userAccount);
// public static String TOKEN_SECRET = "12345678";