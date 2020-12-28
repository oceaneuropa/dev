package org.orbit.component.runtime.extension.configregistry;

import java.util.Map;

import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceV0;
import org.orbit.component.runtime.tier1.config.service.ConfigRegistryServiceImplV0;
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
public class ConfigRegistryServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.component.runtime.ConfigRegistryServiceActivator";

	public static ConfigRegistryServiceActivator INSTANCE = new ConfigRegistryServiceActivator();

	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start ConfigRegistryService
		ConfigRegistryServiceImplV0 configRegistryService = new ConfigRegistryServiceImplV0(properties);
		configRegistryService.start(bundleContext);

		process.adapt(ConfigRegistryServiceV0.class, configRegistryService);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ConfigRegistryService
		ConfigRegistryServiceV0 configRegistry = process.getAdapter(ConfigRegistryServiceV0.class);
		if (configRegistry instanceof ILifecycle) {
			((ILifecycle) configRegistry).stop(bundleContext);
		}
	}

}
