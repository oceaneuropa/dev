package org.orbit.component.runtime.relay.other;

import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.OrbitRelayConstants;
import org.orbit.component.runtime.relay.util.SwitcherUtil;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.URIUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrbitSwitchers {

	protected static Logger LOG = LoggerFactory.getLogger(OrbitSwitchers.class);

	private static Object lock = new Object[0];
	private static OrbitSwitchers instance = null;

	public static OrbitSwitchers getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OrbitSwitchers();
				}
			}
		}
		return instance;
	}

	// tier1
	protected UserRegistryWSApplicationRelay userRegistrySwitcher;
	protected AuthWSApplicationRelay authSwitcher;
	protected ConfigRegistryWSApplicationRelay configRegistrySwitcher;

	// tier2
	protected AppStoreWSApplicationRelay appStoreSwitcher;

	// tier3
	protected DomainServiceWSApplicationRelay domainServiceSwitcher;
	protected TransferAgentWSApplicationRelay transferAgentSwitcher;

	// tier4
	protected MissionControlWSApplicationRelay missionControlSwitcher;

	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		Map<Object, Object> properties = new Hashtable<Object, Object>();

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

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_URLS);

		// PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_CONTEXT_ROOT);
		// PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_HOSTS);
		// PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_URLS);

		WSClientFactory factory = createClientFactory(bundleContext, properties);

		// tier1
		startUserRegistrySwitcher(bundleContext, factory, properties);
		startAuthSwitcher(bundleContext, factory, properties);
		startConfigRegistrySwitcher(bundleContext, factory, properties);

		// tier2
		startAppStoreSwitcher(bundleContext, factory, properties);

		// tier3
		startDomainServiceSwitcher(bundleContext, factory, properties);
		// startTransferAgentSwitcher(bundleContext, factory, properties);

		// tier4
		startMissionControlSwitcher(bundleContext, factory, properties);
	}

	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		// tier4
		stopMissionControlSwitcher(bundleContext);

		// tier3
		stopDomainServiceSwitcher(bundleContext);
		// stopTransferAgentSwitcher(bundleContext);

		// tier2
		stopAppStoreSwitcher(bundleContext);

		// tier1
		stopUserRegistrySwitcher(bundleContext);
		stopAuthSwitcher(bundleContext);
		stopConfigRegistrySwitcher(bundleContext);
	}

	protected List<URI> toList(String baseURIsString) {
		return URIUtil.toList(baseURIsString);
	}

	protected List<URI> toList(String hostURLsString, String contextRoot) {
		return URIUtil.toList(hostURLsString, contextRoot);
	}

	protected WSClientFactory createClientFactory(BundleContext bundleContext, Map<Object, Object> properties) {
		return new WSClientFactoryImpl();
	}

	// tier1
	protected void startUserRegistrySwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
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

		startUserRegistrySwitcher(bundleContext, factory, contextRoot, uriList);
	}

	protected void startUserRegistrySwitcher(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.userRegistrySwitcher = new UserRegistryWSApplicationRelay(contextRoot, uriSwitcher, factory);
		this.userRegistrySwitcher.start(bundleContext);
	}

	protected void stopUserRegistrySwitcher(BundleContext bundleContext) {
		if (this.userRegistrySwitcher != null) {
			this.userRegistrySwitcher.stop(bundleContext);
			this.userRegistrySwitcher = null;
		}
	}

	protected void startAuthSwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
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

		startAuthSwitcher(bundleContext, factory, contextRoot, uriList);
	}

	protected void startAuthSwitcher(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.authSwitcher = new AuthWSApplicationRelay(contextRoot, uriSwitcher, factory);
		this.authSwitcher.start(bundleContext);
	}

	protected void stopAuthSwitcher(BundleContext bundleContext) {
		if (this.authSwitcher != null) {
			this.authSwitcher.stop(bundleContext);
			this.authSwitcher = null;
		}
	}

	protected void startConfigRegistrySwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
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

		startConfigRegistrySwitcher(bundleContext, factory, contextRoot, uriList);
	}

	protected void startConfigRegistrySwitcher(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.configRegistrySwitcher = new ConfigRegistryWSApplicationRelay(contextRoot, uriSwitcher, factory);
		this.configRegistrySwitcher.start(bundleContext);
	}

	protected void stopConfigRegistrySwitcher(BundleContext bundleContext) {
		if (this.configRegistrySwitcher != null) {
			this.configRegistrySwitcher.stop(bundleContext);
			this.configRegistrySwitcher = null;
		}
	}

	// tier2
	protected void startAppStoreSwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
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

		startAppStoreSwitcher(bundleContext, factory, contextRoot, uriList);
	}

	protected void startAppStoreSwitcher(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.appStoreSwitcher = new AppStoreWSApplicationRelay(contextRoot, uriSwitcher, factory);
		this.appStoreSwitcher.start(bundleContext);
	}

	protected void stopAppStoreSwitcher(BundleContext bundleContext) {
		if (this.appStoreSwitcher != null) {
			this.appStoreSwitcher.stop(bundleContext);
			this.appStoreSwitcher = null;
		}
	}

	// tier3
	protected void startDomainServiceSwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_DOMAIN_MANAGEMENT_RELAY_URLS);

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

		startDomainServiceSwitcher(bundleContext, factory, contextRoot, uriList);
	}

	protected void startDomainServiceSwitcher(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.domainServiceSwitcher = new DomainServiceWSApplicationRelay(contextRoot, uriSwitcher, factory);
		this.domainServiceSwitcher.start(bundleContext);
	}

	protected void stopDomainServiceSwitcher(BundleContext bundleContext) {
		if (this.domainServiceSwitcher != null) {
			this.domainServiceSwitcher.stop(bundleContext);
			this.domainServiceSwitcher = null;
		}
	}

	protected void startTransferAgentSwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_NODE_CONTROL_RELAY_URLS);

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

		startTransferAgentSwitcher(bundleContext, factory, contextRoot, uriList);
	}

	protected void startTransferAgentSwitcher(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.transferAgentSwitcher = new TransferAgentWSApplicationRelay(contextRoot, uriSwitcher, factory);
		this.transferAgentSwitcher.start(bundleContext);
	}

	protected void stopTransferAgentSwitcher(BundleContext bundleContext) {
		if (this.transferAgentSwitcher != null) {
			this.transferAgentSwitcher.stop(bundleContext);
			this.transferAgentSwitcher = null;
		}
	}

	// tier4
	protected void startMissionControlSwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_HOSTS);
		String urls = (String) properties.get(OrbitRelayConstants.COMPONENT_MISSION_CONTROL_RELAY_URLS);

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

		startMissionControlSwitcher(bundleContext, factory, contextRoot, uriList);
	}

	protected void startMissionControlSwitcher(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.missionControlSwitcher = new MissionControlWSApplicationRelay(contextRoot, uriSwitcher, factory);
		this.missionControlSwitcher.start(bundleContext);
	}

	protected void stopMissionControlSwitcher(BundleContext bundleContext) {
		if (this.missionControlSwitcher != null) {
			this.missionControlSwitcher.stop(bundleContext);
			this.missionControlSwitcher = null;
		}
	}

}
