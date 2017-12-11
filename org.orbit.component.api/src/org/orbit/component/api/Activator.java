package org.orbit.component.api;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		Activator.instance = null;
	}

}

// protected IndexServiceLoadBalancer indexServiceLoadBalancer;

// // tier1
// protected UserRegistryConnectorAdapter userRegistryConnectorAdapter;
// protected AuthConnectorAdapter authConnectorAdapter;
// protected ConfigRegistryConnectorAdapter configRegistryConnectorAdapter;
// // tier2
// protected AppStoreConnectorAdapter appStoreConnectorAdapter;
// // tier3
// protected DomainManagementConnectorAdapter domainManagementConnectorAdapter;
// protected TransferAgentConnectorAdapter transferAgentConnectorAdapter;

// // Start tracking connectors
// // tier1
// this.userRegistryConnectorAdapter = new UserRegistryConnectorAdapter();
// this.userRegistryConnectorAdapter.start(bundleContext);
//
// this.authConnectorAdapter = new AuthConnectorAdapter();
// this.authConnectorAdapter.start(bundleContext);
//
// this.configRegistryConnectorAdapter = new ConfigRegistryConnectorAdapter();
// this.configRegistryConnectorAdapter.start(bundleContext);
//
// // tier2
// this.appStoreConnectorAdapter = new AppStoreConnectorAdapter();
// this.appStoreConnectorAdapter.start(bundleContext);
//
// // tier3
// this.domainManagementConnectorAdapter = new DomainManagementConnectorAdapter();
// this.domainManagementConnectorAdapter.start(bundleContext);
//
// this.transferAgentConnectorAdapter = new TransferAgentConnectorAdapter();
// this.transferAgentConnectorAdapter.start(bundleContext);

// // Stop connector service trackers
// // tier3
// if (this.domainManagementConnectorAdapter != null) {
// this.domainManagementConnectorAdapter.stop(bundleContext);
// this.domainManagementConnectorAdapter = null;
// }
//
// if (this.transferAgentConnectorAdapter != null) {
// this.transferAgentConnectorAdapter.stop(bundleContext);
// this.transferAgentConnectorAdapter = null;
// }
//
// // tier2
// if (this.appStoreConnectorAdapter != null) {
// this.appStoreConnectorAdapter.stop(bundleContext);
// this.appStoreConnectorAdapter = null;
// }
//
// // tier1
// if (this.userRegistryConnectorAdapter != null) {
// this.userRegistryConnectorAdapter.stop(bundleContext);
// this.userRegistryConnectorAdapter = null;
// }
//
// if (this.authConnectorAdapter != null) {
// this.authConnectorAdapter.stop(bundleContext);
// this.authConnectorAdapter = null;
// }
//
// if (this.configRegistryConnectorAdapter != null) {
// this.configRegistryConnectorAdapter.stop(bundleContext);
// this.configRegistryConnectorAdapter = null;
// }

//// Get IndexProvider load balancer
//// load properties from accessing index service
// Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, indexProviderProps, OrbitConstants.COMPONENT_INDEX_SERVICE_URL);
// this.indexServiceLoadBalancer = IndexServiceUtil.getIndexServiceLoadBalancer(indexProviderProps);

// if (this.indexServiceConnectorAdapter != null) {
// this.indexServiceConnectorAdapter.stop(bundleContext);
// this.indexServiceConnectorAdapter = null;
// }

// protected ServiceTracker<OAuth2Connector, OAuth2Connector> oauth2ConnectorTracker;

// public OAuth2Connector getOAuth2Connector() {
// OAuth2Connector connector = null;
// if (this.oauth2ConnectorTracker != null) {
// connector = this.oauth2ConnectorTracker.getService();
// }
// return connector;
// }

// Stop OAuth2Connector ServiceTracker
// if (this.oauth2ConnectorTracker != null) {
// this.oauth2ConnectorTracker.close();
// this.oauth2ConnectorTracker = null;
// }

// Start ServiceTracker for tracking OAuth2Connector service
// this.oauth2ConnectorTracker = new ServiceTracker<OAuth2Connector, OAuth2Connector>(bundleContext, OAuth2Connector.class, new
// ServiceTrackerCustomizer<OAuth2Connector, OAuth2Connector>() {
// @Override
// public OAuth2Connector addingService(ServiceReference<OAuth2Connector> reference) {
// if (debug) {
// System.out.println(getClass().getName() + " OAuth2Connector service is added.");
// }
// return bundleContext.getService(reference);
// }
//
// @Override
// public void modifiedService(ServiceReference<OAuth2Connector> reference, OAuth2Connector arg1) {
// if (debug) {
// System.out.println(getClass().getName() + " OAuth2Connector service is modified.");
// }
// }
//
// @Override
// public void removedService(ServiceReference<OAuth2Connector> reference, OAuth2Connector arg1) {
// if (debug) {
// System.out.println(getClass().getName() + " OAuth2Connector service is removed.");
// }
// }
// });

// public UserRegistryConnector getUserRegistryConnector() {
// return this.userRegistryConnectorAdapter != null ? this.userRegistryConnectorAdapter.getConnector() : null;
// }
//
// public AuthConnector getAuthConnector() {
// return this.authConnectorAdapter != null ? this.authConnectorAdapter.getConnector() : null;
// }
//
// public ConfigRegistryConnector getConfigRegistryConnector() {
// return this.configRegistryConnectorAdapter != null ? this.configRegistryConnectorAdapter.getConnector() : null;
// }
//
// public AppStoreConnector getAppStoreConnector() {
// return this.appStoreConnectorAdapter != null ? this.appStoreConnectorAdapter.getConnector() : null;
// }
//
// public DomainManagementConnector getDomainMgmtConnector() {
// return this.domainManagementConnectorAdapter != null ? this.domainManagementConnectorAdapter.getConnector() : null;
// }
//
// public TransferAgentConnector getTransferAgentConnector() {
// return this.transferAgentConnectorAdapter != null ? this.transferAgentConnectorAdapter.getConnector() : null;
// }
