package org.orbit.component.connector;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.connector.tier1.account.UserRegistryConnectorImpl;
import org.orbit.component.connector.tier1.config.ConfigRegistryConnectorImpl;
import org.orbit.component.connector.tier1.session.OAuth2ConnectorImpl;
import org.orbit.component.connector.tier2.appstore.AppStoreConnectorImpl;
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

	protected AppStoreConnectorImpl appStoreConnector;
	protected ConfigRegistryConnectorImpl configRegistryConnector;
	protected UserRegistryConnectorImpl userRegistryConnector;
	protected OAuth2ConnectorImpl oauth2Connector;

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
		// Start connectors
		// -----------------------------------------------------------------------------
		this.appStoreConnector = new AppStoreConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.appStoreConnector.start(bundleContext);

		this.configRegistryConnector = new ConfigRegistryConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.configRegistryConnector.start(bundleContext);

		this.userRegistryConnector = new UserRegistryConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.userRegistryConnector.start(bundleContext);

		this.oauth2Connector = new OAuth2ConnectorImpl(this.indexServiceLoadBalancer.createLoadBalancableIndexService());
		this.oauth2Connector.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		// -----------------------------------------------------------------------------
		// Stop connectors
		// -----------------------------------------------------------------------------
		if (this.appStoreConnector != null) {
			this.appStoreConnector.stop();
			this.appStoreConnector = null;
		}

		if (this.configRegistryConnector != null) {
			this.configRegistryConnector.stop();
			this.configRegistryConnector = null;
		}

		if (this.userRegistryConnector != null) {
			this.userRegistryConnector.stop();
			this.userRegistryConnector = null;
		}

		if (this.oauth2Connector != null) {
			this.oauth2Connector.stop();
			this.oauth2Connector = null;
		}
	}

}
