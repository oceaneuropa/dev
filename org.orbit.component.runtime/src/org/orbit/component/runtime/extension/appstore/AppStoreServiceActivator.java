package org.orbit.component.runtime.extension.appstore;

import java.util.Map;

import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreServiceImpl;
import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class AppStoreServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.AppStoreServiceActivator";

	public static AppStoreServiceActivator INSTANCE = new AppStoreServiceActivator();

	@Override
	public void start(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start AppStoreService
		AppStoreServiceImpl appStore = new AppStoreServiceImpl(properties);
		appStore.start(bundleContext);

		process.adapt(AppStoreService.class, appStore);
	}

	@Override
	public void stop(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop AppStoreService
		AppStoreService appStore = process.getAdapter(AppStoreService.class);
		if (appStore instanceof LifecycleAware) {
			((LifecycleAware) appStore).stop(bundleContext);
		}
	}

}
