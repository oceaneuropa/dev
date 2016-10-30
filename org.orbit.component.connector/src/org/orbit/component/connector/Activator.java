package org.orbit.component.connector;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.connector.appstore.DistributedAppStoreManager;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected DistributedAppStoreManager appStoreManager;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// -----------------------------------------------------------------------------
		// Global index service
		// -----------------------------------------------------------------------------
		Map<Object, Object> props = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, props, "indexservice.url");

		// -----------------------------------------------------------------------------
		// AppStore connector
		// -----------------------------------------------------------------------------
		// Register AppStoreManager service
		this.appStoreManager = new DistributedAppStoreManager(bundleContext, props);
		this.appStoreManager.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;

		// -----------------------------------------------------------------------------
		// AppStore connector
		// -----------------------------------------------------------------------------
		// Unregister AppStoreManager service
		if (this.appStoreManager != null) {
			this.appStoreManager.stop();
			this.appStoreManager = null;
		}
	}

}
