package org.orbit.component.runtime.extensions;

import org.orbit.component.runtime.tier2.appstore.service.AppStoreServiceDatabaseImpl;
import org.orbit.os.runtime.api.ServiceControl;
import org.osgi.framework.BundleContext;

public class AppStoreServiceControl implements ServiceControl {

	public static AppStoreServiceControl INSTANCE = new AppStoreServiceControl();

	protected AppStoreServiceDatabaseImpl appStoreService;

	@Override
	public void start(BundleContext bundleContext) {
		AppStoreServiceDatabaseImpl appStoreService = new AppStoreServiceDatabaseImpl();
		appStoreService.start(bundleContext);
		this.appStoreService = appStoreService;
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.appStoreService != null) {
			this.appStoreService.stop();
			this.appStoreService = null;
		}
	}

}
