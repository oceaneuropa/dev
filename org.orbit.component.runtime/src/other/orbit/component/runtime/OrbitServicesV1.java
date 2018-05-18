package other.orbit.component.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.Activator;
import org.orbit.component.runtime.common.ws.OrbitConstants;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.component.runtime.tier4.missioncontrol.service.MissionControlService;
import org.orbit.infra.api.indexes.IndexProviderLoadBalancer;
import org.orbit.infra.api.indexes.IndexServiceUtil;
import org.orbit.infra.api.indexes.other.IndexProviderConnectorAdapterV1;
import org.orbit.infra.api.indexes.other.IndexProviderConnectorV1;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import other.orbit.component.runtime.tier1.account.ws.UserRegistryServiceAdapterV1;
import other.orbit.component.runtime.tier1.auth.ws.AuthServiceAdapterV1;
import other.orbit.component.runtime.tier1.config.ws.ConfigRegistryServiceAdapterV1;
import other.orbit.component.runtime.tier2.appstore.ws.AppStoreServiceAdapterV1;
import other.orbit.component.runtime.tier3.domainmanagement.ws.DomainServiceAdapterV1;
import other.orbit.component.runtime.tier3.nodecontrol.ws.TransferAgentServiceAdapterV1;
import other.orbit.component.runtime.tier4.missioncontrol.ws.MissionControlAdapterV1;

public class OrbitServicesV1 {

	protected static Logger LOG = LoggerFactory.getLogger(OrbitServicesV1.class);

	private static Object lock = new Object[0];
	private static OrbitServicesV1 instance = null;

	public static OrbitServicesV1 getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OrbitServicesV1();
				}
			}
		}
		return instance;
	}

	protected IndexProviderConnectorAdapterV1 indexProviderConnectorAdapter;

	// tier1
	protected UserRegistryServiceAdapterV1 userRegistryServiceAdapter;
	protected AuthServiceAdapterV1 authServiceAdapter;
	protected ConfigRegistryServiceAdapterV1 configRegistryServiceAdapter;

	// tier2
	protected AppStoreServiceAdapterV1 appStoreServiceAdapter;

	// tier3
	protected DomainServiceAdapterV1 domainServiceAdapter;
	protected TransferAgentServiceAdapterV1 transferAgentServiceAdapter;

	// tier4
	protected MissionControlAdapterV1 missionControlServiceAdapter;

	public void start(final BundleContext bundleContext) {
		this.indexProviderConnectorAdapter = new IndexProviderConnectorAdapterV1() {
			@Override
			public void connectorAdded(IndexProviderConnectorV1 connector) {
				doStart(Activator.getBundleContext(), connector);
			}

			@Override
			public void connectorRemoved(IndexProviderConnectorV1 connector) {
				doStop(Activator.getBundleContext());
			}
		};
		this.indexProviderConnectorAdapter.start(bundleContext);
	}

	public void stop(final BundleContext bundleContext) {
		if (this.indexProviderConnectorAdapter != null) {
			this.indexProviderConnectorAdapter.stop(bundleContext);
			this.indexProviderConnectorAdapter = null;
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param connector
	 */
	protected void doStart(BundleContext bundleContext, IndexProviderConnectorV1 connector) {
		// Get IndexProvider load balancer
		// load properties from accessing index service
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		// TASetupUtil.loadConfigIniProperties(bundleContext, indexProviderProps);
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OrbitConstants.ORBIT_INDEX_SERVICE_URL);
		IndexProviderLoadBalancer indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(connector, indexProviderProps);

		// Start service adapters
		// tier1
		userRegistryServiceAdapter = new UserRegistryServiceAdapterV1(indexProviderLoadBalancer);
		userRegistryServiceAdapter.start(bundleContext);

		authServiceAdapter = new AuthServiceAdapterV1(indexProviderLoadBalancer);
		authServiceAdapter.start(bundleContext);

		configRegistryServiceAdapter = new ConfigRegistryServiceAdapterV1(indexProviderLoadBalancer);
		configRegistryServiceAdapter.start(bundleContext);

		// tier2
		appStoreServiceAdapter = new AppStoreServiceAdapterV1(indexProviderLoadBalancer);
		appStoreServiceAdapter.start(bundleContext);

		// tier3
		domainServiceAdapter = new DomainServiceAdapterV1(indexProviderLoadBalancer);
		domainServiceAdapter.start(bundleContext);

		transferAgentServiceAdapter = new TransferAgentServiceAdapterV1(indexProviderLoadBalancer);
		transferAgentServiceAdapter.start(bundleContext);

		// tier4
		missionControlServiceAdapter = new MissionControlAdapterV1(indexProviderLoadBalancer);
		missionControlServiceAdapter.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	protected void doStop(BundleContext bundleContext) {
		// Stop service adapters
		// tier4
		if (missionControlServiceAdapter != null) {
			missionControlServiceAdapter.stop(bundleContext);
			missionControlServiceAdapter = null;
		}

		// tier3
		if (domainServiceAdapter != null) {
			domainServiceAdapter.stop(bundleContext);
			domainServiceAdapter = null;
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

	// tier1
	public UserRegistryService getUserRegistryService() {
		return (this.userRegistryServiceAdapter != null) ? this.userRegistryServiceAdapter.getService() : null;
	}

	public AuthService getAuthService() {
		return (this.authServiceAdapter != null) ? this.authServiceAdapter.getService() : null;
	}

	public ConfigRegistryService getConfigRegistryService() {
		return (this.configRegistryServiceAdapter != null) ? this.configRegistryServiceAdapter.getService() : null;
	}

	// tier2
	public AppStoreService getAppStoreService() {
		return (this.appStoreServiceAdapter != null) ? this.appStoreServiceAdapter.getService() : null;
	}

	// tier3
	public DomainManagementService getDomainService() {
		return (this.domainServiceAdapter != null) ? this.domainServiceAdapter.getService() : null;
	}

	public NodeControlService getTransferAgentService() {
		return (this.transferAgentServiceAdapter != null) ? this.transferAgentServiceAdapter.getService() : null;
	}

	// tier4
	public MissionControlService getMissionControlService() {
		return (this.missionControlServiceAdapter != null) ? this.missionControlServiceAdapter.getService() : null;
	}

}

// protected boolean hasUserRegistryService;
// protected boolean hasAuthService;
// protected boolean hasConfigRegistryService;
// protected boolean hasAppStoreService;
// protected boolean hasDomainMgmtService;
// protected boolean hasTransferAgentService;
// protected boolean hasMissionControlService;

// Map<Object, Object> configProps = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_USER_REGISTRY_NAME);
// PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_AUTH_NAME);
// PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME);
// PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_APP_STORE_NAME);
// PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_DOMAIN_SERVICE_NAME);
// PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME);
// PropertyUtil.loadProperty(bundleContext, configProps, OrbitConstants.COMPONENT_MISSION_CONTROL_NAME);
// hasUserRegistryService = configProps.containsKey(OrbitConstants.COMPONENT_USER_REGISTRY_NAME) ? true : false;
// hasAuthService = configProps.containsKey(OrbitConstants.COMPONENT_AUTH_NAME) ? true : false;
// hasConfigRegistryService = configProps.containsKey(OrbitConstants.COMPONENT_CONFIG_REGISTRY_NAME) ? true : false;
// hasAppStoreService = configProps.containsKey(OrbitConstants.COMPONENT_APP_STORE_NAME) ? true : false;
// hasDomainMgmtService = configProps.containsKey(OrbitConstants.COMPONENT_DOMAIN_SERVICE_NAME) ? true : false;
// hasTransferAgentService = configProps.containsKey(OrbitConstants.COMPONENT_TRANSFER_AGENT_NAME) ? true : false;
// hasMissionControlService = configProps.containsKey(OrbitConstants.COMPONENT_MISSION_CONTROL_NAME) ? true : false;
// LOG.info("hasUserRegistryService = " + hasUserRegistryService);
// LOG.info("hasAuthService = " + hasAuthService);
// LOG.info("hasConfigRegistryService = " + hasConfigRegistryService);
// LOG.info("hasAppStoreService = " + hasAppStoreService);
// LOG.info("hasDomainMgmtService = " + hasDomainMgmtService);
// LOG.info("hasTransferAgentService = " + hasTransferAgentService);
// LOG.info("hasMissionControlService = " + hasMissionControlService);

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

//// get http host
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
//// get http port
// String port = (String) commonProps.get(OrbitConstants.OSGI_HTTP_PORT);
// if (port == null || port.isEmpty()) {
// port = (String) commonProps.get(OrbitConstants.ORBIT_HTTP_PORT);
// }
// if (port == null || port.isEmpty()) {
// port = "80";
// }
//
//// current host URL
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
