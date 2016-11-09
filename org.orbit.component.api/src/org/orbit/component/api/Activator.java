package org.orbit.component.api;

import org.orbit.component.api.appstore.AppStoreManager;
import org.orbit.component.api.configregistry.ConfigRegistryManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	protected ServiceTracker<AppStoreManager, AppStoreManager> appStoreServiceTracker;
	protected ServiceTracker<ConfigRegistryManager, ConfigRegistryManager> configRegistryServiceTracker;
	protected boolean debug = true;

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;

		// Open ServiceTracker for tracking AppStoreManager service
		this.appStoreServiceTracker = new ServiceTracker<AppStoreManager, AppStoreManager>(bundleContext, AppStoreManager.class, new ServiceTrackerCustomizer<AppStoreManager, AppStoreManager>() {
			@Override
			public AppStoreManager addingService(ServiceReference<AppStoreManager> reference) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator AppStoreManager service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<AppStoreManager> reference, AppStoreManager appStoreManager) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator AppStoreManager service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<AppStoreManager> reference, AppStoreManager appStoreManager) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator AppStoreManager service is removed.");
				}
			}
		});
		this.appStoreServiceTracker.open();

		// Open ServiceTracker for tracking ConfigRegistryManager service
		this.configRegistryServiceTracker = new ServiceTracker<ConfigRegistryManager, ConfigRegistryManager>(bundleContext, ConfigRegistryManager.class, new ServiceTrackerCustomizer<ConfigRegistryManager, ConfigRegistryManager>() {
			@Override
			public ConfigRegistryManager addingService(ServiceReference<ConfigRegistryManager> reference) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator ConfigRegistryManager service is added.");
				}
				return bundleContext.getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<ConfigRegistryManager> reference, ConfigRegistryManager appStoreManager) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator ConfigRegistryManager service is modified.");
				}
			}

			@Override
			public void removedService(ServiceReference<ConfigRegistryManager> reference, ConfigRegistryManager appStoreManager) {
				if (debug) {
					System.out.println("org.orbit.component.api.Activator ConfigRegistryManager service is removed.");
				}
			}
		});
		this.configRegistryServiceTracker.open();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.instance = null;
		Activator.context = null;

		// Close ServiceTracker for tracking AppStoreManager service
		if (this.appStoreServiceTracker != null) {
			this.appStoreServiceTracker.close();
			this.appStoreServiceTracker = null;
		}

		// Close ServiceTracker for tracking ConfigRegistryManager service
		if (this.configRegistryServiceTracker != null) {
			this.configRegistryServiceTracker.close();
			this.configRegistryServiceTracker = null;
		}
	}

	public AppStoreManager getAppStoreManager() {
		AppStoreManager appStoreManager = null;
		if (this.appStoreServiceTracker != null) {
			appStoreManager = this.appStoreServiceTracker.getService();
		}
		return appStoreManager;
	}

	public ConfigRegistryManager getConfigRegistryManager() {
		ConfigRegistryManager configRegistryManager = null;
		if (this.configRegistryServiceTracker != null) {
			configRegistryManager = this.configRegistryServiceTracker.getService();
		}
		return configRegistryManager;
	}

}
