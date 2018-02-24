package org.orbit.component.api;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.config.ConfigRegistry;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.nodecontrol.NodeManagementClient;
import org.orbit.component.api.tier4.mission.MissionControlClient;
import org.origin.common.rest.client.GlobalContext;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.util.PropertyUtil;
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
	protected ServiceConnectorAdapter<UserRegistry> userRegistryConnector;
	protected ServiceConnectorAdapter<Auth> authConnector;
	protected ServiceConnectorAdapter<ConfigRegistry> configRegistryConnector;

	// tier2
	protected ServiceConnectorAdapter<AppStore> appStoreConnector;

	// tier3
	protected ServiceConnectorAdapter<DomainManagementClient> domainServiceConnector;
	protected ServiceConnectorAdapter<NodeManagementClient> transferAgentConnector;

	// tier4
	protected ServiceConnectorAdapter<MissionControlClient> missionControlConnector;

	public OrbitClients() {
	}

	public void start(final BundleContext bundleContext) {
		// tier1
		this.authConnector = new ServiceConnectorAdapter<Auth>(Auth.class);
		this.authConnector.start(bundleContext);

		this.userRegistryConnector = new ServiceConnectorAdapter<UserRegistry>(UserRegistry.class);
		this.userRegistryConnector.start(bundleContext);

		this.configRegistryConnector = new ServiceConnectorAdapter<ConfigRegistry>(ConfigRegistry.class);
		this.configRegistryConnector.start(bundleContext);

		// tier2
		this.appStoreConnector = new ServiceConnectorAdapter<AppStore>(AppStore.class);
		this.appStoreConnector.start(bundleContext);

		// tier3
		this.domainServiceConnector = new ServiceConnectorAdapter<DomainManagementClient>(DomainManagementClient.class);
		this.domainServiceConnector.start(bundleContext);

		this.transferAgentConnector = new ServiceConnectorAdapter<NodeManagementClient>(NodeManagementClient.class);
		this.transferAgentConnector.start(bundleContext);

		// tier4
		this.missionControlConnector = new ServiceConnectorAdapter<MissionControlClient>(MissionControlClient.class);
		this.missionControlConnector.start(bundleContext);

		// Client API properties for connecting to other remote services.
		Map<Object, Object> configProps = new Hashtable<Object, Object>();

		PropertyUtil.loadProperty(bundleContext, configProps, org.orbit.component.api.OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, configProps, org.orbit.component.api.OrbitConstants.ORBIT_TRANSFER_AGENT_URL);
	}

	public void stop(final BundleContext bundleContext) {
		// tier1
		if (this.userRegistryConnector != null) {
			this.userRegistryConnector.stop(bundleContext);
			this.userRegistryConnector = null;
		}

		if (this.authConnector != null) {
			this.authConnector.stop(bundleContext);
			this.authConnector = null;
		}

		if (this.configRegistryConnector != null) {
			this.configRegistryConnector.stop(bundleContext);
			this.configRegistryConnector = null;
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

		if (this.transferAgentConnector != null) {
			this.transferAgentConnector.stop(bundleContext);
			this.transferAgentConnector = null;
		}

		// tier4
		if (this.missionControlConnector != null) {
			this.missionControlConnector.stop(bundleContext);
			this.missionControlConnector = null;
		}
	}

	public UserRegistry getUserRegistry(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_USER_REGISTRY_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_USER_REGISTRY_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_USER_REGISTRY_URL + "' property is not found.");
		}
		return getUserRegistry(url);
	}

	public UserRegistry getUserRegistry(String url) {
		return getUserRegistry(null, null, url);
	}

	public UserRegistry getUserRegistry(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

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

	public Auth getAuth(Map<?, ?> properties) {
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

	public Auth getAuth(String url) {
		return getAuth(null, null, url);
	}

	public Auth getAuth(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

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

	public ConfigRegistry getConfigRegistry(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_CONFIG_REGISTRY_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_CONFIG_REGISTRY_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_CONFIG_REGISTRY_URL + "' property is not found.");
		}
		return getConfigRegistry(url);
	}

	public ConfigRegistry getConfigRegistry(String url) {
		return getConfigRegistry(null, null, url);
	}

	public ConfigRegistry getConfigRegistry(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		ConfigRegistry configRegistry = this.configRegistryConnector.getService(properties);
		if (configRegistry == null) {
			LOG.error("ConfigRegistry is not available.");
			throw new IllegalStateException("ConfigRegistry is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return configRegistry;
	}

	public AppStore getAppStore(Map<?, ?> properties) {
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

	public AppStore getAppStore(String url) {
		return getAppStore(null, null, url);
	}

	public AppStore getAppStore(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		AppStore auth = this.appStoreConnector.getService(properties);
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

	public NodeManagementClient getTransferAgent(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OrbitConstants.ORBIT_TRANSFER_AGENT_URL);
		}
		if (url == null) {
			LOG.error("'" + OrbitConstants.ORBIT_TRANSFER_AGENT_URL + "' property is not found.");
			throw new IllegalStateException("'" + OrbitConstants.ORBIT_TRANSFER_AGENT_URL + "' property is not found.");
		}
		return getTransferAgent(url);
	}

	public NodeManagementClient getTransferAgent(String url) {
		return getTransferAgent(null, null, url);
	}

	public NodeManagementClient getTransferAgent(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		NodeManagementClient transferAgent = this.transferAgentConnector.getService(properties);
		if (transferAgent == null) {
			LOG.error("TransferAgent is not available.");
			throw new IllegalStateException("TransferAgent is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return transferAgent;
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
//
// public void setUserInfo(UserInfo userInfo) {
// this.threadUserInfo.set(userInfo);
// }
