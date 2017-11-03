package org.orbit.component.connector;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.connector.tier1.account.UserRegistryConnectorImpl;
import org.orbit.component.connector.tier1.auth.AuthConnectorImpl;
import org.orbit.component.connector.tier1.config.ConfigRegistryConnectorImpl;
import org.orbit.component.connector.tier2.appstore.AppStoreConnectorImpl;
import org.orbit.component.connector.tier3.domain.DomainMgmtConnectorImpl;
import org.orbit.component.connector.tier3.transferagent.TransferAgentConnectorFactoryImpl;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.client.OriginConstants;
import org.origin.mgm.client.api.IndexServiceUtil;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalancer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected IndexServiceLoadBalancer indexServiceLoadBalancer;

	protected ConfigRegistryConnectorImpl configRegistryConnector;
	protected UserRegistryConnectorImpl userRegistryConnector;
	// protected OAuth2ConnectorImpl oauth2Connector;
	protected AuthConnectorImpl authConnector;
	protected AppStoreConnectorImpl appStoreConnector;
	protected DomainMgmtConnectorImpl domainMgmtConnector;
	protected TransferAgentConnectorFactoryImpl transferAgentConnectorFactory;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// -----------------------------------------------------------------------------
		// Get load balancer for IndexProvider
		// -----------------------------------------------------------------------------
		// load properties from accessing index service
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OriginConstants.COMPONENT_INDEX_SERVICE_URL_PROP);
		this.indexServiceLoadBalancer = IndexServiceUtil.getIndexServiceLoadBalancer(indexProviderProps);

		// -----------------------------------------------------------------------------
		// Start tier1 service connectors
		// -----------------------------------------------------------------------------
		// this.configRegistryConnector = new ConfigRegistryConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		// this.configRegistryConnector.start(bundleContext);

		// this.userRegistryConnector = new UserRegistryConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		// this.userRegistryConnector.start(bundleContext);

		// this.oauth2Connector = new OAuth2ConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		// this.oauth2Connector.start(bundleContext);

		this.authConnector = new AuthConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.authConnector.start(bundleContext);

		// -----------------------------------------------------------------------------
		// Start tier2 service connectors
		// -----------------------------------------------------------------------------
		// this.appStoreConnector = new AppStoreConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		// this.appStoreConnector.start(bundleContext);

		// -----------------------------------------------------------------------------
		// Start tier3 service connectors
		// -----------------------------------------------------------------------------
		this.domainMgmtConnector = new DomainMgmtConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.domainMgmtConnector.start(bundleContext);

		this.transferAgentConnectorFactory = new TransferAgentConnectorFactoryImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.transferAgentConnectorFactory.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// -----------------------------------------------------------------------------
		// Stop tier3 service connectors
		// -----------------------------------------------------------------------------
		if (this.domainMgmtConnector != null) {
			this.domainMgmtConnector.stop();
			this.domainMgmtConnector = null;
		}

		if (this.transferAgentConnectorFactory != null) {
			this.transferAgentConnectorFactory.stop(bundleContext);
			this.transferAgentConnectorFactory = null;
		}

		// -----------------------------------------------------------------------------
		// Stop tier2 service connectors
		// -----------------------------------------------------------------------------
		// if (this.appStoreConnector != null) {
		// this.appStoreConnector.stop();
		// this.appStoreConnector = null;
		// }

		// -----------------------------------------------------------------------------
		// Stop tier1 service connectors
		// -----------------------------------------------------------------------------
		// if (this.userRegistryConnector != null) {
		// this.userRegistryConnector.stop();
		// this.userRegistryConnector = null;
		// }

		// if (this.configRegistryConnector != null) {
		// this.configRegistryConnector.stop();
		// this.configRegistryConnector = null;
		// }

		// if (this.oauth2Connector != null) {
		// this.oauth2Connector.stop();
		// this.oauth2Connector = null;
		// }

		if (this.authConnector != null) {
			this.authConnector.stop();
			this.authConnector = null;
		}

		Activator.context = null;
	}

}
