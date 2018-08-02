package org.orbit.component.api;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.api.tier1.configregistry.ConfigRegistryClient;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier4.missioncontrol.MissionControlClient;
import org.origin.common.rest.client.GlobalContext;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrbitClients {

	protected static Logger LOG = LoggerFactory.getLogger(OrbitClients.class);

	private static Object lock = new Object[0];
	private static OrbitClients instance = null;

	public static OrbitClients getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OrbitClients();
				}
			}
		}
		return instance;
	}

	// tier1
	protected ServiceConnectorAdapter<AuthClient> authConnector;
	protected ServiceConnectorAdapter<UserAccountClient> userAccountsConnector;
	protected ServiceConnectorAdapter<ConfigRegistryClient> registryConnector;

	// tier2
	protected ServiceConnectorAdapter<AppStoreClient> appStoreConnector;

	// tier3
	protected ServiceConnectorAdapter<DomainManagementClient> domainServiceConnector;
	protected ServiceConnectorAdapter<NodeControlClient> nodeControlConnector;

	// tier4
	protected ServiceConnectorAdapter<MissionControlClient> missionControlConnector;

	private OrbitClients() {
	}

	public void start(final BundleContext bundleContext) {
		// tier1
		this.authConnector = new ServiceConnectorAdapter<AuthClient>(AuthClient.class);
		this.authConnector.start(bundleContext);

		this.userAccountsConnector = new ServiceConnectorAdapter<UserAccountClient>(UserAccountClient.class);
		this.userAccountsConnector.start(bundleContext);

		this.registryConnector = new ServiceConnectorAdapter<ConfigRegistryClient>(ConfigRegistryClient.class);
		this.registryConnector.start(bundleContext);

		// tier2
		this.appStoreConnector = new ServiceConnectorAdapter<AppStoreClient>(AppStoreClient.class);
		this.appStoreConnector.start(bundleContext);

		// tier3
		this.domainServiceConnector = new ServiceConnectorAdapter<DomainManagementClient>(DomainManagementClient.class);
		this.domainServiceConnector.start(bundleContext);

		this.nodeControlConnector = new ServiceConnectorAdapter<NodeControlClient>(NodeControlClient.class);
		this.nodeControlConnector.start(bundleContext);

		// tier4
		this.missionControlConnector = new ServiceConnectorAdapter<MissionControlClient>(MissionControlClient.class);
		this.missionControlConnector.start(bundleContext);
	}

	public void stop(final BundleContext bundleContext) {
		// tier1
		if (this.authConnector != null) {
			this.authConnector.stop(bundleContext);
			this.authConnector = null;
		}

		if (this.userAccountsConnector != null) {
			this.userAccountsConnector.stop(bundleContext);
			this.userAccountsConnector = null;
		}

		if (this.registryConnector != null) {
			this.registryConnector.stop(bundleContext);
			this.registryConnector = null;
		}

		// tier2
		if (this.appStoreConnector != null) {
			this.appStoreConnector.stop(bundleContext);
			this.appStoreConnector = null;
		}

		// tier3
		if (this.domainServiceConnector != null) {
			this.domainServiceConnector.stop(bundleContext);
			this.domainServiceConnector = null;
		}

		if (this.nodeControlConnector != null) {
			this.nodeControlConnector.stop(bundleContext);
			this.nodeControlConnector = null;
		}

		// tier4
		if (this.missionControlConnector != null) {
			this.missionControlConnector.stop(bundleContext);
			this.missionControlConnector = null;
		}
	}

	public AuthClient getAuth(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_AUTH_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_AUTH_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_AUTH_URL + "' property is not found.");
		}
		return getAuth(url);
	}

	public AuthClient getAuth(String url) {
		return getAuth(null, null, url);
	}

	public AuthClient getAuth(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		AuthClient auth = this.authConnector.getService(properties);
		if (auth == null) {
			LOG.error("Auth is not available.");
			throw new IllegalStateException("Auth is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return auth;
	}

	public UserAccountClient getUserAccounts(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_USER_ACCOUNTS_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_USER_ACCOUNTS_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_USER_ACCOUNTS_URL + "' property is not found.");
		}
		return getUserAccounts(url);
	}

	public UserAccountClient getUserAccounts(String url) {
		return getUserAccounts(null, null, url);
	}

	public UserAccountClient getUserAccounts(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		UserAccountClient userRegistry = this.userAccountsConnector.getService(properties);
		if (userRegistry == null) {
			LOG.error("UserRegistry is not available.");
			throw new IllegalStateException("UserRegistry is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return userRegistry;
	}

	public ConfigRegistryClient getRegistry(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_REGISTRY_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_REGISTRY_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_REGISTRY_URL + "' property is not found.");
		}
		return getRegistry(url);
	}

	public ConfigRegistryClient getRegistry(String url) {
		return getRegistry(null, null, url);
	}

	public ConfigRegistryClient getRegistry(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		ConfigRegistryClient configRegistry = this.registryConnector.getService(properties);
		if (configRegistry == null) {
			LOG.error("ConfigRegistry is not available.");
			throw new IllegalStateException("ConfigRegistry is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return configRegistry;
	}

	public AppStoreClient getAppStore(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_APP_STORE_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_APP_STORE_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_APP_STORE_URL + "' property is not found.");
		}
		return getAppStore(url);
	}

	public AppStoreClient getAppStore(String url) {
		return getAppStore(null, null, url);
	}

	public AppStoreClient getAppStore(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		AppStoreClient auth = this.appStoreConnector.getService(properties);
		if (auth == null) {
			LOG.error("AppStore is not available.");
			throw new IllegalStateException("AppStore is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return auth;
	}

	public DomainManagementClient getDomainService(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_DOMAIN_SERVICE_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_DOMAIN_SERVICE_URL + "' property is not found.");
		}
		return getDomainService(url);
	}

	public DomainManagementClient getDomainService(String url) {
		return getDomainService(null, null, url);
	}

	public DomainManagementClient getDomainService(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		DomainManagementClient domainService = this.domainServiceConnector.getService(properties);
		if (domainService == null) {
			LOG.error("DomainService is not available.");
			throw new IllegalStateException("DomainService is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return domainService;
	}

	public NodeControlClient getNodeControl(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_NODE_CONTROL_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_NODE_CONTROL_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_NODE_CONTROL_URL + "' property is not found.");
		}
		return getNodeControl(url);
	}

	public NodeControlClient getNodeControl(String url) {
		return getNodeControl(null, null, url);
	}

	public NodeControlClient getNodeControl(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		NodeControlClient nodeControlClient = this.nodeControlConnector.getService(properties);
		if (nodeControlClient == null) {
			LOG.error("TransferAgent is not available.");
			throw new IllegalStateException("TransferAgent is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return nodeControlClient;
	}

	public MissionControlClient getMissionControl(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_MISSION_CONTROL_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_MISSION_CONTROL_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_MISSION_CONTROL_URL + "' property is not found.");
		}
		return getMissionControl(url);
	}

	public MissionControlClient getMissionControl(String url) {
		return getMissionControl(null, null, url);
	}

	public MissionControlClient getMissionControl(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		MissionControlClient missionControl = this.missionControlConnector.getService(properties);
		if (missionControl == null) {
			LOG.error("MissionControl is not available.");
			throw new IllegalStateException("MissionControl is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return missionControl;
	}

}

// public UserInfo getUserInfo() {
// return this.threadUserInfo.get();
// }

// public void setUserInfo(UserInfo userInfo) {
// this.threadUserInfo.set(userInfo);
// }

// Client API properties for connecting to other remote services.
// Map<Object, Object> properties = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, properties, org.orbit.component.api.OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
// PropertyUtil.loadProperty(bundleContext, properties, org.orbit.component.api.OrbitConstants.ORBIT_NODE_CONTROL_URL);
