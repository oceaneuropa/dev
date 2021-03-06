package other.orbit.component.connector;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import other.orbit.component.connector.tier1.account.UserRegistryManager;

public class OrbitConnectors {

	protected static final String USER_REGISTRY_MANAGED_SERVICE_FACTORY_PID = "component.userregistry.manager"; //$NON-NLS-1$

	protected static Logger LOG = LoggerFactory.getLogger(OrbitConnectors.class);

	private static Object lock = new Object[0];
	private static OrbitConnectors instance = null;

	public static OrbitConnectors getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OrbitConnectors();
				}
			}
		}
		return instance;
	}

	// ManagedServiceFactory
	protected UserRegistryManager userRegistryManager;

	// Connectors
	// // tier1
	// protected ConfigRegistryConnector configRegistryConnector;
	// protected UserRegistryConnector userRegistryConnector;
	// protected AuthConnector authConnector;
	//
	// // tier2
	// protected AppStoreConnector appStoreConnector;
	//
	// // tier3
	// protected DomainManagementConnector domainServiceConnector;
	// protected NodeControlConnector transferAgentConnector;
	//
	// // tier4
	// protected MissionControlConnector missionControlConnector;

	public void start(BundleContext bundleContext) {
		// tier1
		this.userRegistryManager = new UserRegistryManager();
		this.userRegistryManager.start(bundleContext);

		// this.userRegistryConnector = new UserRegistryConnector();
		// this.userRegistryConnector.start(bundleContext);
		//
		// this.authConnector = new AuthConnector();
		// this.authConnector.start(bundleContext);
		//
		// // tier2
		// this.appStoreConnector = new AppStoreConnector();
		// this.appStoreConnector.start(bundleContext);
		//
		// // tier3
		// this.domainServiceConnector = new DomainManagementConnector();
		// this.domainServiceConnector.start(bundleContext);
		//
		// this.transferAgentConnector = new NodeControlConnector();
		// this.transferAgentConnector.start(bundleContext);
		//
		// // tier4
		// this.missionControlConnector = new MissionControlConnector();
		// this.missionControlConnector.start(bundleContext);
	}

	public void stop(BundleContext bundleContext) {
		// // tier4
		// if (this.missionControlConnector != null) {
		// this.missionControlConnector.stop(bundleContext);
		// this.missionControlConnector = null;
		// }
		//
		// // tier3
		// if (this.domainServiceConnector != null) {
		// this.domainServiceConnector.stop(bundleContext);
		// this.domainServiceConnector = null;
		// }
		//
		// if (this.transferAgentConnector != null) {
		// this.transferAgentConnector.stop(bundleContext);
		// this.transferAgentConnector = null;
		// }
		//
		// // tier2
		// if (this.appStoreConnector != null) {
		// this.appStoreConnector.stop(bundleContext);
		// this.appStoreConnector = null;
		// }
		//
		// // tier1
		// if (this.userRegistryConnector != null) {
		// this.userRegistryConnector.stop(bundleContext);
		// this.userRegistryConnector = null;
		// }
		//
		// if (this.authConnector != null) {
		// this.authConnector.stop(bundleContext);
		// this.authConnector = null;
		// }

		if (this.userRegistryManager != null) {
			this.userRegistryManager.stop(bundleContext);
			this.userRegistryManager = null;
		}
	}

}

// protected OAuth2ConnectorImpl oauth2Connector;
// protected AuthConnectorLoadBalanceImpl authConnector;

// this.authConnector = new AuthConnectorLoadBalanceImpl(indexServiceLoadBalancer.createLoadBalancableIndexService());
// this.authConnector.start(bundleContext);

// this.oauth2Connector = new OAuth2ConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
// this.oauth2Connector.start(bundleContext);

// if (this.oauth2Connector != null) {
// this.oauth2Connector.stop();
// this.oauth2Connector = null;
// }

// this.configRegistryConnector = new ConfigRegistryConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
// this.configRegistryConnector.start(bundleContext);

// if (this.configRegistryConnector != null) {
// this.configRegistryConnector.stop();
// this.configRegistryConnector = null;
// }
