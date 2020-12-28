package org.orbit.infra.runtime.extensions.extensionregistry;

import java.util.Map;

import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryServiceImpl;
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
public class ExtensionRegistryActivator implements ServiceActivator {

	public static final String ID = "org.orbit.infra.runtime.ExtensionRegistryActivator";

	public static ExtensionRegistryActivator INSTANCE = new ExtensionRegistryActivator();

	/** ILifecycle */
	@Override
	public void start(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();
		Map<Object, Object> properties = context.getProperties();

		// Start ExtensionRegistryService
		ExtensionRegistryServiceImpl service = new ExtensionRegistryServiceImpl(properties);
		service.start(bundleContext);

		process.adapt(ExtensionRegistryService.class, service);
	}

	@Override
	public void stop(ProcessContext context, IProcess process) throws Exception {
		BundleContext bundleContext = context.getBundleContext();

		// Stop ExtensionRegistryService
		ExtensionRegistryService service = process.getAdapter(ExtensionRegistryService.class);
		if (service instanceof ILifecycle) {
			((ILifecycle) service).stop(bundleContext);
		}
	}

}
