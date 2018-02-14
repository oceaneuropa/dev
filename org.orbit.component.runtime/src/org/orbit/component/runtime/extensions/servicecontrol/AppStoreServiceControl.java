package org.orbit.component.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier2.appstore.service.AppStoreServiceDatabaseImpl;
import org.orbit.platform.sdk.servicecontrol.ServiceControlImpl;
import org.osgi.framework.BundleContext;

public class AppStoreServiceControl extends ServiceControlImpl {

	public static AppStoreServiceControl INSTANCE = new AppStoreServiceControl();

	protected AppStoreServiceDatabaseImpl appStoreService;

	@Override
	public void start(BundleContext bundleContext, Map<String, Object> properties) {
		AppStoreServiceDatabaseImpl appStoreService = new AppStoreServiceDatabaseImpl();
		appStoreService.start(bundleContext);
		this.appStoreService = appStoreService;
	}

	@Override
	public void stop(BundleContext bundleContext, Map<String, Object> properties) {
		if (this.appStoreService != null) {
			this.appStoreService.stop();
			this.appStoreService = null;
		}
	}

}
