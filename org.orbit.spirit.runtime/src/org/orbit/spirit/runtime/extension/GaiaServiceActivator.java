package org.orbit.spirit.runtime.extension;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.spirit.runtime.gaia.service.GaiaService;
import org.orbit.spirit.runtime.gaia.service.impl.GaiaServiceImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class GaiaServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.spirit.runtime.GaiaServiceActivator";

	@Override
	public void start(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		GaiaServiceImpl service = new GaiaServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(GaiaService.class, service);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		GaiaService service = process.getAdapter(GaiaService.class);
		if (service instanceof LifecycleAware) {
			((LifecycleAware) service).stop(bundleContext);
		}
	}

}
