package org.orbit.component.api;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.config.ConfigRegistry;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier4.mission.MissionControl;
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
	protected ServiceConnectorAdapter<Auth> authConnector;
	protected ServiceConnectorAdapter<UserRegistry> userRegistryConnector;
	protected ServiceConnectorAdapter<ConfigRegistry> configRegistryConnector;

	// tier3
	protected ServiceConnectorAdapter<TransferAgent> taConnector;

	// tier4
	protected ServiceConnectorAdapter<MissionControl> missionControlConnector;

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

		// tier3
		this.taConnector = new ServiceConnectorAdapter<TransferAgent>(TransferAgent.class);
		this.taConnector.start(bundleContext);

		// tier4
		this.missionControlConnector = new ServiceConnectorAdapter<MissionControl>(MissionControl.class);
		this.missionControlConnector.start(bundleContext);
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

		if (this.configRegistryConnector != null) {
			this.configRegistryConnector.stop(bundleContext);
			this.configRegistryConnector = null;
		}

		// tier3
		if (this.taConnector != null) {
			this.taConnector.stop(bundleContext);
			this.taConnector = null;
		}

		// tier4
		if (this.missionControlConnector != null) {
			this.missionControlConnector.stop(bundleContext);
			this.missionControlConnector = null;
		}
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

	public TransferAgent getTransferAgent(String url) {
		return getTransferAgent(null, null, url);
	}

	public TransferAgent getTransferAgent(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

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

	public MissionControl getMissionControl(String url) {
		return getMissionControl(null, null, url);
	}

	public MissionControl getMissionControl(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OrbitConstants.REALM, realm);
		properties.put(OrbitConstants.USERNAME, username);
		properties.put(OrbitConstants.URL, url);

		MissionControl missionControl = this.missionControlConnector.getService(properties);
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
