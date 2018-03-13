package org.orbit.component.runtime.extensions.serviceactivator;

import java.util.Map;

import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreServiceDatabaseImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

public class AppStoreServiceActivator implements ServiceActivator {

	public static final String ID = "component.app_store.service_activator";

	public static AppStoreServiceActivator INSTANCE = new AppStoreServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start AppStoreService
		AppStoreServiceDatabaseImpl appStore = new AppStoreServiceDatabaseImpl(properties);
		appStore.start(bundleContext);

		process.adapt(AppStoreService.class, appStore);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop AppStoreService
		AppStoreService appStore = process.getAdapter(AppStoreService.class);
		if (appStore instanceof AppStoreServiceDatabaseImpl) {
			((AppStoreServiceDatabaseImpl) appStore).stop(bundleContext);
		}
	}

}

// @Override
// public String getName() {
// return "AppStore";
// }
