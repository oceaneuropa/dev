package org.orbit.component.runtime.extensions.serviceactivator;

import java.util.Map;

import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.component.runtime.tier1.auth.service.AuthServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivator;
import org.osgi.framework.BundleContext;

public class AuthServiceActivator implements ServiceActivator {

	public static final String ID = "component.auth.service_activator";

	public static AuthServiceActivator INSTANCE = new AuthServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<String, Object> properties = context.getProperties();

		// Start AuthService
		AuthServiceImpl authService = new AuthServiceImpl(properties);
		authService.start(bundleContext);

		process.adapt(AuthService.class, authService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop AuthService
		AuthService authService = process.getAdapter(AuthService.class);
		if (authService instanceof AuthServiceImpl) {
			((AuthServiceImpl) authService).stop(bundleContext);
		}
	}

}

// @Override
// public String getName() {
// return "AuthService";
// }
