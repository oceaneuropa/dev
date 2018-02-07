package org.orbit.component.runtime.switcher;

import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.switcher.tier1.AuthWSApplicationDesc;
import org.orbit.component.runtime.switcher.tier1.ConfigRegistryWSApplicationDesc;
import org.orbit.component.runtime.switcher.tier1.UserRegistryWSApplicationDesc;
import org.orbit.component.runtime.switcher.tier2.AppStoreWSApplicationDesc;
import org.orbit.component.runtime.switcher.tier3.DomainServiceWSApplicationDesc;
import org.orbit.component.runtime.switcher.tier3.TransferAgentWSApplicationDesc;
import org.orbit.component.runtime.switcher.tier4.MissionControlWSApplicationDesc;
import org.orbit.component.runtime.switcher.util.SwitcherHelper;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryJerseyImpl;
import org.origin.common.rest.server.WSApplicationDesc;
import org.origin.common.rest.server.WSApplicationDescriptiveSwitcher;
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

	protected WSClientFactory wsClientFactory;

	// tier1
	protected WSApplicationDescriptiveSwitcher userRegistrySwitcher;
	protected WSApplicationDescriptiveSwitcher authSwitcher;
	protected WSApplicationDescriptiveSwitcher configRegistrySwitcher;

	// tier2
	protected WSApplicationDescriptiveSwitcher appStoreSwitcher;

	// tier3
	protected WSApplicationDescriptiveSwitcher domainServiceSwitcher;
	protected WSApplicationDescriptiveSwitcher transferAgentSwitcher;

	// tier4
	protected WSApplicationDescriptiveSwitcher missionControlSwitcher;

	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		Map<Object, Object> properties = new Hashtable<Object, Object>();

		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_USER_REGISTRY_SWITCHER_CONTEXTY_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_USER_REGISTRY_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_USER_REGISTRY_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_AUTH_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_AUTH_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_AUTH_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_CONFIG_REGISTRY_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_CONFIG_REGISTRY_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_CONFIG_REGISTRY_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_APP_STORE_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_APP_STORE_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_APP_STORE_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_MISSION_CONTROL_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_MISSION_CONTROL_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitSwitchersConstants.COMPONENT_MISSION_CONTROL_SWITCHER_URLS);

		this.wsClientFactory = new WSClientFactoryJerseyImpl();

		// tier1
		startUserRegistrySwitcher(bundleContext, this.wsClientFactory, properties);
		startAuthSwitcher(bundleContext, this.wsClientFactory, properties);
		startConfigRegistrySwitcher(bundleContext, this.wsClientFactory, properties);

		// tier2
		startAppStoreSwitcher(bundleContext, this.wsClientFactory, properties);

		// tier3
		startDomainServiceSwitcher(bundleContext, this.wsClientFactory, properties);
		startTransferAgentSwitcher(bundleContext, this.wsClientFactory, properties);

		// tier4
		startMissionControlSwitcher(bundleContext, this.wsClientFactory, properties);
	}

	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		// tier4
		stopMissionControlSwitcher(bundleContext);

		// tier3
		stopDomainServiceSwitcher(bundleContext);
		stopTransferAgentSwitcher(bundleContext);

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

	// tier1
	protected void startUserRegistrySwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitSwitchersConstants.COMPONENT_USER_REGISTRY_SWITCHER_CONTEXTY_ROOT);
		String hosts = (String) properties.get(OrbitSwitchersConstants.COMPONENT_USER_REGISTRY_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitSwitchersConstants.COMPONENT_USER_REGISTRY_SWITCHER_URLS);

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
		startUserRegistrySwitcher(bundleContext, appDesc, factory, uriList);
	}

	protected void startUserRegistrySwitcher(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);

		this.userRegistrySwitcher = new WSApplicationDescriptiveSwitcher(appDesc, uriSwitcher, factory);
		this.userRegistrySwitcher.start(bundleContext);
	}

	protected void stopUserRegistrySwitcher(BundleContext bundleContext) {
		if (this.userRegistrySwitcher != null) {
			this.userRegistrySwitcher.stop(bundleContext);
			this.userRegistrySwitcher = null;
		}
	}

	protected void startAuthSwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitSwitchersConstants.COMPONENT_AUTH_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitSwitchersConstants.COMPONENT_AUTH_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitSwitchersConstants.COMPONENT_AUTH_SWITCHER_URLS);

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
		startAuthSwitcher(bundleContext, appDesc, factory, uriList);
	}

	protected void startAuthSwitcher(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.authSwitcher = new WSApplicationDescriptiveSwitcher(appDesc, uriSwitcher, factory);
		this.authSwitcher.start(bundleContext);
	}

	protected void stopAuthSwitcher(BundleContext bundleContext) {
		if (this.authSwitcher != null) {
			this.authSwitcher.stop(bundleContext);
			this.authSwitcher = null;
		}
	}

	protected void startConfigRegistrySwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitSwitchersConstants.COMPONENT_CONFIG_REGISTRY_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitSwitchersConstants.COMPONENT_CONFIG_REGISTRY_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitSwitchersConstants.COMPONENT_CONFIG_REGISTRY_SWITCHER_URLS);

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
		startConfigRegistrySwitcher(bundleContext, appDesc, factory, uriList);
	}

	protected void startConfigRegistrySwitcher(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.configRegistrySwitcher = new WSApplicationDescriptiveSwitcher(appDesc, uriSwitcher, factory);
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
		String contextRoot = (String) properties.get(OrbitSwitchersConstants.COMPONENT_APP_STORE_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitSwitchersConstants.COMPONENT_APP_STORE_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitSwitchersConstants.COMPONENT_APP_STORE_SWITCHER_URLS);

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
		startAppStoreSwitcher(bundleContext, appDesc, factory, uriList);
	}

	protected void startAppStoreSwitcher(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.appStoreSwitcher = new WSApplicationDescriptiveSwitcher(appDesc, uriSwitcher, factory);
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
		String contextRoot = (String) properties.get(OrbitSwitchersConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitSwitchersConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitSwitchersConstants.COMPONENT_DOMAIN_SERVICE_SWITCHER_URLS);

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
		startDomainServiceSwitcher(bundleContext, appDesc, factory, uriList);
	}

	protected void startDomainServiceSwitcher(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.domainServiceSwitcher = new WSApplicationDescriptiveSwitcher(appDesc, uriSwitcher, factory);
		this.domainServiceSwitcher.start(bundleContext);
	}

	protected void stopDomainServiceSwitcher(BundleContext bundleContext) {
		if (this.domainServiceSwitcher != null) {
			this.domainServiceSwitcher.stop(bundleContext);
			this.domainServiceSwitcher = null;
		}
	}

	protected void startTransferAgentSwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(OrbitSwitchersConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitSwitchersConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitSwitchersConstants.COMPONENT_TRANSFER_AGENT_SWITCHER_URLS);

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
		startTransferAgentSwitcher(bundleContext, appDesc, factory, uriList);
	}

	protected void startTransferAgentSwitcher(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_STICKY);
		this.transferAgentSwitcher = new WSApplicationDescriptiveSwitcher(appDesc, uriSwitcher, factory);
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
		String contextRoot = (String) properties.get(OrbitSwitchersConstants.COMPONENT_MISSION_CONTROL_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(OrbitSwitchersConstants.COMPONENT_MISSION_CONTROL_SWITCHER_HOSTS);
		String urls = (String) properties.get(OrbitSwitchersConstants.COMPONENT_MISSION_CONTROL_SWITCHER_URLS);

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
		startMissionControlSwitcher(bundleContext, appDesc, factory, uriList);
	}

	protected void startMissionControlSwitcher(BundleContext bundleContext, WSApplicationDesc appDesc, WSClientFactory factory, List<URI> uriList) {
		Switcher<URI> uriSwitcher = SwitcherHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.missionControlSwitcher = new WSApplicationDescriptiveSwitcher(appDesc, uriSwitcher, factory);
		this.missionControlSwitcher.start(bundleContext);
	}

	protected void stopMissionControlSwitcher(BundleContext bundleContext) {
		if (this.missionControlSwitcher != null) {
			this.missionControlSwitcher.stop(bundleContext);
			this.missionControlSwitcher = null;
		}
	}

}
