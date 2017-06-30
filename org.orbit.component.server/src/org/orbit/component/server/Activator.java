package org.orbit.component.server;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.cli.ComponentServicesCommand;
import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.orbit.component.server.tier1.account.ws.UserRegistryServiceAdapter;
import org.orbit.component.server.tier1.config.service.ConfigRegistryService;
import org.orbit.component.server.tier1.config.ws.ConfigRegistryServiceAdapter;
import org.orbit.component.server.tier1.session.service.OAuth2Service;
import org.orbit.component.server.tier1.session.ws.OAuth2ServiceAdapter;
import org.orbit.component.server.tier2.appstore.service.AppStoreService;
import org.orbit.component.server.tier2.appstore.ws.AppStoreServiceAdapter;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.orbit.component.server.tier3.domain.ws.DomainMgmtServiceAdapter;
import org.orbit.component.server.tier3.transferagent.service.TransferAgentService;
import org.orbit.component.server.tier3.transferagent.util.SetupUtil;
import org.orbit.component.server.tier3.transferagent.ws.TransferAgentServiceAdapter;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.client.api.IndexServiceUtil;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static BundleContext bundleContext;

	// tier1
	protected static boolean hasUserRegistryComponent;
	protected static boolean hasOauth2Component;
	protected static boolean hasConfigRegistryComponent;
	// tier2
	protected static boolean hasAppStoreComponent;
	// tier3
	protected static boolean hasDomainMgmtComponent;
	protected static boolean hasTransferAgentComponent;

	// tier1
	protected static UserRegistryServiceAdapter userRegistryServiceAdapter;
	protected static OAuth2ServiceAdapter oauth2ServiceAdapter;
	protected static ConfigRegistryServiceAdapter configRegistryServiceAdapter;
	// tier2
	protected static AppStoreServiceAdapter appStoreServiceAdapter;
	// tier3
	protected static DomainMgmtServiceAdapter domainMgmtServiceAdapter;
	protected static TransferAgentServiceAdapter transferAgentServiceAdapter;

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static UserRegistryService getUserRegistryService() {
		return (userRegistryServiceAdapter != null) ? userRegistryServiceAdapter.getService() : null;
	}

	public static OAuth2Service getOAuth2Service() {
		return (oauth2ServiceAdapter != null) ? oauth2ServiceAdapter.getService() : null;
	}

	public static ConfigRegistryService getConfigRegistryService() {
		return (configRegistryServiceAdapter != null) ? configRegistryServiceAdapter.getService() : null;
	}

	public static AppStoreService getAppStoreService() {
		return (appStoreServiceAdapter != null) ? appStoreServiceAdapter.getService() : null;
	}

	public static DomainManagementService getDomainMgmtService() {
		return (domainMgmtServiceAdapter != null) ? domainMgmtServiceAdapter.getService() : null;
	}

	public static TransferAgentService getTransferAgentService() {
		return (transferAgentServiceAdapter != null) ? transferAgentServiceAdapter.getService() : null;
	}

	protected boolean debug = true;
	protected IndexProviderLoadBalancer indexProviderLoadBalancer;
	protected ComponentServicesCommand servicesCommand;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		if (debug) {
			System.out.println(getClass().getName() + ".start()");
		}

		Activator.bundleContext = bundleContext;

		// -----------------------------------------------------------------------------
		// Get IndexProvider load balancer
		// -----------------------------------------------------------------------------
		// load properties from accessing index service
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		SetupUtil.loadTAConfigIniProperties(bundleContext, indexProviderProps);
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OrbitConstants.COMPONENT_INDEX_SERVICE_URL);
		this.indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(indexProviderProps);

		// -----------------------------------------------------------------------------
		// Get the available components
		// -----------------------------------------------------------------------------
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		SetupUtil.loadTAConfigIniProperties(bundleContext, configProps);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_OAUTH2_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME);
		PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME);

		hasUserRegistryComponent = configProps.containsKey(OrbitConstants.COMPONENT_USER_REGISTRY_NAME) ? true : false;
		hasOauth2Component = configProps.containsKey(OrbitConstants.COMPONENT_OAUTH2_NAME) ? true : false;
		hasConfigRegistryComponent = configProps.containsKey(OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME) ? true : false;
		hasAppStoreComponent = configProps.containsKey(OrbitConstants.COMPONENT_APP_STORE_NAME) ? true : false;
		hasDomainMgmtComponent = configProps.containsKey(OrbitConstants.COMPONENT_DOMAIN_MANAGEMENT_NAME) ? true : false;
		hasTransferAgentComponent = configProps.containsKey(OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME) ? true : false;

		if (debug) {
			System.out.println("hasUserRegistryComponent = " + hasUserRegistryComponent);
			System.out.println("hasOauth2Component = " + hasOauth2Component);
			System.out.println("hasConfigRegistryComponent = " + hasConfigRegistryComponent);
			System.out.println("hasAppStoreComponent = " + hasAppStoreComponent);
			System.out.println("hasDomainMgmtComponent = " + hasDomainMgmtComponent);
			System.out.println("hasTransferAgentComponent = " + hasTransferAgentComponent);
		}

		// -----------------------------------------------------------------------------
		// Start service adapters
		// -----------------------------------------------------------------------------
		// tier1
		if (hasUserRegistryComponent) {
			userRegistryServiceAdapter = new UserRegistryServiceAdapter(this.indexProviderLoadBalancer);
			userRegistryServiceAdapter.start(bundleContext);
		}
		if (hasOauth2Component) {
			oauth2ServiceAdapter = new OAuth2ServiceAdapter(this.indexProviderLoadBalancer);
			oauth2ServiceAdapter.start(bundleContext);
		}
		if (hasConfigRegistryComponent) {
			configRegistryServiceAdapter = new ConfigRegistryServiceAdapter(this.indexProviderLoadBalancer);
			configRegistryServiceAdapter.start(bundleContext);
		}
		// tier2
		if (hasAppStoreComponent) {
			appStoreServiceAdapter = new AppStoreServiceAdapter(this.indexProviderLoadBalancer);
			appStoreServiceAdapter.start(bundleContext);
		}
		// tier3
		if (hasDomainMgmtComponent) {
			domainMgmtServiceAdapter = new DomainMgmtServiceAdapter(this.indexProviderLoadBalancer);
			domainMgmtServiceAdapter.start(bundleContext);
		}
		if (hasTransferAgentComponent) {
			transferAgentServiceAdapter = new TransferAgentServiceAdapter(this.indexProviderLoadBalancer);
			transferAgentServiceAdapter.start(bundleContext);
		}

		// --------------------------------------------------------------------------------
		// Start CLI commands
		// --------------------------------------------------------------------------------
		this.servicesCommand = new ComponentServicesCommand();
		this.servicesCommand.start(bundleContext);
		// tier1
		if (hasUserRegistryComponent) {
			this.servicesCommand.startservice(ComponentServicesCommand.USER_REGISTRY);
		}
		if (hasOauth2Component) {
			this.servicesCommand.startservice(ComponentServicesCommand.OAUTH2);
		}
		if (hasConfigRegistryComponent) {
			this.servicesCommand.startservice(ComponentServicesCommand.CONFIGR_EGISTRY);
		}
		// tier2
		if (hasAppStoreComponent) {
			this.servicesCommand.startservice(ComponentServicesCommand.APP_STORE);
		}
		// tier3
		if (hasDomainMgmtComponent) {
			this.servicesCommand.startservice(ComponentServicesCommand.DOMAIN);
		}
		if (hasTransferAgentComponent) {
			this.servicesCommand.startservice(ComponentServicesCommand.TRANSFER_AGENT);
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (debug) {
			System.out.println(getClass().getName() + ".stop()");
		}

		// --------------------------------------------------------------------------------
		// Stop CLI commands
		// --------------------------------------------------------------------------------
		if (this.servicesCommand != null) {
			// tier3
			if (hasTransferAgentComponent) {
				this.servicesCommand.stopservice(ComponentServicesCommand.TRANSFER_AGENT);
			}
			if (hasDomainMgmtComponent) {
				this.servicesCommand.stopservice(ComponentServicesCommand.DOMAIN);
			}
			// tier2
			if (hasAppStoreComponent) {
				this.servicesCommand.stopservice(ComponentServicesCommand.APP_STORE);
			}
			// tier1
			if (hasConfigRegistryComponent) {
				this.servicesCommand.stopservice(ComponentServicesCommand.CONFIGR_EGISTRY);
			}
			if (hasOauth2Component) {
				this.servicesCommand.stopservice(ComponentServicesCommand.OAUTH2);
			}
			if (hasUserRegistryComponent) {
				this.servicesCommand.stopservice(ComponentServicesCommand.USER_REGISTRY);
			}
			this.servicesCommand.stop(bundleContext);
			this.servicesCommand = null;
		}

		// -----------------------------------------------------------------------------
		// Stop service adapters
		// -----------------------------------------------------------------------------
		// tier3
		if (hasTransferAgentComponent) {
			if (transferAgentServiceAdapter != null) {
				transferAgentServiceAdapter.stop(bundleContext);
				transferAgentServiceAdapter = null;
			}
		}
		if (hasDomainMgmtComponent) {
			if (domainMgmtServiceAdapter != null) {
				domainMgmtServiceAdapter.stop(bundleContext);
				domainMgmtServiceAdapter = null;
			}
		}
		// tier2
		if (hasAppStoreComponent) {
			if (appStoreServiceAdapter != null) {
				appStoreServiceAdapter.stop(bundleContext);
				appStoreServiceAdapter = null;
			}
		}
		// tier1
		if (hasConfigRegistryComponent) {
			if (configRegistryServiceAdapter != null) {
				configRegistryServiceAdapter.stop(bundleContext);
				configRegistryServiceAdapter = null;
			}
		}
		if (hasOauth2Component) {
			if (oauth2ServiceAdapter != null) {
				oauth2ServiceAdapter.stop(bundleContext);
				oauth2ServiceAdapter = null;
			}
		}
		if (hasUserRegistryComponent) {
			if (userRegistryServiceAdapter != null) {
				userRegistryServiceAdapter.stop(bundleContext);
				userRegistryServiceAdapter = null;
			}
		}

		Activator.bundleContext = null;
	}

}

// public IndexProvider createIndexProvider() {
// if (this.indexProviderLoadBalancer == null) {
// return null;
// }
// return this.indexProviderLoadBalancer.createLoadBalancableIndexProvider();
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
