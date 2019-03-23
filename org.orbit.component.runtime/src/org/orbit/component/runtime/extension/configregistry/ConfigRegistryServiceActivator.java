package org.orbit.component.runtime.extension.configregistry;

import java.util.Map;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryService;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceImpl;
import org.orbit.platform.sdk.IProcessContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;

public class ConfigRegistryServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.ConfigRegistryServiceActivator";

	public static ConfigRegistryServiceActivator INSTANCE = new ConfigRegistryServiceActivator();

	@Override
	public void start(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start ConfigRegistryService
		ConfigRegistryServiceImpl configRegistryService = new ConfigRegistryServiceImpl(properties);
		configRegistryService.start(bundleContext);

		process.adapt(ConfigRegistryService.class, configRegistryService);
	}

	@Override
	public void stop(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ConfigRegistryService
		ConfigRegistryService configRegistry = process.getAdapter(ConfigRegistryService.class);
		if (configRegistry instanceof LifecycleAware) {
			((LifecycleAware) configRegistry).stop(bundleContext);
		}
	}

}
