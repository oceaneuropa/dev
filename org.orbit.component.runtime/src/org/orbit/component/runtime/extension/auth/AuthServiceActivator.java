package org.orbit.component.runtime.extension.auth;

import java.util.Map;

import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.orbit.component.runtime.tier1.auth.service.AuthServiceImpl;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.origin.common.service.ILifecycle;
import org.orbit.platform.sdk.IProcess;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
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
		if (authService instanceof ILifecycle) {
			((ILifecycle) authService).stop(bundleContext);
		}
	}

}
