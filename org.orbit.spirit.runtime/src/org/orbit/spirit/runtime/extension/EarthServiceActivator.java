package org.orbit.spirit.runtime.extension;

import java.util.Map;

import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.spirit.runtime.earth.service.EarthService;
import org.orbit.spirit.runtime.earth.service.impl.EarthServiceImpl;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class EarthServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.spirit.runtime.EarthServiceActivator";

	@Override
	public void start(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		EarthServiceImpl service = new EarthServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(EarthService.class, service);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		EarthService service = process.getAdapter(EarthService.class);
		if (service instanceof LifecycleAware) {
			((LifecycleAware) service).stop(bundleContext);
		}
	}

}
