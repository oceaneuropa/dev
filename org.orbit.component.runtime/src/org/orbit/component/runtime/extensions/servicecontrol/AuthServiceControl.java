package org.orbit.component.runtime.extensions.servicecontrol;

import java.util.Map;

import org.orbit.component.runtime.tier1.auth.service.AuthServiceImpl;
import org.orbit.sdk.ServiceControlImpl;
import org.osgi.framework.BundleContext;

public class AuthServiceControl extends ServiceControlImpl {

	public static AuthServiceControl INSTANCE = new AuthServiceControl();

	protected AuthServiceImpl authService;

	@Override
	public void start(BundleContext bundleContext, Map<String, Object> properties) {
		AuthServiceImpl authService = new AuthServiceImpl();
		authService.start(bundleContext);
		this.authService = authService;
	}

	@Override
	public void stop(BundleContext bundleContext, Map<String, Object> properties) {
		if (this.authService != null) {
			this.authService.stop(bundleContext);
			this.authService = null;
		}
	}

}
