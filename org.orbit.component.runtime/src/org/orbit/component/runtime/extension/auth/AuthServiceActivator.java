package org.orbit.component.runtime.extension.auth;

import java.util.Map;

import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.component.runtime.tier1.auth.service.AuthServiceImpl;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.IProcess;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class AuthServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.AuthServiceActivator";

	public static AuthServiceActivator INSTANCE = new AuthServiceActivator();

	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start AuthService
		AuthServiceImpl authService = new AuthServiceImpl(properties);
		authService.start(bundleContext);

		process.adapt(AuthService.class, authService);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop AuthService
		AuthService authService = process.getAdapter(AuthService.class);
		if (authService instanceof LifecycleAware) {
			((LifecycleAware) authService).stop(bundleContext);
		}
	}

}
