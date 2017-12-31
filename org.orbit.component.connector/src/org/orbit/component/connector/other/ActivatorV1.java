package org.orbit.component.connector.other;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.connector.tier1.account.UserRegistryConnectorImpl;
import org.orbit.component.connector.tier1.account.other.UserRegistryManager;
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

public class ActivatorV1 implements BundleActivator {

	protected static final String USER_REGISTRY_MANAGED_SERVICE_FACTORY_PID = "component.userregistry.manager"; //$NON-NLS-1$

	protected static BundleContext context;
	protected static ActivatorV1 instance;

	public static BundleContext getContext() {
		return context;
	}

	public static ActivatorV1 getInstance() {
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

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		ActivatorV1.context = bundleContext;
		ActivatorV1.instance = this;

		this.indexServiceConnectorAdapter = new IndexServiceConnectorAdapter() {
			@Override
			public void connectorAdded(IndexServiceConnector connector) {
				doStart(ActivatorV1.context, connector);
			}

			@Override
			public void connectorRemoved(IndexServiceConnector connector) {
				doStop(ActivatorV1.context);
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

		ActivatorV1.instance = null;
		ActivatorV1.context = null;
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

		this.userRegistryConnector = new UserRegistryConnectorImpl();
		this.userRegistryConnector.start(bundleContext);

		this.authConnector = new AuthConnectorImpl();
		this.authConnector.start(bundleContext);

		// tier2
		this.appStoreConnector = new AppStoreConnectorImpl();
		this.appStoreConnector.start(bundleContext);

		// tier3
		// this.domainMgmtConnector = new DomainServiceConnectorImpl(indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.domainMgmtConnector = new DomainServiceConnectorImpl();
		this.domainMgmtConnector.start(bundleContext);

		this.transferAgentConnector = new TransferAgentConnectorImpl();
		this.transferAgentConnector.start(bundleContext);
	}

	protected void doStop(BundleContext bundleContext) {
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