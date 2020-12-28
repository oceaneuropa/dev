package org.orbit.infra.runtime.extensions.configregistry;

import java.util.Map;

import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.configregistry.service.impl.ConfigRegistryServiceImpl;
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
public class ConfigRegistryServiceActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.ConfigRegistryServiceActivator";

	public static ConfigRegistryServiceActivator INSTANCE = new ConfigRegistryServiceActivator();

	/** ILifecycle */
	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start service
		ConfigRegistryServiceImpl service = new ConfigRegistryServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(ConfigRegistryService.class, service);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop service
		ConfigRegistryService service = process.getAdapter(ConfigRegistryService.class);
		if (service instanceof ILifecycle) {
			((ILifecycle) service).stop(bundleContext);
		}
	}

}
