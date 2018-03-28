package org.orbit.component.runtime;

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
import org.origin.common.rest.server.WSRelayApplication;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.service.WebServiceAware;
import org.origin.common.service.WebServiceAwareImpl;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.URIUtil;
import org.osgi.framework.BundleContext;

public class OrbitRelays {

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

	/**
	 * 
	 * @param bundleContext
	 * @param initProperties
	 * @return
	 */
	public WSRelayApplication createUserRegistryRelay(BundleContext bundleContext, Map<Object, Object> initProperties) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (initProperties != null) {
			properties.putAll(initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_CONTEXTY_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_URLS);

		String hostURL = (String) properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_NAME);
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_CONTEXTY_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_URLS);
		if (contextRoot == null) {
			return null;
		}
		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return null;
		}

		UserRegistryWSApplicationDesc desc = new UserRegistryWSApplicationDesc(contextRoot);
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);

		WSRelayApplication relay = new WSRelayApplication(desc, uriSwitcher, null);
		relay.adapt(WebServiceAware.class, new WebServiceAwareImpl(name, hostURL, contextRoot));

		return relay;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param properties
	 * @return
	 */
	public WSRelayApplication createAuthRelay(BundleContext bundleContext, Map<Object, Object> initProperties) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (initProperties != null) {
			properties.putAll(initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_URLS);

		String hostURL = (String) properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitRelayConstants.COMPONENT_AUTH_RELAY_NAME);
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_AUTH_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_AUTH_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_AUTH_RELAY_URLS);

		if (contextRoot == null) {
			return null;
		}
		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return null;
		}

		AuthWSApplicationDesc desc = new AuthWSApplicationDesc(contextRoot);
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);

		WSRelayApplication relay = new WSRelayApplication(desc, uriSwitcher, null);
		relay.adapt(WebServiceAware.class, new WebServiceAwareImpl(name, hostURL, contextRoot));

		return relay;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param properties
	 */
	public WSRelayApplication createConfigRegistryRelay(BundleContext bundleContext, Map<Object, Object> initProperties) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (initProperties != null) {
			properties.putAll(initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_URLS);

		String hostURL = (String) properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_NAME);
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_URLS);

		if (contextRoot == null) {
			return null;
		}
		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return null;
		}

		ConfigRegistryWSApplicationDesc desc = new ConfigRegistryWSApplicationDesc(contextRoot);
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);

		WSRelayApplication relay = new WSRelayApplication(desc, uriSwitcher, null);
		relay.adapt(WebServiceAware.class, new WebServiceAwareImpl(name, hostURL, contextRoot));

		return relay;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param initProperties
	 * @return
	 */
	public WSRelayApplication createAppStoreRelay(BundleContext bundleContext, Map<Object, Object> initProperties) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (initProperties != null) {
			properties.putAll(initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_URLS);

		String hostURL = (String) properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_NAME);
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_URLS);

		if (contextRoot == null) {
			return null;
		}
		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return null;
		}

		AppStoreWSApplicationDesc desc = new AppStoreWSApplicationDesc(contextRoot);
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);

		WSRelayApplication relay = new WSRelayApplication(desc, uriSwitcher, null);
		relay.adapt(WebServiceAware.class, new WebServiceAwareImpl(name, hostURL, contextRoot));

		return relay;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param initProperties
	 * @return
	 */
	public WSRelayApplication createDomainManagementRelay(BundleContext bundleContext, Map<Object, Object> initProperties) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (initProperties != null) {
			properties.putAll(initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_URLS);

		String hostURL = (String) properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_NAME);
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_URLS);

		if (contextRoot == null) {
			return null;
		}
		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return null;
		}

		DomainServiceWSApplicationDesc desc = new DomainServiceWSApplicationDesc(contextRoot);
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);

		WSRelayApplication relay = new WSRelayApplication(desc, uriSwitcher, null);
		relay.adapt(WebServiceAware.class, new WebServiceAwareImpl(name, hostURL, contextRoot));

		return relay;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param properties
	 * @return
	 */
	public WSRelayApplication createTransferAgentRelay(BundleContext bundleContext, Map<Object, Object> initProperties) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (initProperties != null) {
			properties.putAll(initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_URLS);

		String hostURL = (String) properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_NAME);
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_URLS);

		if (contextRoot == null) {
			return null;
		}
		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return null;
		}

		NodeControlWSApplicationDesc desc = new NodeControlWSApplicationDesc(contextRoot);
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);

		WSRelayApplication relay = new WSRelayApplication(desc, uriSwitcher, null);
		relay.adapt(WebServiceAware.class, new WebServiceAwareImpl(name, hostURL, contextRoot));

		return relay;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param properties
	 * @return
	 */
	public WSRelayApplication createMissionControlRelay(BundleContext bundleContext, Map<Object, Object> initProperties) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (initProperties != null) {
			properties.putAll(initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_URLS);

		String hostURL = (String) properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
		String name = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_NAME);
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_URLS);

		if (contextRoot == null) {
			return null;
		}
		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return null;
		}

		MissionControlWSApplicationDesc desc = new MissionControlWSApplicationDesc(contextRoot);
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);

		WSRelayApplication relay = new WSRelayApplication(desc, uriSwitcher, null);
		relay.adapt(WebServiceAware.class, new WebServiceAwareImpl(name, hostURL, contextRoot));

		return relay;
	}

	protected List<URI> toList(String baseURIsString) {
		return URIUtil.toList(baseURIsString);
	}

	protected List<URI> toList(String hostURLsString, String contextRoot) {
		return URIUtil.toList(hostURLsString, contextRoot);
	}

}

// protected static Logger LOG = LoggerFactory.getLogger(OrbitRelays.class);
// protected Map<Object, Object> properties;
// protected WSClientFactory wsClientFactory;

// protected WSRelayApplication userRegistryRelay;
// protected WSRelayApplication authRelay;
// protected WSRelayApplication configRegistryRelay;
// protected WSRelayApplication appStoreRelay;
// protected WSRelayApplication domainManagementRelay;
// protected WSRelayApplication transferAgentRelay;
// protected WSRelayApplication missionControlRelay;

// /**
// *
// * @param bundleContext
// */
// public void start(BundleContext bundleContext) {
// // LOG.info("start()");
//
// Map<Object, Object> properties = new Hashtable<Object, Object>();
//
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_HOST_URL);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.ORBIT_INDEX_SERVICE_URL);
//
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_CONTEXTY_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_HOSTS);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_USER_REGISTRY_RELAY_URLS);
//
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_HOSTS);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_AUTH_RELAY_URLS);
//
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_HOSTS);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_CONFIG_REGISTRY_RELAY_URLS);
//
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_HOSTS);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_APP_STORE_RELAY_URLS);
//
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_HOSTS);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_URLS);
//
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_SWITCHER_CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_SWITCHER_HOSTS);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_NODE_CONTROL_SWITCHER_URLS);
//
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_SWITCHER_CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_SWITCHER_HOSTS);
// PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_SWITCHER_URLS);
//
// // this.wsClientFactory = new WSClientFactoryImpl();
//
// // tier1
// this.userRegistryRelay = createUserRegistryRelay(bundleContext, properties);
// if (this.userRegistryRelay != null) {
// this.userRegistryRelay.start(bundleContext);
// }
// this.authRelay = createAuthRelay(bundleContext, properties);
// if (this.authRelay != null) {
// this.authRelay.start(bundleContext);
// }
// this.configRegistryRelay = createConfigRegistryRelay(bundleContext, properties);
// if (this.configRegistryRelay != null) {
// this.configRegistryRelay.start(bundleContext);
// }
//
// // tier2
// this.appStoreRelay = createAppStoreRelay(bundleContext, properties);
// if (this.appStoreRelay != null) {
// this.appStoreRelay.start(bundleContext);
// }
//
// // tier3
// this.domainServiceRelay = createDomainServiceRelay(bundleContext, properties);
// if (this.domainServiceRelay != null) {
// this.domainServiceRelay.start(bundleContext);
// }
//
// this.nodeControlRelay = createNodeControlRelay(bundleContext, properties);
// if (this.nodeControlRelay != null) {
// this.nodeControlRelay.start(bundleContext);
// }
//
// // tier4
// this.missionControlRelay = createMissionControlRelay(bundleContext, properties);
// if (this.missionControlRelay != null) {
// this.missionControlRelay.start(bundleContext);
// }
// }
//
// /**
// *
// * @param bundleContext
// */
// public void stop(BundleContext bundleContext) {
// // LOG.info("stop()");
//
// // tier4
// if (this.missionControlRelay != null) {
// this.missionControlRelay.stop(bundleContext);
// this.missionControlRelay = null;
// }
//
// // tier3
// if (this.domainServiceRelay != null) {
// this.domainServiceRelay.stop(bundleContext);
// this.domainServiceRelay = null;
// }
// if (this.nodeControlRelay != null) {
// this.nodeControlRelay.stop(bundleContext);
// this.nodeControlRelay = null;
// }
//
// // tier2
// if (this.appStoreRelay != null) {
// this.appStoreRelay.stop(bundleContext);
// this.appStoreRelay = null;
// }
//
// // tier1
// if (this.userRegistryRelay != null) {
// this.userRegistryRelay.stop(bundleContext);
// this.userRegistryRelay = null;
// }
// if (this.authRelay != null) {
// this.authRelay.stop(bundleContext);
// this.authRelay = null;
// }
// if (this.configRegistryRelay != null) {
// this.configRegistryRelay.stop(bundleContext);
// this.configRegistryRelay = null;
// }
// }

// protected String getHostURL() {
// String globalHostURL = (String) this.properties.get(OrbitRelayConstants.ORBIT_HOST_URL);
// if (globalHostURL != null) {
// return globalHostURL;
// }
// return null;
// }
