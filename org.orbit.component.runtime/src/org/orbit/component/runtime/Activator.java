package org.orbit.component.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.cli.ServicesCommand;
import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.component.runtime.tier1.account.ws.UserRegistryServiceAdapter;
import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.component.runtime.tier1.auth.ws.AuthServiceAdapter;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.component.runtime.tier1.config.ws.ConfigRegistryServiceAdapter;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.component.runtime.tier2.appstore.ws.AppStoreServiceAdapter;
import org.orbit.component.runtime.tier3.domain.service.DomainService;
import org.orbit.component.runtime.tier3.domain.ws.DomainServiceAdapter;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.orbit.component.runtime.tier3.transferagent.ws.TransferAgentServiceAdapter;
import org.orbit.infra.api.indexes.IndexProviderConnector;
import org.orbit.infra.api.indexes.IndexProviderConnectorAdapter;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
import org.orbit.infra.api.indexes.IndexServiceUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getBundleContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected IndexProviderConnectorAdapter indexProviderConnectorAdapter;

	// tier1
	protected boolean hasUserRegistryService;
	protected boolean autoStartUserRegistryService;
	protected UserRegistryServiceAdapter userRegistryServiceAdapter;

	protected boolean hasAuthService;
	protected boolean autoStartAuthService;
	protected AuthServiceAdapter authServiceAdapter;

	protected boolean hasConfigRegistryService;
	protected boolean autoStartConfigRegistryService;
	protected ConfigRegistryServiceAdapter configRegistryServiceAdapter;

	// tier2
	protected boolean hasAppStoreService;
	protected boolean autoStartAppStoreService;
	protected AppStoreServiceAdapter appStoreServiceAdapter;

	// tier3
	protected boolean hasDomainMgmtService;
	protected boolean autoStartDomainMgmtService;
	protected DomainServiceAdapter domainMgmtServiceAdapter;

	protected boolean hasTransferAgentService;
	protected boolean autoStartTransferAgentService;
	protected TransferAgentServiceAdapter transferAgentServiceAdapter;

	protected ServicesCommand servicesCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.context = bundleContext;
		Activator.instance = this;

		// Get the available components
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		// TASetupUtil.loadConfigIniProperties(bundleContext, configProps);

		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_AUTH_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_SERVICE_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME);

		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_AUTH_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_SERVICE_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_TRANSFER_AGENT_AUTOSTART);

		hasUserRegistryService = configProps.containsKey(OrbitConstants.COMPONENT_USER_REGISTRY_NAME) ? true : false;
		hasAuthService = configProps.containsKey(OrbitConstants.COMPONENT_AUTH_NAME) ? true : false;
		hasConfigRegistryService = configProps.containsKey(OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME) ? true : false;
		hasAppStoreService = configProps.containsKey(OrbitConstants.COMPONENT_APP_STORE_NAME) ? true : false;
		hasDomainMgmtService = configProps.containsKey(OrbitConstants.COMPONENT_DOMAIN_SERVICE_NAME) ? true : false;
		hasTransferAgentService = configProps.containsKey(OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME) ? true : false;

		autoStartUserRegistryService = configProps.containsKey(OrbitConstants.COMPONENT_USER_REGISTRY_AUTOSTART) ? true : false;
		autoStartAuthService = configProps.containsKey(OrbitConstants.COMPONENT_AUTH_AUTOSTART) ? true : false;
		autoStartConfigRegistryService = configProps.containsKey(OrbitConstants.COMPONENT_CONFIG_REGISTRY_AUTOSTART) ? true : false;
		autoStartAppStoreService = configProps.containsKey(OrbitConstants.COMPONENT_APP_STORE_AUTOSTART) ? true : false;
		autoStartDomainMgmtService = configProps.containsKey(OrbitConstants.COMPONENT_DOMAIN_SERVICE_AUTOSTART) ? true : false;
		autoStartTransferAgentService = configProps.containsKey(OrbitConstants.COMPONENT_TRANSFER_AGENT_AUTOSTART) ? true : false;

		LOG.info("hasUserRegistryService = " + hasUserRegistryService);
		LOG.info("hasAuthService = " + hasAuthService);
		LOG.info("hasConfigRegistryService = " + hasConfigRegistryService);
		LOG.info("hasAppStoreService = " + hasAppStoreService);
		LOG.info("hasDomainMgmtService = " + hasDomainMgmtService);
		LOG.info("hasTransferAgentService = " + hasTransferAgentService);

		LOG.info("autoStartUserRegistryService = " + autoStartUserRegistryService);
		LOG.info("autoStartAuthService = " + autoStartAuthService);
		LOG.info("autoStartConfigRegistryService = " + autoStartConfigRegistryService);
		LOG.info("autoStartAppStoreService = " + autoStartAppStoreService);
		LOG.info("autoStartDomainMgmtService = " + autoStartDomainMgmtService);
		LOG.info("autoStartTransferAgentService = " + autoStartTransferAgentService);

		// Start commands
		this.servicesCommand = new ServicesCommand();
		this.servicesCommand.start(bundleContext);

		// tier1
		if (autoStartUserRegistryService) {
			this.servicesCommand.startservice(ServicesCommand.USER_REGISTRY);
		}
		if (autoStartAuthService) {
			this.servicesCommand.startservice(ServicesCommand.AUTH);
		}
		if (autoStartConfigRegistryService) {
			this.servicesCommand.startservice(ServicesCommand.CONFIGR_EGISTRY);
		}

		// tier2
		if (autoStartAppStoreService) {
			this.servicesCommand.startservice(ServicesCommand.APP_STORE);
		}

		// tier3
		if (autoStartDomainMgmtService) {
			this.servicesCommand.startservice(ServicesCommand.DOMAIN);
		}
		if (autoStartTransferAgentService) {
			this.servicesCommand.startservice(ServicesCommand.TRANSFER_AGENT);
		}

		this.indexProviderConnectorAdapter = new IndexProviderConnectorAdapter() {
			@Override
			public void connectorAdded(IndexProviderConnector connector) {
				doStart(Activator.context, connector);
			}

			@Override
			public void connectorRemoved(IndexProviderConnector connector) {
				doStop(Activator.context);
			}
		};
		this.indexProviderConnectorAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		if (this.indexProviderConnectorAdapter != null) {
			this.indexProviderConnectorAdapter.stop(bundleContext);
			this.indexProviderConnectorAdapter = null;
		}

		// Stop commands
		if (this.servicesCommand != null) {
			// tier3
			if (getTransferAgentService() != null) {
				this.servicesCommand.stopservice(ServicesCommand.TRANSFER_AGENT);
			}
			if (getDomainMgmtService() != null) {
				this.servicesCommand.stopservice(ServicesCommand.DOMAIN);
			}

			// tier2
			if (getAppStoreService() != null) {
				this.servicesCommand.stopservice(ServicesCommand.APP_STORE);
			}

			// tier1
			if (getConfigRegistryService() != null) {
				this.servicesCommand.stopservice(ServicesCommand.CONFIGR_EGISTRY);
			}
			if (getAuthService() != null) {
				this.servicesCommand.stopservice(ServicesCommand.AUTH);
			}
			if (getUserRegistryService() != null) {
				this.servicesCommand.stopservice(ServicesCommand.USER_REGISTRY);
			}

			this.servicesCommand.stop(bundleContext);
			this.servicesCommand = null;
		}

		Activator.instance = null;
		Activator.context = null;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param connector
	 */
	protected void doStart(BundleContext bundleContext, IndexProviderConnector connector) {
		// Get IndexProvider load balancer
		// load properties from accessing index service
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		// TASetupUtil.loadConfigIniProperties(bundleContext, indexProviderProps);
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OrbitConstants.COMPONENT_INDEX_SERVICE_URL);
		IndexProviderLoadBalancer indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(connector, indexProviderProps);

		// Start service adapters
		// tier1
		if (hasUserRegistryService) {
			userRegistryServiceAdapter = new UserRegistryServiceAdapter(indexProviderLoadBalancer);
			userRegistryServiceAdapter.start(bundleContext);
		}

		if (hasAuthService) {
			authServiceAdapter = new AuthServiceAdapter(indexProviderLoadBalancer);
			authServiceAdapter.start(bundleContext);
		}

		if (hasConfigRegistryService) {
			configRegistryServiceAdapter = new ConfigRegistryServiceAdapter(indexProviderLoadBalancer);
			configRegistryServiceAdapter.start(bundleContext);
		}

		// tier2
		if (hasAppStoreService) {
			appStoreServiceAdapter = new AppStoreServiceAdapter(indexProviderLoadBalancer);
			appStoreServiceAdapter.start(bundleContext);
		}

		// tier3
		if (hasDomainMgmtService) {
			domainMgmtServiceAdapter = new DomainServiceAdapter(indexProviderLoadBalancer);
			domainMgmtServiceAdapter.start(bundleContext);
		}
		if (hasTransferAgentService) {
			transferAgentServiceAdapter = new TransferAgentServiceAdapter(indexProviderLoadBalancer);
			transferAgentServiceAdapter.start(bundleContext);
		}
	}

	/**
	 * 
	 * @param bundleContext
	 */
	protected void doStop(BundleContext bundleContext) {
		// Stop service adapters
		// tier3
		if (domainMgmtServiceAdapter != null) {
			domainMgmtServiceAdapter.stop(bundleContext);
			domainMgmtServiceAdapter = null;
		}
		if (transferAgentServiceAdapter != null) {
			transferAgentServiceAdapter.stop(bundleContext);
			transferAgentServiceAdapter = null;
		}

		// tier2
		if (appStoreServiceAdapter != null) {
			appStoreServiceAdapter.stop(bundleContext);
			appStoreServiceAdapter = null;
		}

		// tier1
		if (userRegistryServiceAdapter != null) {
			userRegistryServiceAdapter.stop(bundleContext);
			userRegistryServiceAdapter = null;
		}
		if (authServiceAdapter != null) {
			authServiceAdapter.stop(bundleContext);
			authServiceAdapter = null;
		}
		if (configRegistryServiceAdapter != null) {
			configRegistryServiceAdapter.stop(bundleContext);
			configRegistryServiceAdapter = null;
		}
	}

	public UserRegistryService getUserRegistryService() {
		return (userRegistryServiceAdapter != null) ? userRegistryServiceAdapter.getService() : null;
	}

	public AuthService getAuthService() {
		return (authServiceAdapter != null) ? authServiceAdapter.getService() : null;
	}

	public ConfigRegistryService getConfigRegistryService() {
		return (configRegistryServiceAdapter != null) ? configRegistryServiceAdapter.getService() : null;
	}

	public AppStoreService getAppStoreService() {
		return (appStoreServiceAdapter != null) ? appStoreServiceAdapter.getService() : null;
	}

	public DomainService getDomainMgmtService() {
		return (domainMgmtServiceAdapter != null) ? domainMgmtServiceAdapter.getService() : null;
	}

	public TransferAgentService getTransferAgentService() {
		return (transferAgentServiceAdapter != null) ? transferAgentServiceAdapter.getService() : null;
	}

}

// protected static boolean hasOauth2Component;
// protected static OAuth2ServiceAdapter oauth2ServiceAdapter;

// public IndexProvider createIndexProvider() {
// if (this.indexProviderLoadBalancer == null) {
// return null;
// }
// return this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
// }

// public static OAuth2Service getOAuth2Service() {
// return (oauth2ServiceAdapter != null) ? oauth2ServiceAdapter.getService() : null;
// }

// -----------------------------------------------------------------------------
// Get common properties
// -----------------------------------------------------------------------------
// Map<Object, Object> commonProps = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HOST_URL);
// String hostURL = (String) commonProps.get(OrbitConstants.ORBIT_HOST_URL);
// System.out.println("hostURL = " + hostURL);

// Map<Object, Object> commonProps = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.OSGI_HTTP_PORT);
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HTTP_HOST);
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HTTP_PORT);
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HTTP_CONTEXTROOT);
// PropertyUtil.loadProperty(bundleContext, commonProps, OrbitConstants.ORBIT_HOST_URL);

// // get http host
// String host = (String) commonProps.get(OrbitConstants.ORBIT_HTTP_HOST);
// if (host == null || host.isEmpty()) {
// try {
// InetAddress address = InetAddress.getLocalHost();
// host = address.getHostAddress();
// } catch (UnknownHostException e) {
// e.printStackTrace();
// }
// }
//
// // get http port
// String port = (String) commonProps.get(OrbitConstants.OSGI_HTTP_PORT);
// if (port == null || port.isEmpty()) {
// port = (String) commonProps.get(OrbitConstants.ORBIT_HTTP_PORT);
// }
// if (port == null || port.isEmpty()) {
// port = "80";
// }
//
// // current host URL
// String hostURL = "http://" + host + ":" + port;
// System.out.println("hostURL = " + hostURL);

// port = (String) commonProps.get(OrbitConstants.ORBIT_HOST_URL);

// public static void main(String[] args) {
// String host = "";
// try {
// InetAddress address = InetAddress.getLocalHost();
// host = address.getHostAddress();
// } catch (UnknownHostException e) {
// e.printStackTrace();
// }
// System.out.println("host = " + host);
// }

// PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_OAUTH2_NAME);

// hasOauth2Component = configProps.containsKey(OrbitConstants.COMPONENT_OAUTH2_NAME) ? true : false;
// System.out.println("hasOauth2Component = " + hasOauth2Component);

// if (hasOauth2Component) {
// oauth2ServiceAdapter = new OAuth2ServiceAdapter(this.indexProviderLoadBalancer);
// oauth2ServiceAdapter.start(bundleContext);
// }

// if (hasOauth2Component) {
// if (oauth2ServiceAdapter != null) {
// oauth2ServiceAdapter.stop(bundleContext);
// oauth2ServiceAdapter = null;
// }
// }

// if (hasOauth2Component) {
// this.servicesCommand.startservice(ComponentServicesCommand.OAUTH2);
// }

// if (hasOauth2Component) {
// this.servicesCommand.stopservice(ComponentServicesCommand.OAUTH2);
// }
