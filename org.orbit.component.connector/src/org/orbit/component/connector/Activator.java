package org.orbit.component.connector;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.connector.appstore.AppStoreManagerImpl;
import org.orbit.component.connector.configregistry.ConfigRegistryManagerImpl;
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

	protected AppStoreManagerImpl appStoreManager;
	protected ConfigRegistryManagerImpl configRegistryManager;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// -----------------------------------------------------------------------------
		// IndexProvider
		// -----------------------------------------------------------------------------
		// load properties from accessing index service
		Map<Object, Object> indexProviderProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, indexProviderProps, OriginConstants.COMPONENT_INDEX_SERVICE_URL_PROP);
		this.indexServiceLoadBalancer = IndexServiceUtil.getIndexServiceLoadBalancer(indexProviderProps);

		// -----------------------------------------------------------------------------
		// Start service managers
		// -----------------------------------------------------------------------------
		this.appStoreManager = new AppStoreManagerImpl(bundleContext, this.indexServiceLoadBalancer);
		this.appStoreManager.start();

		this.configRegistryManager = new ConfigRegistryManagerImpl(bundleContext, this.indexServiceLoadBalancer);
		this.configRegistryManager.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		// -----------------------------------------------------------------------------
		// Stop service managers
		// -----------------------------------------------------------------------------
		if (this.appStoreManager != null) {
			this.appStoreManager.stop();
			this.appStoreManager = null;
		}

		if (this.configRegistryManager != null) {
			this.configRegistryManager.stop();
			this.configRegistryManager = null;
		}
	}

}
