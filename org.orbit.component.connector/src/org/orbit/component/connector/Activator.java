package org.orbit.component.connector;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.cli.AppStoreCommand;
import org.orbit.component.cli.AuthCommand;
import org.orbit.component.cli.DomainServiceCommand;
import org.orbit.component.cli.ServicesCommand;
import org.orbit.component.cli.TransferAgentCommand;
import org.orbit.component.cli.UserRegistryCommand;
import org.orbit.component.connector.tier1.account.UserRegistryConnectorImpl;
import org.orbit.component.connector.tier1.account.UserRegistryManager;
import org.orbit.component.connector.tier1.auth.AuthConnectorImpl;
import org.orbit.component.connector.tier1.config.ConfigRegistryConnectorImpl;
import org.orbit.component.connector.tier2.appstore.AppStoreConnectorImpl;
import org.orbit.component.connector.tier3.domain.DomainServiceConnectorImpl;
import org.orbit.component.connector.tier3.transferagent.TransferAgentConnectorImpl;
import org.orbit.infra.api.indexes.IndexServiceConnector;
import org.orbit.infra.api.indexes.IndexServiceConnectorAdapter;
import org.orbit.infra.api.indexes.IndexServiceLoadBalancer;
import org.orbit.infra.api.indexes.IndexServiceUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static final String USER_REGISTRY_MANAGED_SERVICE_FACTORY_PID = "component.userregistry.manager"; //$NON-NLS-1$

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected IndexServiceConnectorAdapter indexServiceConnectorAdapter;

	// ManagedServiceFactory
	protected UserRegistryManager userRegistryManager;

	// Connectors
	protected ConfigRegistryConnectorImpl configRegistryConnector;
	protected UserRegistryConnectorImpl userRegistryConnector;
	protected AuthConnectorImpl authConnector;
	protected AppStoreConnectorImpl appStoreConnector;
	protected DomainServiceConnectorImpl domainMgmtConnector;
	protected TransferAgentConnectorImpl transferAgentConnector;

	// Commands
	protected ServicesCommand servicesCommand;
	protected AuthCommand authCommand;
	protected UserRegistryCommand userRegistryCommand;
	protected AppStoreCommand appStoreCommand;
	protected DomainServiceCommand domainMgmtCommand;
	protected TransferAgentCommand transferAgentCommand;

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;

		this.indexServiceConnectorAdapter = new IndexServiceConnectorAdapter() {
			@Override
			public void connectorAdded(IndexServiceConnector connector) {
				doStart(Activator.context, connector);
			}

			@Override
			public void connectorRemoved(IndexServiceConnector connector) {
				doStop(Activator.context);
			}
		};
		this.indexServiceConnectorAdapter.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.indexServiceConnectorAdapter != null) {
			this.indexServiceConnectorAdapter.stop(bundleContext);
			this.indexServiceConnectorAdapter = null;
		}

		Activator.instance = null;
		Activator.context = null;
	}

	protected void doStart(BundleContext bundleContext, IndexServiceConnector connector) {
		// Get load balancer for IndexProvider
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, org.orbit.infra.api.OrbitConstants.COMPONENT_INDEX_SERVICE_URL_PROP);
		IndexServiceLoadBalancer indexServiceLoadBalancer = IndexServiceUtil.getIndexServiceLoadBalancer(connector, indexProviderProps);

		// Register ManagedServiceFactories and connector services
		// tier1
		this.userRegistryManager = new UserRegistryManager();
		this.userRegistryManager.start(bundleContext);

		this.userRegistryConnector = new UserRegistryConnectorImpl(indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.userRegistryConnector.start(bundleContext);

		this.authConnector = new AuthConnectorImpl();
		this.authConnector.start(bundleContext);

		// tier2
		this.appStoreConnector = new AppStoreConnectorImpl(indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.appStoreConnector.start(bundleContext);

		// tier3
		this.domainMgmtConnector = new DomainServiceConnectorImpl(indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.domainMgmtConnector.start(bundleContext);

		this.transferAgentConnector = new TransferAgentConnectorImpl();
		this.transferAgentConnector.start(bundleContext);

		// Start commands
		this.servicesCommand = new ServicesCommand(bundleContext, indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.servicesCommand.start();

		this.authCommand = new AuthCommand();
		this.authCommand.start(bundleContext);

		this.userRegistryCommand = new UserRegistryCommand(bundleContext);
		this.userRegistryCommand.start();

		this.appStoreCommand = new AppStoreCommand(bundleContext);
		this.appStoreCommand.start();

		this.domainMgmtCommand = new DomainServiceCommand(bundleContext);
		this.domainMgmtCommand.start();

		this.transferAgentCommand = new TransferAgentCommand();
		this.transferAgentCommand.start(bundleContext);
	}

	protected void doStop(BundleContext bundleContext) {
		// Stop commands
		if (this.servicesCommand != null) {
			this.servicesCommand.stop();
			this.servicesCommand = null;
		}

		if (this.authCommand != null) {
			this.authCommand.stop(bundleContext);
			this.authCommand = null;
		}

		if (this.userRegistryCommand != null) {
			this.userRegistryCommand.stop();
			this.userRegistryCommand = null;
		}

		if (this.appStoreCommand != null) {
			this.appStoreCommand.stop();
			this.appStoreCommand = null;
		}

		if (this.domainMgmtCommand != null) {
			this.domainMgmtCommand.stop();
			this.domainMgmtCommand = null;
		}

		if (this.transferAgentCommand != null) {
			this.transferAgentCommand.stop(bundleContext);
			this.transferAgentCommand = null;
		}

		// Unregister ManagedServiceFactories and connector services
		// tier3
		if (this.domainMgmtConnector != null) {
			this.domainMgmtConnector.stop(bundleContext);
			this.domainMgmtConnector = null;
		}

		if (this.transferAgentConnector != null) {
			this.transferAgentConnector.stop(bundleContext);
			this.transferAgentConnector = null;
		}

		// tier2
		if (this.appStoreConnector != null) {
			this.appStoreConnector.stop(bundleContext);
			this.appStoreConnector = null;
		}

		// tier1
		if (this.userRegistryConnector != null) {
			this.userRegistryConnector.stop(bundleContext);
			this.userRegistryConnector = null;
		}

		if (this.authConnector != null) {
			this.authConnector.stop(bundleContext);
			this.authConnector = null;
		}

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
