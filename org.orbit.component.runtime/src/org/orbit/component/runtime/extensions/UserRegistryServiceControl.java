package org.orbit.component.runtime.extensions;

import org.orbit.component.runtime.tier1.account.service.UserRegistryServiceImpl;
import org.orbit.os.runtime.api.ServiceControl;
import org.osgi.framework.BundleContext;

public class UserRegistryServiceControl implements ServiceControl {

	public static UserRegistryServiceControl INSTANCE = new UserRegistryServiceControl();

	protected UserRegistryServiceImpl userRegistryService;

	@Override
	public void start(BundleContext bundleContext) {
		UserRegistryServiceImpl userRegistryService = new UserRegistryServiceImpl();
		userRegistryService.start(bundleContext);
		this.userRegistryService = userRegistryService;
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.userRegistryService != null) {
			this.userRegistryService.stop(bundleContext);
			this.userRegistryService = null;
		}
	}

}
