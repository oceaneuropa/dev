package org.orbit.component.runtime.relay;

import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.relay.desc.AppStoreWSApplicationDesc;
import org.orbit.component.runtime.relay.desc.AuthWSApplicationDesc;
import org.orbit.component.runtime.relay.desc.ConfigRegistryWSApplicationDesc;
import org.orbit.component.runtime.relay.desc.DomainServiceWSApplicationDesc;
import org.orbit.component.runtime.relay.desc.MissionControlWSApplicationDesc;
import org.orbit.component.runtime.relay.desc.NodeControlWSApplicationDesc;
import org.orbit.component.runtime.relay.desc.UserRegistryWSApplicationDesc;
import org.orbit.component.runtime.relay.util.SwitcherUtil;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.URIUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrbitRelays {

	protected static Logger LOG = LoggerFactory.getLogger(OrbitRelays.class);

	private static Object lock = new Object[0];
	private static OrbitRelays instance = null;

	public static OrbitRelays getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OrbitRelays();
				}
			}
		}
		return instance;
	}

	protected Map<Object, Object> properties;
	protected WSClientFactory wsClientFactory;

	// tier1
	protected WSRelayApplication userRegistryRelay;
	protected WSRelayApplication authRelay;
	protected WSRelayApplication configRegistryRelay;

	// tier2
	protected WSRelayApplication appStoreRelay;

	// tier3
	protected WSRelayApplication domainServiceRelay;
	protected WSRelayApplication nodeControlRelay;

	// tier4
	protected WSRelayApplication missionControlRelay;

	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		this.properties = new Hashtable<Object, Object>();

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_INDEX_SERVICE_URL);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_CONTEXTY_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_SWITCHER_URLS);

		this.wsClientFactory = new WSClientFactoryImpl();

		// tier1
		startUserRegistryRelay(bundleContext, this.wsClientFactory, properties);
		startAuthRelay(bundleContext, this.wsClientFactory, properties);
		startConfigRegistryRelay(bundleContext, this.wsClientFactory, properties);

		// tier2
		startAppStoreRelay(bundleContext, this.wsClientFactory, properties);

		// tier3
		startDomainServiceRelay(bundleContext, this.wsClientFactory, properties);
		startNodeControlRelay(bundleContext, this.wsClientFactory, properties);

		// tier4
		startMissionControlRelay(bundleContext, this.wsClientFactory, properties);
	}

	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		// tier4
		stopMissionControlRelay(bundleContext);

		// tier3
		stopDomainServiceRelay(bundleContext);
		stopNodeControlRelay(bundleContext);

		// tier2
		stopAppStoreRelay(bundleContext);

		// tier1
		stopUserRegistryRelay(bundleContext);
		stopAuthRelay(bundleContext);
		stopConfigRegistryRelay(bundleContext);
	}

	protected List<URI> toList(String baseURIsString) {
		return URIUtil.toList(baseURIsString);
	}

	protected List<URI> toList(String hostURLsString, String contextRoot) {
		return URIUtil.toList(hostURLsString, contextRoot);
	}

	public String getHostURL() {
		String globalHostURL = (String) this.properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	// tier1
	protected void startUserRegistryRelay(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_CONTEXTY_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_URLS);

		if (contextRoot == null) {
			return;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return;
		}

		UserRegistryWSApplicationDesc appDesc = new UserRegistryWSApplicationDesc(contextRoot);
		startUserRegistryRelay(bundleContext, appDesc, factory, uriList);
	}

	protected void startUserRegistryRelay(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);

		this.userRegistryRelay = new WSRelayApplication(appDesc, uriSwitcher, factory);
		this.userRegistryRelay.start(bundleContext);
	}

	protected void stopUserRegistryRelay(BundleContext bundleContext) {
		if (this.userRegistryRelay != null) {
			this.userRegistryRelay.stop(bundleContext);
			this.userRegistryRelay = null;
		}
	}

	protected void startAuthRelay(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_AUTH_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_AUTH_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_AUTH_RELAY_URLS);

		if (contextRoot == null) {
			return;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return;
		}

		AuthWSApplicationDesc appDesc = new AuthWSApplicationDesc(contextRoot);
		startAuthRelay(bundleContext, appDesc, factory, uriList);
	}

	protected void startAuthRelay(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.authRelay = new WSRelayApplication(appDesc, uriSwitcher, factory);
		this.authRelay.start(bundleContext);
	}

	protected void stopAuthRelay(BundleContext bundleContext) {
		if (this.authRelay != null) {
			this.authRelay.stop(bundleContext);
			this.authRelay = null;
		}
	}

	protected void startConfigRegistryRelay(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_URLS);

		if (contextRoot == null) {
			return;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return;
		}

		ConfigRegistryWSApplicationDesc appDesc = new ConfigRegistryWSApplicationDesc(contextRoot);
		startConfigRegistryRelay(bundleContext, appDesc, factory, uriList);
	}

	protected void startConfigRegistryRelay(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.configRegistryRelay = new WSRelayApplication(appDesc, uriSwitcher, factory);
		this.configRegistryRelay.start(bundleContext);
	}

	protected void stopConfigRegistryRelay(BundleContext bundleContext) {
		if (this.configRegistryRelay != null) {
			this.configRegistryRelay.stop(bundleContext);
			this.configRegistryRelay = null;
		}
	}

	// tier2
	protected void startAppStoreRelay(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_URLS);

		if (contextRoot == null) {
			return;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return;
		}

		AppStoreWSApplicationDesc appDesc = new AppStoreWSApplicationDesc(contextRoot);
		startAppStoreRelay(bundleContext, appDesc, factory, uriList);
	}

	protected void startAppStoreRelay(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.appStoreRelay = new WSRelayApplication(appDesc, uriSwitcher, factory);
		this.appStoreRelay.start(bundleContext);
	}

	protected void stopAppStoreRelay(BundleContext bundleContext) {
		if (this.appStoreRelay != null) {
			this.appStoreRelay.stop(bundleContext);
			this.appStoreRelay = null;
		}
	}

	// tier3
	protected void startDomainServiceRelay(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_URLS);

		if (contextRoot == null) {
			return;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return;
		}

		DomainServiceWSApplicationDesc appDesc = new DomainServiceWSApplicationDesc(contextRoot);
		startDomainServiceRelay(bundleContext, appDesc, factory, uriList);
	}

	protected void startDomainServiceRelay(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.domainServiceRelay = new WSRelayApplication(appDesc, uriSwitcher, factory);
		this.domainServiceRelay.start(bundleContext);
	}

	protected void stopDomainServiceRelay(BundleContext bundleContext) {
		if (this.domainServiceRelay != null) {
			this.domainServiceRelay.stop(bundleContext);
			this.domainServiceRelay = null;
		}
	}

	protected void startNodeControlRelay(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_SWITCHER_URLS);

		if (contextRoot == null) {
			return;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return;
		}

		NodeControlWSApplicationDesc appDesc = new NodeControlWSApplicationDesc(contextRoot);
		startNodeControlRelay(bundleContext, appDesc, factory, uriList);
	}

	protected void startNodeControlRelay(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.nodeControlRelay = new WSRelayApplication(appDesc, uriSwitcher, factory);
		this.nodeControlRelay.start(bundleContext);
	}

	protected void stopNodeControlRelay(BundleContext bundleContext) {
		if (this.nodeControlRelay != null) {
			this.nodeControlRelay.stop(bundleContext);
			this.nodeControlRelay = null;
		}
	}

	// tier4
	protected void startMissionControlRelay(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_SWITCHER_URLS);

		if (contextRoot == null) {
			return;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return;
		}

		MissionControlWSApplicationDesc appDesc = new MissionControlWSApplicationDesc(contextRoot);
		startMissionControlRelay(bundleContext, appDesc, factory, uriList);
	}

	protected void startMissionControlRelay(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.missionControlRelay = new WSRelayApplication(appDesc, uriSwitcher, factory);
		this.missionControlRelay.start(bundleContext);
	}

	protected void stopMissionControlRelay(BundleContext bundleContext) {
		if (this.missionControlRelay != null) {
			this.missionControlRelay.stop(bundleContext);
			this.missionControlRelay = null;
		}
	}

}
