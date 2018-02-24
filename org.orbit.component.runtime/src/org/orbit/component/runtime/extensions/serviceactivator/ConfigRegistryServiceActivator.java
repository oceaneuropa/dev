package org.orbit.component.runtime.extensions.serviceactivator;

import java.util.Map;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceDatabaseImpl;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.ServiceActivatorImpl;
import org.osgi.framework.BundleContext;

public class ConfigRegistryServiceActivator extends ServiceActivatorImpl {

	public static final String ID = "component.config_registry.service_activator";

	public static ConfigRegistryServiceActivator INSTANCE = new ConfigRegistryServiceActivator();

	@Override
	public String getProcessName() {
		return "ConfigRegistry";
	}

	@Override
	public void start(IPlatformContext context, IProcess process) {
		BundleContext bundleContext = context.getBundleContext();
		Map<String, Object> properties = context.getProperties();

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
		if (configRegistry instanceof ConfigRegistryServiceDatabaseImpl) {
			((ConfigRegistryServiceDatabaseImpl) configRegistry).stop(bundleContext);
		}
	}

}