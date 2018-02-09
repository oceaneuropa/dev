package org.orbit.component.runtime.extensions;

import org.orbit.component.runtime.tier1.auth.service.AuthServiceImpl;
import org.orbit.os.runtime.api.ServiceControl;
import org.osgi.framework.BundleContext;

public class AuthServiceControl implements ServiceControl {

	public static AuthServiceControl INSTANCE = new AuthServiceControl();

	protected AuthServiceImpl authService;

	@Override
	public void start(BundleContext bundleContext) {
		AuthServiceImpl authService = new AuthServiceImpl();
		authService.start(bundleContext);
		this.authService = authService;
	}

	@Override
	public void stop(BundleContext bundleContext) {
		if (this.authService != null) {
			this.authService.stop(bundleContext);
			this.authService = null;
		}
	}

}
