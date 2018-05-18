package org.orbit.component.runtime.extension.configregistry;

import java.util.Map;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceDatabaseImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.spi.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class ConfigRegistryServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.ConfigRegistryServiceActivator";

	public static ConfigRegistryServiceActivator INSTANCE = new ConfigRegistryServiceActivator();

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start ConfigRegistryService
		ConfigRegistryServiceDatabaseImpl configRegistryService = new ConfigRegistryServiceDatabaseImpl(properties);
		configRegistryService.start(bundleContext);

		process.adapt(ConfigRegistryService.class, configRegistryService);
	}

	@Override
	public void stop(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ConfigRegistryService
		ConfigRegistryService configRegistry = process.getAdapter(ConfigRegistryService.class);
		if (configRegistry instanceof LifecycleAware) {
			((LifecycleAware) configRegistry).stop(bundleContext);
		}
	}

}
