package org.orbit.component.runtime.extension.identity;

import java.util.Map;

import org.orbit.component.runtime.tier1.identity.service.IdentityService;
import org.orbit.component.runtime.tier1.identity.service.IdentityServiceImpl;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ProcessContext;
import org.orbit.platform.sdk.ServiceActivator;
import org.origin.common.service.ILifecycle;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IdentityServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.IdentityServiceActivator";

	public static IdentityServiceActivator INSTANCE = new IdentityServiceActivator();

	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start IdentityService
		IdentityServiceImpl identityService = new IdentityServiceImpl(properties);
		identityService.start(bundleContext);

		process.adapt(IdentityService.class, identityService);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop IdentityService
		IdentityService identityService = process.getAdapter(IdentityService.class);
		if (identityService instanceof ILifecycle) {
			((ILifecycle) identityService).stop(bundleContext);
		}
	}

}
