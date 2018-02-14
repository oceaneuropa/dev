package org.orbit.component.runtime.relay;

import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.relay.tier1.AuthWSApplicationDesc;
import org.orbit.component.runtime.relay.tier1.ConfigRegistryWSApplicationDesc;
import org.orbit.component.runtime.relay.tier1.UserRegistryWSApplicationDesc;
import org.orbit.component.runtime.relay.tier2.AppStoreWSApplicationDesc;
import org.orbit.component.runtime.relay.tier3.DomainServiceWSApplicationDesc;
import org.orbit.component.runtime.relay.tier3.TransferAgentWSApplicationDesc;
import org.orbit.component.runtime.relay.tier4.MissionControlWSApplicationDesc;
import org.orbit.component.runtime.relay.util.SwitcherUtil;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSApplicationDescriptiveRelay;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.URIUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WSRelays {

	protected static Logger LOG = LoggerFactory.getLogger(WSRelays.class);

	private static Object lock = new Object[0];
	private static WSRelays instance = null;

	public static WSRelays getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new WSRelays();
				}
			}
		}
		return instance;
	}

	protected Map<Object, Object> properties;
	protected WSClientFactory wsClientFactory;

	// tier1
	protected WSApplicationDescriptiveRelay userRegistryRelay;
	protected WSApplicationDescriptiveRelay authRelay;
	protected WSApplicationDescriptiveRelay configRegistryRelay;

	// tier2
	protected WSApplicationDescriptiveRelay appStoreRelay;

	// tier3
	protected WSApplicationDescriptiveRelay domainServiceRelay;
	protected WSApplicationDescriptiveRelay transferAgentRelay;

	// tier4
	protected WSApplicationDescriptiveRelay missionControlRelay;

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

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_URLS);

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
		startTransferAgentRelay(bundleContext, this.wsClientFactory, properties);

		// tier4
		startMissionControlRelay(bundleContext, this.wsClientFactory, properties);
	}

	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		// tier4
		stopMissionControlRelay(bundleContext);

		// tier3
		stopDomainServiceRelay(bundleContext);
		stopTransferAgentRelay(bundleContext);

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

		this.userRegistryRelay = new WSApplicationDescriptiveRelay(appDesc, uriSwitcher, factory);
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
		this.authRelay = new WSApplicationDescriptiveRelay(appDesc, uriSwitcher, factory);
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
		this.configRegistryRelay = new WSApplicationDescriptiveRelay(appDesc, uriSwitcher, factory);
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
		this.appStoreRelay = new WSApplicationDescriptiveRelay(appDesc, uriSwitcher, factory);
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
		this.domainServiceRelay = new WSApplicationDescriptiveRelay(appDesc, uriSwitcher, factory);
		this.domainServiceRelay.start(bundleContext);
	}

	protected void stopDomainServiceRelay(BundleContext bundleContext) {
		if (this.domainServiceRelay != null) {
			this.domainServiceRelay.stop(bundleContext);
			this.domainServiceRelay = null;
		}
	}

	protected void startTransferAgentRelay(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_URLS);

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

		TransferAgentWSApplicationDesc appDesc = new TransferAgentWSApplicationDesc(contextRoot);
		startTransferAgentRelay(bundleContext, appDesc, factory, uriList);
	}

	protected void startTransferAgentRelay(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.transferAgentRelay = new WSApplicationDescriptiveRelay(appDesc, uriSwitcher, factory);
		this.transferAgentRelay.start(bundleContext);
	}

	protected void stopTransferAgentRelay(BundleContext bundleContext) {
		if (this.transferAgentRelay != null) {
			this.transferAgentRelay.stop(bundleContext);
			this.transferAgentRelay = null;
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
		this.missionControlRelay = new WSApplicationDescriptiveRelay(appDesc, uriSwitcher, factory);
		this.missionControlRelay.start(bundleContext);
	}

	protected void stopMissionControlRelay(BundleContext bundleContext) {
		if (this.missionControlRelay != null) {
			this.missionControlRelay.stop(bundleContext);
			this.missionControlRelay = null;
		}
	}

}
