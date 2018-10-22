package org.orbit.infra.runtime.extensions.configregistry;

import java.util.Map;

import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.configregistry.service.impl.ConfigRegistryServiceImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class ConfigRegistryServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.ConfigRegistryServiceActivator";

	public static ConfigRegistryServiceActivator INSTANCE = new ConfigRegistryServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		ConfigRegistryServiceImpl service = new ConfigRegistryServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(ConfigRegistryService.class, service);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		ConfigRegistryService service = process.getAdapter(ConfigRegistryService.class);
		if (service instanceof LifecycleAware) {
			((LifecycleAware) service).stop(bundleContext);
		}
	}

}
