package org.orbit.component.runtime.extension.configregistry;

import java.util.Map;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceImplV0;
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
		ConfigRegistryServiceImplV0 configRegistryService = new ConfigRegistryServiceImplV0(properties);
		configRegistryService.start(bundleContext);

		process.adapt(ConfigRegistryServiceV0.class, configRegistryService);
	}

	@Override
	public void stop(IProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ConfigRegistryService
		ConfigRegistryServiceV0 configRegistry = process.getAdapter(ConfigRegistryServiceV0.class);
		if (configRegistry instanceof LifecycleAware) {
			((LifecycleAware) configRegistry).stop(bundleContext);
		}
	}

}
