package org.orbit.component.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrbitClient {

	protected static Logger LOG = LoggerFactory.getLogger(OrbitClient.class);

	private static String DEFAULT_REALM = ClientConfiguration.DEFAULT_REALM;
	private static String DEFAULT_USERNAME = ClientConfiguration.UNKNOWN_USERNAME;

	private static Object lock = new Object[0];
	private static OrbitClient instance = null;

	public static OrbitClient getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OrbitClient();
				}
			}
		}
		return instance;
	}

	public class UserInfo {
		private String realm;
		private String username;
		private String password;

		public UserInfo() {
		}

		public UserInfo(String realm, String username, String password) {
			this.realm = realm;
			this.username = username;
			this.password = password;
		}

		String getRealm() {
			return this.realm;
		}

		void setRealm(String realm) {
			this.realm = realm;
		}

		String getUsername() {
			return username;
		}

		void setUsername(String username) {
			this.username = username;
		}

		String getPassword() {
			return password;
		}

		void setPassword(String password) {
			this.password = password;
		}
	}

	protected ThreadLocal<UserInfo> threadUserInfo;

	// tier1
	protected ServiceConnectorAdapter<Auth> authConnector;
	protected ServiceConnectorAdapter<UserRegistry> userRegistryConnector;

	// tier3
	protected ServiceConnectorAdapter<TransferAgent> taConnector;

	public OrbitClient() {
		this.threadUserInfo = new ThreadLocal<UserInfo>() {
			@Override
			protected UserInfo initialValue() {
				return new UserInfo(DEFAULT_REALM, DEFAULT_USERNAME, null);
			}
		};
	}

	public void start(final BundleContext bundleContext) {
		// tier1
		this.authConnector = new ServiceConnectorAdapter<Auth>(Auth.class);
		this.authConnector.start(bundleContext);

		this.userRegistryConnector = new ServiceConnectorAdapter<UserRegistry>(UserRegistry.class);
		this.userRegistryConnector.start(bundleContext);

		// tier3
		this.taConnector = new ServiceConnectorAdapter<TransferAgent>(TransferAgent.class);
		this.taConnector.start(bundleContext);
	}

	public void stop(final BundleContext bundleContext) {
		// tier1
		if (this.authConnector != null) {
			this.authConnector.stop(bundleContext);
			this.authConnector = null;
		}

		if (this.userRegistryConnector != null) {
			this.userRegistryConnector.stop(bundleContext);
			this.userRegistryConnector = null;
		}

		// tier3
		if (this.taConnector != null) {
			this.taConnector.stop(bundleContext);
			this.taConnector = null;
		}
	}

	public String getRealm() {
		return this.threadUserInfo.get().getRealm();
	}

	public String getUsername() {
		return this.threadUserInfo.get().getUsername();
	}

	public void setUserInfo(String realm, String username, String password) {
		if (isRealmUnset(realm)) {
			realm = DEFAULT_REALM;
		}
		if (isUsernameUnset(username)) {
			username = DEFAULT_USERNAME;
		}

		UserInfo userInfo = this.threadUserInfo.get();
		userInfo.setRealm(realm);
		userInfo.setUsername(username);
		userInfo.setPassword(password);
	}

	protected boolean isRealmUnset(String realm) {
		if (realm == null || realm.isEmpty() || Parameter.UNSPECIFIED.equals(realm)) {
			return true;
		}
		return false;
	}

	protected boolean isUsernameUnset(String username) {
		if (username == null || username.isEmpty() || Parameter.UNSPECIFIED.equals(username)) {
			return true;
		}
		return false;
	}

	public Auth getAuth(String url) throws ClientException {
		return getAuth(null, null, url);
	}

	public Auth getAuth(String realm, String username, String url) throws ClientException {
		if (isRealmUnset(realm)) {
			realm = getRealm();
		}
		if (isUsernameUnset(username)) {
			username = getUsername();
		}

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		Auth auth = this.authConnector.getService(properties);
		if (auth == null) {
			LOG.error("Auth is not available.");
			throw new IllegalStateException("Auth is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return auth;
	}

	public UserRegistry getUserRegistryService(String url) throws ClientException {
		return getUserRegistryService(null, null, url);
	}

	public UserRegistry getUserRegistryService(String realm, String username, String url) throws ClientException {
		if (isRealmUnset(realm)) {
			realm = getRealm();
		}
		if (isUsernameUnset(username)) {
			username = getUsername();
		}

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		UserRegistry userRegistry = this.userRegistryConnector.getService(properties);
		if (userRegistry == null) {
			LOG.error("UserRegistry is not available.");
			throw new IllegalStateException("UserRegistry is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return userRegistry;
	}

	public TransferAgent getTransferAgent(String url) throws ClientException {
		return getTransferAgent(null, null, url);
	}

	public TransferAgent getTransferAgent(String realm, String username, String url) throws ClientException {
		if (isRealmUnset(realm)) {
			realm = getRealm();
		}
		if (isUsernameUnset(username)) {
			username = getUsername();
		}

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		TransferAgent transferAgent = this.taConnector.getService(properties);
		if (transferAgent == null) {
			LOG.error("TransferAgent is not available.");
			throw new IllegalStateException("TransferAgent is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return transferAgent;
	}

}

// public UserInfo getUserInfo() {
// return this.threadUserInfo.get();
// }
//
// public void setUserInfo(UserInfo userInfo) {
// this.threadUserInfo.set(userInfo);
// }
