package org.orbit.component.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.api.tier1.configregistry.ConfigRegistryClient;
import org.orbit.component.api.tier1.identity.IdentityServiceClient;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier4.missioncontrol.MissionControlClient;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.service.ILifecycle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ComponentClients implements ILifecycle {

	protected static Logger LOG = LoggerFactory.getLogger(ComponentClients.class);

	private static ComponentClients INSTANCE = new ComponentClients();

	public static ComponentClients getInstance() {
		return INSTANCE;
	}

	// tier1
	protected ServiceConnectorAdapter<IdentityServiceClient> identityServiceConnector;
	protected ServiceConnectorAdapter<UserAccountClient> userAccountsConnector;
	protected ServiceConnectorAdapter<AuthClient> authConnector;
	protected ServiceConnectorAdapter<ConfigRegistryClient> registryConnector;

	// tier2
	protected ServiceConnectorAdapter<AppStoreClient> appStoreConnector;

	// tier3
	protected ServiceConnectorAdapter<DomainManagementClient> domainServiceConnector;
	protected ServiceConnectorAdapter<NodeControlClient> nodeControlConnector;

	// tier4
	protected ServiceConnectorAdapter<MissionControlClient> missionControlConnector;

	private ComponentClients() {
	}

	@Override
	public void start(final BundleContext bundleContext) {
		// tier1
		this.identityServiceConnector = new ServiceConnectorAdapter<IdentityServiceClient>(IdentityServiceClient.class);
		this.identityServiceConnector.start(bundleContext);

		this.userAccountsConnector = new ServiceConnectorAdapter<UserAccountClient>(UserAccountClient.class);
		this.userAccountsConnector.start(bundleContext);

		this.authConnector = new ServiceConnectorAdapter<AuthClient>(AuthClient.class);
		this.authConnector.start(bundleContext);

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

	@Override
	public void stop(final BundleContext bundleContext) {
		// tier1
		if (this.identityServiceConnector != null) {
			this.identityServiceConnector.stop(bundleContext);
			this.identityServiceConnector = null;
		}

		if (this.userAccountsConnector != null) {
			this.userAccountsConnector.stop(bundleContext);
			this.userAccountsConnector = null;
		}

		if (this.authConnector != null) {
			this.authConnector.stop(bundleContext);
			this.authConnector = null;
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

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public IdentityServiceClient getIdentityClient(Map<String, Object> properties) {
		IdentityServiceClient identityClient = this.identityServiceConnector.getService(properties);
		if (identityClient == null) {
			throw new RuntimeException("IdentityClient is not available.");
		}
		return identityClient;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public UserAccountClient getUserAccounts(Map<String, Object> properties) {
		UserAccountClient userRegistry = this.userAccountsConnector.getService(properties);
		if (userRegistry == null) {
			throw new RuntimeException("UserAccountClient is not available.");
		}
		return userRegistry;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public AuthClient getAuth(Map<String, Object> properties) {
		AuthClient auth = this.authConnector.getService(properties);
		if (auth == null) {
			throw new RuntimeException("AuthClient is not available.");
		}
		return auth;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public ConfigRegistryClient getConfigRegistry(Map<String, Object> properties) {
		ConfigRegistryClient configRegistry = this.registryConnector.getService(properties);
		if (configRegistry == null) {
			throw new RuntimeException("ConfigRegistryClient is not available.");
		}
		return configRegistry;
	}

	/**
	 * 
	 * @param appStoreUrl
	 * @param accessToken
	 * @return
	 */
	public AppStoreClient getAppStoreClient(String appStoreUrl, String accessToken) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSClientConstants.REALM, null);
		properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		properties.put(WSClientConstants.URL, appStoreUrl);

		AppStoreClient appStoreClient = this.appStoreConnector.getService(properties);
		return appStoreClient;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public DomainManagementClient getDomainClient(Map<String, Object> properties) {
		DomainManagementClient domainService = this.domainServiceConnector.getService(properties);
		if (domainService == null) {
			throw new RuntimeException("DomainManagementClient is not available.");
		}
		return domainService;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public NodeControlClient getNodeControl(Map<String, Object> properties) {
		NodeControlClient nodeControlClient = this.nodeControlConnector.getService(properties);
		if (nodeControlClient == null) {
			throw new RuntimeException("NodeControlClient is not available.");
		}
		return nodeControlClient;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public MissionControlClient getMissionControl(Map<String, Object> properties) {
		MissionControlClient missionControl = this.missionControlConnector.getService(properties);
		if (missionControl == null) {
			throw new IllegalStateException("MissionControl is not available.");
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

// public UserAccountClient getUserAccounts(String url) {
// return getUserAccounts(null, null, url);
// }
//
// public UserAccountClient getUserAccounts(String realm, String username, String url) {
// realm = GlobalContext.getInstance().checkRealm(realm);
// username = GlobalContext.getInstance().checkUsername(realm, username);
//
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(OrbitConstants.REALM, realm);
// properties.put(OrbitConstants.USERNAME, username);
// properties.put(OrbitConstants.URL, url);
//
// UserAccountClient userRegistry = this.userAccountsConnector.getService(properties);
// if (userRegistry == null) {
// LOG.error("UserAccountClient is not available.");
// throw new IllegalStateException("UserAccountClient is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
// }
// return userRegistry;
// }

// public ConfigRegistryClient getConfigRegistry(String url) {
// return getConfigRegistry(null, null, url);
// }
//
// public ConfigRegistryClient getConfigRegistry(String realm, String username, String url) {
// realm = GlobalContext.getInstance().checkRealm(realm);
// username = GlobalContext.getInstance().checkUsername(realm, username);
//
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(OrbitConstants.REALM, realm);
// properties.put(OrbitConstants.USERNAME, username);
// properties.put(OrbitConstants.URL, url);
//

// public AuthClient getAuth(String url) {
// return getAuth(null, null, url);
// }
//
// public AuthClient getAuth(String realm, String username, String url) {
// realm = GlobalContext.getInstance().checkRealm(realm);
// username = GlobalContext.getInstance().checkUsername(realm, username);
//
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(OrbitConstants.REALM, realm);
// properties.put(OrbitConstants.USERNAME, username);
// properties.put(OrbitConstants.URL, url);

// public IdentityClient getIdentityClient(String url) {
// return getIdentityClient(null, null, url);
// }
//
// public IdentityClient getIdentityClient(String realm, String username, String url) {
// realm = GlobalContext.getInstance().checkRealm(realm);
// username = GlobalContext.getInstance().checkUsername(realm, username);
//
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(OrbitConstants.REALM, realm);
// properties.put(OrbitConstants.USERNAME, username);
// properties.put(OrbitConstants.URL, url);
//
// IdentityClient identityClient = this.identityServiceConnector.getService(properties);
// if (identityClient == null) {
// LOG.error("IdentityClient is not available.");
// throw new IllegalStateException("IdentityClient is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
// }
// return identityClient;
// }

// AuthClient auth = this.authConnector.getService(properties);
// if (auth == null) {
// LOG.error("Auth is not available.");
// throw new IllegalStateException("Auth is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
// }
// return auth;
// }

// ConfigRegistryClient configRegistry = this.registryConnector.getService(properties);
// if (configRegistry == null) {
// LOG.error("ConfigRegistry is not available.");
// throw new IllegalStateException("ConfigRegistry is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
// }
// return configRegistry;
// }

// public MissionControlClient getMissionControl(String url) {
// return getMissionControl(null, null, url);
// }
//

// public MissionControlClient getMissionControl(String realm, String username, String url) {
// realm = GlobalContext.getInstance().checkRealm(realm);
// username = GlobalContext.getInstance().checkUsername(realm, username);
//
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(OrbitConstants.REALM, realm);
// properties.put(OrbitConstants.USERNAME, username);
// properties.put(OrbitConstants.URL, url);
//
// MissionControlClient missionControl = this.missionControlConnector.getService(properties);
// if (missionControl == null) {
// LOG.error("MissionControl is not available.");
// throw new IllegalStateException("MissionControl is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
// }
// return missionControl;
// }

// /**
// *
// * @param properties
// * @return
// */
// public AppStoreClient getAppStore(Map<String, Object> properties) {
// AppStoreClient appStoreClient = this.appStoreConnector.getService(properties);
// if (appStoreClient == null) {
// throw new RuntimeException("AppStore is not available.");
// }
// return appStoreClient;
// }
