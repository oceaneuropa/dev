package org.orbit.component.runtime.extension.appstore;

import java.util.Map;

import org.orbit.component.runtime.tier2.appstore.service.AppStoreService;
import org.orbit.component.runtime.tier2.appstore.service.AppStoreServiceImpl;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

public class AppStoreServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.AppStoreServiceActivator";

	public static AppStoreServiceActivator INSTANCE = new AppStoreServiceActivator();

	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		AppStoreService appStore = new AppStoreServiceImpl(properties);
		appStore.start(bundleContext);

		process.adapt(AppStoreService.class, appStore);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		AppStoreService appStore = process.getAdapter(AppStoreService.class);
		if (appStore != null) {
			BundleContext bundleContext = context.getBundleContext();
			appStore.stop(bundleContext);

			process.adapt(AppStoreService.class, null);
		}
	}

}
